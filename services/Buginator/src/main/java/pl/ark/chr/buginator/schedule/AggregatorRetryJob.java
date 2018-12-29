package pl.ark.chr.buginator.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.BuginatorProperties;
import pl.ark.chr.buginator.aggregator.domain.Aggregator;
import pl.ark.chr.buginator.aggregator.domain.AggregatorLog;
import pl.ark.chr.buginator.aggregator.domain.AggregatorLogStatus;
import pl.ark.chr.buginator.aggregator.repository.AggregatorLogRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-04-01.
 */
@Service
@Transactional
public class AggregatorRetryJob {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorRetryJob.class);

    @Autowired
    private ExecutorService innerJobScheduler;

    @Autowired
    private AggregatorLogRepository aggregatorLogRepository;

    @Autowired
    private BuginatorProperties buginatorProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Scheduled(cron = "${buginator.cron-schedule-aggregator-retry}")
    public void performRetry() {
        logger.info("Starting job: Retry aggregators");
        List<AggregatorLog> failedAggregatorLogs = aggregatorLogRepository.findByStatus(AggregatorLogStatus.FAILURE)
                .stream()
                .filter(aggregatorLog -> aggregatorLog.getRetryCount() < buginatorProperties.getAggregatorRetryCount())
                .collect(Collectors.toList());

        if(failedAggregatorLogs.isEmpty()) {
            logger.info("No retries found for aggregators with retry count less than: " + buginatorProperties.getAggregatorRetryCount());
        }
        failedAggregatorLogs.forEach(aggregatorLog -> launchSingleRetry(aggregatorLog));
    }

    private void launchSingleRetry(AggregatorLog aggregatorLog) {
        innerJobScheduler.submit(() -> {
            try {
                Aggregator aggregator = aggregatorLog.getAggregator();
                //TODO: reimplement
//                AggregatorService aggregatorService = aggregatorReflection.getAggregatorService(aggregator, applicationContext);
//                aggregatorService.notifyExternalAggregator(aggregator, aggregatorLog.getError());

                saveAggregatorLogSuccess(aggregatorLog);
            } catch (Exception e) {
                logger.error("Error performing retry for aggregatorLog with id: " + aggregatorLog.getId(), e);
                saveAggregatorLogError(aggregatorLog, e.getLocalizedMessage());
            }
        });
    }

    private void saveAggregatorLogSuccess(AggregatorLog aggregatorLog) {
        aggregatorLog.updateTimestamp();
        aggregatorLog.setStatus(AggregatorLogStatus.SUCCESS);
        aggregatorLog.setErrorDescription(null);

        aggregatorLogRepository.save(aggregatorLog);
    }

    private void saveAggregatorLogError(AggregatorLog aggregatorLog, String errorDescription) {
        aggregatorLog.incrementRetryCount();
        aggregatorLog.setErrorDescription(errorDescription);
        aggregatorLog.updateTimestamp();

        aggregatorLogRepository.save(aggregatorLog);
    }
}
