package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.auth.PasswordReset;
import pl.ark.chr.buginator.domain.auth.User;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByToken(String token);

    PasswordReset findFirstByUserOrderByCreatedAtDesc(User user);
}
