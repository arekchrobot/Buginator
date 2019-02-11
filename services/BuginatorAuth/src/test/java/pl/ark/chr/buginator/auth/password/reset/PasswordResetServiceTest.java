package pl.ark.chr.buginator.auth.password.reset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ark.chr.buginator.auth.email.sender.EmailSender;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.auth.util.TestObjectCreator;
import pl.ark.chr.buginator.commons.exceptions.PasswordTokenExpiredException;
import pl.ark.chr.buginator.commons.exceptions.PasswordTokenNotFoundException;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PasswordReset;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.PasswordResetRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    private PasswordResetService passwordResetService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordResetRepository passwordResetRepository;
    @Mock
    private EmailSender emailSender;

    private PasswordEncoder passwordEncoder = TestObjectCreator.passwordEncoder();

    private final Long tokenHoursExpiry = 24L;

    @BeforeEach
    public void setUp() {
        passwordResetService = new PasswordResetService(userRepository, passwordResetRepository, emailSender,
                passwordEncoder, tokenHoursExpiry);
    }

    @Test
    @DisplayName("should create password reset and send password reset email")
    public void shouldCreateAndSendPasswordReset() {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("TEST_PAYMENT"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER));

        doReturn(Optional.of(user)).when(userRepository).findByEmail(eq(user.getEmail()));

        doAnswer(invocationOnMock -> {
            PasswordReset passwordReset = invocationOnMock.getArgument(0);

            assertThat(passwordReset.getUser()).isEqualTo(user);
            assertThat(passwordReset.isTokenUsed()).isFalse();
            assertThat(passwordReset.getToken()).isNotEmpty();

            return passwordReset;
        }).when(passwordResetRepository).save(any(PasswordReset.class));

        //when
        passwordResetService.sendPasswordResetEmail(user.getEmail());

        //then
        verify(emailSender, times(1)).composeAndSendEmail(eq(user), eq(company), eq(EmailType.PASSWORD_RESET));
        verify(passwordResetRepository, times(1)).save(any(PasswordReset.class));
        verify(userRepository, times(1)).findByEmail(eq(user.getEmail()));
    }

    @Test
    @DisplayName("should throw UsernameNotFoundException when user is not active")
    public void shouldThrowUsernameNotFoundExceptionWhenUserIsNotActive() {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("TEST_PAYMENT"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER), false);

        doReturn(Optional.of(user)).when(userRepository).findByEmail(eq(user.getEmail()));

        //when
        Executable codeUnderException = () -> passwordResetService.sendPasswordResetEmail(user.getEmail());

        //then
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, codeUnderException,
                "Should throw UsernameNotFoundException");
        assertThat(ex.getMessage()).isEqualTo("user.notExist");
        verify(emailSender, never()).composeAndSendEmail(eq(user), eq(company), eq(EmailType.PASSWORD_RESET));
        verify(passwordResetRepository, never()).save(any(PasswordReset.class));
        verify(userRepository, times(1)).findByEmail(eq(user.getEmail()));
    }

    @Test
    @DisplayName("should throw UsernameNotFoundException when no user found")
    public void shouldThrowUsernameNotFoundExceptionWhenNoUserFound() {
        //given
        doReturn(Optional.empty()).when(userRepository).findByEmail(any(String.class));

        //when
        Executable codeUnderException = () -> passwordResetService.sendPasswordResetEmail("notExist");

        //then
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, codeUnderException,
                "Should throw UsernameNotFoundException");
        assertThat(ex.getMessage()).isEqualTo("user.notExist");
        verify(emailSender, never()).composeAndSendEmail(any(User.class), any(Company.class), eq(EmailType.PASSWORD_RESET));
        verify(passwordResetRepository, never()).save(any(PasswordReset.class));
        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    @DisplayName("should change user password")
    public void shouldChangeUserPassword() {
        //given
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("TEST_PAYMENT"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER), false);

        String token = "TEST_TOKEN";
        var passwordReset = new PasswordReset(user, token, LocalDateTime.now());

        doReturn(Optional.of(passwordReset)).when(passwordResetRepository).findByToken(eq(token));

        var passwordResetDTO = new PasswordResetDTO(token, "newPass123");

        doAnswer(invocationOnMock -> {
            User changedPassUser = invocationOnMock.getArgument(0);

            assertThat(user.getPassword()).isEqualTo("{def}" + passwordResetDTO.getNewPassword());

            return user;
        }).when(userRepository).save(eq(user));

        doAnswer(invocationOnMock -> {
            PasswordReset usedToken = invocationOnMock.getArgument(0);

            assertThat(usedToken.isTokenUsed()).isTrue();

            return usedToken;
        }).when(passwordResetRepository).save(eq(passwordReset));

        //when
        passwordResetService.changePassword(passwordResetDTO);

        //then
        verify(passwordResetRepository, times(1)).findByToken(eq(token));
        verify(passwordResetRepository, times(1)).save(eq(passwordReset));
        verify(userRepository, times(1)).save(eq(user));
    }

    @Test
    @DisplayName("should throw PasswordTokenNotFoundException when no password reset found for given token")
    public void shouldThrowPasswordTokenNotFoundExceptionWhenNoPasswordResetFound() {
        //given
        String token = "NOT_EXISTING_TOKEN";
        doReturn(Optional.empty()).when(passwordResetRepository).findByToken(eq(token));

        var passwordResetDTO = new PasswordResetDTO(token, "newPass123");

        //when
        Executable codeUnderException = () -> passwordResetService.changePassword(passwordResetDTO);

        //then
        PasswordTokenNotFoundException ex = assertThrows(PasswordTokenNotFoundException.class, codeUnderException,
                "Should throw PasswordTokenNotFoundException");
        assertThat(ex.getMessage()).isEqualTo("user.password.reset.token.not.found");
        verify(passwordResetRepository, times(1)).findByToken(eq(token));
        verify(passwordResetRepository, never()).save(any(PasswordReset.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @ParameterizedTest(name = "Condition: {1}")
    @MethodSource("changePasswordProvider")
    @DisplayName("should throw PasswordTokenExpiredException when one of condition is not met")
    public void shouldThrowPasswordTokenExpiredExceptionWhenConditionNotMet(PasswordReset passwordReset, String condition) {
        //given
        String token = "123";
        doReturn(Optional.of(passwordReset)).when(passwordResetRepository).findByToken(eq(token));

        var passwordResetDTO = new PasswordResetDTO(token, "newPass123");

        //when
        Executable codeUnderException = () -> passwordResetService.changePassword(passwordResetDTO);

        //then
        PasswordTokenExpiredException ex = assertThrows(PasswordTokenExpiredException.class, codeUnderException,
                "Should throw PasswordTokenNotFoundException");
        assertThat(ex.getMessage()).isEqualTo("user.password.reset.token.expired");
        verify(passwordResetRepository, times(1)).findByToken(eq(token));
        verify(passwordResetRepository, never()).save(any(PasswordReset.class));
        verify(userRepository, never()).save(any(User.class));
    }

    private static Stream<Arguments> changePasswordProvider() {
        var company = TestObjectCreator.createCompany(TestObjectCreator.createPaymentOption("TEST_PAYMENT"));
        var user = TestObjectCreator.createUser(company, Role.getRole(Role.MANAGER), false);

        var passwordResetFalse = new PasswordReset(user, "123");
        passwordResetFalse.markTokenAsUsed();
        return Stream.of(
                Arguments.of(passwordResetFalse, "Token is already used"),
                Arguments.of(new PasswordReset(user, "123", LocalDateTime.now().minusDays(2)), "Token is expired")
        );
    }
}