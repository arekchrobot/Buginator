package pl.ark.chr.buginator.auth.register;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ark.chr.buginator.auth.util.TestObjectCreator;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {

    private RegisterService registerService;

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PaymentOptionRepository paymentOptionRepository;

    private PasswordEncoder passwordEncoder = TestObjectCreator.passwordEncoder();

    @BeforeEach
    public void setUp() throws Exception {
        registerService = new RegisterService(companyRepository, userRepository, paymentOptionRepository, passwordEncoder);
    }

    @Test
    @DisplayName("should successfully create company and user objects from RegisterDTO")
    public void shouldSuccessfullyRegister() {
        //given
        var registerDTO = RegisterDTO.builder()
                .companyName("TestCompany")
                .userEmail("testEmail123@gmail.com")
                .userPassword("password")
                .build();
        var paymentOption = TestObjectCreator.createPaymentOption("TEST");
        when(companyRepository.findByName(eq(registerDTO.getCompanyName()))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(eq(registerDTO.getUserEmail().toLowerCase()))).thenReturn(Optional.empty());
        when(paymentOptionRepository.findById(eq(PaymentOption.TRIAL))).thenReturn(Optional.of(paymentOption));

        doAnswer(invocationOnMock -> {
            Company companyToSave = (Company) invocationOnMock.getArguments()[0];

            LocalDate expectedExpiryDate = LocalDate.now().plusDays(paymentOption.getDuration());

            assertThat(companyToSave)
                    .isNotNull();
            assertThat(companyToSave.getToken())
                    .isNotEmpty();
            assertThat(companyToSave.getUniqueKey())
                    .isNotEmpty();
            assertThat(companyToSave.getUserLimit())
                    .isEqualTo(paymentOption.getMaxUsers());
            assertThat(companyToSave.getExpiryDate())
                    .isNotNull()
                    .isEqualTo(expectedExpiryDate);
            assertThat(companyToSave.getAddress())
                    .isNull();

            return companyToSave;
        }).when(companyRepository).save(any(Company.class));

        doAnswer(invocationOnMock -> {
            User userToSave = (User) invocationOnMock.getArguments()[0];

            assertThat(userToSave)
                    .isNotNull();
            assertThat(userToSave.getCompany())
                    .isNotNull();
            assertThat(userToSave.getName())
                    .isEqualTo(registerDTO.getUserEmail());
            assertThat(userToSave.getRole())
                    .isNotNull();
            assertThat(userToSave.getPassword())
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo("{def}" + registerDTO.getUserPassword());
            assertThat(userToSave.getEmail())
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(registerDTO.getUserEmail().toLowerCase());

            return userToSave;
        }).when(userRepository).save(any(User.class));

        //when
        registerService.registerCompanyAndUser(registerDTO);

        //then
        verify(companyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(userRepository, times(1)).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(paymentOptionRepository, times(1)).findById(eq(PaymentOption.TRIAL));
        verify(companyRepository, times(1)).save(any(Company.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("should append explicit username instead of email when passed in RegisterDTO")
    public void shouldSetExplicitUsername() {
        //given
        var registerDTO = RegisterDTO.builder()
                .companyName("TestCompany")
                .userEmail("testEmail123@gmail.com")
                .userName("userTest")
                .userPassword("password")
                .build();
        var paymentOption = TestObjectCreator.createPaymentOption("TEST");
        when(companyRepository.findByName(eq(registerDTO.getCompanyName()))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(eq(registerDTO.getUserEmail().toLowerCase()))).thenReturn(Optional.empty());
        when(paymentOptionRepository.findById(eq(PaymentOption.TRIAL))).thenReturn(Optional.of(paymentOption));
        when(companyRepository.save(any(Company.class))).then(returnsFirstArg());

        doAnswer(invocationOnMock -> {
            User userToSave = (User) invocationOnMock.getArguments()[0];

            assertThat(userToSave)
                    .isNotNull();
            assertThat(userToSave.getName())
                    .isEqualTo(registerDTO.getUserName());
            assertThat(userToSave.getEmail())
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(registerDTO.getUserEmail().toLowerCase());

            return userToSave;
        }).when(userRepository).save(any(User.class));

        //when
        registerService.registerCompanyAndUser(registerDTO);

        //then
        verify(companyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(userRepository, times(1)).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(paymentOptionRepository, times(1)).findById(eq(PaymentOption.TRIAL));
        verify(companyRepository, times(1)).save(any(Company.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("should throw DuplicateException when company exists in database")
    public void shouldThrowExceptionWhenCompanyExists() {
        //given
        var registerDTO = RegisterDTO.builder()
                .companyName("TestCompany")
                .userEmail("testEmail123@gmail.com")
                .userPassword("password")
                .build();
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("TEST"));
        when(companyRepository.findByName(eq(registerDTO.getCompanyName()))).thenReturn(Optional.of(company));

        //when
        Executable codeUnderException = () -> registerService.registerCompanyAndUser(registerDTO);

        //then
        var duplicateException = assertThrows(DuplicateException.class, codeUnderException,
                "Should throw DuplicateException");
        assertThat(duplicateException.getMessage()).isEqualTo("company.duplicate");
        verify(companyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(userRepository, never()).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(paymentOptionRepository, never()).findById(eq(PaymentOption.TRIAL));
        verify(companyRepository, never()).save(any(Company.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("should throw DuplicateException when user exists in database")
    public void shouldThrowExceptionWhenUserExists() {
        //given
        var registerDTO = RegisterDTO.builder()
                .companyName("TestCompany")
                .userEmail("testEmail123@gmail.com")
                .userName("userTest")
                .userPassword("password")
                .build();
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("TEST"));
        var user = TestObjectCreator.createUser(company, new Role());
        when(companyRepository.findByName(eq(registerDTO.getCompanyName()))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(eq(registerDTO.getUserEmail().toLowerCase()))).thenReturn(Optional.of(user));

        //when
        Executable codeUnderException = () -> registerService.registerCompanyAndUser(registerDTO);

        //then
        var duplicateException = assertThrows(DuplicateException.class, codeUnderException,
                "Should throw DuplicateException");
        assertThat(duplicateException.getMessage()).isEqualTo("user.duplicate");
        verify(companyRepository, times(1)).findByName(eq(registerDTO.getCompanyName()));
        verify(userRepository, times(1)).findByEmail(eq(registerDTO.getUserEmail().toLowerCase()));
        verify(paymentOptionRepository, never()).findById(eq(PaymentOption.TRIAL));
        verify(companyRepository, never()).save(any(Company.class));
        verify(userRepository, never()).save(any(User.class));
    }
}