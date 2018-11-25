package pl.ark.chr.buginator.repository.core;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.core.ErrorStackTrace;

public interface ErrorStackTraceRepository extends CrudRepository<ErrorStackTrace, Long> {
}
