package pl.ark.chr.buginator.auth.register;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestRegisterApplicationContext.class)
@DataJpaTest
public class RegisterServiceIT {

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Autowired
    private RegisterService registerService;

    @SpyBean
    private CompanyRepository delegatedMockCompanyRepository;
    @SpyBean
    private UserRepository delegatedMockUserRepository;
    @SpyBean
    private PaymentOptionRepository delegatedMockPaymentOptionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldCorrectlyRegister() {
        //given
        var registerDTO = RegisterDTO.builder()
                .companyName("IntegrationTestCompany")
                .userEmail("integrationTestEmail123@gmail.com")
                .userPassword("password123")
                .build();

        //when
        registerService.registerCompanyAndUser(registerDTO);

        //then
        verify(delegatedMockCompanyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(delegatedMockUserRepository, times(1)).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(delegatedMockPaymentOptionRepository, times(1)).findById(eq(PaymentOption.TRIAL));
        verify(delegatedMockCompanyRepository, times(1)).save(any(Company.class));
        verify(delegatedMockUserRepository, times(1)).save(any(User.class));

        Company company = delegatedMockCompanyRepository.findByName(registerDTO.getCompanyName()).orElseThrow();
        PaymentOption paymentOption = delegatedMockPaymentOptionRepository.findById(PaymentOption.TRIAL).orElseThrow();
        User user = delegatedMockUserRepository.findByEmail(registerDTO.getUserEmail().toLowerCase()).orElseThrow();

        assertThat(company.getSelectedPaymentOption().getId()).isEqualTo(PaymentOption.TRIAL);
        assertThat(company.getUserLimit()).isEqualTo(paymentOption.getMaxUsers());
        assertThat(company.getExpiryDate()).isEqualTo(LocalDate.now().plusDays(paymentOption.getDuration()));

        assertThat(user.getName()).isEqualTo(registerDTO.getUserEmail());
        assertThat(user.getEmail()).isEqualTo(registerDTO.getUserEmail().toLowerCase());
        assertThat(user.getRole().getId()).isEqualTo(Role.MANAGER);
        assertThat(user.getCompany().getId()).isEqualTo(company.getId());
        assertThat(user.getPassword())
                .startsWith("{def}");
        assertThat(passwordEncoder.matches(registerDTO.getUserPassword(), user.getPassword())).isTrue();
    }

    @Test
    public void shouldThrowExceptionWhenCompanyIsDuplicated() {
        //given
        PaymentOption paymentOption = PaymentOption.getPaymentOption(PaymentOption.TRIAL);
        paymentOption.setDuration(30);
        paymentOption.setMaxUsers(5);
        paymentOption.setPrice(0.0);
        var company = new Company("ITTestComp", paymentOption);
        testEntityManager.persist(company);

        var registerDTO = RegisterDTO.builder()
                .companyName("ITTestComp")
                .userEmail("integrationTestEmail123@gmail.com")
                .userPassword("password123")
                .build();

        fluentThrown
                .expect(DuplicateException.class)
                .hasMessage("company.duplicate");

        //when
        registerService.registerCompanyAndUser(registerDTO);

        //then
        verify(delegatedMockCompanyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(delegatedMockUserRepository, never()).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(delegatedMockPaymentOptionRepository, never()).findById(eq(PaymentOption.TRIAL));
        verify(delegatedMockCompanyRepository, never()).save(any(Company.class));
        verify(delegatedMockUserRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenUserIsDuplicated() {
        //given
        PaymentOption paymentOption = PaymentOption.getPaymentOption(PaymentOption.TRIAL);
        paymentOption.setDuration(30);
        paymentOption.setMaxUsers(5);
        paymentOption.setPrice(0.0);
        var company = new Company("ITTestComp", paymentOption);
        testEntityManager.persist(company);
        Role role = Role.getRole(Role.MANAGER);
        User user = User.builder()
                .company(company)
                .role(role)
                .email("itTest@gmail.com")
                .name("test")
                .password("{def}12345")
                .active(true)
                .build();
        testEntityManager.persist(user);

        var registerDTO = RegisterDTO.builder()
                .companyName("IntegrationTestCompany123")
                .userEmail(user.getEmail())
                .userPassword("password123")
                .build();

        fluentThrown
                .expect(DuplicateException.class)
                .hasMessage("user.duplicate");

        //when
        registerService.registerCompanyAndUser(registerDTO);

        //then
        verify(delegatedMockCompanyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(delegatedMockUserRepository, times(1)).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(delegatedMockPaymentOptionRepository, never()).findById(eq(PaymentOption.TRIAL));
        verify(delegatedMockCompanyRepository, never()).save(any(Company.class));
        verify(delegatedMockUserRepository, never()).save(any(User.class));
    }
}
