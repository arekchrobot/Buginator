package pl.ark.chr.buginator.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.enums.ErrorSeverity;
import pl.ark.chr.buginator.domain.enums.ErrorStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Arek on 2016-09-29.
 */
public interface ErrorRepository extends CrudRepository<Error, Long> {

    Long countByApplication(Application application);

    Long countByApplicationAndLastOccurrenceGreaterThanEqual(Application application, LocalDate lastOccurrence);

    List<Error> findByApplicationAndLastOccurrenceGreaterThanEqual(Application application, LocalDate lastOccurrence);

    List<Error> findByApplication(Application application);

    List<Error> findByStatusAndLastOccurrenceLessThanEqual(ErrorStatus status, LocalDate lastOccurrence);

    @Query("select e from Error e " +
            "where e.title = :title " +
            "and e.description = :description " +
            "and e.severity = :severity " +
            "and (e.queryParams = :queryParams or e.queryParams is null) " +
            "and (e.requestUrl = :requestUrl or e.requestUrl is null) " +
            "and e.application = :application")
    List<Error> findDuplicateError(@Param("title") String title,
                                   @Param("description") String description,
                                   @Param("severity") ErrorSeverity severity,
                                   @Param("queryParams") String queryParams,
                                   @Param("requestUrl") String requestUrl,
                                   @Param("application") Application application);
}
