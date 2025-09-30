package tech.oorjaa.datashastra.enums;

/**
 * User Status Enumeration
 * Represents the account status of a user
 */
public enum UserStatus {
    ACTIVE("Active", "User account is active"),
    INACTIVE("Inactive", "User account is inactive"),
    LOCKED("Locked", "User account is locked due to security reasons"),
    PENDING("Pending", "User account is pending activation"),
    SUSPENDED("Suspended", "User account is temporarily suspended");

    private final String displayName;
    private final String description;

    UserStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean canLogin() {
        return this == ACTIVE;
    }
}
