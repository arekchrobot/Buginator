package pl.ark.chr.buginator.app.core.security.filter;

import pl.ark.chr.buginator.app.application.UserApplicationDTO;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.persistence.security.FilterData;

import java.util.Set;

abstract class AbstractClientFilter implements ClientFilter {

    private ClientFilter nextClientFilter;

    AbstractClientFilter() {
    }

    @Override
    public void validateAccess(FilterData filterData, Set<UserApplicationDTO> userApplications) throws DataAccessException {
        validate(filterData, userApplications);
        if(null != nextClientFilter) {
            nextClientFilter.validateAccess(filterData, userApplications);
        }
    }

    protected abstract void validate(FilterData filterData, Set<UserApplicationDTO> userApplications) throws DataAccessException;

    @Override
    public void setNextClientFilter(ClientFilter nextClientFilter) {
        this.nextClientFilter = nextClientFilter;
    }
}
