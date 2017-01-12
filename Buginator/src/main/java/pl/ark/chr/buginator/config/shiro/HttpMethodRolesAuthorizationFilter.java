package pl.ark.chr.buginator.config.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arek on 2016-09-28.
 */
public class HttpMethodRolesAuthorizationFilter extends RolesAuthorizationFilter {

    private enum HttpMethod {

        GET, DELETE, HEAD, MKCOL, OPTIONS, POST, PUT, TRACE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public static HttpMethod parse(String method) {
            for (HttpMethod httpMethod : values()) {
                if (httpMethod.toString().equals(method.toLowerCase())) {
                    return httpMethod;
                }
            }
            throw new IllegalArgumentException("Unknown HTTP method: " + method);
        }
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws
            IOException {

        Subject subject = this.getSubject(request, response);

        if (subject.getPrincipals() == null || subject.getPrincipals().isEmpty()) {
            //no user found so no need to check
            return false;
        }

        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return true;
        }

        final Map<HttpMethod, String> methodToRoleMapping = new HashMap<>();
        for (String rolesEntry : rolesArray) {
            final String[] kv = rolesEntry.split("=");
            final HttpMethod httpMethod = HttpMethod.parse(kv[0]);
            methodToRoleMapping.put(httpMethod, kv[1]);
        }

        final HttpMethod requestMethod = HttpMethod.parse(((HttpServletRequest) request).getMethod());
        final String role = methodToRoleMapping.get(requestMethod);
        return subject.isPermitted(role);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = this.getSubject(request, response);
        if (subject.getPrincipal() == null) {
            //show user 401 instead of redirect
            //front-end is handled by angular so no need for shiro to handle this
            WebUtils.toHttp(response).sendError(401);
        } else {
            String unauthorizedUrl = this.getUnauthorizedUrl();
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                WebUtils.toHttp(response).sendError(401);
            }
        }

        return false;
    }
}
