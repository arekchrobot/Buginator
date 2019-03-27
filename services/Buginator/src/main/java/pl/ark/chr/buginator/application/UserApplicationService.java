package pl.ark.chr.buginator.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.repository.auth.UserApplicationRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import java.util.Objects;
import java.util.Set;

@Service
public class UserApplicationService {

    private UserApplicationRepository userApplicationRepository;
    private UserRepository userRepository;

    @Autowired
    public UserApplicationService(UserApplicationRepository userApplicationRepository, UserRepository userRepository) {
        this.userApplicationRepository = userApplicationRepository;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "userApplications", key = "#email")
    public Set<UserApplication> getCachedUserApplications(String email) {
        Objects.requireNonNull(email);

        return userApplicationRepository.findByPk_User_Email(email);
    }

    UserApplicationDTO linkApplicationToUser(Application application, LoggedUserDTO currentUser) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new DataNotFoundException("user.notFound"));

        UserApplication userApplication = new UserApplication(user, application);
        userApplication.setModify(true);

        userApplicationRepository.save(userApplication);

        return new UserApplicationDTO();
    }
}
