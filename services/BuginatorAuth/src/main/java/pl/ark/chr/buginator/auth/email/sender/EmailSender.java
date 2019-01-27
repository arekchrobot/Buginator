package pl.ark.chr.buginator.auth.email.sender;

import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.User;

public interface EmailSender {

    void composeAndSendEmail(User user, Company company, EmailType emailType);
}
