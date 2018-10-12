package pl.ark.chr.buginator.aggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ark.chr.buginator.aggregator.util.AggregatorSenderValidator;
import pl.ark.chr.buginator.aggregator.domain.Aggregator;
import pl.ark.chr.buginator.domain.core.Error;

/**
 * Skeleton implementation for AggregatorService.
 * Provides necessary validation before sending Error
 */
public abstract class AbstractAggregatorSender<T extends Aggregator> implements AggregatorSender<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAggregatorSender.class);

    @Override
    public void notifyExternalAggregator(T aggregator, Error error) {
        if (AggregatorSenderValidator.contractNotMatch(aggregator, error)) {
            LOGGER.info("Error has invalid severity or counts does not match for id: " + error.getId()
                    + " for aggregator: " + aggregator.getId());
            return;
        }

        sendData(aggregator, error);
    }

    protected abstract void sendData(T aggregator, Error error);
}