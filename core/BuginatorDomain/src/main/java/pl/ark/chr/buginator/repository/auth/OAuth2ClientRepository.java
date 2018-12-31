package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.auth.OAuth2Client;

import java.util.Optional;

public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, Long> {

    Optional<OAuth2Client> findByClientId(String clientId);
}
