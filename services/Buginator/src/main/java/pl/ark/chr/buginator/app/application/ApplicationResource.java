package pl.ark.chr.buginator.app.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
public class ApplicationResource {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationResource(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/api/buginator/application/by-user")
    public Set<ApplicationDTO> getForLoggedUser() {
        return applicationService.getUserApps();
    }

    @PostMapping("/api/buginator/application")
    @ResponseStatus(HttpStatus.CREATED)
    public UserApplicationDTO create(@Valid @RequestBody ApplicationRequestDTO applicationRequest) {
        return applicationService.create(applicationRequest);
    }

    @GetMapping("/api/buginator/application/{id}")
    public ApplicationDetailsDTO getById(@PathVariable Long id) {
        return applicationService.get(id);
    }
}
