package pl.ark.chr.buginator.app.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

class ApplicationRequestDTO {

    @NotBlank(message = "application.name.blank")
    private String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    ApplicationRequestDTO(@JsonProperty("name") String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
