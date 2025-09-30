package tech.oorjaa.datashastra.enums;

/**
 * Tenant Status Enumeration
 * Represents the lifecycle status of a tenant/company
 */
public enum TenantStatus {
    ACTIVE("Active", "Tenant is active and operational"),
    INACTIVE("Inactive", "Tenant is inactive"),
    SUSPENDED("Suspended", "Tenant is temporarily suspended"),
    TRIAL("Trial", "Tenant is in trial period"),
    EXPIRED("Expired", "Tenant subscription has expired");

    private final String displayName;
    private final String description;

    TenantStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOperational() {
        return this == ACTIVE || this == TRIAL;
    }
}
