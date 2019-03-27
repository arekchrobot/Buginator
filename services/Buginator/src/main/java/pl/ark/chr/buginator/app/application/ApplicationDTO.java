package pl.ark.chr.buginator.app.application;

import java.util.Objects;

public class ApplicationDTO {

    private String name;
    private int allErrorCount;
    private int lastWeekErrorCount;

    private ApplicationDTO(Builder builder) {
        name = builder.name;
        allErrorCount = builder.allErrorCount;
        lastWeekErrorCount = builder.lastWeekErrorCount;
    }

    public String getName() {
        return name;
    }

    public int getAllErrorCount() {
        return allErrorCount;
    }

    public int getLastWeekErrorCount() {
        return lastWeekErrorCount;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private String name;
        private int allErrorCount;
        private int lastWeekErrorCount;

        private Builder(String val) {
            Objects.requireNonNull(val);

            name = val;
        }

        public Builder allErrorCount(int val) {
            allErrorCount = val;
            return this;
        }

        public Builder lastWeekErrorCount(int val) {
            lastWeekErrorCount = val;
            return this;
        }

        public ApplicationDTO build() {
            return new ApplicationDTO(this);
        }
    }
}
