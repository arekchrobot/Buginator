package pl.ark.chr.buginator.client.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arek on 2017-04-17.
 */
public class BuginatorServletRequestListener implements ServletRequestListener {

    private static final ThreadLocal<HttpServletRequest> httpServletRequest = new ThreadLocal<>();

    public static HttpServletRequest getServletRequest() {
        return httpServletRequest.get();
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        ServletRequest servletRequest = servletRequestEvent.getServletRequest();
        if (servletRequest instanceof HttpServletRequest) {
            httpServletRequest.set((HttpServletRequest) servletRequest);
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        httpServletRequest.remove();
    }
}
