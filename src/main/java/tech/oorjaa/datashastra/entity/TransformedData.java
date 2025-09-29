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
 * Processed and transformed data entity for ML operations.
 * Contains cleaned, processed data ready for analysis and modeling.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "transformed_data", indexes = {
    @Index(name = "idx_transformed_data_original", columnList = "original_data_id"),
    @Index(name = "idx_transformed_data_quality", columnList = "data_quality_score, tenant_id"),
    @Index(name = "idx_transformed_data_timestamp", columnList = "transformed_at, tenant_id")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class TransformedData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_overview", columnDefinition = "JSON")
    private String dataOverview; // JSON containing data overview information

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_statistics", columnDefinition = "JSON")
    private String dataStatistics; // JSON containing statistical analysis

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_sample", columnDefinition = "JSON")
    private String dataSample; // JSON containing sample data records

    @Column(name = "transformation_log", columnDefinition = "TEXT")
    private String transformationLog; // Processing log details

    @Column(name = "row_count", nullable = false)
    @NotNull(message = "Row count is required")
    @Min(value = 0, message = "Row count cannot be negative")
    private Long rowCount;

    @Column(name = "column_count", nullable = false)
    @NotNull(message = "Column count is required")
    @Min(value = 0, message = "Column count cannot be negative")
    private Integer columnCount;

    @CreationTimestamp
    @Column(name = "transformed_at", nullable = false)
    private LocalDateTime transformedAt;

    @Column(name = "data_quality_score", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Data quality score must be non-negative")
    @DecimalMax(value = "100.0", message = "Data quality score cannot exceed 100")
    private BigDecimal dataQualityScore;

    // Data Processing Metrics
    @Column(name = "processing_time_seconds")
    @Min(value = 0, message = "Processing time cannot be negative")
    private Integer processingTimeSeconds;

    @Column(name = "memory_used_mb")
    @Min(value = 0, message = "Memory usage cannot be negative")
    private Integer memoryUsedMb;

    @Column(name = "records_processed")
    @Min(value = 0, message = "Records processed cannot be negative")
    private Long recordsProcessed;

    @Column(name = "records_filtered")
    @Min(value = 0, message = "Records filtered cannot be negative")
    private Long recordsFiltered;

    @Column(name = "records_modified")
    @Min(value = 0, message = "Records modified cannot be negative")
    private Long recordsModified;

    // Data Quality Metrics
    @Column(name = "missing_values_count")
    @Min(value = 0, message = "Missing values count cannot be negative")
    private Long missingValuesCount;

    @Column(name = "outliers_count")
    @Min(value = 0, message = "Outliers count cannot be negative")
    private Long outliersCount;

    @Column(name = "duplicates_removed")
    @Min(value = 0, message = "Duplicates removed cannot be negative")
    private Long duplicatesRemoved;

    @Column(name = "data_completeness", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Data completeness must be non-negative")
    @DecimalMax(value = "100.0", message = "Data completeness cannot exceed 100")
    private BigDecimal dataCompleteness;

    @Column(name = "data_consistency", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Data consistency must be non-negative")
    @DecimalMax(value = "100.0", message = "Data consistency cannot exceed 100")
    private BigDecimal dataConsistency;

    // Time Series Specific Metrics
    @Column(name = "date_range_start")
    private LocalDateTime dateRangeStart;

    @Column(name = "date_range_end")
    private LocalDateTime dateRangeEnd;

    @Column(name = "time_series_gaps")
    @Min(value = 0, message = "Time series gaps cannot be negative")
    private Integer timeSeriesGaps;

    @Column(name = "seasonal_patterns_detected")
    private Boolean seasonalPatternsDetected;

    @Column(name = "trend_detected")
    private Boolean trendDetected;

    // Foreign Key Relationships
    @Column(name = "original_data_id", nullable = false, unique = true)
    @NotNull(message = "Original data reference is required")
    private Long originalDataId;

    // Transformation Configuration
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "transformation_config", columnDefinition = "JSON")
    private String transformationConfig; // JSON containing transformation parameters

    @Column(name = "transformation_version", length = 20)
    @Size(max = 20, message = "Transformation version cannot exceed 20 characters")
    private String transformationVersion;

    // Global Usage Fields
    @Column(name = "currency_detected", length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid 3-letter ISO code")
    private String currencyDetected;

    @Column(name = "timezone_detected", length = 50)
    private String timezoneDetected;

    @Column(name = "locale_detected", length = 10)
    private String localeDetected;

    // Audit Fields
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // Business Logic Methods
    public boolean hasGoodDataQuality() {
        return dataQualityScore != null && 
               dataQualityScore.compareTo(BigDecimal.valueOf(70.0)) >= 0;
    }

    public boolean hasMinimumDataForForecasting() {
        return rowCount != null && rowCount >= 100; // Minimum 100 records
    }

    public boolean hasSeasonalData() {
        return Boolean.TRUE.equals(seasonalPatternsDetected);
    }

    public boolean hasTrendingData() {
        return Boolean.TRUE.equals(trendDetected);
    }

    public double getDataCompletenessPercentage() {
        return dataCompleteness != null ? dataCompleteness.doubleValue() : 0.0;
    }

    public double getDataConsistencyPercentage() {
        return dataConsistency != null ? dataConsistency.doubleValue() : 0.0;
    }

    public long getValidRecordsCount() {
        return rowCount != null && recordsFiltered != null ? 
               rowCount - recordsFiltered : 
               (rowCount != null ? rowCount : 0);
    }

    public boolean hasTimeSeriesData() {
        return dateRangeStart != null && dateRangeEnd != null;
    }

    public long getTimeSeriesDurationDays() {
        if (hasTimeSeriesData()) {
            return java.time.Duration.between(dateRangeStart, dateRangeEnd).toDays();
        }
        return 0;
    }

    public boolean hasSufficientTimeSeriesData() {
        return getTimeSeriesDurationDays() >= 90; // Minimum 3 months of data
    }

    public double getOutlierPercentage() {
        if (rowCount == null || rowCount == 0 || outliersCount == null) {
            return 0.0;
        }
        return (outliersCount.doubleValue() / rowCount.doubleValue()) * 100.0;
    }

    public boolean hasExcessiveOutliers() {
        return getOutlierPercentage() > 10.0; // More than 10% outliers
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.seasonalPatternsDetected == null) {
            this.seasonalPatternsDetected = false;
        }
        if (this.trendDetected == null) {
            this.trendDetected = false;
        }
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    // Custom Validation
    @AssertTrue(message = "End date must be after start date")
    public boolean isDateRangeValid() {
        return dateRangeStart == null || dateRangeEnd == null || 
               dateRangeEnd.isAfter(dateRangeStart);
    }

    @AssertTrue(message = "Records filtered cannot exceed total records")
    public boolean isRecordsFilteredValid() {
        return recordsFiltered == null || rowCount == null || 
               recordsFiltered <= rowCount;
    }

    @AssertTrue(message = "Quality score must be set for processed data")
    public boolean isQualityScorePresent() {
        return dataQualityScore != null;
    }
}