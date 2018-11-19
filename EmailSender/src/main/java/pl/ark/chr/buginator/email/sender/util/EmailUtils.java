package pl.ark.chr.buginator.email.sender.util;

import pl.ark.chr.buginator.commons.dto.EmailDTO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Utility for creating mailing objects using JavaMail API
 */
public class EmailUtils {

    static final String SMTP_HOST = "mail.smtp.host";
    static final String SMTP_PORT = "mail.smtp.port";
    static final String SMTP_AUTH = "mail.smtp.auth";
    static final String SOCKET_PORT = "mail.smtp.socketFactory.port";
    static final String SOCKET_CLASS = "mail.smtp.socketFactory.class";
    static final String SSL_CHECK_IDENTITY = "mail.smtps.ssl.checkserveridentity";
    static final String SSL_TRUST = "mail.smtps.ssl.trust";

    /**
     * Creates smtp and ssl properties used by JavaMail to configure connection
     *
     * @param mail Configuration class
     * @return Minimal required properties to send email
     */
    public static Properties createEmailProperties(EmailDTO mail) {
        var props = new Properties();
        props.put(SMTP_HOST, mail.getSmtpHost() == null ? "" : mail.getSmtpHost());
        if (mail.isSsl()) {
            props.put(SOCKET_PORT, mail.getSmtpPort() == null ? "" : mail.getSmtpPort());
            props.put(SOCKET_CLASS, "javax.net.ssl.SSLSocketFactory");
            props.put(SSL_CHECK_IDENTITY, false);
            props.put(SSL_TRUST, "*");
        }
        props.put(SMTP_AUTH, "true");
        props.put(SMTP_PORT, mail.getSmtpPort() == null ? "" : mail.getSmtpPort());
        return props;
    }

    public static Session createMailSession(EmailDTO mail, Properties props) {
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail.getUsername(), mail.getPassword());
            }
        });
    }

    public static Message createMessage(EmailDTO mail, Session session) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(mail.getFrom()));
        setRecipients(message, mail.getTo(), Message.RecipientType.TO);

        setRecipients(message, mail.getCc(), Message.RecipientType.CC);
        setRecipients(message, mail.getBcc(), Message.RecipientType.BCC);

        message.setSubject(mail.getSubject());
        message.setSentDate(new Date());

        setContent(mail, message);
        return message;
    }

    static void setContent(EmailDTO mail, Message message) throws MessagingException {
        if (mail.isHtmlBody()) {
            message.setContent(mail.getEmailBody(), "text/html; charset=utf-8");
        } else {
            message.setText(mail.getEmailBody());
        }
    }

    static void setRecipients(Message message, String recipients, Message.RecipientType type) throws MessagingException {
        if (recipients != null && !recipients.isBlank()) {
            message.setRecipients(type, InternetAddress.parse(recipients));
        }
    }
}
