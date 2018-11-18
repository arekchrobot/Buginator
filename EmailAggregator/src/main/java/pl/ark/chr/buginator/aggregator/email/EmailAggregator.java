package pl.ark.chr.buginator.aggregator.email;

import pl.ark.chr.buginator.aggregator.domain.Aggregator;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Sends notifications via email to predefined recipients.
 * If provided the language the email will be sent in one of supported translations
 */
@Entity
@Table(name = "buginator_email_aggregator")
public class EmailAggregator extends Aggregator<EmailAggregator> {

    private static final long serialVersionUID = 9140655678253265412L;

    public static final String EMAIL_AGGREGATOR_NAME = EmailAggregator.class.getName();

    public static final String RECIPIENT_SPLIT_TOKEN = ",";

    @Column(name = "recipients", nullable = false)
    private String recipients = "";
    private String language;
    private String cc;
    private String bcc;
    private String smtpPort;
    private String smtpHost;
    private boolean ssl;

    protected EmailAggregator() {
        super();
        this.setAggregatorClass(EMAIL_AGGREGATOR_NAME);
    }

    public EmailAggregator(Application application, String recipients) {
        super(EMAIL_AGGREGATOR_NAME, application);

        Objects.requireNonNull(recipients);

        this.recipients = recipients;
    }

    private EmailAggregator(Builder builder) {
        super(EMAIL_AGGREGATOR_NAME, builder.application);
        setId(builder.id);
        setLogin(builder.login);
        setPassword(builder.password);
        setErrorSeverity(builder.errorSeverity);
        setCount(builder.count);
        setRecipients(builder.recipients);
        setLanguage(builder.language);
        setCc(builder.cc);
        setBcc(builder.bcc);
        setSmtpPort(builder.smtpPort);
        setSmtpHost(builder.smtpHost);
        setSsl(builder.ssl);
    }

    public String getRecipients() {
        return recipients;
    }

    protected void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getLanguage() {
        return language;
    }

    protected void setLanguage(String language) {
        this.language = language;
    }

    public String getCc() {
        return cc;
    }

    protected void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    protected void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getFrom() {
        return getLogin();
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    protected void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    protected void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public boolean isSsl() {
        return ssl;
    }

    protected void setSsl(boolean ssl) {
        this.ssl = ssl;
    }


    public static final class Builder {
        private Long id;
        private String login;
        private String password;
        private ErrorSeverity errorSeverity;
        private Application application;
        private int count;
        private String recipients;
        private String language;
        private String cc;
        private String bcc;
        private String smtpPort;
        private String smtpHost;
        private boolean ssl;

        public Builder(Application application, String recipients) {
            Objects.requireNonNull(application);
            Objects.requireNonNull(recipients);

            this.application = application;
            this.recipients = recipients;
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder login(String val) {
            login = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder errorSeverity(ErrorSeverity val) {
            errorSeverity = val;
            return this;
        }

        public Builder count(int val) {
            count = val;
            return this;
        }

        public Builder language(String val) {
            language = val;
            return this;
        }

        public Builder cc(String val) {
            cc = val;
            return this;
        }

        public Builder bcc(String val) {
            bcc = val;
            return this;
        }

        public Builder smtpPort(String val) {
            smtpPort = val;
            return this;
        }

        public Builder smtpHost(String val) {
            smtpHost = val;
            return this;
        }

        public Builder ssl(boolean val) {
            ssl = val;
            return this;
        }

        public EmailAggregator build() {
            return new EmailAggregator(this);
        }
    }
}
