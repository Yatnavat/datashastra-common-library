package tech.oorjaa.datashastra.enums;

/**
 * Enumeration for forecast execution status
 * Tracks the current state of individual forecast runs
 */
public enum ForecastStatus {
    DRAFT("Draft", "Forecast is being configured"),
    IN_PROGRESS("In Progress", "Forecast is currently running"),
    COMPLETED("Completed", "Forecast completed successfully"),
    FAILED("Failed", "Forecast execution failed"),
    CANCELLED("Cancelled", "Forecast was cancelled by user");

    private final String displayName;
    private final String description;

    ForecastStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isFailed() {
        return this == FAILED;
    }

    public boolean isRunning() {
        return this == IN_PROGRESS;
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }

    public static ForecastStatus getDefault() {
        return DRAFT;
    }
}