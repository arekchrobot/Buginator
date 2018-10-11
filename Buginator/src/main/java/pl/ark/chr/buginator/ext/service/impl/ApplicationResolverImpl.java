package pl.ark.chr.buginator.ext.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.ext.service.ApplicationResolver;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.util.ValidationUtil;

import java.util.Optional;

/**
 * Created by Arek on 2017-04-03.
 */
@Service
@Transactional
public class ApplicationResolverImpl implements ApplicationResolver {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationResolverImpl.class);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public Application resolveApplication(String uniqueKey, String token, String applicationName) {

        Company company = getCompany(uniqueKey, token);

        return getApplication(company, applicationName);
    }

    private Company getCompany(String uniqueKey, String token) {
        validateEmptyString(uniqueKey, "uniqueKey");
        validateEmptyString(token, "token");

        Optional<Company> company = companyRepository.findByTokenAndUniqueKey(token, uniqueKey);

        if (!company.isPresent()) {
            logger.error("No company found for unique key: " + uniqueKey + " and token: " + token);
            throw new IllegalArgumentException("Company not found");
        }

        return company.get();
    }

    private Application getApplication(Company company, String applicationName) {
        validateEmptyString(applicationName, "applicationName");

        Optional<Application> application = applicationRepository.findByNameAndCompany(applicationName, company);

        if (!application.isPresent()) {
            logger.error("No application found for name: " + applicationName + " and company: " + company.getId());
            throw new IllegalArgumentException("Application not found");
        }

        return application.get();
    }

    private void validateEmptyString(String predicate, String key) {
        if (ValidationUtil.isBlank(predicate)) {
            logger.error(key + " is empty");
            throw new IllegalArgumentException(key + " is empty");
        }
    }
}
