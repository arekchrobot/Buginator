package pl.ark.chr.buginator.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arek on 2016-09-29.
 */
public class HttpUtil {

    public static String generateOriginalUrl(HttpServletRequest request) {
        return request.getRequestURL().append("?")
                .append(request.getQueryString() != null ? request.getQueryString() : "").toString();
    }
}
