package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * An user thta can log in to portal and perform operations based on role
 */
@Entity
@Table(name = "buginator_user")
public class User extends BaseEntity<User> {

    private static final long serialVersionUID = -2530893894458448440L;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "pass", length = 100)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user")
    private Set<UserApplication> userApplications = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
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

    public Set<UserApplication> getUserApplications() {
        return Set.copyOf(userApplications);
    }

    protected void setUserApplications(Set<UserApplication> userApplications) {
        this.userApplications = userApplications;
    }
}
