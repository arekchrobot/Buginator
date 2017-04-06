package pl.ark.chr.buginator.ext.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.aggregator.service.AggregatorService;
import pl.ark.chr.buginator.aggregator.util.AggregatorReflection;
import pl.ark.chr.buginator.data.ExternalData;
import pl.ark.chr.buginator.domain.*;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.enums.AggregatorLogStatus;
import pl.ark.chr.buginator.ext.service.ApplicationResolver;
import pl.ark.chr.buginator.ext.service.ErrorResolver;
import pl.ark.chr.buginator.ext.service.ExternalDataService;
import pl.ark.chr.buginator.repository.*;
import pl.ark.chr.buginator.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Arek on 2017-04-01.
 */
@Service
@Transactional
public class ExternalDataServiceImpl implements ExternalDataService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalDataServiceImpl.class);

    @Autowired
    private AggregatorReflection aggregatorReflection;

    @Autowired
    private AggregatorRepository aggregatorRepository;

    @Autowired
    private ApplicationResolver applicationResolver;

    @Autowired
    private ErrorResolver errorResolver;

    @Autowired
    private ExecutorService innerJobScheduler;

    @Autowired
    private AggregatorLogRepository aggregatorLogRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void saveErrorAndNotifyAggregators(String uniqueKey, String token, ExternalData externalData) {
        if (externalData == null) {
            logger.error("External data is null");
            throw new NullPointerException("External data is null");
        }

        Application application = applicationResolver.resolveApplication(uniqueKey, token, externalData.getApplicationName());

        Error error = errorResolver.resolveError(externalData, application);

        List<Aggregator> aggregators = aggregatorRepository.findByApplicationAndSeverityAndCount(application, error.getSeverity(), error.getCount());

        aggregators.forEach(aggregator -> launchSingleNotification(aggregator, error));
    }

    @SuppressWarnings("unchecked")
    private void launchSingleNotification(Aggregator aggregator, Error error) {
        innerJobScheduler.submit(() -> {
            try {
                AggregatorService aggregatorService = aggregatorReflection.getAggregatorService(aggregator, applicationContext);

                aggregatorService.notifyExternalAggregator(aggregator, error);

                saveSuccessAggregatorLog(aggregator, error);
            } catch (Exception e) {
                logger.error("Error performing notification for aggregator with id: " + aggregator.getId(), e);
                saveFailedAggregatorLog(aggregator, error, e.getLocalizedMessage());
            }
        });
    }

    private void saveSuccessAggregatorLog(Aggregator aggregator, Error error) {
        createAggregatorLog(aggregator, error, AggregatorLogStatus.SUCCESS, null);
    }

    private void saveFailedAggregatorLog(Aggregator aggregator, Error error, String localizedMessage) {
        createAggregatorLog(aggregator, error, AggregatorLogStatus.FAILURE, localizedMessage);
    }

    private void createAggregatorLog(Aggregator aggregator, Error error, AggregatorLogStatus status, String errorDescription) {
        AggregatorLog aggregatorLog = new AggregatorLog();
        if (!ValidationUtil.isBlank(errorDescription)) {
            aggregatorLog.setErrorDescription(errorDescription);
        }
        aggregatorLog.setError(error);
        aggregatorLog.setStatus(status);
        aggregatorLog.setAggregator(aggregator);
        aggregatorLog.setRetryCount(0);
        aggregatorLog.setTimestamp(LocalDateTime.now());

        aggregatorLogRepository.save(aggregatorLog);
    }
}
