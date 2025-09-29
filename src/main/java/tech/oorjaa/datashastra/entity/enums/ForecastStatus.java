package tech.oorjaa.datashastra.entity.enums;

/**
 * Status enum for Forecast entities
 */
public enum ForecastStatus {
    DRAFT("Draft"),
    RUNNING("Running"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELLED("Cancelled");

    private final String displayName;

    ForecastStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}