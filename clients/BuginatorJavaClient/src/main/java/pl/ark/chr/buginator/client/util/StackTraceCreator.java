package pl.ark.chr.buginator.client.util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Arek on 2017-04-22.
 */
public class StackTraceCreator {

    public JSONArray createStackTrace(Throwable throwable, Thread thread) {
        JSONArray stackTrace = new JSONArray();

        int i = 1;
        stackTrace.put(createFirstLine(throwable, thread, i));
        i++;

        i = parseStackTrace(stackTrace, throwable, i);

        while(throwable.getCause() != null) {
            throwable = throwable.getCause();
            stackTrace.put(createCausedByLine(throwable, i));
            i++;
            i = parseStackTrace(stackTrace, throwable, i);
        }

        return stackTrace;
    }

    private int parseStackTrace(JSONArray stackTrace, Throwable throwable, int i) {
        for (StackTraceElement el : throwable.getStackTrace()) {
            stackTrace.put(createStackTraceLine(el, i));
            i++;
        }

        return i;
    }

    private JSONObject createFirstLine(Throwable throwable, Thread thread, int i) {
        return createStackTraceElement(i,"Exception in thread \"" + thread.getName() + "\": " + throwable.getClass().getName() + ": " + throwable.getLocalizedMessage());
    }

    private JSONObject createCausedByLine(Throwable throwable, int i) {
        return createStackTraceElement(i, "Caused by: " + throwable.getClass().getName() + ": " + throwable.getLocalizedMessage());
    }

    private JSONObject createStackTraceLine(StackTraceElement el, int i) {
        return createStackTraceElement(i, "at " + el.toString());
    }

    private JSONObject createStackTraceElement(int order, String description) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("order", order);
        jsonObject.put("description", description);

        return jsonObject;
    }
}
