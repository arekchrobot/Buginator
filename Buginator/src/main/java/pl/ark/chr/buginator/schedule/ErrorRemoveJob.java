package pl.ark.chr.buginator.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.BuginatorProperties;
import pl.ark.chr.buginator.domain.*;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.enums.ErrorStatus;
import pl.ark.chr.buginator.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-03-23.
 */
@Service
@Transactional
public class ErrorRemoveJob {

    private static final Logger logger = LoggerFactory.getLogger(ErrorRemoveJob.class);

    @Autowired
    private BuginatorProperties buginatorProperties;

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ErrorStackTraceRepository errorStackTraceRepository;

    @Autowired
    private UserAgentDataRepository userAgentDataRepository;

    @Autowired
    private AggregatorLogRepository aggregatorLogRepository;

    @Scheduled(cron = "${buginator.cron-schedule-error-remove}")
    public void performRemove() {
        logger.info("Starting job: Remove all old errors");
        LocalDate lastOccurrence = LocalDate.now().minusMonths(buginatorProperties.getErrorMonthsOldToRemove());

        List<Error> errorsToRemove = errorRepository.findByStatusAndLastOccurrenceLessThanEqual(ErrorStatus.RESOLVED, lastOccurrence);


        if (!errorsToRemove.isEmpty()) {
            String ids = errorsToRemove.stream().map(error -> error.getId().toString()).collect(Collectors.joining(","));
            logger.info("Removing all old resolved errors with date older than " + buginatorProperties.getErrorMonthsOldToRemove() + " months with ids: " + ids);

            removeNotifications(errorsToRemove);
            removeErrorStackTraces(errorsToRemove);
            removeUserAgentData(errorsToRemove);
            removeAggregatorLogs(errorsToRemove);

            errorRepository.deleteAll(errorsToRemove);
        } else {
            logger.info("No errors found for remove with date older than " + buginatorProperties.getErrorMonthsOldToRemove() + " months");
        }
    }

    private void removeUserAgentData(List<Error> errorsToRemove) {
        List<UserAgentData> userAgentsToRemove = errorsToRemove.stream()
                .map(error -> error.getUserAgent())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!userAgentsToRemove.isEmpty()) {
            String ids = userAgentsToRemove.stream().map(uad -> uad.getId().toString()).collect(Collectors.joining(","));

            logger.info("Removing userAgentData with ids: " + ids);

            userAgentDataRepository.deleteAll(userAgentsToRemove);
        } else {
            logger.info("No userAgentData to remove");
        }
    }

    private void removeErrorStackTraces(List<Error> errorsToRemove) {
        List<ErrorStackTrace> errorStackTraces = errorsToRemove.stream()
                .flatMap(error -> error.getStackTrace().stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!errorStackTraces.isEmpty()) {
            String ids = errorStackTraces.stream().map(est -> est.getId().toString()).collect(Collectors.joining(","));

            logger.info("Removing errorStackTrace with ids: " + ids);

            errorStackTraceRepository.deleteAll(errorStackTraces);
        } else {
            logger.info("No errorStackTrace to remove");
        }
    }

    private void removeNotifications(List<Error> errorsToRemove) {
        List<Notification> notificationsToRemove = notificationRepository.findByErrorIn(errorsToRemove);

        if (!notificationsToRemove.isEmpty()) {
            String ids = notificationsToRemove.stream().map(n -> n.getId().toString()).collect(Collectors.joining(","));

            logger.info("Removing notifications with ids: " + ids);

            notificationRepository.deleteAll(notificationsToRemove);
        } else {
            logger.info("No notification to remove");
        }
    }

    private void removeAggregatorLogs(List<Error> errorsToRemove) {
        List<AggregatorLog> aggregatorLogs = aggregatorLogRepository.findByErrorIn(errorsToRemove);

        if(!aggregatorLogs.isEmpty()) {
            String ids = aggregatorLogs.stream().map(al -> al.getId().toString()).collect(Collectors.joining(","));

            logger.info("Removing aggregatorLogs with ids: " + ids);

            aggregatorLogRepository.deleteAll(aggregatorLogs);
        } else {
            logger.info("No aggregatorLogs to remove");
        }
    }
}
