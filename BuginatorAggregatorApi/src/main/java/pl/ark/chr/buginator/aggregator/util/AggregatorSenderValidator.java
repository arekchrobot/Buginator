package pl.ark.chr.buginator.aggregator.util;

import pl.ark.chr.buginator.domain.aggregator.Aggregator;
import pl.ark.chr.buginator.domain.core.Error;

/**
 * Validation util for Error and Aggregator
 */
public final class AggregatorSenderValidator {

    static boolean checkErrorSeverityDoesNotMatch(Aggregator aggregator, Error error) {
        return !aggregator.getErrorSeverity().equals(error.getSeverity());
    }

    static boolean checkErrorCountLessThanAggregator(Aggregator aggregator, Error error) {
        return error.getCount() < aggregator.getCount();
    }

    public static boolean contractNotMatch(Aggregator aggregator, Error error) {
        return checkErrorSeverityDoesNotMatch(aggregator, error)
                || checkErrorCountLessThanAggregator(aggregator, error);
    }
}
