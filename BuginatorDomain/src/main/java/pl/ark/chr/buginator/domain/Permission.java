package pl.ark.chr.buginator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Arek on 2016-09-25.
 */
@Entity
@Table(name = "buginator_permission")
@SequenceGenerator(name = "default_gen", sequenceName = "buginator_permission_seq", allocationSize = 1)
public class Permission extends BaseEntity implements Authority {

    private static final long serialVersionUID = 5827480917177013654L;

    @Column(name = "name", length = 50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public int hashCode() {
        return getAuthority().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        Authority auth = (Authority) obj;
        return getAuthority().equals(auth.getAuthority());
    }
}
