package tech.oorjaa.datashastra.entity.enums;

/**
 * Types of uploaded data
 */
public enum UploadDataType {
    RAW("Raw Data"),
    PREPROCESSED("Pre-processed Data");

    private final String displayName;

    UploadDataType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}