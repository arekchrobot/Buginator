package pl.ark.chr.buginator.domain;

import javax.persistence.*;

/**
 * Created by Arek on 2016-09-25.
 */
@Entity
@Table(name = "application")
@SequenceGenerator(name = "default_gen", sequenceName = "application_seq", allocationSize = 1)
public class Application extends BaseEntity {

    private static final long serialVersionUID = -2010034649811124041L;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

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
}
