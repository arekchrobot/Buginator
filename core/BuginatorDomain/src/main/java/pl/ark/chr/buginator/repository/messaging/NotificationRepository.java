package pl.ark.chr.buginator.repository.messaging;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.messaging.Notification;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.core.Error;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserAndSeenFalse(User user);
    List<Notification> findByErrorIn(List<Error> errors);
    Optional<Notification> findByUserAndError(User user, Error error);
}
