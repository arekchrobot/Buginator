package pl.ark.chr.buginator.aggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Error;

/**
 * Created by Arek on 2017-03-12.
 */
@Component
public abstract class AbstractAggregatorService<T extends Aggregator> implements AggregatorService<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAggregatorService.class);

    @Autowired
    private AggregatorServiceValidator aggregatorServiceValidator;

    @Override
    public void notifyExternalAggregator(T aggregator, Error error) {
        if (aggregatorServiceValidator.checkErrorSeverityDoesNotMatch(aggregator, error) ||
                aggregatorServiceValidator.checkErrorCountLessTanAggregator(aggregator, error)) {
            logger.info("Error has invalid severity or counts does not match for id: " + error.getId());
            return;
        }

        notifyExternalAggregatorInternal(aggregator, error);
    }

    protected abstract void notifyExternalAggregatorInternal(T aggregator, Error error);
}