package tech.oorjaa.datashastra.enums;

/**
 * Enumeration for activity lifecycle status
 * Tracks the current state of forecasting activities
 */
public enum ActivityStatus {
    DRAFT("Draft", "Activity is being created"),
    ACTIVE("Active", "Activity is active and can have forecasts"),
    COMPLETED("Completed", "Activity has been completed successfully"),
    ON_HOLD("On Hold", "Activity is temporarily paused"),
    ARCHIVED("Archived", "Activity has been archived");

    private final String displayName;
    private final String description;

    ActivityStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canCreateForecasts() {
        return this == ACTIVE || this == ON_HOLD;
    }

    public static ActivityStatus getDefault() {
        return DRAFT;
    }
}