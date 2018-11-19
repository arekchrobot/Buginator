package pl.ark.chr.buginator.email.sender.service.impl;

import com.sun.mail.smtp.SMTPSendFailedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.email.sender.service.jms.MailFailedQueueSender;
import pl.wkr.fluentrule.api.FluentExpectedException;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JavaMailEmailSenderImplTest {

    @Spy
    @InjectMocks
    private JavaMailEmailSenderImpl emailSender;

    @Mock
    private MailFailedQueueSender mailFailedQueueSender;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
        doNothing().when(emailSender).sendMessage(any(Message.class));
    }

    @Test
    public void shouldCorrectlySendEmail() throws Exception {
        //given
        var mail = EmailDTO.builder("from@gmail.com", "to@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .subject("Subject")
                .cc("toCC@gmail.com,abc@gmail.com")
                .bcc("abc2@gmail.com")
                .build();

        //when
        emailSender.createAndSendMessage(mail);

        //then
        verify(emailSender, times(1)).sendMessage(any(Message.class));
        verify(mailFailedQueueSender, never()).sendAuthFailed(eq(mail));
        verify(mailFailedQueueSender, never()).sendSpamRejected(eq(mail));
    }

    @Test
    public void shouldSendToAuthQueue() throws Exception {
        //given
        doThrow(new AuthenticationFailedException()).when(emailSender).sendMessage(any(Message.class));
        var mail = EmailDTO.builder("from@gmail.com", "to@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .subject("Subject")
                .cc("toCC@gmail.com,abc@gmail.com")
                .bcc("abc2@gmail.com")
                .build();

        //when
        emailSender.createAndSendMessage(mail);

        //then
        verify(emailSender, times(1)).sendMessage(any(Message.class));
        verify(mailFailedQueueSender, times(1)).sendAuthFailed(eq(mail));
        verify(mailFailedQueueSender, never()).sendSpamRejected(eq(mail));
    }

    @Test
    public void shouldSendToSpamQueue() throws Exception {
        //given
        doThrow(new SMTPSendFailedException("", 552, null, null, null, null, null)).when(emailSender).sendMessage(any(Message.class));
        var mail = EmailDTO.builder("from@gmail.com", "to@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .subject("Subject")
                .cc("toCC@gmail.com,abc@gmail.com")
                .bcc("abc2@gmail.com")
                .build();

        //when
        emailSender.createAndSendMessage(mail);

        //then
        verify(emailSender, times(1)).sendMessage(any(Message.class));
        verify(mailFailedQueueSender, never()).sendAuthFailed(eq(mail));
        verify(mailFailedQueueSender, times(1)).sendSpamRejected(eq(mail));
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenNotSpam() throws Exception {
        //given
        doThrow(new SMTPSendFailedException("", 551, "SMTP ERROR", null, null, null, null)).when(emailSender).sendMessage(any(Message.class));
        var mail = EmailDTO.builder("from@gmail.com", "to@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .subject("Subject")
                .cc("toCC@gmail.com,abc@gmail.com")
                .bcc("abc2@gmail.com")
                .build();

        fluentThrown
                .expect(RuntimeException.class)
                .hasMessage("com.sun.mail.smtp.SMTPSendFailedException: SMTP ERROR");

        //when
        emailSender.createAndSendMessage(mail);

        //then
        verify(emailSender, times(1)).sendMessage(any(Message.class));
        verify(mailFailedQueueSender, never()).sendAuthFailed(eq(mail));
        verify(mailFailedQueueSender, never()).sendSpamRejected(eq(mail));
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenGeneralMessagingError() throws Exception {
        //given
        doThrow(new MessagingException("GeneralError")).when(emailSender).sendMessage(any(Message.class));
        var mail = EmailDTO.builder("from@gmail.com", "to@gmail.com")
                .smtpHost("host")
                .smtpPort("547")
                .username("usename")
                .password("pass")
                .emailBody("TEST MESSAGE")
                .subject("Subject")
                .cc("toCC@gmail.com,abc@gmail.com")
                .bcc("abc2@gmail.com")
                .build();

        fluentThrown
                .expect(RuntimeException.class)
                .hasMessage("javax.mail.MessagingException: GeneralError");

        //when
        emailSender.createAndSendMessage(mail);

        //then
        verify(emailSender, times(1)).sendMessage(any(Message.class));
        verify(mailFailedQueueSender, never()).sendAuthFailed(eq(mail));
        verify(mailFailedQueueSender, never()).sendSpamRejected(eq(mail));
    }
}