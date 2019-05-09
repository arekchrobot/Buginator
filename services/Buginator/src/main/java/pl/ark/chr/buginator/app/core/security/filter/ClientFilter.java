package pl.ark.chr.buginator.app.core.security.filter;

import pl.ark.chr.buginator.app.application.UserApplicationDTO;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.persistence.security.FilterData;

import java.util.Set;

public interface ClientFilter {

    void validateAccess(FilterData filterData, Set<UserApplicationDTO> userApplications) throws DataAccessException;

    void setNextClientFilter(ClientFilter nextClientFilter);
}
