package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Feature engineering and selection configuration entity.
 * Stores configuration for temporal, lag, rolling, external, and advanced features.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "feature_configuration", indexes = {
    @Index(name = "idx_feature_config_forecast", columnList = "forecast_id"),
    @Index(name = "idx_feature_config_count", columnList = "selected_feature_count, tenant_id"),
    @Index(name = "idx_feature_config_training_time", columnList = "estimated_training_time, tenant_id")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class FeatureConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temporal_features", columnDefinition = "JSON")
    private String temporalFeatures; // JSON containing temporal feature configuration

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "lag_features", columnDefinition = "JSON")
    private String lagFeatures; // JSON containing lag feature configuration

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rolling_features", columnDefinition = "JSON")
    private String rollingFeatures; // JSON containing rolling window feature configuration

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "external_features", columnDefinition = "JSON")
    private String externalFeatures; // JSON containing external data feature configuration

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "advanced_features", columnDefinition = "JSON")
    private String advancedFeatures; // JSON containing advanced feature engineering

    @Column(name = "selected_feature_count", nullable = false)
    @NotNull(message = "Selected feature count is required")
    @Min(value = 1, message = "At least 1 feature must be selected")
    @Max(value = 50, message = "Maximum 50 features allowed for performance")
    private Integer selectedFeatureCount;

    @Column(name = "estimated_training_time", nullable = false)
    @NotNull(message = "Estimated training time is required")
    @Min(value = 1, message = "Training time must be at least 1 minute")
    private Integer estimatedTrainingTime; // in minutes

    @Column(name = "feature_importance_threshold", precision = 3, scale = 2)
    @DecimalMin(value = "0.0", message = "Feature importance threshold must be non-negative")
    @DecimalMax(value = "1.0", message = "Feature importance threshold cannot exceed 1.0")
    private BigDecimal featureImportanceThreshold;

    // Feature Category Counts (for quick analysis)
    @Column(name = "temporal_feature_count")
    @Min(value = 0, message = "Temporal feature count cannot be negative")
    private Integer temporalFeatureCount = 0;

    @Column(name = "lag_feature_count")
    @Min(value = 0, message = "Lag feature count cannot be negative")
    private Integer lagFeatureCount = 0;

    @Column(name = "rolling_feature_count")
    @Min(value = 0, message = "Rolling feature count cannot be negative")
    private Integer rollingFeatureCount = 0;

    @Column(name = "external_feature_count")
    @Min(value = 0, message = "External feature count cannot be negative")
    private Integer externalFeatureCount = 0;

    @Column(name = "advanced_feature_count")
    @Min(value = 0, message = "Advanced feature count cannot be negative")
    private Integer advancedFeatureCount = 0;

    // Feature Selection Metadata
    @Column(name = "auto_feature_selection")
    private Boolean autoFeatureSelection = false;

    @Column(name = "feature_selection_method", length = 50)
    @Size(max = 50, message = "Feature selection method cannot exceed 50 characters")
    private String featureSelectionMethod; // CORRELATION, MUTUAL_INFO, FEATURE_IMPORTANCE, etc.

    @Column(name = "dimensionality_reduction")
    private Boolean dimensionalityReduction = false;

    @Column(name = "reduction_method", length = 30)
    @Size(max = 30, message = "Reduction method cannot exceed 30 characters")
    private String reductionMethod; // PCA, LDA, etc.

    @Column(name = "target_dimensions")
    @Min(value = 1, message = "Target dimensions must be positive")
    @Max(value = 20, message = "Target dimensions cannot exceed 20")
    private Integer targetDimensions;

    // Feature Engineering Parameters
    @Column(name = "normalize_features")
    private Boolean normalizeFeatures = true;

    @Column(name = "handle_missing_values")
    private Boolean handleMissingValues = true;

    @Column(name = "missing_value_strategy", length = 20)
    @Size(max = 20, message = "Missing value strategy cannot exceed 20 characters")
    private String missingValueStrategy; // MEAN, MEDIAN, MODE, FORWARD_FILL, etc.

    @Column(name = "outlier_treatment")
    private Boolean outlierTreatment = false;

    @Column(name = "outlier_method", length = 20)
    @Size(max = 20, message = "Outlier method cannot exceed 20 characters")
    private String outlierMethod; // IQR, Z_SCORE, ISOLATION_FOREST, etc.

    // Performance Configuration
    @Column(name = "feature_computation_parallel")
    private Boolean featureComputationParallel = true;

    @Column(name = "max_memory_usage_mb")
    @Min(value = 100, message = "Minimum memory usage is 100MB")
    @Max(value = 8192, message = "Maximum memory usage is 8GB")
    private Integer maxMemoryUsageMb = 1024;

    @Column(name = "cache_features")
    private Boolean cacheFeatures = true;

    @Column(name = "feature_cache_ttl_hours")
    @Min(value = 1, message = "Cache TTL must be at least 1 hour")
    @Max(value = 168, message = "Cache TTL cannot exceed 1 week")
    private Integer featureCacheTtlHours = 24;

    // Validation and Quality
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "feature_validation_rules", columnDefinition = "JSON")
    private String featureValidationRules; // JSON containing validation rules

    @Column(name = "min_feature_variance", precision = 10, scale = 8)
    @DecimalMin(value = "0.0", message = "Minimum feature variance must be non-negative")
    private BigDecimal minFeatureVariance;

    @Column(name = "max_feature_correlation", precision = 3, scale = 2)
    @DecimalMin(value = "0.0", message = "Maximum feature correlation must be non-negative")
    @DecimalMax(value = "1.0", message = "Maximum feature correlation cannot exceed 1.0")
    private BigDecimal maxFeatureCorrelation;

    // Foreign Key Relationships
    @Column(name = "forecast_id", nullable = false, unique = true)
    @NotNull(message = "Forecast reference is required")
    private Long forecastId;

    // Global Configuration
    @Column(name = "locale_aware_features")
    private Boolean localeAwareFeatures = false;

    @Column(name = "timezone_features")
    private Boolean timezoneFeatures = false;

    @Column(name = "currency_features")
    private Boolean currencyFeatures = false;

    @Column(name = "holiday_calendar", length = 10)
    @Size(max = 10, message = "Holiday calendar cannot exceed 10 characters")
    private String holidayCalendar; // US, EU, JP, etc.

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

    // Business Logic Methods
    public boolean hasMinimumFeatures() {
        return selectedFeatureCount != null && selectedFeatureCount >= 3;
    }

    public boolean hasRecommendedFeatureCount() {
        return selectedFeatureCount != null && selectedFeatureCount >= 6 && selectedFeatureCount <= 20;
    }

    public boolean requiresHighMemory() {
        return selectedFeatureCount != null && selectedFeatureCount > 20;
    }

    public boolean hasTemporalFeatures() {
        return temporalFeatureCount != null && temporalFeatureCount > 0;
    }

    public boolean hasLagFeatures() {
        return lagFeatureCount != null && lagFeatureCount > 0;
    }

    public boolean hasExternalDataFeatures() {
        return externalFeatureCount != null && externalFeatureCount > 0;
    }

    public boolean isHighComplexity() {
        return Boolean.TRUE.equals(dimensionalityReduction) || 
               (advancedFeatureCount != null && advancedFeatureCount > 5);
    }

    public int getTotalFeatureCount() {
        return (temporalFeatureCount != null ? temporalFeatureCount : 0) +
               (lagFeatureCount != null ? lagFeatureCount : 0) +
               (rollingFeatureCount != null ? rollingFeatureCount : 0) +
               (externalFeatureCount != null ? externalFeatureCount : 0) +
               (advancedFeatureCount != null ? advancedFeatureCount : 0);
    }

    public boolean isBalancedFeatureSet() {
        int total = getTotalFeatureCount();
        return total > 0 && hasTemporalFeatures() && hasLagFeatures();
    }

    public String getComplexityLevel() {
        if (isHighComplexity()) return "High";
        if (selectedFeatureCount != null && selectedFeatureCount > 10) return "Medium";
        return "Low";
    }

    public boolean needsFeatureSelection() {
        return selectedFeatureCount != null && selectedFeatureCount > 30;
    }

    public boolean shouldUseParallelProcessing() {
        return Boolean.TRUE.equals(featureComputationParallel) && requiresHighMemory();
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.autoFeatureSelection == null) {
            this.autoFeatureSelection = false;
        }
        if (this.dimensionalityReduction == null) {
            this.dimensionalityReduction = false;
        }
        if (this.normalizeFeatures == null) {
            this.normalizeFeatures = true;
        }
        if (this.handleMissingValues == null) {
            this.handleMissingValues = true;
        }
        if (this.outlierTreatment == null) {
            this.outlierTreatment = false;
        }
        if (this.featureComputationParallel == null) {
            this.featureComputationParallel = true;
        }
        if (this.cacheFeatures == null) {
            this.cacheFeatures = true;
        }
        if (this.localeAwareFeatures == null) {
            this.localeAwareFeatures = false;
        }
        if (this.timezoneFeatures == null) {
            this.timezoneFeatures = false;
        }
        if (this.currencyFeatures == null) {
            this.currencyFeatures = false;
        }

        // Calculate total if individual counts are set
        updateSelectedFeatureCount();
    }

    @PreUpdate
    public void preUpdate() {
        updateSelectedFeatureCount();
    }

    private void updateSelectedFeatureCount() {
        if (selectedFeatureCount == null || selectedFeatureCount == 0) {
            selectedFeatureCount = getTotalFeatureCount();
        }
    }

    // Custom Validation
    @AssertTrue(message = "Selected feature count must match sum of individual feature counts")
    public boolean isFeatureCountConsistent() {
        if (selectedFeatureCount == null) return false;
        int calculatedTotal = getTotalFeatureCount();
        return calculatedTotal == 0 || Math.abs(selectedFeatureCount - calculatedTotal) <= 2; // Allow small variance
    }

    @AssertTrue(message = "Dimensionality reduction requires target dimensions")
    public boolean isDimensionalityReductionConfigValid() {
        return !Boolean.TRUE.equals(dimensionalityReduction) || 
               (targetDimensions != null && reductionMethod != null);
    }

    @AssertTrue(message = "Auto feature selection requires selection method")
    public boolean isAutoFeatureSelectionConfigValid() {
        return !Boolean.TRUE.equals(autoFeatureSelection) || featureSelectionMethod != null;
    }
}