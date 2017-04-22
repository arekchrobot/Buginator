package pl.ark.chr.buginator.client.servlet;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.ark.chr.buginator.client.util.DataAutoFill;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Arek on 2017-04-17.
 */
public class ServletDataAutoFill implements DataAutoFill {

    public static boolean isAvailable() {
        try {
            Class.forName("javax.servlet.ServletRequestListener", false,
                    ServletDataAutoFill.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    @Override
    public void autoFillData(JSONObject object) {
        HttpServletRequest request = BuginatorServletRequestListener.getServletRequest();

        if (request == null) {
            return;
        }

        object.put("userAgentString", request.getHeader("User-Agent"));
        object.put("requestUrl", request.getRequestURL().toString());
        object.put("queryParams", request.getQueryString());
        object.put("requestMethod", request.getMethod());
        object.put("requestParams", createRequestParams(request));
        object.put("requestHeaders", createRequestHeaders(request));
    }

    private JSONArray createRequestParams(HttpServletRequest request) {
        JSONArray requestParams = new JSONArray();
        for (Map.Entry<String, String[]> params : request.getParameterMap().entrySet()) {
            requestParams.put(params.getKey() + "=" + Arrays.toString(params.getValue()));
        }
        return requestParams;
    }

    private JSONArray createRequestHeaders(HttpServletRequest request) {
        JSONArray requestHeaders = new JSONArray();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            if(!key.equalsIgnoreCase("User-Agent")) {
                requestHeaders.put(key + "=" + request.getHeader(key));
            }
        }
        return requestHeaders;
    }
}
