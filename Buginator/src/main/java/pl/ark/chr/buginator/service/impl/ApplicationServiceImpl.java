package pl.ark.chr.buginator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.repository.ApplicationRepository;
import pl.ark.chr.buginator.repository.UserApplicationRepository;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.ApplicationService;
import pl.ark.chr.buginator.util.UserWrapper;

import javax.transaction.Transactional;

/**
 * Created by Arek on 2016-12-03.
 */
@Service
@Transactional
public class ApplicationServiceImpl extends CrudServiceImpl<Application> implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected CrudRepository<Application, Long> getRepository() {
        return applicationRepository;
    }

    @Override
    public UserApplication save(Application entity, UserWrapper userWrapper) {
        Application savedApp = super.save(entity);

        User user = userRepository.findByEmail(userWrapper.getEmail()).get();

        UserApplication userApplication = new UserApplication();
        userApplication.setApplication(savedApp);
        userApplication.setUser(user);
        userApplication.setModify(true);

        userApplication = userApplicationRepository.save(userApplication);

        return userApplication;
    }
}
