package pl.ark.chr.buginator.email.sender.service.impl;

import com.sun.mail.smtp.SMTPSendFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.email.sender.service.EmailSender;
import pl.ark.chr.buginator.email.sender.service.jms.MailFailedQueueSender;
import pl.ark.chr.buginator.email.sender.util.EmailUtils;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.util.Objects;
import java.util.Properties;

@Service
public class JavaMailEmailSenderImpl implements EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaMailEmailSenderImpl.class);

    private MailFailedQueueSender mailFailedQueueSender;

    @Autowired
    public JavaMailEmailSenderImpl(MailFailedQueueSender mailFailedQueueSender) {
        this.mailFailedQueueSender = mailFailedQueueSender;
    }

    @Override
    public void createAndSendMessage(EmailDTO mail) {
        Objects.requireNonNull(mail);

        LOGGER.info("Preparing mail configuration from: {} to: {}", mail.getFrom(), mail.getTo());

        Properties emailProperties = EmailUtils.createEmailProperties(mail);
        Session mailSession = EmailUtils.createMailSession(mail, emailProperties);

        try {
            LOGGER.info("Creating email message from: {} to: {}", mail.getFrom(), mail.getTo());
            Message message = EmailUtils.createMessage(mail, mailSession);

            LOGGER.info("Sending message from: {} to: {}", mail.getFrom(), mail.getTo());
            EmailUtils.sendMessage(message);
        } catch(AuthenticationFailedException aef) {
            LOGGER.error("Authentication failed for message from: {} to: {}. Sending message to authFailedMailQueue. Error message: {}",
                    mail.getFrom(), mail.getTo(), aef.getMessage());

            mailFailedQueueSender.sendAuthFailed(mail);
        } catch(SMTPSendFailedException sfe) {
            LOGGER.error("Message from: {} to: {} failed. Error message: {}", mail.getFrom(), mail.getTo(), sfe.getMessage());

            if(markedAsSpam(sfe)) {
                LOGGER.error("Sending message from: {} to: {} to spamMailQueue", mail.getFrom(), mail.getTo());
                mailFailedQueueSender.sendSpamRejected(mail);
            }
            else {
                throw new RuntimeException(sfe);
            }
        } catch (MessagingException e) {
            LOGGER.error("Error creating or seding message from: {} to: {}. Error message: {}", mail.getFrom(), mail.getTo(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean markedAsSpam(SMTPSendFailedException sfe) {
        //552 is code for "Message rejected for spam or virus content"
        return sfe.getReturnCode() == 552;
    }
}
