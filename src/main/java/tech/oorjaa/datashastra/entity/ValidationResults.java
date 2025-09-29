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
 * Data validation and quality assessment results entity.
 * Contains validation errors, warnings, and quality metrics for uploaded data.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "validation_results", indexes = {
    @Index(name = "idx_validation_uploaded_data", columnList = "uploaded_data_id"),
    @Index(name = "idx_validation_quality_score", columnList = "quality_score, tenant_id"),
    @Index(name = "idx_validation_status", columnList = "is_valid, tenant_id"),
    @Index(name = "idx_validation_timestamp", columnList = "validated_at, tenant_id")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class ValidationResults implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "total_rows", nullable = false)
    @NotNull(message = "Total rows count is required")
    @Min(value = 0, message = "Total rows cannot be negative")
    private Long totalRows;

    @Column(name = "valid_rows", nullable = false)
    @NotNull(message = "Valid rows count is required")
    @Min(value = 0, message = "Valid rows cannot be negative")
    private Long validRows;

    @Column(name = "missing_values", nullable = false)
    @NotNull(message = "Missing values count is required")
    @Min(value = 0, message = "Missing values cannot be negative")
    private Long missingValues;

    @Column(name = "detected_outliers", nullable = false)
    @NotNull(message = "Detected outliers count is required")
    @Min(value = 0, message = "Detected outliers cannot be negative")
    private Long detectedOutliers;

    @Column(name = "outlier_columns", columnDefinition = "TEXT")
    private String outlierColumns; // Comma-separated column names

    @Column(name = "additional_columns", columnDefinition = "TEXT")
    private String additionalColumns; // Extra columns found

    @Column(name = "quality_score", precision = 5, scale = 2, nullable = false)
    @NotNull(message = "Quality score is required")
    @DecimalMin(value = "0.0", message = "Quality score must be non-negative")
    @DecimalMax(value = "100.0", message = "Quality score cannot exceed 100")
    private BigDecimal qualityScore;

    @Column(name = "completeness_score", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Completeness score must be non-negative")
    @DecimalMax(value = "100.0", message = "Completeness score cannot exceed 100")
    private BigDecimal completenessScore;

    @Column(name = "consistency_score", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Consistency score must be non-negative")
    @DecimalMax(value = "100.0", message = "Consistency score cannot exceed 100")
    private BigDecimal consistencyScore;

    @Column(name = "accuracy_score", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Accuracy score must be non-negative")
    @DecimalMax(value = "100.0", message = "Accuracy score cannot exceed 100")
    private BigDecimal accuracyScore;

    @Column(name = "is_valid", nullable = false)
    @NotNull(message = "Validation status is required")
    private Boolean isValid;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "validation_errors", columnDefinition = "JSON")
    private String validationErrors; // JSON array of error details

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "validation_warnings", columnDefinition = "JSON")
    private String validationWarnings; // JSON array of warning details

    @CreationTimestamp
    @Column(name = "validated_at", nullable = false)
    private LocalDateTime validatedAt;

    @Column(name = "validation_duration_ms")
    @Min(value = 0, message = "Validation duration cannot be negative")
    private Integer validationDurationMs;

    // Schema Validation Results
    @Column(name = "required_columns_present")
    private Boolean requiredColumnsPresent;

    @Column(name = "missing_required_columns", columnDefinition = "TEXT")
    private String missingRequiredColumns; // Comma-separated column names

    @Column(name = "unexpected_columns", columnDefinition = "TEXT")
    private String unexpectedColumns; // Comma-separated column names

    @Column(name = "data_type_errors")
    @Min(value = 0, message = "Data type errors cannot be negative")
    private Integer dataTypeErrors;

    @Column(name = "format_errors")
    @Min(value = 0, message = "Format errors cannot be negative")
    private Integer formatErrors;

    // Business Rule Validation
    @Column(name = "business_rule_violations")
    @Min(value = 0, message = "Business rule violations cannot be negative")
    private Integer businessRuleViolations;

    @Column(name = "date_range_valid")
    private Boolean dateRangeValid;

    @Column(name = "min_date")
    private LocalDateTime minDate;

    @Column(name = "max_date")
    private LocalDateTime maxDate;

    @Column(name = "sku_count")
    @Min(value = 0, message = "SKU count cannot be negative")
    private Integer skuCount;

    @Column(name = "depot_count")
    @Min(value = 0, message = "Depot count cannot be negative")
    private Integer depotCount;

    @Column(name = "exceeds_sku_limit")
    private Boolean exceedsSkuLimit;

    @Column(name = "exceeds_depot_limit")
    private Boolean exceedsDepotLimit;

    // Statistical Validation
    @Column(name = "zero_demand_records")
    @Min(value = 0, message = "Zero demand records cannot be negative")
    private Long zeroDemandRecords;

    @Column(name = "negative_demand_records")
    @Min(value = 0, message = "Negative demand records cannot be negative")
    private Long negativeDemandRecords;

    @Column(name = "extreme_values_detected")
    @Min(value = 0, message = "Extreme values detected cannot be negative")
    private Integer extremeValuesDetected;

    @Column(name = "seasonal_gaps_detected")
    @Min(value = 0, message = "Seasonal gaps detected cannot be negative")
    private Integer seasonalGapsDetected;

    // Foreign Key Relationships
    @Column(name = "uploaded_data_id", nullable = false, unique = true)
    @NotNull(message = "Uploaded data reference is required")
    private Long uploadedDataId;

    // Processing Information
    @Column(name = "validation_engine_version", length = 20)
    @Size(max = 20, message = "Validation engine version cannot exceed 20 characters")
    private String validationEngineVersion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "validation_config", columnDefinition = "JSON")
    private String validationConfig; // JSON containing validation parameters

    // Global Validation Context
    @Column(name = "validation_locale", length = 10)
    private String validationLocale;

    @Column(name = "expected_currency", length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid 3-letter ISO code")
    private String expectedCurrency;

    @Column(name = "expected_timezone", length = 50)
    private String expectedTimezone;

    // Audit Fields
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    // Business Logic Methods
    public boolean passesQualityThreshold() {
        return qualityScore != null && qualityScore.compareTo(BigDecimal.valueOf(70.0)) >= 0;
    }

    public boolean hasAcceptableCompleteness() {
        return completenessScore != null && completenessScore.compareTo(BigDecimal.valueOf(85.0)) >= 0;
    }

    public boolean hasMinimumDataForForecasting() {
        return totalRows != null && totalRows >= 100; // Minimum 100 records
    }

    public boolean exceedsBusinessLimits() {
        return Boolean.TRUE.equals(exceedsSkuLimit) || Boolean.TRUE.equals(exceedsDepotLimit);
    }

    public boolean hasSchemaViolations() {
        return Boolean.FALSE.equals(requiredColumnsPresent) || 
               (dataTypeErrors != null && dataTypeErrors > 0) ||
               (formatErrors != null && formatErrors > 0);
    }

    public boolean hasBusinessRuleIssues() {
        return (businessRuleViolations != null && businessRuleViolations > 0) ||
               Boolean.FALSE.equals(dateRangeValid) ||
               (negativeDemandRecords != null && negativeDemandRecords > 0);
    }

    public boolean hasDataQualityIssues() {
        return (extremeValuesDetected != null && extremeValuesDetected > 0) ||
               (seasonalGapsDetected != null && seasonalGapsDetected > 0) ||
               (zeroDemandRecords != null && zeroDemandRecords > totalRows / 2);
    }

    public double getValidRowsPercentage() {
        if (totalRows == null || totalRows == 0) return 0.0;
        return validRows != null ? (validRows.doubleValue() / totalRows.doubleValue()) * 100.0 : 0.0;
    }

    public double getMissingValuesPercentage() {
        if (totalRows == null || totalRows == 0) return 0.0;
        return missingValues != null ? (missingValues.doubleValue() / totalRows.doubleValue()) * 100.0 : 0.0;
    }

    public double getOutlierPercentage() {
        if (totalRows == null || totalRows == 0) return 0.0;
        return detectedOutliers != null ? (detectedOutliers.doubleValue() / totalRows.doubleValue()) * 100.0 : 0.0;
    }

    public boolean isReadyForProcessing() {
        return Boolean.TRUE.equals(isValid) && 
               passesQualityThreshold() && 
               hasMinimumDataForForecasting() && 
               !exceedsBusinessLimits();
    }

    public boolean requiresDataCleaning() {
        return getMissingValuesPercentage() > 10.0 || 
               getOutlierPercentage() > 5.0 || 
               hasDataQualityIssues();
    }

    public boolean hasTimeSeriesData() {
        return minDate != null && maxDate != null;
    }

    public long getTimeRangeDays() {
        if (!hasTimeSeriesData()) return 0;
        return java.time.Duration.between(minDate, maxDate).toDays();
    }

    public boolean hasSufficientTimeRange() {
        return getTimeRangeDays() >= 90; // Minimum 3 months
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.isValid == null) {
            this.isValid = false;
        }
        if (this.requiredColumnsPresent == null) {
            this.requiredColumnsPresent = false;
        }
        if (this.dateRangeValid == null) {
            this.dateRangeValid = false;
        }
        if (this.exceedsSkuLimit == null) {
            this.exceedsSkuLimit = false;
        }
        if (this.exceedsDepotLimit == null) {
            this.exceedsDepotLimit = false;
        }
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }

        // Calculate validity based on quality score and business rules
        updateValidityStatus();
    }

    private void updateValidityStatus() {
        this.isValid = passesQualityThreshold() && 
                      !hasSchemaViolations() && 
                      !exceedsBusinessLimits() &&
                      hasMinimumDataForForecasting();
    }

    // Custom Validation
    @AssertTrue(message = "Valid rows cannot exceed total rows")
    public boolean isValidRowsCountValid() {
        return validRows == null || totalRows == null || validRows <= totalRows;
    }

    @AssertTrue(message = "SKU count must not exceed limit of 5")
    public boolean isSkuCountWithinLimit() {
        return skuCount == null || skuCount <= 5;
    }

    @AssertTrue(message = "Depot count must not exceed limit of 5")
    public boolean isDepotCountWithinLimit() {
        return depotCount == null || depotCount <= 5;
    }

    @AssertTrue(message = "End date must be after start date")
    public boolean isDateRangeConsistent() {
        return minDate == null || maxDate == null || maxDate.isAfter(minDate);
    }
}