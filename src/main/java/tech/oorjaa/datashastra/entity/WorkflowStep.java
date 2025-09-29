package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import tech.oorjaa.datashastra.enums.StepStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Workflow step tracking entity for the 8-step forecasting process.
 * Tracks completion status, validation, and progress for each step.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "workflow_step", indexes = {
    @Index(name = "idx_workflow_step_forecast", columnList = "forecast_id, step_number"),
    @Index(name = "idx_workflow_step_status", columnList = "status, tenant_id"),
    @Index(name = "idx_workflow_step_completion", columnList = "completed_at, tenant_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_workflow_step_forecast_number", 
                     columnNames = {"forecast_id", "step_number"})
})
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowStep implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "step_number", nullable = false)
    @NotNull(message = "Step number is required")
    @Min(value = 1, message = "Step number must be between 1 and 8")
    @Max(value = 8, message = "Step number must be between 1 and 8")
    private Integer stepNumber;

    @Column(name = "step_name", nullable = false, length = 100)
    @NotBlank(message = "Step name is required")
    @Size(max = 100, message = "Step name cannot exceed 100 characters")
    private String stepName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Status is required")
    private StepStatus status = StepStatus.PENDING;

    @Column(name = "is_required", nullable = false)
    @NotNull(message = "Required flag is required")
    private Boolean isRequired = true;

    @Column(name = "is_accessible", nullable = false)
    @NotNull(message = "Accessible flag is required")
    private Boolean isAccessible = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "progress_percentage", nullable = false)
    @Min(value = 0, message = "Progress percentage must be between 0 and 100")
    @Max(value = 100, message = "Progress percentage must be between 0 and 100")
    private Integer progressPercentage = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "validation_errors", columnDefinition = "JSON")
    private String validationErrors; // JSON array of validation errors

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "validation_warnings", columnDefinition = "JSON")
    private String validationWarnings; // JSON array of warnings

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "step_data", columnDefinition = "JSON")
    private String stepData; // Step-specific configuration and data

    // Step Completion Metrics
    @Column(name = "processing_time_seconds")
    @Min(value = 0, message = "Processing time cannot be negative")
    private Integer processingTimeSeconds;

    @Column(name = "retry_count")
    @Min(value = 0, message = "Retry count cannot be negative")
    @Max(value = 5, message = "Maximum 5 retries allowed")
    private Integer retryCount = 0;

    @Column(name = "last_error_message", length = 1000)
    @Size(max = 1000, message = "Error message cannot exceed 1000 characters")
    private String lastErrorMessage;

    @Column(name = "error_count")
    @Min(value = 0, message = "Error count cannot be negative")
    private Integer errorCount = 0;

    // Step Dependencies and Requirements
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "required_fields", columnDefinition = "JSON")
    private String requiredFields; // JSON array of required field names

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "completed_fields", columnDefinition = "JSON")
    private String completedFields; // JSON array of completed field names

    @Column(name = "validation_score", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Validation score must be non-negative")
    @DecimalMax(value = "100.0", message = "Validation score cannot exceed 100")
    private java.math.BigDecimal validationScore;

    // User Interaction Tracking
    @Column(name = "user_skipped")
    private Boolean userSkipped = false;

    @Column(name = "auto_completed")
    private Boolean autoCompleted = false;

    @Column(name = "user_modified")
    private Boolean userModified = false;

    @Column(name = "last_user_action", length = 50)
    @Size(max = 50, message = "Last user action cannot exceed 50 characters")
    private String lastUserAction; // SAVE, VALIDATE, SKIP, COMPLETE, etc.

    @Column(name = "user_notes", length = 500)
    @Size(max = 500, message = "User notes cannot exceed 500 characters")
    private String userNotes;

    // Foreign Key Relationships
    @Column(name = "forecast_id", nullable = false)
    @NotNull(message = "Forecast reference is required")
    private Long forecastId;

    // Step Configuration
    @Column(name = "estimated_duration_minutes")
    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDurationMinutes;

    @Column(name = "max_allowed_duration_minutes")
    @Min(value = 1, message = "Max allowed duration must be at least 1 minute")
    private Integer maxAllowedDurationMinutes;

    @Column(name = "timeout_warning_minutes")
    @Min(value = 1, message = "Timeout warning must be at least 1 minute")
    private Integer timeoutWarningMinutes;

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
    public boolean isCompleted() {
        return status.isCompleted();
    }

    public boolean hasErrors() {
        return status.hasError();
    }

    public boolean canProceedToNext() {
        return status.canProceedToNext();
    }

    public boolean isAccessibleForUser() {
        return Boolean.TRUE.equals(isAccessible);
    }

    public boolean canBeSkipped() {
        return !Boolean.TRUE.equals(isRequired);
    }

    public boolean isInProgress() {
        return status == StepStatus.IN_PROGRESS;
    }

    public boolean isPending() {
        return status == StepStatus.PENDING;
    }

    public boolean needsUserAttention() {
        return hasErrors() || (isInProgress() && isOverdue());
    }

    public void markAsStarted() {
        this.status = StepStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
        this.progressPercentage = 5;
    }

    public void markAsCompleted() {
        this.status = StepStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.progressPercentage = 100;
        if (startedAt != null) {
            this.processingTimeSeconds = (int) java.time.Duration.between(startedAt, completedAt).getSeconds();
        }
    }

    public void markAsSkipped() {
        if (canBeSkipped()) {
            this.status = StepStatus.SKIPPED;
            this.completedAt = LocalDateTime.now();
            this.progressPercentage = 100;
            this.userSkipped = true;
        }
    }

    public void markAsError(String errorMessage) {
        this.status = StepStatus.ERROR;
        this.lastErrorMessage = errorMessage;
        this.errorCount = (this.errorCount != null ? this.errorCount : 0) + 1;
        this.progressPercentage = Math.max(this.progressPercentage, 10); // Maintain some progress
    }

    public void incrementRetryCount() {
        this.retryCount = (this.retryCount != null ? this.retryCount : 0) + 1;
    }

    public boolean canRetry() {
        return this.retryCount != null && this.retryCount < 3 && hasErrors();
    }

    public void updateProgress(int percentage) {
        if (percentage >= 0 && percentage <= 100) {
            this.progressPercentage = percentage;
        }
    }

    public boolean isOverdue() {
        if (startedAt == null || maxAllowedDurationMinutes == null) {
            return false;
        }
        LocalDateTime deadline = startedAt.plusMinutes(maxAllowedDurationMinutes);
        return LocalDateTime.now().isAfter(deadline);
    }

    public boolean shouldShowTimeoutWarning() {
        if (startedAt == null || timeoutWarningMinutes == null) {
            return false;
        }
        LocalDateTime warningTime = startedAt.plusMinutes(timeoutWarningMinutes);
        return LocalDateTime.now().isAfter(warningTime) && !isOverdue();
    }

    public long getElapsedTimeMinutes() {
        if (startedAt == null) return 0;
        LocalDateTime endTime = completedAt != null ? completedAt : LocalDateTime.now();
        return java.time.Duration.between(startedAt, endTime).toMinutes();
    }

    public String getStepDisplayName() {
        return stepNumber + ". " + stepName;
    }

    public double getValidationScoreValue() {
        return validationScore != null ? validationScore.doubleValue() : 0.0;
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = StepStatus.PENDING;
        }
        if (this.isRequired == null) {
            this.isRequired = true;
        }
        if (this.isAccessible == null) {
            this.isAccessible = stepNumber != null && stepNumber == 1; // First step is always accessible
        }
        if (this.progressPercentage == null) {
            this.progressPercentage = 0;
        }
        if (this.retryCount == null) {
            this.retryCount = 0;
        }
        if (this.errorCount == null) {
            this.errorCount = 0;
        }
        if (this.userSkipped == null) {
            this.userSkipped = false;
        }
        if (this.autoCompleted == null) {
            this.autoCompleted = false;
        }
        if (this.userModified == null) {
            this.userModified = false;
        }
        
        // Set default step name based on step number if not provided
        if (stepName == null || stepName.trim().isEmpty()) {
            setDefaultStepName();
        }
    }

    private void setDefaultStepName() {
        switch (stepNumber != null ? stepNumber : 0) {
            case 1 -> this.stepName = "Data Upload";
            case 2 -> this.stepName = "Data Analysis";
            case 3 -> this.stepName = "Model Selection";
            case 4 -> this.stepName = "Feature Selection";
            case 5 -> this.stepName = "Aggregation";
            case 6 -> this.stepName = "Date Ranges";
            case 7 -> this.stepName = "Model Training";
            case 8 -> this.stepName = "Results & Visualization";
            default -> this.stepName = "Unknown Step";
        }
    }

    // Custom Validation
    @AssertTrue(message = "Completed steps must have completion timestamp")
    public boolean isCompletionTimestampValid() {
        return !isCompleted() || completedAt != null;
    }

    @AssertTrue(message = "In progress steps must have started timestamp")
    public boolean isStartTimestampValid() {
        return status != StepStatus.IN_PROGRESS || startedAt != null;
    }

    @AssertTrue(message = "Error steps must have error message")
    public boolean isErrorMessageValid() {
        return !hasErrors() || 
               (lastErrorMessage != null && !lastErrorMessage.trim().isEmpty());
    }
}