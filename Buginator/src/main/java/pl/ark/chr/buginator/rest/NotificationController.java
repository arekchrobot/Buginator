package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.data.NotificationData;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.rest.annotations.PUT;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.NotificationService;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    @PUT("/{id}")
    public Map markNotificationSeen(HttpServletRequest request, HttpServletResponse response, Locale locale, @PathVariable("id") Long id) throws RestException {
        logger.info("Marking notification with id: " + id + " seen with user: " + sessionUtil.getCurrentUser(request).getEmail());

        notificationService.markNotificationSeen(id);

        return Collections.singletonMap("success", "Notification marked successfully");
    }

    @PUT("/")
    public Map markAllNotificationsSeen(HttpServletRequest request, HttpServletResponse response, Locale locale,
                                        @RequestBody List<NotificationData> notifications) throws RestException {

        logger.info("Marking notifications with ids: " + notifications.stream()
                .map(notificationData -> notificationData.getId().toString())
                .collect(Collectors.joining(","))
                + " seen with user: " + sessionUtil.getCurrentUser(request).getEmail());

        notificationService.markNotificationsSeen(notifications);

        return Collections.singletonMap("success", "Notification marked successfully");
    }
}
