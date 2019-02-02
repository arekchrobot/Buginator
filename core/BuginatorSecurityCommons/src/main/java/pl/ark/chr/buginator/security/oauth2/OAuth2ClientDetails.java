package pl.ark.chr.buginator.security.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import pl.ark.chr.buginator.domain.auth.OAuth2Client;

import java.util.*;

public class OAuth2ClientDetails implements ClientDetails {

    private static final long serialVersionUID = 7037913737782889343L;

    static final String ALLOWED_DOMAINS = "allowedDomains";
    static final String ALLOWED_IPS = "allowedIPs";

    private OAuth2Client client;
    private int defaultExpiration;

    OAuth2ClientDetails(OAuth2Client client, int defaultExpiration) {
        Objects.requireNonNull(client);
        this.defaultExpiration = defaultExpiration;
        this.client = client;
    }

    @Override
    public String getClientId() {
        return client.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        return Set.of("resource");
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return client.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return Set.of("read", "write");
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return Set.of("password", "authorization_code");
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return new HashSet<>();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new LinkedHashSet<>();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return client.getAccessTokenExpiration() <= 0 ? defaultExpiration : client.getAccessTokenExpiration();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return defaultExpiration;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        switch (client.getType()) {
            case API:
                Map<String, Object> allowedIps = new HashMap<>();
                allowedIps.put(ALLOWED_IPS, client.getAllowedIPs());
                return allowedIps;
            case WEB:
                Map<String, Object> allowedDomains = new HashMap<>();
                allowedDomains.put(ALLOWED_DOMAINS, client.getAllowedDomains());
                return allowedDomains;
            default:
                return new HashMap<>();
        }
    }
}
