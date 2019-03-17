package pl.ark.chr.buginator.security.actuator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ActuatorSecurityFilter implements Filter {

    static final String ACTUATOR_URI = "/actuator";
    static final String AUTH_HEADER = "Authorization";

    private final String username;
    private final String password;

    ActuatorSecurityFilter(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        if (httpServletRequest.getRequestURI().startsWith(ACTUATOR_URI)) {

            final String authorization = httpServletRequest.getHeader(AUTH_HEADER);

            HttpServletResponse res = (HttpServletResponse) servletResponse;

            if (isBasicAuth(authorization)) {

                final String[] values = getCredentials(authorization);

                if (hasAccessToResource(values)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    unauthorized(res);
                }
            } else {
                unauthorized(res);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    void unauthorized(HttpServletResponse res) throws IOException {
        res.setHeader("WWW-Authenticate", "Basic realm=\"Protected\"");
        res.sendError(401, "Unauthorized");
    }

    boolean isBasicAuth(String authorization) {
        return authorization != null && authorization.toLowerCase().startsWith("basic");
    }

    private String[] getCredentials(String authorization) {
        String base64Credentials = authorization.substring("Basic".length()).trim();

        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        // credentials = username:password
        return credentials.split(":", 2);
    }

    boolean hasAccessToResource(String[] values) {
        return values[0].equals(username) && values[1].equals(password);
    }
}
