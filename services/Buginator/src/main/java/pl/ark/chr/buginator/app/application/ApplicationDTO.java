package pl.ark.chr.buginator.app.application;

import java.util.Objects;

public class ApplicationDTO extends UserApplicationDTO {

    private int allErrorCount;
    private int lastWeekErrorCount;

    private ApplicationDTO(Builder builder) {
        super(builder.name, builder.modify);
        allErrorCount = builder.allErrorCount;
        lastWeekErrorCount = builder.lastWeekErrorCount;
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
    public static Builder builder(String name, boolean modify) {
        return new Builder(name, modify);
    }
    public static Builder builder(UserApplicationDTO base) {
        return new Builder(base);
    }

    public static final class Builder {
        private String name;
        private boolean modify;
        private int allErrorCount;
        private int lastWeekErrorCount;

        private Builder(String val) {
            Objects.requireNonNull(val);

            name = val;
        }

        private Builder(UserApplicationDTO base) {
            this(base.getName(), base.isModify());
        }

        private Builder(String val1, boolean val2) {
            Objects.requireNonNull(val1);

            name = val1;
            modify = val2;
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
