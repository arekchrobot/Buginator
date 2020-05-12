package pl.ark.chr.buginator.app.error;

import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ErrorDetailsDTO {

    private final String title;
    private final String description;
    private final ErrorStatus status;
    private final ErrorSeverity severity;
    private final LocalDateTime dateTime;
    private final LocalDate lastOccurrence;
    private final int count;
    private final String requestUrl;
    private final String requestMethod;
    private final String requestParams;
    private final String queryParams;
    private final String requestHeaders;
    private final String stackTrace;
    private final UserAgentDTO userAgent;

    private ErrorDetailsDTO(Builder builder) {
        title = builder.title;
        description = builder.description;
        status = builder.status;
        severity = builder.severity;
        dateTime = builder.dateTime;
        lastOccurrence = builder.lastOccurrence;
        count = builder.count;
        requestUrl = builder.requestUrl;
        requestMethod = builder.requestMethod;
        requestParams = builder.requestParams;
        queryParams = builder.queryParams;
        requestHeaders = builder.requestHeaders;
        stackTrace = builder.stackTrace;
        userAgent = builder.userAgent;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ErrorStatus getStatus() {
        return status;
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getLastOccurrence() {
        return lastOccurrence;
    }

    public int getCount() {
        return count;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public UserAgentDTO getUserAgent() {
        return userAgent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private String description;
        private ErrorStatus status;
        private ErrorSeverity severity;
        private LocalDateTime dateTime;
        private LocalDate lastOccurrence;
        private int count;
        private String requestUrl;
        private String requestMethod;
        private String requestParams;
        private String queryParams;
        private String requestHeaders;
        private String stackTrace;
        private UserAgentDTO userAgent;

        private Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder status(ErrorStatus val) {
            status = val;
            return this;
        }

        public Builder severity(ErrorSeverity val) {
            severity = val;
            return this;
        }

        public Builder dateTime(LocalDateTime val) {
            dateTime = val;
            return this;
        }

        public Builder lastOccurrence(LocalDate val) {
            lastOccurrence = val;
            return this;
        }

        public Builder count(int val) {
            count = val;
            return this;
        }

        public Builder requestUrl(String val) {
            requestUrl = val;
            return this;
        }

        public Builder requestMethod(String val) {
            requestMethod = val;
            return this;
        }

        public Builder requestParams(String val) {
            requestParams = val;
            return this;
        }

        public Builder queryParams(String val) {
            queryParams = val;
            return this;
        }

        public Builder requestHeaders(String val) {
            requestHeaders = val;
            return this;
        }

        public Builder stackTrace(String val) {
            stackTrace = val;
            return this;
        }

        public Builder userAgent(UserAgentDTO val) {
            userAgent = val;
            return this;
        }

        public ErrorDetailsDTO build() {
            return new ErrorDetailsDTO(this);
        }
    }
}
