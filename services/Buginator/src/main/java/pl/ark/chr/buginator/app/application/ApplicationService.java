package pl.ark.chr.buginator.app.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;
import pl.ark.chr.buginator.app.core.security.AbstractApplicationAccessRestricted;
import pl.ark.chr.buginator.commons.util.Pair;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.app.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.repository.core.ErrorRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationService extends AbstractApplicationAccessRestricted<Application> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);

    private static final int NUMBER_OF_DAYS = 7;

    private ApplicationRepository applicationRepository;
    //TODO: replace by services!!!
    private CompanyRepository companyRepository;
    private ErrorRepository errorRepository;

    @Autowired
    public ApplicationService(LoggedUserService loggedUserService, UserApplicationService userApplicationService,
                              ApplicationRepository applicationRepository, CompanyRepository companyRepository,
                              ErrorRepository errorRepository) {
        super(loggedUserService, userApplicationService);
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
        this.errorRepository = errorRepository;
    }

    @CacheEvict(value = "applications", allEntries = true)
    public UserApplicationDTO createApplication(ApplicationRequestDTO applicationRequestDTO) {
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

    @Cacheable(value = "applications", key = "#root.target.loggedUserService.getCurrentUser().email")
    public Set<ApplicationDTO> getUserApplications() {
        LocalDate lastWeek = LocalDate.now().minusDays(NUMBER_OF_DAYS);
        LoggedUserDTO currentUser = loggedUserService.getCurrentUser();

        return userApplicationService.getCachedUserApplications(currentUser.getEmail())
                .stream()
                .map(userApplication -> new Pair<>(userApplication.getApplication(), userApplication.isModify()))
                .map(pair -> ApplicationDTO.builder(pair._1.getName(), pair._2)
                        .allErrorCount(errorRepository.countByApplication(pair._1))
                        .lastWeekErrorCount(errorRepository.countByApplicationAndLastOccurrenceGreaterThanEqual(pair._1, lastWeek))
                        .build())
                .collect(Collectors.toSet());
    }
}
