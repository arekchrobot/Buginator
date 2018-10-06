package pl.ark.chr.buginator.domain;

import pl.ark.chr.buginator.domain.filter.FilterData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Application for which the bugs will be stored
 */
@Entity
@Table(name = "application")
public class Application extends BaseEntity<Application> implements FilterData {

    private static final long serialVersionUID = -2010034649811124041L;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.application")
    private Set<UserApplication> applicationUsers = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<UserApplication> getApplicationUsers() {
        return applicationUsers;
    }

    public void setApplicationUsers(Set<UserApplication> applicationUsers) {
        this.applicationUsers = applicationUsers;
    }

    @Override
    @Transient
    public Application getApplication() {
        return this;
    }
}
