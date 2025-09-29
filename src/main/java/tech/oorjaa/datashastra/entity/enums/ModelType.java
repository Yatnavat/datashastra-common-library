package tech.oorjaa.datashastra.entity.enums;

/**
 * Types of ML models available
 */
public enum ModelType {
    STATISTICAL("Statistical"),
    MACHINE_LEARNING("Machine Learning"),
    DEEP_LEARNING("Deep Learning"),
    HYBRID("Hybrid");

    private final String displayName;

    ModelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}