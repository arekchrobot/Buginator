package pl.ark.chr.buginator.app.application.report;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.app.BuginatorApplication;
import pl.ark.chr.buginator.commons.util.Pair;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.repository.auth.RoleRepository;
import pl.ark.chr.buginator.util.TestApplicationContext;
import pl.ark.chr.buginator.util.TestObjectCreator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BuginatorApplication.class, TestApplicationContext.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class ReportServiceIT {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("should correctly generate last week errors report")
    void shouldCorrectlyCalculateLastWeekErrorsReport() {
        //given
        Company company = TestObjectCreator.createCompany(TestObjectCreator.trialPaymentOption());
        testEntityManager.persist(company);

        var testUserEmail = "getuserpppstest@gmail.com";

        var role = roleRepository.findById(Role.MANAGER).orElseThrow();
        var user = TestObjectCreator.createUser(company, role, testUserEmail);
        testEntityManager.persist(user);

        TestObjectCreator.setAuthentication(authenticationManager, testUserEmail);

        Pair<Application, UserApplication> userApp1 = TestObjectCreator.createApplicationForUser("App1", company, user);
        testEntityManager.persist(userApp1._1);
        testEntityManager.persist(userApp1._2);

        List<Error> errors = TestObjectCreator.generateErrorListForLastWeekForApplication(userApp1._1);
        errors.forEach(testEntityManager::persist);

        //when
        LastWeekErrorsDTO result = reportService.generateLastWeekErrorReport(userApp1._1.getId());

        //then
        int expectedDataAndLabelsLength = 8;

        assertThat(result.getData())
                .hasSize(expectedDataAndLabelsLength)
                .containsExactly(3L, 0L, 0L, 2L, 0L, 0L, 1L, 3L);
    }
}
