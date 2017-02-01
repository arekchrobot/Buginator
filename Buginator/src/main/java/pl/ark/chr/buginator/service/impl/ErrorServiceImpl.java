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

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-01-16.
 */
@Service
@Transactional
public class ErrorServiceImpl extends RestrictedAccessCrudServiceImpl<Error> implements ErrorService {

    private final ClientFilter clientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS);

    private Comparator<Error> statusComparator = Comparator.comparing(error -> error.getStatus().getOrder());
    private Comparator<Error> severityComparator = Comparator.comparing(error -> error.getSeverity().getOrder());
    private Comparator<Error> lastOccurrenceComparator = Comparator.comparing(Error::getLastOccurrence).reversed();
    private Comparator<Error> errorComparator = lastOccurrenceComparator.thenComparing(severityComparator).thenComparing(statusComparator);

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    protected CrudRepository<Error, Long> getRepository() {
        return errorRepository;
    }

    @Override
    protected ClientFilter getReadClientFilter() {
        return clientFilter;
    }

    @Override
    protected ClientFilter getWriteClientFilter() {
        return clientFilter;
    }

    @Override
    public List<Error> getAllByApplication(Long appId, Set<UserApplication> userApplications) throws DataAccessException {
        Application application = applicationRepository.findOne(appId);

        clientFilter.validateAccess(application, userApplications);

        List<Error> errors = errorRepository.findByApplication(application);

        errors = errors.stream().sorted(errorComparator).collect(Collectors.toList());

        return errors;
    }
}
