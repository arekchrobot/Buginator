package pl.ark.chr.buginator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.filter.ClientFilterFactory;
import pl.ark.chr.buginator.repository.ApplicationRepository;
import pl.ark.chr.buginator.repository.ErrorRepository;
import pl.ark.chr.buginator.service.ErrorService;

import java.util.List;
import java.util.Set;

/**
 * Created by Arek on 2017-01-16.
 */
@Service
@Transactional
public class ErrorServiceImpl extends RestrictedAccessCrudServiceImpl<Error> implements ErrorService {

    private final ClientFilter clientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS);

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    protected CrudRepository<Error, Long> getRepository() {
        return errorRepository;
    }

    @Override
    protected ClientFilter getClientFilter() {
        return clientFilter;
    }

    @Override
    public List<Error> getAllByApplication(Long appId, Set<UserApplication> userApplications) throws DataAccessException {
        Application application = applicationRepository.findOne(appId);

        clientFilter.validateAccess(application, userApplications);

        return errorRepository.findByApplication(application);
    }
}
