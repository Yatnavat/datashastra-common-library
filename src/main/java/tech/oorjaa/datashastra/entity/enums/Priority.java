package tech.oorjaa.datashastra.entity.enums;

/**
 * Priority enum with sort order support
 */
public enum Priority {
    HIGH("High", 1),
    MEDIUM("Medium", 2),
    LOW("Low", 3);

    private final String displayName;
    private final int sortOrder;

    Priority(String displayName, int sortOrder) {
        this.displayName = displayName;
        this.sortOrder = sortOrder;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}