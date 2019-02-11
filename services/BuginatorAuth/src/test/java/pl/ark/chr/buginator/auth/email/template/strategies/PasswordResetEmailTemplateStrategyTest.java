package pl.ark.chr.buginator.auth.email.template.strategies;

import freemarker.template.Configuration;
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
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.auth.util.TestObjectCreator;
import pl.ark.chr.buginator.commons.util.NetworkUtil;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.repository.auth.PasswordResetRepository;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PasswordResetEmailTemplateStrategyTest {

    @Spy
    @InjectMocks
    private PasswordResetEmailTemplateStrategy passwordResetEmailTemplateStrategy;

    @Mock
    private Configuration freeMarkerConfig;
    @Mock
    private MessageSource authEmailMessageSource;
    @Mock
    private PasswordResetRepository passwordResetRepository;

    private final String TEST_MESSAGE_SOURCE = "test_message";

    @BeforeEach
    void setUp() throws Exception {
        doReturn("").when(passwordResetEmailTemplateStrategy).constructEmailBody(any(Map.class));
        doReturn(TEST_MESSAGE_SOURCE).when(authEmailMessageSource)
                .getMessage(any(String.class), isNull(), any(Locale.class));
    }

    @Test
    @DisplayName("Strategy should be valid for EmailType.PASSWORD_RESET")
    void shouldBeValidStrategy() {
        //expect
        assertThat(passwordResetEmailTemplateStrategy.isValid(EmailType.PASSWORD_RESET)).isTrue();
    }

    @Test
    @DisplayName("Should return Subject from given locale for Register mail")
    void shouldGetSubjectFromLocale() {
        //given
        Locale locale = new Locale("en");
        doReturn("PASS_SUBJECT_TEST").when(authEmailMessageSource)
                .getMessage(eq("email.password.reset.subject"), isNull(), any(Locale.class));

        //expect
        assertThat(passwordResetEmailTemplateStrategy.getSubject(locale)).isEqualTo("PASS_SUBJECT_TEST");
    }

    @Test
    @DisplayName("should build correct map of key-values for freeMarker template engine")
    void shouldCorrectlyBuildPropertiesForEmail() throws Exception {
        //given
        NetworkUtil.setHostIP("127.0.0.1");
        NetworkUtil.setHostPort(80);

        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));
        String token = "TEST_TOKEN";
        var passwordReset = TestObjectCreator.createPasswordReset(user, token, LocalDateTime.now());

        doReturn(passwordReset).when(passwordResetRepository).findFirstByUserOrderByCreatedAtDesc(eq(user));

        doAnswer(invocationOnMock -> {
            Map<String, Object> model = invocationOnMock.getArgument(0);

            assertThat(model.get("passwordResetTitle")).isEqualTo(TEST_MESSAGE_SOURCE);
            assertThat(model.get("passwordResetGreeting")).isEqualTo(TEST_MESSAGE_SOURCE);
            assertThat(model.get("passwordResetInfo")).isEqualTo(TEST_MESSAGE_SOURCE);
            assertThat(model.get("userName")).isEqualTo(user.getName());
            assertThat(model.get("passwordResetUrl")).isEqualTo("http://127.0.0.1:80/changePassword?token=" + token);

            return "";

        }).when(passwordResetEmailTemplateStrategy).constructEmailBody(any(Map.class));

        Locale locale = new Locale("en");

        //when
        passwordResetEmailTemplateStrategy.constructEmailBody(user, company, locale);
    }

    @Test
    @DisplayName("should throw NullPointerException when passing null user argument")
    void shouldThrowNpeWhenPassingNullUser() {
        //given
        Locale locale = new Locale("en");

        //when
        Executable codeUnderException = () -> passwordResetEmailTemplateStrategy.constructEmailBody(null, null, locale);

        //then
        assertThrows(NullPointerException.class, codeUnderException,
                "Should throw NullPointerException");
    }

    @Test
    @DisplayName("should throw NullPointerException when no password reset found for user")
    void shouldThrowNpeWhenNoPasswordResetFound() {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));

        Locale locale = new Locale("en");

        //when
        Executable codeUnderException = () -> passwordResetEmailTemplateStrategy.constructEmailBody(user, company, locale);

        //then
        assertThrows(NullPointerException.class, codeUnderException,
                "Should throw NullPointerException");
    }
}