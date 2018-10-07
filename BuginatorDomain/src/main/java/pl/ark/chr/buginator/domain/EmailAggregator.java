package pl.ark.chr.buginator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//TODO: move this aggregator to external API
/**
 * Sends notifications via email to predefined recipients.
 * If provided the language the email will be sent in one of supported translations
 */
@Entity
@Table(name = "buginator_email_aggregator")
public class EmailAggregator extends Aggregator<EmailAggregator> {

    private static final long serialVersionUID = 9140655678253265412L;

    @Column(name = "recipients")
    private String recipients = "";

    @Column(name = "language")
    private String language;

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
}
