package pl.ark.chr.buginator.core.security;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.persistence.security.FilterData;

public interface ApplicationAccessRestricted<T extends BaseEntity & FilterData> {

    void readAccessAllowed(T entity) throws DataAccessException;

    void writeAccessAllowed(T entity) throws DataAccessException;
}
