package pl.ark.chr.buginator.app.core.security.filter;

import pl.ark.chr.buginator.app.application.UserApplicationDTO;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.persistence.security.FilterData;

import java.util.Set;

class EmptyClientFilter extends AbstractClientFilter {

    EmptyClientFilter() {
    }

    @Override
    protected void validate(FilterData filterData, Set<UserApplicationDTO> userApplications) throws DataAccessException {
        //do nothing, this is basic filter for every configuration
    }
}
