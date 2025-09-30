package tech.oorjaa.datashastra.enums;

/**
 * Project Status Enumeration
 * Represents the lifecycle status of a project
 */
public enum ProjectStatus {
    ACTIVE("Active", "Project is active and in progress"),
    COMPLETED("Completed", "Project has been completed"),
    ON_HOLD("On Hold", "Project is temporarily on hold"),
    CANCELLED("Cancelled", "Project has been cancelled"),
    PLANNING("Planning", "Project is in planning phase");

    private final String displayName;
    private final String description;

    ProjectStatus(String displayName, String description) {
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
}
