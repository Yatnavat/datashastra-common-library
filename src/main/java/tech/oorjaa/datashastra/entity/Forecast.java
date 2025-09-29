package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tech.oorjaa.datashastra.enums.ForecastStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Individual forecast execution instances within activities.
 * Represents a single run of the forecasting workflow with specific configuration.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Entity
@Table(name = "forecast", indexes = {
    @Index(name = "idx_forecast_activity_status", columnList = "activity_id, status, tenant_id"),
    @Index(name = "idx_forecast_version", columnList = "activity_id, version"),
    @Index(name = "idx_forecast_status", columnList = "status, tenant_id"),
    @Index(name = "idx_forecast_created_date", columnList = "created_date, tenant_id"),
    @Index(name = "idx_forecast_model", columnList = "model_id, status")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_forecast_activity_version", columnNames = {"activity_id", "version"})
})
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"workflowSteps", "trainingProgress"})
@ToString(exclude = {"workflowSteps", "trainingProgress"})
public class Forecast implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Version is required")
    @Pattern(regexp = "^v\\d+\\.\\d+$", message = "Version must be in format 'v1.0'")
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Status is required")
    private ForecastStatus status = ForecastStatus.DRAFT;

    @Column(precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "Accuracy must be non-negative")
    @DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100%")
    private BigDecimal accuracy;

    @Column(name = "training_duration")
    @Min(value = 0, message = "Training duration cannot be negative")
    private Long trainingDuration; // in seconds

    @Column(name = "current_step", nullable = false)
    @Min(value = 1, message = "Current step must be between 1 and 8")
    @Max(value = 8, message = "Current step must be between 1 and 8")
    private Integer currentStep = 1;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message", length = 1000)
    @Size(max = 1000, message = "Error message cannot exceed 1000 characters")
    private String errorMessage;

    // Foreign Key Relationships
    @Column(name = "activity_id", nullable = false)
    @NotNull(message = "Activity is required")
    private Long activityId;

    @Column(name = "uploaded_data_id")
    private Long uploadedDataId;

    @Column(name = "transformed_data_id")
    private Long transformedDataId;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "feature_config_id")
    private Long featureConfigId;

    @Column(name = "aggregation_config_id")
    private Long aggregationConfigId;

    @Column(name = "date_range_config_id")
    private Long dateRangeConfigId;

    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "exploratory_analytics_id")
    private Long exploratoryAnalyticsId;

    // Global Usage Fields
    @Column(name = "forecast_currency", length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be a valid 3-letter ISO code")
    private String forecastCurrency;

    @Column(name = "forecast_timezone", length = 50)
    private String forecastTimezone;

    @Column(name = "locale", length = 10)
    private String locale;

    // Configuration Summary (for quick access)
    @Column(name = "model_name", length = 100)
    private String modelName;

    @Column(name = "forecast_horizon_days")
    @Min(value = 1, message = "Forecast horizon must be at least 1 day")
    @Max(value = 365, message = "Forecast horizon cannot exceed 365 days")
    private Integer forecastHorizonDays;

    @Column(name = "training_data_points")
    @Min(value = 0, message = "Training data points cannot be negative")
    private Integer trainingDataPoints;

    @Column(name = "feature_count")
    @Min(value = 0, message = "Feature count cannot be negative")
    private Integer featureCount;

    // Performance Metrics (cached for quick access)
    @Column(name = "mae", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", message = "MAE must be non-negative")
    private BigDecimal mae; // Mean Absolute Error

    @Column(name = "rmse", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", message = "RMSE must be non-negative")
    private BigDecimal rmse; // Root Mean Square Error

    @Column(name = "mape", precision = 5, scale = 2)
    @DecimalMin(value = "0.0", message = "MAPE must be non-negative")
    private BigDecimal mape; // Mean Absolute Percentage Error

    @Column(name = "r2_score", precision = 5, scale = 4)
    @DecimalMin(value = "-1.0", message = "R2 score must be >= -1.0")
    @DecimalMax(value = "1.0", message = "R2 score must be <= 1.0")
    private BigDecimal r2Score;

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

    // Relationships
    @OneToMany(mappedBy = "forecastId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("stepNumber ASC")
    private List<WorkflowStep> workflowSteps = new ArrayList<>();

    @OneToOne(mappedBy = "forecastId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private TrainingProgress trainingProgress;

    // Business Logic Methods
    public boolean isCompleted() {
        return status.isCompleted();
    }

    public boolean isFailed() {
        return status.isFailed();
    }

    public boolean isRunning() {
        return status.isRunning();
    }

    public boolean isTerminal() {
        return status.isTerminal();
    }

    public boolean canModifyConfiguration() {
        return status == ForecastStatus.DRAFT;
    }

    public boolean canStartTraining() {
        return status == ForecastStatus.DRAFT && currentStep >= 7;
    }

    public void advanceToNextStep() {
        if (currentStep < 8) {
            currentStep++;
        }
    }

    public void setStepCompleted(int stepNumber) {
        if (stepNumber == currentStep) {
            advanceToNextStep();
        }
    }

    public void markCompleted() {
        this.status = ForecastStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.currentStep = 8;
    }

    public void markFailed(String errorMessage) {
        this.status = ForecastStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }

    public boolean hasAcceptableAccuracy() {
        return accuracy != null && accuracy.compareTo(new BigDecimal("70.0")) >= 0;
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = ForecastStatus.DRAFT;
        }
        if (this.currentStep == null) {
            this.currentStep = 1;
        }
        // Initialize workflow steps
        initializeWorkflowSteps();
    }

    @PostPersist
    public void postPersist() {
        // Create initial workflow steps after entity is persisted
        // This will be handled in service layer
    }

    private void initializeWorkflowSteps() {
        if (workflowSteps == null) {
            workflowSteps = new ArrayList<>();
        }
    }

    // Validation
    @AssertTrue(message = "Completed forecasts must have accuracy value")
    public boolean isAccuracyValidForCompletedStatus() {
        return status != ForecastStatus.COMPLETED || accuracy != null;
    }

    @AssertTrue(message = "Failed forecasts must have error message")
    public boolean isErrorMessageValidForFailedStatus() {
        return status != ForecastStatus.FAILED || (errorMessage != null && !errorMessage.trim().isEmpty());
    }
}