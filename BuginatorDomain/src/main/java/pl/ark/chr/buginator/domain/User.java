package pl.ark.chr.buginator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Arek on 2016-09-25.
 */
@Entity
@Table(name = "buginator_user")
@SequenceGenerator(name = "default_gen", sequenceName = "buginator_user_seq", allocationSize = 1)
public class User extends BaseEntity {

    private static final long serialVersionUID = -2530893894458448440L;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "pass", length = 100)
    private String password;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.user")
    private Set<UserApplication> userApplications = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "buginator_role_id", nullable = false)
    private Role role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    public Set<UserApplication> getUserApplications() {
        return userApplications;
    }

    public void setUserApplications(Set<UserApplication> userApplications) {
        this.userApplications = userApplications;
    }
}
