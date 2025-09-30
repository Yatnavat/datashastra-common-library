package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Role Entity - Standalone Entity for RBAC
 * Represents system roles (Admin, User, Viewer, etc.)
 */
@Entity
@Table(name = "role", indexes = {
    @Index(name = "idx_role_name", columnList = "name", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

    @NotBlank(message = "Role name is required")
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull(message = "Active status is required")
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // Business logic methods

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}