package pl.ark.chr.buginator.auth.util;

import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.ark.chr.buginator.auth.email.sender.EmailSender;
import pl.ark.chr.buginator.auth.email.template.EmailTemplateStrategy;
import pl.ark.chr.buginator.auth.email.template.strategies.RegisterEmailTemplateStrategy;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;
import pl.ark.chr.buginator.repository.messaging.EmailMessageRepository;

@Configuration
@ImportAutoConfiguration(classes = {
        FreeMarkerAutoConfiguration.class,
        ArtemisAutoConfiguration.class,
        JmsAutoConfiguration.class
})
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

    @Primary
    @Bean(name = "delegatedMockEmailSender")
    public EmailSender delegatedMockEmailSender(final EmailSender real) {
        return Mockito.mock(EmailSender.class, AdditionalAnswers.delegatesTo(real));
    }

    @Primary
    @Bean(name = "delegatedMockEmailMessageRepository")
    public EmailMessageRepository delegatedMockEmailMessageRepository(final EmailMessageRepository real) {
        return Mockito.mock(EmailMessageRepository.class, AdditionalAnswers.delegatesTo(real));
    }

    @Primary
    @Bean(name = "delegatedMockRegisterEmailTemplateStrategy")
    public RegisterEmailTemplateStrategy delegatedMockRegisterEmailTemplateStrategy(final RegisterEmailTemplateStrategy real) {
        return Mockito.mock(RegisterEmailTemplateStrategy.class, AdditionalAnswers.delegatesTo(real));
    }
}
