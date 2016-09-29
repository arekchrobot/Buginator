package pl.ark.chr.buginator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.ark.chr.buginator.domain.Aggregator;

/**
 * Created by Arek on 2016-09-29.
 */
@NoRepositoryBean
public interface AggregatorRepository<T extends Aggregator> extends JpaRepository<T, Long> {
}
