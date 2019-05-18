package pl.ark.chr.buginator.app.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.app.error.ErrorService;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;
import pl.ark.chr.buginator.app.core.security.AbstractApplicationAccessRestricted;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.app.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationService extends AbstractApplicationAccessRestricted<Application> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);

    private static final int NUMBER_OF_DAYS = 7;

    private ApplicationRepository applicationRepository;
    private ErrorService errorService;
    private CompanyRepository companyRepository;

    @Autowired
    public ApplicationService(LoggedUserService loggedUserService, UserApplicationService userApplicationService,
                              ApplicationRepository applicationRepository, CompanyRepository companyRepository,
                              ErrorService errorService) {
        super(loggedUserService, userApplicationService);
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
        this.errorService = errorService;
    }

    @CacheEvict(value = "applications", allEntries = true)
    public UserApplicationDTO create(ApplicationRequestDTO applicationRequestDTO) {
        Objects.requireNonNull(applicationRequestDTO);

        LoggedUserDTO currentUser = loggedUserService.getCurrentUser();
        Company company = companyRepository.findById(currentUser.getCompanyId())
                .orElseThrow(() -> new DataNotFoundException("company.notFound"));

        validateDuplicate(applicationRequestDTO.getName(), company);

        Application application = new Application(applicationRequestDTO.getName(), company);
        applicationRepository.save(application);

        return userApplicationService.linkApplicationToUser(application, currentUser);
    }

    private void validateDuplicate(String appName, Company company) {
        LOGGER.info("Validating duplicate application with name: " + appName + " for company: " + company.getName());

        applicationRepository.findByNameAndCompany(appName, company)
                .ifPresent(a -> {
                    LOGGER.warn("Duplicated application with name: "
                            + appName + " for company: " + company.getName());
                    throw new DuplicateException("application.duplicate");
                });
    }

    /**
     * Used in spring SpEL to getForUser current user key for cached applications.
     * For method: {@link #getUserApps() getUserApps}
     *
     * @return Current logged user email
     */
    public String getUserAppCacheKey() {
        return loggedUserService.getCurrentUser().getEmail();
    }

    @Cacheable(value = "applications", key = "#root.target.getUserAppCacheKey()")
    public Set<ApplicationDTO> getUserApps() {
        LocalDate lastWeek = LocalDate.now().minusDays(NUMBER_OF_DAYS);
        LoggedUserDTO currentUser = loggedUserService.getCurrentUser();

        return userApplicationService.getAllForUser(currentUser.getEmail())
                .stream()
                .map(userApplication -> ApplicationDTO.builder(userApplication)
                        .allErrorCount(errorService.countByApplication(userApplication))
                        .lastWeekErrorCount(errorService.countSince(userApplication, lastWeek))
                        .build())
                .collect(Collectors.toSet());
    }

    public ApplicationDetailsDTO get(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("application.notFound"));

        readAccessAllowed(application);

        LoggedUserDTO currentUser = loggedUserService.getCurrentUser();
        UserApplicationDTO userApplication = userApplicationService.getForUser(currentUser.getEmail(), id);

        return ApplicationDetailsDTO.builder(userApplication)
                .errors(errorService.getAllByApplication(application))
                .build();
    }
}
