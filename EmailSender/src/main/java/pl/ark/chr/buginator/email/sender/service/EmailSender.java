package pl.ark.chr.buginator.email.sender.service;

import pl.ark.chr.buginator.commons.dto.EmailDTO;

/**
 * Service responsible for creating and sending emails
 */
public interface EmailSender {

    /**
     * Creates email message and sends it to SMTP server
     *
     * @param mail Mail configuration
     */
    void createAndSendMessage(EmailDTO mail);
}
