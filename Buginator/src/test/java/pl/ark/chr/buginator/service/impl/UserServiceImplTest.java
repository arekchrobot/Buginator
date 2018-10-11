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
import pl.ark.chr.buginator.BuginatorProperties;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.repository.auth.UserRepository;
import pl.ark.chr.buginator.service.EmailService;
import pl.ark.chr.buginator.service.UserService;
import pl.ark.chr.buginator.data.Credentials;
import pl.wkr.fluentrule.api.CheckExpectedException;
import pl.wkr.fluentrule.api.FluentExpectedException;
import pl.wkr.fluentrule.api.check.SafeCheck;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2016-11-28.
 */
@RunWith(MockitoJUnitRunner.class)
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

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Rule
    public CheckExpectedException checkThrown = CheckExpectedException.none();

    @Before
    public void setUp() throws Exception {
        when(messageSource.getMessage(any(String.class), nullable(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
        when(buginatorProperties.getBcryptStrength()).thenReturn(11);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testLoadUserByEmail__Success() {
        //given
        String login = "test@gmail.com";

        Role role = new Role();
        role.setId(1L);
        role.setName("Test role");

        User user = new User();
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

        fluentThrown
                .expect(UsernameNotFoundException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN + " " + login);

        //when
        User loadedUser = sut.loadUserByEmail(login, new Locale("en"));

        //then
    }

    @Test
    public void testLoadUserByEmail__UserHasNoRole() {
        //given
        String login = "test@gmail.com";

        User user = new User();
        user.setEmail(login);
        user.setId(1L);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        fluentThrown
                .expect(UsernameNotFoundException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN + " " + login + " " + TEST_MESSAGE_SOURCE_RETURN);

        //when
        User loadedUser = sut.loadUserByEmail(login, new Locale("en"));

        //then
    }

    @Test
    public void testValidateUserLogin__ExpiryDate() throws RestException {
        //given
        LocalDate yesterday = LocalDate.now().minusDays(1L);
        Company company = new Company();
        company.setExpiryDate(yesterday);

        String login = "test@gmail.com";

        Role role = new Role();
        role.setId(1L);
        role.setName("Test role");

        User user = new User();
        user.setEmail(login);
        user.setId(1L);
        user.setRole(role);
        user.setCompany(company);

        Credentials credentials = new Credentials();
        credentials.setUsername(user.getEmail());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        checkThrown.check(new SafeCheck<RestException>() {
            @Override
            protected void safeCheck(RestException e) {
                assertThat(e.getStatus().value()).isEqualTo(403);
                assertThat(e).hasMessage(TEST_MESSAGE_SOURCE_RETURN);
                assertThat(e.getRequestBody()).isInstanceOf(Credentials.class).isEqualTo(credentials);
            }
        });

        //when
        User loaded = sut.validateUserLogin(credentials, new Locale("en"));

        //then
    }

    @Test
    public void testValidateUserLogin__ExpiryDateIsToday() throws RestException {
        //given
        LocalDate today = LocalDate.now();
        Company company = new Company();
        company.setExpiryDate(today);

        String login = "test@gmail.com";

        Role role = new Role();
        role.setId(1L);
        role.setName("Test role");

        User user = new User();
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

    @Test
    public void testSendResetPassword__Success() {
        //given
        String login = "test@gmail.com";
        String oldPassword = "shouldBeChanged";

        User user = new User();
        user.setEmail(login);
        user.setPassword(oldPassword);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

//        doAnswer(invocationOnMock -> {
//            User passedUser = (User) invocationOnMock.getArguments()[0];
//            String generatedPassword = (String) invocationOnMock.getArguments()[2];
//
//            assertThat(passedUser.getEmail()).isEqualTo(login);
//            assertThat(generatedPassword).isNotEqualTo(oldPassword);
//
//            assertThat(BCrypt.checkpw(generatedPassword, passedUser.getPassword())).isTrue();
//
//            return null;
//        }).when(emailService).sendResetPassword(any(User.class), any(Locale.class), any(String.class));

        //when
        sut.resetPassword(login, new Locale("pl"));

        //then
    }
}