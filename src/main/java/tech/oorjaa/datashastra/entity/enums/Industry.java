package tech.oorjaa.datashastra.entity.enums;

/**
 * Industry classification enum
 */
public enum Industry {
    RETAIL("Retail"),
    LOGISTICS("Logistics"),
    MANUFACTURING("Manufacturing"),
    HEALTHCARE("Healthcare"),
    FINANCE("Finance"),
    ECOMMERCE("E-Commerce"),
    OTHER("Other");

    private final String displayName;

    Industry(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}