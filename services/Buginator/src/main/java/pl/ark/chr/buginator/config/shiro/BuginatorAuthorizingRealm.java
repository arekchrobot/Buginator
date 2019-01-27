package pl.ark.chr.buginator.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.repository.auth.UserRepository;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Arek on 2016-09-28.
 */
@Component
public class BuginatorAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username;
        try {
            username = (String) principalCollection.fromRealm(getName()).iterator().next();
        } catch (NoSuchElementException ex) {
            return null;
        }
        Optional<User> userOptional = userRepository.findByEmailAndActiveTrue(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<String> roles = new HashSet<>();
            Set<String> perms = new HashSet<>();
            roles.add(user.getRole().getAuthority());
            user.getRole().getPermissions().stream().forEach(p -> perms.add(p.getAuthority()));
            SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo(roles);
            authInfo.setStringPermissions(perms);

            return authInfo;
        }

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken authToken = (UsernamePasswordToken) authenticationToken;

        Optional<User> user = userRepository.findByEmailAndActiveTrue(authToken.getUsername());

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Account does not exists");
        }

        User loggedUser = user.get();

        return new SimpleAuthenticationInfo(loggedUser.getEmail(), loggedUser.getPassword(), getName());
    }
}