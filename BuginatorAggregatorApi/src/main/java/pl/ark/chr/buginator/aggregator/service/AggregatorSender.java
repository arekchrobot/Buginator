package pl.ark.chr.buginator.aggregator.service;

import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Error;

/**
 * The strategy responsible for sending Error to an external notification platform
 */
public interface AggregatorSender<T extends Aggregator> {

    /**
     * Checks if the sender is valid for given aggregatorClass
     * @param aggregatorClass Aggregator class defined in aggregatorClass field
     * @return whether the strategy can be used or not
     */
    boolean isValid(String aggregatorClass);

    /**
     * Validates and send Error to given notification platform
     * Validation should be against Error severity and count
     * @param aggregator Connection details for notification platform
     * @param error Data to be send
     */
    void notifyExternalAggregator(T aggregator, Error error);
}
