package pl.ark.chr.buginator.aggregator.repository;

import pl.ark.chr.buginator.aggregator.domain.Aggregator;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;

import java.util.List;

/**
 * Basic repository to get all aggregator implementations.
 * Used for scheduled notifiying aggregators about new errors
 */
public interface AggregatorRepository extends AbstractAggregatorRepository<Aggregator> {

    List<Aggregator> findByApplicationAndErrorSeverityAndCount(Application application, ErrorSeverity errorSeverity, int count);
}
