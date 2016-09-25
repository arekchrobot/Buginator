package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Arek on 2016-09-25.
 */
@Entity
@Table(name = "user_application")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "pk.application", joinColumns = @JoinColumn(name = "application_id"))
})
public class UserApplication implements Serializable {

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

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getModify() {
        return modify;
    }

    public void setModify(Boolean modify) {
        this.modify = modify;
    }
}
