package tech.oorjaa.datashastra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import tech.oorjaa.datashastra.enums.UploadDataType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object for UploadedData entity.
 * Represents uploaded file metadata and processing status.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadedDataDto implements Serializable {

    private Long id;

    @NotNull(message = "Activity ID is required")
    private Long activityId;
    
    private String activityName;

    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name cannot exceed 255 characters")
    private String fileName;

    @Size(max = 255, message = "Original file name cannot exceed 255 characters")
    private String originalFileName;

    @NotNull(message = "Upload data type is required")
    private UploadDataType uploadDataType;

    @NotBlank(message = "File path is required")
    private String filePath;

    @Min(value = 1, message = "File size must be positive")
    private Long fileSize;

    @Size(max = 100, message = "Content type cannot exceed 100 characters")
    private String contentType;

    // Processing Information
    private Boolean isProcessed = false;
    private LocalDateTime processingStartedAt;
    private LocalDateTime processingCompletedAt;
    private String processingError;
    private Integer processingAttempts = 0;

    // Data Quality Metrics
    @DecimalMin(value = "0.0", message = "Data quality score must be non-negative")
    @DecimalMax(value = "100.0", message = "Data quality score cannot exceed 100")
    private BigDecimal dataQualityScore;
    
    private Integer rowCount;
    private Integer columnCount;
    private BigDecimal missingDataPercentage;
    private BigDecimal duplicatePercentage;
    private BigDecimal outlierPercentage;

    // Validation Results
    private String validationErrors;
    private String validationWarnings;
    private Boolean passedValidation = true;
    private Map<String, Object> validationMetrics;

    // Schema Information
    private String dataSchema;
    private String columnTypes;
    private String dateColumns;
    private String numericColumns;
    private String categoricalColumns;

    // Security and Compliance
    @Size(max = 64, message = "Checksum cannot exceed 64 characters")
    private String checksum;
    
    private Boolean legalHold = false;
    private LocalDateTime retentionExpiresAt;
    private String dataClassification; // "public", "internal", "confidential", "restricted"
    private Boolean containsPii = false;
    private Boolean encryptedAtRest = true;

    // Global Support
    private String dataResidencyRegion;
    private Boolean crossBorderTransferApproved = false;
    private String sourceTimezone;
    private String sourceCurrency;

    // User Information
    private Long uploadedByUserId;
    private String uploadedByUserName;
    private LocalDateTime timestamp;

    // Related Files
    private Long parentFileId;
    private Long transformedDataId;
    private String relatedFileIds;

    // Audit Fields
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;

    // Multi-tenant Field
    private Integer tenantId;

    // UI Display Fields
    private String fileSizeDisplay;
    private String uploadTimeAgo;
    private String processingStatusBadge;
    private String qualityScoreBadge;
    private Boolean showWarningIcon;
    private Boolean showErrorIcon;

    // Computed Fields
    private Boolean canProcess;
    private Boolean canDelete;
    private Boolean canDownload;
    private Boolean isExpired;
    private Boolean hasErrors;
    private Boolean hasWarnings;

    // Business Logic Methods
    public String getFileSizeFormatted() {
        if (fileSize == null) return "0 B";
        
        double size = fileSize.doubleValue();
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    public boolean isHighQuality() {
        return dataQualityScore != null && 
               dataQualityScore.compareTo(BigDecimal.valueOf(80)) >= 0;
    }

    public boolean isMediumQuality() {
        return dataQualityScore != null && 
               dataQualityScore.compareTo(BigDecimal.valueOf(60)) >= 0 &&
               dataQualityScore.compareTo(BigDecimal.valueOf(80)) < 0;
    }

    public boolean isLowQuality() {
        return dataQualityScore != null && 
               dataQualityScore.compareTo(BigDecimal.valueOf(60)) < 0;
    }

    public String getProcessingStatus() {
        if (processingError != null) return "Failed";
        if (isProcessed) return "Completed";
        if (processingStartedAt != null) return "Processing";
        return "Pending";
    }

    public String getFileExtension() {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    public boolean isSpreadsheet() {
        String ext = getFileExtension();
        return "csv".equals(ext) || "xlsx".equals(ext) || "xls".equals(ext);
    }

    public boolean requiresRetention() {
        return legalHold || (retentionExpiresAt != null && 
               LocalDateTime.now().isBefore(retentionExpiresAt));
    }

    public long getProcessingDurationSeconds() {
        if (processingStartedAt == null || processingCompletedAt == null) return 0;
        return java.time.Duration.between(processingStartedAt, processingCompletedAt).getSeconds();
    }
}