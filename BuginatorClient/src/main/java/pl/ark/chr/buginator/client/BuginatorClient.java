package pl.ark.chr.buginator.client;

import pl.ark.chr.buginator.client.sender.AsyncHttpSenderImpl;
import pl.ark.chr.buginator.client.sender.Sender;

/**
 * Created by Arek on 2017-04-17.
 */
public class BuginatorClient {

    private final String token;
    private final String uniqueKey;
    private final String appName;

    private Sender notificationSender = new AsyncHttpSenderImpl();

    public BuginatorClient(String token, String uniqueKey, String appName) {
        this(token, uniqueKey, appName, true);
    }

    public BuginatorClient(String token, String uniqueKey, String appName, boolean enableUncaughtHandler) {
        this.token = token;
        this.uniqueKey = uniqueKey;
        this.appName = appName;

        if(enableUncaughtHandler) {
            BuginatorExceptionHandler.enableHandler(this);
        }
    }

    public void setNotificationSender(Sender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void sendNotification(Throwable exception) {
        sendNotification(exception, ErrorSeverity.ERROR);
    }

    public void sendNotification(Throwable exception, ErrorSeverity error) {
        notificationSender.send(null, uniqueKey, token);
    }

    public void close() {
        notificationSender.close();
        BuginatorExceptionHandler.disableHandler();
    }
}
