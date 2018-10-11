package pl.ark.chr.buginator.repository.aggregator;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.ark.chr.buginator.domain.aggregator.Aggregator;
import pl.ark.chr.buginator.domain.core.Application;

import java.util.List;

/**
 * Basic repository to be extended by aggregator implementations
 */
@NoRepositoryBean
public interface AbstractAggregatorRepository<T extends Aggregator> extends CrudRepository<T, Long> {

    List<T> findByApplication(Application application);
}
