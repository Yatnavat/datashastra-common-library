package tech.oorjaa.datashastra.enums;

/**
 * Configuration Data Type Enumeration
 * Represents the data type of configuration values
 */
public enum ConfigDataType {
    STRING("String", "Text value"),
    INTEGER("Integer", "Whole number value"),
    BOOLEAN("Boolean", "True/False value"),
    DOUBLE("Double", "Decimal number value"),
    JSON("JSON", "JSON object or array");

    private final String displayName;
    private final String description;

    ConfigDataType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
