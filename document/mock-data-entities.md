# Mock Data Service - Entity Documentation

## Overview
This document contains all entity definitions from the `mock-data.service.ts` file for the Demand Forecasting module.

## Core Entities

### 1. Activity
```typescript
interface Activity {
  id: string;                    // e.g., 'act_001'
  name: string;                   // Activity name
  client: string;                 // Client name
  project: string;                // Project name
  description: string;            // Activity description
  industry: string;               // Industry type
  priority: 'High' | 'Medium' | 'Low';
  status: 'Active' | 'Completed' | 'Draft';
  createdAt: string;              // ISO date string
  lastModified: string;           // ISO date string
  createdBy: string;              // User email
  forecasts: Forecast[];          // Array of forecasts
  tags: string[];                 // Activity tags
  permissions: {
    canEdit: boolean;
    canDelete: boolean;
    canView: boolean;
  };
}
```

### 2. Forecast
```typescript
interface Forecast {
  id: string;                     // e.g., 'forecast_001'
  version: string;                // e.g., 'v1.0'
  status: 'Completed' | 'Draft' | 'Training' | 'Failed';
  accuracy?: number;              // Optional accuracy percentage
  createdAt: string;              // ISO date string
  modelType: string;              // e.g., 'XGBoost', 'Prophet'
  configuration?: any;            // Optional configuration
}
```

### 3. DashboardStatistics
```typescript
interface DashboardStatistics {
  totalActivities: number;
  totalForecasts: number;
  activeActivities: number;
  uniqueClients: number;
  trends: {
    activitiesGrowth: number;    // Percentage
    forecastsGrowth: number;      // Percentage
    clientsGrowth: number;        // Percentage
  };
}
```

### 4. ValidationResults
```typescript
interface ValidationResults {
  isValid: boolean;
  totalRows: number;
  validRows: number;
  errorCount: number;
  warningCount: number;
  qualityScore: number;           // Percentage
  processingTime: number;         // Seconds
  errors: ValidationIssue[];
  warnings: ValidationIssue[];
  statistics: {
    dateRange: {
      start: string;
      end: string;
    };
    columnStats: {
      [key: string]: {            // e.g., 'demand', 'price'
        mean: number;
        median: number;
        min: number;
        max: number;
        nullCount: number;
      };
    };
  };
}

interface ValidationIssue {
  row: number;
  column: string;
  message: string;
  severity: 'warning' | 'error';
  suggestedFix?: string;
}
```

### 5. DataPreview
```typescript
interface DataPreview {
  columns: DataColumn[];
  rows: any[][];                  // Array of data rows
  totalRows: number;
  totalColumns: number;
}

interface DataColumn {
  name: string;
  type: 'date' | 'string' | 'number';
  required: boolean;
  nullCount: number;
  uniqueCount: number;
}
```

### 6. DataRequirements
```typescript
interface DataRequirements {
  requiredColumns: ColumnRequirement[];
  optionalColumns: ColumnRequirement[];
}

interface ColumnRequirement {
  name: string;
  description: string;
  type: 'date' | 'string' | 'number';
  examples: (string | number)[];
}
```

### 7. ModelOption
```typescript
interface ModelOption {
  id: string;                     // e.g., 'arima', 'prophet'
  name: string;                    // Display name
  description: string;             // Detailed description
  type: 'ARIMA' | 'Prophet' | 'XGBoost' | 'LSTM' | 'LightGBM';
  complexity: 'Low' | 'Medium' | 'High';
  trainingTime: 'Fast' | 'Medium' | 'Slow';
  accuracy: 'Good' | 'Very Good' | 'Excellent';
  interpretability: 'Low' | 'Medium' | 'High';
  recommended: boolean;
  requirements: string[];          // List of requirements
  bestFor: string[];              // Best use cases
}
```

### 8. FeatureCategory
```typescript
interface FeatureCategory {
  id: string;                     // e.g., 'temporal', 'external'
  name: string;                   // Display name
  description: string;
  enabled: boolean;
  features: Feature[];
}

interface Feature {
  id: string;
  name: string;
  description: string;
  type: 'lag' | 'rolling' | 'price' | 'promotion' | 'holiday' | 'advanced';
  enabled: boolean;
  importance?: number;            // 0-1 scale
  subFeatures?: SubFeature[];
}

interface SubFeature {
  id: string;
  name: string;
  description: string;
  enabled: boolean;
}
```

