package pl.ark.chr.buginator.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.TestObjectCreator;
import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.domain.*;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.repository.ApplicationRepository;
import pl.ark.chr.buginator.repository.ErrorRepository;
import pl.ark.chr.buginator.repository.UserApplicationRepository;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.ApplicationService;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2016-12-26.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceImplTest {

    private static final String TEST_MESSAGE_SOURCE_RETURN = "TEST INFO";

    @InjectMocks
    private ApplicationService sut = new ApplicationServiceImpl();

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserApplicationRepository userApplicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ErrorRepository errorRepository;

    @Mock
    private MessageSource messageSource;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSave__Success() throws DataAccessException {
        //given
        String companyName = "Test Company";
        Company company = TestObjectCreator.createCompany(LocalDate.now(), companyName, "", "");

        Role role = TestObjectCreator.createRole(null);

        String email = "test@test.test";
        User user = TestObjectCreator.createUser(email, role, company, "");

        UserWrapper userWrapper = TestObjectCreator.createUserWrapper(user);

        String appName = "Test App";
        Application testApp = new Application();
        testApp.setName(appName);

        when(applicationRepository.findByNameAndCompany(any(String.class), any(Company.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);
        when(userApplicationRepository.save(any(UserApplication.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        //when
        UserApplication result = sut.save(testApp, userWrapper);

        //then
        Application savedApp = result.getApplication();
        assertThat(savedApp)
                .isNotNull();
        assertThat(savedApp.getCompany())
                .isNotNull()
                .isEqualTo(company);
        assertThat(savedApp.getName())
                .isEqualTo(appName);
        assertThat(result.isModify());
        assertThat(result.getUser().getEmail())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(email);
    }

    @Test
    public void testSave__ApplicationWithThisNameAlreadyExists() throws DataAccessException {
        //given
        String companyName = "Test Company";
        Company company = TestObjectCreator.createCompany(LocalDate.now(), companyName, "", "");

        Role role = TestObjectCreator.createRole(null);

        String email = "test@test.test";
        User user = TestObjectCreator.createUser(email, role, company, "");

        UserWrapper userWrapper = TestObjectCreator.createUserWrapper(user);

        String appName = "Test App";
        Application testApp = new Application();
        testApp.setName(appName);

        when(applicationRepository.findByNameAndCompany(any(String.class), any(Company.class))).thenReturn(Optional.of(testApp));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);
        when(userApplicationRepository.save(any(UserApplication.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        fluentThrown.expect(IllegalArgumentException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN + " " + appName + " " + TEST_MESSAGE_SOURCE_RETURN);

        //when
        UserApplication result = sut.save(testApp, userWrapper);

        //then
    }

    @Test
    public void testGetUserApplications__ErrorsProperlySet() {
        //given
        String companyName = "Test Company";
        Company company = TestObjectCreator.createCompany(LocalDate.now(), companyName, "", "");

        String email = "test@test.test";
        User user = TestObjectCreator.createUser(email, TestObjectCreator.createRole(null), company, "");

        Application testApp = TestObjectCreator.createApplication(company, "Test App");

        UserApplication ua = TestObjectCreator.createUserApplication(user, testApp, true);

        Application testApp2 = TestObjectCreator.createApplication(company, "Test App 2", 2L);

        UserApplication ua2 = TestObjectCreator.createUserApplication(user, testApp2, true);

        user.getUserApplications().add(ua);
        user.getUserApplications().add(ua2);

        UserWrapper userWrapper = TestObjectCreator.createUserWrapper(user);

        Long errorCount = 11L;
        Long lastWeekErrorCountApp1 = 7L;
        Long lastWeekErrorCountApp2 = 6L;

        when(errorRepository.countByApplication(any(Application.class))).thenReturn(errorCount);
        when(errorRepository.countByApplicationAndLastOccurrenceGreaterThanEqual(eq(testApp), any(LocalDate.class))).thenReturn(lastWeekErrorCountApp1);
        when(errorRepository.countByApplicationAndLastOccurrenceGreaterThanEqual(eq(testApp2), any(LocalDate.class))).thenReturn(lastWeekErrorCountApp2);

        //when
        List<Application> applications = sut.getUserApplications(userWrapper);

        //then
        assertThat(applications)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .contains(testApp, testApp2);

        applications.forEach(application -> {
            if (application.getId().equals(1L)) {
                assertThat(application.getErrorCount())
                        .isEqualTo(errorCount);
                assertThat(application.getLastWeekErrorCount())
                        .isEqualTo(lastWeekErrorCountApp1);
            } else if(application.getId().equals(2L)){
                assertThat(application.getErrorCount())
                        .isEqualTo(errorCount);
                assertThat(application.getLastWeekErrorCount())
                        .isEqualTo(lastWeekErrorCountApp2);
            } else {
                fail("This id should not be in this list: " + application.getId());
            }
        });
    }
}