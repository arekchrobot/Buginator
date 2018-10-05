//package pl.ark.chr.buginator.config.websocket;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptorAdapter;
//import pl.ark.chr.buginator.exceptions.TokenAlreadyInUseException;
//import pl.ark.chr.buginator.service.NotificationService;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Created by Arek on 2017-02-21.
// */
//public class BuginatorChannelInterceptorAdapter extends ChannelInterceptorAdapter {
//
//    private static final Logger logger = LoggerFactory.getLogger(BuginatorChannelInterceptorAdapter.class);
//
//    private static final String DESTINATION_SPLITTER = "/";
//
//    private static volatile Map<String, String> sessionTokens = new ConcurrentHashMap<>();
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
//            String[] destinations = headerAccessor.getDestination().split(DESTINATION_SPLITTER);
//            String token = destinations[destinations.length - 1];
//
//            if (notificationService.checkTokenActivated(token)) {
//                throw new TokenAlreadyInUseException("Token: " + token + " is already used");
//            } else {
//                logger.info("Activating websocket token: " + token);
//                notificationService.updateTokenConnectionStatus(token, true);
//                sessionTokens.put(headerAccessor.getSessionId(), token);
//            }
//        } else if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
//            if(sessionTokens.containsKey(headerAccessor.getSessionId())) {
//                String token = sessionTokens.get(headerAccessor.getSessionId());
//                logger.info("Deactivating websocket token: " + token);
//                notificationService.updateTokenConnectionStatus(token, false);
//                sessionTokens.remove(headerAccessor.getSessionId());
//            }
//        }
//
//        return message;
//    }
//}
