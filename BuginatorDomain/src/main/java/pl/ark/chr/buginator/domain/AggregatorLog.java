package pl.ark.chr.buginator.domain;

import pl.ark.chr.buginator.domain.enums.AggregatorLogStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Stores logs of statuses for communication with external platforms.
 */
@Entity
@Table(name = "aggregator_log",
        indexes = {
                @Index(name = "aggregator_index", columnList = "aggregator_id"),
                @Index(name = "timestamp_index", columnList = "timestamp")
        })
public class AggregatorLog extends BaseEntity<AggregatorLog> {

    private static final long serialVersionUID = -6299139410461999506L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aggregator_id", nullable = false)
    private Aggregator aggregator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "error_id", nullable = false)
    private Error error;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AggregatorLogStatus status;

    @Column(name = "error_description", length = 250)
    private String errorDescription;

    @Column(name = "retry_count")
    private int retryCount;

    public Aggregator getAggregator() {
        return aggregator;
    }

    public void setAggregator(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public AggregatorLogStatus getStatus() {
        return status;
    }

    public void setStatus(AggregatorLogStatus status) {
        this.status = status;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
