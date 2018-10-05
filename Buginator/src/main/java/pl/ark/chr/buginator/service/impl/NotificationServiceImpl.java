package pl.ark.chr.buginator.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.data.NotificationData;
import pl.ark.chr.buginator.domain.Notification;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.TokenNotExistException;
import pl.ark.chr.buginator.repository.NotificationRepository;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.NotificationService;
import pl.ark.chr.buginator.util.TokenGenerator;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-02-27.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private static final String TOKEN_SPLITTER = ":";

    private static volatile Map<String, Boolean> activeTokens = new HashMap<>();

    private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private static final Lock readLock = readWriteLock.readLock();
    private static final Lock writeLock = readWriteLock.writeLock();

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<NotificationData> getNotificationsForUser(String token) {
        String decodedToken = TokenGenerator.decode(token);
        String userEmail = decodedToken.split(TOKEN_SPLITTER)[0];
        User user = userRepository.findByEmail(userEmail).get();

        return notificationRepository.findByUserAndSeenFalse(user).stream()
                .map(n -> new NotificationData(n))
                .collect(Collectors.toList());
    }

    @Override
    public void removeNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    @Async
    public void removeNotifications(List<NotificationData> notifications) {
        notifications.forEach(notification -> removeNotification(notification.getId()));
    }

    @Override
    public String addTokenForActiveSession(User user) {
        String token = TokenGenerator.generateToken(user);

        try {
            writeLock.lock();
            if (!activeTokens.containsKey(token)) {
                logger.info("Creating websocket access token for: " + user.getEmail() + " with value: " + token);
                activeTokens.put(token, false);
            }
        } finally {
            writeLock.unlock();
        }

        return token;
    }

    @Override
    public void updateTokenConnectionStatus(String token, boolean status) {
        try {
            writeLock.lock();

            if (activeTokens.containsKey(token)) {
                logger.info("Updating connection status of websocket for token: " + token + " with value: " + status);
                activeTokens.put(token, status);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeTokenForSession(String token) {
        try {
            writeLock.lock();

            logger.info("Removing token from active tokens: " + token);
            activeTokens.remove(token);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean checkTokenActivated(String token) {
        Boolean tokenActivated;

        try {
            readLock.lock();

            tokenActivated = activeTokens.get(token);
        } finally {
            readLock.unlock();
        }

        if(tokenActivated == null) {
            logger.warn("No token is registered: " + token);
            return false;
//            throw new TokenNotExistException("No token is registered: " + token);
        }
        return tokenActivated;
    }

//    private String generateToken(User user) {
//        String rawToken = user.getEmail() + TOKEN_SPLITTER + user.getCompany().getId();
//        return encode(rawToken);
//    }
//
//    private String encode(String rawToken) {
//        return Base64.getEncoder().encodeToString(
//                Base64.getEncoder().encode(rawToken.getBytes()));
//    }
//
//    private String decode(String encodedToken) {
//        byte[] encodedBytes = Base64.getDecoder().decode(
//                Base64.getDecoder().decode(encodedToken));
//        return new String(encodedBytes);
//    }
}
