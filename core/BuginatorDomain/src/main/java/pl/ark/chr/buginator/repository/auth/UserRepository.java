package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndActiveTrue(String email);

    List<User> findByCompany(Company company);

    List<User> findByIdIn(List<Long> id);

    List<User> findByRole(Role role);
}
