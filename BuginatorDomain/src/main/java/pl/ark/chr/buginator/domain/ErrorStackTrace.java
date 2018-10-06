package pl.ark.chr.buginator.domain;

import javax.persistence.*;

/**
 * Represents the whole stack trace of the Error
 */
@Entity
@Table(name = "buginator_error_stack_trace",
        indexes = {@Index(name = "error_index", columnList = "buginator_error_id")
        })
public class ErrorStackTrace extends BaseEntity<ErrorStackTrace> {

    private static final long serialVersionUID = 5055088228557459563L;

    @Column(name = "stack_trace")
    private String stackTrace;

    @ManyToOne
    @JoinColumn(name = "buginator_error_id", nullable = false)
    private Error error;

    @Column(name = "stack_trace_order")
    private int stackTraceOrder;

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public int getStackTraceOrder() {
        return stackTraceOrder;
    }

    public void setStackTraceOrder(int stackTraceOrder) {
        this.stackTraceOrder = stackTraceOrder;
    }
}
