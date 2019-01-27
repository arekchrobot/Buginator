package pl.ark.chr.buginator.auth.email.sender;

import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.auth.email.template.EmailTemplateStrategy;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.messaging.EmailMessage;
import pl.ark.chr.buginator.repository.messaging.EmailMessageRepository;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class EmailJmsSender implements EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailJmsSender.class);

    private JmsTemplate jmsTemplate;
    private EmailMessageRepository emailMessageRepository;
    private String mailQueue;
    private List<EmailTemplateStrategy> emailTemplateStrategies;

    @Autowired
    public EmailJmsSender(JmsTemplate jmsTemplate,
                          EmailMessageRepository emailMessageRepository,
                          @Value("${jms.queue.mailQueue}") String mailQueue,
                          List<EmailTemplateStrategy> emailTemplateStrategies) {
        this.jmsTemplate = jmsTemplate;
        this.emailMessageRepository = emailMessageRepository;
        this.mailQueue = mailQueue;
        this.emailTemplateStrategies = emailTemplateStrategies;
    }

    @Override
    public void composeAndSendEmail(User user, Company company, EmailType emailType) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(company);
        Objects.requireNonNull(emailType);
        //TODO: get locale from User
        Locale locale = new Locale("en");
        String emailBody = null;
        String subject = null;
        for (var emailTemplateStrategy : emailTemplateStrategies) {
            if (emailTemplateStrategy.isValid(emailType)) {
                try {
                    emailBody = emailTemplateStrategy.constructEmailBody(company, locale);
                    subject = emailTemplateStrategy.getSubject(locale);
                } catch (IOException | TemplateException e) {
                    LOGGER.error("Error creating email body for type: " + emailType + " for user: " + user.getEmail());
                    return;
                }
            }
        }

        EmailMessage emailMessage = emailMessageRepository
                .findById(EmailMessage.BUGINATOR)
                .orElseThrow();

        EmailDTO emailDTO = constructEmail(user.getEmail(), emailMessage, subject, emailBody);

        jmsTemplate.convertAndSend(mailQueue, emailDTO);
    }

    EmailDTO constructEmail(String recipient, EmailMessage emailMessage, String subject, String emailBody) {
        Objects.requireNonNull(subject);
        Objects.requireNonNull(emailBody);

        return EmailDTO.builder(emailMessage.getFrom(), recipient)
                .emailBody(emailBody)
                .htmlBody(true)
                .username(emailMessage.getUsername())
                .password(emailMessage.getPassword())
                .smtpHost(emailMessage.getSmtpHost())
                .smtpPort(emailMessage.getSmtpPort())
                .ssl(emailMessage.isSsl())
                .subject(subject)
                .build();
    }
}
