package tech.oorjaa.datashastra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import tech.oorjaa.datashastra.enums.ActivityPriority;
import tech.oorjaa.datashastra.enums.ActivityStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Activity entity.
 * Used for API communication and client data exchange.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityDto implements Serializable {

    private Long id;

    @NotBlank(message = "Activity name is required")
    @Size(min = 3, max = 100, message = "Activity name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private String clientName;
    private String projectName;

    @NotNull(message = "Status is required")
    private ActivityStatus status = ActivityStatus.DRAFT;

    @NotNull(message = "Priority is required")
    private ActivityPriority priority = ActivityPriority.MEDIUM;

    private String industry;

    @Min(value = 0, message = "Forecast count cannot be negative")
    private Integer forecastCount = 0;

    // Global Support Fields
    private String baseCurrency;
    private String businessTimezone;
    private String locale;
    private String dataResidency;

    // Configuration Fields
    private String configurations;
    private String tags;
    private String businessContext;
    private String forecastingRequirements;

    // User Information
    private Long createdByUserId;
    private String createdByUserName;
    private Long updatedByUserId;
    private String updatedByUserName;

    // Audit Fields
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;

    // Computed Fields
    private Boolean canCreateForecasts;
    private Boolean isActive;
    private Integer activeForecastsCount;
    private LocalDateTime lastForecastDate;
    private Double averageAccuracy;

    // Multi-tenant Field
    private Integer tenantId;

    // UI Display Fields
    private String statusBadgeColor;
    private String priorityBadgeColor;
    private String displayName;

    // Validation Methods
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               clientId != null && projectId != null &&
               status != null && priority != null;
    }

    public boolean canBeDeleted() {
        return status == ActivityStatus.DRAFT || 
               status == ActivityStatus.ARCHIVED;
    }

    public boolean canBeEdited() {
        return status != ActivityStatus.ARCHIVED;
    }

    // Business Logic Methods
    public String getFullName() {
        if (clientName != null && projectName != null) {
            return String.format("%s - %s - %s", clientName, projectName, name);
        }
        return name;
    }

    public String getStatusDisplayText() {
        return status != null ? status.getDisplayName() : "Unknown";
    }

    public String getPriorityDisplayText() {
        return priority != null ? priority.getDisplayName() : "Unknown";
    }
}