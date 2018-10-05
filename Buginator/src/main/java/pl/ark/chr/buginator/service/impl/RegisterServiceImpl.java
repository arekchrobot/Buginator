package pl.ark.chr.buginator.service.impl;

//import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import pl.ark.chr.buginator.data.RegisterData;
import pl.ark.chr.buginator.util.UserCompanyValidator;

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
    private UserCompanyValidator userCompanyValidator;

    //TODO: remove after refactoring
    public void setUserCompanyValidator(UserCompanyValidator userCompanyValidator) {
        this.userCompanyValidator = userCompanyValidator;
    }

    @Override
    public void registerUser(RegisterData registerData, Locale locale) throws ValidationException {
        userCompanyValidator.validateCompanyData(registerData.getCompany(), locale);
        userCompanyValidator.validateUserData(registerData.getUser(), locale);

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
        user.setEmail(user.getEmail().toLowerCase());

        userRepository.save(user);

        emailService.sendRegister(company, locale, user.getEmail());
    }

    private LocalDate generateExpiryDate(Integer duration) {
        LocalDate expiryDate = LocalDate.now();
        expiryDate = expiryDate.plusDays(duration);
        return expiryDate;
    }

    private String generateToken() {
        //TODO: fix generating token
        return "NOT IMPLEMENTED TOKEN";
//        return RandomStringUtils.random(PASSWORD_LENGTH, true, true);
    }
}
