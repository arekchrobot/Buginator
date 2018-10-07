package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * Notifications for user about new errors in applications.
 * This class should be used for notifying user via web platform or mobile app about the new problem
 */
@Entity
@Table(name = "buginator_notification",
        indexes = {
                @Index(name = "user_index", columnList = "user_id"),
                @Index(name = "user_error_index", columnList = "user_id,error_id")
        })
public class Notification extends BaseEntity<Notification> {

    private static final long serialVersionUID = -1058915386956441446L;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "error_id", nullable = false)
    private Error error;

    @Column(name = "seen", nullable = false)
    private boolean seen;

    protected Notification() {
    }

    public Notification(User user, Error error) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(error);

        this.user = user;
        this.error = error;
        this.seen = false;
    }

    public User getUser() {
        return user;
    }

    protected void setUser(User user) {
        this.user = user;
    }

    public Error getError() {
        return error;
    }

    protected void setError(Error error) {
        this.error = error;
    }

    public boolean getSeen() {
        return seen;
    }

    protected void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void markSeen() {
        this.seen = true;
    }
}
