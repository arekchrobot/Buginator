package pl.ark.chr.buginator.ext.service.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.ark.chr.buginator.TestObjectCreator;
import pl.ark.chr.buginator.data.ExternalData;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.UserAgentData;
import pl.ark.chr.buginator.domain.enums.ErrorSeverity;
import pl.ark.chr.buginator.domain.enums.ErrorStatus;
import pl.ark.chr.buginator.ext.service.ErrorResolver;
import pl.ark.chr.buginator.repository.ErrorRepository;
import pl.ark.chr.buginator.repository.UserAgentDataRepository;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2017-04-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class ErrorResolverImplTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @InjectMocks
    private ErrorResolver sut = new ErrorResolverImpl();

    @Mock
    private ErrorRepository errorRepository;

    @Mock
    private UserAgentDataRepository userAgentDataRepository;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Test
    public void testResolveError__createNew() {
        //given
        Company company = TestObjectCreator.createCompany(LocalDate.now(), "", "", "");

        String appName = "App1";
        Application testApp = TestObjectCreator.createApplication(company, appName);

        ExternalData externalData = createExternalData(appName);

        when(errorRepository.findDuplicateError(any(String.class),any(String.class), any(ErrorSeverity.class),any(String.class),any(String.class),any(Application.class)))
                .thenReturn(new ArrayList<>());

        when(errorRepository.save(any(Error.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);
        when(userAgentDataRepository.save(any(UserAgentData.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        //when
        Error resolvedError = sut.resolveError(externalData, testApp);

        //then
        verify(userAgentDataRepository, times(1)).save(any(UserAgentData.class));

        assertThat(resolvedError.getUserAgent()).isNotNull();
        assertThat(resolvedError.getRequestUrl()).isNull();
        assertThat(resolvedError.getQueryParams()).isNotNull();
        assertThat(resolvedError.getCount()).isEqualTo(1);
        assertThat(resolvedError.getStatus()).isEqualTo(ErrorStatus.CREATED);
    }

    @Test
    public void testResolveError__createNew__emptyStringError() {
        //given
        Company company = TestObjectCreator.createCompany(LocalDate.now(), "", "", "");

        String appName = "App1";
        Application testApp = TestObjectCreator.createApplication(company, appName);

        ExternalData externalData = createExternalData(appName);
        externalData.setErrorTitle("");

        when(errorRepository.findDuplicateError(any(String.class),any(String.class), any(ErrorSeverity.class),any(String.class),any(String.class),any(Application.class)))
                .thenReturn(new ArrayList<>());

        fluentThrown
                .expect(IllegalArgumentException.class)
                .hasMessage("Title is empty");

        //when
        sut.resolveError(externalData, testApp);

        //then
    }

    @Test
    public void testResolveError__createNew__nullStringError() {
        //given
        Company company = TestObjectCreator.createCompany(LocalDate.now(), "", "", "");

        String appName = "App1";
        Application testApp = TestObjectCreator.createApplication(company, appName);

        ExternalData externalData = createExternalData(appName);
        externalData.setErrorDescription(null);

        when(errorRepository.findDuplicateError(any(String.class),any(String.class), any(ErrorSeverity.class),any(String.class),any(String.class),any(Application.class)))
                .thenReturn(new ArrayList<>());

        fluentThrown
                .expect(IllegalArgumentException.class)
                .hasMessage("Description is empty");

        //when
        sut.resolveError(externalData, testApp);

        //then
    }

    @Test
    public void testResolveError__createNew__settingDefaultSeverity() {
        //given
        Company company = TestObjectCreator.createCompany(LocalDate.now(), "", "", "");

        String appName = "App1";
        Application testApp = TestObjectCreator.createApplication(company, appName);

        ExternalData externalData = createExternalData(appName);
        externalData.setErrorSeverity(null);

        when(errorRepository.findDuplicateError(any(String.class),any(String.class), any(ErrorSeverity.class),any(String.class),any(String.class),any(Application.class)))
                .thenReturn(new ArrayList<>());

        when(errorRepository.save(any(Error.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);
        when(userAgentDataRepository.save(any(UserAgentData.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        //when
        Error resolvedError = sut.resolveError(externalData, testApp);

        //then
        assertThat(resolvedError.getSeverity()).isEqualTo(ErrorSeverity.ERROR);
    }

    @Test
    public void testResolveError__createNew__stackTraceNotSame() {
        //given
        Company company = TestObjectCreator.createCompany(LocalDate.now(), "", "", "");

        String appName = "App1";
        Application testApp = TestObjectCreator.createApplication(company, appName);

        Error error1 = TestObjectCreator.createFullDataError(true, false, testApp);
        error1.setId(1L);
        Error error2 = TestObjectCreator.createFullDataError(true, false, testApp);
        error2.setId(2L);

        List<Error> errors = new ArrayList<>();
        errors.add(error1);
        errors.add(error2);

        ExternalData externalData = createExternalData(appName);

        when(errorRepository.findDuplicateError(any(String.class),any(String.class), any(ErrorSeverity.class),any(String.class),any(String.class),any(Application.class)))
                .thenReturn(errors);

        when(errorRepository.save(any(Error.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);
        when(userAgentDataRepository.save(any(UserAgentData.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        //when
        Error resolvedError = sut.resolveError(externalData, testApp);

        //then
        verify(userAgentDataRepository, times(1)).save(any(UserAgentData.class));

        assertThat(resolvedError.getUserAgent()).isNotNull();
        assertThat(resolvedError.getStatus()).isEqualTo(ErrorStatus.CREATED);
    }

    @Test
    public void testResolveError__returnDuplicatedWithIncreasedCount__statusNotChanged() {
        //given
        Company company = TestObjectCreator.createCompany(LocalDate.now(), "", "", "");

        String appName = "App1";
        Application testApp = TestObjectCreator.createApplication(company, appName);

        Error error1 = TestObjectCreator.createFullDataError(true, true, testApp);
        error1.setId(1L);
        error1.setStatus(ErrorStatus.ONGOING);
        Error error2 = TestObjectCreator.createFullDataError(true, false, testApp);
        error2.setId(2L);

        List<Error> errors = new ArrayList<>();
        errors.add(error1);
        errors.add(error2);

        int currentCount = error1.getCount();

        ExternalData externalData = createExternalData(appName);

        when(errorRepository.findDuplicateError(any(String.class),any(String.class), any(ErrorSeverity.class),any(String.class),any(String.class),any(Application.class)))
                .thenReturn(errors);

        when(errorRepository.save(any(Error.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);
        when(userAgentDataRepository.save(any(UserAgentData.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        //when
        Error resolvedError = sut.resolveError(externalData, testApp);

        //then

        assertThat(resolvedError).isEqualTo(error1);
        assertThat(resolvedError.getCount()).isEqualTo(currentCount+1);
    }

    @Test
    public void testResolveError__returnDuplicatedWithIncreasedCount__statusChanged() {
        //given
        Company company = TestObjectCreator.createCompany(LocalDate.now(), "", "", "");

        String appName = "App1";
        Application testApp = TestObjectCreator.createApplication(company, appName);

        Error error1 = TestObjectCreator.createFullDataError(true, true, testApp);
        error1.setId(1L);
        error1.setStatus(ErrorStatus.RESOLVED);
        Error error2 = TestObjectCreator.createFullDataError(true, false, testApp);
        error2.setId(2L);

        List<Error> errors = new ArrayList<>();
        errors.add(error1);
        errors.add(error2);

        int currentCount = error1.getCount();

        ExternalData externalData = createExternalData(appName);

        when(errorRepository.findDuplicateError(any(String.class),any(String.class), any(ErrorSeverity.class),any(String.class),any(String.class),any(Application.class)))
                .thenReturn(errors);

        when(errorRepository.save(any(Error.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);
        when(userAgentDataRepository.save(any(UserAgentData.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        //when
        Error resolvedError = sut.resolveError(externalData, testApp);

        //then

        assertThat(resolvedError).isEqualTo(error1);
        assertThat(resolvedError.getStatus()).isEqualTo(ErrorStatus.REOPENED);
    }

    private ExternalData createExternalData(String appName) {
        ExternalData externalData = new ExternalData();

        externalData.setErrorSeverity(ErrorSeverity.ERROR);
        externalData.setErrorDescription("Test description 1");
        externalData.setErrorTitle("Test Title 1");
        externalData.setApplicationName(appName);
        externalData.setDateTimeString(LocalDateTime.now().plusDays(2).format(dateTimeFormatter));
        externalData.setDateString(LocalDate.now().plusDays(2).format(dateFormatter));
        externalData.setQueryParams("q=123&b=432");
        externalData.setStackTrace(createStackTrace());

        String userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
        externalData.setUserAgentString(userAgentString);

        return externalData;
    }

    private List<ExternalData.ExternalStackTrace> createStackTrace() {
        ExternalData.ExternalStackTrace externalStackTrace = new ExternalData.ExternalStackTrace();

        externalStackTrace.setDescription("Test stack trace 1");
        externalStackTrace.setOrder(1);

        ExternalData.ExternalStackTrace externalStackTrace2 = new ExternalData.ExternalStackTrace();

        externalStackTrace2.setDescription("Test stack trace 2");
        externalStackTrace2.setOrder(2);

        List<ExternalData.ExternalStackTrace> externalStackTraces = new ArrayList<>();

        externalStackTraces.add(externalStackTrace);
        externalStackTraces.add(externalStackTrace2);

        return externalStackTraces;
    }
}