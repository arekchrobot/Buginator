package pl.ark.chr.buginator.app.application.report;

import java.util.Collections;
import java.util.List;

public class LastWeekErrorsDTO {

    private List<String> labels;
    private List<Long> data;

    private LastWeekErrorsDTO(Builder builder) {
        labels = builder.labels;
        data = builder.data;
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Long> getData() {
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private List<String> labels;
        private List<Long> data;

        private Builder() {
        }

        public Builder labels(List<String> val) {
            labels = Collections.unmodifiableList(val);
            return this;
        }

        public Builder data(List<Long> val) {
            data = Collections.unmodifiableList(val);
            return this;
        }

        public LastWeekErrorsDTO build() {
            return new LastWeekErrorsDTO(this);
        }
    }
}
