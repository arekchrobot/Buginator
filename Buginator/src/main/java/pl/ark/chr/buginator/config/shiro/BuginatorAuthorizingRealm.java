package pl.ark.chr.buginator.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ark.chr.buginator.domain.Permission;
import pl.ark.chr.buginator.domain.Role;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.repository.UserRepository;

import java.util.HashSet;
import java.util.NoSuchElementException;
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
        User user = userRepository.findByEmail(username);

        if (user != null) {
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

        User user = userRepository.findByEmail(authToken.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("Account does not exists");
        }

        return new SimpleAuthenticationInfo(authToken.getUsername(), null, getName());
    }
}
