package pl.ark.chr.buginator.app.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ark.chr.buginator.app.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.commons.util.Pair;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.repository.auth.UserApplicationRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;
import pl.ark.chr.buginator.util.TestObjectCreator;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @InjectMocks
    private UserApplicationService userApplicationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserApplicationRepository userApplicationRepository;

    static final PaymentOption paymentOption = TestObjectCreator.createPaymentOption("Trial");
    static final Company company = TestObjectCreator.createCompany(paymentOption);

    @Test
    @DisplayName("should get all applications linked to user")
    void shouldGetAllApplicationsLinkedToUsed() {
        //given
        var role = Role.getRole(Role.MANAGER);

        String testUserEmail = "testMail@gmail.com";
        String testUserEmail2 = "test2Mail@gmail.com";
        var user = TestObjectCreator.createUser(company, role, testUserEmail);

        Set<UserApplication> userApps = new HashSet<>();

        Pair<Application, UserApplication> testApp1 = TestObjectCreator.createApplicationForUser("TestApp1", company, user);
        testApp1._1.setId(1L);

        Stream
                .iterate(testApp1, pair -> {
                            Pair<Application, UserApplication> userApp =
                                    TestObjectCreator.createApplicationForUser("TestApp" + (pair._1.getId() + 1L), company, user);
                            userApp._1.setId(pair._1.getId() + 1L);
                            return userApp;
                        }
                )
                .limit(5)
                .forEach(pair -> userApps.add(pair._2));

        doReturn(userApps.stream()).when(userApplicationRepository).findByPk_User_Email(eq(testUserEmail));
        doReturn(Stream.empty()).when(userApplicationRepository).findByPk_User_Email(eq(testUserEmail2));

        //when
        Set<UserApplicationDTO> user1Apps = userApplicationService.getAllForUser(testUserEmail);
        Set<UserApplicationDTO> user2Apps = userApplicationService.getAllForUser(testUserEmail2);

        //then
        assertThat(user1Apps)
                .isNotNull()
                .hasSize(5)
                .extracting(BaseApplicationDTO::getName)
                .containsOnly("TestApp1", "TestApp2", "TestApp3", "TestApp4", "TestApp5");
        assertThat(user1Apps)
                .extracting(BaseApplicationDTO::getId)
                .containsOnly(1L, 2L, 3L, 4L, 5L);
        assertThat(user1Apps)
                .extracting(UserApplicationDTO::isModify)
                .containsOnly(true);
        assertThat(user2Apps)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when getting user apps for null email")
    void shouldThrowNPEWhenPassingNullEmail() {
        //when
        Executable codeUnderException = () -> userApplicationService.getAllForUser(null);

        //then
        assertThrows(NullPointerException.class, codeUnderException);
    }

    @Test
    @DisplayName("should link application to user with modify permission")
    void shouldLinkApplicationToUser() {
        //given
        String appName = "testApp";
        var application = TestObjectCreator.createApplication(appName, company);
        application.setId(3L);

        String testUserEmail = "testMail@gmail.com";
        LoggedUserDTO loggedUser = TestObjectCreator.loggedUser(testUserEmail, 1L);

        var role = Role.getRole(Role.MANAGER);
        doReturn(Optional.of(TestObjectCreator.createUser(company, role, testUserEmail)))
                .when(userRepository).findByEmail(eq(testUserEmail));

        //when
        UserApplicationDTO userApplication = userApplicationService.linkApplicationToUser(application, loggedUser);

        //then
        assertThat(userApplication).isNotNull();
        assertThat(userApplication.getName()).isEqualTo(appName);
        assertThat(userApplication.getId()).isEqualTo(application.getId());
        assertThat(userApplication.isModify()).isTrue();
    }

    @ParameterizedTest(name = "User: {0}, App: {1}")
    @MethodSource("linkApplicationToUserProvider")
    @DisplayName("should not allow nulls when linking app to user")
    void shouldNotAllowNullsWhenLinkingApp(LoggedUserDTO loggedUser, Application application) {
        //when
        Executable codeUnderException = () -> userApplicationService.linkApplicationToUser(application, loggedUser);

        //then
        assertThrows(NullPointerException.class, codeUnderException);
    }

    private static Stream<Arguments> linkApplicationToUserProvider() {
        return Stream.of(
                Arguments.of(null, TestObjectCreator.createApplication("TestApp", company)),
                Arguments.of(TestObjectCreator.loggedUser("test@gmail.com", 1L), null)
        );
    }

    @Test
    @DisplayName("should not allow to link application when no user found for given email")
    void shouldNotAllowToLinkApplicationWhenNoUser() {
        //given
        String appName = "testApp";
        var application = TestObjectCreator.createApplication(appName, company);

        String testUserEmail = "testMail@gmail.com";
        LoggedUserDTO loggedUser = TestObjectCreator.loggedUser(testUserEmail, 1L);

        doReturn(Optional.empty()).when(userRepository).findByEmail(eq(testUserEmail));

        //when
        Executable codeUnderException = () -> userApplicationService.linkApplicationToUser(application, loggedUser);

        //then
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, codeUnderException);

        assertThat(dataNotFoundException.getMessage()).isEqualTo("user.notFound");
    }

    @Test
    @DisplayName("should get single application linked to user")
    void shouldGetSingleAppLinkedToUser() {
        //given
        var role = Role.getRole(Role.MANAGER);
        String testUserEmail = "testMail@gmail.com";
        var user = TestObjectCreator.createUser(company, role, testUserEmail);

        String appName = "testApp";
        Pair<Application, UserApplication> testApp = TestObjectCreator.createApplicationForUser(appName, company, user);

        doReturn(Optional.of(testApp._2)).when(userApplicationRepository)
                .findByPk_User_EmailAndPk_Application_Id(eq(testUserEmail), any(Long.class));

        //when
        UserApplicationDTO userApplication = userApplicationService.getForUser(testUserEmail, 1L);

        //then
        assertThat(userApplication).isNotNull();
        assertThat(userApplication.getName()).isEqualTo(appName);
        assertThat(userApplication.isModify()).isTrue();
        assertThat(userApplication.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should throw DataNotFoundException when application is not linked to user")
    void shouldThrowDataNotFoundWhenNoApplicationLinkedToUser() {
        //given
        String testUserEmail = "testMail@gmail.com";
        Long appId = 1L;

        doReturn(Optional.empty()).when(userApplicationRepository)
                .findByPk_User_EmailAndPk_Application_Id(eq(testUserEmail), eq(appId));

        //when
        Executable codeUnderException = () -> userApplicationService.getForUser(testUserEmail, appId);

        //then
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, codeUnderException);

        assertThat(dataNotFoundException.getMessage()).isEqualTo("userApplication.notFound");
    }

    @ParameterizedTest(name = "Email: {0}, AppId: {1}")
    @MethodSource("getForUserProvider")
    @DisplayName("should not allow nulls when getting app linked to user")
    void shouldNotGetAppForUserWhenNull(String email, Long applicationId) {
        //when
        Executable codeUnderException = () -> userApplicationService.getForUser(email, applicationId);

        //then
        assertThrows(NullPointerException.class, codeUnderException);
    }

    private static Stream<Arguments> getForUserProvider() {
        return Stream.of(
                Arguments.of(null, 1L),
                Arguments.of("test@gmail.com", null)
        );
    }
}