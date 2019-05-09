package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.auth.UserApplicationId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserApplicationRepository extends JpaRepository<UserApplication, UserApplicationId> {

    List<UserApplication> findByPk_Application(Application application);

    Optional<UserApplication> findByPk_ApplicationAndPk_User(Application application, User user);

    Stream<UserApplication> findByPk_User_Email(String userEmail);

    Optional<UserApplication> findByPk_User_EmailAndPk_Application_Id(String userEmail, Long id);
}
