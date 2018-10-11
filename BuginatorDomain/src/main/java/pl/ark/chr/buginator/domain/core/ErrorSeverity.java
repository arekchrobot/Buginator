package pl.ark.chr.buginator.domain.core;

/**
 * Severity of an Error.
 * The lowest order is the most important
 */
public enum ErrorSeverity {
    WARNING(3),
    ERROR(2),
    CRITICAL(1);

    private int order;

    ErrorSeverity(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
