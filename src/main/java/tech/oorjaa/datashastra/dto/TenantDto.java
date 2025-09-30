package tech.oorjaa.datashastra.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import tech.oorjaa.datashastra.enums.TenantStatus;

import java.time.LocalDateTime;

/**
 * Tenant Data Transfer Object
 * Used for API communication and tenant management
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantDto {

    private Long id;

    private Integer tenantId;

    @NotBlank(message = "Tenant name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Status is required")
    private TenantStatus status;

    // Keycloak Configuration
    @NotBlank(message = "Keycloak server URL is required")
    private String keycloakServerUrl;

    @NotBlank(message = "Keycloak realm is required")
    private String keycloakRealm;

    @NotBlank(message = "Keycloak client ID is required")
    private String keycloakClientId;

    // Note: Client secret is write-only, not returned in DTO

    // Contact Information
    @Email(message = "Valid email is required")
    private String contactEmail;

    private String contactPhone;

    private String address;

    private String city;

    private String country;

    // Subscription & Limits
    private Integer maxUsers;

    private Integer maxProjects;

    private Integer maxStorageGb;

    // Configuration JSON (as String)
    private String configuration;

    // Computed/Display Fields
    private Boolean isActive;

    private String statusBadgeColor;

    private String displayName;

    // Audit Fields
    private String createdBy;

    private LocalDateTime createdDate;

    private String modifiedBy;

    private LocalDateTime modifiedDate;
}