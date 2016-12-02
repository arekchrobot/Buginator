package pl.ark.chr.buginator.filter;

import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.domain.filter.FilterData;
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
        for (UserApplication userApplication : userApplications) {
            if (userApplication.getApplication().getId().equals(filterData.getApplication().getId())) {
                return;
            }
        }
        throw new DataAccessException("Attempt to access forbidden resources", "");
    }
}
