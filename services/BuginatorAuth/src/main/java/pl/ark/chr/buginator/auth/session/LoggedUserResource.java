package pl.ark.chr.buginator.auth.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.security.session.LoggedUserService;

@RestController
public class LoggedUserResource {

    private LoggedUserService loggedUserService;

    @Autowired
    public LoggedUserResource(LoggedUserService loggedUserService) {
        this.loggedUserService = loggedUserService;
    }

    @GetMapping("/api/auth/session/user/current")
    public LoggedUserDTO getCurrentUser() {
        return loggedUserService.getCurrentUser();
    }
}