### 9. TrainingProgress
```typescript
interface TrainingProgress {
  progress: number;               // 0-100 percentage
  status: 'idle' | 'initializing' | 'training' | 'complete' | 'failed';
  estimatedTimeRemaining: number; // Seconds
  currentMetrics: {
    accuracy: number;
    mae: number;
    rmse: number;
    mape: number;
    convergence: number;
  };
  resourceUtilization: {
    cpu: number;                 // Percentage
    memory: number;               // Percentage
  };
}
```

### 10. ForecastResults
```typescript
interface ForecastResults {
  overview: {
    modelAccuracy: number;
    forecastHorizon: number;      // Days
    dataQuality: number;
    completionTime: string;       // ISO date string
    keyMetrics: KeyMetric[];
  };
  forecast: {
    historical: DataPoint[];
    forecast: DataPoint[];
    confidenceIntervals: ConfidenceInterval[];
    seasonality: {
      yearly: number[];
      monthly: number[];
      weekly: number[];
      daily: number[];
    };
  };
  performance: PerformanceMetrics;
  insights: InsightsData;
}

interface KeyMetric {
  label: string;
  value: number;
  unit: string;
  trend: 'up' | 'down' | 'stable';
}

interface DataPoint {
  date: string;
  value: number;
}

interface ConfidenceInterval {
  date: string;
  lower: number;
  upper: number;
  confidence: number;
}
```

### 11. PerformanceMetrics
```typescript
interface PerformanceMetrics {
  accuracy: number;
  mae: number;
  rmse: number;
  mape: number;
  r2Score: number;
  smape: number;
  detailedMetrics: DetailedMetric[];
}

interface DetailedMetric {
  metric: string;
  value: number;
  description: string;
  interpretation: string;
}
```

### 12. InsightsData
```typescript
interface InsightsData {
  keyDrivers: KeyDriver[];
  recommendations: Recommendation[];
  riskFactors: RiskFactor[];
  opportunityAreas: OpportunityArea[];
}

interface KeyDriver {
  feature: string;
  importance: number;
  impact: 'positive' | 'negative';
  description: string;
}

interface Recommendation {
  title: string;
  description: string;
  priority: 'High' | 'Medium' | 'Low';
  category: 'business' | 'performance' | 'accuracy' | 'data';
  actionItems: string[];
}

interface RiskFactor {
  factor: string;
  probability: number;           // 0-1 scale
  impact: 'Low' | 'Medium' | 'High';
  mitigation: string;
}

interface OpportunityArea {
  area: string;
  potential: number;              // 0-1 scale
  description: string;
  requirements: string[];
}
```

### 13. PaginatedResponse
```typescript
interface PaginatedResponse<T> {
  items: T[];
  totalCount: number;
  page: number;
  limit: number;
  hasNext: boolean;
  hasPrevious: boolean;
}
```

### 14. ApiResponse
```typescript
interface ApiResponse<T> {
  data: T;
  success: boolean;
  message: string;
  timestamp: string;              // ISO date string
}
```

## Configuration Entities

### 15. AggregationConfiguration
```typescript
interface AggregationConfiguration {
  geographical: {
    level: 'national' | 'regional' | 'depot';
    selectedLocations: string[];
  };
  product: {
    level: 'brand' | 'category' | 'sku';
    selectedProducts: string[];
    maxSkus: number;
    maxDepots: number;
  };
  temporal: {
    granularity: 'daily' | 'weekly' | 'monthly';
    startDate: string;
    endDate: string;
  };
  forecastHorizon: number;       // Days
  confidenceInterval: number;    // Percentage
  outputFormat: 'json' | 'csv' | 'excel';
}
```

### 16. Depot
```typescript
interface Depot {
  id: string;
  name: string;
  location: string;
  region: string;
  selected: boolean;
}
```

### 17. Product
```typescript
interface Product {
  id: string;
  name: string;
  category: string;
  price: number;
  selected: boolean;
}
```

