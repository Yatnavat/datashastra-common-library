package tech.oorjaa.datashastra.entity.enums;

/**
 * Types of forecasting available
 */
public enum ForecastType {
    DEMAND_FORECAST("Demand Forecast"),
    INVENTORY_FORECAST("Inventory Forecast"),
    SALES_FORECAST("Sales Forecast"),
    REVENUE_FORECAST("Revenue Forecast");

    private final String displayName;

    ForecastType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}