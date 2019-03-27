package pl.ark.chr.buginator.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.app.BuginatorProperties;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.app.exceptions.RestException;
import pl.ark.chr.buginator.app.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.repository.auth.UserRepository;
import pl.ark.chr.buginator.service.EmailService;
import pl.ark.chr.buginator.service.UserService;
import pl.ark.chr.buginator.data.Credentials;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2016-11-28.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTest {

    private static final String TEST_MESSAGE_SOURCE_RETURN = "TEST INFO";

    @InjectMocks
    private UserService sut = new UserServiceImpl();

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private BuginatorProperties buginatorProperties;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() throws Exception {
        when(messageSource.getMessage(any(String.class), nullable(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
        when(buginatorProperties.getBcryptStrength()).thenReturn(11);
    }

    @Test
    public void testLoadUserByEmail__Success() {
        //given
        String login = "test@gmail.com";

        Role role = new Role();
        role.setId(1L);
        role.setName("Test role");

        User user = new User.Builder().build();
        user.setEmail(login);
        user.setId(1L);
        user.setRole(role);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        //when
        User loadedUser = sut.loadUserByEmail(login, new Locale("en"));

        //then
        assertThat(loadedUser).isNotNull();
        assertThat(loadedUser.getEmail()).isEqualTo(login);
        assertThat(loadedUser.getId()).isEqualTo(user.getId());
        assertThat(loadedUser.getRole()).isNotNull();
        assertThat(loadedUser.getRole().getName()).isEqualTo(role.getName());
    }

    @Test
    public void testLoadUserByEmail__UserNotFound() {
        //given
        String login = "test@gmail.com";

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        //when
        Executable codeUnderException = () -> sut.loadUserByEmail(login, new Locale("en"));

        //then
        var illegalArgumentException = assertThrows(UsernameNotFoundException.class, codeUnderException,
                "should throw UsernameNotFoundException");
        assertThat(illegalArgumentException.getMessage()).isEqualTo(TEST_MESSAGE_SOURCE_RETURN + " " + login);
    }

    @Test
    public void testLoadUserByEmail__UserHasNoRole() {
        //given
        String login = "test@gmail.com";

        User user = new User.Builder().build();
        user.setEmail(login);
        user.setId(1L);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        //when
        Executable codeUnderException = () -> sut.loadUserByEmail(login, new Locale("en"));

        //then
        var illegalArgumentException = assertThrows(UsernameNotFoundException.class, codeUnderException,
                "should throw UsernameNotFoundException");
        assertThat(illegalArgumentException.getMessage()).isEqualTo(TEST_MESSAGE_SOURCE_RETURN + " " + login + " " + TEST_MESSAGE_SOURCE_RETURN);
    }

    @Test
    public void testValidateUserLogin__ExpiryDate() throws RestException {
        //given
        LocalDate yesterday = LocalDate.now().minusDays(1L);
        PaymentOption po = new PaymentOption();
        po.setDuration(-1);
        Company company = new Company("Test compy", po);
//        company.setExpiryDate(yesterday);

        String login = "test@gmail.com";

        Role role = new Role();
        role.setId(1L);
        role.setName("Test role");

        User user = new User.Builder().build();
        user.setEmail(login);
        user.setId(1L);
        user.setRole(role);
        user.setCompany(company);

        Credentials credentials = new Credentials();
        credentials.setUsername(user.getEmail());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        //when
        Executable codeUnderException = () -> sut.validateUserLogin(credentials, new Locale("en"));

        //then
        var restException = assertThrows(RestException.class, codeUnderException,
                "should throw RestException");
        assertThat(restException.getMessage()).isEqualTo(TEST_MESSAGE_SOURCE_RETURN);
        assertThat(restException.getRequestBody()).isInstanceOf(Credentials.class).isEqualTo(credentials);
        assertThat(restException.getStatus().value()).isEqualTo(403);
    }

    @Test
    public void testValidateUserLogin__ExpiryDateIsToday() throws RestException {
        //given
        LocalDate today = LocalDate.now();
        Company company = new Company("Test", new PaymentOption());
//        company.setExpiryDate(today);

        String login = "test@gmail.com";

        Role role = new Role();
        role.setId(1L);
        role.setName("Test role");

        User user = new User.Builder().build();
        user.setEmail(login);
        user.setId(1L);
        user.setRole(role);
        user.setCompany(company);

        Credentials credentials = new Credentials();
        credentials.setUsername(user.getEmail());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        //when
        User loaded = sut.validateUserLogin(credentials, new Locale("en"));

        //then
        assertThat(loaded.getEmail()).isEqualTo(login);
    }
}