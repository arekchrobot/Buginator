package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.User;

import java.util.Locale;

/**
 * Created by Arek on 2016-10-07.
 */
public interface EmailService {

    void sendResetPassword(User user, Locale locale, String newPassword);
}
