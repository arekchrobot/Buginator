package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.auth.Company;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name);

    Optional<Company> findByTokenAndUniqueKey(String token, String uniqueKey);
}
