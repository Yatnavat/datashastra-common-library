package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;
import tech.oorjaa.datashastra.enums.ActivityPriority;
import tech.oorjaa.datashastra.enums.ActivityStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Main container entity for forecasting activities within client projects.
 * Represents a business forecasting initiative with specific scope and objectives.
 *
 * @author DataShastra Team
 * @version 2.0
 */
@Entity
@Table(name = "activity", indexes = {
    @Index(name = "idx_activity_tenant_status", columnList = "tenant_id, status, deleted"),
    @Index(name = "idx_activity_client_project", columnList = "client_id, project_id, tenant_id"),
    @Index(name = "idx_activity_priority_status", columnList = "priority, status, tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"forecasts"})
@ToString(exclude = {"forecasts"})
@SQLRestriction("deleted = false")
public class Activity extends BaseEntity {

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Activity name is required")
    @Size(min = 3, max = 100, message = "Activity name must be between 3 and 100 characters")
    private String name;

    @Column(length = 500)
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Priority is required")
    @Builder.Default
    private ActivityPriority priority = ActivityPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Status is required")
    @Builder.Default
    private ActivityStatus status = ActivityStatus.DRAFT;

    @Column(length = 100)
    @Size(max = 100, message = "Industry cannot exceed 100 characters")
    private String industry;

    @Column(name = "forecast_count", nullable = false)
    @Min(value = 0, message = "Forecast count cannot be negative")
    @Builder.Default
    private Integer forecastCount = 0;

    // Foreign Key Relationships (cross-aggregate references)
    @Column(name = "client_id", nullable = false)
    @NotNull(message = "Client is required")
    private Long clientId;

    @Column(name = "project_id", nullable = false)
    @NotNull(message = "Project is required")
    private Long projectId;

    @Column(name = "created_by_user_id", nullable = false)
    @NotNull(message = "Created by user is required")
    private Long createdByUserId;

    // Global Usage Fields
    @Column(name = "base_currency", length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid 3-letter ISO code")
    private String baseCurrency;

    @Column(name = "business_timezone", length = 50)
    private String businessTimezone;

    @Column(name = "locale", length = 10)
    @Pattern(regexp = "^[a-z]{2}(_[A-Z]{2})?$", message = "Locale must be in format 'en' or 'en_US'")
    private String locale;

    // Relationships
    @OneToMany(mappedBy = "activityId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("version DESC")
    @BatchSize(size = 20)
    private List<Forecast> forecasts = new ArrayList<>();

    // Business Logic Methods
    public boolean canCreateForecasts() {
        return status.canCreateForecasts();
    }

    public boolean isActive() {
        return status.isActive();
    }

    public void incrementForecastCount() {
        this.forecastCount++;
    }

    public void decrementForecastCount() {
        if (this.forecastCount > 0) {
            this.forecastCount--;
        }
    }

    // Custom Validation
    @AssertTrue(message = "Project must belong to the selected client")
    public boolean isProjectClientConsistent() {
        // This validation will be implemented in service layer
        // where we can access client and project repositories
        return true;
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = ActivityStatus.DRAFT;
        }
        if (this.priority == null) {
            this.priority = ActivityPriority.MEDIUM;
        }
        if (this.forecastCount == null) {
            this.forecastCount = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        // Validate tenant consistency in service layer
        // as we cannot access other repositories from entity
    }
}