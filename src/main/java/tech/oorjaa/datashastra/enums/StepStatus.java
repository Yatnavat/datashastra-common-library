package tech.oorjaa.datashastra.enums;

/**
 * Enumeration for workflow step status
 * Tracks the completion state of each step in the 8-step workflow
 */
public enum StepStatus {
    PENDING("Pending", "Step has not been started"),
    IN_PROGRESS("In Progress", "Step is currently being processed"),
    COMPLETED("Completed", "Step has been completed successfully"),
    SKIPPED("Skipped", "Step was skipped (optional steps only)"),
    ERROR("Error", "Step encountered an error and needs attention");

    private final String displayName;
    private final String description;

    StepStatus(String displayName, String description) {
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
        return this == COMPLETED || this == SKIPPED;
    }

    public boolean hasError() {
        return this == ERROR;
    }

    public boolean canProceedToNext() {
        return isCompleted();
    }

    public static StepStatus getDefault() {
        return PENDING;
    }
}