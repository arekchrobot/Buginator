package pl.ark.chr.buginator.domain;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 * Custom id for UserApplication
 */
@Embeddable
public class UserApplicationId implements Serializable, Comparable<UserApplicationId> {

    private static final long serialVersionUID = 3494068464951906271L;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    protected UserApplicationId() {
    }

    public UserApplicationId(User user, Application application) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(application);

        this.user = user;
        this.application = application;
    }

    public User getUser() {
        return user;
    }

    protected void setUser(User user) {
        this.user = user;
    }

    public Application getApplication() {
        return application;
    }

    protected void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, application);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserApplicationId uai = (UserApplicationId) obj;

        if (user != null ? !user.equals(uai.user) : uai.user != null) return false;
        return application != null ? application.equals(uai.application) : uai.application == null;
    }

    @Override
    public int compareTo(UserApplicationId userApplicationId) {
        if (this == userApplicationId) return 0;

        int userCompare = user.compareTo(userApplicationId.getUser());

        if(userCompare == 0) {
            return application.compareTo(userApplicationId.getApplication());
        }

        return userCompare;
    }

    @Override
    public String toString() {
        return "UserApplicationId { " +
                "user = " + user.toString() +
                ", application = " + application.toString() +
                " }";
    }
}
