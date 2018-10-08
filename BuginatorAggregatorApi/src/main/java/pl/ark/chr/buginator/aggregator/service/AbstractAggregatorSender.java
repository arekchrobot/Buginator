package pl.ark.chr.buginator.aggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ark.chr.buginator.aggregator.util.AggregatorSenderValidator;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Error;

/**
 * Skeleton implementation for AggregatorService.
 * Provides necessary validation before sending Error
 */
public abstract class AbstractAggregatorSender<T extends Aggregator> implements AggregatorSender<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAggregatorSender.class);

    @Override
    public void notifyExternalAggregator(T aggregator, Error error) {
        if (AggregatorSenderValidator.contractNotMatch(aggregator, error)) {
            logger.info("Error has invalid severity or counts does not match for id: " + error.getId());
            //TODO: throw better error
            throw new RuntimeException("Error has invalid severity or counts does not match for id: " + error.getId());
        }

        sendData(aggregator, error);
    }

    protected abstract void sendData(T aggregator, Error error);
}