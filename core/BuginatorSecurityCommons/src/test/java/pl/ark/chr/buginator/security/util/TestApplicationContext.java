package pl.ark.chr.buginator.security.util;

import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import pl.ark.chr.buginator.repository.auth.OAuth2ClientRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

@SpringBootApplication
public class TestApplicationContext {

    @Primary
    @Bean(name = "delegatedMockOAuth2ClientRepository")
    public OAuth2ClientRepository delegatedMockOAuth2ClientRepository(final OAuth2ClientRepository real) {
        return Mockito.mock(OAuth2ClientRepository.class, AdditionalAnswers.delegatesTo(real));
    }

    @Primary
    @Bean(name = "delegatedMockUserRepository")
    public UserRepository delegatedMockUserRepository(final UserRepository real) {
        return Mockito.mock(UserRepository.class, AdditionalAnswers.delegatesTo(real));
    }
}
