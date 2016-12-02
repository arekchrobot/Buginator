package pl.ark.chr.buginator.filter;

import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.domain.filter.FilterData;
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
        for (UserApplication userApplication : userApplications) {
            if (userApplication.getApplication().getId().equals(filterData.getApplication().getId())) {
                if (userApplication.isModify()) {
                    return;
                } else {
                    throw new DataAccessException("User is not permitted to modify application","");
                }
            }
        }
        throw new DataAccessException("User is not permitted to modify application","");
    }
}
