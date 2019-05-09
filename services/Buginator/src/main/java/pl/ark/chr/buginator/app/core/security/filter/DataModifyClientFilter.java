package pl.ark.chr.buginator.app.core.security.filter;

import pl.ark.chr.buginator.app.application.UserApplicationDTO;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.persistence.security.FilterData;

import java.util.Set;

class DataModifyClientFilter extends AbstractClientFilter {

    DataModifyClientFilter() {
    }

    @Override
    protected void validate(FilterData filterData, Set<UserApplicationDTO> userApplications) throws DataAccessException {
        userApplications.stream()
                .filter(ua -> ua.getId().equals(filterData.getApplication().getId()) && ua.isModify())
                .findAny()
                .orElseThrow(() -> new DataAccessException("User is not permitted to modify application",""));
    }
}
