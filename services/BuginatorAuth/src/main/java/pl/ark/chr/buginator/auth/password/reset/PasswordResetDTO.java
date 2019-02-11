package pl.ark.chr.buginator.auth.password.reset;

import javax.validation.constraints.NotBlank;

class PasswordResetDTO {

    @NotBlank(message = "user.password.reset.token")
    private String token;
    @NotBlank(message = "user.password.blank")
    private CharSequence newPassword;

    PasswordResetDTO(String token, CharSequence newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    String getToken() {
        return token;
    }

    CharSequence getNewPassword() {
        return newPassword;
    }
}
