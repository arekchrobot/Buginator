package pl.ark.chr.buginator.app.application;

public class UserApplicationDTO extends BaseApplicationDTO {

    private boolean modify;

    protected UserApplicationDTO(Builder builder) {
        super(builder);
        modify = builder.modify;
    }

    public static Builder2 builder() {
        return new Builder2();
    }

    public boolean isModify() {
        return modify;
    }

    static abstract class Builder<T extends UserApplicationDTO.Builder<T>> extends BaseApplicationDTO.Builder<T> {
        protected boolean modify;

        Builder() {
        }

        public T modify(boolean val) {
            modify = val;
            return self();
        }

        public UserApplicationDTO build() {
            return new UserApplicationDTO(this);
        }
    }

    public static final class Builder2 extends Builder<Builder2> {

        Builder2() {
        }

        @Override
        protected Builder2 self() {
            return this;
        }
    }
}
