package pl.ark.chr.buginator.filter;

import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.Set;

/**
 * Created by Arek on 2016-12-01.
 */
public class ApplicationAccessClientFilter extends AbstractClientFilter {

    ApplicationAccessClientFilter() {
    }

    @Override
    protected void validate(FilterData filterData, Set<UserApplication> userApplications) throws DataAccessException {
        userApplications.stream()
                .filter(ua -> ua.getApplication().getId().equals(filterData.getApplication().getId()))
                .findAny()
                .map(userApplication1 -> userApplication1)
                .orElseThrow(() -> new DataAccessException("Attempt to access forbidden resources", ""));
    }
}
