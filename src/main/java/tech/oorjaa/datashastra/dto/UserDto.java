package tech.oorjaa.datashastra.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import tech.oorjaa.datashastra.enums.UserStatus;

import java.time.LocalDateTime;

/**
 * User Data Transfer Object
 * Used for API communication and user management
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private Integer tenantId;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;

    private String phoneNumber;

    @NotBlank(message = "Keycloak ID is required")
    private String keycloakId;

    @NotNull(message = "Status is required")
    private UserStatus status;

    private LocalDateTime lastLogin;

    private Boolean emailVerified;

    // Computed/Display Fields
    private String fullName;

    private Boolean isActive;

    private String statusBadgeColor;

    // Audit Fields
    private String createdBy;

    private LocalDateTime createdDate;

    private String modifiedBy;

    private LocalDateTime modifiedDate;
}