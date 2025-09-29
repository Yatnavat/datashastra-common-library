package tech.oorjaa.datashastra.entity.enums;

/**
 * Status enum for Activity entities
 */
public enum ActivityStatus {
    CREATED("Created"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ARCHIVED("Archived");

    private final String displayName;

    ActivityStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}