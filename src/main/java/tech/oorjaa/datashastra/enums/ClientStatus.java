package tech.oorjaa.datashastra.enums;

/**
 * Client Status Enumeration
 * Represents the status of a client/customer
 */
public enum ClientStatus {
    ACTIVE("Active", "Client is active"),
    INACTIVE("Inactive", "Client is inactive"),
    PROSPECT("Prospect", "Potential client"),
    ARCHIVED("Archived", "Client is archived");

    private final String displayName;
    private final String description;

    ClientStatus(String displayName, String description) {
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
