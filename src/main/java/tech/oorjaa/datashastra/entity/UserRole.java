package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * UserRole Entity - Junction Entity for User-Role Many-to-Many
 * Maps users to their assigned roles
 */
@Entity
@Table(name = "user_role", indexes = {
    @Index(name = "idx_user_role_user", columnList = "user_id"),
    @Index(name = "idx_user_role_role", columnList = "role_id"),
    @Index(name = "idx_user_role_unique", columnList = "user_id, role_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole extends BaseEntity {

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Role ID is required")
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "granted_by", length = 100)
    private String grantedBy;
}