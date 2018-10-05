package pl.ark.chr.buginator.aggregator.service.impl;

//import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.velocity.VelocityEngineUtils;
import pl.ark.chr.buginator.aggregator.service.AbstractAggregatorService;
import pl.ark.chr.buginator.aggregator.service.AggregatorService;
import pl.ark.chr.buginator.domain.EmailAggregator;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.util.NetworkUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Arek on 2017-03-11.
 */
@Service
@Transactional
public class EmailAggregatorServiceImpl extends AbstractAggregatorService<EmailAggregator> implements AggregatorService<EmailAggregator> {

    private static final Logger logger = LoggerFactory.getLogger(EmailAggregatorServiceImpl.class);

    private static final String ERROR_NOTIFICATION_TEMPLATE = "errorNotification.vm";
    private static final String RECIPIENT_SPLIT_TOKEN = ",";
    private static final String UTF_8 = "UTF-8";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

//    @Autowired
//    private VelocityEngine velocityEngine;

    @Value("${spring.mail.username}")
    private String noReplyEmailAddress;

    @Override
    protected void notifyExternalAggregatorInternal(EmailAggregator aggregator, Error error) {
        Locale locale = new Locale(aggregator.getLanguage());
        Map<String, Object> emailData = new HashMap<>();

        emailData.put("errorTitle", error.getTitle());
        emailData.put("applicationName", error.getApplication().getName());
        emailData.put("errorUrl", createUrl(error));

        String[] emails = aggregator.getRecipients().split(RECIPIENT_SPLIT_TOKEN);

        logger.info("Sending email notification for error: " + error.getId() + " to recipients: " + aggregator.getRecipients());
        sendMessage(emailData, locale, emails, error.getId());
    }

    private String createUrl(Error error) {
        StringBuilder sb = new StringBuilder(50)
                .append("http://")
                .append(NetworkUtil.getHostIP())
                .append(":").append(NetworkUtil.getHostPort())
                .append("/#/application/")
                .append(error.getApplication().getId())
                .append("/error/")
                .append(error.getId());

        return sb.toString();
    }

    private void sendMessage(Map<String, Object> emailData, Locale locale, String[] emails, Long errorId) {
        if (emailData == null) {
            emailData = new HashMap<>();
        }

        emailData.put("emailBody", messageSource);
        emailData.put("locale", locale);

//        String emailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, ERROR_NOTIFICATION_TEMPLATE, UTF_8, emailData);
        //TODO: add another email template + extract API
        String emailBody = "NULL TEMPLATE";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, UTF_8);

        try {
            helper.setSubject(messageSource.getMessage("errorNotification.title", null, locale));
            helper.setTo(emails);
            helper.setFrom(noReplyEmailAddress);
            helper.setSentDate(new Date());
            helper.setText(emailBody, true);
        } catch (MessagingException e) {
            logger.error("Error sending notification to: " + emails + " for error: " + errorId);
            throw new RuntimeException(e);
        }

        mailSender.send(message);
    }
}
