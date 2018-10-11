package pl.ark.chr.buginator.ext.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.messaging.Notification;
import pl.ark.chr.buginator.ext.service.NotificationSender;
import pl.ark.chr.buginator.repository.messaging.NotificationRepository;
import pl.ark.chr.buginator.util.TokenGenerator;
//import pl.ark.chr.buginator.websocket.NotificationEndpoint;

import java.util.Optional;

/**
 * Created by Arek on 2017-05-04.
 */
@Service
@Transactional
public class NotificationSenderImpl implements NotificationSender {

    @Autowired
    private NotificationRepository notificationRepository;

//    @Autowired
//    private NotificationEndpoint notificationEndpoint;

//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void createAndSendNotifications(Error error, Application application) {
        application.getApplicationUsers().stream()
                .map(ua -> ua.getUser())
                .forEach(user -> {
                    createNotification(user, error);
                    String token = TokenGenerator.generateToken(user);
//                    simpMessagingTemplate.convertAndSend(NotificationEndpoint.TOPIC_NOTIFICATION_URL + token, notificationEndpoint.sendNotification(token));
                });
    }

    private void createNotification(User user, Error error) {
        Optional<Notification> possibleDuplicate = notificationRepository.findByUserAndError(user, error);
        if(!possibleDuplicate.isPresent()) {
            Notification notification = new Notification();
            notification.setError(error);
            notification.setSeen(false);
            notification.setUser(user);

            notificationRepository.save(notification);
        }
    }
}
