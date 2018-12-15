package pl.ark.chr.buginator.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import pl.ark.chr.buginator.repository.auth.OAuth2ClientRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

@Configuration
@EntityScan({"pl.ark.chr.buginator.domain", "pl.ark.chr.buginator.persistence"})
@EnableJpaRepositories({"pl.ark.chr.buginator.repository"})
public class OAuth2DetailsServiceConfig {

    @Bean
    @Primary
    public ClientDetailsService oauth2ClientDetailsService(OAuth2ClientRepository oAuth2ClientRepository,
                                                     @Value("${oauth.expiration:3600}") int expiration) {
        return new OAuth2ClientDetailsService(oAuth2ClientRepository, expiration);
    }

    @Bean
    @Primary
    public UserDetailsService oauth2UserDetailsService(UserRepository userRepository) {
        return new OAuth2UserDetailsService(userRepository);
    }
}
