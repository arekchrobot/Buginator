package pl.ark.chr.buginator.app.application;

import java.util.Objects;

public class UserApplicationDTO {

    private String name;
    private boolean modify;

    public UserApplicationDTO(String name, boolean modify) {
        Objects.requireNonNull(name);

        this.name = name;
        this.modify = modify;
    }

    public String getName() {
        return name;
    }

    public boolean isModify() {
        return modify;
    }
}
