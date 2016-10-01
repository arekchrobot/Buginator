package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.Error;

/**
 * Created by Arek on 2016-09-29.
 */
public interface ErrorRepository extends CrudRepository<Error, Long> {
}
