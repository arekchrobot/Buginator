package pl.ark.chr.buginator.security.util;

import pl.ark.chr.buginator.domain.auth.*;

public class TestObjectCreator {

    public static OAuth2Client createOAuth2ClientWithAllowedDomains(String allowedDomains) {
        return OAuth2Client.builder((String s) -> s)
                .type(OAuth2ClientType.WEB)
                .accessTokenExpiration(3000)
                .allowedDomains(allowedDomains)
                .build();
    }

    public static OAuth2Client createOAuth2ClientWithAllowedIps(String allowedIps) {
        return OAuth2Client.builder((String s) -> s)
                .type(OAuth2ClientType.API)
                .accessTokenExpiration(3000)
                .allowedIPs(allowedIps)
                .build();
    }

    public static User createUser(Company company, Role role, boolean active) {
        return User.builder()
                .name("TEST_USER")
                .email("test@gmail.com")
                .password("{def}$2a$10$ra/Scxal23zJrh.sh8nQP.LreuuTp0Ez8L9/aeQCA4AzRXct6zlea")
                .active(active)
                .company(company)
                .role(role)
                .build();
    }
}
