package pl.ark.chr.buginator.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.data.ChartData;
import pl.ark.chr.buginator.domain.*;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.exceptions.ChartException;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.filter.ClientFilterFactory;
import pl.ark.chr.buginator.repository.ApplicationRepository;
import pl.ark.chr.buginator.repository.ErrorRepository;
import pl.ark.chr.buginator.service.ChartService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2016-12-25.
 */
@Service
@Transactional
public class ChartServiceImpl implements ChartService {

    private static final Logger logger = LoggerFactory.getLogger(ChartServiceImpl.class);

    private static final int LAST_SEVEN_DAYS = 7;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final long NO_ERRORS = 0;

    private final ClientFilter clientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ChartData generateLastWeekErrorsForApplication(Long appId, Set<UserApplication> userApplications) throws ChartException, DataAccessException {
        LocalDate lastSevenDays = LocalDate.now().minusDays(LAST_SEVEN_DAYS);

        Application application = applicationRepository.findOne(appId);

        Locale locale = LocaleContextHolder.getLocale();

        logger.info("Generating last week errors for application with id: " + appId);

        validateNullApplication(application, locale, appId);

        clientFilter.validateAccess(application, userApplications);

        List<Error> applicationErrors = errorRepository.findByApplicationAndLastOccurrenceGreaterThanEqual(application, lastSevenDays);

        ChartData chartData = new ChartData(messageSource.getMessage("charts.application.errorLastWeek.series", null, locale));

        List<Long> errorCounts = new ArrayList<>();

        Map<String, Long> groupedErrors = groupErrorsByDay(applicationErrors);

        for (int i = LAST_SEVEN_DAYS; i >= 0; i--) {
            LocalDate selectedDate =  LocalDate.now().minusDays(i);
            String dateKey = selectedDate.format(DATE_FORMAT);

            chartData.getLabels().add(dateKey);
            errorCounts.add(groupedErrors.containsKey(dateKey) ? groupedErrors.get(dateKey) : NO_ERRORS);
        }

        chartData.getData().add(errorCounts);

        logger.info("Generating last week errors for application with id: " + appId + " finished.");

        return chartData;
    }

    private void validateNullApplication(Application application, Locale locale, Long appId) throws ChartException {
        if(application == null) {
            logger.warn("No application found with id: " + appId + " to generate chart data");
            throw new ChartException(messageSource.getMessage("chartException.applicationNotFound", null, locale));
        }
    }

    private Map<String, Long> groupErrorsByDay(List<Error> applicationErrors) {
        return applicationErrors.stream()
                .map(e -> e.getLastOccurrence())
                .collect(Collectors.groupingBy(i -> i.format(DATE_FORMAT), Collectors.counting()));
    }
}
