package pl.ark.chr.buginator.app.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.app.BuginatorApplication;
import pl.ark.chr.buginator.commons.util.Pair;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.repository.auth.RoleRepository;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.util.TestApplicationContext;
import pl.ark.chr.buginator.util.TestObjectCreator;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BuginatorApplication.class, TestApplicationContext.class})
@Transactional
public class ApplicationServiceIT {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("should get all applications that are connected to logged user")
    void shouldGetUserApplications() {
        //given
        PaymentOption paymentOption = setupPaymentOption();
        var company = TestObjectCreator.createCompany(paymentOption);

        testEntityManager.persist(company);

        var testUserEmail = "getuserpppstest@gmail.com";

        var role = roleRepository.findById(Role.MANAGER).orElseThrow();
        var user = TestObjectCreator.createUser(company, role, testUserEmail);
        testEntityManager.persist(user);

        TestObjectCreator.setAuthentication(authenticationManager, testUserEmail);

        Pair<Application, UserApplication> userApp1 = TestObjectCreator.createApplicationForUser("App1", company, user);
        Pair<Application, UserApplication> userApp2 = TestObjectCreator.createApplicationForUser("App2", company, user);
        testEntityManager.persist(userApp1._1);
        testEntityManager.persist(userApp1._2);
        testEntityManager.persist(userApp2._1);
        testEntityManager.persist(userApp2._2);

        Application app3 = TestObjectCreator.createApplication("App3", company);
        testEntityManager.persist(app3);

        createErrors(userApp1);

        //when
        Set<ApplicationDTO> userApplications = applicationService.getUserApps();

        //then
        assertThat(userApplications)
                .isNotEmpty()
                .hasSize(2);
        userApplications.forEach(ua -> {
            if (ua.getName().equals(userApp1._1.getName())) {
                assertThat(ua.getAllErrorCount()).isEqualTo(5);
                assertThat(ua.getLastWeekErrorCount()).isEqualTo(3);
            } else {
                assertThat(ua.getAllErrorCount()).isEqualTo(0);
                assertThat(ua.getLastWeekErrorCount()).isEqualTo(0);
            }
        });
    }

    @Test
    @DisplayName("should throw NullPointerException when no logged used is found")
    void shouldThrowNPEWhenNoLoggedUserPresent() {
        //when
        Executable codeUnderException = () -> applicationService.getUserApps();

        //then
        assertThrows(NullPointerException.class, codeUnderException, "should throws NPE");
    }

    @Test
    @DisplayName("should create application and link it to logged user with modify permission")
    void shouldCreateApplication() {
        //given
        PaymentOption paymentOption = setupPaymentOption();
        var company = TestObjectCreator.createCompany(paymentOption);

        testEntityManager.persist(company);

        var testUserEmail = "createtest@gmail.com";

        var role = roleRepository.findById(Role.MANAGER).orElseThrow();
        var user = TestObjectCreator.createUser(company, role, testUserEmail);
        testEntityManager.persist(user);

        TestObjectCreator.setAuthentication(authenticationManager, testUserEmail);

        var appName = "TrialApp";

        ApplicationRequestDTO applicationRequest = new ApplicationRequestDTO(appName);

        //when
        UserApplicationDTO userApplication = applicationService.create(applicationRequest);

        //then
        assertThat(userApplication.getName())
                .isNotEmpty()
                .isEqualTo(appName);
        assertThat(userApplication.isModify()).isTrue();
        Application savedApp = applicationRepository.findById(userApplication.getId()).orElseThrow();
        assertThat(savedApp.getCompany().getId()).isEqualTo(company.getId());
    }

    @Test
    @DisplayName("should get application details by id")
    void shouldGetApplicationDetails() {
        //given
        PaymentOption paymentOption = setupPaymentOption();
        var company = TestObjectCreator.createCompany(paymentOption);

        testEntityManager.persist(company);

        var testUserEmail = "getappdetailstest@gmail.com";

        var role = roleRepository.findById(Role.MANAGER).orElseThrow();
        var user = TestObjectCreator.createUser(company, role, testUserEmail);
        testEntityManager.persist(user);

        TestObjectCreator.setAuthentication(authenticationManager, testUserEmail);

        Pair<Application, UserApplication> userApp1 = TestObjectCreator.createApplicationForUser("App7", company, user);
        testEntityManager.persist(userApp1._1);
        testEntityManager.persist(userApp1._2);

        createErrors(userApp1);

        //when
        ApplicationDetailsDTO applicationDetails = applicationService.get(userApp1._1.getId());

        //then
        assertThat(applicationDetails.getName()).isEqualTo(userApp1._1.getName());
        assertThat(applicationDetails.getErrors())
                .isNotEmpty()
                .hasSize(5);
    }

    private PaymentOption setupPaymentOption() {
        var paymentOption = PaymentOption.getPaymentOption(PaymentOption.TRIAL);
        paymentOption.setDuration(30);
        paymentOption.setMaxUsers(5);
        paymentOption.setPrice(0.0);
        return paymentOption;
    }

    private void createErrors(Pair<Application, UserApplication> userApp1) {
        LocalDateTime currentTime = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            Error error = TestObjectCreator.createError(userApp1._1, "Error" + i, currentTime.minusDays(i * 3));
            testEntityManager.persist(error);
        }
    }
}