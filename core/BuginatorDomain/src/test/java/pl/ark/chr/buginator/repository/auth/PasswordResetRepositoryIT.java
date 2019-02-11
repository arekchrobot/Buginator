package pl.ark.chr.buginator.repository.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.ark.chr.buginator.domain.auth.PasswordReset;
import pl.ark.chr.buginator.repository.TestApplicationContext;
import pl.ark.chr.buginator.repository.util.TestObjectCreator;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes= TestApplicationContext.class)
public class PasswordResetRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Test
    @DisplayName("should find password reset by given token")
    public void shouldFindByToken() {
        //given
        var trialPaymentOption = TestObjectCreator.createPaymentOption("Trial");
        entityManager.persist(trialPaymentOption);
        var company = TestObjectCreator.createCompany(trialPaymentOption);
        entityManager.persist(company);
        var testRole = TestObjectCreator.createRole();
        entityManager.persist(testRole);
        var user = TestObjectCreator.createUser(company, testRole);
        entityManager.persist(user);

        var token = UUID.randomUUID().toString();

        var passwordReset = TestObjectCreator.createPasswordReset(user, token);
        entityManager.persist(passwordReset);

        //when
        Optional<PasswordReset> result = passwordResetRepository.findByToken(token);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("should not find password reset by given token")
    public void shouldNotFindByToken() {
        //given
        var token = UUID.randomUUID().toString();

        //when
        Optional<PasswordReset> result = passwordResetRepository.findByToken(token);

        //then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("should find last created password reset for user")
    public void shouldFindLastCreatedPasswordReset() {
        //given
        var trialPaymentOption = TestObjectCreator.createPaymentOption("Trial");
        entityManager.persist(trialPaymentOption);
        var company = TestObjectCreator.createCompany(trialPaymentOption);
        entityManager.persist(company);
        var testRole = TestObjectCreator.createRole();
        entityManager.persist(testRole);
        var user = TestObjectCreator.createUser(company, testRole);
        entityManager.persist(user);

        var token = UUID.randomUUID().toString();
        var token2 = UUID.randomUUID().toString();

        var passwordReset1 = TestObjectCreator.createPasswordReset(user, token, LocalDateTime.now().minusDays(2));
        var passwordReset2 = TestObjectCreator.createPasswordReset(user, token2, LocalDateTime.now().minusDays(1));
        entityManager.persist(passwordReset1);
        entityManager.persist(passwordReset2);

        //when
        PasswordReset result = passwordResetRepository.findFirstByUserOrderByCreatedAtDesc(user);

        //then
        assertThat(result.getCreatedAt()).isEqualTo(passwordReset2.getCreatedAt());
    }

    @Test
    @DisplayName("should not allow to persist multiple password resets with same token")
    public void shouldNotAllowDuplicateTokens() {
        //given
        var trialPaymentOption = TestObjectCreator.createPaymentOption("Trial");
        entityManager.persist(trialPaymentOption);
        var company = TestObjectCreator.createCompany(trialPaymentOption);
        entityManager.persist(company);
        var testRole = TestObjectCreator.createRole();
        entityManager.persist(testRole);
        var user = TestObjectCreator.createUser(company, testRole);
        entityManager.persist(user);

        var token = UUID.randomUUID().toString();
        var token2 = UUID.randomUUID().toString();

        var passwordReset1 = TestObjectCreator.createPasswordReset(user, token, LocalDateTime.now().minusDays(2));
        var passwordReset2 = TestObjectCreator.createPasswordReset(user, token, LocalDateTime.now().minusDays(1));
        entityManager.persist(passwordReset1);

        //when
        Executable codeUnderException = () -> entityManager.persist(passwordReset2);

        //then
        PersistenceException ex = assertThrows(PersistenceException.class, codeUnderException,
                "Should throw PersistenceException");
        assertThat(ex.getMessage()).isEqualTo("org.hibernate.exception.ConstraintViolationException: could not execute statement");
    }
}
