package pl.ark.chr.buginator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.data.ManageUserData;
import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.filter.ClientFilterFactory;
import pl.ark.chr.buginator.repository.ApplicationRepository;
import pl.ark.chr.buginator.repository.UserApplicationRepository;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.ManageUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-02-04.
 */
@Service
@Transactional
public class ManageUserServiceImpl implements ManageUserService {

    private final ClientFilter clientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS,
            ClientFilterFactory.ClientFilterType.DATA_MODIFY);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ManageUserData> getAllApplicationUsers(Long appId, Set<UserApplication> userApplications) throws DataAccessException {
        return userApplicationRepository.findByPk_Application(validateAccessAndReturnApplication(appId, userApplications))
                .stream()
                .map(ua -> new ManageUserData(ua.getUser(), appId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManageUserData> getAllUsersNotInApplication(Long appId, UserWrapper userWrapper) throws DataAccessException {
        List<User> allCompanyUsers = userRepository.findByCompany(userWrapper.getCompany());

        List<User> usersBoundToApplication = userApplicationRepository
                .findByPk_Application(validateAccessAndReturnApplication(appId, userWrapper.getUserApplications()))
                .stream()
                .map(ua -> ua.getUser())
                .collect(Collectors.toList());

        return allCompanyUsers.stream()
                .filter(user -> !usersBoundToApplication.contains(user))
                .map(user -> new ManageUserData(user, appId))
                .collect(Collectors.toList());
    }

    @Override
    public void addUsersToApplication(List<ManageUserData> usersToAdd, Long appId, Set<UserApplication> userApplications) throws DataAccessException {
        Application application = validateAccessAndReturnApplication(appId, userApplications);

        List<User> users = getAllUsersById(usersToAdd);

        List<UserApplication> permissionsToApp = new ArrayList<>();

        users.forEach(user -> createPermissionForUser(user, application, permissionsToApp, usersToAdd));

        userApplicationRepository.saveAll(permissionsToApp);
    }

    @Override
    public void removeUsersFromApplication(List<ManageUserData> usersToRemove, Long appId, Set<UserApplication> userApplications) throws DataAccessException {
        Application application = validateAccessAndReturnApplication(appId, userApplications);

        List<User> users = getAllUsersById(usersToRemove);

        List<UserApplication> permissionsToApp = new ArrayList<>();

        users.forEach(user -> createPermissionForUser(user, application, permissionsToApp, usersToRemove));

        userApplicationRepository.deleteAll(permissionsToApp);
    }

    @Override
    public void changeAccessToAppForUser(ManageUserData userData, Long appId, Set<UserApplication> userApplications) throws DataAccessException {
        Application application = validateAccessAndReturnApplication(appId, userApplications);

        User user = userRepository.findById(userData.getUserId()).get();

        Optional<UserApplication> ua = userApplicationRepository.findByPk_ApplicationAndPk_User(application, user);

        ua.ifPresent(u -> {
            u.setModify(userData.isModify());
            userApplicationRepository.save(u);
        });
    }

    private List<User> getAllUsersById(List<ManageUserData> usersData) {
        return userRepository.findByIdIn(usersData.stream()
                .map(u -> u.getUserId())
                .collect(Collectors.toList()));
    }

    private void createPermissionForUser(User user, Application application, List<UserApplication> permissionsToApp, List<ManageUserData> usersToAdd) {
        UserApplication ua = new UserApplication();

        ua.setApplication(application);
        ua.setUser(user);
        ua.setModify(usersToAdd.stream()
                .filter(u -> u.getUserId() == user.getId())
                .map(u -> u.isModify())
                .findFirst()
                .get());

        permissionsToApp.add(ua);
    }

    private Application validateAccessAndReturnApplication(Long appId, Set<UserApplication> userApplications) throws DataAccessException {
        Application application = applicationRepository.findById(appId).get();

        clientFilter.validateAccess(application, userApplications);

        return application;
    }
}
