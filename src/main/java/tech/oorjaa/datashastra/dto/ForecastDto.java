package tech.oorjaa.datashastra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import tech.oorjaa.datashastra.enums.ForecastStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Forecast entity.
 * Represents a complete forecast instance with all workflow steps.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastDto implements Serializable {

    private Long id;

    @NotNull(message = "Activity ID is required")
    private Long activityId;
    
    private String activityName;

    @NotBlank(message = "Version is required")
    @Pattern(regexp = "^v\\d+\\.\\d+$", message = "Version must follow pattern v1.0, v2.1, etc.")
    private String version;

    @NotNull(message = "Status is required")
    private ForecastStatus status = ForecastStatus.DRAFT;

    // Model Information
    private Long modelId;
    private String modelName;
    private String modelType;
    private String modelComplexity;

    // Workflow Progress (8-step)
    @Min(value = 1, message = "Current step must be between 1 and 8")
    @Max(value = 8, message = "Current step must be between 1 and 8")
    private Integer currentStep = 1;
    
    private Boolean step1Complete = false; // Data Upload
    private Boolean step2Complete = false; // Analysis
    private Boolean step3Complete = false; // Model Selection
    private Boolean step4Complete = false; // Features
    private Boolean step5Complete = false; // Aggregation
    private Boolean step6Complete = false; // Date Ranges
    private Boolean step7Complete = false; // Training
    private Boolean step8Complete = false; // Results

    // Configuration Data
    private String forecastConfiguration;
    private String selectedFeatures;
    private String aggregationLevel;
    private LocalDateTime forecastStartDate;
    private LocalDateTime forecastEndDate;
    private Integer forecastHorizon;
    private String forecastGranularity;

    // Performance Metrics
    @DecimalMin(value = "0.0", message = "Accuracy must be non-negative")
    @DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100")
    private BigDecimal accuracy;

    private BigDecimal mae;
    private BigDecimal rmse;
    private BigDecimal mape;
    private BigDecimal r2Score;

    // Training Information
    private LocalDateTime trainingStartedAt;
    private LocalDateTime trainingCompletedAt;
    private Long trainingDuration; // in seconds
    private Integer trainingDataPoints;
    private Integer validationDataPoints;
    private Integer testDataPoints;
    private Integer featureCount;
    private String hyperparameters;

    // Results
    private String forecastResults;
    private String visualizationConfig;
    private String modelExplanation;
    private String featureImportance;
    private String confidenceIntervals;
    private String errorAnalysis;

    // Global Support
    private String forecastCurrency;
    private String forecastTimezone;
    private String forecastLocale;

    // Progress Tracking
    private Long trainingProgressId;
    private Integer progressPercentage;
    private String currentStage;
    private Long estimatedTimeRemaining;

    // Quality Metrics
    private BigDecimal dataQualityScore;
    private BigDecimal modelConfidenceScore;
    private String validationMessages;

    // Audit Fields
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime completedAt;
    private String createdBy;
    private String updatedBy;
    private Long createdByUserId;
    private Long updatedByUserId;

    // Multi-tenant Field
    private Integer tenantId;

    // UI Display Fields
    private String statusBadgeColor;
    private String progressBarColor;
    private String displayVersion;
    private List<String> availableActions;

    // Computed Fields
    private Boolean isLatestVersion;
    private Boolean canEdit;
    private Boolean canRetrain;
    private Boolean canExport;
    private Boolean hasResults;

    // Validation Methods
    public boolean isComplete() {
        return step1Complete && step2Complete && step3Complete && step4Complete &&
               step5Complete && step6Complete && step7Complete && step8Complete;
    }

    public boolean isInProgress() {
        return status == ForecastStatus.IN_PROGRESS;
    }

    public boolean isFailed() {
        return status == ForecastStatus.FAILED;
    }

    public boolean isSuccessful() {
        return status == ForecastStatus.COMPLETED && accuracy != null;
    }

    // Business Logic Methods
    public String getStepName() {
        if (currentStep == null) return "Unknown";
        return switch (currentStep) {
            case 1 -> "Data Upload";
            case 2 -> "Analysis";
            case 3 -> "Model Selection";
            case 4 -> "Features";
            case 5 -> "Aggregation";
            case 6 -> "Date Ranges";
            case 7 -> "Training";
            case 8 -> "Results";
            default -> "Unknown";
        };
    }

    public String getCompletionPercentage() {
        if (currentStep == null) return "0%";
        return (currentStep * 12.5) + "%";
    }

    public String getDurationDisplay() {
        if (trainingDuration == null) return "N/A";
        long hours = trainingDuration / 3600;
        long minutes = (trainingDuration % 3600) / 60;
        long seconds = trainingDuration % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
}