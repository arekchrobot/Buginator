package pl.ark.chr.buginator.app.application;

public class ApplicationDTO extends UserApplicationDTO {

    private int allErrorCount;
    private int lastWeekErrorCount;

    private ApplicationDTO(Builder builder) {
        super(builder);
        allErrorCount = builder.allErrorCount;
        lastWeekErrorCount = builder.lastWeekErrorCount;
    }

    public int getAllErrorCount() {
        return allErrorCount;
    }

    public int getLastWeekErrorCount() {
        return lastWeekErrorCount;
    }

    public static Builder builder(UserApplicationDTO base) {
        return new Builder(base);
    }

    static final class Builder extends UserApplicationDTO.Builder<Builder> {
        private int allErrorCount;
        private int lastWeekErrorCount;

        @Override
        protected Builder self() {
            return this;
        }

        private Builder(UserApplicationDTO base) {
            this.id = base.getId();
            this.name = base.getName();
            this.modify = base.isModify();
        }

        public Builder allErrorCount(int val) {
            allErrorCount = val;
            return self();
        }

        public Builder lastWeekErrorCount(int val) {
            lastWeekErrorCount = val;
            return self();
        }

        public ApplicationDTO build() {
            return new ApplicationDTO(this);
        }
    }
}
