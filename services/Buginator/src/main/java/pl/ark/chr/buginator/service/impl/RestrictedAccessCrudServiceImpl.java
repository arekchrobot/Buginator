package pl.ark.chr.buginator.service.impl;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.app.core.security.filter.ClientFilter;
import pl.ark.chr.buginator.service.RestrictedAccessCrudService;

import java.util.Set;

/**
 * Created by Arek on 2017-01-18.
 */
public abstract class RestrictedAccessCrudServiceImpl<T extends BaseEntity & FilterData> extends CrudServiceImpl<T> implements RestrictedAccessCrudService<T> {

    protected abstract ClientFilter getReadClientFilter();

    protected abstract ClientFilter getWriteClientFilter();

    @Override
    public T save(T t, Set<UserApplication> userApplications) throws DataAccessException {
        if (!t.isNew()) {
//            getWriteClientFilter().validateAccess(t, userApplications);
        }
        return super.save(t);
    }

    @Override
    public T get(Long id, Set<UserApplication> userApplications) throws DataAccessException {
        T entity = super.get(id);
//        getReadClientFilter().validateAccess(entity, userApplications);
        return entity;
    }

    @Override
    public void delete(Long id, Set<UserApplication> userApplications) throws DataAccessException {
        T entity = super.get(id);
//        getWriteClientFilter().validateAccess(entity, userApplications);
        super.delete(id);
    }
}
