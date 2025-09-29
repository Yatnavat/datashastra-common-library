# Chart JSON Documentation

## Step 1: Data Analysis

### 1. Temporal Analysis Tab Charts

#### Daily Demand Trend (Line Chart)
```json
{
  "title": "Daily Demand Trend",
  "subtitle": "Demand patterns over time",
  "chartType": "line",
  "chartData": {
    "labels": ["01/02", "01/07", "01/12", "01/17", "01/22", "01/27", "02/01", "02/06", "02/11", "02/16"],
    "datasets": [{
      "label": "Daily Demand",
      "data": [500, 850, 550, 400, 550, 350, 550, 850, 400, 600]
    }]
  }
}
```

#### Seasonal Patterns (Mixed Bar/Line Chart)
```json
{
  "title": "Seasonal Patterns",
  "subtitle": "Monthly demand aggregation",
  "chartType": "line",
  "chartData": {
    "labels": ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    "datasets": [
      {
        "type": "bar",
        "label": "Avg Demand",
        "data": [105, 120, 130, 125, 130, 110, 100, 85, 75, 70, 70, 85]
      },
      {
        "type": "line",
        "label": "Avg Price",
        "data": [38, 37, 38, 36, 37, 38, 36, 35, 35, 35, 35, 36]
      }
    ]
  }
}
```

#### Demand vs Price Relationship (Scatter Plot)
```json
{
  "title": "Demand vs Price Relationship",
  "subtitle": "Correlation between price and demand over time",
  "chartType": "scatter",
  "chartData": {
    "datasets": [
      {
        "label": "Demand vs Price",
        "data": [
          {"x": 27.8, "y": 650}, {"x": 28.2, "y": 720}, {"x": 29.1, "y": 580},
          {"x": 30.9, "y": 520}, {"x": 31.3, "y": 480}, {"x": 32.1, "y": 450},
          {"x": 33.7, "y": 380}, {"x": 34.2, "y": 420}, {"x": 35.1, "y": 350},
          {"x": 37.6, "y": 600}, {"x": 38.1, "y": 720}, {"x": 38.9, "y": 680},
          {"x": 32.8, "y": 580}, {"x": 33.2, "y": 650}, {"x": 34.5, "y": 720}
        ]
      }
    ]
  }
}
```

### 2. Distribution Analysis Tab Charts

#### Demand Distribution (Histogram)
```json
{
  "title": "Demand Distribution",
  "subtitle": "Histogram of demand values",
  "chartType": "bar",
  "chartData": {
    "labels": ["27-48", "48-69", "69-91", "91-112", "112-133", "133-154", "154-175", "175-197", "197-218", "218-239"],
    "datasets": [{
      "label": "Frequency",
      "data": [80, 290, 440, 350, 310, 170, 60, 60, 20, 10]
    }]
  }
}
```

### 3. Categorical Analysis Tab Charts

#### Demand by Category (Bar Chart)
```json
{
  "title": "Demand by Category",
  "subtitle": "Performance comparison across categories",
  "chartType": "bar",
  "chartData": {
    "labels": ["Electronics", "Home", "Books", "Sports", "Fashion"],
    "datasets": [{
      "label": "Demand",
      "data": [140, 90, 72, 92, 110]
    }]
  }
}
```

#### Category Market Share (Pie Chart)
```json
{
  "title": "Category Market Share",
  "subtitle": "Distribution of total demand by category",
  "chartType": "pie",
  "chartData": {
    "labels": ["Electronics", "Home", "Books", "Sports", "Fashion"],
    "datasets": [{
      "data": [27.3, 18.1, 14.4, 18.2, 22.0]
    }]
  }
}
```

### 4. Relationships Tab Charts

#### Correlation Strength (Bar Chart)
```json
{
  "title": "Correlation Strength",
  "subtitle": "Visual representation of correlation coefficients",
  "chartType": "bar",
  "chartData": {
    "labels": ["Quarter", "Month", "Holiday", "Weekend", "Discount", "Price"],
    "datasets": [{
      "data": [0.34, 0.23, 0.58, -0.32, 0.45, -0.68]
    }]
  }
}
```

### 5. Supporting Data Structures

