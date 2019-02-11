package pl.ark.chr.buginator.auth.password.reset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.auth.email.sender.EmailSender;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.commons.exceptions.PasswordTokenExpiredException;
import pl.ark.chr.buginator.commons.exceptions.PasswordTokenNotFoundException;
import pl.ark.chr.buginator.domain.auth.PasswordReset;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.PasswordResetRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
class PasswordResetService {

    private UserRepository userRepository;
    private PasswordResetRepository passwordResetRepository;
    private EmailSender emailSender;
    private PasswordEncoder passwordEncoder;

    private final Long tokenHoursExpiry;

    @Autowired
    public PasswordResetService(UserRepository userRepository,
                                PasswordResetRepository passwordResetRepository,
                                EmailSender emailSender,
                                PasswordEncoder passwordEncoder,
                                @Value("${buginator.password.reset.token.exipry.hours}") Long tokenHoursExpiry) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.tokenHoursExpiry = tokenHoursExpiry;
    }

    void sendPasswordResetEmail(String email) {
        Objects.requireNonNull(email);

        User user = userRepository.findByEmail(email)
                .filter(User::isActive)
                .orElseThrow(() -> new UsernameNotFoundException("user.notExist"));

        var passwordReset = new PasswordReset(user, generateToken());

        passwordResetRepository.save(passwordReset);

        emailSender.composeAndSendEmail(user, user.getCompany(), EmailType.PASSWORD_RESET);
    }

    private String generateToken() {
        String token = UUID.randomUUID().toString();
        return token.replace("-", "");
    }

    void changePassword(PasswordResetDTO passwordResetDTO) {
        Objects.requireNonNull(passwordResetDTO);
        Objects.requireNonNull(passwordResetDTO.getNewPassword());
        Objects.requireNonNull(passwordResetDTO.getToken());

        PasswordReset passwordReset = passwordResetRepository.findByToken(passwordResetDTO.getToken())
                .orElseThrow(() -> new PasswordTokenNotFoundException("user.password.reset.token.not.found"));

        if (passwordResetExpired(passwordReset)) {
            throw new PasswordTokenExpiredException("user.password.reset.token.expired");
        }

        User user = passwordReset.getUser();

        user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
        userRepository.save(user);

        passwordReset.markTokenAsUsed();
        passwordResetRepository.save(passwordReset);
    }

    private boolean passwordResetExpired(PasswordReset passwordReset) {
        Long hoursFromResetRequest = Duration.between(passwordReset.getCreatedAt(), LocalDateTime.now()).toHours();
        return (hoursFromResetRequest >= tokenHoursExpiry) || passwordReset.isTokenUsed();
    }
}