### 18. DataQualityAnalysis
```typescript
interface DataQualityAnalysis {
  overall_completeness: number;
  data_quality_score: number;
  missing_values: {
    fields: MissingValueField[];
  };
  outlier_summary: {
    total_outliers: number;
    outlier_rate: number;
    categories: OutlierCategory[];
  };
  quality_metrics: {
    completeness: number;
    accuracy: number;
    consistency: number;
    timeliness: number;
    uniqueness: number;
    validity: number;
  };
  quality_issues: QualityIssue[];
  correlations: {
    feature_correlations_with_demand: FeatureCorrelation[];
    correlation_matrix: CorrelationMatrix[];
  };
}

interface MissingValueField {
  name: string;
  missing_count: number;
  status: 'good' | 'warning' | 'error';
}

interface OutlierCategory {
  name: string;
  outlier_count: number;
}

interface QualityIssue {
  type: string;
  count: number;
  severity: 'none' | 'low' | 'medium' | 'high';
  columns: string[];
}

interface FeatureCorrelation {
  feature: string;
  correlation: number;
  p_value: number;
  significance: 'Low' | 'Medium' | 'High';
}

interface CorrelationMatrix {
  feature1: string;
  feature2: string;
  correlation: number;
  significance: 'low' | 'medium' | 'high';
}
```

## UI Configuration Entities

### 19. TabConfiguration
```typescript
interface TabConfiguration {
  pageTitle: string;
  tabCount: number;
  tabList: Tab[];
}

interface Tab {
  title: string;
  icon: string;
}
```

### 20. ChartConfiguration
```typescript
interface ChartDataset {
  label: string;
  data: number[];
  borderColor?: string;
  backgroundColor?: string;
  borderWidth?: number;
  tension?: number;
  fill?: boolean | string;
  pointRadius?: number;
  pointHoverRadius?: number;
  borderDash?: number[];
  yAxisID?: string;
  type?: 'bar' | 'line';
}

interface ChartData {
  labels: string[];
  datasets: ChartDataset[];
}
```

### 21. ModelTrainingConfiguration
```typescript
interface ModelTrainingConfig {
  summary: {
    models: number;
    progress: number;
    completed: number;
  };
  modelStatus: {
    name: string;
    status: 'idle' | 'initializing' | 'running' | 'completed' | 'failed';
    progress: number;
    accuracy: number;
    metrics: {
      mae: number;
      mape: number;
      rmse: number;
      r2: number;
    };
    executionTime: number;
  };
  executionState: {
    isRunning: boolean;
    isCompleted: boolean;
    canContinue: boolean;
  };
  progressConfig: {
    updateInterval: number;
    minProgressStep: number;
    maxProgressStep: number;
    initializingThreshold: number;
    runningThreshold: number;
  };
  messages: {
    startExecution: MessageConfig;
    completionSuccess: MessageConfig;
  };
  logs: {
    initial: LogEntry[];
    completion: LogEntry[];
  };
}

interface MessageConfig {
  severity: 'info' | 'success' | 'warning' | 'error';
  summary: string;
  detail: string;
}

interface LogEntry {
  level: 'info' | 'success' | 'warning' | 'error';
  message: string;
}
```

## Sample Data Values

### Activity Status Values
- 'Active'
- 'Completed'
- 'Draft'

### Priority Values
- 'High'
- 'Medium'
- 'Low'

### Model Types
- 'ARIMA'
- 'Prophet'
- 'XGBoost'
- 'LSTM'
- 'LightGBM'

### Industry Types (Examples)
- 'Technology'
- 'Retail'
- 'Healthcare'
- 'Manufacturing'
- 'Finance'
- 'Automotive'
- 'Energy'
- 'Food & Beverage'

### Feature Types
- 'lag' - Historical lag patterns
- 'rolling' - Rolling statistics
- 'price' - Price-related features
- 'promotion' - Promotion features
- 'holiday' - Holiday features
- 'advanced' - Advanced category features

## Notes for Entity Project

1. **Date Format**: All date fields use ISO 8601 format (e.g., '2024-01-15T09:30:00Z')
2. **Percentages**: Stored as numbers (0-100) unless specified as decimals (0-1)
3. **Optional Fields**: Fields marked with `?` are optional
4. **Enums**: String literal types should be implemented as enums in your entity project
5. **Nested Objects**: Many entities contain nested objects that may need separate entity definitions
6. **Arrays**: Several entities use arrays which may need collection handling in your implementation
7. **Generic Types**: `PaginatedResponse<T>` and `ApiResponse<T>` are generic wrapper types

## API Response Pattern
All API responses follow this pattern:
```typescript
{
  data: <actual_data>,
  success: boolean,
  message: string,
  timestamp: string
}
```

## Pagination Pattern
List endpoints support pagination with:
```typescript
{
  items: T[],
  totalCount: number,
  page: number,
  limit: number,
  hasNext: boolean,
  hasPrevious: boolean
}
```