package tech.oorjaa.datashastra.enums;

/**
 * Enumeration for model complexity levels
 * Used to categorize models by their computational requirements and interpretability
 */
public enum ModelComplexity {
    LOW("Low", "Simple models, fast training, high interpretability"),
    MEDIUM("Medium", "Moderate complexity, balanced performance"),
    HIGH("High", "Complex models, longer training, potentially higher accuracy");

    private final String displayName;
    private final String description;

    ModelComplexity(String displayName, String description) {
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