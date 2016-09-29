package pl.ark.chr.buginator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.ErrorStackTrace;

/**
 * Created by Arek on 2016-09-29.
 */
public interface ErrorStackTraceRepository extends JpaRepository<ErrorStackTrace, Long> {
}
