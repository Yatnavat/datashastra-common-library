package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.oorjaa.datashastra.enums.UploadDataType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Metadata entity for uploaded forecast data files.
 * Stores file information, validation results, and processing status.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "uploaded_data", indexes = {
    @Index(name = "idx_uploaded_data_activity", columnList = "activity_id, tenant_id"),
    @Index(name = "idx_uploaded_data_timestamp", columnList = "timestamp, tenant_id"),
    @Index(name = "idx_uploaded_data_type", columnList = "upload_data_type, tenant_id"),
    @Index(name = "idx_uploaded_data_user", columnList = "uploaded_by_user_id, tenant_id")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class UploadedData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "file_name", nullable = false, length = 255)
    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name cannot exceed 255 characters")
    private String fileName;

    @Column(name = "original_file_name", length = 255)
    @Size(max = 255, message = "Original file name cannot exceed 255 characters")
    private String originalFileName;

    @Column(name = "file_size", nullable = false)
    @NotNull(message = "File size is required")
    @Min(value = 1, message = "File size must be positive")
    @Max(value = 104857600, message = "File size cannot exceed 100MB") // 100MB in bytes
    private Long fileSize;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "File URL is required")
    @Size(max = 500, message = "URL cannot exceed 500 characters")
    private String url;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "upload_data_type", nullable = false, length = 20)
    @NotNull(message = "Upload data type is required")
    private UploadDataType uploadDataType;

    @Column(name = "content_type", length = 100)
    @Size(max = 100, message = "Content type cannot exceed 100 characters")
    private String contentType;

    @Column(length = 64)
    @Size(max = 64, message = "Checksum cannot exceed 64 characters")
    private String checksum; // MD5 or SHA256 hash for integrity

    @Column(name = "data_schema_requirement", columnDefinition = "TEXT")
    private String dataSchemaRequirement; // JSON string

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "validation_results", columnDefinition = "JSON")
    private String validationResults; // JSON containing validation results

    // File Processing Status
    @Column(name = "is_processed", nullable = false)
    private Boolean isProcessed = false;

    @Column(name = "processing_started_at")
    private LocalDateTime processingStartedAt;

    @Column(name = "processing_completed_at")
    private LocalDateTime processingCompletedAt;

    @Column(name = "processing_error", columnDefinition = "TEXT")
    private String processingError;

    // Data Statistics (cached for quick access)
    @Column(name = "row_count")
    @Min(value = 0, message = "Row count cannot be negative")
    private Long rowCount;

    @Column(name = "column_count")
    @Min(value = 0, message = "Column count cannot be negative")
    private Integer columnCount;

    @Column(name = "data_quality_score", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Data quality score must be non-negative")
    @DecimalMax(value = "100.0", message = "Data quality score cannot exceed 100")
    private java.math.BigDecimal dataQualityScore;

    // Foreign Key Relationships
    @Column(name = "activity_id", nullable = false)
    @NotNull(message = "Activity is required")
    private Long activityId;

    @Column(name = "uploaded_by_user_id", nullable = false)
    @NotNull(message = "Uploaded by user is required")
    private Long uploadedByUserId;

    // Global Usage Fields
    @Column(name = "data_residency_region", length = 10)
    @Size(max = 10, message = "Data residency region cannot exceed 10 characters")
    private String dataResidencyRegion; // US, EU, APAC

    @Column(name = "cross_border_transfer_approved", nullable = false)
    private Boolean crossBorderTransferApproved = false;

    @Column(name = "retention_expires_at")
    private LocalDateTime retentionExpiresAt;

    @Column(name = "legal_hold", nullable = false)
    private Boolean legalHold = false;

    // Audit Fields
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // Business Logic Methods
    public boolean isValidFile() {
        return dataQualityScore != null && 
               dataQualityScore.compareTo(java.math.BigDecimal.valueOf(70.0)) >= 0;
    }

    public boolean canBeProcessed() {
        return !isProcessed && processingError == null && !legalHold;
    }

    public boolean isLargeFile() {
        return fileSize != null && fileSize > 50 * 1024 * 1024; // 50MB
    }

    public void markProcessingStarted() {
        this.processingStartedAt = LocalDateTime.now();
        this.isProcessed = false;
    }

    public void markProcessingCompleted() {
        this.processingCompletedAt = LocalDateTime.now();
        this.isProcessed = true;
        this.processingError = null;
    }

    public void markProcessingFailed(String error) {
        this.processingError = error;
        this.isProcessed = false;
        this.processingCompletedAt = LocalDateTime.now();
    }

    public boolean isRetentionExpired() {
        return retentionExpiresAt != null && 
               LocalDateTime.now().isAfter(retentionExpiresAt) && 
               !legalHold;
    }

    public String getFileExtension() {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    public boolean isCsvFile() {
        return "csv".equals(getFileExtension());
    }

    public boolean isExcelFile() {
        String ext = getFileExtension();
        return "xlsx".equals(ext) || "xls".equals(ext);
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.isProcessed == null) {
            this.isProcessed = false;
        }
        if (this.crossBorderTransferApproved == null) {
            this.crossBorderTransferApproved = false;
        }
        if (this.legalHold == null) {
            this.legalHold = false;
        }
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
        
        // Set retention expiry (default 2 years)
        if (this.retentionExpiresAt == null) {
            this.retentionExpiresAt = LocalDateTime.now().plusYears(2);
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    // Custom Validation
    @AssertTrue(message = "Processed files must have processing completed timestamp")
    public boolean isProcessingTimestampValid() {
        return !Boolean.TRUE.equals(isProcessed) || processingCompletedAt != null;
    }

    @AssertTrue(message = "File must be CSV or Excel format")
    public boolean isSupportedFileFormat() {
        return isCsvFile() || isExcelFile();
    }
}