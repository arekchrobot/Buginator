package pl.ark.chr.buginator.core.security.filter;

import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.Set;

class ApplicationAccessClientFilter extends AbstractClientFilter {

    ApplicationAccessClientFilter() {
    }

    @Override
    protected void validate(FilterData filterData, Set<UserApplication> userApplications) throws DataAccessException {
        userApplications.stream()
                .filter(ua -> ua.getApplication().getId().equals(filterData.getApplication().getId()))
                .findAny()
                .orElseThrow(() -> new DataAccessException("Attempt to access forbidden resources", ""));
    }
}
