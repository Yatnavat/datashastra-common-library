# Demand Forecasting API Reference

## Overview
This document provides comprehensive JSON API specifications for all demand forecasting screens based on the operations available in the mock data service.

## Table of Contents
1. [API Response Format](#api-response-format)
2. [Dashboard APIs](#dashboard-apis)
3. [Activity Management APIs](#activity-management-apis)
4. [Data Upload & Validation APIs](#data-upload--validation-apis)
5. [Model Configuration APIs](#model-configuration-apis)
6. [Training & Execution APIs](#training--execution-apis)
7. [Results & Analysis APIs](#results--analysis-apis)
8. [Configuration APIs](#configuration-apis)
9. [Version Management APIs](#version-management-apis)
10. [Lookup Data APIs](#lookup-data-apis)

---

## API Response Format

All API responses follow this standard format:

```json
{
  "data": "<actual_response_data>",
  "success": boolean,
  "message": "string",
  "timestamp": "ISO_8601_datetime"
}
```

### Pagination Response Format

```json
{
  "data": {
    "items": [...],
    "totalCount": number,
    "page": number,
    "limit": number,
    "hasNext": boolean,
    "hasPrevious": boolean
  },
  "success": boolean,
  "message": "string",
  "timestamp": "ISO_8601_datetime"
}
```

---

## Dashboard APIs

### GET /api/demand-forecasting/dashboard/statistics
**Purpose**: Get dashboard overview statistics

**Response**:
```json
{
  "data": {
    "totalActivities": 124,
    "totalForecasts": 89,
    "activeActivities": 23,
    "uniqueClients": 15,
    "trends": {
      "activitiesGrowth": 12.5,
      "forecastsGrowth": 8.3,
      "clientsGrowth": 25.0
    }
  },
  "success": true,
  "message": "Dashboard statistics retrieved successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

---

## Activity Management APIs

### GET /api/demand-forecasting/activities
**Purpose**: Get paginated list of activities

**Query Parameters**:
- `page` (number, default: 1)
- `limit` (number, default: 20)
- `client` (string, optional)
- `status` (string, optional): "Active" | "Completed" | "Draft"
- `priority` (string, optional): "High" | "Medium" | "Low"

**Response**:
```json
{
  "data": {
    "items": [
      {
        "id": "act_001",
        "name": "Q4 Demand Forecast - Electronics",
        "client": "TechCorp Solutions",
        "project": "Product Demand Optimization",
        "description": "Quarterly demand forecasting for consumer electronics portfolio",
        "industry": "Technology",
        "priority": "High",
        "status": "Active",
        "createdAt": "2024-01-15T09:30:00Z",
        "lastModified": "2024-01-20T14:45:00Z",
        "createdBy": "john.smith@example.com",
        "forecasts": [
          {
            "id": "forecast_001",
            "version": "v1.0",
            "status": "Completed",
            "accuracy": 94.2,
            "createdAt": "2024-01-18T11:20:00Z",
            "modelType": "XGBoost"
          }
        ],
        "tags": ["quarterly", "electronics", "high-priority"],
        "permissions": {
          "canEdit": true,
          "canDelete": false,
          "canView": true
        }
      }
    ],
    "totalCount": 3,
    "page": 1,
    "limit": 20,
    "hasNext": false,
    "hasPrevious": false
  },
  "success": true,
  "message": "Activities retrieved successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/activities/{activityId}
**Purpose**: Get specific activity details

**Response**:
```json
{
  "data": {
    "id": "act_001",
    "name": "Q4 Demand Forecast - Electronics",
    "client": "TechCorp Solutions",
    "project": "Product Demand Optimization",
    "description": "Quarterly demand forecasting for consumer electronics portfolio",
    "industry": "Technology",
    "priority": "High",
    "status": "Active",
    "createdAt": "2024-01-15T09:30:00Z",
    "lastModified": "2024-01-20T14:45:00Z",
    "createdBy": "john.smith@example.com",
    "forecasts": [...],
    "tags": ["quarterly", "electronics", "high-priority"],
    "permissions": {
      "canEdit": true,
      "canDelete": false,
      "canView": true
    }
  },
  "success": true,
  "message": "Activity retrieved successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### POST /api/demand-forecasting/activities
**Purpose**: Create new activity

**Request Body**:
```json
{
  "name": "string",
  "client": "string",
  "project": "string",
  "description": "string",
  "industry": "string",
  "priority": "High" | "Medium" | "Low",
  "tags": ["string"]
}
```

### PUT /api/demand-forecasting/activities/{activityId}
**Purpose**: Update existing activity

**Request Body**: Same as POST

### DELETE /api/demand-forecasting/activities/{activityId}
**Purpose**: Delete activity

---

## Data Upload & Validation APIs

### POST /api/demand-forecasting/activities/{activityId}/upload
**Purpose**: Upload and validate data file

**Request**: Multipart form data
- `file`: File upload
- `uploadType`: "raw" | "preprocessed"

**Response**:
```json
{
  "data": {
    "isValid": true,
    "totalRows": 15000,
    "validRows": 14995,
    "errorCount": 0,
    "warningCount": 5,
    "qualityScore": 94.2,
    "processingTime": 45.3,
    "errors": [],
    "warnings": [
      {
        "row": 856,
        "column": "price",
        "message": "Unusually high price value detected",
        "severity": "warning",
        "suggestedFix": "Verify price is correct"
      }
    ],
    "statistics": {
      "dateRange": {
        "start": "2022-01-01",
        "end": "2023-12-31"
      },
      "columnStats": {
        "demand": {
          "mean": 245.7,
          "median": 189.0,
          "min": 0,
          "max": 2500,
          "nullCount": 15
        },
        "price": {
          "mean": 29.45,
          "median": 24.99,
          "min": 5.99,
          "max": 299.99,
          "nullCount": 8
        }
      }
    }
  },
  "success": true,
  "message": "Data validation completed",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### POST /api/demand-forecasting/activities/{activityId}/preview
**Purpose**: Generate data preview

**Request**: Multipart form data
- `file`: File upload

**Response**:
```json
{
  "data": {
    "columns": [
      {
        "name": "date",
        "type": "date",
        "required": true,
        "nullCount": 0,
        "uniqueCount": 730
      },
      {
        "name": "sku",
        "type": "string",
        "required": true,
        "nullCount": 0,
        "uniqueCount": 45
      },
      {
        "name": "demand",
        "type": "number",
        "required": true,
        "nullCount": 15,
        "uniqueCount": 892
      }
    ],
    "rows": [
      ["2023-01-01", "SKU001", 245, "DEPOT_A", 24.99],
      ["2023-01-01", "SKU002", 189, "DEPOT_A", 19.99]
    ],
    "totalRows": 15000,
    "totalColumns": 5
  },
  "success": true,
  "message": "Data preview generated",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/data-requirements
**Purpose**: Get data requirements and column specifications

**Query Parameters**:
- `uploadType`: "raw" | "preprocessed"

**Response**:
```json
{
  "data": {
    "requiredColumns": [
      {
        "name": "Date",
        "description": "Sales date in ISO format",
        "type": "date",
        "examples": ["2024-01-15"]
      },
      {
        "name": "SKUID",
        "description": "Unique SKU identifier",
        "type": "string",
        "examples": ["SKU001", "ABC123"]
      },
      {
        "name": "Depot",
        "description": "Depot/warehouse location code",
        "type": "string",
        "examples": ["D001", "WH_NORTH"]
      },
      {
        "name": "Region",
        "description": "Geographical region or territory code",
        "type": "string",
        "examples": ["North", "South", "East", "West"]
      },
      {
        "name": "Demand",
        "description": "Actual units demanded/sold",
        "type": "number",
        "examples": ["150", "89", "234"]
      }
    ],
    "optionalColumns": [
      {
        "name": "Category",
        "description": "Product category classification",
        "type": "string",
        "examples": ["Electronics", "Books"]
      },
      {
        "name": "Price",
        "description": "Unit price on sale date",
        "type": "number",
        "examples": ["25.99", "15.50"]
      }
    ]
  },
  "success": true,
  "message": "Data requirements retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/templates/{uploadType}
**Purpose**: Download data template

**Response**:
```json
{
  "data": {
    "downloadUrl": "/templates/raw_template.csv",
    "success": true
  },
  "success": true,
  "message": "Template download URL generated",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

---

## Model Configuration APIs

### GET /api/demand-forecasting/models
**Purpose**: Get available forecasting models

**Response**:
```json
{
  "data": [
    {
      "id": "arima",
      "name": "ARIMA",
      "description": "Autoregressive Integrated Moving Average - Classic statistical time series model",
      "type": "ARIMA",
      "complexity": "Medium",
      "trainingTime": "Fast",
      "accuracy": "Good",
      "interpretability": "High",
      "recommended": false,
      "requirements": ["Stationary data", "Sufficient historical data"],
      "bestFor": ["Stationary time series", "Short-term forecasting", "Interpretable results"]
    },
    {
      "id": "prophet",
      "name": "Prophet",
      "description": "Facebook Prophet - Robust forecasting for time series with strong seasonal patterns",
      "type": "Prophet",
      "complexity": "Low",
      "trainingTime": "Fast",
      "accuracy": "Very Good",
      "interpretability": "High",
      "recommended": true,
      "requirements": ["Daily/weekly/yearly seasonality", "Missing data tolerance"],
      "bestFor": ["Strong seasonal patterns", "Holiday effects", "Missing data handling"]
    }
  ],
  "success": true,
  "message": "Available models retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/features
**Purpose**: Get available feature categories

**Response**:
```json
{
  "data": [
    {
      "id": "temporal",
      "name": "Temporal Features",
      "description": "Time-based patterns and historical dependencies",
      "enabled": true,
      "features": [
        {
          "id": "lag_features",
          "name": "Lag Features",
          "description": "Historical lag patterns",
          "type": "lag",
          "enabled": true,
          "importance": 0.85,
          "subFeatures": [
            {
              "id": "lag_1d",
              "name": "1d",
              "description": "Demand from 1 day ago",
              "enabled": true
            },
            {
              "id": "lag_7d",
              "name": "7d",
              "description": "Demand from 7 days ago",
              "enabled": true
            }
          ]
        }
      ]
    },
    {
      "id": "external",
      "name": "External Features",
      "description": "Business and environmental factors",
      "enabled": true,
      "features": [
        {
          "id": "price_features",
          "name": "Price Features",
          "description": "Price, discounts, price elasticity, price changes",
          "type": "price",
          "enabled": true,
          "importance": 0.78
        }
      ]
    }
  ],
  "success": true,
  "message": "Available features retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### POST /api/demand-forecasting/activities/{activityId}/configuration
**Purpose**: Save activity configuration (aggregation, features, model selection, etc.)

**Request Body**:
```json
{
  "aggregation": {
    "geographical": {
      "level": "depot",
      "selectedLocations": ["depot_1", "depot_2"]
    },
    "product": {
      "level": "sku",
      "selectedProducts": ["sku_1", "sku_2"],
      "maxSkus": 5,
      "maxDepots": 5
    },
    "temporal": {
      "granularity": "daily",
      "startDate": "2024-01-01",
      "endDate": "2024-12-31"
    },
    "forecastHorizon": 30,
    "confidenceInterval": 95,
    "outputFormat": "json"
  },
  "features": {
    "selectedFeatures": [
      {
        "categoryId": "temporal",
        "featureId": "lag_features",
        "subFeatures": ["lag_1d", "lag_7d", "lag_14d"]
      },
      {
        "categoryId": "external",
        "featureId": "price_features"
      }
    ]
  },
  "model": {
    "selectedModelId": "prophet",
    "hyperparameters": {
      "seasonality_mode": "multiplicative",
      "changepoint_prior_scale": 0.05
    }
  },
  "dateRanges": {
    "training": {
      "startDate": "2024-01-01",
      "endDate": "2024-10-31"
    },
    "testing": {
      "startDate": "2024-11-01",
      "endDate": "2024-12-31"
    }
  }
}
```

### GET /api/demand-forecasting/activities/{activityId}/configuration
**Purpose**: Get saved activity configuration

---

## Training & Execution APIs

### POST /api/demand-forecasting/activities/{activityId}/train
**Purpose**: Start model training

**Request Body**:
```json
{
  "modelId": "prophet",
  "features": [...],
  "hyperparameters": {...}
}
```

**Response**:
```json
{
  "data": {
    "trainingId": "training_12345",
    "status": "started",
    "estimatedDuration": 300
  },
  "success": true,
  "message": "Model training started",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/activities/{activityId}/training/progress
**Purpose**: Get training progress

**Response**:
```json
{
  "data": {
    "progress": 75,
    "status": "training",
    "estimatedTimeRemaining": 180,
    "currentMetrics": {
      "accuracy": 89.5,
      "mae": 23.4,
      "rmse": 45.6,
      "mape": 12.3,
      "convergence": 0.95
    },
    "resourceUtilization": {
      "cpu": 85,
      "memory": 65
    }
  },
  "success": true,
  "message": "Training progress retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/activities/{activityId}/training/logs
**Purpose**: Get training logs

**Response**:
```json
{
  "data": {
    "logs": [
      {
        "timestamp": "2025-01-20T10:30:00Z",
        "level": "info",
        "message": "Starting model training",
        "details": {...}
      },
      {
        "timestamp": "2025-01-20T10:31:00Z",
        "level": "info",
        "message": "Feature engineering completed",
        "details": {...}
      }
    ]
  },
  "success": true,
  "message": "Training logs retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

---

## Results & Analysis APIs

### GET /api/demand-forecasting/activities/{activityId}/results
**Purpose**: Get comprehensive forecast results

**Response**:
```json
{
  "data": {
    "overview": {
      "modelAccuracy": 88.1,
      "forecastHorizon": 30,
      "dataQuality": 94.2,
      "completionTime": "2025-09-18T15:30:00Z",
      "keyMetrics": [
        {
          "label": "Model Accuracy",
          "value": 88.1,
          "unit": "%",
          "trend": "up"
        },
        {
          "label": "MAPE",
          "value": 11.2,
          "unit": "%",
          "trend": "down"
        }
      ]
    },
    "forecast": {
      "historical": [
        {"date": "2025-08-20", "value": 105},
        {"date": "2025-08-21", "value": 95}
      ],
      "forecast": [
        {"date": "2025-09-20", "value": 125},
        {"date": "2025-09-21", "value": 130}
      ],
      "confidenceIntervals": [
        {"date": "2025-09-20", "lower": 105, "upper": 145, "confidence": 95}
      ],
      "seasonality": {
        "yearly": [1.0, 1.1, 1.2, 1.3, 1.2, 1.1, 1.0, 0.9, 0.8, 0.9, 1.0, 1.1],
        "monthly": [1.0, 1.0, 1.0, 1.0],
        "weekly": [0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.1],
        "daily": []
      }
    },
    "performance": {
      "accuracy": 88.1,
      "mae": 10.09,
      "rmse": 15.97,
      "mape": 11.2,
      "r2Score": 0.920,
      "smape": 10.8,
      "detailedMetrics": [
        {
          "metric": "Mean Absolute Error",
          "value": 10.09,
          "description": "Average absolute difference between predicted and actual values",
          "interpretation": "Lower values indicate better performance"
        }
      ]
    },
    "insights": {
      "keyDrivers": [
        {
          "feature": "Category",
          "importance": 4.2,
          "impact": "positive",
          "description": "Product category has strong predictive power"
        }
      ],
      "recommendations": [
        {
          "title": "Deploy to Production",
          "description": "Model shows excellent performance and is ready for production use.",
          "priority": "High",
          "category": "business",
          "actionItems": [
            "Set up automated retraining schedule",
            "Configure monitoring and alerts"
          ]
        }
      ],
      "riskFactors": [
        {
          "factor": "Data Quality Degradation",
          "probability": 0.3,
          "impact": "Medium",
          "mitigation": "Implement data quality monitoring and validation checks"
        }
      ],
      "opportunityAreas": [
        {
          "area": "Feature Engineering Enhancement",
          "potential": 0.15,
          "description": "Additional external data sources could improve accuracy",
          "requirements": ["Weather data", "Economic indicators"]
        }
      ]
    }
  },
  "success": true,
  "message": "Forecast results retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/activities/{activityId}/data-analysis
**Purpose**: Get data analysis results

**Response**:
```json
{
  "data": {
    "overall_completeness": 99.34,
    "data_quality_score": 94.2,
    "missing_values": {
      "fields": [
        {"name": "Demand", "missing_count": 0, "status": "good"},
        {"name": "Price", "missing_count": 8, "status": "warning"}
      ]
    },
    "outlier_summary": {
      "total_outliers": 28,
      "outlier_rate": 1.53,
      "categories": [
        {"name": "Electronics", "outlier_count": 28},
        {"name": "Home", "outlier_count": 0}
      ]
    },
    "quality_metrics": {
      "completeness": 99.34,
      "accuracy": 97.2,
      "consistency": 98.8,
      "timeliness": 95.0,
      "uniqueness": 99.9,
      "validity": 96.5
    },
    "quality_issues": [
      {"type": "Missing Values", "count": 12, "severity": "low", "columns": ["price", "discount"]},
      {"type": "Outliers", "count": 28, "severity": "medium", "columns": ["demand"]}
    ],
    "correlations": {
      "feature_correlations_with_demand": [
        {"feature": "Price", "correlation": -0.68, "p_value": 0.001, "significance": "High"},
        {"feature": "Discount", "correlation": 0.45, "p_value": 0.023, "significance": "Medium"}
      ],
      "correlation_matrix": [
        {"feature1": "Demand", "feature2": "Price", "correlation": -0.72, "significance": "high"}
      ]
    }
  },
  "success": true,
  "message": "Data analysis completed",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

---

## Configuration APIs

### GET /api/demand-forecasting/aggregation-config
**Purpose**: Get aggregation configuration options

**Response**:
```json
{
  "data": {
    "defaultConfig": {
      "geographical": {
        "level": "depot",
        "selectedLocations": []
      },
      "product": {
        "level": "sku",
        "selectedProducts": [],
        "maxSkus": 5,
        "maxDepots": 5
      },
      "temporal": {
        "granularity": "daily",
        "startDate": "",
        "endDate": ""
      },
      "forecastHorizon": 30,
      "confidenceInterval": 95,
      "outputFormat": "json"
    },
    "depots": [
      {
        "id": "depot_1",
        "name": "North Warehouse",
        "location": "Seattle",
        "region": "North",
        "selected": false
      }
    ],
    "products": [
      {
        "id": "sku_1",
        "name": "Laptop Computer",
        "category": "Electronics",
        "price": 899.99,
        "selected": false
      }
    ],
    "dropdownOptions": {
      "geographicLevels": [
        {"label": "National Total", "value": "national"},
        {"label": "Regional Aggregation", "value": "regional"},
        {"label": "Depot Level", "value": "depot"}
      ],
      "productLevels": [
        {"label": "Brand Level", "value": "brand"},
        {"label": "Product Categories", "value": "category"},
        {"label": "Individual SKUs", "value": "sku"}
      ],
      "timeAggregation": [
        {"label": "Daily", "value": "daily"},
        {"label": "Weekly", "value": "weekly"},
        {"label": "Monthly", "value": "monthly"}
      ]
    }
  },
  "success": true,
  "message": "Aggregation configuration retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/feature-selection-config
**Purpose**: Get feature selection configuration

**Response**:
```json
{
  "data": {
    "pageTitle": "Feature Engineering & Selection",
    "pageDescription": "Configure features that will be used to train your forecasting models",
    "summary": {
      "selectedFeatures": 12,
      "estimatedTime": "~3",
      "recommendedCount": 6
    },
    "categories": [...],
    "recommendations": [
      {
        "id": "lag_7d",
        "name": "Lag 7 days",
        "category": "temporal",
        "importance": 85,
        "recommended": true
      }
    ],
    "configurationSummary": {
      "temporal": {
        "title": "Temporal Features",
        "metrics": [
          {"label": "Lag periods", "value": 4},
          {"label": "Rolling windows", "value": 3}
        ]
      }
    },
    "validationRules": {
      "minFeaturesRequired": 1,
      "minFeaturesRecommended": 3,
      "importanceThresholds": {
        "veryHigh": 0.8,
        "high": 0.7,
        "medium": 0.6,
        "low": 0.5,
        "recommendedMinimum": 0.6
      }
    }
  },
  "success": true,
  "message": "Feature selection configuration retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

---

## Version Management APIs

### GET /api/demand-forecasting/activities/{activityId}/forecasts
**Purpose**: Get forecast versions for an activity

**Response**:
```json
{
  "data": [
    {
      "id": "forecast_v3_001",
      "version": "v3.0",
      "status": "Draft",
      "createdAt": "2024-01-25T10:15:00Z",
      "modelType": "XGBoost"
    },
    {
      "id": "forecast_v2_001",
      "version": "v2.1",
      "status": "Training",
      "createdAt": "2024-01-22T14:30:00Z",
      "modelType": "Prophet"
    },
    {
      "id": "forecast_v2_002",
      "version": "v2.0",
      "status": "Completed",
      "accuracy": 91.2,
      "createdAt": "2024-01-20T16:45:00Z",
      "modelType": "LSTM"
    }
  ],
  "success": true,
  "message": "Forecast versions retrieved",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### POST /api/demand-forecasting/activities/{activityId}/forecasts/{sourceVersionId}/clone
**Purpose**: Clone a forecast version

**Request Body**:
```json
{
  "newVersionName": "v3.1"
}
```

**Response**:
```json
{
  "data": {
    "id": "forecast_clone_1642680000000",
    "version": "v3.1",
    "status": "Draft",
    "createdAt": "2025-01-20T10:30:00Z",
    "modelType": "XGBoost",
    "configuration": {...}
  },
  "success": true,
  "message": "Forecast version cloned successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### DELETE /api/demand-forecasting/activities/{activityId}/forecasts/{versionId}
**Purpose**: Delete a forecast version

**Response**:
```json
{
  "data": {
    "deleted": true
  },
  "success": true,
  "message": "Forecast version deleted successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### POST /api/demand-forecasting/activities/{activityId}/forecasts/compare
**Purpose**: Compare multiple forecast versions

**Request Body**:
```json
{
  "versionIds": ["forecast_v2_001", "forecast_v2_002", "forecast_v3_001"]
}
```

**Response**:
```json
{
  "data": {
    "comparison": {
      "accuracy": [
        {"versionId": "forecast_v2_001", "accuracy": 89.5},
        {"versionId": "forecast_v2_002", "accuracy": 91.2},
        {"versionId": "forecast_v3_001", "accuracy": 88.1}
      ],
      "performance": {
        "mae": [
          {"versionId": "forecast_v2_001", "value": 12.3},
          {"versionId": "forecast_v2_002", "value": 10.8}
        ],
        "rmse": [
          {"versionId": "forecast_v2_001", "value": 18.5},
          {"versionId": "forecast_v2_002", "value": 16.2}
        ]
      }
    },
    "metrics": {
      "bestVersion": "forecast_v2_002",
      "improvement": 2.3,
      "consistentFeatures": ["price", "seasonality", "lag_features"]
    },
    "recommendations": [
      "Version forecast_v2_002 shows better accuracy",
      "Consider ensemble approach for improved performance",
      "Monitor feature importance consistency across versions"
    ]
  },
  "success": true,
  "message": "Forecast versions compared successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

---

## Lookup Data APIs

### GET /api/demand-forecasting/clients
**Purpose**: Get list of available clients

**Response**:
```json
{
  "data": [
    "TechCorp Solutions",
    "RetailMax Inc",
    "MedHealth Systems",
    "Global Manufacturing",
    "FinanceFirst Bank"
  ],
  "success": true,
  "message": "Clients retrieved successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/projects
**Purpose**: Get list of available projects

**Response**:
```json
{
  "data": [
    "Product Demand Optimization",
    "Inventory Optimization",
    "Resource Planning",
    "Supply Chain Analytics",
    "Financial Forecasting"
  ],
  "success": true,
  "message": "Projects retrieved successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### GET /api/demand-forecasting/industries
**Purpose**: Get list of available industries

**Response**:
```json
{
  "data": [
    "Technology",
    "Retail",
    "Healthcare",
    "Manufacturing",
    "Finance",
    "Automotive",
    "Energy",
    "Food & Beverage"
  ],
  "success": true,
  "message": "Industries retrieved successfully",
  "timestamp": "2025-01-20T10:30:00Z"
}
```

---

## Real-time APIs

### WebSocket /ws/demand-forecasting/activities/{activityId}/training
**Purpose**: Real-time training progress updates

**Message Format**:
```json
{
  "type": "training_progress",
  "data": {
    "progress": 75,
    "status": "training",
    "estimatedTimeRemaining": 180,
    "currentMetrics": {
      "accuracy": 89.5,
      "mae": 23.4
    }
  },
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### WebSocket /ws/demand-forecasting/file-upload
**Purpose**: Real-time file upload progress

**Message Format**:
```json
{
  "type": "upload_progress",
  "data": {
    "progress": 65,
    "status": "uploading",
    "filename": "demand_data.csv",
    "bytesUploaded": 65000,
    "totalBytes": 100000
  },
  "timestamp": "2025-01-20T10:30:00Z"
}
```

---

## Error Responses

### Standard Error Format
```json
{
  "data": null,
  "success": false,
  "message": "Error description",
  "error": {
    "code": "ERROR_CODE",
    "details": "Detailed error information",
    "field": "fieldName" // for validation errors
  },
  "timestamp": "2025-01-20T10:30:00Z"
}
```

### Common Error Codes
- `VALIDATION_ERROR`: Input validation failed
- `NOT_FOUND`: Resource not found
- `UNAUTHORIZED`: Authentication required
- `FORBIDDEN`: Access denied
- `TRAINING_IN_PROGRESS`: Training already in progress
- `INVALID_FILE_FORMAT`: Unsupported file format
- `DATA_QUALITY_ISSUES`: Data quality below threshold

---

## Authentication & Authorization

All API endpoints require authentication via JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

### Permission Levels
- `view`: Can view activities and results
- `edit`: Can create and modify activities
- `delete`: Can delete activities
- `admin`: Full access to all operations

---

## Rate Limiting

- API requests: 1000 requests per hour per user
- File uploads: 10 uploads per hour per user
- Training operations: 5 concurrent trainings per user

---

## File Upload Specifications

### Supported Formats
- CSV (.csv)
- Excel (.xlsx, .xls)
- JSON (.json)

### File Size Limits
- Maximum file size: 100MB
- Maximum rows: 1,000,000
- Maximum columns: 100

### CSV Requirements
- UTF-8 encoding
- Comma-separated values
- Header row required
- Date format: ISO 8601 (YYYY-MM-DD)

---

## Caching Strategy

### Cache Headers
- `Cache-Control`: Used for static configuration data
- `ETag`: Used for activity and forecast data
- `Last-Modified`: Used for analysis results

### Cache Duration
- Dashboard statistics: 5 minutes
- Configuration data: 1 hour
- Activity data: 15 minutes
- Analysis results: 1 hour
- Lookup data: 24 hours

---

This comprehensive API reference covers all the operations and screens in the demand forecasting application. Each endpoint provides the necessary JSON structure for both request and response data, making it easy for your API project to implement the required functionality.