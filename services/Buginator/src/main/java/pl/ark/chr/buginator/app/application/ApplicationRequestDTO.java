package pl.ark.chr.buginator.app.application;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

class ApplicationRequestDTO {

    @NotBlank(message = "application.name.blank")
    private String name;

    ApplicationRequestDTO(@NotBlank(message = "application.name.blank") String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
