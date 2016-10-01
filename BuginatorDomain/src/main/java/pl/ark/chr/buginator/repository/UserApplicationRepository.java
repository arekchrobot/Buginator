package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.UserApplication;

/**
 * Created by Arek on 2016-09-29.
 */
public interface UserApplicationRepository extends CrudRepository<UserApplication, Long> {
}
