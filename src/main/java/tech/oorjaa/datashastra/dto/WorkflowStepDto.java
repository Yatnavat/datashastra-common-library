package tech.oorjaa.datashastra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import tech.oorjaa.datashastra.enums.StepStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for WorkflowStep entity.
 * Represents 8-step forecasting workflow progress tracking.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowStepDto implements Serializable {

    private Long id;

    @NotNull(message = "Step number is required")
    @Min(value = 1, message = "Step number must be between 1 and 8")
    @Max(value = 8, message = "Step number must be between 1 and 8")
    private Integer stepNumber;

    @NotBlank(message = "Step name is required")
    @Size(max = 100, message = "Step name cannot exceed 100 characters")
    private String stepName;

    @Size(max = 500, message = "Step description cannot exceed 500 characters")
    private String stepDescription;

    @NotNull(message = "Step status is required")
    private StepStatus status;

    private Boolean isCompleted;
    private Boolean isSkipped;
    private Boolean isOptional;

    @Min(value = 0, message = "Progress percentage must be between 0 and 100")
    @Max(value = 100, message = "Progress percentage must be between 0 and 100")
    private Integer progressPercentage;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @Min(value = 0, message = "Duration cannot be negative")
    private Long durationSeconds;

    private String stepData; // JSON containing step-specific data
    private String validationResults; // JSON containing validation results
    private String errorMessages; // JSON array of error messages

    @Min(value = 0, message = "Error count cannot be negative")
    private Integer errorCount;

    @Min(value = 0, message = "Warning count cannot be negative")
    private Integer warningCount;

    // User Information
    private String completedByUser;
    private String lastModifiedByUser;

    // Foreign Key References
    @NotNull(message = "Forecast reference is required")
    private Long forecastId;

    // Audit Fields
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
    private String createdBy;
    private String modifiedBy;
    private Integer tenantId;

    // UI Display Fields
    private String statusBadgeColor;
    private String progressBarColor;
    private String stepIcon;
    private String durationText;
    private String completionTimeText;
    private Boolean showErrors;
    private Boolean showWarnings;
    private Boolean canEdit;
    private Boolean canSkip;
    private Boolean canReset;

    // Computed Fields
    private Boolean isPending;
    private Boolean isInProgress;
    private Boolean isFailed;
    private Boolean hasErrors;
    private Boolean hasWarnings;
    private Boolean canProceedToNext;

    // Business Logic Methods
    public String getStepDisplayName() {
        if (stepNumber == null) return stepName;
        return String.format("Step %d: %s", stepNumber, stepName != null ? stepName : "Unknown");
    }

    public String getStatusDisplayText() {
        return status != null ? status.getDisplayName() : "Unknown";
    }

    public String getProgressText() {
        if (progressPercentage == null) return "0%";
        return progressPercentage + "%";
    }

    public String getDurationDisplay() {
        if (durationSeconds == null) return "N/A";
        
        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        long seconds = durationSeconds % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

    public String getCompletionTimeDisplay() {
        if (completedAt == null) return "Not completed";
        return completedAt.toString(); // Can be formatted as needed
    }

    public boolean isActive() {
        return status == StepStatus.IN_PROGRESS;
    }

    public boolean isBlocked() {
        return status == StepStatus.ERROR;
    }

    public boolean needsAttention() {
        return Boolean.TRUE.equals(hasErrors) || status == StepStatus.ERROR;
    }

    public String getStepTypeIcon() {
        if (stepNumber == null) return "question-circle";
        return switch (stepNumber) {
            case 1 -> "upload"; // Data Upload
            case 2 -> "chart-line"; // Analysis
            case 3 -> "cog"; // Model Selection
            case 4 -> "list"; // Features
            case 5 -> "layer-group"; // Aggregation
            case 6 -> "calendar"; // Date Ranges
            case 7 -> "play"; // Training
            case 8 -> "chart-bar"; // Results
            default -> "question-circle";
        };
    }

    public String getEstimatedDuration() {
        if (stepNumber == null) return "Unknown";
        // Estimated durations based on step complexity
        return switch (stepNumber) {
            case 1 -> "5-10 minutes";
            case 2 -> "2-5 minutes";
            case 3 -> "3-7 minutes";
            case 4 -> "5-15 minutes";
            case 5 -> "2-5 minutes";
            case 6 -> "2-3 minutes";
            case 7 -> "10-30 minutes";
            case 8 -> "1-2 minutes";
            default -> "Unknown";
        };
    }

    public boolean isTimeConsuming() {
        return stepNumber != null && (stepNumber == 7 || stepNumber == 4); // Training and Features
    }

    public boolean isQuickStep() {
        return stepNumber != null && (stepNumber == 6 || stepNumber == 8); // Date Ranges and Results
    }
}