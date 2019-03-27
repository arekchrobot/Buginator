package pl.ark.chr.buginator.app.core.security;

import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.persistence.security.FilterData;

public interface ApplicationAccessRestricted<T extends BaseEntity & FilterData> {

    void readAccessAllowed(T entity) throws DataAccessException;

    void writeAccessAllowed(T entity) throws DataAccessException;
}
