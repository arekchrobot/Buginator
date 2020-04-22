package pl.ark.chr.buginator.app.application.report;

import org.junit.jupiter.api.BeforeEach;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.ark.chr.buginator.app.application.UserApplicationDTO;
import pl.ark.chr.buginator.app.application.UserApplicationService;
import pl.ark.chr.buginator.app.error.ErrorService;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.app.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;
import pl.ark.chr.buginator.util.TestObjectCreator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ErrorService errorService;
    @Mock
    private LoggedUserService loggedUserService;
    @Mock
    private UserApplicationService userApplicationService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String loggedUserEmail = "testEmail@gmail.com";
    private final Long companyId = 1L;
    private final LoggedUserDTO loggedUser = TestObjectCreator.loggedUser(loggedUserEmail, companyId);
    private final Company company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("Trial"));
    private final String appName = "TestApp";
    private final Long appId = 1L;
    private final Long appId2 = 3L;
    private final Application application = TestObjectCreator.createApplicationWithId(appName, company, appId);
    private final Application application2 = TestObjectCreator.createApplicationWithId(appName, company, appId2);
    private final Set<UserApplicationDTO> userApps = TestObjectCreator.createUserApps(appName, 1);

    @BeforeEach
    void setUp() {
        doReturn(loggedUser).when(loggedUserService).getCurrentUser();
        doReturn(userApps).when(userApplicationService).getAllForUser(eq(loggedUserEmail));
        doReturn(Optional.of(application)).when(applicationRepository).findById(eq(appId));
    }

    @ParameterizedTest
    @MethodSource("lastWeekErrorsProvider")
    @DisplayName("should correctly generate error counts for each day for last 7 days")
    void shouldCorrectlyGenerateErrorsForLastWeek(Map<String, Long> errors, Long[] expectedCounts) {
        //given
        doReturn(errors).when(errorService).getErrorsGroupedByDaySince(eq(application), any(LocalDate.class));

        //when
        LastWeekErrorsDTO result = reportService.generateLastWeekErrorReport(appId);

        //then
        int expectedDataAndLabelsLength = 8;

        assertThat(result).isNotNull();
        assertThat(result.getLabels())
                .hasSize(expectedDataAndLabelsLength)
                .containsExactly(getLastSevenDaysAsStrings());
        assertThat(result.getData())
                .hasSize(expectedDataAndLabelsLength);
        assertThat(result.getData())
                .hasSize(expectedDataAndLabelsLength)
                .containsExactly(expectedCounts);
    }

    private static Stream<Arguments> lastWeekErrorsProvider() {
        return Stream.of(
                Arguments.of(Map.of(
                        LocalDate.now().minusDays(7).format(DATE_FORMAT), 3L,
                        LocalDate.now().minusDays(4).format(DATE_FORMAT), 2L,
                        LocalDate.now().minusDays(2).format(DATE_FORMAT), 1L,
                        LocalDate.now().minusDays(1).format(DATE_FORMAT), 1L
                ), new Long[]{3L,0L,0L,2L,0L,1L,1L,0L}),
                Arguments.of(Collections.emptyMap(), new Long[]{0L,0L,0L,0L,0L,0L,0L,0L})
        );
    }

    private String[] getLastSevenDaysAsStrings() {
        int LAST_SEVEN_DAYS = 7;
        String[] elements = new String[LAST_SEVEN_DAYS + 1];

        for (int i = LAST_SEVEN_DAYS; i >= 0; i--) {
            elements[elements.length - 1 - i] = LocalDate.now().minusDays(i).format(DATE_FORMAT);
        }

        return elements;
    }

    @Test
    @DisplayName("should throw DataNotFoundException when no application exists")
    void shouldThrowWhenNoApplicationFound() {
        //when
        Executable codeUnderException = () -> reportService.generateLastWeekErrorReport(appId2);

        //then
        DataNotFoundException dataNotFound = assertThrows(DataNotFoundException.class, codeUnderException,
                "should throw DataNotFoundException");

        assertThat(dataNotFound.getMessage()).isEqualTo("application.notFound");
    }

    @Test
    @DisplayName("should not allow to generate last week error report when user has no access to application")
    void shouldNotAllowWhenNoRead() {
        //given
        doReturn(Optional.of(application2)).when(applicationRepository).findById(eq(appId2));

        //when
        Executable codeUnderException = () -> reportService.generateLastWeekErrorReport(appId2);

        //then
        DataAccessException dataAccessException = assertThrows(DataAccessException.class, codeUnderException,
                "should throw DataAccessException");

        assertThat(dataAccessException.getMessage()).isEqualTo("Attempt to access forbidden resources");
    }
}