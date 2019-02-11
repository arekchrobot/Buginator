package pl.ark.chr.buginator.auth.password.reset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.auth.AuthApplication;
import pl.ark.chr.buginator.auth.email.sender.EmailSender;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.auth.email.template.strategies.PasswordResetEmailTemplateStrategy;
import pl.ark.chr.buginator.auth.util.TestApplicationContext;
import pl.ark.chr.buginator.auth.util.TestObjectCreator;
import pl.ark.chr.buginator.domain.auth.PasswordReset;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.PasswordResetRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import javax.persistence.EntityManager;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AuthApplication.class, TestApplicationContext.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PasswordResetServiceIT {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private PasswordResetRepository delegatedMockPasswordResetRepository;
    @SpyBean
    private EmailSender delegatedMockEmailSender;
    @SpyBean
    private PasswordResetEmailTemplateStrategy delegatedMockPasswordResetEmailTemplateStrategy;
    @SpyBean
    private UserRepository delegatedMockUserRepository;

    @AfterEach
    void tearDown() {
        reset(delegatedMockPasswordResetRepository, delegatedMockEmailSender,
                delegatedMockPasswordResetEmailTemplateStrategy, delegatedMockUserRepository);
    }

    @Test
    @DisplayName("should create password reset and send it via email")
    public void shouldCreatePasswordResetAndSendCorrectEmail() throws Exception {
        //given
        var paymentOption = TestObjectCreator.createPaymentOption("TEST_PAYMENT");
        entityManager.persist(paymentOption);
        var company = TestObjectCreator.createCompany(paymentOption);
        entityManager.persist(company);
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));
        entityManager.persist(user);

        //when
        passwordResetService.sendPasswordResetEmail(user.getEmail());

        //then
        PasswordReset passwordReset = passwordResetRepository.findFirstByUserOrderByCreatedAtDesc(user);

        assertThat(passwordReset).isNotNull();
        assertThat(passwordReset.getToken()).isNotEmpty();
        assertThat(passwordReset.isTokenUsed()).isFalse();

        verify(delegatedMockPasswordResetRepository, times(1)).save(any(PasswordReset.class));
        verify(delegatedMockUserRepository, times(1)).findByEmail(eq(user.getEmail()));
        verify(delegatedMockEmailSender, times(1)).composeAndSendEmail(eq(user), eq(company), eq(EmailType.PASSWORD_RESET));
        verify(delegatedMockPasswordResetEmailTemplateStrategy, times(1)).constructEmailBody(eq(user), eq(company), any(Locale.class));
    }

    @Test
    @DisplayName("should change user password")
    public void shouldChangeUserPassword() {
        //given
        var paymentOption = TestObjectCreator.createPaymentOption("TEST_PAYMENT");
        entityManager.persist(paymentOption);
        var company = TestObjectCreator.createCompany(paymentOption);
        entityManager.persist(company);
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER), false);
        entityManager.persist(user);

        String token = "TEST_TOKEN_123";
        var passwordReset = new PasswordReset(user, token);
        entityManager.persist(passwordReset);

        String newPass = "changedPass";
        var passwordResetDTO = new PasswordResetDTO(token, newPass);

        //when
        passwordResetService.changePassword(passwordResetDTO);

        //then
        @SuppressWarnings("OptionalGetWithoutIsPresent") User userWithNewPass = userRepository.findByEmail(user.getEmail()).get();
        assertTrue(passwordEncoder.matches(newPass, userWithNewPass.getPassword()));

        PasswordReset usedPasswordReset = passwordResetRepository.findFirstByUserOrderByCreatedAtDesc(user);
        assertThat(usedPasswordReset.isTokenUsed()).isTrue();

        verify(delegatedMockPasswordResetRepository, times(1)).findByToken(eq(token));
        verify(delegatedMockPasswordResetRepository, times(1)).save(eq(passwordReset));
        verify(userRepository, times(1)).save(eq(user));
    }
}
