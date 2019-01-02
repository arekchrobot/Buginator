package pl.ark.chr.buginator.email.sender.service.impl;

import com.sun.mail.smtp.SMTPSendFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.email.sender.service.jms.MailFailedQueueSender;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class JavaMailEmailSenderImplTest {

    @Spy
    @InjectMocks
    private JavaMailEmailSenderImpl emailSender;

    @Mock
    private MailFailedQueueSender mailFailedQueueSender;

    @BeforeEach
    public void setUp() throws Exception {
        doNothing().when(emailSender).sendMessage(any(Message.class));
    }

    @Test
    @DisplayName("should correctly send email for given DTO")
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
    @DisplayName("should send failed message to authQueue if authentication failed")
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
    @DisplayName("should send failed message to spamQueue if detected as spam")
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
    @DisplayName("should throw exception when failed message is authenticated and not detected as spam")
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

        //when
        Executable codeUnderException = () -> emailSender.createAndSendMessage(mail);

        //then
        var runtimeException = assertThrows(RuntimeException.class, codeUnderException,
                "Should throw SMTPSendFailedException");
        assertThat(runtimeException.getMessage()).isEqualTo("com.sun.mail.smtp.SMTPSendFailedException: SMTP ERROR");
        verify(emailSender, times(1)).sendMessage(any(Message.class));
        verify(mailFailedQueueSender, never()).sendAuthFailed(eq(mail));
        verify(mailFailedQueueSender, never()).sendSpamRejected(eq(mail));
    }

    @Test
    @DisplayName("should throw exception when GeneralError occurs")
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

        //when
        Executable codeUnderException = () -> emailSender.createAndSendMessage(mail);

        //then
        var runtimeException = assertThrows(RuntimeException.class, codeUnderException,
                "Should throw MessagingException");
        assertThat(runtimeException.getMessage()).isEqualTo("javax.mail.MessagingException: GeneralError");
        verify(emailSender, times(1)).sendMessage(any(Message.class));
        verify(mailFailedQueueSender, never()).sendAuthFailed(eq(mail));
        verify(mailFailedQueueSender, never()).sendSpamRejected(eq(mail));
    }
}