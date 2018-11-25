package pl.ark.chr.buginator.domain.core;

/**
 * Status of the error workflow
 * The lower the order the more important the error
 */
public enum ErrorStatus {
    CREATED(1),
    REOPENED(1),
    ONGOING(2),
    RESOLVED(3);

    private int order;

    ErrorStatus(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
