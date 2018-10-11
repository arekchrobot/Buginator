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
import pl.ark.chr.buginator.data.ChartData;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.exceptions.ChartException;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.repository.core.ErrorRepository;
import pl.ark.chr.buginator.service.ChartService;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2016-12-26.
 */
@RunWith(MockitoJUnitRunner.class)
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

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
        when(messageSource.getMessage(any(String.class), nullable(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGenerateLastWeekErrorsForApplication__ProperValues() throws ChartException, DataAccessException {
        //given
        Application testApplication = TestObjectCreator.createApplication(null, "App1");

        Set<UserApplication> userApps = new HashSet<>();
        UserApplication ua = new UserApplication();
        ua.setApplication(testApplication);
        userApps.add(ua);

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
        Application testApplication = TestObjectCreator.createApplication(null, "App1");

        Set<UserApplication> userApps = new HashSet<>();
        UserApplication ua = new UserApplication();
        ua.setApplication(testApplication);
        userApps.add(ua);

        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

//        when(errorRepository.findByApplicationAndLastOccurrenceGreaterThanEqual(any(Application.class), any(LocalDate.class)))
//                .thenReturn(TestObjectCreator.generateErrorListForLastWeekForApplication(testApplication));

        fluentThrown.expect(ChartException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.generateLastWeekErrorsForApplication(1L, userApps);

        //then
    }

    @Test
    public void testGenerateLastWeekErrorsForApplication__UserHasNoAccessToApplication() throws ChartException, DataAccessException {
        //given
        Application testApplication = TestObjectCreator.createApplication(null, "App1");
        Application testApplication2 = TestObjectCreator.createApplication(null, "App2", 2L);

        Set<UserApplication> userApps = new HashSet<>();
        UserApplication ua = new UserApplication();
        ua.setApplication(testApplication2);
        userApps.add(ua);

        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(testApplication));

//        when(errorRepository.findByApplicationAndLastOccurrenceGreaterThanEqual(any(Application.class), any(LocalDate.class)))
//                .thenReturn(TestObjectCreator.generateErrorListForLastWeekForApplication(testApplication));

        fluentThrown.expect(DataAccessException.class)
                .hasMessage("Attempt to access forbidden resources");

        //when
        sut.generateLastWeekErrorsForApplication(1L, userApps);

        //then
    }

    @Test
    public void testGenerateLastWeekErrorsForApplication__NoErrorsFound() throws ChartException, DataAccessException {
        //given
        Application testApplication = TestObjectCreator.createApplication(null, "App1");

        Set<UserApplication> userApps = new HashSet<>();
        UserApplication ua = new UserApplication();
        ua.setApplication(testApplication);
        userApps.add(ua);

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