package tech.oorjaa.datashastra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for ValidationResults entity.
 * Contains validation errors, warnings, and quality metrics for uploaded data.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationResultsDto implements Serializable {

    private Long id;

    @NotNull(message = "Total rows count is required")
    @Min(value = 0, message = "Total rows cannot be negative")
    private Long totalRows;

    @NotNull(message = "Valid rows count is required")
    @Min(value = 0, message = "Valid rows cannot be negative")
    private Long validRows;

    @NotNull(message = "Missing values count is required")
    @Min(value = 0, message = "Missing values cannot be negative")
    private Long missingValues;

    @NotNull(message = "Detected outliers count is required")
    @Min(value = 0, message = "Detected outliers cannot be negative")
    private Long detectedOutliers;

    private String outlierColumns;
    private String additionalColumns;

    @NotNull(message = "Quality score is required")
    @DecimalMin(value = "0.0", message = "Quality score must be non-negative")
    @DecimalMax(value = "100.0", message = "Quality score cannot exceed 100")
    private BigDecimal qualityScore;

    @DecimalMin(value = "0.0", message = "Completeness score must be non-negative")
    @DecimalMax(value = "100.0", message = "Completeness score cannot exceed 100")
    private BigDecimal completenessScore;

    @DecimalMin(value = "0.0", message = "Consistency score must be non-negative")
    @DecimalMax(value = "100.0", message = "Consistency score cannot exceed 100")
    private BigDecimal consistencyScore;

    @DecimalMin(value = "0.0", message = "Accuracy score must be non-negative")
    @DecimalMax(value = "100.0", message = "Accuracy score cannot exceed 100")
    private BigDecimal accuracyScore;

    @NotNull(message = "Validation status is required")
    private Boolean isValid;

    private String validationErrors;
    private String validationWarnings;
    private LocalDateTime validatedAt;

    @Min(value = 0, message = "Validation duration cannot be negative")
    private Integer validationDurationMs;

    // Schema Validation Results
    private Boolean requiredColumnsPresent;
    private String missingRequiredColumns;
    private String unexpectedColumns;

    @Min(value = 0, message = "Data type errors cannot be negative")
    private Integer dataTypeErrors;

    @Min(value = 0, message = "Format errors cannot be negative")
    private Integer formatErrors;

    // Business Rule Validation
    @Min(value = 0, message = "Business rule violations cannot be negative")
    private Integer businessRuleViolations;

    private Boolean dateRangeValid;
    private LocalDateTime minDate;
    private LocalDateTime maxDate;

    @Min(value = 0, message = "SKU count cannot be negative")
    private Integer skuCount;

    @Min(value = 0, message = "Depot count cannot be negative")
    private Integer depotCount;

    private Boolean exceedsSkuLimit;
    private Boolean exceedsDepotLimit;

    // Statistical Validation
    @Min(value = 0, message = "Zero demand records cannot be negative")
    private Long zeroDemandRecords;

    @Min(value = 0, message = "Negative demand records cannot be negative")
    private Long negativeDemandRecords;

    @Min(value = 0, message = "Extreme values detected cannot be negative")
    private Integer extremeValuesDetected;

    @Min(value = 0, message = "Seasonal gaps detected cannot be negative")
    private Integer seasonalGapsDetected;

    // Foreign Key References
    @NotNull(message = "Uploaded data reference is required")
    private Long uploadedDataId;

    // Processing Information
    @Size(max = 20, message = "Validation engine version cannot exceed 20 characters")
    private String validationEngineVersion;

    private String validationConfig;

    // Global Validation Context
    private String validationLocale;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid 3-letter ISO code")
    private String expectedCurrency;

    private String expectedTimezone;

    // Audit Fields
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer tenantId;

    // UI Display Fields
    private String qualityScoreBadge;
    private String validationStatusBadge;
    private String validRowsPercentageText;
    private String missingValuesPercentageText;
    private String outlierPercentageText;
    private Boolean showWarnings;
    private Boolean showErrors;

    // Computed Fields
    private Boolean passesQualityThreshold;
    private Boolean hasAcceptableCompleteness;
    private Boolean hasMinimumDataForForecasting;
    private Boolean exceedsBusinessLimits;
    private Boolean hasSchemaViolations;
    private Boolean hasBusinessRuleIssues;
    private Boolean hasDataQualityIssues;
    private Boolean isReadyForProcessing;
    private Boolean requiresDataCleaning;
    private Boolean hasTimeSeriesData;
    private Boolean hasSufficientTimeRange;
    private Long timeRangeDays;

    // Business Logic Methods
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

    public String getQualityScoreLevel() {
        if (qualityScore == null) return "Unknown";
        double score = qualityScore.doubleValue();
        if (score >= 90) return "Excellent";
        if (score >= 80) return "Good";
        if (score >= 70) return "Fair";
        if (score >= 60) return "Poor";
        return "Very Poor";
    }

    public String getValidationSummary() {
        if (Boolean.TRUE.equals(isValid)) {
            return String.format("Validation passed with %s quality score", getQualityScoreLevel().toLowerCase());
        } else {
            return "Validation failed - see errors and warnings for details";
        }
    }

    public boolean hasIssues() {
        return Boolean.TRUE.equals(showErrors) || Boolean.TRUE.equals(showWarnings);
    }

    public String getTimeRangeDisplay() {
        if (minDate == null || maxDate == null) return "No date range";
        return String.format("%s to %s (%d days)", 
            minDate.toLocalDate(), 
            maxDate.toLocalDate(), 
            timeRangeDays != null ? timeRangeDays : 0);
    }
}