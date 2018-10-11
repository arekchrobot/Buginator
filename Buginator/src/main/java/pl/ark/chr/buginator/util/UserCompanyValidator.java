package pl.ark.chr.buginator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import java.util.Locale;

/**
 * Created by Arek on 2017-04-02.
 */
@Component
public class UserCompanyValidator {

    private static final Logger logger = LoggerFactory.getLogger(UserCompanyValidator.class);

    private CompanyRepository companyRepository;

    private UserRepository userRepository;

    private MessageSource messageSource;

    @Autowired
    public UserCompanyValidator(CompanyRepository companyRepository, UserRepository userRepository, MessageSource messageSource) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    public void validateCompanyData(Company company, Locale locale) throws ValidationException {
        validateBlankString(company.getName(), "Attempt to create company without name", messageSource.getMessage("validation.companyNameEmpty", null, locale));
        validateBlankString(company.getAddress(), "Attempt to create company without address", messageSource.getMessage("validation.companyAddressEmpty", null, locale));

        companyRepository.findByName(company.getName()).ifPresent(u -> {
            String errorMsg = new StringBuilder(60)
                    .append(messageSource.getMessage("illegalArgument.company.prefix", null, locale))
                    .append(" ")
                    .append(company.getName())
                    .append(" ")
                    .append(messageSource.getMessage("illegalArgument.company.suffix", null, locale))
                    .toString();
            throw new IllegalArgumentException(errorMsg);
        });
    }

    public void validateUserData(User user, Locale locale) throws ValidationException {
        validateBlankString(user.getEmail(), "Attempt to create user without email", messageSource.getMessage("validation.userEmailEmpty", null, locale));
        validateBlankString(user.getPassword(), "Attempt to create user without password", messageSource.getMessage("validation.passwordEmpty", null, locale));
        validateBlankString(user.getName(), "Attempt to create user without name", messageSource.getMessage("validation.usernameEmpty", null, locale));

        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            String errorMsg = new StringBuilder(60)
                    .append(messageSource.getMessage("illegalArgument.user.prefix", null, locale))
                    .append(" ")
                    .append(user.getEmail())
                    .append(" ")
                    .append(messageSource.getMessage("illegalArgument.user.suffix", null, locale))
                    .toString();
            throw new IllegalArgumentException(errorMsg);
        });
    }

    public void validateBlankString(String object, String loggerMsg, String exceptionMsg) throws ValidationException {
        if (ValidationUtil.isBlank(object)) {
            logger.warn(loggerMsg);
            throw new ValidationException(exceptionMsg);
        }
    }
}
