package pl.ark.chr.buginator.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.TestObjectCreator;
import pl.ark.chr.buginator.data.ChartData;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.exceptions.ChartException;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.repository.core.ErrorRepository;
import pl.ark.chr.buginator.service.ChartService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2016-12-26.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChartServiceImplTest {

    private static final String TEST_MESSAGE_SOURCE_RETURN = "TEST INFO";

    @InjectMocks
    private ChartService sut = new ChartServiceImpl();

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ErrorRepository errorRepository;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() throws Exception {
        when(messageSource.getMessage(any(String.class), nullable(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
    }

    @Test
    @Disabled
    public void testGenerateLastWeekErrorsForApplication__ProperValues() throws ChartException, DataAccessException {
        //given
        Application testApplication = TestObjectCreator.createApplication(new Company("", new PaymentOption()), "App1");

        Set<UserApplication> userApps = new HashSet<>();
        UserApplication ua = new UserApplication(new User.Builder().build(), testApplication);
//        ua.setApplication(testApplication);
//        userApps.add(ua);

        int chartLabelsAndDataLength = 8;
        int chartDataAndSeriesLength = 1;

        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(testApplication));

        when(errorRepository.findByApplicationAndLastOccurrenceGreaterThanEqual(any(Application.class), any(LocalDate.class)))
                .thenReturn(TestObjectCreator.generateErrorListForLastWeekForApplication(testApplication));

        //when
        ChartData result = sut.generateLastWeekErrorsForApplication(1L, userApps);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getSeries())
                .hasSize(chartDataAndSeriesLength)
                .containsExactly(TEST_MESSAGE_SOURCE_RETURN);
        assertThat(result.getLabels())
                .hasSize(chartLabelsAndDataLength)
                .containsExactly(getLastSevenDaysAsStrings());
        assertThat(result.getData())
                .hasSize(chartDataAndSeriesLength);
        result.getData().forEach(list -> assertThat(list)
                        .isNotNull()
                        .isNotEmpty()
        );
        assertThat(result.getData().get(0))
                .hasSize(chartLabelsAndDataLength)
                .containsExactly(getCorrectOrderForEachDay());
    }

    @Test
    public void testGenerateLastWeekErrorsForApplication__NoApplicationFoundWithId() throws ChartException, DataAccessException {
        //given
        Application testApplication = TestObjectCreator.createApplication(new Company("", new PaymentOption()), "App1");

        Set<UserApplication> userApps = new HashSet<>();
        UserApplication ua = new UserApplication(new User.Builder().build(), testApplication);
//        ua.setApplication(testApplication);
//        userApps.add(ua);

        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

//        when(errorRepository.findByApplicationAndLastOccurrenceGreaterThanEqual(any(Application.class), any(LocalDate.class)))
//                .thenReturn(TestObjectCreator.generateErrorListForLastWeekForApplication(testApplication));

        //when
        Executable codeUnderException = () -> sut.generateLastWeekErrorsForApplication(1L, userApps);

        //then
        var chartException = assertThrows(ChartException.class, codeUnderException,
                "should throw ChartException");
        assertThat(chartException.getMessage()).isEqualTo(TEST_MESSAGE_SOURCE_RETURN);
    }

    @Test
    public void testGenerateLastWeekErrorsForApplication__UserHasNoAccessToApplication() throws ChartException, DataAccessException {
        //given
        Application testApplication = TestObjectCreator.createApplication(new Company("", new PaymentOption()), "App1");
        Application testApplication2 = TestObjectCreator.createApplication(new Company("", new PaymentOption()), "App2", 2L);

        Set<UserApplication> userApps = new HashSet<>();
        UserApplication ua = new UserApplication(new User.Builder().build(), testApplication2);
//        ua.setApplication(testApplication2);
//        userApps.add(ua);

        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(testApplication));

//        when(errorRepository.findByApplicationAndLastOccurrenceGreaterThanEqual(any(Application.class), any(LocalDate.class)))
//                .thenReturn(TestObjectCreator.generateErrorListForLastWeekForApplication(testApplication));

        //when
        Executable codeUnderException = () -> sut.generateLastWeekErrorsForApplication(1L, userApps);

        //then
        var dataAccessException = assertThrows(DataAccessException.class, codeUnderException,
                "should throw DataAccessException");
        assertThat(dataAccessException.getMessage()).isEqualTo("Attempt to access forbidden resources");
    }

    @Test
    @Disabled
    public void testGenerateLastWeekErrorsForApplication__NoErrorsFound() throws ChartException, DataAccessException {
        //given
        Application testApplication = TestObjectCreator.createApplication(new Company("", new PaymentOption()), "App1");

        Set<UserApplication> userApps = new HashSet<>();
        UserApplication ua = new UserApplication(new User.Builder().build(), testApplication);
//        ua.setApplication(testApplication);
//        userApps.add(ua);

        int chartLabelsAndDataLength = 8;
        int chartDataAndSeriesLength = 1;

        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(testApplication));

        when(errorRepository.findByApplicationAndLastOccurrenceGreaterThanEqual(any(Application.class), any(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        //when
        ChartData result = sut.generateLastWeekErrorsForApplication(1L, userApps);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getSeries())
                .hasSize(chartDataAndSeriesLength)
                .containsExactly(TEST_MESSAGE_SOURCE_RETURN);
        assertThat(result.getLabels())
                .hasSize(chartLabelsAndDataLength)
                .containsExactly(getLastSevenDaysAsStrings());
        assertThat(result.getData())
                .hasSize(chartDataAndSeriesLength);
        result.getData().forEach(list -> assertThat(list)
                        .isNotNull()
                        .isNotEmpty()
        );
        assertThat(result.getData().get(0))
                .hasSize(chartLabelsAndDataLength)
                .containsExactly(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    }

    private String[] getLastSevenDaysAsStrings() {
        DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int LAST_SEVEN_DAYS = 7;
        String[] elements = new String[LAST_SEVEN_DAYS + 1];

        for (int i = LAST_SEVEN_DAYS; i >= 0; i--) {
            elements[elements.length - 1 - i] = LocalDate.now().minusDays(i).format(DATE_FORMAT);
        }

        return elements;
    }

    private Long[] getCorrectOrderForEachDay() {
        return new Long[]{3L, 0L, 0L, 2L, 0L, 0L, 1L, 3L};
    }
}