package pl.ark.chr.buginator.auth.register;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.auth.AuthApplication;
import pl.ark.chr.buginator.auth.email.sender.EmailSender;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.auth.util.TestApplicationContext;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AuthApplication.class, TestApplicationContext.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RegisterServiceIT {

    @Autowired
    private RegisterService registerService;

    @SpyBean
    private CompanyRepository delegatedMockCompanyRepository;
    @SpyBean
    private UserRepository delegatedMockUserRepository;
    @SpyBean
    private PaymentOptionRepository delegatedMockPaymentOptionRepository;
    @SpyBean
    private EmailSender delegatedMockEmailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager testEntityManager;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.queue.mailQueue}")
    private String mailQueue;

    @AfterEach
    void tearDown() {
        reset(delegatedMockCompanyRepository, delegatedMockUserRepository, delegatedMockPaymentOptionRepository,
                delegatedMockEmailSender);
    }

    @Test
    @DisplayName("should correctly save company and user in database")
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
        verify(delegatedMockEmailSender, times(1)).composeAndSendEmail(any(User.class), any(Company.class), eq(EmailType.REGISTER));

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

        EmailDTO registerEmail = (EmailDTO) jmsTemplate.receiveAndConvert(mailQueue);
        assertThat(registerEmail).isNotNull();
        assertThat(registerEmail.getTo()).isEqualTo(user.getEmail());
        assertThat(registerEmail.getEmailBody())
                .contains(company.getName())
                .contains(company.getUniqueKey())
                .contains(company.getToken());
    }

    @Test
    @DisplayName("should throw DuplicateException when company exists in database")
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

        //when
        Executable codeUnderException = () -> registerService.registerCompanyAndUser(registerDTO);

        //then
        var duplicateException = assertThrows(DuplicateException.class, codeUnderException,
                "Should throw DuplicateException");
        assertThat(duplicateException.getMessage()).isEqualTo("company.duplicate");
        verify(delegatedMockCompanyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(delegatedMockUserRepository, never()).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(delegatedMockPaymentOptionRepository, never()).findById(eq(PaymentOption.TRIAL));
        verify(delegatedMockCompanyRepository, never()).save(any(Company.class));
        verify(delegatedMockUserRepository, never()).save(any(User.class));
        verify(delegatedMockEmailSender, never()).composeAndSendEmail(any(User.class), any(Company.class), eq(EmailType.REGISTER));
    }

    @Test
    @DisplayName("should throw DuplicateException when user exists in database")
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

        //when
        Executable codeUnderException = () -> registerService.registerCompanyAndUser(registerDTO);

        //then
        var duplicateException = assertThrows(DuplicateException.class, codeUnderException,
                "Should throw DuplicateException");
        assertThat(duplicateException.getMessage()).isEqualTo("user.duplicate");
        verify(delegatedMockCompanyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(delegatedMockUserRepository, times(1)).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(delegatedMockPaymentOptionRepository, never()).findById(eq(PaymentOption.TRIAL));
        verify(delegatedMockCompanyRepository, never()).save(any(Company.class));
        verify(delegatedMockUserRepository, never()).save(any(User.class));
        verify(delegatedMockEmailSender, never()).composeAndSendEmail(any(User.class), any(Company.class), eq(EmailType.REGISTER));
    }
}
