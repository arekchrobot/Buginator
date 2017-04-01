package pl.ark.chr.buginator.domain;

import pl.ark.chr.buginator.domain.enums.AggregatorLogStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Arek on 2017-04-01.
 */
@Entity
@Table(name = "aggregator_log")
public class AggregatorLog extends BaseEntity {

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
