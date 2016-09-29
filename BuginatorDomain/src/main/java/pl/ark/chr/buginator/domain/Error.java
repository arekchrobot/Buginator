package pl.ark.chr.buginator.domain;

import pl.ark.chr.buginator.domain.enums.ErrorSeverity;
import pl.ark.chr.buginator.domain.enums.ErrorStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arek on 2016-09-26.
 */
@Entity
@Table(name = "buginator_error")
@SequenceGenerator(name = "default_gen", sequenceName = "buginator_error_seq", allocationSize = 1)
public class Error extends BaseEntity {

    private static final long serialVersionUID = -6062066697736318840L;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "error")
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

    @Column(name = "query_params", length = 250)
    private String queryParams;

    @Column(name = "request_url", length = 200)
    private String requestUrl;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "sent_to_aggregators")
    private Boolean sentToAggregators;

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

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Boolean getSentToAggregators() {
        return sentToAggregators;
    }

    public void setSentToAggregators(Boolean sentToAggregators) {
        this.sentToAggregators = sentToAggregators;
    }
}