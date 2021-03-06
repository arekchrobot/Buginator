package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.app.exceptions.RestException;
import pl.ark.chr.buginator.app.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.data.Credentials;
import pl.ark.chr.buginator.app.exceptions.ValidationException;

import java.util.List;
import java.util.Locale;

/**
 * Created by Arek on 2016-09-29.
 */
public interface UserService {

    User loadUserByEmail(String login, Locale locale) throws UsernameNotFoundException;

    User validateUserLogin(Credentials credentials, Locale locale) throws RestException;

    List<User> getAllByCompany(Company company);

    User save(User user, Company company) throws DataAccessException, ValidationException;

    void activateDeactivateAccount(String email, Company company, boolean active) throws DataAccessException;
}
