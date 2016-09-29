package pl.ark.chr.buginator.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.service.CrudService;
import pl.ark.chr.buginator.util.HibernateLazyInitiator;

import java.util.List;

/**
 * Created by Arek on 2016-09-29.
 */
public abstract class CrudServiceImpl<T extends BaseEntity> implements CrudService<T> {

    protected abstract JpaRepository<T, Long> getRepository();

    @Override
    public T save(T entity) {
        entity = getRepository().save(entity);
        return entity;
    }

    @Override
    public T get(Long id) {
        T entity = getRepository().findOne(id);
        HibernateLazyInitiator.init(entity);
        return entity;
    }

    @Override
    public List<T> getAll() {
        List<T> entities = (List<T>) getRepository().findAll();
        HibernateLazyInitiator.initList(entities);
        return entities;
    }

    @Override
    public void delete(Long id) {
        getRepository().delete(id);
    }
}
