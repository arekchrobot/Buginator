package pl.ark.chr.buginator.auth.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegisterResource {

    private RegisterService registerService;

    @Autowired
    public RegisterResource(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/api/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterDTO registerDTO) {
        registerService.registerCompanyAndUser(registerDTO);
    }
}
