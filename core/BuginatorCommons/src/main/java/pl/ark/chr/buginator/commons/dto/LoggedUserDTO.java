package pl.ark.chr.buginator.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class LoggedUserDTO {

    private String name;
    private String email;
    private Set<String> permissions;
    @JsonIgnore
    private Long companyId;

    public LoggedUserDTO(String name, String email, Set<String> permissions, Long companyId) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(email);
        Objects.requireNonNull(permissions);
        Objects.requireNonNull(companyId);

        this.name = name;
        this.email = email;
        this.permissions = permissions;
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getPermissions() {
        Set.of(permissions.toArray());
        return Collections.unmodifiableSet(permissions);
    }

    public Long getCompanyId() {
        return companyId;
    }
}
