package pl.ark.chr.buginator.domain.auth;

import org.apache.commons.lang3.RandomStringUtils;
import pl.ark.chr.buginator.domain.BaseEntity;

import javax.persistence.*;
import java.util.function.Function;

@Entity
@Table(name = "buginator_oauth2_client",
        indexes = {
                @Index(name = "client_id_index", columnList = "client_id")
        })
public class OAuth2Client extends BaseEntity<OAuth2Client> {

    private static final long serialVersionUID = 7926737756067305541L;
    private static final int TOKEN_LENGTH = 17;

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;
    @Column(name = "client_secret", nullable = false, unique = true)
    private String clientSecret;
    @Column(name = "access_token_expiration")
    private int accessTokenExpiration;
    @Enumerated(EnumType.STRING)
    private OAuth2ClientType type;
    @Column(name = "allowed_domains")
    private String allowedDomains;
    @Column(name = "allowed_ips")
    private String allowedIPs;

    protected OAuth2Client() {
    }

    private OAuth2Client(Builder builder) {
        setClientId(builder.clientId);
        setClientSecret(builder.clientSecret);
        setAccessTokenExpiration(builder.accessTokenExpiration);
        setType(builder.type);
        setAllowedDomains(builder.allowedDomains);
        setAllowedIPs(builder.allowedIPs);
    }

    public static Builder builder(Function<String, String> encoder) {
        return new Builder(encoder);
    }

    public String getClientId() {
        return clientId;
    }

    protected void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    protected void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    protected void setAccessTokenExpiration(int accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public OAuth2ClientType getType() {
        return type;
    }

    protected void setType(OAuth2ClientType type) {
        this.type = type;
    }

    public String getAllowedDomains() {
        return allowedDomains;
    }

    protected void setAllowedDomains(String allowedDomains) {
        this.allowedDomains = allowedDomains;
    }

    public String getAllowedIPs() {
        return allowedIPs;
    }

    protected void setAllowedIPs(String allowedIPs) {
        this.allowedIPs = allowedIPs;
    }

    private static String generateToken() {
        return RandomStringUtils.random(TOKEN_LENGTH, true, true);
    }


    public static final class Builder {
        private String clientId;
        private String clientSecret;
        private int accessTokenExpiration;
        private OAuth2ClientType type;
        private String allowedDomains;
        private String allowedIPs;

        private Builder(Function<String, String> encoder) {
            clientId = encoder.apply(generateToken());
            clientSecret = encoder.apply(generateToken());
        }

        public Builder accessTokenExpiration(int val) {
            accessTokenExpiration = val;
            return this;
        }

        public Builder type(OAuth2ClientType val) {
            type = val;
            return this;
        }

        public Builder allowedDomains(String val) {
            allowedDomains = val;
            return this;
        }

        public Builder allowedIPs(String val) {
            allowedIPs = val;
            return this;
        }

        public OAuth2Client build() {
            return new OAuth2Client(this);
        }
    }
}
