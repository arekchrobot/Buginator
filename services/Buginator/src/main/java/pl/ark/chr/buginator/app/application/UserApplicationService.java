package pl.ark.chr.buginator.app.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.app.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.repository.auth.UserApplicationRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Transactional(readOnly = true)
    public Set<UserApplicationDTO> getAllForUser(String email) {
        Objects.requireNonNull(email);

        try (Stream<UserApplication> userApplications = userApplicationRepository.findByPk_User_Email(email)) {
            return userApplications
                    .map(ua -> UserApplicationDTO.builder()
                            .id(ua.getApplication().getId())
                            .name(ua.getApplication().getName())
                            .modify(ua.isModify())
                            .build())
                    .collect(Collectors.toSet());
        }
    }

    @CacheEvict(value = "userApplications", key = "#currentUser.email")
    public UserApplicationDTO linkApplicationToUser(Application application, LoggedUserDTO currentUser) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new DataNotFoundException("user.notFound"));

        UserApplication userApplication = new UserApplication(user, application);
        userApplication.setModify(true);

        userApplicationRepository.save(userApplication);

        return UserApplicationDTO.builder()
                .id(application.getId())
                .name(application.getName())
                .modify(userApplication.isModify())
                .build();
    }

    @Cacheable(value = "userApplications", key = "#email+'_'+#appId")
    public UserApplicationDTO getForUser(String email, Long appId) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(appId);

        UserApplication userApplication = userApplicationRepository.findByPk_User_EmailAndPk_Application_Id(email, appId)
                .orElseThrow(() -> new DataNotFoundException("userApplication.notFound"));

        return UserApplicationDTO.builder()
                .id(appId)
                .name(userApplication.getApplication().getName())
                .modify(userApplication.isModify())
                .build();
    }
}
