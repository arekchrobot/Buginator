package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.Notification;
import pl.ark.chr.buginator.domain.User;

import java.util.List;

/**
 * Created by Arek on 2016-09-29.
 */
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    List<Notification> findByUserAndSeenFalse(User user);
}
