package pl.ark.chr.buginator.service.impl;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.domain.filter.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.service.RestrictedAccessCrudService;

import java.util.Set;

/**
 * Created by Arek on 2017-01-18.
 */
public abstract class RestrictedAccessCrudServiceImpl<T extends BaseEntity & FilterData> extends CrudServiceImpl<T> implements RestrictedAccessCrudService<T> {

    protected abstract ClientFilter getClientFilter();

    @Override
    public T save(T t, Set<UserApplication> userApplications) throws DataAccessException {
        if (!t.isNew()) {
            getClientFilter().validateAccess(t, userApplications);
        }
        return super.save(t);
    }

    @Override
    public T get(Long id, Set<UserApplication> userApplications) throws DataAccessException {
        T entity = super.get(id);
        getClientFilter().validateAccess(entity, userApplications);
        return entity;
    }

    @Override
    public void delete(Long id, Set<UserApplication> userApplications) throws DataAccessException {
        T entity = super.get(id);
        getClientFilter().validateAccess(entity, userApplications);
        super.delete(id);
    }
}
