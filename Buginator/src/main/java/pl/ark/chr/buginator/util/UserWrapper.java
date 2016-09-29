package pl.ark.chr.buginator.util;

import pl.ark.chr.buginator.domain.Authority;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.domain.UserApplication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2016-09-29.
 */
public class UserWrapper {

    private String username;
    private Collection<String> perms;
    private Company company;
    private Set<UserApplication> userApplications;

    public UserWrapper(User user) {
        this.username = user.getName();
        Set<String> roles = new HashSet<>();
        roles.add(user.getRole().getAuthority());
        user.getRole().getPermissions().stream().forEach(p -> roles.add(p.getAuthority()));
        this.perms = roles;
        this.company = user.getCompany();
        this.userApplications = user.getUserApplications();
    }

    public String getUsername() {
        return username;
    }

    public Collection<String> getPerms() {
        return perms;
    }

    public Company getCompany() {
        return company;
    }

    public Set<UserApplication> getUserApplications() {
        return userApplications;
    }
}
