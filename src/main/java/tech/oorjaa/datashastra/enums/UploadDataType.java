package tech.oorjaa.datashastra.enums;

/**
 * Enumeration for data upload types
 * Distinguishes between raw and preprocessed data uploads
 */
public enum UploadDataType {
    RAW("Raw Data", "Original unprocessed data requiring full preprocessing"),
    PREPROCESSED("Preprocessed Data", "Data that has been cleaned and transformed");

    private final String displayName;
    private final String description;

    UploadDataType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRaw() {
        return this == RAW;
    }

    public boolean isPreprocessed() {
        return this == PREPROCESSED;
    }
}