package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.AggregatorLog;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.enums.AggregatorLogStatus;

import java.util.List;

/**
 * Created by Arek on 2017-04-01.
 */
public interface AggregatorLogRepository extends CrudRepository<AggregatorLog, Long> {

    List<AggregatorLog> findByStatus(AggregatorLogStatus status);
    List<AggregatorLog> findByErrorIn(List<Error> errors);
}
