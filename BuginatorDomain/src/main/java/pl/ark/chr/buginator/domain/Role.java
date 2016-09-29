package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Arek on 2016-09-25.
 */
@Entity
@Table(name = "buginator_role")
@SequenceGenerator(name = "default_gen", sequenceName = "buginator_role_seq", allocationSize = 1)
public class Role extends BaseEntity implements Authority {

    private static final long serialVersionUID = -160920230822990299L;

    @Column(name = "name", length = 50)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "buginator_role_permission",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
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
        if (obj == null) {
            return false;
        }

        Authority auth = (Authority) obj;
        return getAuthority().equals(auth.getAuthority());
    }
}