package pl.ark.chr.buginator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Arek on 2017-03-11.
 */
@Entity
@Table(name = "email_aggregator")
public class EmailAggregator extends Aggregator {

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
