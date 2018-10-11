package pl.ark.chr.buginator.ext.service;

import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;

/**
 * Created by Arek on 2017-05-04.
 */
public interface NotificationSender {

    void createAndSendNotifications(Error error, Application application);
}
