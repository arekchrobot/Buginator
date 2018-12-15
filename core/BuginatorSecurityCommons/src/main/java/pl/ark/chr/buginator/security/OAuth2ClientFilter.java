package pl.ark.chr.buginator.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class OAuth2ClientFilter implements Filter {

    private ClientDetailsService clientDetailsService;

    public OAuth2ClientFilter(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ServletException("Authentication is required");
        }

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        ClientDetails clientDetails = clientDetailsService
                .loadClientByClientId(oAuth2Authentication.getOAuth2Request().getClientId());

        clientDetails.getAdditionalInformation()
                .computeIfPresent(OAuth2ClientDetails.ALLOWED_DOMAINS, (key, value) ->
                        validateDomain((HttpServletRequest) servletRequest, (String) value));

        clientDetails.getAdditionalInformation()
                .computeIfPresent(OAuth2ClientDetails.ALLOWED_IPS, (key, value) ->
                        validateIP((HttpServletRequest) servletRequest, (String) value));

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String validateIP(HttpServletRequest servletRequest, String allowedIps) {
        String requestIp = getRequestIpAddres(servletRequest);
        if (!allowedIps.contains(requestIp)) {
            throw new RuntimeException("IP not supported for given client");
        }
        return allowedIps;
    }

    private String validateDomain(HttpServletRequest servletRequest, String allowedDomains) {
        String requestDomain = getRequestDomain(servletRequest);
        if (!allowedDomains.contains(requestDomain)) {
            throw new RuntimeException("Domain not supported for given client");
        }
        return allowedDomains;
    }

    String getRequestIpAddres(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    String getRequestDomain(HttpServletRequest request) {
        String domain = request.getServerName();
        domain += portNotDefault(request) ? ":" + request.getServerPort() : "";
        return domain;
    }

    private boolean portNotDefault(HttpServletRequest request) {
        return request.getServerPort() != 80 || request.getServerPort() != 443;
    }
}
