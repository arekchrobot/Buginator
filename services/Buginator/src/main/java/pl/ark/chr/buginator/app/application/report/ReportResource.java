package pl.ark.chr.buginator.app.application.report;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.ark.chr.buginator.rest.annotations.RestController;

@RestController
class ReportResource {

    private final ReportService reportService;

    ReportResource(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/api/buginator/application/report/last-week-errors/{id}")
    LastWeekErrorsDTO generateLastWeekErrorsReport(@PathVariable Long id) {
        return reportService.generateLastWeekErrorReport(id);
    }
}
