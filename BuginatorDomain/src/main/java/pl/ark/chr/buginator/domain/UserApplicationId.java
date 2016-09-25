package pl.ark.chr.buginator.domain;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Arek on 2016-09-25.
 */
@Embeddable
public class UserApplicationId implements Serializable {

    private static final long serialVersionUID = 3494068464951906271L;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public int hashCode() {
        int result;

        result = (user != null ? user.hashCode() : 0);
        result = 31 * result + (application != null ? application.hashCode() : 0);

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserApplicationId uai = (UserApplicationId) obj;

        if (user != null ? !user.equals(uai.user) : uai.user != null) return false;
        if (application != null ? !application.equals(uai.application) : uai.application != null) return false;

        return true;
    }
}
