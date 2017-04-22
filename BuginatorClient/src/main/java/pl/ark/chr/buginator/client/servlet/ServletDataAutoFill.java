package pl.ark.chr.buginator.client.servlet;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.ark.chr.buginator.client.util.DataAutoFill;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
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

        if(request == null) {
            return;
        }

        object.put("userAgentString", request.getHeader("User-Agent"));
        object.put("requestUrl", request.getRequestURL().toString());
        object.put("queryParams", request.getQueryString());

//        object.put("requestMethod", request.getMethod());
//
//        JSONArray array = new JSONArray();
//        for (Map.Entry<String, String[]> params : request.getParameterMap().entrySet()) {
//            array.put(params.getKey()+"="+ Arrays.toString(params.getValue()));
//        }
//        object.put("requestParams", array);
    }
}
