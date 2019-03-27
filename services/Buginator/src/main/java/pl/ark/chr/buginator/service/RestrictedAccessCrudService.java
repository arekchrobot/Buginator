package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;

import java.util.List;
import java.util.Set;

/**
 * Created by Arek on 2017-01-18.
 */
public interface RestrictedAccessCrudService<T extends BaseEntity & FilterData> {

    T save(T t, Set<UserApplication> userApplications) throws DataAccessException;

    T get(Long id,  Set<UserApplication> userApplications) throws DataAccessException;

    List<T> getAllByApplication(Long appId,  Set<UserApplication> userApplications) throws DataAccessException;

    void delete(Long id,  Set<UserApplication> userApplications) throws DataAccessException;
}
