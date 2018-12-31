package pl.ark.chr.buginator.auth.register;

import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan("pl.ark.chr.buginator.auth.register")
@EntityScan({"pl.ark.chr.buginator.domain", "pl.ark.chr.buginator.persistence"})
@EnableJpaRepositories({"pl.ark.chr.buginator.repository"})
public class TestRegisterApplicationContext {

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("def", new BCryptPasswordEncoder());

        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder("def", encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("def"));

        return delegatingPasswordEncoder;
    }

    @Primary
    @Bean(name = "delegatedMockCompanyRepository")
    public CompanyRepository delegatedMockCompanyRepository(final CompanyRepository real) {
        return Mockito.mock(CompanyRepository.class, AdditionalAnswers.delegatesTo(real));
    }

    @Primary
    @Bean(name = "delegatedMockUserRepository")
    public UserRepository delegatedMockUserRepository(final UserRepository real) {
        return Mockito.mock(UserRepository.class, AdditionalAnswers.delegatesTo(real));
    }

    @Primary
    @Bean(name = "delegatedMockPaymentOptionRepository")
    public PaymentOptionRepository delegatedMockPaymentOptionRepository(final PaymentOptionRepository real) {
        return Mockito.mock(PaymentOptionRepository.class, AdditionalAnswers.delegatesTo(real));
    }
}
