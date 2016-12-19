package pl.ark.chr.buginator.filter;

import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.domain.filter.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.Set;

/**
 * Created by Arek on 2016-12-01.
 */
public abstract class AbstractClientFilter implements ClientFilter {

    private ClientFilter nextClientFilter;

    AbstractClientFilter() {
    }

    @Override
    public void validateAccess(FilterData filterData, Set<UserApplication> userApplications) throws DataAccessException {
        validate(filterData, userApplications);
        if(null != nextClientFilter) {
            nextClientFilter.validateAccess(filterData, userApplications);
        }
    }

    protected abstract void validate(FilterData filterData, Set<UserApplication> userApplications) throws DataAccessException;

    @Override
    public void setNextClientFilter(ClientFilter nextClientFilter) {
        this.nextClientFilter = nextClientFilter;
    }
}