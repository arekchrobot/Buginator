package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Company;

import java.util.List;

/**
 * Created by Arek on 2017-03-13.
 */
@NoRepositoryBean
public interface AbstractAggregatorRepository<T extends Aggregator> extends CrudRepository<T, Long> {

    List<T> findByApplication(Application application);
}
