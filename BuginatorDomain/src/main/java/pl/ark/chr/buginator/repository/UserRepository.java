package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by Arek on 2016-09-29.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByCompany(Company company);

    List<User> findByIdIn(List<Long> id);
}
