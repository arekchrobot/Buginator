package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Basic representation for JPA Entity in Buginator application used for persistence in database.
 * @param <E> This should be the class that extends BaseEntity (ideally the same class) used for Comparable interface.
 */
@MappedSuperclass
public abstract class BaseEntity<E extends BaseEntity<?>> implements Serializable, Comparable<E> {

    private static final long serialVersionUID = -5161848974240597639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Version
    private long version;

    public BaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public boolean isNew() {
        return null == getId();
    }

    /**
     * Two entities are equal if their IDs are equal
     * Also support only equaling within same type. Is not made for multiple inheritance
     * @param o Object to be comared with
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        if (id == null) return false;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " { " +
                "id = " + id +
                ", version = " + version +
                " }";
    }

    @Override
    public int compareTo(E e) {
        if (this == e) return 0;
        return getId().compareTo(e.getId());
    }
}
