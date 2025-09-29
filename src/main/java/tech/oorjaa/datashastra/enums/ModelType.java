package tech.oorjaa.datashastra.enums;

/**
 * Enumeration for machine learning model types
 * Represents available forecasting algorithms
 */
public enum ModelType {
    ARIMA("ARIMA", "Autoregressive Integrated Moving Average", "Statistical time series model"),
    PROPHET("Prophet", "Facebook Prophet", "Robust forecasting for time series with strong seasonal patterns"),
    XGBOOST("XGBoost", "Extreme Gradient Boosting", "Machine learning model for complex pattern recognition"),
    LSTM("LSTM", "Long Short-Term Memory", "Deep learning neural network for sequential data"),
    LIGHTGBM("LightGBM", "Light Gradient Boosting Machine", "Fast gradient boosting framework");

    private final String code;
    private final String displayName;
    private final String description;

    ModelType(String code, String displayName, String description) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static ModelType fromCode(String code) {
        for (ModelType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown model type code: " + code);
    }
}