package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import tech.oorjaa.datashastra.enums.UserStatus;

import java.time.LocalDateTime;

/**
 * User Entity - Supporting Entity within Tenant Aggregate
 * Integrated with Keycloak for authentication
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_tenant_email", columnList = "tenant_id, email", unique = true),
    @Index(name = "idx_user_keycloak", columnList = "keycloak_id", unique = true),
    @Index(name = "idx_user_status", columnList = "tenant_id, status, deleted")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class User extends BaseEntity {

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    @Size(max = 255)
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @NotBlank(message = "Keycloak ID is required")
    @Size(max = 100)
    @Column(name = "keycloak_id", nullable = false, unique = true, length = 100)
    private String keycloakId;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @NotNull(message = "Email verified status is required")
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    // Business logic methods

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void lock() {
        this.status = UserStatus.LOCKED;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }
}