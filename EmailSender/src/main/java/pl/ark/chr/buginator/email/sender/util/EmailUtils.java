package pl.ark.chr.buginator.email.sender.util;

import pl.ark.chr.buginator.commons.dto.EmailDTO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

    public static Properties createEmailProperties(EmailDTO mail) {
        Properties props = new Properties();
        props.put("mail.smtp.host", mail.getSmtpHost());
        if (mail.isSsl()) {
            props.put("mail.smtp.socketFactory.port", mail.getSmtpPort());
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtps.ssl.checkserveridentity", false);
            props.put("mail.smtps.ssl.trust", "*");
        }
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", mail.getSmtpPort());
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
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));

        setCopyRecipients(message, mail.getCc(), Message.RecipientType.CC);
        setCopyRecipients(message, mail.getBcc(), Message.RecipientType.BCC);

        message.setSubject(mail.getSubject());
        message.setSentDate(new Date());

        setContent(mail, message);
        return message;
    }

    public static void sendMessage(Message message) throws MessagingException {
        Transport.send(message);
    }

    static void setContent(EmailDTO mail, Message message) throws MessagingException {
        if (mail.isHtmlBody()) {
            message.setContent(mail.getEmailBody(), "text/html; charset=utf-8");
        } else {
            message.setText(mail.getEmailBody());
        }
    }

    static void setCopyRecipients(Message message, String cc, Message.RecipientType type) throws MessagingException {
        if (cc != null && !cc.isBlank()) {
            message.setRecipients(type, InternetAddress.parse(cc));
        }
    }
}
