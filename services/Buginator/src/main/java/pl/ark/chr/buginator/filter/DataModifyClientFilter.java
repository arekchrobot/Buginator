package pl.ark.chr.buginator.filter;

import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.Set;

/**
 * Created by Arek on 2016-12-01.
 */
public class DataModifyClientFilter extends AbstractClientFilter {

    DataModifyClientFilter() {
    }

    @Override
    protected void validate(FilterData filterData, Set<UserApplication> userApplications) throws DataAccessException {
        userApplications.stream()
                .filter(ua -> ua.getApplication().getId().equals(filterData.getApplication().getId()) && ua.isModify())
                .findAny()
                .map(ua -> ua)
                .orElseThrow(() -> new DataAccessException("User is not permitted to modify application",""));
    }
}
