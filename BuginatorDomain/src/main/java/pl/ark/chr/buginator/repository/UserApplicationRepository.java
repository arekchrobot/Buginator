package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.domain.UserApplicationId;

import java.util.List;
import java.util.Optional;

/**
 * Created by Arek on 2016-09-29.
 */
public interface UserApplicationRepository extends CrudRepository<UserApplication, UserApplicationId> {

    List<UserApplication> findByPk_Application(Application application);

    Optional<UserApplication> findByPk_ApplicationAndPk_User(Application application, User user);
}
