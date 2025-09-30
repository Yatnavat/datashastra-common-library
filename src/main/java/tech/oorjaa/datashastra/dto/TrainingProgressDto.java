package tech.oorjaa.datashastra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import tech.oorjaa.datashastra.enums.TrainingStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for TrainingProgress entity.
 * Represents real-time model training progress tracking.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainingProgressDto implements Serializable {

    private Long id;

    @NotNull(message = "Status is required")
    private TrainingStatus status;

    @Min(value = 0, message = "Progress percentage must be between 0 and 100")
    @Max(value = 100, message = "Progress percentage must be between 0 and 100")
    private Integer progressPercentage;

    @Min(value = 0, message = "Current iteration cannot be negative")
    private Integer currentIteration;

    @Min(value = 1, message = "Total iterations must be at least 1")
    private Integer totalIterations;

    @Min(value = 0, message = "Elapsed time cannot be negative")
    private Long elapsedTime; // in seconds

    @Min(value = 0, message = "Estimated time remaining cannot be negative")
    private Long estimatedTimeRemaining; // in seconds

    private String currentMetrics; // JSON containing current training metrics
    private String resourceUtilization; // JSON containing resource usage data
    private String trainingLogs; // JSON array of recent log entries
    private LocalDateTime lastUpdated;

    // Training Stage Information
    @Size(max = 50, message = "Current stage cannot exceed 50 characters")
    private String currentStage;

    @Min(value = 0, message = "Stage progress must be between 0 and 100")
    @Max(value = 100, message = "Stage progress must be between 0 and 100")
    private Integer stageProgress;

    // Performance Metrics (Real-time)
    @DecimalMin(value = "0.0", message = "Current loss must be non-negative")
    private BigDecimal currentLoss;

    @DecimalMin(value = "0.0", message = "Best loss must be non-negative")
    private BigDecimal bestLoss;

    @DecimalMin(value = "0.0", message = "Current accuracy must be non-negative")
    @DecimalMax(value = "100.0", message = "Current accuracy cannot exceed 100")
    private BigDecimal currentAccuracy;

    @DecimalMin(value = "0.0", message = "Validation accuracy must be non-negative")
    @DecimalMax(value = "100.0", message = "Validation accuracy cannot exceed 100")
    private BigDecimal validationAccuracy;

    // Resource Monitoring
    @Min(value = 0, message = "Memory usage cannot be negative")
    private Integer memoryUsageMb;

    @DecimalMin(value = "0.0", message = "CPU usage must be non-negative")
    @DecimalMax(value = "100.0", message = "CPU usage cannot exceed 100")
    private BigDecimal cpuUsagePercent;

    @DecimalMin(value = "0.0", message = "GPU usage must be non-negative")
    @DecimalMax(value = "100.0", message = "GPU usage cannot exceed 100")
    private BigDecimal gpuUsagePercent;

    @DecimalMin(value = "0.0", message = "Disk I/O must be non-negative")
    private BigDecimal diskIoMbPerSec;

    // Training Configuration Info
    @Min(value = 1, message = "Batch size must be at least 1")
    private Integer batchSize;

    @DecimalMin(value = "0.0", message = "Learning rate must be non-negative")
    private BigDecimal learningRate;

    @Min(value = 1, message = "Early stopping patience must be at least 1")
    private Integer earlyStoppingPatience;

    @Min(value = 0, message = "Epochs without improvement cannot be negative")
    private Integer epochsWithoutImprovement;

    // Error and Warning Tracking
    @Min(value = 0, message = "Error count cannot be negative")
    private Integer errorCount;

    @Min(value = 0, message = "Warning count cannot be negative")
    private Integer warningCount;

    @Size(max = 1000, message = "Error message cannot exceed 1000 characters")
    private String lastErrorMessage;

    private Boolean convergenceAchieved;

    @Min(value = 0, message = "Convergence iteration cannot be negative")
    private Integer convergenceIteration;

    // Foreign Key References
    @NotNull(message = "Forecast reference is required")
    private Long forecastId;

    // Audit Fields
    private LocalDateTime createdDate;
    private String createdBy;
    private Integer tenantId;

    // UI Display Fields
    private String statusBadgeColor;
    private String progressBarColor;
    private String completionPercentageText;
    private String elapsedTimeText;
    private String estimatedRemainingText;
    private String currentStageText;
    private Boolean showResourceWarning;
    private Boolean showConvergenceInfo;

    // Computed Fields
    private Boolean isRunning;
    private Boolean isCompleted;
    private Boolean isFailed;
    private Boolean isTerminal;
    private Boolean shouldEarlyStopping;
    private Boolean hasResourceConstraints;
    private Double completionPercentage;

    // Business Logic Methods
    public String getStatusDisplayText() {
        return status != null ? status.getDisplayName() : "Unknown";
    }

    public String getProgressDisplay() {
        if (progressPercentage == null) return "0%";
        return progressPercentage + "%";
    }

    public String getIterationDisplay() {
        if (currentIteration == null || totalIterations == null) return "N/A";
        return String.format("%d / %d", currentIteration, totalIterations);
    }

    public String getElapsedTimeDisplay() {
        if (elapsedTime == null) return "0s";
        long hours = elapsedTime / 3600;
        long minutes = (elapsedTime % 3600) / 60;
        long seconds = elapsedTime % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

    public String getEstimatedRemainingDisplay() {
        if (estimatedTimeRemaining == null) return "Unknown";
        long hours = estimatedTimeRemaining / 3600;
        long minutes = (estimatedTimeRemaining % 3600) / 60;
        
        if (hours > 0) {
            return String.format("%dh %dm remaining", hours, minutes);
        } else if (minutes > 0) {
            return String.format("%dm remaining", minutes);
        } else {
            return "< 1m remaining";
        }
    }

    public String getAccuracyDisplay() {
        if (currentAccuracy == null) return "N/A";
        return String.format("%.2f%%", currentAccuracy);
    }

    public String getResourceUsageDisplay() {
        StringBuilder usage = new StringBuilder();
        if (cpuUsagePercent != null) {
            usage.append(String.format("CPU: %.1f%%", cpuUsagePercent));
        }
        if (memoryUsageMb != null) {
            if (usage.length() > 0) usage.append(", ");
            usage.append(String.format("Memory: %dMB", memoryUsageMb));
        }
        if (gpuUsagePercent != null) {
            if (usage.length() > 0) usage.append(", ");
            usage.append(String.format("GPU: %.1f%%", gpuUsagePercent));
        }
        return usage.toString();
    }

    public boolean hasErrors() {
        return errorCount != null && errorCount > 0;
    }

    public boolean hasWarnings() {
        return warningCount != null && warningCount > 0;
    }

    public boolean isStuck() {
        return Boolean.TRUE.equals(isRunning) && lastUpdated != null && 
               lastUpdated.isBefore(LocalDateTime.now().minusMinutes(10));
    }
}