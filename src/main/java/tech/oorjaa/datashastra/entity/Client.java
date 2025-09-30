package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import tech.oorjaa.datashastra.enums.ClientStatus;

/**
 * Client Entity - Customer/Client under Tenant
 * References Tenant by Foreign Key (cross-aggregate)
 */
@Entity
@Table(name = "client", indexes = {
    @Index(name = "idx_client_tenant_status", columnList = "tenant_id, status, deleted"),
    @Index(name = "idx_client_name", columnList = "tenant_id, name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class Client extends BaseEntity {

    @NotBlank(message = "Client name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 100)
    @Column(name = "industry", length = 100)
    private String industry;

    @Email(message = "Valid email is required")
    @Size(max = 255)
    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ClientStatus status = ClientStatus.ACTIVE;

    // Business logic methods

    public boolean isActive() {
        return status == ClientStatus.ACTIVE;
    }

    public void activate() {
        this.status = ClientStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = ClientStatus.INACTIVE;
    }
}