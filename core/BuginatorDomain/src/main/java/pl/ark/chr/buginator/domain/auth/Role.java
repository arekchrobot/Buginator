package pl.ark.chr.buginator.domain.auth;

import pl.ark.chr.buginator.domain.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents one role that has given permits in the portal
 * Can be created by companies at free will
 */
@Entity
@Table(name = "buginator_role",
        uniqueConstraints = {
                @UniqueConstraint(name = "companyId_name", columnNames = {"name", "company_id"})
        }
)
public class Role extends BaseEntity<Role> implements Authority {

    private static final long serialVersionUID = -160920230822990299L;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "buginator_role_permission",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
