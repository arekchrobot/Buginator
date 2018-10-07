package pl.ark.chr.buginator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Represent single permit in the portal
 */
@Entity
@Table(name = "buginator_permission")
public class Permission extends BaseEntity<Permission> implements Authority {

    private static final long serialVersionUID = 5827480917177013654L;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    protected Permission() {
    }

    public Permission(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
