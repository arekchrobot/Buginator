package pl.ark.chr.buginator.domain.core;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.persistence.security.FilterData;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stores the single error that occured in external application.
 */
@Entity
@Table(name = "buginator_error")
public class Error extends BaseEntity<Error> implements FilterData {

    private static final long serialVersionUID = -6062066697736318840L;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "error", cascade = CascadeType.PERSIST)
    @OrderColumn(name = "stack_trace_order")
    private List<ErrorStackTrace> stackTrace = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ErrorStatus status;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private ErrorSeverity severity;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @OneToOne
    @JoinColumn(name = "user_agent_data_id")
    private UserAgentData userAgent;

    @Column(name = "query_params", length = 750)
    private String queryParams;

    @Column(name = "request_url", length = 200)
    private String requestUrl;

    @Column(name = "request_method", length = 50)
    private String requestMethod;

    @Column(name = "request_params", length = 1000)
    private String requestParams;

    @Column(name = "request_headers", length = 800)
    private String requestHeaders;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "error_count", nullable = false)
    private int count;

    @Column(name = "last_occurrence", nullable = false)
    private LocalDate lastOccurrence;

    protected Error() {
    }

    private Error(Builder builder) {
        setId(builder.id);
        setTitle(builder.title);
        setDescription(builder.description);
        setStackTrace(builder.stackTrace);
        setStatus(builder.status);
        setSeverity(builder.severity);
        setDateTime(builder.dateTime);
        setUserAgent(builder.userAgent);
        setQueryParams(builder.queryParams);
        setRequestUrl(builder.requestUrl);
        setRequestMethod(builder.requestMethod);
        setRequestParams(builder.requestParams);
        setRequestHeaders(builder.requestHeaders);
        setApplication(builder.application);
        setCount(builder.count);
        setLastOccurrence(builder.lastOccurrence);
    }

    public String getTitle() {
        return title;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ErrorStackTrace> getStackTrace() {
        return List.copyOf(stackTrace);
    }

    //TODO: rethink solution
    public void setStackTrace(List<ErrorStackTrace> stackTrace) {
        this.stackTrace = stackTrace;
    }

    public ErrorStatus getStatus() {
        return status;
    }

    public void setStatus(ErrorStatus status) {
        this.status = status;
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ErrorSeverity severity) {
        this.severity = severity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    protected void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public UserAgentData getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(UserAgentData userAgent) {
        this.userAgent = userAgent;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Application getApplication() {
        return application;
    }

    protected void setApplication(Application application) {
        this.application = application;
    }

    public int getCount() {
        return count;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    public void incrementCount() {
        this.count += 1;
    }

    public LocalDate getLastOccurrence() {
        return lastOccurrence;
    }

    protected void setLastOccurrence(LocalDate lastOccurrence) {
        this.lastOccurrence = lastOccurrence;
    }

    public void parseAndSetLastOccurrence(String lastOccurence) {
        this.lastOccurrence = LocalDateTime.parse(lastOccurence, DATE_TIME_FORMATTER).toLocalDate();
    }

    public static Builder builder(String title, ErrorSeverity severity, ErrorStatus status,
                                  String date, Application application) {
        return new Builder()
                .title(title)
                .severity(severity)
                .status(status)
                .dateTime(date)
                .lastOccurrence(date)
                .application(application);
    }


    public static final class Builder {
        private Long id;
        private String title;
        private String description;
        private List<ErrorStackTrace> stackTrace;
        private ErrorStatus status;
        private ErrorSeverity severity;
        private LocalDateTime dateTime;
        private UserAgentData userAgent;
        private String queryParams;
        private String requestUrl;
        private String requestMethod;
        private String requestParams;
        private String requestHeaders;
        private Application application;
        private int count;
        private LocalDate lastOccurrence;

        private Builder() {
            count = 1;
        }

        public Builder id(Long val) {
            Objects.requireNonNull(val);
            id = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder stackTrace(List<ErrorStackTrace> val) {
            stackTrace = val;
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

        public Builder dateTime(String val) {
            dateTime = LocalDateTime.parse(val, DATE_TIME_FORMATTER);
            return this;
        }

        public Builder userAgent(UserAgentData val) {
            userAgent = val;
            return this;
        }

        public Builder queryParams(String val) {
            queryParams = val;
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

        public Builder requestHeaders(String val) {
            requestHeaders = val;
            return this;
        }

        public Builder application(Application val) {
            application = val;
            return this;
        }

        public Builder count(int val) {
            count = val;
            return this;
        }

        public Builder lastOccurrence(String val) {
            lastOccurrence = LocalDateTime.parse(val, DATE_TIME_FORMATTER).toLocalDate();
            return this;
        }

        public Error build() {
            return new Error(this);
        }
    }
}
