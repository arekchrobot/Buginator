package pl.ark.chr.buginator.app.core.security.filter;

import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;

import java.util.Set;

class DataModifyClientFilter extends AbstractClientFilter {

    DataModifyClientFilter() {
    }

    @Override
    protected void validate(FilterData filterData, Set<UserApplication> userApplications) throws DataAccessException {
        userApplications.stream()
                .filter(ua -> ua.getApplication().getId().equals(filterData.getApplication().getId()) && ua.isModify())
                .findAny()
                .orElseThrow(() -> new DataAccessException("User is not permitted to modify application",""));
    }
}
