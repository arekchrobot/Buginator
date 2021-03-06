package pl.ark.chr.buginator.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.auth.UserApplication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Arek on 2016-09-29.
 */
public class UserWrapper {

    private String username;
    private Collection<String> perms;
    private String role;
    private Company company;
    private Set<UserApplication> userApplications;
    private String email;
    private String token;

    public UserWrapper(User user, String token) {
        this.username = user.getName();
        this.email = user.getEmail();
        Set<String> roles = new HashSet<>();
        roles.add(user.getRole().getAuthority());
        user.getRole().getPermissions().stream().forEach(p -> roles.add(p.getAuthority()));
        this.perms = roles;
        this.role = user.getRole().getName();
        this.company = user.getCompany();
        this.userApplications = user.getUserApplications();
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public Collection<String> getPerms() {
        return perms;
    }

    public String getRole() {
        return role;
    }

    public Company getCompany() {
        return company;
    }

    @JsonIgnore
    public Set<UserApplication> getUserApplications() {
        return userApplications;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
