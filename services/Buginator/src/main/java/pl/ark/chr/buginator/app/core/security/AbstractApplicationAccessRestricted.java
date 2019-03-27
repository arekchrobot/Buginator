package pl.ark.chr.buginator.app.core.security;

import pl.ark.chr.buginator.app.application.UserApplicationService;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;
import pl.ark.chr.buginator.app.core.security.filter.ClientFilter;
import pl.ark.chr.buginator.app.core.security.filter.ClientFilterFactory;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.security.session.LoggedUserService;

import java.util.Set;

public abstract class AbstractApplicationAccessRestricted<T extends BaseEntity & FilterData> implements ApplicationAccessRestricted<T> {

    private final ClientFilter readClientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS);
    private final ClientFilter writeClientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS,
            ClientFilterFactory.ClientFilterType.DATA_MODIFY);

    protected LoggedUserService loggedUserService;
    protected UserApplicationService userApplicationService;

    public AbstractApplicationAccessRestricted(LoggedUserService loggedUserService,
                                               UserApplicationService userApplicationService) {
        this.loggedUserService = loggedUserService;
        this.userApplicationService = userApplicationService;
    }

    @Override
    public void readAccessAllowed(T entity) throws DataAccessException {
        Set<UserApplication> userApps = getUserApplications();
        readClientFilter.validateAccess(entity, userApps);
    }

    @Override
    public void writeAccessAllowed(T entity) throws DataAccessException {
        Set<UserApplication> userApps = getUserApplications();
        writeClientFilter.validateAccess(entity, userApps);
    }

    private Set<UserApplication> getUserApplications() {
        LoggedUserDTO currentUser = loggedUserService.getCurrentUser();
        return userApplicationService.getCachedUserApplications(currentUser.getEmail());
    }
}
