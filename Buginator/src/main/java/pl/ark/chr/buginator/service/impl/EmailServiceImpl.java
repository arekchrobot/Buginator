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

        emailData.put("emailBody", messageSource);
        emailData.put("locale", locale);
        emailData.put("newPassword", newPassword);

        String emailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, PASSWORD_RESET_TEMPLATE, UTF_8, emailData);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setSubject(messageSource.getMessage("passwordReminder.title", null, locale));
            helper.setTo(user.getEmail());
            helper.setFrom(noReplyEmailAddress);
            helper.setSentDate(new Date());
            helper.setText(emailBody, true);
        } catch(MessagingException e) {
            logger.error("Error sending reset password to user with id: " + user.getId() + " with error: " + e.getMessage());
        }

        mailSender.send(message);
    }
}
