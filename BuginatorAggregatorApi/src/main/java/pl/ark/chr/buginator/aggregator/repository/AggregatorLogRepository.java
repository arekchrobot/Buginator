package pl.ark.chr.buginator.aggregator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.aggregator.domain.Aggregator;
import pl.ark.chr.buginator.aggregator.domain.AggregatorLog;
import pl.ark.chr.buginator.aggregator.domain.AggregatorLogStatus;
import pl.ark.chr.buginator.domain.core.Error;

import java.util.List;

public interface AggregatorLogRepository extends CrudRepository<AggregatorLog, Long> {

    List<AggregatorLog> findByStatus(AggregatorLogStatus status);
    List<AggregatorLog> findByErrorIn(List<Error> errors);
    List<AggregatorLog> findByAggregator(Aggregator aggregator);
}
