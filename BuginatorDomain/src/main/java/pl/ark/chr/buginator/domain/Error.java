package pl.ark.chr.buginator.domain;

import pl.ark.chr.buginator.domain.enums.ErrorSeverity;
import pl.ark.chr.buginator.domain.enums.ErrorStatus;
import pl.ark.chr.buginator.domain.filter.FilterData;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the single error that occured in external application.
 */
@Entity
@Table(name = "buginator_error")
public class Error extends BaseEntity<Error> implements FilterData {

    private static final long serialVersionUID = -6062066697736318840L;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "error", cascade = CascadeType.PERSIST)
    @OrderColumn(name = "stack_trace_order")
    private List<ErrorStackTrace> stackTrace = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private ErrorStatus status;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "severity")
    private ErrorSeverity severity;

    @Column(name = "date_time")
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

    @Column(name = "error_count")
    private int count;

    @Column(name = "last_occurence")
    private LocalDate lastOccurrence;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ErrorStackTrace> getStackTrace() {
        return stackTrace;
    }

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

    public void setDateTime(LocalDateTime dateTime) {
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

    public void setApplication(Application application) {
        this.application = application;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDate getLastOccurrence() {
        return lastOccurrence;
    }

    public void setLastOccurrence(LocalDate lastOccurrence) {
        this.lastOccurrence = lastOccurrence;
    }
}
