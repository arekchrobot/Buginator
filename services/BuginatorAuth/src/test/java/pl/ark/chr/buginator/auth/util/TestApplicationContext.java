package pl.ark.chr.buginator.auth.util;

import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

@Configuration
public class TestApplicationContext {

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
