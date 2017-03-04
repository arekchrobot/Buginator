package pl.ark.chr.buginator.data;

import pl.ark.chr.buginator.domain.Notification;

/**
 * Created by Arek on 2017-02-27.
 */
public class NotificationData {

    private Long id;
    private Long errorId;
    private String errorTitle;
    private Long applicationId;

    public NotificationData() {
    }

    public NotificationData(Notification notification) {
        this.id = notification.getId();
        this.errorId = notification.getError().getId();
        this.errorTitle = notification.getError().getTitle();
        this.applicationId = notification.getError().getApplication().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getErrorId() {
        return errorId;
    }

    public void setErrorId(Long errorId) {
        this.errorId = errorId;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
}
