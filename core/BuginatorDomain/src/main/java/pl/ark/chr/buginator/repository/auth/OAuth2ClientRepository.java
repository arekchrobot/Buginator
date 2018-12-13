package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.auth.OAuth2Client;

import java.util.Optional;

public interface OAuth2ClientRepository extends CrudRepository<OAuth2Client, Long> {

    Optional<OAuth2Client> findByClientId(String clientId);
}
