package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.List;

/**
 * Created by Arek on 2016-12-03.
 */
public interface ApplicationService extends RestrictedAccessCrudService<Application> {

    UserApplication save(Application entity, UserWrapper userWrapper) throws DataAccessException;

    List<Application> getUserApplications(UserWrapper userWrapper);
}
