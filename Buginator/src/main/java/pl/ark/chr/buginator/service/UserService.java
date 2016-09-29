package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.UsernameNotFoundException;

/**
 * Created by Arek on 2016-09-29.
 */
public interface UserService {

    User loadUserByEmail(String login) throws UsernameNotFoundException;
}
