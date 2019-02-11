package pl.ark.chr.buginator.auth.email.template.strategies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.auth.AuthApplication;
import pl.ark.chr.buginator.auth.util.TestApplicationContext;
import pl.ark.chr.buginator.auth.util.TestObjectCreator;
import pl.ark.chr.buginator.commons.util.NetworkUtil;
import pl.ark.chr.buginator.domain.auth.Role;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AuthApplication.class, TestApplicationContext.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PasswordResetEmailTemplateStrategyIT {

    @Autowired
    private PasswordResetEmailTemplateStrategy passwordResetEmailTemplateStrategy;

    @Autowired
    private EntityManager entityManager;

    private static final String EN_BODY = "<html>\n" +
            "    <body>\n" +
            "        <p style=\"margin:15px 0;\">\n" +
            "            <b>There has been a request to reset your password</b>\n" +
            "        </p>\n" +
            "        <p style=\"margin:15px 0;\">Hello, TEST_USER!</p>\n" +
            "        <p style=\"margin:15px 0;\">In order to reset your password please open the following link in browser. Given link is available for 24 hours:</p>\n" +
            "        <p style=\"margin:15px 0;\"><a href=\"${passwordResetUrl}\">${passwordResetUrl}</a></p>\n" +
            "        Buginator.com</p>\n" +
            "    </body>\n" +
            "</html>";

    private static final String PL_BODY = "<html>\n" +
            "    <body>\n" +
            "        <p style=\"margin:15px 0;\">\n" +
            "            <b>Zostało zgłoszone żadanie resetu twojego hasła</b>\n" +
            "        </p>\n" +
            "        <p style=\"margin:15px 0;\">Witaj, TEST_USER!</p>\n" +
            "        <p style=\"margin:15px 0;\">W celu zresetowania hasła otwórz poniższy adres w przeglądarce. Link jest ważny przez 24 godziny:</p>\n" +
            "        <p style=\"margin:15px 0;\"><a href=\"${passwordResetUrl}\">${passwordResetUrl}</a></p>\n" +
            "        Buginator.com</p>\n" +
            "    </body>\n" +
            "</html>";

    @ParameterizedTest(name = "For locale: \"{0}\"")
    @MethodSource("emailBodyConstructProvider")
    @DisplayName("should correctly build password reset email body")
    void shouldCorrectlyConstructEmailBody(Locale locale, String templateBody) throws Exception {
        //given
        NetworkUtil.setHostIP("127.0.0.1");
        NetworkUtil.setHostPort(80);

        var paymentOption = TestObjectCreator.createPaymentOption("test");
        entityManager.persist(paymentOption);
        var company = TestObjectCreator.createCompany(paymentOption);
        entityManager.persist(company);
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));
        entityManager.persist(user);

        String token = "123_TOKEN";
        var passwordReset = TestObjectCreator.createPasswordReset(user, token, LocalDateTime.now());
        entityManager.persist(passwordReset);

        String passwordResetUrl = "http://127.0.0.1:80/changePassword?token=" + token;

        String emailBody = templateBody
                .replace("${passwordResetUrl}", passwordResetUrl);

        //when
        String constructedEmailBody = passwordResetEmailTemplateStrategy.constructEmailBody(user, company, locale);

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
    @DisplayName("should correctly return password reset email subject")
    void shouldCorrectlyReturnSubject(Locale locale, String expected) {
        //expect
        assertThat(passwordResetEmailTemplateStrategy.getSubject(locale)).isEqualTo(expected);
    }

    private static Stream<Arguments> subjectLocaleProvider() {
        return Stream.of(
                Arguments.of(new Locale("en"), "Buginator password reset"),
                Arguments.of(new Locale("pl"), "Reset hasła na platformie Buginator")
        );
    }
}
