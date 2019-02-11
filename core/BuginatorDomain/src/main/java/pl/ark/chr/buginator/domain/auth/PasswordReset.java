package pl.ark.chr.buginator.domain.auth;

import pl.ark.chr.buginator.domain.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "buginator_password_reset")
public class PasswordReset extends BaseEntity<PasswordReset> {

    private static final long serialVersionUID = 48908733650474501L;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "token_used")
    private boolean tokenUsed;

    protected PasswordReset() {
    }

    public PasswordReset(User user, String token) {
        this(user, token, LocalDateTime.now());
    }

    public PasswordReset(User user, String token, LocalDateTime createdAt) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(token);
        Objects.requireNonNull(createdAt);

        this.user = user;
        this.token = token;
        this.createdAt = createdAt;
        this.tokenUsed = false;
    }

    public User getUser() {
        return user;
    }

    protected void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isTokenUsed() {
        return tokenUsed;
    }

    protected void setTokenUsed(boolean tokenUsed) {
        this.tokenUsed = tokenUsed;
    }

    public void markTokenAsUsed() {
        this.tokenUsed = true;
    }
}
