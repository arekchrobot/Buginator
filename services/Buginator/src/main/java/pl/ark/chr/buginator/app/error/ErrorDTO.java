package pl.ark.chr.buginator.app.error;

import java.time.LocalDate;

public class ErrorDTO {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String severity;
    private LocalDate lastOccurrence;
    private int count;

    private ErrorDTO(Builder builder) {
        id = builder.id;
        title = builder.title;
        description = builder.description;
        status = builder.status;
        severity = builder.severity;
        lastOccurrence = builder.lastOccurrence;
        count = builder.count;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getSeverity() {
        return severity;
    }

    public LocalDate getLastOccurrence() {
        return lastOccurrence;
    }

    public int getCount() {
        return count;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private Long id;
        private String title;
        private String description;
        private String status;
        private String severity;
        private LocalDate lastOccurrence;
        private int count;

        private Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder status(String val) {
            status = val;
            return this;
        }

        public Builder severity(String val) {
            severity = val;
            return this;
        }

        public Builder lastOccurrence(LocalDate val) {
            lastOccurrence = val;
            return this;
        }

        public Builder count(int val) {
            count = val;
            return this;
        }

        public ErrorDTO build() {
            return new ErrorDTO(this);
        }
    }
}
