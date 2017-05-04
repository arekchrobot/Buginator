package pl.ark.chr.buginator.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import pl.ark.chr.buginator.data.NotificationData;
import pl.ark.chr.buginator.exceptions.TokenNotActiveException;
import pl.ark.chr.buginator.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arek on 2017-02-21.
 */
@Controller
public class NotificationEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(NotificationEndpoint.class);

    public static final String TOPIC_NOTIFICATION_URL = "/topic/notification/";

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/notification/send/{token}")
    @SendTo(TOPIC_NOTIFICATION_URL + "{token}")
    public List<NotificationData> sendNotification(@DestinationVariable String token) {
        if (notificationService.checkTokenActivated(token)) {
            logger.info("Returning all notifications for token: " + token);
            return notificationService.getNotificationsForUser(token);
        } else {
            return new ArrayList<>();
        }
    }
}
