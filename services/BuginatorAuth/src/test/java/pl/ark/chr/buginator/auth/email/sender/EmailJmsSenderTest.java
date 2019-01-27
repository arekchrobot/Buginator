package pl.ark.chr.buginator.auth.email.sender;

import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jms.core.JmsTemplate;
import pl.ark.chr.buginator.auth.email.template.EmailTemplateStrategy;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.auth.util.TestObjectCreator;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.messaging.EmailMessage;
import pl.ark.chr.buginator.repository.messaging.EmailMessageRepository;

import java.util.Collections;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmailJmsSenderTest {

    private EmailJmsSender emailJmsSender;

    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private EmailMessageRepository emailMessageRepository;
    @Mock
    private EmailTemplateStrategy emailTemplateStrategy;

    private final String mailQueue = "mailQueue";

    private static final String TEST_EMAIL_BODY = "TEST_EMAIL_BODY";
    private static final String TEST_SUBJECT = "TEST_SUBJECT";

    @BeforeEach
    public void setUp() throws Exception {
        emailJmsSender = new EmailJmsSender(jmsTemplate, emailMessageRepository, mailQueue,
                Collections.singletonList(emailTemplateStrategy));

        doReturn(true).when(emailTemplateStrategy).isValid(any(EmailType.class));
        doReturn(TEST_EMAIL_BODY).when(emailTemplateStrategy).constructEmailBody(any(Company.class), any(Locale.class));
        doReturn(TEST_SUBJECT).when(emailTemplateStrategy).getSubject(any(Locale.class));
    }

    @Test
    @DisplayName("should compose message for user and send it to jms queue")
    public void shouldCreateMessageAndSendToJms() throws Exception {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));
        var emailMessage = TestObjectCreator.createEmailMessage();
        doReturn(Optional.of(emailMessage)).when(emailMessageRepository).findById(eq(EmailMessage.BUGINATOR));

        doAnswer(invocationOnMock -> {
            EmailDTO emailDTO = invocationOnMock.getArgument(1);

            assertThat(emailDTO.getFrom()).isEqualTo(emailMessage.getFrom());
            assertThat(emailDTO.getTo()).isEqualTo(user.getEmail());
            assertThat(emailDTO.getEmailBody()).isEqualTo(TEST_EMAIL_BODY);
            assertThat(emailDTO.isHtmlBody()).isTrue();
            assertThat(emailDTO.getSubject()).isEqualTo(TEST_SUBJECT);
            assertThat(emailDTO.getBcc()).isNull();
            assertThat(emailDTO.getCc()).isNull();
            assertThat(emailDTO.getUsername()).isEqualTo(emailMessage.getUsername());
            assertThat(emailDTO.getPassword()).isEqualTo(emailMessage.getPassword());
            assertThat(emailDTO.getSmtpHost()).isEqualTo(emailMessage.getSmtpHost());
            assertThat(emailDTO.getSmtpPort()).isEqualTo(emailMessage.getSmtpPort());
            assertThat(emailDTO.isSsl()).isEqualTo(emailMessage.isSsl());

            return null;
        }).when(jmsTemplate).convertAndSend(eq(mailQueue), any(EmailDTO.class));

        //when
        emailJmsSender.composeAndSendEmail(user, company, EmailType.REGISTER);

        //then
        verify(jmsTemplate, times(1)).convertAndSend(eq(mailQueue), any(EmailDTO.class));
        verify(emailTemplateStrategy, times(1)).isValid(any(EmailType.class));
        verify(emailTemplateStrategy, times(1)).constructEmailBody(any(Company.class), any(Locale.class));
        verify(emailTemplateStrategy, times(1)).getSubject(any(Locale.class));
        verify(emailMessageRepository, times(1)).findById(eq(EmailMessage.BUGINATOR));
    }

    @Test
    @DisplayName("should throw NoSuchElementException when no email message config is present")
    public void shouldThrowErrorWhenNoEmailMessageFound() throws Exception {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));
        doReturn(Optional.empty()).when(emailMessageRepository).findById(eq(EmailMessage.BUGINATOR));

        //when
        Executable codeUnderException = () -> emailJmsSender.composeAndSendEmail(user, company, EmailType.REGISTER);

        //then
        var noSuchElementException = assertThrows(NoSuchElementException.class, codeUnderException,
                "Should throw NoSuchElementException");
        assertThat(noSuchElementException.getMessage()).isEqualTo("No value present");

        verify(jmsTemplate, never()).convertAndSend(eq(mailQueue), any(EmailDTO.class));
        verify(emailTemplateStrategy, times(1)).isValid(any(EmailType.class));
        verify(emailTemplateStrategy, times(1)).constructEmailBody(any(Company.class), any(Locale.class));
        verify(emailTemplateStrategy, times(1)).getSubject(any(Locale.class));
        verify(emailMessageRepository, times(1)).findById(eq(EmailMessage.BUGINATOR));
    }

    @Test
    @DisplayName("should not send message to jsm when expection occurs during executing template strategy method")
    public void shouldNotSendMessageWhenTemplateStrategyError() throws Exception {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));
        var emailMessage = TestObjectCreator.createEmailMessage();
        doReturn(Optional.of(emailMessage)).when(emailMessageRepository).findById(eq(EmailMessage.BUGINATOR));

        doThrow(new TemplateException(null)).when(emailTemplateStrategy).constructEmailBody(any(Company.class), any(Locale.class));

        //when
        emailJmsSender.composeAndSendEmail(user, company, EmailType.REGISTER);

        //then
        verify(jmsTemplate, never()).convertAndSend(eq(mailQueue), any(EmailDTO.class));
        verify(emailTemplateStrategy, times(1)).isValid(any(EmailType.class));
        verify(emailTemplateStrategy, times(1)).constructEmailBody(any(Company.class), any(Locale.class));
        verify(emailTemplateStrategy, never()).getSubject(any(Locale.class));
        verify(emailMessageRepository, never()).findById(eq(EmailMessage.BUGINATOR));
    }

    @ParameterizedTest
    @MethodSource("composeAndSendEmailNpeProvider")
    @DisplayName("should throw NullPointerException when passing null to composeAndSendEmail")
    public void shouldThrowNpeWhenOneOfArgumentIsNull(User user, Company company, EmailType emailType) throws Exception {
        //when
        Executable codeUnderException = () -> emailJmsSender.composeAndSendEmail(user, company, emailType);

        //then
        assertThrows(NullPointerException.class, codeUnderException,
                "Should throw NullPointerException");
        verify(jmsTemplate, never()).convertAndSend(eq(mailQueue), any(EmailDTO.class));
        verify(emailTemplateStrategy, never()).isValid(any(EmailType.class));
        verify(emailTemplateStrategy, never()).constructEmailBody(any(Company.class), any(Locale.class));
        verify(emailTemplateStrategy, never()).getSubject(any(Locale.class));
        verify(emailMessageRepository, never()).findById(eq(EmailMessage.BUGINATOR));
    }

    private static Stream<Arguments> composeAndSendEmailNpeProvider() {
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));
        return Stream.of(
                Arguments.of(null, company, EmailType.REGISTER),
                Arguments.of(user, null, EmailType.REGISTER),
                Arguments.of(user, company, null)
        );
    }

    @ParameterizedTest
    @MethodSource("constructEmailNpeProvider")
    @DisplayName("should throw NullPointerException when passing null to constructEmail")
    public void shouldNotCreateEmailDTOWhenArgumentIsNull(String subject, String emailBody) {
        //when
        Executable codeUnderException = () -> emailJmsSender.constructEmail("test",
                EmailMessage.builder().build(), subject, emailBody);

        //then
        assertThrows(NullPointerException.class, codeUnderException,
                "Should throw NullPointerException");
    }

    private static Stream<Arguments> constructEmailNpeProvider() {
        return Stream.of(
                Arguments.of(TEST_SUBJECT, null),
                Arguments.of(null, TEST_EMAIL_BODY)
        );
    }
}