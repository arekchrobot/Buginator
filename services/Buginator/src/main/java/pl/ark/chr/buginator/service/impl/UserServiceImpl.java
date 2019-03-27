package pl.ark.chr.buginator.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.app.exceptions.RestException;
import pl.ark.chr.buginator.app.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.app.exceptions.ValidationException;
import pl.ark.chr.buginator.repository.auth.UserRepository;
import pl.ark.chr.buginator.service.UserService;
import pl.ark.chr.buginator.data.Credentials;
import pl.ark.chr.buginator.util.UserCompanyValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by Arek on 2016-09-29.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserCompanyValidator userCompanyValidator;

    @Override
    public User loadUserByEmail(String login, Locale locale) throws UsernameNotFoundException {
        Optional<User> userWrapper = userRepository.findByEmail(login.toLowerCase());

        if (!userWrapper.isPresent()) {
            logger.info("No user found for email: " + login);
            throw new UsernameNotFoundException(messageSource.getMessage("usernameNotFound.msg", null, locale) + " " + login);
        } else if (userWrapper.get().getRole() == null) {
            logger.warn("User: " + login + " has no authorities");
            String errorMsg = new StringBuilder(60)
                    .append(messageSource.getMessage("usernameNotFound.prefix", null, locale))
                    .append(" ")
                    .append(login)
                    .append(" ")
                    .append(messageSource.getMessage("usernameNotFound.suffix", null, locale))
                    .toString();
            throw new UsernameNotFoundException(errorMsg);
        }

        return userWrapper.get();
    }

    @Override
    public User validateUserLogin(Credentials credentials, Locale locale) throws RestException {
        User user = loadUserByEmail(credentials.getUsername(), locale);

        LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(user.getCompany().getExpiryDate())) {
            logger.warn("User with id:" + user.getId() + " is inactive due to company with id: " + user.getCompany().getId() + " hasn't payed for subscription.");
            throw new RestException(messageSource.getMessage("accountExpired.msg", null, locale), HttpStatus.FORBIDDEN, "/auth/login", credentials);
        }

        return user;
    }

    @Override
    @Cacheable(value = "users", key = "#company.id")
    public List<User> getAllByCompany(Company company) {
        return userRepository.findByCompany(company);
    }

    @Override
    @CacheEvict(value = "users", key = "#company.id")
    public User save(User user, Company company) throws DataAccessException, ValidationException {
//        if (user.isNew()) {
//            userCompanyValidator.validateUserData(user, LocaleContextHolder.getLocale());
//            user.setCompany(company);
//            user.setActive(true);
//        } else {
//            validateAccess(user, company);
//        }
//
//        return userRepository.save(user);
        return null;
    }

    private void validateAccess(User user, Company company) throws DataAccessException {
        if (!user.getCompany().getId().equals(company.getId())) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new DataAccessException(messageSource.getMessage("userModify.forbidden", null, locale), null);
        }
    }

    @Override
    @CacheEvict(value = "users", key = "#company.id")
    public void activateDeactivateAccount(String email, Company company, boolean active) throws DataAccessException {
        User user = loadUserByEmail(email, LocaleContextHolder.getLocale());

        validateAccess(user, company);

        user.setActive(active);

        userRepository.save(user);
    }
}
