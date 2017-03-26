package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Arek on 2016-09-25.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

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
}
