# Dynamic Forecasting Data Models

## Overview

This document provides comprehensive TypeScript interface definitions for the Dynamic Forecasting Engine API. All interfaces are designed to ensure type safety, maintainability, and consistency with the existing STOCKSENSE architecture.

---

## Table of Contents

1. [Core Enums & Types](#core-enums--types)
2. [Core Entity Models](#core-entity-models)
3. [Workflow Step Models](#workflow-step-models)
4. [API Request/Response Models](#api-requestresponse-models)
5. [Dashboard & Statistics Models](#dashboard--statistics-models)
6. [Real-time Event Models](#real-time-event-models)
7. [Utility Models](#utility-models)

---

## Core Enums & Types

### Basic Enums

```typescript
// Activity status enumeration
export type ActivityStatus = 'Active' | 'Completed' | 'On-hold' | 'Draft';

// Activity priority levels
export type ActivityPriority = 'High' | 'Medium' | 'Low';

// Forecast execution status
export type ForecastStatus = 'Draft' | 'In-Progress' | 'Completed' | 'Failed';

// Available ML models for forecasting
export type ModelType = 'ARIMA' | 'Prophet' | 'XGBoost' | 'LSTM' | 'LightGBM';

// Data upload types
export type DataUploadType = 'raw' | 'preprocessed';

// Aggregation level options
export type AggregationLevel = 'depot' | 'region' | 'country' | 'sku' | 'category' | 'brand';

// Temporal aggregation options
export type TemporalAggregation = 'daily' | 'weekly' | 'monthly';
```

---

## Core Entity Models

### Activity Model

```typescript
// Main activity entity representing a demand forecasting project
export interface Activity {
  id: string;                        // Unique identifier
  name: string;                      // Activity name (3-100 characters)
  client: string;                    // Client organization name
  project: string;                   // Project name within client
  description?: string;              // Optional description (max 500 chars)
  industry?: string;                 // Industry classification
  priority: ActivityPriority;       // Business priority level
  status: ActivityStatus;           // Current activity status
  createdAt: string;                // ISO 8601 creation timestamp
  lastModified: string;             // ISO 8601 last modification timestamp
  createdBy: string;                // Email of creator
  forecasts: Forecast[];            // Associated forecast runs
  tags: string[];                   // Searchable tags
  permissions: ActivityPermissions; // User access permissions
}

// Activity access control
export interface ActivityPermissions {
  canEdit: boolean;    // Can modify activity details
  canDelete: boolean;  // Can archive/delete activity
  canView: boolean;    // Can view activity details
}
```

### Forecast Model

```typescript
// Individual forecast run within an activity
export interface Forecast {
  id: string;                           // Unique forecast identifier
  activityId: string;                   // Parent activity ID
  version: string;                      // Version identifier (e.g., "v1.0")
  status: ForecastStatus;              // Current execution status
  accuracy?: number;                    // Model accuracy percentage (if completed)
  createdAt: string;                   // ISO 8601 creation timestamp
  lastModified: string;                // ISO 8601 last modification timestamp
  configuration: ForecastConfiguration; // Model and step configurations
  results?: ForecastResults;           // Results (if completed)
  currentStep: number;                 // Current workflow step (1-8)
  steps: WorkflowStep[];               // Step completion status
}

// Complete forecast configuration
export interface ForecastConfiguration {
  modelType?: ModelType;                    // Selected ML model
  dataUploadType?: DataUploadType;         // Raw vs preprocessed data
  features?: FeatureConfiguration;         // Feature engineering settings
  aggregation?: AggregationConfiguration; // Data aggregation settings
  dateRanges?: DateRangeConfiguration;    // Training/testing periods
  trainingParams?: ModelTrainingParams;   // Model training parameters
}
```

### Workflow Step Model

```typescript
// Individual step in the 8-step forecasting workflow
export interface WorkflowStep {
  stepNumber: number;                    // Step number (1-8)
  name: string;                         // Step display name
  status: 'pending' | 'in_progress' | 'completed' | 'skipped' | 'error'; // Current status
  isRequired: boolean;                  // Whether step is mandatory
  isAccessible: boolean;                // Whether step can be accessed
  completedAt?: string;                 // ISO 8601 completion timestamp
  data?: any;                          // Step-specific data
  validation?: StepValidation;         // Validation results
}

// Step validation results
export interface StepValidation {
  isValid: boolean;        // Overall validation status
  errors: string[];        // Validation error messages
  warnings: string[];      // Validation warnings
  requiredFields: string[]; // Required field names
  completedFields: string[]; // Completed field names
}
```

---

## Workflow Step Models

### Step 1: Data Upload

```typescript
// Data upload step configuration and state
export interface DataUploadStep {
  uploadType: DataUploadType;    // Raw or preprocessed data
  file?: UploadedFile;          // Uploaded file details
  validation: DataValidation;   // File validation results
  preview?: DataPreview;        // Data preview with statistics
  schema: DataSchema;           // Required data schema
}

// Uploaded file metadata
export interface UploadedFile {
  name: string;           // Original filename
  size: number;          // File size in bytes
  type: string;          // MIME type
  lastModified: number;  // Last modified timestamp
  data?: any[][];        // Parsed data rows (optional)
  headers?: string[];    // Column headers (optional)
}

// File validation results
export interface DataValidation {
  isValid: boolean;                    // Overall validation status
  errors: ValidationError[];          // Critical errors
  warnings: ValidationWarning[];      // Non-critical warnings
  requiredColumns: string[];          // Expected column names
  foundColumns: string[];             // Detected column names
  rowCount: number;                   // Total data rows
  dateRange?: { start: string; end: string }; // Data date range
}

// Validation error details
export interface ValidationError {
  type: 'missing_column' | 'invalid_format' | 'empty_data' | 'date_format' | 'data_type';
  message: string;        // Human-readable error message
  column?: string;        // Affected column name
  row?: number;          // Affected row number
}

// Validation warning details
export interface ValidationWarning {
  type: 'missing_values' | 'outliers' | 'date_gaps' | 'low_data_quality';
  message: string;        // Human-readable warning message
  column?: string;        // Affected column name
  count?: number;        // Number of affected records
}

// Required data schema definition
export interface DataSchema {
  requiredColumns: RequiredColumn[];     // Mandatory columns
  optionalColumns: OptionalColumn[];     // Optional columns
  constraints: SchemaConstraints;        // File and data constraints
}

// Required column specification
export interface RequiredColumn {
  name: string;           // Column name
  type: 'date' | 'string' | 'number'; // Expected data type
  description: string;    // Column purpose description
  format?: string;        // Expected format (for dates)
  example: string;        // Example value
}

// Optional column specification
export interface OptionalColumn {
  name: string;           // Column name
  type: 'date' | 'string' | 'number'; // Expected data type
  description: string;    // Column purpose description
  example?: string;       // Example value
}

// File and data constraints
export interface SchemaConstraints {
  maxFileSize: number;      // Maximum file size in bytes (100MB)
  supportedFormats: string[]; // Allowed file formats ['csv', 'xlsx', 'xls']
  maxSKUs: number;         // Maximum SKU count (5)
  maxDepots: number;       // Maximum depot count (5)
  minRows: number;         // Minimum required rows
  maxRows: number;         // Maximum allowed rows
}

// Data preview with statistics
export interface DataPreview {
  headers: string[];               // Column headers
  rows: any[][];                  // Sample data rows
  statistics: ColumnStatistics[]; // Per-column statistics
  totalRows: number;              // Total row count
  displayRows: number;            // Number of preview rows shown
}

// Statistical analysis per column
export interface ColumnStatistics {
  column: string;                 // Column name
  type: 'numeric' | 'categorical' | 'date'; // Detected data type
  count: number;                  // Non-null value count
  nullCount: number;              // Null value count
  uniqueCount: number;            // Unique value count
  min?: any;                      // Minimum value
  max?: any;                      // Maximum value
  mean?: number;                  // Mean (numeric only)
  examples: any[];                // Sample values
}
```

### Step 2: Data Analysis (EDA)

```typescript
// Exploratory Data Analysis results
export interface DataAnalysisStep {
  overview: OverviewAnalysis;           // Summary statistics
  temporal: TemporalAnalysis;          // Time series analysis
  distribution: DistributionAnalysis;  // Data distribution analysis
  categorical: CategoricalAnalysis;    // Categorical variable analysis
  relationships: RelationshipAnalysis; // Feature relationships
  dataQuality: DataQualityAnalysis;   // Data quality assessment
}

// Overview analysis results
export interface OverviewAnalysis {
  summaryStats: SummaryStatistics;    // Basic statistics
  dataShape: { rows: number; columns: number }; // Dataset dimensions
  dataTypes: { [column: string]: string }; // Column data types
  missingValues: { [column: string]: number }; // Missing value counts
}

// Summary statistics per column
export interface SummaryStatistics {
  [column: string]: {
    count: number;        // Non-null count
    mean?: number;        // Mean value (numeric)
    std?: number;         // Standard deviation (numeric)
    min?: any;           // Minimum value
    max?: any;           // Maximum value
    quartiles?: number[]; // Q1, Q2, Q3 (numeric)
  };
}

// Time series analysis results
export interface TemporalAnalysis {
  timeSeriesData: TimeSeriesPoint[];  // Time series data points
  seasonality: SeasonalityAnalysis;   // Seasonal patterns
  trends: TrendAnalysis;              // Trend analysis
  patterns: PatternDetection;         // Pattern detection results
}

// Individual time series data point
export interface TimeSeriesPoint {
  date: string;      // ISO 8601 date
  value: number;     // Demand value
  depot?: string;    // Depot identifier
  sku?: string;      // SKU identifier
}

// Seasonality detection results
export interface SeasonalityAnalysis {
  hasSeasonality: boolean;            // Whether seasonality is detected
  seasonalPeriod?: number;            // Period length (e.g., 7 for weekly)
  seasonalStrength?: number;          // Strength of seasonal pattern (0-1)
  seasonalComponents?: { period: string; strength: number }[]; // Multiple seasonal components
}

// Trend analysis results
export interface TrendAnalysis {
  trendDirection: 'increasing' | 'decreasing' | 'stable'; // Overall trend direction
  trendStrength: number;              // Trend strength (0-1)
  changePoints?: string[];            // Detected trend change dates
}

// Pattern detection results
export interface PatternDetection {
  patterns: DetectedPattern[];        // Identified patterns
  anomalies: AnomalyDetection[];     // Detected anomalies
}

// Individual detected pattern
export interface DetectedPattern {
  type: 'weekly' | 'monthly' | 'quarterly' | 'yearly' | 'custom'; // Pattern type
  description: string;                // Human-readable description
  strength: number;                   // Pattern strength (0-1)
  confidence: number;                 // Detection confidence (0-1)
}

// Anomaly detection result
export interface AnomalyDetection {
  date: string;          // Date of anomaly
  value: number;         // Observed value
  expectedValue: number; // Expected value
  severity: 'low' | 'medium' | 'high'; // Anomaly severity
  reason: string;        // Explanation of anomaly
}
```

### Step 3: Model Selection

```typescript
// Model selection step data
export interface ModelSelectionStep {
  availableModels: ModelOption[];      // Available model options
  selectedModel?: ModelType;           // Currently selected model
  recommendation: ModelRecommendation; // AI recommendation
  comparison: ModelComparison;         // Model comparison matrix
}

// Individual model option
export interface ModelOption {
  type: ModelType;                     // Model type identifier
  name: string;                        // Display name
  description: string;                 // Model description
  complexity: 'low' | 'medium' | 'high'; // Implementation complexity
  interpretability: 'low' | 'medium' | 'high'; // Model interpretability
  trainingTime: 'fast' | 'medium' | 'slow'; // Expected training time
  accuracy: 'good' | 'better' | 'best'; // Expected accuracy tier
  dataRequirements: DataRequirement;  // Data requirements
  suitability: number;                 // Suitability score (0-100)
}

// Model data requirements
export interface DataRequirement {
  minDataPoints: number;          // Minimum required data points
  handlesMissingValues: boolean;  // Can handle missing values
  handlesSeasonality: boolean;    // Can detect/handle seasonality
  handlesNonLinearity: boolean;   // Can model non-linear relationships
  requiresFeatureEngineering: boolean; // Needs feature engineering
}

// AI model recommendation
export interface ModelRecommendation {
  recommendedModel: ModelType;         // Top recommended model
  confidence: number;                  // Recommendation confidence (0-1)
  reasoning: string[];                 // Reasons for recommendation
  alternatives: { model: ModelType; score: number; reason: string }[]; // Alternative options
}

// Model comparison matrix
export interface ModelComparison {
  criteria: ComparisonCriteria[];      // Comparison criteria
  scores: { [model: string]: { [criteria: string]: number } }; // Score matrix
  recommendation: string;              // Overall recommendation
}

// Model comparison criteria
export interface ComparisonCriteria {
  name: string;         // Criteria name
  weight: number;       // Criteria weight (0-1)
  description: string;  // Criteria description
}
```

### Step 4: Feature Selection

```typescript
// Feature selection and engineering configuration
export interface FeatureSelectionStep {
  temporalFeatures: TemporalFeatures;     // Date/time-based features
  lagFeatures: LagFeatures;               // Historical lag features
  rollingFeatures: RollingFeatures;       // Rolling window features
  externalFeatures: ExternalFeatures;     // External data features
  advancedFeatures: AdvancedFeatures;     // Advanced feature engineering
  selectedFeatures: string[];             // Currently selected features
  featureImportance: FeatureImportancePreview[]; // Feature importance preview
}

// Complete feature configuration
export interface FeatureConfiguration {
  temporal: { [key: string]: boolean };   // Temporal feature flags
  lag: { [key: string]: number[] };       // Lag periods per feature
  rolling: { [key: string]: RollingConfig }; // Rolling window configs
  external: { [key: string]: boolean };   // External feature flags
  advanced: { [key: string]: any };       // Advanced feature configs
}

// Temporal (date/time) features
export interface TemporalFeatures {
  dayOfWeek: boolean;      // Day of week (1-7)
  dayOfMonth: boolean;     // Day of month (1-31)
  month: boolean;          // Month (1-12)
  quarter: boolean;        // Quarter (1-4)
  year: boolean;           // Year
  isWeekend: boolean;      // Weekend indicator
  isHoliday: boolean;      // Holiday indicator
  seasonality: boolean;    // Seasonal components
}

// Lag features configuration
export interface LagFeatures {
  demandLags: number[];                        // Demand lag periods
  externalLags: { [variable: string]: number[] }; // External variable lags
  autoCorrelationLags: number[];               // Auto-correlation based lags
}

// Rolling window features
export interface RollingFeatures {
  windows: number[];                           // Rolling window sizes
  statistics: ('mean' | 'std' | 'min' | 'max' | 'median')[]; // Statistics to compute
  rollingConfigs: { [feature: string]: RollingConfig }; // Per-feature configs
}

// Rolling window configuration
export interface RollingConfig {
  window: number;          // Window size
  statistic: string;       // Statistic type
  centerAlignment: boolean; // Center vs trailing alignment
}

// External data features
export interface ExternalFeatures {
  promotions: boolean;        // Promotional campaigns
  holidays: boolean;          // Holiday calendar
  weather: boolean;           // Weather data
  events: boolean;            // Special events
  economicIndicators: boolean; // Economic indicators
  customVariables: string[];   // Custom external variables
}

// Advanced feature engineering
export interface AdvancedFeatures {
  polynomialFeatures: { degree: number; features: string[] }; // Polynomial features
  interactionFeatures: string[][];        // Feature interactions
  customTransformations: CustomTransformation[]; // Custom transformations
}

// Custom feature transformation
export interface CustomTransformation {
  name: string;           // Transformation name
  expression: string;     // Mathematical expression
  description: string;    // Human-readable description
  inputFeatures: string[]; // Required input features
}

// Feature importance preview
export interface FeatureImportancePreview {
  feature: string;        // Feature name
  importance: number;     // Importance score (0-1)
  type: 'temporal' | 'lag' | 'rolling' | 'external' | 'advanced'; // Feature category
  description: string;    // Feature description
}
```

### Step 5: Aggregation

```typescript
// Data aggregation configuration
export interface AggregationStep {
  geographical: GeographicalAggregation;  // Geographic aggregation
  product: ProductAggregation;            // Product aggregation
  temporal: TemporalAggregationConfig;    // Temporal aggregation
  custom: CustomAggregation;              // Custom aggregation rules
  scope: ForecastScope;                   // Forecast scope settings
}

// Complete aggregation configuration
export interface AggregationConfiguration {
  geographical: AggregationLevel[];       // Geographic levels
  product: AggregationLevel[];            // Product levels
  temporal: TemporalAggregation;          // Temporal aggregation
  customRules: AggregationRule[];         // Custom aggregation rules
}

// Geographical aggregation options
export interface GeographicalAggregation {
  levels: GeographicalLevel[];            // Available geographic levels
  selectedLevel: AggregationLevel;        // Currently selected level
  hierarchy: GeographicalHierarchy;       // Geographic hierarchy
}

// Geographic level details
export interface GeographicalLevel {
  level: AggregationLevel;  // Aggregation level
  name: string;             // Display name
  description: string;      // Level description
  count: number;            // Number of entities at this level
  examples: string[];       // Example entity names
}

// Geographic hierarchy structure
export interface GeographicalHierarchy {
  depot: string[];    // Available depots
  region: string[];   // Available regions
  country: string[];  // Available countries
}

// Product aggregation options
export interface ProductAggregation {
  levels: ProductLevel[];              // Available product levels
  selectedLevel: AggregationLevel;     // Currently selected level
  hierarchy: ProductHierarchy;         // Product hierarchy
}

// Product level details
export interface ProductLevel {
  level: AggregationLevel;  // Aggregation level
  name: string;             // Display name
  description: string;      // Level description
  count: number;            // Number of entities at this level
  examples: string[];       // Example entity names
}

// Product hierarchy structure
export interface ProductHierarchy {
  sku: string[];      // Available SKUs
  category: string[]; // Available categories
  brand: string[];    // Available brands
}

// Temporal aggregation configuration
export interface TemporalAggregationConfig {
  level: TemporalAggregation;     // Selected temporal level
  options: TemporalOption[];      // Available temporal options
  customPeriods?: CustomPeriod[]; // Custom period definitions
}

// Temporal aggregation option
export interface TemporalOption {
  level: TemporalAggregation; // Aggregation level
  description: string;        // Level description
  forecastHorizon: number;   // Typical forecast horizon
  suitability: number;       // Suitability score (0-100)
}

// Custom temporal period
export interface CustomPeriod {
  name: string;        // Period name
  days: number;        // Period length in days
  description: string; // Period description
}

// Custom aggregation rules
export interface CustomAggregation {
  rules: AggregationRule[];             // Aggregation rules
  combinations: AggregationCombination[]; // Predefined combinations
}

// Individual aggregation rule
export interface AggregationRule {
  name: string;           // Rule name
  field: string;          // Field to aggregate
  operation: 'sum' | 'avg' | 'min' | 'max' | 'count'; // Aggregation operation
  groupBy: string[];      // Grouping fields
}

// Aggregation combination preset
export interface AggregationCombination {
  name: string;                     // Combination name
  geographical: AggregationLevel;   // Geographic level
  product: AggregationLevel;        // Product level
  temporal: TemporalAggregation;    // Temporal level
}

// Forecast scope configuration
export interface ForecastScope {
  horizon: number;                  // Forecast horizon (30-60 days)
  confidenceInterval: number;       // Confidence interval (95%, 99%)
  granularity: ScopeGranularity;    // Output granularity
  outputFormat: OutputFormat;       // Output format preferences
}

// Forecast granularity settings
export interface ScopeGranularity {
  geographical: string[];           // Geographic entities to forecast
  product: string[];                // Product entities to forecast
  temporal: TemporalAggregation;    // Temporal granularity
}

// Output format configuration
export interface OutputFormat {
  format: 'table' | 'chart' | 'export'; // Primary output format
  includeConfidenceIntervals: boolean;   // Include confidence bands
  includeFeatureImportance: boolean;     // Include feature importance
  includeMetrics: boolean;               // Include performance metrics
}
```

### Step 6: Date Ranges

```typescript
// Date range configuration step
export interface DateRangeStep {
  trainingPeriod: DatePeriod;           // Training data period
  testingPeriod: DatePeriod;            // Testing data period
  validationSplit: ValidationSplit;     // Validation configuration
  historicalData: HistoricalDataConfig; // Historical data usage
}

// Complete date range configuration
export interface DateRangeConfiguration {
  training: DatePeriod;                 // Training period
  testing: DatePeriod;                  // Testing period
  validation: ValidationSplit;          // Validation split
  crossValidation: CrossValidationConfig; // Cross-validation setup
}

// Date period specification
export interface DatePeriod {
  start: string;      // Start date (ISO 8601)
  end: string;        // End date (ISO 8601)
  duration: number;   // Duration in days
  dataPoints: number; // Number of data points in period
}

// Validation split configuration
export interface ValidationSplit {
  method: 'holdout' | 'rolling' | 'expanding'; // Validation method
  percentage: number;                           // Validation percentage
  periods: number;                              // Number of validation periods
  configuration: ValidationConfig;              // Detailed configuration
}

// Detailed validation configuration
export interface ValidationConfig {
  folds: number;     // Number of CV folds
  testSize: number;  // Test set size
  gap: number;       // Gap between train/test (days)
  purge: number;     // Purge period before test (days)
}

// Cross-validation configuration
export interface CrossValidationConfig {
  method: 'time_series' | 'blocked' | 'purged'; // CV method
  folds: number;                                 // Number of folds
  testRatio: number;                             // Test ratio per fold
  gapDays: number;                               // Gap between folds
}

// Historical data usage configuration
export interface HistoricalDataConfig {
  utilizationStrategy: 'all' | 'recent' | 'seasonal' | 'custom'; // Usage strategy
  maxHistory: number;                            // Maximum history (days)
  weightingScheme: 'uniform' | 'linear_decay' | 'exponential_decay'; // Weighting scheme
  seasonalAdjustment: boolean;                   // Apply seasonal adjustment
}
```

### Step 7: Model Training

```typescript
// Model training step data
export interface ModelTrainingStep {
  trainingConfig: ModelTrainingConfig;   // Training configuration
  progress: TrainingProgress;            // Current progress
  metrics: TrainingMetrics;              // Training metrics
  monitoring: TrainingMonitoring;        // Resource monitoring
}

// Model training parameters
export interface ModelTrainingParams {
  hyperparameters: { [key: string]: any }; // Model hyperparameters
  optimization: OptimizationConfig;        // Optimization settings
  resources: ResourceConfig;               // Resource allocation
  monitoring: MonitoringConfig;            // Monitoring configuration
}

// Training configuration
export interface ModelTrainingConfig {
  modelType: ModelType;                     // Selected model type
  hyperparameters: HyperparameterConfig;   // Hyperparameter settings
  optimization: OptimizationConfig;        // Optimization algorithm
  validation: TrainingValidationConfig;    // Validation configuration
}

// Model-specific hyperparameters
export interface HyperparameterConfig {
  [key: string]: any; // Model-specific parameters
}

// Optimization configuration
export interface OptimizationConfig {
  algorithm: 'grid_search' | 'random_search' | 'bayesian' | 'hyperband'; // Optimization algorithm
  maxIterations: number;                    // Maximum iterations
  earlyStoppingCriteria: EarlyStoppingCriteria; // Early stopping rules
  parallelJobs: number;                     // Parallel execution jobs
}

// Early stopping criteria
export interface EarlyStoppingCriteria {
  metric: string;     // Metric to monitor
  patience: number;   // Patience (iterations without improvement)
  minDelta: number;   // Minimum improvement threshold
  mode: 'min' | 'max'; // Whether lower or higher is better
}

// Training validation configuration
export interface TrainingValidationConfig {
  crossValidation: CrossValidationConfig; // Cross-validation setup
  metrics: string[];                      // Metrics to track
  scoringFunction: string;                // Primary scoring function
}

// Resource allocation configuration
export interface ResourceConfig {
  maxMemory: number;     // Maximum memory (MB)
  maxCpuCores: number;   // Maximum CPU cores
  maxTrainingTime: number; // Maximum training time (minutes)
  useGpu: boolean;       // Enable GPU acceleration
}

// Monitoring configuration
export interface MonitoringConfig {
  logLevel: 'info' | 'debug' | 'warning' | 'error'; // Log level
  checkpointFrequency: number;          // Checkpoint frequency (iterations)
  metricLogging: boolean;               // Enable metric logging
  progressUpdates: boolean;             // Enable progress updates
}

// Real-time training progress
export interface TrainingProgress {
  status: 'initializing' | 'training' | 'validating' | 'completed' | 'failed'; // Current status
  currentIteration: number;             // Current iteration
  totalIterations: number;              // Total iterations
  elapsedTime: number;                  // Elapsed time (seconds)
  estimatedTimeRemaining: number;       // ETA (seconds)
  currentMetrics: { [metric: string]: number }; // Current metric values
  stage: string;                        // Current training stage
}

// Comprehensive training metrics
export interface TrainingMetrics {
  accuracy: AccuracyMetrics;           // Accuracy measurements
  performance: PerformanceMetrics;     // Performance measurements
  validation: ValidationMetrics;       // Validation results
}

// Model accuracy metrics
export interface AccuracyMetrics {
  mae: number;    // Mean Absolute Error
  rmse: number;   // Root Mean Square Error
  mape: number;   // Mean Absolute Percentage Error
  r2: number;     // R-squared
  customMetrics: { [metric: string]: number }; // Custom metrics
}

// Performance metrics
export interface PerformanceMetrics {
  trainingTime: number;               // Training time (seconds)
  memoryUsage: number;                // Memory usage (MB)
  cpuUsage: number;                   // CPU usage (percentage)
  convergence: ConvergenceMetrics;    // Convergence analysis
}

// Model convergence metrics
export interface ConvergenceMetrics {
  hasConverged: boolean;              // Whether model converged
  convergenceIteration: number;       // Iteration at convergence
  finalLoss: number;                  // Final loss value
  learningCurve: LearningCurvePoint[]; // Learning curve data
}

// Learning curve data point
export interface LearningCurvePoint {
  iteration: number;      // Iteration number
  trainingLoss: number;   // Training loss
  validationLoss: number; // Validation loss
  timestamp: string;      // Timestamp (ISO 8601)
}

// Validation metrics
export interface ValidationMetrics {
  crossValidationScore: number;       // Overall CV score
  foldScores: number[];               // Per-fold scores
  consistency: number;                // Score consistency
  overfitting: OverfittingAnalysis;   // Overfitting analysis
}

// Overfitting analysis
export interface OverfittingAnalysis {
  isOverfitted: boolean;   // Whether model is overfitted
  trainingScore: number;   // Training set score
  validationScore: number; // Validation set score
  gap: number;             // Score gap
  recommendation: string;  // Recommendation to address overfitting
}

// Training monitoring data
export interface TrainingMonitoring {
  resourceUsage: ResourceUsage;       // Resource usage metrics
  progressLog: ProgressLogEntry[];    // Progress log entries
  alerts: TrainingAlert[];            // Training alerts
}

// Resource usage monitoring
export interface ResourceUsage {
  memory: UsageMetric;    // Memory usage
  cpu: UsageMetric;       // CPU usage
  disk: UsageMetric;      // Disk usage
  network?: UsageMetric;  // Network usage (optional)
}

// Usage metric details
export interface UsageMetric {
  current: number;  // Current usage
  peak: number;     // Peak usage
  average: number;  // Average usage
  unit: string;     // Measurement unit
}

// Progress log entry
export interface ProgressLogEntry {
  timestamp: string;                          // Entry timestamp
  level: 'info' | 'warning' | 'error' | 'debug'; // Log level
  message: string;                            // Log message
  details?: any;                              // Additional details
}

// Training alert
export interface TrainingAlert {
  type: 'resource' | 'performance' | 'error' | 'warning'; // Alert type
  severity: 'low' | 'medium' | 'high' | 'critical';      // Alert severity
  message: string;                            // Alert message
  timestamp: string;                          // Alert timestamp
  resolved: boolean;                          // Whether alert is resolved
}
```

### Step 8: Results & Visualization

```typescript
// Results and visualization step data
export interface ResultsVisualizationStep {
  overview: ResultsOverview;              // Results overview
  forecast: ForecastVisualization;        // Forecast visualizations
  performance: PerformanceVisualization;  // Performance visualizations
  insights: AIInsights;                   // AI-generated insights
}

// Complete forecast results
export interface ForecastResults {
  summary: ForecastSummary;              // Results summary
  predictions: ForecastPrediction[];     // Forecast predictions
  performance: ModelPerformance;         // Model performance
  visualizations: ForecastVisualization; // Visualization data
  insights: AIInsights;                  // AI insights
  export: ExportOptions;                 // Export options
}

// Forecast summary
export interface ForecastSummary {
  modelType: ModelType;        // Model used
  accuracy: number;            // Overall accuracy
  horizon: number;             // Forecast horizon
  confidenceLevel: number;     // Confidence level
  totalPredictions: number;    // Number of predictions
  trainingPeriod: DatePeriod;  // Training period used
  features: string[];          // Features used
  aggregationLevel: string;    // Aggregation level
}

// Individual forecast prediction
export interface ForecastPrediction {
  date: string;         // Prediction date
  predicted: number;    // Predicted value
  lowerBound: number;   // Lower confidence bound
  upperBound: number;   // Upper confidence bound
  confidence: number;   // Prediction confidence
  actual?: number;      // Actual value (if available)
  residual?: number;    // Residual (actual - predicted)
  trend?: number;       // Trend component
  seasonal?: number;    // Seasonal component
}

// Comprehensive model performance
export interface ModelPerformance {
  accuracy: AccuracyMetrics;           // Accuracy metrics
  stability: StabilityMetrics;         // Stability metrics
  robustness: RobustnessMetrics;       // Robustness metrics
  featureImportance: FeatureImportanceResult[]; // Feature importance
}

// Model stability metrics
export interface StabilityMetrics {
  varianceExplained: number;    // Explained variance
  residualVariance: number;     // Residual variance
  predictionStability: number;  // Prediction stability
  seasonalStability: number;    // Seasonal component stability
}

// Model robustness metrics
export interface RobustnessMetrics {
  outlierSensitivity: number;   // Sensitivity to outliers
  noiseResistance: number;      // Resistance to noise
  missingDataHandling: number;  // Missing data handling capability
  distributionShift: number;    // Robustness to distribution shift
}

// Feature importance result
export interface FeatureImportanceResult {
  feature: string;        // Feature name
  importance: number;     // Importance score
  rank: number;           // Importance rank
  category: string;       // Feature category
  interpretation: string; // Human-readable interpretation
}

// Forecast visualization data
export interface ForecastVisualization {
  timeSeriesChart: TimeSeriesChartData;           // Time series chart
  residualPlots: ResidualPlotData;                // Residual analysis plots
  featureImportanceChart: FeatureImportanceChartData; // Feature importance chart
  performanceCharts: PerformanceChartData;        // Performance charts
}

// Time series chart data
export interface TimeSeriesChartData {
  historical: TimeSeriesPoint[];      // Historical data
  predicted: TimeSeriesPoint[];       // Predicted data
  confidenceIntervals: ConfidenceInterval[]; // Confidence intervals
  annotations: ChartAnnotation[];      // Chart annotations
}

// Confidence interval data
export interface ConfidenceInterval {
  date: string;    // Date
  lower: number;   // Lower bound
  upper: number;   // Upper bound
  level: number;   // Confidence level (95%, 99%)
}

// Chart annotation
export interface ChartAnnotation {
  date: string;                               // Annotation date
  value: number;                              // Value at date
  label: string;                              // Annotation label
  type: 'event' | 'anomaly' | 'change_point'; // Annotation type
}

// Residual analysis plot data
export interface ResidualPlotData {
  residuals: ResidualPoint[];           // Residual data points
  normalityTest: NormalityTest;         // Normality test results
  autocorrelation: AutocorrelationData; // Autocorrelation analysis
}

// Individual residual point
export interface ResidualPoint {
  predicted: number;     // Predicted value
  residual: number;      // Residual value
  standardized: number;  // Standardized residual
  date: string;          // Date
}

// Normality test results
export interface NormalityTest {
  statistic: number;  // Test statistic
  pValue: number;     // P-value
  isNormal: boolean;  // Whether residuals are normal
  method: string;     // Test method used
}

// Autocorrelation analysis data
export interface AutocorrelationData {
  lags: number[];           // Lag values
  values: number[];         // Autocorrelation values
  confidenceBounds: number[]; // Confidence bounds
}

// Feature importance chart data
export interface FeatureImportanceChartData {
  features: string[];   // Feature names
  importance: number[]; // Importance values
  categories: string[]; // Feature categories
  colors: string[];     // Chart colors
}

// Performance chart data
export interface PerformanceChartData {
  learningCurve: LearningCurvePoint[];      // Learning curve
  validationCurve: ValidationCurvePoint[];  // Validation curve
  residualDistribution: DistributionData;   // Residual distribution
}

// Validation curve point
export interface ValidationCurvePoint {
  parameterValue: any;    // Parameter value
  trainingScore: number;  // Training score
  validationScore: number; // Validation score
}

// Distribution data
export interface DistributionData {
  bins: number[];     // Histogram bins
  counts: number[];   // Bin counts
  density: number[];  // Probability density
}

// AI-generated insights
export interface AIInsights {
  summary: InsightSummary;         // Insight summary
  recommendations: Recommendation[]; // Actionable recommendations
  patterns: DiscoveredPattern[];   // Discovered patterns
  warnings: InsightWarning[];      // Warnings and concerns
}

// Insight summary
export interface InsightSummary {
  keyFindings: string[];                    // Key findings
  modelQuality: 'excellent' | 'good' | 'fair' | 'poor'; // Overall quality
  confidenceLevel: number;                  // Confidence in results
  actionableInsights: number;               // Number of actionable insights
}

// AI recommendation
export interface Recommendation {
  category: 'model' | 'data' | 'features' | 'business'; // Recommendation category
  priority: 'high' | 'medium' | 'low';     // Priority level
  title: string;                            // Recommendation title
  description: string;                      // Detailed description
  impact: string;                           // Expected impact
  effort: 'low' | 'medium' | 'high';       // Implementation effort
  reasoning: string[];                      // Reasoning behind recommendation
}

// Discovered pattern
export interface DiscoveredPattern {
  type: 'trend' | 'seasonality' | 'cyclical' | 'anomaly' | 'correlation'; // Pattern type
  description: string;    // Pattern description
  strength: number;       // Pattern strength
  confidence: number;     // Detection confidence
  period?: string;        // Pattern period (if applicable)
  impact: string;         // Business impact
}

// AI insight warning
export interface InsightWarning {
  level: 'info' | 'warning' | 'critical';              // Warning level
  category: 'data_quality' | 'model_performance' | 'business_logic'; // Warning category
  message: string;                                      // Warning message
  recommendation: string;                               // Recommended action
  impact: string;                                       // Potential impact
}

// Export options
export interface ExportOptions {
  formats: ExportFormat[];          // Available formats
  includeVisualizations: boolean;   // Include charts/graphs
  includeRawData: boolean;          // Include raw data
  includeMetadata: boolean;         // Include metadata
}

// Export format option
export interface ExportFormat {
  type: 'csv' | 'excel' | 'json' | 'pdf' | 'png'; // Format type
  description: string;              // Format description
  available: boolean;               // Whether format is available
}
```

---

## API Request/Response Models

### Basic API Response Structure

```typescript
// Generic API response wrapper
export interface ApiResponse<T> {
  data: T;              // Response data
  success: boolean;     // Operation success flag
  message?: string;     // Human-readable message
  errors?: string[];    // Error messages (if any)
}

// Paginated response structure
export interface PaginatedResponse<T> {
  data: T[];                    // Data items
  pagination: PaginationInfo;   // Pagination metadata
  totalCount: number;           // Total item count
  hasMore: boolean;             // Whether more items exist
}

// Pagination metadata
export interface PaginationInfo {
  currentPage: number;    // Current page number (1-based)
  totalPages: number;     // Total number of pages
  pageSize: number;       // Items per page
  totalCount: number;     // Total item count
  hasNext: boolean;       // Whether next page exists
  hasPrevious: boolean;   // Whether previous page exists
}
```

### Activity Management Requests

```typescript
// Create activity request
export interface CreateActivityRequest {
  name: string;                  // Activity name (required, 3-100 chars)
  client: string;                // Client name (required)
  project: string;               // Project name (required)
  description?: string;          // Description (optional, max 500 chars)
  industry?: string;             // Industry (optional)
  priority: ActivityPriority;    // Priority (required)
}

// Create activity response
export interface CreateActivityResponse {
  activity: Activity;     // Created activity
  success: boolean;       // Success flag
  message: string;        // Response message
}

// Update activity request
export interface UpdateActivityRequest {
  name?: string;                 // New name (optional)
  description?: string;          // New description (optional)
  industry?: string;             // New industry (optional)
  priority?: ActivityPriority;   // New priority (optional)
  status?: ActivityStatus;       // New status (optional)
}

// Activity filter parameters
export interface ActivityFilters {
  search?: string;               // Search term
  status?: ActivityStatus[];     // Status filter
  priority?: ActivityPriority[]; // Priority filter
  client?: string[];             // Client filter
  project?: string[];            // Project filter
  industry?: string[];           // Industry filter
  dateRange?: { from: string; to: string }; // Date range filter
  sortBy?: 'name' | 'createdAt' | 'lastModified' | 'priority' | 'status'; // Sort field
  sortOrder?: 'asc' | 'desc';    // Sort direction
}

// Filter options response
export interface FilterOptions {
  statuses: ActivityStatus[];    // Available statuses
  priorities: ActivityPriority[]; // Available priorities
  clients: string[];             // Available clients
  projects: string[];            // Available projects
  industries: string[];          // Available industries
}
```

### File Upload Models

```typescript
// File upload request
export interface FileUploadRequest {
  file: File;               // File to upload
  uploadType: DataUploadType; // Upload type
  activityId: string;       // Activity ID
  forecastId: string;       // Forecast ID
}

// File upload response
export interface FileUploadResponse {
  fileId: string;           // Uploaded file ID
  validation: DataValidation; // Validation results
  preview: DataPreview;     // Data preview
  success: boolean;         // Upload success
  message: string;          // Response message
}
```

### Version Management Models

```typescript
// Version history response
export interface VersionHistory {
  versions: ForecastVersion[];  // All versions
  totalVersions: number;        // Total version count
  latestVersion: string;        // Latest version identifier
}

// Individual forecast version
export interface ForecastVersion {
  id: string;                   // Version ID
  version: string;              // Version identifier
  status: ForecastStatus;       // Version status
  accuracy?: number;            // Model accuracy (if completed)
  createdAt: string;            // Creation timestamp
  createdBy: string;            // Creator email
  modelType: ModelType;         // Model type used
  trainingDuration: number;     // Training duration (seconds)
  configuration: ForecastConfiguration; // Configuration used
  metrics: AccuracyMetrics;     // Performance metrics
  isActive: boolean;            // Whether version is active
  notes?: string;               // Optional notes
}
```

---

## Dashboard & Statistics Models

### Dashboard Statistics

```typescript
// Dashboard statistics response
export interface DashboardStatistics {
  totalActivities: number;      // Total activity count
  totalForecasts: number;       // Total forecast count
  activeActivities: number;     // Active activity count
  uniqueClients: number;        // Unique client count
  trends: StatisticsTrends;     // Trending statistics
}

// Statistics trends
export interface StatisticsTrends {
  activitiesGrowth: number;     // Activity growth percentage
  forecastsGrowth: number;      // Forecast growth percentage
  clientsGrowth: number;        // Client growth percentage
  accuracyTrend: number;        // Average accuracy trend
}
```

---

## Real-time Event Models

### WebSocket Events

```typescript
// Generic WebSocket event
export interface WebSocketEvent {
  type: 'activity_update' | 'forecast_update' | 'training_progress' | 'system_notification'; // Event type
  data: any;                    // Event data
  timestamp: string;            // Event timestamp
  userId?: string;              // User ID (optional)
}

// Activity update event
export interface ActivityUpdateEvent {
  activityId: string;   // Activity ID
  field: string;        // Updated field
  oldValue: any;        // Previous value
  newValue: any;        // New value
  updatedBy: string;    // User who made the update
}

// Forecast update event
export interface ForecastUpdateEvent {
  forecastId: string;   // Forecast ID
  activityId: string;   // Parent activity ID
  status: ForecastStatus; // New status
  step?: number;        // Current step (optional)
  progress?: number;    // Progress percentage (optional)
}

// Training progress event
export interface TrainingProgressEvent {
  forecastId: string;             // Forecast ID
  progress: TrainingProgress;     // Progress details
  metrics: Partial<TrainingMetrics>; // Current metrics
}
```

---

## Utility Models

### Search and Filtering

```typescript
// Search result
export interface SearchResult<T> {
  items: T[];           // Matching items
  totalCount: number;   // Total match count
  query: string;        // Search query
  filters: any;         // Applied filters
}

// Sort configuration
export interface SortConfig {
  field: string;                // Sort field
  direction: 'asc' | 'desc';    // Sort direction
}
```

### Error Handling

```typescript
// API error response
export interface ApiErrorResponse {
  success: false;       // Always false for errors
  message: string;      // Error message
  errors?: string[];    // Detailed error list
  code?: string;        // Error code
  timestamp?: string;   // Error timestamp
}

// Validation error
export interface ValidationErrorDetail {
  field: string;        // Field with error
  message: string;      // Error message
  code: string;         // Error code
  value?: any;          // Invalid value
}
```

### Metadata and Configuration

```typescript
// System configuration
export interface SystemConfig {
  maxFileSize: number;          // Maximum file size
  supportedFormats: string[];   // Supported file formats
  modelLimits: ModelLimits;     // Model-specific limits
  features: FeatureFlags;       // Feature flags
}

// Model limits
export interface ModelLimits {
  maxSKUs: number;              // Maximum SKUs
  maxDepots: number;            // Maximum depots
  maxTrainingTime: number;      // Maximum training time
  maxDataPoints: number;        // Maximum data points
}

// Feature flags
export interface FeatureFlags {
  advancedFeatures: boolean;    // Advanced features enabled
  realTimeUpdates: boolean;     // Real-time updates enabled
  exportFormats: string[];      // Available export formats
  modelTypes: ModelType[];      // Available model types
}
```

---

## Model Validation Rules

### Field Constraints

```typescript
// Activity name constraints
const ACTIVITY_NAME_CONSTRAINTS = {
  minLength: 3,
  maxLength: 100,
  required: true,
  unique: true // Within client-project combination
};

// Description constraints
const DESCRIPTION_CONSTRAINTS = {
  maxLength: 500,
  required: false
};

// File upload constraints
const FILE_UPLOAD_CONSTRAINTS = {
  maxSize: 100 * 1024 * 1024, // 100MB
  supportedTypes: ['text/csv', 'application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'],
  requiredColumns: ['Date', 'SKUID', 'Depot', 'Demand', 'Region']
};

// Forecast limits
const FORECAST_LIMITS = {
  maxSKUs: 5,
  maxDepots: 5,
  minDataPoints: 100,
  maxDataPoints: 1000000,
  horizonRange: { min: 30, max: 60 } // days
};
```

---

## Usage Examples

### Creating an Activity

```typescript
const createRequest: CreateActivityRequest = {
  name: "Q4 Electronics Demand Forecast",
  client: "TechCorp Solutions", 
  project: "Product Demand Optimization",
  description: "Quarterly demand forecasting for consumer electronics portfolio",
  industry: "Technology",
  priority: "High"
};

// API call
const response: CreateActivityResponse = await activityService.createActivity(createRequest);
```

### Filtering Activities

```typescript
const filters: ActivityFilters = {
  search: "electronics",
  status: ["Active", "Completed"],
  priority: ["High"],
  dateRange: {
    from: "2024-01-01",
    to: "2024-12-31"
  },
  sortBy: "lastModified",
  sortOrder: "desc"
};

const activities: Activity[] = await activityService.getActivities(filters);
```

### Uploading Data

```typescript
const uploadRequest: FileUploadRequest = {
  file: selectedFile,
  uploadType: "raw",
  activityId: "act_001",
  forecastId: "forecast_001"
};

const uploadResponse: FileUploadResponse = await dataService.uploadFile(uploadRequest);
if (uploadResponse.validation.isValid) {
  // Proceed to next step
  console.log("Data uploaded successfully:", uploadResponse.preview);
} else {
  // Handle validation errors
  console.error("Validation errors:", uploadResponse.validation.errors);
}
```

---

This comprehensive data model documentation ensures type safety throughout the Dynamic Forecasting Engine implementation and provides clear contracts for API integration.