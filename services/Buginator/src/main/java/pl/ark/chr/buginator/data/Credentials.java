package pl.ark.chr.buginator.data;

/**
 * Created by Arek on 2016-09-29.
 */
public class Credentials {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new StringBuilder(60)
                .append("Credentials{ username:")
                .append(username)
                .append(", password: ")
                .append(password)
                .append("}")
                .toString();
    }
}
