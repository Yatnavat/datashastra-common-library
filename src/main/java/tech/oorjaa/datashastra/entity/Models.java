package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tech.oorjaa.datashastra.enums.ModelComplexity;
import tech.oorjaa.datashastra.enums.ModelType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * System entity representing available machine learning models for forecasting.
 * Contains model metadata, capabilities, and configuration information.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "models", indexes = {
    @Index(name = "idx_models_type_active", columnList = "model_type, is_active"),
    @Index(name = "idx_models_recommended", columnList = "is_recommended, is_active"),
    @Index(name = "idx_models_complexity", columnList = "complexity, is_active")
})
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"forecasts"})
@ToString(exclude = {"forecasts"})
public class Models implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name", nullable = false, length = 100)
    @NotBlank(message = "Model name is required")
    @Size(max = 100, message = "Model name cannot exceed 100 characters")
    private String modelName;

    @Column(name = "model_description", length = 1000)
    @Size(max = 1000, message = "Model description cannot exceed 1000 characters")
    private String modelDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "model_type", nullable = false, length = 20)
    @NotNull(message = "Model type is required")
    private ModelType modelType;

    @Column(name = "model_icon", length = 255)
    @Size(max = 255, message = "Model icon URL cannot exceed 255 characters")
    private String modelIcon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Complexity is required")
    private ModelComplexity complexity;

    @Column(length = 50)
    @Size(max = 50, message = "Accuracy tier cannot exceed 50 characters")
    private String accuracy; // "GOOD", "BETTER", "BEST"

    @Column(name = "estimated_time", length = 50)
    @Size(max = 50, message = "Estimated time cannot exceed 50 characters")
    private String estimatedTime; // "FAST", "MEDIUM", "SLOW"

    @Column(name = "best_for", length = 500)
    @Size(max = 500, message = "Best for description cannot exceed 500 characters")
    private String bestFor;

    @Column(name = "is_recommended", nullable = false)
    @NotNull
    private Boolean isRecommended = false;

    @Column(name = "is_active", nullable = false)
    @NotNull
    private Boolean isActive = true;

    // Model Capabilities
    @Column(name = "min_data_points", nullable = false)
    @Min(value = 1, message = "Minimum data points must be at least 1")
    private Integer minDataPoints;

    @Column(name = "handles_seasonality", nullable = false)
    @NotNull
    private Boolean handlesSeasonality = false;

    @Column(name = "handles_missing_values", nullable = false)
    @NotNull
    private Boolean handlesMissingValues = false;

    @Column(name = "handles_nonlinearity", nullable = false)
    @NotNull
    private Boolean handlesNonlinearity = false;

    @Column(name = "requires_feature_engineering", nullable = false)
    @NotNull
    private Boolean requiresFeatureEngineering = false;

    // Performance Characteristics
    @Column(name = "training_time_minutes")
    @Min(value = 1, message = "Training time must be positive")
    private Integer trainingTimeMinutes;

    @Column(name = "memory_requirements_mb")
    @Min(value = 1, message = "Memory requirements must be positive")
    private Integer memoryRequirementsMb;

    @Column(name = "cpu_intensive", nullable = false)
    @NotNull
    private Boolean cpuIntensive = false;

    @Column(name = "gpu_accelerated", nullable = false)
    @NotNull
    private Boolean gpuAccelerated = false;

    // Model Configuration
    @Column(name = "default_hyperparameters", columnDefinition = "TEXT")
    private String defaultHyperparameters; // JSON string

    @Column(name = "parameter_ranges", columnDefinition = "TEXT")
    private String parameterRanges; // JSON string for parameter tuning

    @Column(name = "supported_features", columnDefinition = "TEXT")
    private String supportedFeatures; // JSON array of supported feature types

    // Global Support
    @Column(name = "supports_multi_currency", nullable = false)
    @NotNull
    private Boolean supportsMultiCurrency = false;

    @Column(name = "supports_multi_timezone", nullable = false)
    @NotNull
    private Boolean supportsMultiTimezone = true;

    // Audit Fields
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Relationships
    @OneToMany(mappedBy = "modelId", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<Forecast> forecasts = new ArrayList<>();

    // Business Logic Methods
    public boolean isAvailable() {
        return isActive != null && isActive;
    }

    public boolean isRecommendedForUse() {
        return isAvailable() && isRecommended != null && isRecommended;
    }

    public boolean canHandleDataSize(int dataPoints) {
        return minDataPoints == null || dataPoints >= minDataPoints;
    }

    public boolean requiresGpu() {
        return gpuAccelerated != null && gpuAccelerated;
    }

    public int getEstimatedTrainingTimeMinutes() {
        return trainingTimeMinutes != null ? trainingTimeMinutes : 60; // Default to 1 hour
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = true;
        }
        if (this.isRecommended == null) {
            this.isRecommended = false;
        }
        if (this.handlesSeasonality == null) {
            this.handlesSeasonality = false;
        }
        if (this.handlesMissingValues == null) {
            this.handlesMissingValues = false;
        }
        if (this.handlesNonlinearity == null) {
            this.handlesNonlinearity = false;
        }
        if (this.requiresFeatureEngineering == null) {
            this.requiresFeatureEngineering = false;
        }
    }
}