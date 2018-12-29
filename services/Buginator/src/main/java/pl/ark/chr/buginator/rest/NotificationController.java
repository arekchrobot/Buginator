package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.data.NotificationData;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.rest.annotations.DELETE;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.NotificationService;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-03-03.
 */
@RestController("/notification")
public class NotificationController {

    private final static Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SessionUtil sessionUtil;

    @DELETE("/{id}")
    public void markNotificationSeen(HttpServletRequest request, HttpServletResponse response, Locale locale, @PathVariable("id") Long id) throws RestException {
        logger.info("Deleting notification with id: " + id + " with user: " + sessionUtil.getCurrentUserEmail(request));

        notificationService.removeNotification(id);
    }

    @DELETE("/")
    public void markAllNotificationsSeen(HttpServletRequest request, HttpServletResponse response, Locale locale,
                                        @RequestBody List<NotificationData> notifications) throws RestException {

        logger.info("Marking notifications with ids: " + notifications.stream()
                .map(notificationData -> notificationData.getId().toString())
                .collect(Collectors.joining(","))
                + " seen with user: " + sessionUtil.getCurrentUserEmail(request));

        notificationService.removeNotifications(notifications);
    }
}
