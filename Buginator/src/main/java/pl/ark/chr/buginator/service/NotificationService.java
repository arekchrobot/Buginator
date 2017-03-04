package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.data.NotificationData;
import pl.ark.chr.buginator.domain.User;

import java.util.List;

/**
 * Created by Arek on 2017-02-27.
 */
public interface NotificationService {

    List<NotificationData> getNotificationsForUser(String token);

    void markNotificationSeen(Long id);

    void markNotificationsSeen(List<NotificationData> notifications);

    String addTokenForActiveSession(User user);

    void updateTokenConnectionStatus(String token, boolean status);

    void removeTokenForSession(String token);

    boolean checkTokenActivated(String token);
}
