package pl.ark.chr.buginator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.repository.auth.OAuth2ClientRepository;

//@Service
//@Primary
public class OAuth2ClientDetailsService implements ClientDetailsService {

    private OAuth2ClientRepository oAuth2ClientRepository;
    private int expiration;

//    @Autowired
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
