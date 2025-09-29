package tech.oorjaa.datashastra.entity.enums;

/**
 * Status enum for User entities
 */
public enum UserStatus {
    ACTIVE("Active"),
    PENDING("Pending"),
    INACTIVE("Inactive");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}