package pl.ark.chr.buginator.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.ark.chr.buginator.domain.auth.Permission;
import pl.ark.chr.buginator.domain.auth.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OAuth2UserDetails implements UserDetails {

    private User user;

    public OAuth2UserDetails(User user) {
        Objects.requireNonNull(user);
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuths = new HashSet<>();
        for (Permission permission : user.getRole().getPermissions()) {
            grantedAuths.add(new SimpleGrantedAuthority(permission.getAuthority()));
        }
        grantedAuths.add(new SimpleGrantedAuthority(user.getRole().getAuthority()));
        return grantedAuths;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
