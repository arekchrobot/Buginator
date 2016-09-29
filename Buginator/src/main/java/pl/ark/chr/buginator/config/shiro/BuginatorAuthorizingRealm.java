package pl.ark.chr.buginator.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by Arek on 2016-09-28.
 */
public class BuginatorAuthorizingRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username;
        try {
            username = (String) principalCollection.fromRealm(getName()).iterator().next();
        } catch (NoSuchElementException ex) {
            return null;
        }
//        User user = userDao.findByUsername(username);
//
//        if (user != null) {
//            Set<String> roles = new HashSet<>();
//            Set<String> perms = new HashSet<>();
//            for (Role role : user.getRoles()) {
//                roles.add(role.getAuthority());
//                for (Permission perm : role.getPermissions()) {
//                    perms.add(perm.getAuthority());
//                }
//            }
//            SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo(roles);
//            authInfo.setStringPermissions(perms);
//
//            return authInfo;
//        }

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken authToken = (UsernamePasswordToken) authenticationToken;

//        User user = userDao.findByUsername(authToken.getUsername());
//
//        if (user == null) {
//            throw new UsernameNotFoundException("Account does not exists");
//        }

        return new SimpleAuthenticationInfo(authToken.getUsername(), null, getName());
    }
}
