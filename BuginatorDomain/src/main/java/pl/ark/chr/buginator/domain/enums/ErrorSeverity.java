package pl.ark.chr.buginator.domain.enums;

/**
 * Created by Arek on 2016-09-26.
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
