package pl.ark.chr.buginator.domain;

import javax.persistence.*;

/**
 * Created by Arek on 2016-09-26.
 */
@Entity
@Table(name = "buginator_notification")
public class Notification extends BaseEntity {

    private static final long serialVersionUID = -1058915386956441446L;

    @ManyToOne
    @JoinColumn(name = "buginator_user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "buginator_error_id", nullable = false)
    private Error error;

    @Column(name = "seen")
    private Boolean seen;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
