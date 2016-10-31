package pl.ark.chr.buginator.service.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    public void registerUser(RegisterData registerData, Locale locale) throws ValidationException {
        validateCompanyData(registerData.getCompany());
        validateUserData(registerData.getUser());

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

        userRepository.save(user);

        emailService.sendRegister(company, locale, user.getEmail());
    }

    private LocalDate generateExpiryDate(Integer duration) {
        LocalDate expiryDate = LocalDate.now();
        expiryDate.plusDays(duration);
        return expiryDate;
    }

    private void validateUserData(User user) throws ValidationException {
        validateBlankString(user.getEmail(), "Attempt to create user without email", "User email cannot be empty");
        validateBlankString(user.getPassword(), "Attempt to create user without password", "Password cannot be empty");
        validateBlankString(user.getName(), "Attempt to create user without name", "Username cannot be empty");

        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("User with email: " + user.getEmail() + " already exists");
        });
    }

    private void validateCompanyData(Company company) throws ValidationException {
        validateBlankString(company.getName(), "Attempt to create company without name", "Company name cannot be empty");
        validateBlankString(company.getAddress(), "Attempt to create company without address", "Company address cannot be empty");

        companyRepository.findByName(company.getName()).ifPresent(u -> {
            throw new IllegalArgumentException("Company with name: " + company.getName() + " already exists");
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
