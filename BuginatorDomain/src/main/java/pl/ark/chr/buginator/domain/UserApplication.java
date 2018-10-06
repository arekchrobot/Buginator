package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Mapping between User and Applications classes. Defines the action that user can do in the single application.
 */
@Entity
@Table(name = "user_application")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "pk.application", joinColumns = @JoinColumn(name = "application_id"))
})
public class UserApplication implements Serializable, Comparable<UserApplication> {

    private static final long serialVersionUID = 3322958058050699849L;

    @EmbeddedId
    private UserApplicationId pk = new UserApplicationId();

    @Column(name = "view")
    private Boolean view;

    @Column(name = "modify")
    private Boolean modify;

    public UserApplicationId getPk() {
        return pk;
    }

    public void setPk(UserApplicationId pk) {
        this.pk = pk;
    }

    @Transient
    public User getUser() {
        return getPk().getUser();
    }

    public void setUser(User user) {
        getPk().setUser(user);
    }

    @Transient
    public Application getApplication() {
        return getPk().getApplication();
    }

    public void setApplication(Application application) {
        getPk().setApplication(application);
    }

    public Boolean isView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean isModify() {
        return modify;
    }

    public void setModify(Boolean modify) {
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
