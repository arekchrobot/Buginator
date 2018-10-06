package pl.ark.chr.buginator.domain;

import pl.ark.chr.buginator.domain.enums.ErrorSeverity;
import pl.ark.chr.buginator.domain.filter.FilterData;

import javax.persistence.*;

/**
 * Basic class for integrating notification to external platforms.
 * This class is allowed to be extended by specific implementation
 * Inheritance strategy is set to JOINED
 * Should be extended with keeping the contract of BaseEntity
 *
 * @see pl.ark.chr.buginator.domain.BaseEntity
 */
@Entity
@Table(name = "aggregator",
        uniqueConstraints = {
                @UniqueConstraint(name = "severity_count_application", columnNames = {"error_severity", "application_id", "count"})
        }, indexes = {
                @Index(name = "application_index", columnList = "application_id")
        })
@Inheritance(strategy = InheritanceType.JOINED)
public class Aggregator<K extends Aggregator<?>> extends BaseEntity<K> implements FilterData {

    private static final long serialVersionUID = -6032934317550549509L;

    @Column(name = "login", length = 100)
    private String login = "";

    @Column(name = "pass", length = 100)
    private String password = "";

    @Column(name = "aggregator_class", length = 100)
    private String aggregatorClass = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "error_severity")
    private ErrorSeverity errorSeverity;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "count")
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
