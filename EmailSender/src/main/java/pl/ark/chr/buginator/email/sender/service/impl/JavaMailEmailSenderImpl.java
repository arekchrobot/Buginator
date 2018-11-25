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

import javax.mail.*;
import java.util.Objects;

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

        var emailProperties = EmailUtils.createEmailProperties(mail);
        var mailSession = EmailUtils.createMailSession(mail, emailProperties);

        try {
            LOGGER.info("Creating email message from: {} to: {}", mail.getFrom(), mail.getTo());
            var message = EmailUtils.createMessage(mail, mailSession);

            LOGGER.info("Sending message from: {} to: {}", mail.getFrom(), mail.getTo());
            sendMessage(message);
        } catch (MessagingException e) {
            processMailException(mail, e);
        }
    }

    void sendMessage(Message message) throws MessagingException {
        Transport.send(message);
    }

    private void processMailException(EmailDTO mail, MessagingException e) {
        if (e instanceof AuthenticationFailedException) {
            LOGGER.error("Authentication failed for message from: {} to: {}. Sending message to authFailedMailQueue. Error message: {}",
                    mail.getFrom(), mail.getTo(), e.getMessage());

            mailFailedQueueSender.sendAuthFailed(mail);
        } else if (e instanceof SMTPSendFailedException) {
            SMTPSendFailedException sfe = (SMTPSendFailedException) e;

            processSMTPException(mail, sfe);
        } else {
            LOGGER.error("Error creating or sending message from: {} to: {}. Error message: {}", mail.getFrom(), mail.getTo(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void processSMTPException(EmailDTO mail, SMTPSendFailedException sfe) {
        LOGGER.error("Message from: {} to: {} failed. Error message: {}", mail.getFrom(), mail.getTo(), sfe.getMessage());

        if (markedAsSpam(sfe)) {
            LOGGER.error("Sending message from: {} to: {} to spamMailQueue", mail.getFrom(), mail.getTo());
            mailFailedQueueSender.sendSpamRejected(mail);
        } else {
            throw new RuntimeException(sfe);
        }
    }

    private boolean markedAsSpam(SMTPSendFailedException sfe) {
        //552 is code for "Message rejected for spam or virus content"
        return sfe.getReturnCode() == 552;
    }
}
