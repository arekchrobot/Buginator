package pl.ark.chr.buginator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.User;

/**
 * Created by Arek on 2016-09-29.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
