package pl.ark.chr.buginator.email.sender.service.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import org.springframework.messaging.Message;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.email.sender.service.EmailSender;

@Component
public class MailQueueListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailQueueListener.class);

    private EmailSender emailSender;

    @Autowired
    public MailQueueListener(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @JmsListener(destination = "${jms.queue.mailQueue}")
    public void receiveAndSend(Message message) {
        if(message.getPayload() instanceof EmailDTO) {
            EmailDTO mail = (EmailDTO) message.getPayload();

            LOGGER.info("Received mail object: " + mail.toString());

            emailSender.createAndSendMessage(mail);
        } else {

            LOGGER.info("Unknown message type");
        }
    }
}
