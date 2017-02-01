package pl.ark.chr.buginator.domain.enums;

/**
 * Created by Arek on 2016-09-26.
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
