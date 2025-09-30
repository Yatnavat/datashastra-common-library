package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import tech.oorjaa.datashastra.enums.TenantStatus;

/**
 * Tenant (Company) Entity - Aggregate Root for Multi-Tenancy
 * Each tenant has isolated data and Keycloak realm configuration
 */
@Entity
@Table(name = "tenant", indexes = {
    @Index(name = "idx_tenant_name", columnList = "name", unique = true),
    @Index(name = "idx_tenant_status", columnList = "status, deleted"),
    @Index(name = "idx_tenant_realm", columnList = "keycloak_realm")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class Tenant extends BaseEntity {

    @NotBlank(message = "Tenant name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private TenantStatus status = TenantStatus.ACTIVE;

    // Keycloak Configuration

    @NotBlank(message = "Keycloak server URL is required")
    @Column(name = "keycloak_server_url", nullable = false, length = 500)
    private String keycloakServerUrl;

    @NotBlank(message = "Keycloak realm is required")
    @Size(max = 100)
    @Column(name = "keycloak_realm", nullable = false, length = 100)
    private String keycloakRealm;

    @NotBlank(message = "Keycloak client ID is required")
    @Size(max = 100)
    @Column(name = "keycloak_client_id", nullable = false, length = 100)
    private String keycloakClientId;

    @NotBlank(message = "Keycloak client secret is required")
    @Column(name = "keycloak_client_secret", nullable = false, length = 500)
    private String keycloakClientSecret;

    // Contact Information

    @Email(message = "Valid email is required")
    @Size(max = 255)
    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Size(max = 500)
    @Column(name = "address", length = 500)
    private String address;

    @Size(max = 100)
    @Column(name = "city", length = 100)
    private String city;

    @Size(max = 100)
    @Column(name = "country", length = 100)
    private String country;

    // Subscription & Limits

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_projects")
    private Integer maxProjects;

    @Column(name = "max_storage_gb")
    private Integer maxStorageGb;

    /**
     * JSON storage for flexible tenant configuration
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configuration", columnDefinition = "jsonb")
    private String configuration;

    // Business logic methods

    public boolean isActive() {
        return status == TenantStatus.ACTIVE;
    }

    public boolean canAddUser(int currentUserCount) {
        return maxUsers == null || currentUserCount < maxUsers;
    }

    public boolean canAddProject(int currentProjectCount) {
        return maxProjects == null || currentProjectCount < maxProjects;
    }

    public void activate() {
        this.status = TenantStatus.ACTIVE;
    }

    public void suspend() {
        this.status = TenantStatus.SUSPENDED;
    }

    public void deactivate() {
        this.status = TenantStatus.INACTIVE;
    }
}