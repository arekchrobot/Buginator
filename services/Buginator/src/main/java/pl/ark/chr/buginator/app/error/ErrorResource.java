package pl.ark.chr.buginator.app.error;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.ark.chr.buginator.domain.core.ErrorStatus;
import pl.ark.chr.buginator.rest.annotations.RestController;

@RestController
class ErrorResource {

    private final ErrorService errorService;

    ErrorResource(ErrorService errorService) {
        this.errorService = errorService;
    }

    @GetMapping("/api/buginator/error/{id}")
    ErrorDetailsDTO getDetails(@PathVariable Long id) {
        return errorService.details(id);
    }

    @PutMapping("/api/buginator/error/{id}")
    void updateStatus(@PathVariable Long id, @RequestParam("status") ErrorStatus newStatus) {
        errorService.changeStatus(id, newStatus);
    }
}
