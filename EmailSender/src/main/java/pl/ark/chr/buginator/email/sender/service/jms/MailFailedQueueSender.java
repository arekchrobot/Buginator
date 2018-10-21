package pl.ark.chr.buginator.email.sender.service.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import pl.ark.chr.buginator.commons.dto.EmailDTO;

@Component
public class MailFailedQueueSender {

    private JmsTemplate jmsTemplate;
    private String authFailedMailQueue;
    private String spamMailQueue;

    @Autowired
    public MailFailedQueueSender(JmsTemplate jmsTemplate,
                                 @Value("${jms.queue.authFailedMailQueue}") String authFailedMailQueue,
                                 @Value("${jms.queue.spamFailedMailQueue}") String spamMailQueue) {
        this.jmsTemplate = jmsTemplate;
        this.authFailedMailQueue = authFailedMailQueue;
        this.spamMailQueue = spamMailQueue;
    }

    public void sendAuthFailed(EmailDTO mail) {
        sendToQueue(mail, authFailedMailQueue);
    }

    public void sendSpamRejected(EmailDTO mail) {
        sendToQueue(mail, spamMailQueue);
    }

    void sendToQueue(EmailDTO mail, String queueName) {
        jmsTemplate.convertAndSend(queueName, mail);
    }
}
