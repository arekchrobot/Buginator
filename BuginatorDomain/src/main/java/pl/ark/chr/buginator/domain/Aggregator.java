package pl.ark.chr.buginator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.ark.chr.buginator.domain.enums.ErrorSeverity;

import javax.persistence.*;

/**
 * Created by Arek on 2016-09-26.
 */
@Entity
@Table(name = "aggregator")
@SequenceGenerator(name = "default_gen", sequenceName = "aggregator_seq", allocationSize = 1)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Aggregator extends BaseEntity {

    @Column(name = "login", length = 100)
    private String login;

    @Column(name = "pass", length = 100)
    private String password;

    @Column(name = "aggregator_class", length = 100)
    @JsonIgnore
    private String aggregatorClass;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "error_severity")
    private ErrorSeverity errorSeverity;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "count")
    private Integer count;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAggregatorClass() {
        return aggregatorClass;
    }

    public void setAggregatorClass(String aggregatorClass) {
        this.aggregatorClass = aggregatorClass;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ErrorSeverity getErrorSeverity() {
        return errorSeverity;
    }

    public void setErrorSeverity(ErrorSeverity errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
