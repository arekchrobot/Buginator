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

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegisterEmailTemplateStrategyTest {

    @Spy
    @InjectMocks
    private RegisterEmailTemplateStrategy registerEmailTemplateStrategy;

    @Mock
    private Configuration freeMarkerConfig;
    @Mock
    private MessageSource authEmailMessageSource;

    private final String TEST_MESSAGE_SOURCE = "test_message";

    @BeforeEach
    void setUp() throws Exception {
        doReturn("").when(registerEmailTemplateStrategy).constructEmailBody(any(Map.class));
        doReturn(TEST_MESSAGE_SOURCE).when(authEmailMessageSource)
                .getMessage(any(String.class), isNull(), any(Locale.class));
    }

    @Test
    @DisplayName("Strategy should be valid for EmailType.REGISTER")
    void shouldBeValidStrategy() {
        //expect
        assertThat(registerEmailTemplateStrategy.isValid(EmailType.REGISTER)).isTrue();
    }

    @Test
    @DisplayName("Should return Subject from given locale for Register mail")
    void shouldGetSubjectFromLocale() {
        //given
        Locale locale = new Locale("en");
        doReturn("SUBJECT_TEST").when(authEmailMessageSource)
                .getMessage(eq("email.register.subject"), isNull(), any(Locale.class));

        //expect
        assertThat(registerEmailTemplateStrategy.getSubject(locale)).isEqualTo("SUBJECT_TEST");
    }

    @Test
    @DisplayName("should build correct map of key-values for freeMarker template engine")
    void shouldCorrectlyBuildPropertiesForEmail() throws Exception {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        Locale locale = new Locale("en");

        doAnswer(invocationOnMock -> {
            Map<String, Object> model = invocationOnMock.getArgument(0);

            assertThat(model.get("registerTitle")).isEqualTo(TEST_MESSAGE_SOURCE);
            assertThat(model.get("registerGreeting")).isEqualTo(TEST_MESSAGE_SOURCE);
            assertThat(model.get("registerInfo")).isEqualTo(TEST_MESSAGE_SOURCE);
            assertThat(model.get("uniqueKeyInfo")).isEqualTo(TEST_MESSAGE_SOURCE);
            assertThat(model.get("tokenInfo")).isEqualTo(TEST_MESSAGE_SOURCE);
            assertThat(model.get("companyName")).isEqualTo(company.getName());
            assertThat(model.get("uniqueKey")).isEqualTo(company.getUniqueKey());
            assertThat(model.get("token")).isEqualTo(company.getToken());

            return "";

        }).when(registerEmailTemplateStrategy).constructEmailBody(any(Map.class));

        //when
        registerEmailTemplateStrategy.constructEmailBody(null, company, locale);
    }

    @Test
    @DisplayName("should throw NullPointerException when passing null company argument")
    void shouldThrowNpeWhenPassingNullCompany() {
        //given
        Locale locale = new Locale("en");

        //when
        Executable codeUnderException = () -> registerEmailTemplateStrategy.constructEmailBody(null, null, locale);

        //then
        assertThrows(NullPointerException.class, codeUnderException,
                "Should throw NullPointerException");
    }
}