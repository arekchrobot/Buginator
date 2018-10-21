package pl.ark.chr.buginator.email.sender.util;

import org.junit.Test;
import pl.ark.chr.buginator.commons.dto.EmailDTO;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailUtilsTest {

    @Test
    public void shouldCreateNonSslProperties() {
        //given
        EmailDTO mail = EmailDTO.builder("from", "to")
                .smtpHost("host")
                .smtpPort("547")
                .build();

        //when
        Properties result = EmailUtils.createEmailProperties(mail);

        //then
        assertThat(result.getProperty(EmailUtils.SMTP_HOST)).isEqualTo("host");
        assertThat(result.getProperty(EmailUtils.SMTP_PORT)).isEqualTo("547");
        assertThat(result.getProperty(EmailUtils.SMTP_AUTH)).isEqualTo("true");
        assertThat(result.getProperty(EmailUtils.SOCKET_PORT)).isNullOrEmpty();
    }

    @Test
    public void shouldCreateSslProperties() {
        //given
        EmailDTO mail = EmailDTO.builder("from", "to")
                .smtpHost("host")
                .smtpPort("547")
                .ssl(true)
                .build();

        //when
        Properties result = EmailUtils.createEmailProperties(mail);

        //then
        assertThat(result.getProperty(EmailUtils.SMTP_HOST)).isEqualTo("host");
        assertThat(result.getProperty(EmailUtils.SMTP_PORT)).isEqualTo("547");
        assertThat(result.getProperty(EmailUtils.SMTP_AUTH)).isEqualTo("true");
        assertThat(result.getProperty(EmailUtils.SOCKET_PORT)).isEqualTo("547");
        assertThat(result.getProperty(EmailUtils.SSL_TRUST)).isEqualTo("*");
    }

    @Test
    public void shouldSetEmptyIfMissingPropertyParam() {
        //given
        EmailDTO mail = EmailDTO.builder("from", "to")
                .smtpHost("host")
                .build();

        //when
        Properties result = EmailUtils.createEmailProperties(mail);

        //then
        assertThat(result.getProperty(EmailUtils.SMTP_HOST)).isEqualTo("host");
        assertThat(result.getProperty(EmailUtils.SMTP_PORT)).isEmpty();
    }

    @Test
    public void shouldCorrectlyParseRecipients() throws Exception {
        //given
        Address address = new InternetAddress("to@gmail.com");
        Address address2 = new InternetAddress("abc@gmail.com");

        EmailDTO mail = EmailDTO.builder("from@gmail.com", "to@gmail.com,abc@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .subject("Subject")
                .build();

        Properties props = EmailUtils.createEmailProperties(mail);
        Session mailSession = EmailUtils.createMailSession(mail, props);

        //when
        Message result = EmailUtils.createMessage(mail, mailSession);

        //then
        assertThat(result.getRecipients(Message.RecipientType.TO))
                .isNotEmpty()
                .hasSize(2)
                .containsOnly(address, address2);
        assertThat(result.getRecipients(Message.RecipientType.CC)).isNull();
        assertThat(result.getRecipients(Message.RecipientType.BCC)).isNull();
    }

    @Test
    public void shouldSetCcAndBccRecipients() throws Exception {
        //given
        Address cc = new InternetAddress("toCC@gmail.com");
        Address cc2 = new InternetAddress("abc@gmail.com");
        Address bcc = new InternetAddress("abc2@gmail.com");

        EmailDTO mail = EmailDTO.builder("from@gmail.com", "to@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .subject("Subject")
                .cc("toCC@gmail.com,abc@gmail.com")
                .bcc("abc2@gmail.com")
                .build();

        Properties props = EmailUtils.createEmailProperties(mail);
        Session mailSession = EmailUtils.createMailSession(mail, props);

        //when
        Message result = EmailUtils.createMessage(mail, mailSession);

        //then
        assertThat(result.getRecipients(Message.RecipientType.CC))
                .isNotEmpty()
                .hasSize(2)
                .containsOnly(cc, cc2);
        assertThat(result.getRecipients(Message.RecipientType.BCC))
                .isNotEmpty()
                .hasSize(1)
                .containsOnly(bcc);
    }

    @Test
    public void shouldCorrectlySetSubjectAndTextContent() throws Exception {
        //given
        EmailDTO mail = EmailDTO.builder("from@gmail.com", "to@gmail.com,abc@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .subject("Subject")
                .build();

        Properties props = EmailUtils.createEmailProperties(mail);
        Session mailSession = EmailUtils.createMailSession(mail, props);

        //when
        Message result = EmailUtils.createMessage(mail, mailSession);

        //then
        assertThat(result.getSubject()).isEqualTo("Subject");
        assertThat(result.getContent()).isEqualTo("TEST MESSAGE");
        assertThat(result.getContentType()).isEqualTo("text/plain");
    }

    @Test
    public void shouldCorrectlySetHtmlContent() throws Exception {
        //given
        EmailDTO mail = EmailDTO.builder("from@gmail.com", "to@gmail.com,abc@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .htmlBody(true)
                .subject("Subject")
                .build();

        Properties props = EmailUtils.createEmailProperties(mail);
        Session mailSession = EmailUtils.createMailSession(mail, props);

        //when
        Message result = EmailUtils.createMessage(mail, mailSession);

        //then
        assertThat(result.getContent()).isEqualTo("TEST MESSAGE");
        assertThat(result.getDataHandler().getContentType()).isEqualTo("text/html; charset=utf-8");
    }
}