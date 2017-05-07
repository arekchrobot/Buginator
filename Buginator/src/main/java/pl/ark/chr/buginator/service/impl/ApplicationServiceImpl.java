package pl.ark.chr.buginator.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.filter.ClientFilterFactory;
import pl.ark.chr.buginator.repository.ApplicationRepository;
import pl.ark.chr.buginator.repository.ErrorRepository;
import pl.ark.chr.buginator.repository.UserApplicationRepository;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.ApplicationService;
import pl.ark.chr.buginator.data.UserWrapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2016-12-03.
 */
@Service
@Transactional
public class ApplicationServiceImpl extends RestrictedAccessCrudServiceImpl<Application> implements ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ClientFilter readClientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS);
    private final ClientFilter writeClientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS,
            ClientFilterFactory.ClientFilterType.DATA_MODIFY);

    private static final int NUMBER_OF_DAYS = 7;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    protected CrudRepository<Application, Long> getRepository() {
        return applicationRepository;
    }

    @Override
    protected ClientFilter getReadClientFilter() {
        return readClientFilter;
    }

    @Override
    protected ClientFilter getWriteClientFilter() {
        return writeClientFilter;
    }

    @Override
    @CacheEvict(value = "applications", allEntries = true)
    public UserApplication save(Application entity, UserWrapper userWrapper) throws DataAccessException {
        validateDuplicate(entity, userWrapper.getCompany());
        entity.setCompany(userWrapper.getCompany());
        Application savedApp = super.save(entity, userWrapper.getUserApplications());

        User user = userRepository.findByEmail(userWrapper.getEmail()).get();

        UserApplication userApplication = new UserApplication();
        userApplication.setApplication(savedApp);
        userApplication.setUser(user);
        userApplication.setModify(true);

        userApplication = userApplicationRepository.save(userApplication);

        return userApplication;
    }

    private void validateDuplicate(Application application, Company company) {
        logger.info("Validating duplicate application with name: " + application.getName() + " for company: " + company.getName());

        Locale locale = LocaleContextHolder.getLocale();
        applicationRepository.findByNameAndCompany(application.getName(), company)
                .ifPresent(a -> {
                    String errorMsg = new StringBuilder(60)
                            .append(messageSource.getMessage("illegalArgument.applicationDuplicate.prefix", null, locale))
                            .append(" ")
                            .append(application.getName())
                            .append(" ")
                            .append(messageSource.getMessage("illegalArgument.applicationDuplicate.suffix", null, locale))
                            .toString();
                    throw new IllegalArgumentException(errorMsg);
                });
    }

    @Override
    @Cacheable(value = "applications", key = "#user.email")
    public List<Application> getUserApplications(UserWrapper user) {
        LocalDate lastWeek = LocalDate.now().minusDays(NUMBER_OF_DAYS);
        return user.getUserApplications().stream()
                .map(ua -> countErrors(ua.getApplication(), lastWeek))
                .collect(Collectors.toList());
    }

    private Application countErrors(Application application, LocalDate lastWeek) {
        application.setErrorCount(errorRepository.countByApplication(application));
        application.setLastWeekErrorCount(errorRepository.countByApplicationAndLastOccurrenceGreaterThanEqual(application, lastWeek));
        return application;
    }

    @Override
    public List<Application> getAllByApplication(Long appId, Set<UserApplication> userApplications) throws DataAccessException {
        throw new UnsupportedOperationException("Method unsupported. Please use: getUserApplications(UserWrapper user)");
    }
}
