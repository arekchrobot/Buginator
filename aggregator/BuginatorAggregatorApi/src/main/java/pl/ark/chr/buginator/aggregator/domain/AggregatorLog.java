package pl.ark.chr.buginator.aggregator.domain;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.core.Error;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Stores logs of statuses for communication with external platforms.
 */
@Entity
@Table(name = "buginator_aggregator_log",
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

    protected AggregatorLog() {
    }

    private AggregatorLog(Builder builder) {
        setAggregator(builder.aggregator);
        setError(builder.error);
        setTimestamp(builder.timestamp);
        setStatus(builder.status);
        setErrorDescription(builder.errorDescription);
    }

    public Aggregator getAggregator() {
        return aggregator;
    }

    protected void setAggregator(Aggregator aggregator) {
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

    protected void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void updateTimestamp() {
        this.timestamp = LocalDateTime.now();
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

    public void emptyErrorDescription() {
        this.error = null;
    }

    public int getRetryCount() {
        return retryCount;
    }

    protected void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void incrementRetryCount() {
        this.retryCount += 1;
    }

    public static final class Builder {
        private Aggregator aggregator;
        private Error error;
        private LocalDateTime timestamp;
        private AggregatorLogStatus status;
        private String errorDescription;

        public Builder() {
            timestamp = LocalDateTime.now();
        }

        public Builder aggregator(Aggregator val) {
            Objects.requireNonNull(val);
            aggregator = val;
            return this;
        }

        public Builder error(Error val) {
            Objects.requireNonNull(val);
            error = val;
            return this;
        }

        public Builder status(AggregatorLogStatus val) {
            status = val;
            return this;
        }

        public Builder errorDescription(String val) {
            errorDescription = val;
            return this;
        }

        public AggregatorLog build() {
            return new AggregatorLog(this);
        }
    }
}
