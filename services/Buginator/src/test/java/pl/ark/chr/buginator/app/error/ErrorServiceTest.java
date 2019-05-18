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
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;
import pl.ark.chr.buginator.repository.core.ErrorRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;
import pl.ark.chr.buginator.util.TestObjectCreator;

import java.time.LocalDateTime;
import java.util.List;
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

    static final PaymentOption paymentOption = TestObjectCreator.createPaymentOption("Trial");
    static final Company company = TestObjectCreator.createCompany(paymentOption);
    static Application application = TestObjectCreator.createApplication("TestApp", company);
    static final LoggedUserDTO loggedUser = TestObjectCreator.loggedUser("test@gmail.com", 1L);

    @BeforeEach
    void setUp() {
        application.setId(1L);
        errorService = new ErrorService(loggedUserService, userApplicationService, errorRepository, errorMapper);
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

    private Stream generateErrors() {
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
        Error error7= TestObjectCreator.createErrorBuilder(errorTitle, ErrorSeverity.WARNING,
                ErrorStatus.ONGOING, minusOneDay, application)
                .id(7L)
                .build();

        return Stream.of(error1,error2,error3,error4,error5,error6,error7);
    }

    private Long[] getSortedErrorIdArray() {
        return new Long[]{6L,5L,7L,4L,3L,2L,1L};
    }
}