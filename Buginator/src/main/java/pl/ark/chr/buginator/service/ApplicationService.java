package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.util.UserWrapper;

/**
 * Created by Arek on 2016-12-03.
 */
public interface ApplicationService extends CrudService<Application> {

    UserApplication save(Application entity, UserWrapper userWrapper);
}
