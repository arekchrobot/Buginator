package pl.ark.chr.buginator.auth.register;

import javax.validation.constraints.NotBlank;

class RegisterDTO {

    @NotBlank(message = "company.name.blank")
    private String companyName;
    private String companyAddress;

    private String userName;
    @NotBlank(message = "user.email.blank")
    private String userEmail;
    @NotBlank(message = "user.password.blank")
    private CharSequence userPassword;

    RegisterDTO(String companyName, String companyAddress, String userName,
                String userEmail, CharSequence userPassword) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    private RegisterDTO(Builder builder) {
        companyName = builder.companyName;
        companyAddress = builder.companyAddress;
        userName = builder.userName;
        userEmail = builder.userEmail;
        userPassword = builder.userPassword;
    }

    static Builder builder() {
        return new Builder();
    }

    String getCompanyName() {
        return companyName;
    }

    String getCompanyAddress() {
        return companyAddress;
    }

    String getUserName() {
        return userName;
    }

    String getUserEmail() {
        return userEmail;
    }

    CharSequence getUserPassword() {
        return userPassword;
    }


    static final class Builder {
        private @NotBlank(message = "company.name.blank") String companyName;
        private String companyAddress;
        private String userName;
        private @NotBlank(message = "user.email.blank") String userEmail;
        private @NotBlank(message = "user.password.blank") CharSequence userPassword;

        private Builder() {
        }

        Builder companyName(@NotBlank(message = "company.name.blank") String val) {
            companyName = val;
            return this;
        }

        Builder companyAddress(String val) {
            companyAddress = val;
            return this;
        }

        Builder userName(String val) {
            userName = val;
            return this;
        }

        Builder userEmail(@NotBlank(message = "user.email.blank") String val) {
            userEmail = val;
            return this;
        }

        Builder userPassword(@NotBlank(message = "user.password.blank") CharSequence val) {
            userPassword = val;
            return this;
        }

        RegisterDTO build() {
            return new RegisterDTO(this);
        }
    }
}
