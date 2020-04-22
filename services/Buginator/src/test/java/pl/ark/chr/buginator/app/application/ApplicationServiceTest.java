package pl.ark.chr.buginator.app.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.ark.chr.buginator.app.error.ErrorService;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.app.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;
import pl.ark.chr.buginator.util.TestObjectCreator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ApplicationServiceTest {

    @InjectMocks
    private ApplicationService applicationService;

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ErrorService errorService;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private LoggedUserService loggedUserService;
    @Mock
    private UserApplicationService userApplicationService;

    private final String loggedUserEmail = "testEmail@gmail.com";
    private final Long companyId = 1L;
    private final Company company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("Trial"));
    private final LoggedUserDTO loggedUser = TestObjectCreator.loggedUser(loggedUserEmail, companyId);

    @BeforeEach
    void setUp() {
        doReturn(loggedUser).when(loggedUserService).getCurrentUser();
        doReturn(Optional.of(company)).when(companyRepository).findById(eq(companyId));
        when(applicationRepository.save(any(Application.class))).then(returnsFirstArg());
    }

    @Test
    @DisplayName("should create application and correctly link it to logged used")
    void shouldCorrectlyCreateApplication() {
        //given
        final String appName = "TestApp1";

        doReturn(Optional.empty()).when(applicationRepository).findByNameAndCompany(eq(appName), eq(company));

        doAnswer(invocationOnMock -> {
            Application app = invocationOnMock.getArgument(0);
            LoggedUserDTO loggedUser = invocationOnMock.getArgument(1);

            assertThat(app.getName()).isEqualTo(appName);
            assertThat(app.getCompany()).isEqualTo(company);

            assertThat(loggedUser.getEmail()).isEqualTo(loggedUserEmail);

            return UserApplicationDTO.builder()
                    .id(1L)
                    .name(app.getName())
                    .modify(true)
                    .build();
        }).when(userApplicationService).linkApplicationToUser(any(Application.class), eq(loggedUser));

        ApplicationRequestDTO applicationRequest = new ApplicationRequestDTO(appName);

        //when
        UserApplicationDTO userApplicationDTO = applicationService.create(applicationRequest);

        //then
        assertThat(userApplicationDTO.getName()).isEqualTo(appName);
        assertThat(userApplicationDTO.isModify()).isTrue();
    }

    @Test
    @DisplayName("should throw DuplicateException when creating app with existing name for that company")
    void shouldThrowDuplicateExceptionWhenAppCreate() {
        //given
        final String appName = "TestApp1";

        doReturn(Optional.of(TestObjectCreator.createApplication(appName, company)))
                .when(applicationRepository).findByNameAndCompany(eq(appName), eq(company));

        ApplicationRequestDTO applicationRequest = new ApplicationRequestDTO(appName);

        //when
        Executable codeUnderException = () -> applicationService.create(applicationRequest);

        //then
        DuplicateException duplicateException = assertThrows(DuplicateException.class, codeUnderException,
                "should throw DuplicateException");

        assertThat(duplicateException.getMessage()).isEqualTo("application.duplicate");
    }

    @Test
    @DisplayName("should throw data not found exception when no company present")
    void shouldThrowDataNotFoundExceptionWhenNoCompany() {
        //given
        final String appName = "TestApp1";

        doReturn(Optional.empty()).when(companyRepository).findById(eq(companyId));

        ApplicationRequestDTO applicationRequest = new ApplicationRequestDTO(appName);

        //when
        Executable codeUnderException = () -> applicationService.create(applicationRequest);

        //then
        DataNotFoundException companyNotFound = assertThrows(DataNotFoundException.class, codeUnderException,
                "should throw DataNotFoundException");

        assertThat(companyNotFound.getMessage()).isEqualTo("company.notFound");
    }

    @Test
    @DisplayName("should return logged user email as cache key")
    void shouldReturnLoggedUserEmailAsCacheKey() {
        //when
        String userAppCacheKey = applicationService.getUserAppCacheKey();

        //then
        assertThat(userAppCacheKey).isEqualTo(loggedUserEmail);
    }

    @Test
    @DisplayName("should correctly return logged user applications")
    void shouldReturnUserApplications() {
        //given
        final String appName = "TestApp";

        Set<UserApplicationDTO> userApps = TestObjectCreator.createUserApps(appName, 5);

        doReturn(userApps).when(userApplicationService).getAllForUser(eq(loggedUserEmail));

        doAnswer(invocationOnMock -> {
            UserApplicationDTO userApp = invocationOnMock.getArgument(0);
            return Math.toIntExact(userApp.getId());
        }).when(errorService).countByApplication(any(UserApplicationDTO.class));

        doAnswer(invocationOnMock -> {
            UserApplicationDTO userApp = invocationOnMock.getArgument(0);
            return Math.toIntExact(userApp.getId() - 1);
        }).when(errorService).countSince(any(UserApplicationDTO.class), any(LocalDate.class));

        //when
        Set<ApplicationDTO> loggedUserApps = applicationService.getUserApps();

        //then
        assertThat(loggedUserApps)
                .isNotEmpty()
                .hasSize(5);

        loggedUserApps.forEach(ua -> {
            assertThat(ua.getAllErrorCount()).isEqualTo(Math.toIntExact(ua.getId()));
            assertThat(ua.getLastWeekErrorCount()).isEqualTo(Math.toIntExact(ua.getId() - 1));
        });
    }

    @Test
    @DisplayName("should return empty set for logged user if he has no linked apps")
    void shouldReturnEmptySetForLoggedUser() {
        //given
        doReturn(Collections.emptySet()).when(userApplicationService).getAllForUser(eq(loggedUserEmail));

        //when
        Set<ApplicationDTO> loggedUserApps = applicationService.getUserApps();

        //then
        assertThat(loggedUserApps)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("should get application details without modify access")
    void shouldCorrectlyGetAppDetailsWithNoModify() {
        //given
        final String appName = "TestApp";
        final Long id = 1L;

        Application application = TestObjectCreator.createApplication(appName, company);
        application.setId(id);

        doReturn(Optional.of(application)).when(applicationRepository).findById(eq(id));

        UserApplicationDTO userApp = UserApplicationDTO.builder()
                .id(id)
                .name(appName)
                .modify(false)
                .build();

        doReturn(userApp).when(userApplicationService).getForUser(eq(loggedUserEmail), eq(id));
        doReturn(Set.of(userApp)).when(userApplicationService).getAllForUser(eq(loggedUserEmail));
        doReturn(Collections.emptyList()).when(errorService).getAllByApplication(eq(application));

        //when
        ApplicationDetailsDTO applicationDetails = applicationService.get(id);

        //then
        assertThat(applicationDetails.getName()).isEqualTo(userApp.getName());
        assertThat(applicationDetails.getId()).isEqualTo(userApp.getId());
        assertThat(applicationDetails.isModify()).isEqualTo(userApp.isModify());
        assertThat(applicationDetails.getErrors())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("should disable access to application details if not linked to user")
    void shouldDisableAccessToDetails() {
        //given
        final String appName = "TestApp";
        final Long id = 1L;

        Application application = TestObjectCreator.createApplication(appName, company);
        application.setId(id);

        doReturn(Optional.of(application)).when(applicationRepository).findById(eq(id));
        doReturn(Collections.emptySet()).when(userApplicationService).getAllForUser(eq(loggedUserEmail));

        //when
        Executable codeUnderException = () -> applicationService.get(id);

        //then
        DataAccessException accessForbidden = assertThrows(DataAccessException.class, codeUnderException,
                "should throw DataAccessException");

        assertThat(accessForbidden.getMessage()).isEqualTo("Attempt to access forbidden resources");
    }

    @Test
    @DisplayName("should throw DataNotFoundException when no application found for given id")
    void shouldThrowDataNotFoundExceptionWhenNoApplication() {
        //given
        final Long id = 1L;

        doReturn(Optional.empty()).when(applicationRepository).findById(eq(id));

        //when
        Executable codeUnderException = () -> applicationService.get(id);

        //then
        DataNotFoundException applicationNotFound = assertThrows(DataNotFoundException.class, codeUnderException,
                "should throw DataNotFoundException");

        assertThat(applicationNotFound.getMessage()).isEqualTo("application.notFound");
    }
}