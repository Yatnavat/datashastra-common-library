package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import tech.oorjaa.datashastra.enums.TrainingStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Real-time model training progress tracking entity.
 * Monitors training status, metrics, and resource utilization.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "training_progress", indexes = {
    @Index(name = "idx_training_progress_forecast", columnList = "forecast_id"),
    @Index(name = "idx_training_progress_status", columnList = "status, tenant_id"),
    @Index(name = "idx_training_progress_updated", columnList = "last_updated, tenant_id")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class TrainingProgress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Status is required")
    private TrainingStatus status = TrainingStatus.INITIALIZING;

    @Column(name = "progress_percentage", nullable = false)
    @Min(value = 0, message = "Progress percentage must be between 0 and 100")
    @Max(value = 100, message = "Progress percentage must be between 0 and 100")
    private Integer progressPercentage = 0;

    @Column(name = "current_iteration")
    @Min(value = 0, message = "Current iteration cannot be negative")
    private Integer currentIteration = 0;

    @Column(name = "total_iterations")
    @Min(value = 1, message = "Total iterations must be at least 1")
    private Integer totalIterations;

    @Column(name = "elapsed_time", nullable = false)
    @Min(value = 0, message = "Elapsed time cannot be negative")
    private Long elapsedTime = 0L; // in seconds

    @Column(name = "estimated_time_remaining")
    @Min(value = 0, message = "Estimated time remaining cannot be negative")
    private Long estimatedTimeRemaining; // in seconds

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "current_metrics", columnDefinition = "JSON")
    private String currentMetrics; // JSON containing current training metrics

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resource_utilization", columnDefinition = "JSON")
    private String resourceUtilization; // JSON containing resource usage data

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "training_logs", columnDefinition = "JSON")
    private String trainingLogs; // JSON array of recent log entries

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    // Training Stage Information
    @Column(name = "current_stage", length = 50)
    @Size(max = 50, message = "Current stage cannot exceed 50 characters")
    private String currentStage; // "data_preparation", "model_fitting", "validation", etc.

    @Column(name = "stage_progress", nullable = false)
    @Min(value = 0, message = "Stage progress must be between 0 and 100")
    @Max(value = 100, message = "Stage progress must be between 0 and 100")
    private Integer stageProgress = 0;

    // Performance Metrics (Real-time)
    @Column(name = "current_loss", precision = 15, scale = 8)
    @DecimalMin(value = "0.0", message = "Current loss must be non-negative")
    private java.math.BigDecimal currentLoss;

    @Column(name = "best_loss", precision = 15, scale = 8)
    @DecimalMin(value = "0.0", message = "Best loss must be non-negative")
    private java.math.BigDecimal bestLoss;

    @Column(name = "current_accuracy", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Current accuracy must be non-negative")
    @DecimalMax(value = "100.0", message = "Current accuracy cannot exceed 100")
    private java.math.BigDecimal currentAccuracy;

    @Column(name = "validation_accuracy", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Validation accuracy must be non-negative")
    @DecimalMax(value = "100.0", message = "Validation accuracy cannot exceed 100")
    private java.math.BigDecimal validationAccuracy;

    // Resource Monitoring
    @Column(name = "memory_usage_mb")
    @Min(value = 0, message = "Memory usage cannot be negative")
    private Integer memoryUsageMb;

    @Column(name = "cpu_usage_percent", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "CPU usage must be non-negative")
    @DecimalMax(value = "100.0", message = "CPU usage cannot exceed 100")
    private java.math.BigDecimal cpuUsagePercent;

    @Column(name = "gpu_usage_percent", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "GPU usage must be non-negative")
    @DecimalMax(value = "100.0", message = "GPU usage cannot exceed 100")
    private java.math.BigDecimal gpuUsagePercent;

    @Column(name = "disk_io_mb_per_sec", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "Disk I/O must be non-negative")
    private java.math.BigDecimal diskIoMbPerSec;

    // Training Configuration Info
    @Column(name = "batch_size")
    @Min(value = 1, message = "Batch size must be at least 1")
    private Integer batchSize;

    @Column(name = "learning_rate", precision = 15, scale = 10)
    @DecimalMin(value = "0.0", message = "Learning rate must be non-negative")
    private java.math.BigDecimal learningRate;

    @Column(name = "early_stopping_patience")
    @Min(value = 1, message = "Early stopping patience must be at least 1")
    private Integer earlyStoppingPatience;

    @Column(name = "epochs_without_improvement")
    @Min(value = 0, message = "Epochs without improvement cannot be negative")
    private Integer epochsWithoutImprovement = 0;

    // Error and Warning Tracking
    @Column(name = "error_count")
    @Min(value = 0, message = "Error count cannot be negative")
    private Integer errorCount = 0;

    @Column(name = "warning_count")
    @Min(value = 0, message = "Warning count cannot be negative")
    private Integer warningCount = 0;

    @Column(name = "last_error_message", length = 1000)
    @Size(max = 1000, message = "Error message cannot exceed 1000 characters")
    private String lastErrorMessage;

    @Column(name = "convergence_achieved")
    private Boolean convergenceAchieved = false;

    @Column(name = "convergence_iteration")
    @Min(value = 0, message = "Convergence iteration cannot be negative")
    private Integer convergenceIteration;

    // Foreign Key Relationships
    @Column(name = "forecast_id", nullable = false, unique = true)
    @NotNull(message = "Forecast reference is required")
    private Long forecastId;

    // Audit Fields
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    // Business Logic Methods
    public boolean isRunning() {
        return status.isRunning();
    }

    public boolean isCompleted() {
        return status.isCompleted();
    }

    public boolean isFailed() {
        return status.isFailed();
    }

    public boolean isTerminal() {
        return status.isTerminal();
    }

    public void updateProgress(int percentage) {
        if (percentage >= 0 && percentage <= 100) {
            this.progressPercentage = percentage;
        }
    }

    public void updateStageProgress(String stage, int percentage) {
        this.currentStage = stage;
        this.stageProgress = Math.min(100, Math.max(0, percentage));
    }

    public void incrementIteration() {
        if (this.currentIteration != null) {
            this.currentIteration++;
        }
    }

    public void recordError(String errorMessage) {
        this.errorCount = (this.errorCount != null ? this.errorCount : 0) + 1;
        this.lastErrorMessage = errorMessage;
    }

    public void recordWarning() {
        this.warningCount = (this.warningCount != null ? this.warningCount : 0) + 1;
    }

    public boolean shouldEarlyStopping() {
        return earlyStoppingPatience != null && epochsWithoutImprovement != null &&
               epochsWithoutImprovement >= earlyStoppingPatience;
    }

    public void updateResourceUsage(int memoryMb, double cpuPercent, Double gpuPercent) {
        this.memoryUsageMb = memoryMb;
        this.cpuUsagePercent = java.math.BigDecimal.valueOf(cpuPercent);
        if (gpuPercent != null) {
            this.gpuUsagePercent = java.math.BigDecimal.valueOf(gpuPercent);
        }
    }

    public double getCompletionPercentage() {
        if (totalIterations == null || totalIterations == 0) {
            return progressPercentage != null ? progressPercentage.doubleValue() : 0.0;
        }
        if (currentIteration == null) {
            return 0.0;
        }
        return Math.min(100.0, (currentIteration.doubleValue() / totalIterations.doubleValue()) * 100.0);
    }

    public long getEstimatedRemainingSeconds() {
        return estimatedTimeRemaining != null ? estimatedTimeRemaining : 0;
    }

    public boolean hasResourceConstraints() {
        return (memoryUsageMb != null && memoryUsageMb > 6144) || // > 6GB
               (cpuUsagePercent != null && cpuUsagePercent.compareTo(java.math.BigDecimal.valueOf(90)) > 0);
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = TrainingStatus.INITIALIZING;
        }
        if (this.progressPercentage == null) {
            this.progressPercentage = 0;
        }
        if (this.currentIteration == null) {
            this.currentIteration = 0;
        }
        if (this.elapsedTime == null) {
            this.elapsedTime = 0L;
        }
        if (this.stageProgress == null) {
            this.stageProgress = 0;
        }
        if (this.errorCount == null) {
            this.errorCount = 0;
        }
        if (this.warningCount == null) {
            this.warningCount = 0;
        }
        if (this.convergenceAchieved == null) {
            this.convergenceAchieved = false;
        }
        if (this.epochsWithoutImprovement == null) {
            this.epochsWithoutImprovement = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        // Update elapsed time based on creation time
        if (createdDate != null) {
            this.elapsedTime = java.time.Duration.between(createdDate, LocalDateTime.now()).getSeconds();
        }
    }

    // Custom Validation
    @AssertTrue(message = "Current iteration cannot exceed total iterations")
    public boolean isIterationValid() {
        return currentIteration == null || totalIterations == null || 
               currentIteration <= totalIterations;
    }

    @AssertTrue(message = "Convergence iteration must be within current iteration range")
    public boolean isConvergenceIterationValid() {
        return !Boolean.TRUE.equals(convergenceAchieved) || 
               convergenceIteration == null || currentIteration == null ||
               convergenceIteration <= currentIteration;
    }
}