package tech.oorjaa.datashastra.enums;

/**
 * Enumeration for activity priority levels in demand forecasting
 * Used to prioritize activities for resource allocation and processing
 */
public enum ActivityPriority {
    HIGH("High Priority", 3),
    MEDIUM("Medium Priority", 2),
    LOW("Low Priority", 1);

    private final String displayName;
    private final int priorityLevel;

    ActivityPriority(String displayName, int priorityLevel) {
        this.displayName = displayName;
        this.priorityLevel = priorityLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public static ActivityPriority getDefault() {
        return MEDIUM;
    }
}