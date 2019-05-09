package pl.ark.chr.buginator.app.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.app.BuginatorApplication;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.repository.auth.RoleRepository;
import pl.ark.chr.buginator.util.TestApplicationContext;
import pl.ark.chr.buginator.util.TestObjectCreator;

import javax.persistence.EntityManager;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BuginatorApplication.class, TestApplicationContext.class})
@Transactional
class ApplicationServiceIT {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void shouldGetUserApplications() {
        //given
        var paymentOption = PaymentOption.getPaymentOption(PaymentOption.TRIAL);
        paymentOption.setDuration(30);
        paymentOption.setMaxUsers(5);
        paymentOption.setPrice(0.0);
        var company = new Company("ITTestComp", paymentOption);
        testEntityManager.persist(company);
        var role = roleRepository.findById(Role.MANAGER).orElseThrow();
        var user = TestObjectCreator.createUser(company, role);
        testEntityManager.persist(user);


        TestObjectCreator.setAuthentication(authenticationManager);

        //when
        Set<ApplicationDTO> userApplications = applicationService.getUserApps();

        //then
        fail();
    }
}