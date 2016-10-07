package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.util.Credentials;

import java.util.Locale;

/**
 * Created by Arek on 2016-09-29.
 */
public interface UserService {

    User loadUserByEmail(String login) throws UsernameNotFoundException;

    User validateUserLogin(Credentials credentials) throws RestException;

    void resetPassword(String login, Locale locale) throws UsernameNotFoundException;
}
