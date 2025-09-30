package tech.oorjaa.datashastra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import tech.oorjaa.datashastra.enums.ModelComplexity;
import tech.oorjaa.datashastra.enums.ModelType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Models entity.
 * Represents machine learning model information and capabilities.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelsDto implements Serializable {

    private Long id;

    @NotBlank(message = "Model name is required")
    @Size(max = 50, message = "Model name cannot exceed 50 characters")
    private String modelName;

    @NotNull(message = "Model type is required")
    private ModelType modelType;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String modelDescription;

    @NotNull(message = "Model complexity is required")
    private ModelComplexity complexity;

    // Model Capabilities
    private Boolean handlesSeasonality = false;
    private Boolean handlesTrend = true;
    private Boolean handlesMultivariate = false;
    private Boolean handlesMissingValues = false;
    private Boolean handlesOutliers = false;
    private Boolean handlesNonlinearity = false;
    private Boolean supportsConfidenceIntervals = true;
    private Boolean supportsFeatureImportance = false;

    // Performance Characteristics
    @Size(max = 20, message = "Accuracy tier cannot exceed 20 characters")
    private String accuracy; // "High", "Medium", "Low"

    @Size(max = 20, message = "Speed tier cannot exceed 20 characters")
    private String speed; // "Fast", "Moderate", "Slow"

    @Size(max = 20, message = "Interpretability cannot exceed 20 characters")
    private String interpretability; // "High", "Medium", "Low"

    // Resource Requirements
    @Min(value = 10, message = "Minimum data points must be at least 10")
    private Integer minDataPoints = 30;

    @Min(value = 1, message = "Optimal data points must be at least 1")
    private Integer optimalDataPoints = 100;

    @Min(value = 1, message = "Training time must be positive")
    private Integer trainingTimeMinutes = 5;

    @Min(value = 128, message = "Memory requirements must be at least 128 MB")
    private Integer memoryRequirementsMb = 512;

    private Boolean cpuIntensive = false;
    private Boolean gpuAccelerated = false;

    // Global Support
    private Boolean supportsMultiCurrency = false;
    private Boolean supportsMultiTimezone = false;
    private Boolean supportsMultiLanguage = false;

    // Configuration and Usage
    private String defaultHyperparameters;
    private String hyperparameterRanges;
    private String prerequisites;
    private String limitations;
    private String bestFor;
    private String notRecommendedFor;

    // Model Status
    private Boolean isActive = true;
    private Boolean isRecommended = false;
    private Boolean isBeta = false;
    private Boolean requiresLicense = false;

    // Version Information
    @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "Version must follow semantic versioning (e.g., 1.0.0)")
    private String modelVersion;
    
    private String framework; // "sklearn", "tensorflow", "prophet", etc.
    private String frameworkVersion;

    // Usage Statistics (populated from actual usage)
    private Long totalUsageCount;
    private Double averageAccuracy;
    private Double averageTrainingTime;
    private LocalDateTime lastUsedAt;
    private Integer successfulForecasts;
    private Integer failedForecasts;

    // Documentation and Support
    private String documentationUrl;
    private String tutorialUrl;
    private String supportEmail;
    private List<String> tags;
    private List<String> categories;

    // Audit Fields
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;

    // UI Display Fields
    private String complexityBadgeColor;
    private String statusBadgeColor;
    private Boolean showRecommendedBadge;
    private Boolean showBetaBadge;
    private Integer popularityScore;

    // Computed Fields
    private Boolean canHandle;
    private Boolean meetsRequirements;
    private String suitabilityScore;
    private List<String> warnings;
    private List<String> recommendations;

    // Business Logic Methods
    public boolean isHighComplexity() {
        return complexity == ModelComplexity.HIGH;
    }

    public boolean isSimpleModel() {
        return complexity == ModelComplexity.LOW;
    }

    public boolean isSuitableForSmallDatasets() {
        return minDataPoints <= 50;
    }

    public boolean requiresHighCompute() {
        return cpuIntensive || gpuAccelerated || memoryRequirementsMb > 4096;
    }

    public String getComplexityDisplayName() {
        return complexity != null ? complexity.getDisplayName() : "Unknown";
    }

    public String getPerformanceSummary() {
        return String.format("Accuracy: %s, Speed: %s, Interpretability: %s",
            accuracy != null ? accuracy : "N/A",
            speed != null ? speed : "N/A",
            interpretability != null ? interpretability : "N/A");
    }

    public boolean canHandleDataSize(int dataPoints) {
        return dataPoints >= minDataPoints;
    }

    public boolean isOptimalForDataSize(int dataPoints) {
        return dataPoints >= optimalDataPoints;
    }
}