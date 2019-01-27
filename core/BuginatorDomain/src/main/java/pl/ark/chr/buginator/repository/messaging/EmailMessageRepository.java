package pl.ark.chr.buginator.repository.messaging;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.messaging.EmailMessage;

public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {
}
