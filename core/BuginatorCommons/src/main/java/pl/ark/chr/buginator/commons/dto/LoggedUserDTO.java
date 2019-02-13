package pl.ark.chr.buginator.commons.dto;

import java.util.Objects;
import java.util.Set;

public class LoggedUserDTO {

    private String name;
    private String email;
    private Set<String> permissions;

    public LoggedUserDTO(String name, String email, Set<String> permissions) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(email);
        Objects.requireNonNull(permissions);

        this.name = name;
        this.email = email;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
