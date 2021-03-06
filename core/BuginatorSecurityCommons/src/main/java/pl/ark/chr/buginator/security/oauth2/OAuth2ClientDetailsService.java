package pl.ark.chr.buginator.security.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import pl.ark.chr.buginator.repository.auth.OAuth2ClientRepository;

public class OAuth2ClientDetailsService implements ClientDetailsService {

    private OAuth2ClientRepository oAuth2ClientRepository;
    private int expiration;

    public OAuth2ClientDetailsService(OAuth2ClientRepository oAuth2ClientRepository,
                                      @Value("${oauth.expiration:3600}") int expiration) {
        this.oAuth2ClientRepository = oAuth2ClientRepository;
        this.expiration = expiration;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return oAuth2ClientRepository.findByClientId(clientId)
                .map(client -> new OAuth2ClientDetails(client, expiration))
                .orElseThrow(() -> new NoSuchClientException("Client does not exists"));
    }
}
