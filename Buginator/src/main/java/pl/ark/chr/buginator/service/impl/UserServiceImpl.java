package pl.ark.chr.buginator.service.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.BuginatorProperties;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.EmailService;
import pl.ark.chr.buginator.service.UserService;
import pl.ark.chr.buginator.data.Credentials;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by Arek on 2016-09-29.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int PASSWORD_LENGTH = 15;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BuginatorProperties buginatorProperties;

    @Autowired
    private MessageSource messageSource;

    @Override
    public User loadUserByEmail(String login, Locale locale) throws UsernameNotFoundException {
        Optional<User> userWrapper = userRepository.findByEmail(login.toLowerCase());

        if (!userWrapper.isPresent()) {
            logger.info("No user found for email: " + login);
            throw new UsernameNotFoundException(messageSource.getMessage("usernameNotFound.msg", null, locale) + " " +login);
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
    public void resetPassword(String login, Locale locale) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(login);

        if (!user.isPresent()) {
            logger.info("No user found for email: " + login);
            throw new UsernameNotFoundException(messageSource.getMessage("usernameNotFound.msg", null, locale) + " " + login);
        }

        User u = user.get();
        String newPassword = generatePassword();
        u.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(buginatorProperties.getBcryptStrength())));

        u = userRepository.save(u);

        emailService.sendResetPassword(u, locale, newPassword);
    }

    private String generatePassword() {
        return RandomStringUtils.random(PASSWORD_LENGTH, true, true);
    }
}
