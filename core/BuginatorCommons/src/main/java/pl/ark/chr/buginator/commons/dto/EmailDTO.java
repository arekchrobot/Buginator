package pl.ark.chr.buginator.commons.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * Mail configuration used in JMS connections to send email
 */
public class EmailDTO implements Serializable {

    private static final long serialVersionUID = 578411959131684199L;

    private String emailBody;
    private String subject;
    private String to;
    private String cc;
    private String bcc;
    private String from;
    private boolean htmlBody;
    private String username;
    private String password;
    private String smtpPort;
    private String smtpHost;
    private boolean ssl;

    private EmailDTO(Builder builder) {
        emailBody = builder.emailBody;
        subject = builder.subject;
        to = builder.to;
        cc = builder.cc;
        bcc = builder.bcc;
        from = builder.from;
        htmlBody = builder.htmlBody;
        username = builder.username;
        password = builder.password;
        smtpPort = builder.smtpPort;
        smtpHost = builder.smtpHost;
        ssl = builder.ssl;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public String getSubject() {
        return subject;
    }

    public String getTo() {
        return to;
    }

    public String getCc() {
        return cc;
    }

    public String getBcc() {
        return bcc;
    }

    public String getFrom() {
        return from;
    }

    public boolean isHtmlBody() {
        return htmlBody;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public boolean isSsl() {
        return ssl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Created mail object from: ")
                .append(from)
                .append(" to: ")
                .append(to)
                .append(" cc: ")
                .append(cc)
                .append(" bcc: ")
                .append(bcc)
                .append(" with subject: ")
                .append(subject)
                .append(" and body: ")
                .append(emailBody)
                .append("\nConfiguration:")
                .append("\nUsername: ")
                .append(username)
                .append("\nSmtp Port:")
                .append(smtpPort)
                .append("\nSmtp Host:")
                .append(smtpHost)
                .append("\nSSL: ")
                .append(ssl);

        return sb.toString();
    }

    public static Builder builder(String from, String to) {
        return new Builder(from, to);
    }

    public static final class Builder {
        private String emailBody;
        private String subject;
        private String to;
        private String cc;
        private String bcc;
        private String from;
        private boolean htmlBody;
        private String username;
        private String password;
        private String smtpPort;
        private String smtpHost;
        private boolean ssl;

        public Builder(String from, String to) {
            Objects.requireNonNull(to);
            Objects.requireNonNull(from);

            if(to.isBlank() || from.isBlank()) {
                throw new IllegalArgumentException("One of the following parameters is empty: to - " + to + ", from - " + from);
            }

            this.from = from;
            this.to = to;
        }

        public Builder emailBody(String val) {
            emailBody = val;
            return this;
        }

        public Builder subject(String val) {
            subject = val;
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

        public Builder htmlBody(boolean val) {
            htmlBody = val;
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

        public EmailDTO build() {
            return new EmailDTO(this);
        }
    }
}
