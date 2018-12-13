package pl.ark.chr.buginator.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import pl.ark.chr.buginator.domain.auth.OAuth2Client;

import java.util.*;

public class OAuth2ClientDetails implements ClientDetails {

    private OAuth2Client client;
    private int defaultExpiration;

    public OAuth2ClientDetails(OAuth2Client client, int defaultExpiration) {
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
        return false;
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
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return client.getAccessTokenExpiration() <= 0 ? defaultExpiration : client.getAccessTokenExpiration();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return null;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        switch (client.getType()) {
            case API:
                return Collections.singletonMap("allowedIPs", client.getAllowedIPs());
            case WEB:
                return Collections.singletonMap("allowedDomains", client.getAllowedDomains());
            case MOBILE:
            default:
                return null;
        }
    }
}
