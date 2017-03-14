package pl.ark.chr.buginator.aggregator.service;

import org.springframework.stereotype.Component;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Error;

/**
 * Created by Arek on 2017-03-12.
 */
@Component
public class AggregatorServiceValidator {

    public boolean checkErrorSeverityDoesNotMatch(Aggregator aggregator, Error error) {
        return !aggregator.getErrorSeverity().equals(error.getSeverity());
    }

    public boolean checkErrorCountLessTanAggregator(Aggregator aggregator, Error error) {
        return error.getCount() < aggregator.getCount();
    }
}
