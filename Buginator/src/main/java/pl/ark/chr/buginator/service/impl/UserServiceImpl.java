package pl.ark.chr.buginator.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.UserService;

import javax.transaction.Transactional;

/**
 * Created by Arek on 2016-09-29.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByEmail(String login) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(login);

        if (user == null) {
            logger.info("No user found for email: " + login);
            throw new UsernameNotFoundException("No such user: " + login);
        } else if (user.getRole() == null) {
            logger.warn("User: " + login + " has no authorities");
            throw new UsernameNotFoundException("User " + login + " has no authorities");
        }

        return user;
    }
}
