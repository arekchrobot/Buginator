package pl.ark.chr.buginator.client;

import org.json.JSONObject;
import pl.ark.chr.buginator.client.sender.AsyncHttpSenderImpl;
import pl.ark.chr.buginator.client.sender.Sender;
import pl.ark.chr.buginator.client.servlet.ServletDataAutoFill;
import pl.ark.chr.buginator.client.util.DataAutoFill;
import pl.ark.chr.buginator.client.util.ErrorCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arek on 2017-04-17.
 */
public class BuginatorClient {

    private final String token;
    private final String uniqueKey;
    private final String appName;

    private Sender notificationSender = new AsyncHttpSenderImpl();
    private ErrorCreator errorCreator = new ErrorCreator();

    private List<DataAutoFill> autoFills;

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

        autoFills = new ArrayList<>();

        if(ServletDataAutoFill.isAvailable()) {
            autoFills.add(new ServletDataAutoFill());
        }
    }

    public void setNotificationSender(Sender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void sendNotification(Throwable exception, Thread thread) {
        sendNotification(exception, thread, ErrorSeverity.ERROR);
    }

    public void sendNotification(Throwable exception, Thread thread, ErrorSeverity errorSeverity) {
        JSONObject objectToSend =errorCreator.createError(exception, thread, appName, errorSeverity);

        for (DataAutoFill dataAutoFill: autoFills) {
            dataAutoFill.autoFillData(objectToSend);
        }

        notificationSender.send(objectToSend.toString(), uniqueKey, token);
    }

    public void close() {
        notificationSender.close();
        BuginatorExceptionHandler.disableHandler();
    }
}
