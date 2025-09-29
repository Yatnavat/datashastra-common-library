package tech.oorjaa.datashastra.entity.enums;

/**
 * Status enum for Client entities
 */
public enum ClientStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    PENDING_APPROVAL("Pending Approval");

    private final String displayName;

    ClientStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}