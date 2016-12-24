package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Error;

import java.time.LocalDate;

/**
 * Created by Arek on 2016-09-29.
 */
public interface ErrorRepository extends CrudRepository<Error, Long> {

    Long countByApplication(Application application);

    Long countByApplicationAndLastOccurrenceGreaterThanEqual(Application application, LocalDate lastOccurrence);
}
