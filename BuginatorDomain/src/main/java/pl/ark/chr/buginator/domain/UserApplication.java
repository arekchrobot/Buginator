package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Mapping between User and Applications classes. Defines the action that user can do in the single application.
 */
@Entity
@Table(name = "buginator_user_application")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "pk.application", joinColumns = @JoinColumn(name = "application_id"))
})
public class UserApplication implements Serializable, Comparable<UserApplication> {

    private static final long serialVersionUID = 3322958058050699849L;

    @EmbeddedId
    private UserApplicationId pk = new UserApplicationId();

    @Column(name = "view")
    private boolean view;

    @Column(name = "modify")
    private boolean modify;

    protected UserApplication() {
    }

    public UserApplication(User user, Application application) {
        pk = new UserApplicationId(user, application);
    }

    public UserApplicationId getPk() {
        return pk;
    }

    protected void setPk(UserApplicationId pk) {
        this.pk = pk;
    }

    @Transient
    public User getUser() {
        return getPk().getUser();
    }

    @Transient
    public Application getApplication() {
        return getPk().getApplication();
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    @Override
    public int hashCode() {
        return pk.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserApplication ua = (UserApplication) obj;
        return getPk().equals(ua.getPk());
    }

    @Override
    public int compareTo(UserApplication userApplication) {
        return getPk().compareTo(userApplication.getPk());
    }

    @Override
    public String toString() {
        return "UserApplication { " +
                "pk = " + pk.toString() +
                ", view = " + view +
                ", modify = " + modify +
                '}';
    }
}
