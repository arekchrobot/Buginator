package pl.ark.chr.buginator.commons.util;

public interface DtoMapper<T, D> {

    D toDto(T t);
}
