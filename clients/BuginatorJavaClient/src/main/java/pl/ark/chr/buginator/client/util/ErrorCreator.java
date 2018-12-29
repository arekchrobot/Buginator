package pl.ark.chr.buginator.client.util;

import org.json.JSONObject;
import pl.ark.chr.buginator.client.ErrorSeverity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Arek on 2017-04-30.
 */
public class ErrorCreator {

    private StackTraceCreator stackTraceCreator;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ErrorCreator() {
        this.stackTraceCreator = new StackTraceCreator();
    }

    public JSONObject createError(Throwable throwable, Thread thread, String appName, ErrorSeverity errorSeverity) {
        JSONObject jsonObject = new JSONObject();

        Date now = new Date();

        jsonObject.put("applicationName", appName);
        jsonObject.put("errorTitle", throwable.getClass().getName());
        jsonObject.put("errorDescription", throwable.getLocalizedMessage());
        jsonObject.put("errorSeverity", errorSeverity.toString());
        jsonObject.put("dateTimeString", dateTimeFormat.format(now));
        jsonObject.put("dateString", dateFormat.format(now));
        jsonObject.put("stackTrace", stackTraceCreator.createStackTrace(throwable, thread));

        return jsonObject;
    }
}
