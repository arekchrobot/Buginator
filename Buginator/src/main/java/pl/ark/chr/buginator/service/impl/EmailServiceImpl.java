package pl.ark.chr.buginator.service.impl;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Arek on 2016-10-07.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final String PASSWORD_RESET_TEMPLATE = "passwordReset.vm";
    private static final String REGISTER_CONFIRM_TEMPLATE = "registerConfirm.vm";
    private static final String UTF_8 = "UTF-8";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${spring.mail.username}")
    private String noReplyEmailAddress;

    @Override
    @Async
    public void sendResetPassword(User user, Locale locale, String newPassword) {
        Map<String, Object> emailData = new HashMap<>();

        emailData.put("newPassword", newPassword);
        emailData.put("username", user.getName());

        sendMessage(emailData, locale, "passwordReminder.title", user.getEmail(), PASSWORD_RESET_TEMPLATE, "Error sending reset password to user with id: " + user.getId());
    }

    @Override
    @Async
    public void sendRegister(Company company, Locale locale, String email) {
        Map<String, Object> emailData = new HashMap<>();

        emailData.put("token", company.getToken());
        emailData.put("uniqueKey", company.getUniqueKey());
        emailData.put("companyName", company.getName());

        sendMessage(emailData, locale, "registerConfirm.title", email, REGISTER_CONFIRM_TEMPLATE, "Error sending register email for company id: " + company.getId());
    }

    private void sendMessage(Map<String, Object> emailData, Locale locale, String title, String email, String emailTemplate, String errorMsg) {
        if (emailData == null) {
            emailData = new HashMap<>();
        }

        emailData.put("emailBody", messageSource);
        emailData.put("locale", locale);

        String emailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailTemplate, UTF_8, emailData);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, UTF_8);

        try {
            helper.setSubject(messageSource.getMessage(title, null, locale));
            helper.setTo(email);
            helper.setFrom(noReplyEmailAddress);
            helper.setSentDate(new Date());
            helper.setText(emailBody, true);
        } catch(MessagingException e) {
            logger.error(errorMsg  + " with error: " + e.getMessage());
        }

        mailSender.send(message);
    }
}
