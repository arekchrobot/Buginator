package pl.ark.chr.buginator.data;

import pl.ark.chr.buginator.domain.User;

/**
 * Created by Arek on 2017-02-04.
 */
public class ManageUserData {

    private long userId;
    private String email;
    private String username;
    private boolean modify;
    private long appId;

    public ManageUserData() {
    }

    public ManageUserData(User user, Long appId) {
        this.appId = appId;
        this.modify = false;
        user.getUserApplications().stream()
                .filter(ua -> ua.getApplication().getId().equals(appId))
                .map(ua -> ua.isModify())
                .findFirst()
                .ifPresent(m -> this.modify = m);
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getName();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }
}
