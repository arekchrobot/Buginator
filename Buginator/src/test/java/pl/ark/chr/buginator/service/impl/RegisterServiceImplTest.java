package pl.ark.chr.buginator.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.config.shiro.BCryptPasswordService;
import pl.ark.chr.buginator.domain.auth.*;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.auth.RoleRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;
import pl.ark.chr.buginator.service.EmailService;
import pl.ark.chr.buginator.service.RegisterService;
import pl.ark.chr.buginator.data.RegisterData;
import pl.ark.chr.buginator.util.UserCompanyValidator;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2016-11-29.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegisterServiceImplTest {

    private static final String TEST_MESSAGE_SOURCE_RETURN = "TEST INFO";
    private static final String TEST_BCRYPTED_PASSWORD = "TEST_PASS_BCRYPTED";

    @InjectMocks
    private RegisterService sut = new RegisterServiceImpl();

    private UserCompanyValidator userCompanyValidator;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentOptionRepository paymentOptionRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private BCryptPasswordService passwordService;

    @Mock
    private MessageSource messageSource;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
        userCompanyValidator = new UserCompanyValidator(companyRepository, userRepository, messageSource);

        when(messageSource.getMessage(any(String.class), nullable(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
        when(passwordService.encryptPassword(any(Object.class))).thenReturn(TEST_BCRYPTED_PASSWORD);


        ((RegisterServiceImpl)sut).setUserCompanyValidator(userCompanyValidator);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRegisterUser__Success() throws ValidationException {
        //given
        PaymentOption defaultPayment = new PaymentOption();
        defaultPayment.setDuration(30);
        defaultPayment.setMaxUsers(5);
        defaultPayment.setPrice(15.0);

        when(paymentOptionRepository.findByName(any(String.class))).thenReturn(defaultPayment);

        Permission permission = new Permission();
        permission.setName("TEST PERM");
        Role defaultRole = new Role();
        defaultRole.setName("TEST ROLE");
        defaultRole.getPermissions().add(permission);

        when(roleRepository.findByName(any(String.class))).thenReturn(defaultRole);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());


        Company company = new Company();
        company.setName("TEST COMPANY");
        company.setAddress("TEST ADDRESS");

        String email = "testEmail@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setName("TEST USER");
        user.setPassword("TEST_PASSWORD");

        RegisterData registerData = new RegisterData();
        registerData.setCompany(company);
        registerData.setUser(user);

        doAnswer(invocationOnMock -> {
            Company companyToSave = (Company) invocationOnMock.getArguments()[0];

            LocalDate expectedExpiryDate = LocalDate.now().plusDays(defaultPayment.getDuration());

            assertThat(companyToSave)
                    .isNotNull();
            assertThat(companyToSave.getExpiryDate())
                    .isNotNull()
                    .isEqualTo(expectedExpiryDate);
            assertThat(companyToSave.getSelectedPaymentOption())
                    .isNotNull();
            assertThat(companyToSave.getUserLimit())
                    .isEqualTo(defaultPayment.getMaxUsers());
            assertThat(companyToSave.getToken())
                    .isNotNull()
                    .isNotEmpty();
            assertThat(companyToSave.getUniqueKey())
                    .isNotNull()
                    .isNotEmpty();

            return companyToSave;
        }).when(companyRepository).save(any(Company.class));

        doAnswer(invocationOnMock -> {
            User userToSave = (User) invocationOnMock.getArguments()[0];

            assertThat(userToSave)
                    .isNotNull();
            assertThat(userToSave.getCompany())
                    .isNotNull();
            assertThat(userToSave.getCompany().getName())
                    .isEqualTo(company.getName());
            assertThat(userToSave.getRole())
                    .isNotNull();
            assertThat(userToSave.getPassword())
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(TEST_BCRYPTED_PASSWORD);
            assertThat(userToSave.getEmail())
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(email.toLowerCase());

            return userToSave;
        }).when(userRepository).save(any(User.class));

        //when
        sut.registerUser(registerData, new Locale("en"));

        //then
    }

    @Test
    public void testRegisterUser__CompanyNameIsNull() throws ValidationException {
        //given
        Company company = new Company();
        company.setAddress("TEST ADDRESS");

        RegisterData registerData = new RegisterData();
        registerData.setCompany(company);

        fluentThrown
                .expect(ValidationException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.registerUser(registerData, new Locale("pl"));

        //then
    }

    @Test
    public void testRegisterUser__CompanyAddressIsBlank() throws ValidationException {
        //given
        Company company = new Company();
        company.setName("TEST");
        company.setAddress("");

        RegisterData registerData = new RegisterData();
        registerData.setCompany(company);

        fluentThrown
                .expect(ValidationException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.registerUser(registerData, new Locale("pl"));

        //then
    }

    @Test
    public void testRegisterUser__CompanyExists() throws ValidationException {
        //given
        Company company = new Company();
        company.setName("TEST");
        company.setAddress("TEST ADDRESS");

        RegisterData registerData = new RegisterData();
        registerData.setCompany(company);

        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.of(company));

        fluentThrown
                .expect(IllegalArgumentException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN + " " + company.getName() + " " + TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.registerUser(registerData, new Locale("pl"));

        //then
    }

    @Test
    public void testRegisterUser__UserEmailIsNull() throws ValidationException {
        //given
        Company company = new Company();
        company.setName("TEST");
        company.setAddress("TEST ADDRESS");

        User user = new User();
        user.setName("TEST USER");
        user.setPassword("TEST_PASSWORD");

        RegisterData registerData = new RegisterData();
        registerData.setCompany(company);
        registerData.setUser(user);

        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        fluentThrown
                .expect(ValidationException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.registerUser(registerData, new Locale("pl"));

        //then
    }

    @Test
    public void testRegisterUser__UserUsernameIsBlank() throws ValidationException {
        //given
        Company company = new Company();
        company.setName("TEST");
        company.setAddress("TEST ADDRESS");

        User user = new User();
        user.setEmail("testEmail@gmail.com");
        user.setName("");
        user.setPassword("TEST_PASSWORD");

        RegisterData registerData = new RegisterData();
        registerData.setCompany(company);
        registerData.setUser(user);

        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        fluentThrown
                .expect(ValidationException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.registerUser(registerData, new Locale("pl"));

        //then
    }

    @Test
    public void testRegisterUser__UserPasswordIsNull() throws ValidationException {
        //given
        Company company = new Company();
        company.setName("TEST");
        company.setAddress("TEST ADDRESS");

        User user = new User();
        user.setEmail("testEmail@gmail.com");
        user.setName("TEST NAME");

        RegisterData registerData = new RegisterData();
        registerData.setCompany(company);
        registerData.setUser(user);

        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        fluentThrown
                .expect(ValidationException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.registerUser(registerData, new Locale("pl"));

        //then
    }

    @Test
    public void testRegisterUser__UserExists() throws ValidationException {
        //given
        Company company = new Company();
        company.setName("TEST");
        company.setAddress("TEST ADDRESS");

        User user = new User();
        user.setEmail("testEmail@gmail.com");
        user.setName("TEST NAME");
        user.setPassword("TEST_PASSWORD");

        RegisterData registerData = new RegisterData();
        registerData.setCompany(company);
        registerData.setUser(user);

        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        fluentThrown
                .expect(IllegalArgumentException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN + " " + user.getEmail() + " " + TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.registerUser(registerData, new Locale("pl"));

        //then
    }
}