package pl.ark.chr.buginator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
}
