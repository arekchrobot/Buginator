package pl.ark.chr.buginator.aggregator.service;

import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Error;

/**
 * Created by Arek on 2017-03-11.
 */
public interface AggregatorService<T extends Aggregator> {

    void notifyExternalAggregator(T aggregator, Error error);
}