#### Category Performance Metrics (Table Data)
```json
{
  "title": "Category Performance Metrics",
  "subtitle": "Detailed statistics for each category",
  "tableData": [
    {
      "category": "Electronics",
      "avgDemand": "138.0",
      "totalDemand": 50388,
      "avgPrice": "35.95",
      "variability": "Infinity%"
    },
    {
      "category": "Home",
      "avgDemand": "91.5",
      "totalDemand": 33382,
      "avgPrice": "35.99",
      "variability": "Infinity%"
    },
    {
      "category": "Books",
      "avgDemand": "73.0",
      "totalDemand": 26656,
      "avgPrice": "35.42",
      "variability": "Infinity%"
    },
    {
      "category": "Sports",
      "avgDemand": "92.0",
      "totalDemand": 33580,
      "avgPrice": "36.12",
      "variability": "Infinity%"
    },
    {
      "category": "Fashion",
      "avgDemand": "110.0",
      "totalDemand": 40150,
      "avgPrice": "35.78",
      "variability": "Infinity%"
    }
  ]
}
```

#### Feature Correlations with Demand (Data Structure)
```json
{
  "title": "Feature Correlations with Demand",
  "subtitle": "Statistical relationships between features and demand",
  "features": [
    {
      "feature": "Price",
      "correlation": -0.68,
      "p_value": 0.001,
      "significance": "High"
    },
    {
      "feature": "Discount",
      "correlation": 0.45,
      "p_value": 0.023,
      "significance": "Medium"
    },
    {
      "feature": "Weekend",
      "correlation": -0.32,
      "p_value": 0.045,
      "significance": "Medium"
    },
    {
      "feature": "Holiday",
      "correlation": 0.58,
      "p_value": 0.008,
      "significance": "High"
    },
    {
      "feature": "Month",
      "correlation": 0.23,
      "p_value": 0.156,
      "significance": "Low"
    },
    {
      "feature": "Quarter",
      "correlation": 0.34,
      "p_value": 0.067,
      "significance": "Low"
    }
  ]
}
```

---

## Step 2: Results

### 1. Historical Model Fit Chart
```json
{
  "title": "Historical Model Fit",
  "subtitle": "Actual vs Predicted values for validation period",
  "chartType": "line",
  "chartData": {
    "labels": ["1/2", "1/7", "1/12", "1/17", "1/22", "1/27", "2/1", "2/6", "2/11", "2/16"],
    "datasets": [
      {
        "label": "Actual Values",
        "data": [120, 85, 105, 95, 130, 115, 125, 90, 110, 135]
      },
      {
        "label": "Model Predictions",
        "data": [115, 88, 102, 92, 128, 118, 122, 93, 108, 132]
      }
    ]
  }
}
```

### 2. Forecast Chart with Confidence Intervals
```json
{
  "title": "Demand Forecast",
  "subtitle": "30-day demand forecast with confidence intervals",
  "chartType": "line",
  "chartData": {
    "labels": ["3/18", "3/23", "3/28", "4/2", "4/7", "4/12", "4/17", "4/22", "4/27", "5/2", "5/7", "5/12", "5/17", "5/22", "5/27"],
    "datasets": [
      {
        "label": "Forecast",
        "data": [135, 128, 142, 125, 150, 138, 145, 132, 155, 140, 148, 135, 160, 145, 152]
      },
      {
        "label": "Upper Bound",
        "data": [155, 148, 162, 145, 170, 158, 165, 152, 175, 160, 168, 155, 180, 165, 172]
      },
      {
        "label": "Lower Bound",
        "data": [115, 108, 122, 105, 130, 118, 125, 112, 135, 120, 128, 115, 140, 125, 132]
      }
    ]
  }
}
```

### 3. Feature Importance Chart
```json
{
  "title": "Feature Importance",
  "subtitle": "Which features contribute most to the predictions",
  "chartType": "bar",
  "chartData": {
    "labels": ["Category", "Price", "Day of Week", "Month", "Discount", "Holiday"],
    "datasets": [
      {
        "label": "Importance Score",
        "data": [0.35, 0.28, 0.15, 0.12, 0.08, 0.02]
      }
    ]
  }
}
```

---

## Summary

This document contains JSON structures for various charts used in a demand forecasting and analysis application. The charts are organized into two main steps:

1. **Data Analysis Step**: Includes temporal analysis, distribution analysis, categorical analysis, relationships, and supporting data structures
2. **Results Step**: Contains historical model fit, forecast visualization, and feature importance charts

Each JSON structure follows a consistent format with:
- `title`: Main chart title
- `subtitle`: Descriptive subtitle
- `chartType`: Type of chart (line, bar, scatter, pie)
- `chartData`: Contains labels and datasets for the chart
- Additional properties specific to certain data types (tableData, features)