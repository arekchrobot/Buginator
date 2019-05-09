package pl.ark.chr.buginator.app.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.app.application.BaseApplicationDTO;
import pl.ark.chr.buginator.app.application.UserApplicationService;
import pl.ark.chr.buginator.app.core.security.AbstractApplicationAccessRestricted;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.repository.core.ErrorRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ErrorService extends AbstractApplicationAccessRestricted<Application> {

    private ErrorRepository errorRepository;
    private ErrorMapper errorMapper;

    private Comparator<Error> statusComparator = Comparator.comparing(error -> error.getStatus().getOrder());
    private Comparator<Error> severityComparator = Comparator.comparing(error -> error.getSeverity().getOrder());
    private Comparator<Error> lastOccurrenceComparator = Comparator.comparing(Error::getLastOccurrence).reversed();
    private Comparator<Error> errorComparator = lastOccurrenceComparator.thenComparing(severityComparator).thenComparing(statusComparator);

    @Autowired
    public ErrorService(LoggedUserService loggedUserService, UserApplicationService userApplicationService,
                        ErrorRepository errorRepository, ErrorMapper errorMapper) {
        super(loggedUserService, userApplicationService);
        this.errorRepository = errorRepository;
        this.errorMapper = errorMapper;
    }

    public int countByApplication(BaseApplicationDTO application) {
        return errorRepository.countByApplication_Id(application.getId());
    }

    public int countSince(BaseApplicationDTO application, LocalDate timestamp) {
        return errorRepository.countByApplication_IdAndLastOccurrenceGreaterThanEqual(application.getId(), timestamp);
    }

    @Transactional(readOnly = true)
    public List<ErrorDTO> getAllByApplication(Application application) {
        readAccessAllowed(application);

        try (Stream<Error> errors = errorRepository.findByApplication(application)) {
            return errors
                    .sorted(errorComparator)
                    .map(errorMapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}
