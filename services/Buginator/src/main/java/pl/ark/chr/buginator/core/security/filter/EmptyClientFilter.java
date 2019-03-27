package pl.ark.chr.buginator.core.security.filter;

import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.Set;

class EmptyClientFilter extends AbstractClientFilter {

    EmptyClientFilter() {
    }

    @Override
    protected void validate(FilterData filterData, Set<UserApplication> userApplications) throws DataAccessException {
        //do nothing, this is basic filter for every configuration
    }
}
