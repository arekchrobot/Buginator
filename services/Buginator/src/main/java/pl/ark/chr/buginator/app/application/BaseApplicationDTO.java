package pl.ark.chr.buginator.app.application;

import java.util.Objects;

public abstract class BaseApplicationDTO {

    private Long id;
    private String name;

    BaseApplicationDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseApplicationDTO that = (BaseApplicationDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    static abstract class Builder<T extends Builder<T>> {
        protected Long id;
        protected String name;

        protected abstract T self();

        Builder() {
        }

        public T id(Long val) {
            Objects.requireNonNull(val);

            id = val;
            return self();
        }

        public T name(String val) {
            Objects.requireNonNull(val);

            name = val;
            return self();
        }

        public abstract BaseApplicationDTO build();
    }
}
