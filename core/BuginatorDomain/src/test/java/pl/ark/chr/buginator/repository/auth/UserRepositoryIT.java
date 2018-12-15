package pl.ark.chr.buginator.repository.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.TestApplicationContext;
import pl.ark.chr.buginator.repository.util.TestObjectCreator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes= TestApplicationContext.class)
public class UserRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldGetUserByEmailAndActive() {
        //given
        var trialPaymentOption = TestObjectCreator.createPaymentOption("Trial");
        entityManager.persist(trialPaymentOption);
        var company = TestObjectCreator.createCompany(trialPaymentOption);
        entityManager.persist(company);
        var testRole = TestObjectCreator.createRole();
        entityManager.persist(testRole);
        var user = TestObjectCreator.createUser(company, testRole);
        entityManager.persist(user);

        //when
        Optional<User> result = userRepository.findByEmailAndActiveTrue(user.getEmail());

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void shouldReturnEmptyWhenUserNotActive() {
        //given
        var trialPaymentOption = TestObjectCreator.createPaymentOption("Trial");
        entityManager.persist(trialPaymentOption);
        var company = TestObjectCreator.createCompany(trialPaymentOption);
        entityManager.persist(company);
        var testRole = TestObjectCreator.createRole();
        entityManager.persist(testRole);
        var user = TestObjectCreator.createUser(company, testRole, false);
        entityManager.persist(user);

        //when
        Optional<User> result = userRepository.findByEmailAndActiveTrue(user.getEmail());

        //then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldReturnEmptyWhenWrongEmailPassed() {
        //given
        var trialPaymentOption = TestObjectCreator.createPaymentOption("Trial");
        entityManager.persist(trialPaymentOption);
        var company = TestObjectCreator.createCompany(trialPaymentOption);
        entityManager.persist(company);
        var testRole = TestObjectCreator.createRole();
        entityManager.persist(testRole);
        var user = TestObjectCreator.createUser(company, testRole);
        entityManager.persist(user);

        //when
        Optional<User> result = userRepository.findByEmailAndActiveTrue("notExisting@gmail.com");

        //then
        assertThat(result.isPresent()).isFalse();
    }
}