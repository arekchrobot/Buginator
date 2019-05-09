package pl.ark.chr.buginator.app.application;

import pl.ark.chr.buginator.app.error.ErrorDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ApplicationDetailsDTO extends UserApplicationDTO {

    private List<ErrorDTO> errors;

    private ApplicationDetailsDTO(Builder builder) {
        super(builder);
        this.errors = builder.errors;
    }

    public List<ErrorDTO> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public static Builder builder(UserApplicationDTO base) {
        return new Builder(base);
    }

    static final class Builder extends UserApplicationDTO.Builder<Builder> {
        private List<ErrorDTO> errors;

        @Override
        protected Builder self() {
            return this;
        }

        private Builder(UserApplicationDTO base) {
            this.id = base.getId();
            this.name = base.getName();
            this.modify = base.isModify();
        }

        public Builder errors(List<ErrorDTO> val) {
            errors = val;
            return self();
        }

        public ApplicationDetailsDTO build() {
            return new ApplicationDetailsDTO(this);
        }
    }
}
