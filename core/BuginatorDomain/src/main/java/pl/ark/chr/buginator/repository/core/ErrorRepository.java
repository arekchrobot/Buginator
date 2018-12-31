package pl.ark.chr.buginator.repository.core;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

import java.time.LocalDate;
import java.util.List;

public interface ErrorRepository extends JpaRepository<Error, Long> {

    Long countByApplication(Application application);

    Long countByApplicationAndLastOccurrenceGreaterThanEqual(Application application, LocalDate lastOccurrence);

    List<Error> findByApplicationAndLastOccurrenceGreaterThanEqual(Application application, LocalDate lastOccurrence);

    List<Error> findByApplication(Application application);

    List<Error> findByStatusAndLastOccurrenceLessThanEqual(ErrorStatus status, LocalDate lastOccurrence);

    @Query("select e from Error e " +
            "where e.title = :title " +
            "and e.description = :description " +
            "and e.severity = :severity " +
            "and (e.requestMethod = :requestMethod or e.requestMethod is null) " +
            "and (e.requestUrl = :requestUrl or e.requestUrl is null) " +
            "and e.application = :application")
    List<Error> findDuplicateError(@Param("title") String title,
                                   @Param("description") String description,
                                   @Param("severity") ErrorSeverity severity,
                                   @Param("requestMethod") String requestMethod,
                                   @Param("requestUrl") String requestUrl,
                                   @Param("application") Application application);
}
