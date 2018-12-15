package pl.ark.chr.buginator.repository.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.ark.chr.buginator.domain.auth.OAuth2Client;
import pl.ark.chr.buginator.domain.auth.OAuth2ClientType;
import pl.ark.chr.buginator.repository.TestApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes= TestApplicationContext.class)
public class OAuth2ClientRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OAuth2ClientRepository oAuth2ClientRepository;

    @Test
    public void shouldFindOAuth2ClientByClientId() {
        //given
        var oAuth2Client = OAuth2Client.builder((String s) -> s)
                .type(OAuth2ClientType.WEB)
                .accessTokenExpiration(3600)
                .build();
        var oAuth2Client2 = OAuth2Client.builder((String s) -> s)
                .type(OAuth2ClientType.MOBILE)
                .accessTokenExpiration(360)
                .build();
        entityManager.persist(oAuth2Client);
        entityManager.persist(oAuth2Client2);

        //when
        Optional<OAuth2Client> result = oAuth2ClientRepository.findByClientId(oAuth2Client.getClientId());

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void shouldReturnEmptyOptionalWhenNoClientIdFound() {
        //given

        //when
        Optional<OAuth2Client> result = oAuth2ClientRepository.findByClientId("empty");

        //then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shoudlReturnEmptyOptioanlWhenNullClientId() {
        //given

        //when
        Optional<OAuth2Client> result = oAuth2ClientRepository.findByClientId(null);

        //then
        assertThat(result.isPresent()).isFalse();
    }
}
