package pl.ark.chr.buginator.app.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ark.chr.buginator.app.application.UserApplicationDTO;
import pl.ark.chr.buginator.app.application.UserApplicationService;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.core.*;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.repository.core.ErrorRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;
import pl.ark.chr.buginator.util.TestObjectCreator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ErrorServiceTest {

    private ErrorService errorService;

    @Mock
    private ErrorRepository errorRepository;
    @Mock
    private LoggedUserService loggedUserService;
    @Mock
    private UserApplicationService userApplicationService;

    private final ErrorMapper errorMapper = new ErrorMapperImpl();
    private final ErrorDetailsMapper errorDetailsMapper = new ErrorDetailsMapperImpl(new UserAgentMapperImpl());

    static final PaymentOption paymentOption = TestObjectCreator.createPaymentOption("Trial");
    static final Company company = TestObjectCreator.createCompany(paymentOption);
    static Application application = TestObjectCreator.createApplication("TestApp", company);
    static final LoggedUserDTO loggedUser = TestObjectCreator.loggedUser("test@gmail.com", 1L);

    @BeforeEach
    void setUp() {
        application.setId(1L);
        errorService = new ErrorService(loggedUserService, userApplicationService, errorRepository, errorMapper, errorDetailsMapper);
    }

    @Test
    @DisplayName("should fetch sorted error DTO by application")
    void shouldCorrectlyGetSortedErrorsForApp() {
        //given
        doReturn(generateErrors()).when(errorRepository).findByApplication(eq(application));

        doReturn(Set.of(UserApplicationDTO.builder().id(application.getId()).build()))
                .when(userApplicationService).getAllForUser(eq(loggedUser.getEmail()));

        doReturn(loggedUser).when(loggedUserService).getCurrentUser();

        //when
        List<ErrorDTO> errors = errorService.getAllByApplication(application);

        //then
        assertThat(errors)
                .isNotNull()
                .isNotEmpty()
                .hasSize(7)
                .extracting(ErrorDTO::getId)
                .containsExactly(getSortedErrorIdArray());
    }

    @Test
    @DisplayName("should not allow getting errors for null application")
    void shouldNotAllowNullApplication() {
        //when
        Executable codeUnderException = () -> errorService.getAllByApplication(null);

        //then
        assertThrows(NullPointerException.class, codeUnderException);
    }

    @Test
    @DisplayName("should not allow getting errors if user has no access to application")
    void shouldNotAllowToGetErrorsIfApplicationIsNotLinked() {
        //given
        Long otherAppId = 10L;
        doReturn(Set.of(UserApplicationDTO.builder().id(otherAppId).build()))
                .when(userApplicationService).getAllForUser(eq(loggedUser.getEmail()));

        doReturn(loggedUser).when(loggedUserService).getCurrentUser();

        //when
        Executable codeUnderException = () -> errorService.getAllByApplication(application);

        //then
        DataAccessException dataAccessException = assertThrows(DataAccessException.class, codeUnderException);

        assertThat(dataAccessException.getMessage()).isEqualTo("Attempt to access forbidden resources");
    }

    private Stream<Error> generateErrors() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime minusSevenDays = now.minusDays(7);
        LocalDateTime minusFourDays = now.minusDays(4);
        LocalDateTime minusOneDay = now.minusDays(1);

        String errorTitle = "Error";
        Error error1 = TestObjectCreator.createErrorBuilder(errorTitle, ErrorSeverity.CRITICAL,
                ErrorStatus.ONGOING, minusSevenDays, application)
                .id(1L)
                .build();
        Error error2 = TestObjectCreator.createErrorBuilder(errorTitle, ErrorSeverity.CRITICAL,
                ErrorStatus.CREATED, minusSevenDays, application)
                .id(2L)
                .build();
        Error error3 = TestObjectCreator.createErrorBuilder(errorTitle, ErrorSeverity.ERROR,
                ErrorStatus.RESOLVED, minusFourDays, application)
                .id(3L)
                .build();
        Error error4 = TestObjectCreator.createErrorBuilder(errorTitle, ErrorSeverity.ERROR,
                ErrorStatus.CREATED, minusFourDays, application)
                .id(4L)
                .build();
        Error error5 = TestObjectCreator.createErrorBuilder(errorTitle, ErrorSeverity.ERROR,
                ErrorStatus.ONGOING, minusOneDay, application)
                .id(5L)
                .build();
        Error error6 = TestObjectCreator.createErrorBuilder(errorTitle, ErrorSeverity.CRITICAL,
                ErrorStatus.ONGOING, minusOneDay, application)
                .id(6L)
                .build();
        Error error7 = TestObjectCreator.createErrorBuilder(errorTitle, ErrorSeverity.WARNING,
                ErrorStatus.ONGOING, minusOneDay, application)
                .id(7L)
                .build();

        return Stream.of(error1, error2, error3, error4, error5, error6, error7);
    }

    private Long[] getSortedErrorIdArray() {
        return new Long[]{6L, 5L, 7L, 4L, 3L, 2L, 1L};
    }

    @Test
    @DisplayName("should correctly build error details with user agent data")
    void shouldBuildErrorDetailsWithUserAgent() {
        //given
        doReturn(Set.of(UserApplicationDTO.builder().id(application.getId()).build()))
                .when(userApplicationService).getAllForUser(eq(loggedUser.getEmail()));

        doReturn(loggedUser).when(loggedUserService).getCurrentUser();

        String errorTitle = "Test Error";
        Error error = TestObjectCreator.createError(application, errorTitle, LocalDateTime.now());
        error.setUserAgent(TestObjectCreator.createUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:76.0) Gecko/20100101 Firefox/76.0"));

        error.setStackTrace(TestObjectCreator.createErrorStackTrace(error));

        Long errorId = 1L;
        doReturn(Optional.of(error)).when(errorRepository).findWithFullInfo(eq(errorId));

        //when
        ErrorDetailsDTO errorDetails = errorService.details(errorId);

        //then
        assertThat(errorDetails.getTitle()).isEqualTo(errorTitle);
        assertThat(errorDetails.getUserAgent()).isNotNull();
        assertThat(errorDetails.getUserAgent().getBrowser()).isEqualTo("Mozilla Firefox 76.0");
        assertThat(errorDetails.getUserAgent().getDevice()).isEqualTo("COMPUTER x86_64");
        assertThat(errorDetails.getUserAgent().getOperatingSystem()).isEqualTo("Unknown Linux");
    }

    @Test
    @DisplayName("should correctly build error details when user data is not present")
    void shouldBuildErrorDetailsWithoutUserAgent() {
        //given
        doReturn(Set.of(UserApplicationDTO.builder().id(application.getId()).build()))
                .when(userApplicationService).getAllForUser(eq(loggedUser.getEmail()));

        doReturn(loggedUser).when(loggedUserService).getCurrentUser();

        String errorTitle = "Test Error";
        Error error = TestObjectCreator.createError(application, errorTitle, LocalDateTime.now());

        error.setStackTrace(TestObjectCreator.createErrorStackTrace(error));

        Long errorId = 1L;
        doReturn(Optional.of(error)).when(errorRepository).findWithFullInfo(eq(errorId));

        //when
        ErrorDetailsDTO errorDetails = errorService.details(errorId);

        //then
        assertThat(errorDetails.getTitle()).isEqualTo(errorTitle);
        assertThat(errorDetails.getUserAgent()).isNull();
    }

    @Test
    @DisplayName("should correctly present stack trace for error details")
    void shouldCorrectlyPresentStackTraceForErrorDetails() {
        //given
        doReturn(Set.of(UserApplicationDTO.builder().id(application.getId()).build()))
                .when(userApplicationService).getAllForUser(eq(loggedUser.getEmail()));

        doReturn(loggedUser).when(loggedUserService).getCurrentUser();

        String errorTitle = "Test Error";
        Error error = TestObjectCreator.createError(application, errorTitle, LocalDateTime.now());

        error.setStackTrace(TestObjectCreator.createErrorStackTrace(error));

        Long errorId = 1L;
        doReturn(Optional.of(error)).when(errorRepository).findWithFullInfo(eq(errorId));

        //when
        ErrorDetailsDTO errorDetails = errorService.details(errorId);

        //then
        assertThat(errorDetails.getTitle()).isEqualTo(errorTitle);
        assertThat(errorDetails.getStackTrace())
                .isEqualTo("Exception: java.lang.NullPointerException\n" +
                        "\tat java.base/java.util.ImmutableCollections.listCopy(ImmutableCollections.java:92)\n" +
                        "\tat java.base/java.util.List.copyOf(List.java:1061)\n" +
                        "\tat pl.ark.chr.buginator.domain.core.Error.getStackTrace(Error.java:114)\n" +
                        "\tat pl.ark.chr.buginator.app.error.ErrorDetailsMapperImpl.toDto(ErrorDetailsMapperImpl.java:47)\n" +
                        "\tat pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)\n" +
                        "\tat java.base/java.util.ArrayList.forEach(ArrayList.java:1540)\n" +
                        "Caused by: java.lang.NullPointerException\n" +
                        "\tat pl.ark.chr.buginator.app.error.ErrorService.details(ErrorService.java:83)\n" +
                        "\tat java.base/java.util.ArrayList.forEach(ArrayList.java:1540)\n");
    }

    @Test
    @DisplayName("should correctly change status of an error")
    void shouldCorrectlyUpdateErrorStatus() {
        //given
        doReturn(Set.of(UserApplicationDTO.builder().id(application.getId()).modify(true).build()))
                .when(userApplicationService).getAllForUser(eq(loggedUser.getEmail()));

        doReturn(loggedUser).when(loggedUserService).getCurrentUser();

        String errorTitle = "Test Error";
        Error error = TestObjectCreator.createError(application, errorTitle, LocalDateTime.now());

        error.setStackTrace(TestObjectCreator.createErrorStackTrace(error));

        Long errorId = 1L;
        doReturn(Optional.of(error)).when(errorRepository).findById(eq(errorId));

        //when
        ErrorDetailsDTO errorDetails = errorService.changeStatus(errorId, ErrorStatus.REOPENED);

        //then
        assertThat(errorDetails.getStatus()).isEqualTo(ErrorStatus.REOPENED);
    }

    @Test
    @DisplayName("should not allow to change status of an error when user has no modify")
    void shouldNotAllowToUpdateErrorStatus() {
        //given
        doReturn(Set.of(UserApplicationDTO.builder().id(application.getId()).modify(false).build()))
                .when(userApplicationService).getAllForUser(eq(loggedUser.getEmail()));

        doReturn(loggedUser).when(loggedUserService).getCurrentUser();

        String errorTitle = "Test Error";
        Error error = TestObjectCreator.createError(application, errorTitle, LocalDateTime.now());

        error.setStackTrace(TestObjectCreator.createErrorStackTrace(error));

        Long errorId = 1L;
        doReturn(Optional.of(error)).when(errorRepository).findById(eq(errorId));

        //when
        Executable codeUnderException = () -> errorService.changeStatus(errorId, ErrorStatus.REOPENED);

        //then
        DataAccessException dataAccessException = assertThrows(DataAccessException.class, codeUnderException);

        assertThat(dataAccessException.getMessage()).isEqualTo("User is not permitted to modify application");
    }
}