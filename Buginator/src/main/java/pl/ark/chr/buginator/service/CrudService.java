package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.BaseEntity;

import java.util.List;

/**
 * Created by Arek on 2016-09-29.
 */
public interface CrudService<T extends BaseEntity> {

    T save(T t);

    T get(Long id);

    List<T> getAll();

    void delete(Long id);
}
