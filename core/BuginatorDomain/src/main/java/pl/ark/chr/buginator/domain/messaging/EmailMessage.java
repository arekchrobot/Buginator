package pl.ark.chr.buginator.domain.messaging;

import pl.ark.chr.buginator.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "buginator_email_message")
public class EmailMessage extends BaseEntity<EmailMessage> {

    private static final long serialVersionUID = 8861717205403748072L;

    public static final Long BUGINATOR = 1L;

    @Column(name = "from_email")
    private String from;
    private String username;
    @Column(name = "pass")
    private String password;
    @Column(name = "smtp_host")
    private String smtpPort;
    @Column(name = "smtp_port")
    private String smtpHost;
    private boolean ssl;

    protected EmailMessage() {
    }

    private EmailMessage(Builder builder) {
        setFrom(builder.from);
        setUsername(builder.username);
        setPassword(builder.password);
        setSmtpPort(builder.smtpPort);
        setSmtpHost(builder.smtpHost);
        setSsl(builder.ssl);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
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

    public String getFrom() {
        return from;
    }

    protected void setFrom(String from) {
        this.from = from;
    }

    public static final class Builder {
        private String from;
        private String username;
        private String password;
        private String smtpPort;
        private String smtpHost;
        private boolean ssl;

        private Builder() {
        }

        public Builder from(String val) {
            from = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
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

        public EmailMessage build() {
            Objects.requireNonNull(from);
            Objects.requireNonNull(username);
            Objects.requireNonNull(password);
            Objects.requireNonNull(smtpPort);
            Objects.requireNonNull(smtpHost);
            return new EmailMessage(this);
        }
    }
}
