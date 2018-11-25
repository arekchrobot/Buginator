package pl.ark.chr.buginator.aggregator.email;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import pl.ark.chr.buginator.aggregator.service.AbstractAggregatorSender;
import pl.ark.chr.buginator.aggregator.service.AggregatorSender;
import pl.ark.chr.buginator.commons.dto.EmailDTO;
import pl.ark.chr.buginator.commons.util.NetworkUtil;
import pl.ark.chr.buginator.domain.core.Error;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation of AggregatorSender
 * Sends error notification via mail to predefined recipients
 */
@Service
public class EmailAggregatorSender extends AbstractAggregatorSender<EmailAggregator> implements AggregatorSender<EmailAggregator> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAggregatorSender.class);

    private static final String TEMPLATE_PATH = "/errorNotification.html";

    private Configuration freeMarkerConfig;
    private JmsTemplate jmsTemplate;
    private String mailQueue;
    private MessageSource aggregatorEmailMessageSource;

    @Autowired
    public EmailAggregatorSender(Configuration freeMarkerConfig, JmsTemplate jmsTemplate,
                                 @Value("${jms.queue.mailQueue}") String mailQueue,
                                 MessageSource aggregatorEmailMessageSource) {
        this.freeMarkerConfig = freeMarkerConfig;
        this.jmsTemplate = jmsTemplate;
        this.mailQueue = mailQueue;
        this.aggregatorEmailMessageSource = aggregatorEmailMessageSource;
    }

    @Override
    public boolean isValid(String aggregatorClass) {
        return !aggregatorClass.isBlank() && aggregatorClass.equals(EmailAggregator.EMAIL_AGGREGATOR_NAME);
    }

    @Override
    protected void sendData(EmailAggregator aggregator, Error error) {
        var locale = new Locale(aggregator.getLanguage() == null ? "en" : aggregator.getLanguage());

        Map<String, Object> model = createTemplateModel(error, locale);

        LOGGER.info("Sending email notification for error: " + error.getId() + " to recipients: " + aggregator.getRecipients());

        String emailBody;
        try {
            emailBody = constructEmailBody(model);
        } catch (IOException | TemplateException e) {
            LOGGER.error("Error creating email body template for error: " + error.getId());
            throw new EmailCreationException("Error creating email body template for error: " + error.getId(), e);
        }

        EmailDTO emailDTO = constructEmail(aggregator, locale, emailBody);

        jmsTemplate.convertAndSend(mailQueue, emailDTO);
    }

    Map<String, Object> createTemplateModel(Error error, Locale locale) {
        Map<String, Object> model = new HashMap<>();

        model.put("errorTitle", error.getTitle());
        model.put("applicationName", error.getApplication().getName());
        model.put("errorUrl", createUrl(error));
        model.put("topic", aggregatorEmailMessageSource.getMessage("errorNotification.topic", null, locale));
        return model;
    }

    String createUrl(Error error) {
        var sb = new StringBuilder(50)
                .append("http://")
                .append(NetworkUtil.getHostIP());
        if (NetworkUtil.getHostPort() > 0) {
            sb.append(":").append(NetworkUtil.getHostPort());
        }
        sb.append("/application/")
                .append(error.getApplication().getId())
                .append("/error/")
                .append(error.getId());

        return sb.toString();
    }

    String constructEmailBody(Map model) throws IOException, TemplateException {
        var template = freeMarkerConfig.getTemplate(TEMPLATE_PATH);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    EmailDTO constructEmail(EmailAggregator aggregator, Locale locale, String emailBody) {
        return EmailDTO.builder(aggregator.getFrom(), aggregator.getRecipients())
                .emailBody(emailBody)
                .htmlBody(true)
                .cc(aggregator.getCc())
                .bcc(aggregator.getBcc())
                .username(aggregator.getLogin())
                .password(aggregator.getPassword())
                .smtpHost(aggregator.getSmtpHost())
                .smtpPort(aggregator.getSmtpPort())
                .ssl(aggregator.isSsl())
                .subject(aggregatorEmailMessageSource.getMessage("errorNotification.title", null, locale))
                .build();
    }
}
