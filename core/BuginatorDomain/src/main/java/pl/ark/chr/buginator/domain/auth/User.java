package pl.ark.chr.buginator.domain.auth;

import pl.ark.chr.buginator.domain.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An user that can log in to portal and perform operations based on role
 */
@Entity
@Table(name = "buginator_user",
        indexes = {
                @Index(name = "email_index", columnList = "email")
        })
public class User extends BaseEntity<User> {

    private static final long serialVersionUID = -2530893894458448440L;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "pass", length = 100, nullable = false)
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

    protected User() {
    }

    private User(Builder builder) {
        setName(builder.name);
        setEmail(builder.email);
        setPassword(builder.password);
        setActive(builder.active);
        setCompany(builder.company);
        setRole(builder.role);
    }

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

    public boolean isActive() {
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


    public static final class Builder {
        private String name;
        private String email;
        private String password;
        private boolean active;
        private Company company;
        private Role role;

        public Builder() {
        }

        public Builder name(String val) {
            Objects.requireNonNull(val);
            name = val;
            return this;
        }

        public Builder email(String val) {
            Objects.requireNonNull(val);
            email = val;
            return this;
        }

        public Builder password(String val) {
            Objects.requireNonNull(val);
            password = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder company(Company val) {
            Objects.requireNonNull(val);
            company = val;
            return this;
        }

        public Builder role(Role val) {
            Objects.requireNonNull(val);
            role = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
