package tech.oorjaa.datashastra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for TransformedData entity.
 * Represents processed and transformed data ready for modeling.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransformedDataDto implements Serializable {

    private Long id;

    @NotBlank(message = "Transformation name is required")
    @Size(max = 200, message = "Transformation name cannot exceed 200 characters")
    private String transformationName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Original data rows count is required")
    @Min(value = 0, message = "Original data rows cannot be negative")
    private Long originalDataRows;

    @NotNull(message = "Transformed data rows count is required")
    @Min(value = 0, message = "Transformed data rows cannot be negative")
    private Long transformedDataRows;

    @NotNull(message = "Original columns count is required")
    @Min(value = 0, message = "Original columns cannot be negative")
    private Integer originalColumns;

    @NotNull(message = "Transformed columns count is required")
    @Min(value = 0, message = "Transformed columns cannot be negative")
    private Integer transformedColumns;

    @NotNull(message = "Processing status is required")
    private Boolean isProcessed;

    private LocalDateTime processingStartedAt;
    private LocalDateTime processingCompletedAt;

    @Min(value = 0, message = "Processing duration cannot be negative")
    private Long processingDurationMs;

    private String processingError;

    @Min(value = 0, message = "Processing attempts cannot be negative")
    private Integer processingAttempts;

    // Data Quality Metrics
    @DecimalMin(value = "0.0", message = "Data quality score must be non-negative")
    @DecimalMax(value = "100.0", message = "Data quality score cannot exceed 100")
    private BigDecimal dataQualityScore;

    @DecimalMin(value = "0.0", message = "Completeness percentage must be non-negative")
    @DecimalMax(value = "100.0", message = "Completeness percentage cannot exceed 100")
    private BigDecimal completenessPercentage;

    @Min(value = 0, message = "Missing values count cannot be negative")
    private Long missingValues;

    @Min(value = 0, message = "Outliers removed cannot be negative")
    private Integer outliersRemoved;

    @Min(value = 0, message = "Duplicates removed cannot be negative")
    private Integer duplicatesRemoved;

    // Transformation Details
    private String transformationSteps; // JSON array of transformation steps
    private String appliedFilters; // JSON of filters applied
    private String featureEngineering; // JSON of feature engineering applied
    private String aggregationRules; // JSON of aggregation rules
    private String cleaningOperations; // JSON of data cleaning operations

    // File Storage Information
    @NotBlank(message = "Output file path is required")
    private String outputFilePath;

    @Min(value = 0, message = "Output file size cannot be negative")
    private Long outputFileSize;

    @Size(max = 100, message = "Output format cannot exceed 100 characters")
    private String outputFormat;

    @Size(max = 64, message = "File checksum cannot exceed 64 characters")
    private String fileChecksum;

    // Schema Information
    private String outputSchema; // JSON schema of transformed data
    private String columnMappings; // JSON mapping original to transformed columns
    private String dataTypes; // JSON of column data types
    private String validationRules; // JSON of validation rules applied

    // Statistical Summary
    private String statisticalSummary; // JSON of statistical metrics
    private LocalDateTime dataStartDate;
    private LocalDateTime dataEndDate;

    @Min(value = 0, message = "Time series records cannot be negative")
    private Long timeSeriesRecords;

    @Min(value = 0, message = "Unique SKUs cannot be negative")
    private Integer uniqueSkus;

    @Min(value = 0, message = "Unique locations cannot be negative")
    private Integer uniqueLocations;

    // Business Context
    @Size(max = 10, message = "Currency code cannot exceed 10 characters")
    private String dataCurrency;

    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    private String dataTimezone;

    @Size(max = 10, message = "Locale cannot exceed 10 characters")
    private String dataLocale;

    // Version Control
    @NotBlank(message = "Version is required")
    @Size(max = 20, message = "Version cannot exceed 20 characters")
    private String version;

    @Size(max = 50, message = "Transformation engine version cannot exceed 50 characters")
    private String transformationEngineVersion;

    // Foreign Key References
    @NotNull(message = "Uploaded data reference is required")
    private Long uploadedDataId;

    @NotNull(message = "Activity reference is required")
    private Long activityId;

    // Audit Fields
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
    private String createdBy;
    private String modifiedBy;
    private Integer tenantId;

    // UI Display Fields
    private String processingStatusBadge;
    private String qualityScoreBadge;
    private String fileSizeDisplay;
    private String processingTimeDisplay;
    private String dataRangeDisplay;
    private Boolean showErrors;
    private Boolean showWarnings;
    private Boolean canReprocess;
    private Boolean canDownload;

    // Computed Fields
    private Boolean hasErrors;
    private Boolean hasWarnings;
    private Boolean isReadyForModeling;
    private Double rowsChangedPercentage;
    private Double columnsChangedPercentage;
    private Boolean hasSignificantChanges;

    // Business Logic Methods
    public String getProcessingStatusText() {
        if (Boolean.TRUE.equals(isProcessed)) {
            return processingError != null ? "Failed" : "Completed";
        } else if (processingStartedAt != null) {
            return "Processing";
        } else {
            return "Pending";
        }
    }

    public String getFileSizeFormatted() {
        if (outputFileSize == null) return "0 B";
        
        double size = outputFileSize.doubleValue();
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    public String getProcessingDurationFormatted() {
        if (processingDurationMs == null) return "N/A";
        
        long seconds = processingDurationMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

    public String getDataRangeFormatted() {
        if (dataStartDate == null || dataEndDate == null) return "No date range";
        
        long days = java.time.Duration.between(dataStartDate, dataEndDate).toDays();
        return String.format("%s to %s (%d days)", 
               dataStartDate.toLocalDate(), 
               dataEndDate.toLocalDate(), 
               days);
    }

    public boolean isHighQuality() {
        return dataQualityScore != null && dataQualityScore.compareTo(BigDecimal.valueOf(80)) >= 0;
    }

    public boolean hasDataReduction() {
        return transformedDataRows != null && originalDataRows != null && 
               transformedDataRows < originalDataRows;
    }

    public boolean hasFeatureEngineering() {
        return transformedColumns != null && originalColumns != null && 
               transformedColumns > originalColumns;
    }

    public double getRowReductionPercentage() {
        if (originalDataRows == null || originalDataRows == 0) return 0.0;
        if (transformedDataRows == null) return 0.0;
        
        return ((double)(originalDataRows - transformedDataRows) / originalDataRows.doubleValue()) * 100.0;
    }

    public String getTransformationSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (hasDataReduction()) {
            summary.append(String.format("%.1f%% rows reduced", getRowReductionPercentage()));
        }
        
        if (hasFeatureEngineering()) {
            if (summary.length() > 0) summary.append(", ");
            summary.append(String.format("%d features added", transformedColumns - originalColumns));
        }
        
        if (outliersRemoved != null && outliersRemoved > 0) {
            if (summary.length() > 0) summary.append(", ");
            summary.append(String.format("%d outliers removed", outliersRemoved));
        }
        
        if (duplicatesRemoved != null && duplicatesRemoved > 0) {
            if (summary.length() > 0) summary.append(", ");
            summary.append(String.format("%d duplicates removed", duplicatesRemoved));
        }
        
        return summary.length() > 0 ? summary.toString() : "No significant changes";
    }

    public boolean requiresReprocessing() {
        return processingError != null || Boolean.FALSE.equals(isProcessed);
    }
}