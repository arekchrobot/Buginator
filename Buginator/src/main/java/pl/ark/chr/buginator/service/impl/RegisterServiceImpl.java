package pl.ark.chr.buginator.service.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.config.shiro.BCryptPasswordService;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.PaymentOption;
import pl.ark.chr.buginator.domain.Role;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.repository.CompanyRepository;
import pl.ark.chr.buginator.repository.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.RoleRepository;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.EmailService;
import pl.ark.chr.buginator.service.RegisterService;
import pl.ark.chr.buginator.util.RegisterData;
import pl.ark.chr.buginator.util.ValidationUtil;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Created by Arek on 2016-10-21.
 */
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    private static final int PASSWORD_LENGTH = 30;
    private static final String DEFAULT_PAYMENT = "Trial";
    private static final String DEFAULT_ROLE = "Company Manager";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentOptionRepository paymentOptionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordService passwordService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void registerUser(RegisterData registerData, Locale locale) throws ValidationException {
        validateCompanyData(registerData.getCompany(), locale);
        validateUserData(registerData.getUser(), locale);

        Company company = registerData.getCompany();
        company.setToken(generateToken());
        company.setUniqueKey(generateToken());

        PaymentOption defaultPayment = paymentOptionRepository.findByName(DEFAULT_PAYMENT);
        company.setSelectedPaymentOption(defaultPayment);
        company.setExpiryDate(generateExpiryDate(defaultPayment.getDuration()));
        company.setUserLimit(defaultPayment.getMaxUsers());

        company = companyRepository.save(company);

        User user = registerData.getUser();

        Role role = roleRepository.findByName(DEFAULT_ROLE);

        user.setCompany(company);
        user.setRole(role);
        user.setActive(true);
        user.setPassword(passwordService.encryptPassword(user.getPassword()));

        userRepository.save(user);

        emailService.sendRegister(company, locale, user.getEmail());
    }

    private LocalDate generateExpiryDate(Integer duration) {
        LocalDate expiryDate = LocalDate.now();
        expiryDate = expiryDate.plusDays(duration);
        return expiryDate;
    }

    private void validateUserData(User user, Locale locale) throws ValidationException {
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

    private void validateCompanyData(Company company, Locale locale) throws ValidationException {
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

    private String generateToken() {
        return RandomStringUtils.random(PASSWORD_LENGTH, true, true);
    }

    private void validateBlankString(String object, String loggerMsg, String exceptionMsg) throws ValidationException {
        if (ValidationUtil.isBlank(object)) {
            logger.warn(loggerMsg);
            throw new ValidationException(exceptionMsg);
        }
    }
}
