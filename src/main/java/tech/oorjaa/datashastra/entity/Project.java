package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import tech.oorjaa.datashastra.enums.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Project Entity - Project under Client
 * References Client by Foreign Key (cross-aggregate)
 */
@Entity
@Table(name = "project", indexes = {
    @Index(name = "idx_project_client", columnList = "client_id"),
    @Index(name = "idx_project_tenant_status", columnList = "tenant_id, status, deleted"),
    @Index(name = "idx_project_dates", columnList = "start_date, end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class Project extends BaseEntity {

    @NotBlank(message = "Project name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Foreign Key to Client (cross-aggregate reference)
     */
    @NotNull(message = "Client ID is required")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @DecimalMin(value = "0.0", message = "Budget must be positive")
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    @Size(max = 3)
    @Column(name = "currency_code", length = 3)
    private String currencyCode = "USD";

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.ACTIVE;

    // Business logic methods

    public boolean isActive() {
        return status == ProjectStatus.ACTIVE;
    }

    public void activate() {
        this.status = ProjectStatus.ACTIVE;
    }

    public void complete() {
        this.status = ProjectStatus.COMPLETED;
    }

    public void hold() {
        this.status = ProjectStatus.ON_HOLD;
    }

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalStateException("End date cannot be before start date");
        }
    }
}