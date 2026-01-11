package jutjubic.isa.backend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "activation_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_activation_token_value", columnNames = "token"),
                @UniqueConstraint(name = "uk_activation_token_user", columnNames = "user_id")
        },
        indexes = {
                @Index(name = "idx_activation_token_value", columnList = "token")
        }
)
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // deo tokena koji ide u url
    @Column(nullable = false, length = 120)
    private String token;

    // 1 korisnik 1 token
    @OneToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_activation_token_user")
    )
    private User user;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant expiresAt;

    public ActivationToken() {
    }

    public ActivationToken(String token, User user, Instant expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
