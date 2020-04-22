package pl.ark.chr.buginator.app.application.report;

import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.app.application.UserApplicationService;
import pl.ark.chr.buginator.app.core.security.AbstractApplicationAccessRestricted;
import pl.ark.chr.buginator.app.error.ErrorService;
import pl.ark.chr.buginator.app.exceptions.DataNotFoundException;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.security.session.LoggedUserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
class ReportService extends AbstractApplicationAccessRestricted<Application> {

    private final ApplicationRepository applicationRepository;
    private final ErrorService errorService;

    private static final int LAST_SEVEN_DAYS = 7;
    private static final long NO_ERRORS = 0;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReportService(LoggedUserService loggedUserService, UserApplicationService userApplicationService,
                         ApplicationRepository applicationRepository, ErrorService errorService) {
        super(loggedUserService, userApplicationService);
        this.applicationRepository = applicationRepository;
        this.errorService = errorService;
    }

    LastWeekErrorsDTO generateLastWeekErrorReport(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new DataNotFoundException("application.notFound"));

        readAccessAllowed(application);

        Map<String, Long> lastWeekErrors = errorService.getErrorsGroupedByDaySince(application,
                LocalDate.now().minusDays(LAST_SEVEN_DAYS));

        List<String> labels = new ArrayList<>();
        List<Long> errorCounts = new ArrayList<>();

        for (int i = LAST_SEVEN_DAYS; i >= 0; i--) {
            LocalDate selectedDate =  LocalDate.now().minusDays(i);
            String dateKey = selectedDate.format(DATE_FORMAT);

            labels.add(dateKey);
            errorCounts.add(lastWeekErrors.getOrDefault(dateKey, NO_ERRORS));
        }

        return LastWeekErrorsDTO.builder()
                .labels(labels)
                .data(errorCounts)
                .build();
    }
}
