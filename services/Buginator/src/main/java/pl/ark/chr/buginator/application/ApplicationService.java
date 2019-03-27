package pl.ark.chr.buginator.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;
import pl.ark.chr.buginator.core.security.AbstractApplicationAccessRestricted;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;

import java.util.Set;

@Service
public class ApplicationService extends AbstractApplicationAccessRestricted<Application> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);

    private ApplicationRepository applicationRepository;
    private CompanyRepository companyRepository;

    @Autowired
    public ApplicationService(LoggedUserService loggedUserService, UserApplicationService userApplicationService,
                              ApplicationRepository applicationRepository, CompanyRepository companyRepository) {
        super(loggedUserService, userApplicationService);
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
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

    @Cacheable(value = "applications", key = "#root.target.loggedUserService.getCurrentUser().getEmail()")
    public Set<ApplicationDTO> getUserApplications() {
        return null;
    }
}
