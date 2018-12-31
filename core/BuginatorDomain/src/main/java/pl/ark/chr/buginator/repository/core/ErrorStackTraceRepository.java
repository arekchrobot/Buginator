package pl.ark.chr.buginator.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.core.ErrorStackTrace;

public interface ErrorStackTraceRepository extends JpaRepository<ErrorStackTrace, Long> {
}
