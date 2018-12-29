package pl.ark.chr.buginator.filter;

import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.Set;

/**
 * Created by Arek on 2016-12-01.
 */
public interface ClientFilter {

    void validateAccess(FilterData filterData, Set<UserApplication> userApplications) throws DataAccessException;

    void setNextClientFilter(ClientFilter nextClientFilter);
}
