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
public class Application extends BaseEntity implements FilterData {

    private static final long serialVersionUID = -2010034649811124041L;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.application")
    private Set<UserApplication> applicationUsers = new HashSet<>();

    @Transient
    private Long errorCount;

    @Transient
    private Long lastWeekErrorCount;

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

    @JsonIgnore
    public Set<UserApplication> getApplicationUsers() {
        return applicationUsers;
    }

    public void setApplicationUsers(Set<UserApplication> applicationUsers) {
        this.applicationUsers = applicationUsers;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }

    public Long getLastWeekErrorCount() {
        return lastWeekErrorCount;
    }

    public void setLastWeekErrorCount(Long lastWeekErrorCount) {
        this.lastWeekErrorCount = lastWeekErrorCount;
    }

    @Override
    @Transient
    @JsonIgnore
    public Application getApplication() {
        return this;
    }
}
