package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.auth.UserApplicationId;

import java.util.List;
import java.util.Optional;

public interface UserApplicationRepository extends CrudRepository<UserApplication, UserApplicationId> {

    List<UserApplication> findByPk_Application(Application application);

    Optional<UserApplication> findByPk_ApplicationAndPk_User(Application application, User user);
}
