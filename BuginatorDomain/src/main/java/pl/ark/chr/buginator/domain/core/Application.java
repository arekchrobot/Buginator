package pl.ark.chr.buginator.domain.core;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Application for which the bugs will be stored
 */
@Entity
@Table(name = "buginator_application")
public class Application extends BaseEntity<Application> implements FilterData {

    private static final long serialVersionUID = -2010034649811124041L;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.application")
    private Set<UserApplication> applicationUsers = new HashSet<>();

    protected Application() {
    }

    public Application(String name, Company company) {
        Objects.requireNonNull(company);
        Objects.requireNonNull(name);

        this.name = name;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    protected void setCompany(Company company) {
        this.company = company;
    }

    public Set<UserApplication> getApplicationUsers() {
        return Set.copyOf(applicationUsers);
    }

    protected void setApplicationUsers(Set<UserApplication> applicationUsers) {
        this.applicationUsers = applicationUsers;
    }

    @Override
    @Transient
    public Application getApplication() {
        return this;
    }
}
