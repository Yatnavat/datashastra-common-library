package tech.oorjaa.datashastra.entity.enums;

/**
 * Status enum for Project entities
 */
public enum ProjectStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_HOLD("On Hold"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}