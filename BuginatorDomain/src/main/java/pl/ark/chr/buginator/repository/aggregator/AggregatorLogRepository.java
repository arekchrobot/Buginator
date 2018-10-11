package pl.ark.chr.buginator.repository.aggregator;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.aggregator.Aggregator;
import pl.ark.chr.buginator.domain.aggregator.AggregatorLog;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.aggregator.AggregatorLogStatus;

import java.util.List;

public interface AggregatorLogRepository extends CrudRepository<AggregatorLog, Long> {

    List<AggregatorLog> findByStatus(AggregatorLogStatus status);
    List<AggregatorLog> findByErrorIn(List<Error> errors);
    List<AggregatorLog> findByAggregator(Aggregator aggregator);
}
