package pl.ark.chr.buginator.auth.email.sender;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.auth.AuthApplication;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.auth.email.template.strategies.RegisterEmailTemplateStrategy;
import pl.ark.chr.buginator.auth.util.TestApplicationContext;
import pl.ark.chr.buginator.auth.util.TestObjectCreator;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.messaging.EmailMessage;
import pl.ark.chr.buginator.repository.messaging.EmailMessageRepository;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AuthApplication.class, TestApplicationContext.class})
@Transactional
public class EmailJmsSenderIT {

    @Autowired
    private EmailJmsSender emailJmsSender;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.queue.mailQueue}")
    private String mailQueue;

    @SpyBean
    private EmailMessageRepository delegatedMockEmailMessageRepository;
    @SpyBean
    private RegisterEmailTemplateStrategy delegatedMockRegisterEmailTemplateStrategy;

    private static final String EN_BODY = "<html>\n" +
            "    <body>\n" +
            "        <p style=\"margin:15px 0;\">\n" +
            "            <b>Thank you for registering to Buginator</b>\n" +
            "        </p>\n" +
            "        <p style=\"margin:15px 0;\">Welcome to Buginator application, TEST!</p>\n" +
            "        <p style=\"margin:15px 0;\">Here are your api keys to authenticate:</p>\n" +
            "        <p style=\"margin:15px 0;\">Unique key: ${uniqueKey}</p>\n" +
            "        <p style=\"margin:15px 0;\">Token: ${token}</p>\n" +
            "        Buginator.com</p>\n" +
            "    </body>\n" +
            "</html>";

    @AfterEach
    void tearDown() {
        reset(delegatedMockEmailMessageRepository, delegatedMockRegisterEmailTemplateStrategy);
    }

    @Test
    @DisplayName("should correctly compose and send EN register email for new user")
    public void shouldCorrectlyComposeAndSendEnRegisterEmail() throws Exception {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));
        String emailBody = EN_BODY
                .replace("${uniqueKey}", company.getUniqueKey())
                .replace("${token}", company.getToken());

        //when
        emailJmsSender.composeAndSendEmail(user, company, EmailType.REGISTER);

        //then
        verify(delegatedMockEmailMessageRepository, times(1)).findById(eq(EmailMessage.BUGINATOR));
        verify(delegatedMockRegisterEmailTemplateStrategy, times(1)).isValid(eq(EmailType.REGISTER));
        verify(delegatedMockRegisterEmailTemplateStrategy, times(1))
                .constructEmailBody(eq(company), any(Locale.class));
        verify(delegatedMockRegisterEmailTemplateStrategy, times(1)).getSubject(any(Locale.class));

        EmailDTO registerEmail = (EmailDTO) jmsTemplate.receiveAndConvert(mailQueue);
        assertThat(registerEmail).isNotNull();
        assertThat(registerEmail.getTo()).isEqualTo(user.getEmail());
        assertThat(registerEmail.getEmailBody()).isEqualTo(emailBody);
        assertThat(registerEmail.getSubject()).isEqualTo("Buginator registration confirmation");
    }
}
