package pl.ark.chr.buginator.domain.aggregator;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.persistence.security.FilterData;

import javax.persistence.*;
import java.util.Objects;

/**
 * Basic class for integrating notification to external platforms.
 * This class is allowed to be extended by specific implementation
 * Inheritance strategy is set to JOINED
 * Should be extended with keeping the contract of BaseEntity
 *
 * @see pl.ark.chr.buginator.domain.BaseEntity
 */
@Entity
@Table(name = "buginator_aggregator",
        uniqueConstraints = {
                @UniqueConstraint(name = "severity_count_application_unique", columnNames = {"error_severity", "application_id", "count"})
        },
        indexes = {
                @Index(name = "application_index", columnList = "application_id")
        })
@Inheritance(strategy = InheritanceType.JOINED)
public class Aggregator<K extends Aggregator<?>> extends BaseEntity<K> implements FilterData {

    private static final long serialVersionUID = -6032934317550549509L;

    @Column(name = "login", length = 100)
    private String login = "";

    @Column(name = "pass", length = 100)
    private String password = "";

    @Column(name = "aggregator_class", length = 100, nullable = false)
    private String aggregatorClass;

    @Enumerated(EnumType.STRING)
    @Column(name = "error_severity")
    private ErrorSeverity errorSeverity;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "count")
    private int count;

    protected Aggregator() {
    }

    /**
     * Main constructor for Aggregator. Should be called in constructors of classes extending this one.
     *
     * @param aggregatorClass Not null aggregatorClass, should be filled automatically by extending class
     *                        This field is used to get aggregator by reflection. Should follow following convention:
     *                        XAggregator where X is the class prefix extending this aggregator
     *                        Example:
     *                        {@code EmailAggregator extends Aggregator}, so aggregatorClass should also be EmailAggregator
     * @param application     Not null application
     */
    public Aggregator(String aggregatorClass, Application application) {
        Objects.requireNonNull(aggregatorClass);
        Objects.requireNonNull(aggregatorClass);

        this.aggregatorClass = aggregatorClass;
        this.application = application;
    }

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

    protected void setAggregatorClass(String aggregatorClass) {
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

    protected void setApplication(Application application) {
        this.application = application;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
