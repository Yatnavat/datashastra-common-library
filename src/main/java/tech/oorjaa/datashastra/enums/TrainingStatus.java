package tech.oorjaa.datashastra.enums;

/**
 * Enumeration for model training status
 * Tracks the real-time status of model training processes
 */
public enum TrainingStatus {
    INITIALIZING("Initializing", "Setting up training environment"),
    TRAINING("Training", "Model training in progress"),
    VALIDATING("Validating", "Validating model performance"),
    COMPLETED("Completed", "Training completed successfully"),
    FAILED("Failed", "Training failed due to error");

    private final String displayName;
    private final String description;

    TrainingStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRunning() {
        return this == INITIALIZING || this == TRAINING || this == VALIDATING;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isFailed() {
        return this == FAILED;
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED;
    }
}