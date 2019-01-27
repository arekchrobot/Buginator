package pl.ark.chr.buginator.auth.email.template.strategies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.auth.AuthApplication;
import pl.ark.chr.buginator.auth.util.TestApplicationContext;
import pl.ark.chr.buginator.auth.util.TestObjectCreator;

import java.util.Locale;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AuthApplication.class, TestApplicationContext.class})
@Transactional
public class RegisterEmailTemplateStrategyIT {

    @Autowired
    private RegisterEmailTemplateStrategy registerEmailTemplateStrategy;

    private static final String EN_BODY = "<html>\n" +
            "    <body>\n" +
            "        <p style=\"margin:15px 0;\">\n" +
            "            <b>Thank you for registering to Buginator</b>\n" +
            "        </p>\n" +
            "        <p style=\"margin:15px 0;\">Welcome to Buginator application, TEST!</p>\n" +
            "        <p style=\"margin:15px 0;\">Here are your api keys to authenticate:</p>\n" +
            "        <p style=\"margin:15px 0;\">Unique key: ${uniqueKey}</p>\n" +
            "        <p style=\"margin:15px 0;\">Token: ${token}</p>\n" +
            "        Buginator.com</p>\n" +
            "    </body>\n" +
            "</html>";

    private static final String PL_BODY = "<html>\n" +
            "    <body>\n" +
            "        <p style=\"margin:15px 0;\">\n" +
            "            <b>Dziękujemy za rejestrację w aplikacji Buginator</b>\n" +
            "        </p>\n" +
            "        <p style=\"margin:15px 0;\">Witaj w aplikacji Buginator, TEST!</p>\n" +
            "        <p style=\"margin:15px 0;\">Poniżej znajdują się klucze do uwierzytelniania twojego api:</p>\n" +
            "        <p style=\"margin:15px 0;\">Unikalny klucz: ${uniqueKey}</p>\n" +
            "        <p style=\"margin:15px 0;\">Token: ${token}</p>\n" +
            "        Buginator.com</p>\n" +
            "    </body>\n" +
            "</html>";

    @ParameterizedTest(name = "For locale: \"{0}\"")
    @MethodSource("emailBodyConstructProvider")
    @DisplayName("should correctly build register email body")
    void shouldCorrectlyConstructEmailBody(Locale locale, String templateBody) throws Exception {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("test"));
        String emailBody = templateBody
                .replace("${uniqueKey}", company.getUniqueKey())
                .replace("${token}", company.getToken());

        //when
        String constructedEmailBody = registerEmailTemplateStrategy.constructEmailBody(company, locale);

        //then
        assertThat(constructedEmailBody).isEqualTo(emailBody);
    }

    private static Stream<Arguments> emailBodyConstructProvider() {
        return Stream.of(
                Arguments.of(new Locale("en"), EN_BODY),
                Arguments.of(new Locale("pl"), PL_BODY)
        );
    }

    @ParameterizedTest(name = "For locale: \"{0}\"")
    @MethodSource("subjectLocaleProvider")
    @DisplayName("should correctly return register email subject")
    void shouldCorrectlyReturnSubject(Locale locale, String expected) {
        //expect
        assertThat(registerEmailTemplateStrategy.getSubject(locale)).isEqualTo(expected);
    }

    private static Stream<Arguments> subjectLocaleProvider() {
        return Stream.of(
                Arguments.of(new Locale("en"), "Buginator registration confirmation"),
                Arguments.of(new Locale("pl"), "Potwierdzenie rejestracji na platformie Buginator")
        );
    }
}
