package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Comprehensive exploratory data analysis results entity.
 * Contains statistical analysis, pattern detection, and data insights.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "exploratory_data_analytics", indexes = {
    @Index(name = "idx_eda_transformed_data", columnList = "transformed_data_id"),
    @Index(name = "idx_eda_completeness", columnList = "overall_completeness, tenant_id"),
    @Index(name = "idx_eda_quality_score", columnList = "data_quality_score, tenant_id"),
    @Index(name = "idx_eda_analysis_date", columnList = "analysis_completed_at, tenant_id")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class ExploratoryDataAnalytics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_overview", columnDefinition = "JSON")
    private String dataOverview; // JSON containing data overview information

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_quality", columnDefinition = "JSON")
    private String dataQuality; // JSON containing quality metrics

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "temporal_analysis", columnDefinition = "JSON")
    private String temporalAnalysis; // JSON containing time series analysis

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "distributional_analysis", columnDefinition = "JSON")
    private String distributionalAnalysis; // JSON containing distribution analysis

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "categorical_analysis", columnDefinition = "JSON")
    private String categoricalAnalysis; // JSON containing categorical analysis

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "relationship_correlations", columnDefinition = "JSON")
    private String relationshipCorrelations; // JSON containing correlation analysis

    @CreationTimestamp
    @Column(name = "analysis_completed_at", nullable = false)
    private LocalDateTime analysisCompletedAt;

    @Column(name = "overall_completeness", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Overall completeness must be non-negative")
    @DecimalMax(value = "100.0", message = "Overall completeness cannot exceed 100")
    private BigDecimal overallCompleteness;

    @Column(name = "data_quality_score", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Data quality score must be non-negative")
    @DecimalMax(value = "100.0", message = "Data quality score cannot exceed 100")
    private BigDecimal dataQualityScore;

    // Analysis Performance Metrics
    @Column(name = "analysis_duration_seconds")
    @Min(value = 0, message = "Analysis duration cannot be negative")
    private Integer analysisDurationSeconds;

    @Column(name = "memory_usage_mb")
    @Min(value = 0, message = "Memory usage cannot be negative")
    private Integer memoryUsageMb;

    @Column(name = "cpu_usage_percent", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "CPU usage must be non-negative")
    @DecimalMax(value = "100.0", message = "CPU usage cannot exceed 100")
    private BigDecimal cpuUsagePercent;

    // Data Quality Summary Fields (for quick access)
    @Column(name = "missing_values_total")
    @Min(value = 0, message = "Missing values count cannot be negative")
    private Long missingValuesTotal;

    @Column(name = "outliers_total")
    @Min(value = 0, message = "Outliers count cannot be negative")
    private Long outliersTotal;

    @Column(name = "duplicate_records")
    @Min(value = 0, message = "Duplicate records cannot be negative")
    private Long duplicateRecords;

    @Column(name = "invalid_records")
    @Min(value = 0, message = "Invalid records cannot be negative")
    private Long invalidRecords;

    // Statistical Summary Fields
    @Column(name = "numeric_columns_count")
    @Min(value = 0, message = "Numeric columns count cannot be negative")
    private Integer numericColumnsCount;

    @Column(name = "categorical_columns_count")
    @Min(value = 0, message = "Categorical columns count cannot be negative")
    private Integer categoricalColumnsCount;

    @Column(name = "datetime_columns_count")
    @Min(value = 0, message = "DateTime columns count cannot be negative")
    private Integer datetimeColumnsCount;

    // Time Series Analysis Summary
    @Column(name = "seasonality_detected")
    private Boolean seasonalityDetected;

    @Column(name = "trend_detected")
    private Boolean trendDetected;

    @Column(name = "cyclical_patterns")
    private Boolean cyclicalPatterns;

    @Column(name = "anomalies_detected")
    @Min(value = 0, message = "Anomalies detected cannot be negative")
    private Integer anomaliesDetected;

    @Column(name = "time_series_frequency", length = 20)
    @Size(max = 20, message = "Time series frequency cannot exceed 20 characters")
    private String timeSeriesFrequency; // DAILY, WEEKLY, MONTHLY

    // Correlation Analysis Summary
    @Column(name = "high_correlations_count")
    @Min(value = 0, message = "High correlations count cannot be negative")
    private Integer highCorrelationsCount;

    @Column(name = "max_correlation_value", precision = 5, scale = 4)
    @DecimalMin(value = "-1.0", message = "Correlation value must be >= -1.0")
    @DecimalMax(value = "1.0", message = "Correlation value must be <= 1.0")
    private BigDecimal maxCorrelationValue;

    @Column(name = "feature_importance_calculated")
    private Boolean featureImportanceCalculated;

    // Business Insights Summary
    @Column(name = "data_sufficient_for_ml")
    private Boolean dataSufficientForMl;

    @Column(name = "recommended_models", columnDefinition = "TEXT")
    private String recommendedModels; // JSON array of recommended model types

    @Column(name = "data_preprocessing_suggestions", columnDefinition = "TEXT")
    private String dataPreprocessingSuggestions; // JSON array of suggestions

    @Column(name = "analysis_warnings", columnDefinition = "TEXT")
    private String analysisWarnings; // JSON array of warnings

    // Foreign Key Relationships
    @Column(name = "transformed_data_id", nullable = false, unique = true)
    @NotNull(message = "Transformed data reference is required")
    private Long transformedDataId;

    // Global Analysis Configuration
    @Column(name = "analysis_locale", length = 10)
    private String analysisLocale;

    @Column(name = "currency_for_analysis", length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid 3-letter ISO code")
    private String currencyForAnalysis;

    @Column(name = "timezone_for_analysis", length = 50)
    private String timezoneForAnalysis;

    // Audit Fields
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    // Business Logic Methods
    public boolean hasGoodDataQuality() {
        return dataQualityScore != null && 
               dataQualityScore.compareTo(BigDecimal.valueOf(70.0)) >= 0;
    }

    public boolean hasSufficientDataCompleteness() {
        return overallCompleteness != null && 
               overallCompleteness.compareTo(BigDecimal.valueOf(85.0)) >= 0;
    }

    public boolean hasTimeSeriesPatterns() {
        return Boolean.TRUE.equals(seasonalityDetected) || 
               Boolean.TRUE.equals(trendDetected) || 
               Boolean.TRUE.equals(cyclicalPatterns);
    }

    public boolean hasStrongCorrelations() {
        return maxCorrelationValue != null && 
               maxCorrelationValue.abs().compareTo(BigDecimal.valueOf(0.7)) >= 0;
    }

    public boolean isReadyForModeling() {
        return Boolean.TRUE.equals(dataSufficientForMl) && hasGoodDataQuality();
    }

    public double getDataQualityPercentage() {
        return dataQualityScore != null ? dataQualityScore.doubleValue() : 0.0;
    }

    public double getCompletenessPercentage() {
        return overallCompleteness != null ? overallCompleteness.doubleValue() : 0.0;
    }

    public boolean hasSignificantMissingData() {
        if (missingValuesTotal == null) return false;
        // Assuming we have total records count from related entities
        // This would be calculated in the service layer
        return missingValuesTotal > 0;
    }

    public boolean hasExcessiveOutliers() {
        if (outliersTotal == null) return false;
        // This would be calculated as percentage in service layer
        return outliersTotal > 0;
    }

    public boolean requiresDataCleaning() {
        return hasSignificantMissingData() || hasExcessiveOutliers() || 
               (duplicateRecords != null && duplicateRecords > 0) ||
               (invalidRecords != null && invalidRecords > 0);
    }

    public boolean isAnalysisRecent() {
        return analysisCompletedAt != null && 
               analysisCompletedAt.isAfter(LocalDateTime.now().minusHours(24));
    }

    // Analysis Results Getters with Null Safety
    public int getNumericColumnsCount() {
        return numericColumnsCount != null ? numericColumnsCount : 0;
    }

    public int getCategoricalColumnsCount() {
        return categoricalColumnsCount != null ? categoricalColumnsCount : 0;
    }

    public int getDatetimeColumnsCount() {
        return datetimeColumnsCount != null ? datetimeColumnsCount : 0;
    }

    public int getTotalColumnsCount() {
        return getNumericColumnsCount() + getCategoricalColumnsCount() + getDatetimeColumnsCount();
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.seasonalityDetected == null) {
            this.seasonalityDetected = false;
        }
        if (this.trendDetected == null) {
            this.trendDetected = false;
        }
        if (this.cyclicalPatterns == null) {
            this.cyclicalPatterns = false;
        }
        if (this.featureImportanceCalculated == null) {
            this.featureImportanceCalculated = false;
        }
        if (this.dataSufficientForMl == null) {
            this.dataSufficientForMl = false;
        }
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }

    // Custom Validation
    @AssertTrue(message = "Data quality score must be present for completed analysis")
    public boolean isQualityScorePresent() {
        return dataQualityScore != null;
    }

    @AssertTrue(message = "Overall completeness must be present for completed analysis")
    public boolean isCompletenessPresent() {
        return overallCompleteness != null;
    }
}