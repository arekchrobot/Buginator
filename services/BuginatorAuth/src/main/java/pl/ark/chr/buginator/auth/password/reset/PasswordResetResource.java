package pl.ark.chr.buginator.auth.password.reset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PasswordResetResource {

    private PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetResource(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/api/auth/password/reset")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendResetToken(@RequestParam String email) {
        passwordResetService.sendPasswordResetEmail(email);
    }

    @PostMapping("/api/auth/password/change")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody PasswordResetDTO passwordResetDTO) {
        passwordResetService.changePassword(passwordResetDTO);
    }
}
