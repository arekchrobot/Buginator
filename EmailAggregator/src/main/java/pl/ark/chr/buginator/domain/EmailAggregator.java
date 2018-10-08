package pl.ark.chr.buginator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Sends notifications via email to predefined recipients.
 * If provided the language the email will be sent in one of supported translations
 */
@Entity
@Table(name = "buginator_email_aggregator")
public class EmailAggregator extends Aggregator<EmailAggregator> {

    private static final long serialVersionUID = 9140655678253265412L;

    public static final String EMAIL_AGGREGATOR_NAME = "EmailAggregator";

    private static final String RECIPIENT_SPLIT_TOKEN = ",";

    @Column(name = "recipients", nullable = false)
    private String recipients = "";

    @Column(name = "language")
    private String language;


    protected EmailAggregator() {
        super();
        this.setAggregatorClass(EMAIL_AGGREGATOR_NAME);
    }

    public EmailAggregator(String aggregatorClass, Application application, String recipients) {
        super(aggregatorClass, application);
        this.setAggregatorClass(EMAIL_AGGREGATOR_NAME);
        this.recipients = recipients;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String[] splitAndReturnRecipients() {
        return getRecipients().split(RECIPIENT_SPLIT_TOKEN);
    }
}
