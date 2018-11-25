package pl.ark.chr.buginator.domain.core;

import pl.ark.chr.buginator.domain.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents the whole stack trace of the Error
 */
@Entity
@Table(name = "buginator_error_stack_trace",
        indexes = {
                @Index(name = "error_index", columnList = "error_id")
        })
public class ErrorStackTrace extends BaseEntity<ErrorStackTrace> {

    private static final long serialVersionUID = 5055088228557459563L;

    @Column(name = "stack_trace")
    private String stackTrace;

    @ManyToOne
    @JoinColumn(name = "error_id", nullable = false)
    private Error error;

    @Column(name = "stack_trace_order", nullable = false)
    private int stackTraceOrder;

    protected ErrorStackTrace() {
    }

    public ErrorStackTrace(Error error, int stackTraceOrder, String stackTrace) {
        Objects.requireNonNull(error);

        this.stackTrace = stackTrace;
        this.error = error;
        this.stackTraceOrder = stackTraceOrder;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    protected void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Error getError() {
        return error;
    }

    protected void setError(Error error) {
        this.error = error;
    }

    public int getStackTraceOrder() {
        return stackTraceOrder;
    }

    protected void setStackTraceOrder(int stackTraceOrder) {
        this.stackTraceOrder = stackTraceOrder;
    }
}
