package pl.ark.chr.buginator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.ark.chr.buginator.domain.filter.FilterData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Arek on 2016-09-25.
 */
@Entity
@Table(name = "application")
@SequenceGenerator(name = "default_gen", sequenceName = "application_seq", allocationSize = 1)
public class Application extends BaseEntity implements FilterData {

    private static final long serialVersionUID = -2010034649811124041L;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.application")
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
    @JsonIgnore
    public Application getApplication() {
        return this;
    }
}
