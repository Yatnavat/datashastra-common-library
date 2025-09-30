package tech.oorjaa.datashastra.enums;

/**
 * Workflow Step Type Enumeration
 * Represents the 8-step forecasting workflow
 */
public enum WorkflowStepType {
    DATA_UPLOAD(1, "Data Upload", "Upload and validate source data"),
    ANALYSIS(2, "Data Analysis", "Exploratory data analysis"),
    MODEL_SELECTION(3, "Model Selection", "Select ML models"),
    FEATURES(4, "Feature Configuration", "Configure features for training"),
    AGGREGATION(5, "Aggregation", "Configure data aggregation"),
    DATE_RANGE(6, "Date Range", "Specify forecast date range"),
    VALIDATION(7, "Validation", "Validate configuration"),
    TRAINING(8, "Training", "Train models and generate forecasts");

    private final int stepNumber;
    private final String displayName;
    private final String description;

    WorkflowStepType(int stepNumber, String displayName, String description) {
        this.stepNumber = stepNumber;
        this.displayName = displayName;
        this.description = description;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static WorkflowStepType fromStepNumber(int stepNumber) {
        for (WorkflowStepType type : values()) {
            if (type.stepNumber == stepNumber) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid step number: " + stepNumber);
    }

    public boolean isFirstStep() {
        return stepNumber == 1;
    }

    public boolean isLastStep() {
        return stepNumber == 8;
    }
}
