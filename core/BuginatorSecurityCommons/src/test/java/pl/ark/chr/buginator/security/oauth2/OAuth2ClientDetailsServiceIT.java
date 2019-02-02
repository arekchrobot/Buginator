package pl.ark.chr.buginator.security.oauth2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.repository.auth.OAuth2ClientRepository;
import pl.ark.chr.buginator.security.util.TestApplicationContext;
import pl.ark.chr.buginator.security.util.TestObjectCreator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {OAuth2DetailsServiceConfig.class, TestApplicationContext.class})
@Transactional
class OAuth2ClientDetailsServiceIT {

    @Autowired
    private OAuth2ClientDetailsService oAuth2ClientDetailsService;

    @SpyBean
    private OAuth2ClientRepository delegatedMockOAuth2ClientRepository;

    @Value("${oauth.expiration:3600}")
    private int defaultExpiration;

    @AfterEach
    void tearDown() {
        reset(delegatedMockOAuth2ClientRepository);
    }

    @Test
    @DisplayName("should correctly return client details based on OAuth2Client found in repository")
    void shouldCorrectlyFindOAuthClientAndBuildClientDetails() {
        //given
        var oAuth2Client = TestObjectCreator.createOAuth2ClientWithAllowedDomains("test.com");
        delegatedMockOAuth2ClientRepository.save(oAuth2Client);

        //when
        ClientDetails clientDetails = oAuth2ClientDetailsService.loadClientByClientId(oAuth2Client.getClientId());

        //then
        assertThat(clientDetails).isInstanceOf(OAuth2ClientDetails.class);
        assertThat(clientDetails.getClientSecret()).isEqualTo(oAuth2Client.getClientSecret());
        assertThat(clientDetails.getClientId()).isEqualTo(oAuth2Client.getClientId());
        assertThat(clientDetails.isSecretRequired()).isTrue();
        assertThat(clientDetails.getResourceIds()).containsOnly("resource");
        assertThat(clientDetails.isScoped()).isTrue();
        assertThat(clientDetails.getScope()).containsOnly("read", "write");
        assertThat(clientDetails.getAuthorizedGrantTypes()).containsOnly("password", "authorization_code");
        assertThat(clientDetails.getAccessTokenValiditySeconds()).isEqualTo(oAuth2Client.getAccessTokenExpiration());
        assertThat(clientDetails.getRefreshTokenValiditySeconds()).isEqualTo(defaultExpiration);
        assertThat(clientDetails.isAutoApprove("")).isFalse();
        verify(delegatedMockOAuth2ClientRepository, times(1))
                .findByClientId(eq(oAuth2Client.getClientId()));
    }

    @Test
    @DisplayName("should throw NoSuchClientException when no client found in database with given client id")
    void shouldThrowExceptionWhenNoClientFoundWithGivenClientId() {
        //given
        String clientId = "non_existing";

        //when
        Executable codeUnderException = () -> oAuth2ClientDetailsService.loadClientByClientId(clientId);

        //then
        var noSuchClientException = assertThrows(NoSuchClientException.class, codeUnderException,
                "Should throw NoSuchClientException exception");
        assertThat(noSuchClientException.getMessage()).isEqualTo("Client does not exists");
        verify(delegatedMockOAuth2ClientRepository, times(1))
                .findByClientId(eq(clientId));
    }
}