package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.enums.ErrorSeverity;

import java.util.List;

/**
 * Created by Arek on 2016-09-29.
 */
public interface AggregatorRepository extends AbstractAggregatorRepository<Aggregator> {

    List<Aggregator> findByApplicationAndErrorSeverityAndCount(Application application, ErrorSeverity errorSeverity, int count);
}
