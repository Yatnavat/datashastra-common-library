# Dynamic Forecasting Service - Identified Entities Documentation

## Overview

This document provides a comprehensive inventory of entities required for the Dynamic Forecasting Service, designed to support Business Intelligence and Predictive Analytics domain operations. The entities follow Spring Boot 3.5.4 + Java 21 patterns with multi-tenant SaaS architecture and global usage considerations.

## Entity Design Principles

1. **Base Classes**: All entities extend `BaseEntity` or `AbstractTenancyDomain` from common library
2. **Multi-Tenancy**: Business entities use `@TenantId` for row-level tenant isolation
3. **Audit Support**: Automatic tracking of creation/modification timestamps and users
4. **Global Compatibility**: Support for international usage, multiple currencies, timezones
5. **Performance**: Designed for time series data with proper indexing strategies
6. **Validation**: Comprehensive validation rules for forecasting domain requirements

---

## Entity Inventory by Domain

### Core Forecasting Domain

#### 1. Activity (activity)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Main container for forecasting activities within client projects
**Table**: `activity`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `name` (String, 3-100 chars, @NotBlank) - Activity name, unique within client-project
- `description` (String, max 500 chars) - Optional activity description
- `priority` (ActivityPriority enum) - HIGH, MEDIUM, LOW
- `status` (ActivityStatus enum) - ACTIVE, COMPLETED, ON_HOLD, DRAFT, ARCHIVED
- `industry` (String) - Industry classification
- `forecastCount` (Integer, default 0) - Number of associated forecasts

**Relationships**:
- `clientId` (Long, @ManyToOne, @NotNull) - FK to Client
- `projectId` (Long, @ManyToOne, @NotNull) - FK to Project
- `createdByUserId` (Long, @ManyToOne) - FK to User

**Validation Requirements**:
- Unique name within client-project combination
- Priority is required
- Client and project must exist and be active

**Indexing Strategy**:
- Primary: (id, tenantId)
- Composite: (clientId, projectId, tenantId)
- Performance: (status, tenantId), (createdDate, tenantId)

**Audit Requirements**: Full audit trail (created/modified dates and users)

#### 2. Forecast (forecast)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Individual forecast execution instances within activities
**Table**: `forecast`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `version` (String, @NotBlank) - Version identifier (e.g., "v1.0", "v2.1")
- `status` (ForecastStatus enum) - DRAFT, IN_PROGRESS, COMPLETED, FAILED, CANCELLED
- `accuracy` (BigDecimal, precision=5, scale=2) - Model accuracy percentage (0-100)
- `trainingDuration` (Long) - Training duration in seconds
- `currentStep` (Integer, default 1) - Current workflow step (1-8)
- `forecastType` (ForecastType enum) - DEMAND_FORECAST, INVENTORY_FORECAST, SALES_FORECAST
- `completedAt` (LocalDateTime) - Completion timestamp
- `errorMessage` (String, max 1000 chars) - Error details if failed

**Relationships**:
- `activityId` (Long, @ManyToOne, @NotNull) - FK to Activity
- `uploadedDataId` (Long, @OneToOne) - FK to UploadedData
- `transformedDataId` (Long, @OneToOne) - FK to TransformedData
- `modelId` (Long, @ManyToOne) - FK to Models
- `featureConfigId` (Long, @OneToOne) - FK to FeatureConfiguration
- `aggregationConfigId` (Long, @OneToOne) - FK to AggregationConfiguration
- `dateRangeConfigId` (Long, @OneToOne) - FK to DateRangeConfiguration
- `resultId` (Long, @OneToOne) - FK to ForecastResult
- `exploratoryAnalyticsId` (Long, @OneToOne) - FK to ExploratoryDataAnalytics

**Validation Requirements**:
- Version unique within activity
- Accuracy between 0-100 if status is COMPLETED
- Current step between 1-8

**Versioning Strategy**: Timestamp-based with explicit version labels

**Performance Considerations**:
- Lazy loading for large JSON fields
- Separate tables for large analysis data
- Batch processing for forecast generation

#### 3. Models (models)
**Base Class**: `BaseEntity`
**Purpose**: Available machine learning models for forecasting
**Table**: `models`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `modelName` (String, @NotBlank) - Display name of model
- `modelDescription` (String, max 1000 chars) - Detailed description
- `modelType` (ModelType enum) - ARIMA, PROPHET, XGBOOST, LSTM, LIGHTGBM
- `modelIcon` (String) - Icon URL or identifier
- `complexity` (ModelComplexity enum) - LOW, MEDIUM, HIGH
- `accuracy` (String) - Expected accuracy tier (GOOD, BETTER, BEST)
- `estimatedTime` (String) - Training time category (FAST, MEDIUM, SLOW)
- `bestFor` (String, max 500 chars) - Use case description
- `isRecommended` (Boolean, default false) - System recommendation flag
- `isActive` (Boolean, default true) - Availability status
- `minDataPoints` (Integer) - Minimum required data points
- `handlesSeasonality` (Boolean) - Seasonal pattern capability
- `handlesMissingValues` (Boolean) - Missing data tolerance

**Business Logic**:
- System-managed entity (admin interface)
- Used for model selection step
- Supports AI-powered recommendations

**Global Considerations**:
- Model descriptions in multiple languages
- Performance characteristics per region

### Data Management Domain

#### 4. UploadedData (uploaded_data)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Metadata for uploaded forecast data files
**Table**: `uploaded_data`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `fileName` (String, @NotBlank) - Original filename
- `fileSize` (Long) - File size in bytes
- `url` (String, @NotBlank) - Storage URL/path
- `timestamp` (LocalDateTime, @CreationTimestamp) - Upload timestamp
- `uploadDataType` (UploadDataType enum) - RAW, PREPROCESSED
- `contentType` (String) - MIME type
- `checksum` (String) - File integrity checksum
- `dataSchemaRequirement` (String, @Lob) - Required schema definition
- `validationResults` (@Column(columnDefinition = "JSON")) - Validation results JSON

**Relationships**:
- `uploadedByUserId` (Long, @ManyToOne) - FK to User
- `activityId` (Long, @ManyToOne) - FK to Activity

**Validation Requirements**:
- Supported file formats: CSV, XLSX, XLS
- Maximum file size: 100MB
- Required columns: Date, SKUID, Depot, Demand, Region

**Data Retention**: Configurable retention policy (default: 2 years)

#### 5. TransformedData (transformed_data)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Processed and transformed data for ML operations
**Table**: `transformed_data`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `dataOverview` (@Column(columnDefinition = "JSON")) - Data overview JSON
- `dataStatistics` (@Column(columnDefinition = "JSON")) - Statistical analysis JSON
- `dataSample` (@Column(columnDefinition = "JSON")) - Sample data records JSON
- `transformationLog` (String, @Lob) - Processing log
- `rowCount` (Long) - Total processed rows
- `columnCount` (Integer) - Total columns
- `transformedAt` (LocalDateTime) - Processing timestamp
- `dataQualityScore` (BigDecimal, precision=5, scale=2) - Quality score 0-100

**Relationships**:
- `originalDataId` (Long, @OneToOne) - FK to UploadedData

**Performance Considerations**:
- JSON fields for flexible schema storage
- Indexing on data quality score
- Caching strategies for frequently accessed samples

#### 6. ExploratoryDataAnalytics (exploratory_data_analytics)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Comprehensive data analysis results
**Table**: `exploratory_data_analytics`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `dataOverview` (@Column(columnDefinition = "JSON")) - Overview analysis JSON
- `dataQuality` (@Column(columnDefinition = "JSON")) - Quality metrics JSON
- `temporalAnalysis` (@Column(columnDefinition = "JSON")) - Time series analysis JSON
- `distributionalAnalysis` (@Column(columnDefinition = "JSON")) - Distribution analysis JSON
- `categoricalAnalysis` (@Column(columnDefinition = "JSON")) - Categorical analysis JSON
- `relationshipCorrelations` (@Column(columnDefinition = "JSON")) - Correlation analysis JSON
- `analysisCompletedAt` (LocalDateTime) - Analysis completion time
- `overallCompleteness` (BigDecimal, precision=5, scale=2) - Data completeness %
- `qualityScore` (BigDecimal, precision=5, scale=2) - Overall quality score

**Relationships**:
- `transformedDataId` (Long, @OneToOne) - FK to TransformedData

**Caching Strategy**: Analysis results cached for 24 hours

### Configuration Domain

#### 7. FeatureConfiguration (feature_configuration)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Feature engineering and selection configuration
**Table**: `feature_configuration`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `temporalFeatures` (@Column(columnDefinition = "JSON")) - Temporal feature config JSON
- `lagFeatures` (@Column(columnDefinition = "JSON")) - Lag feature config JSON
- `rollingFeatures` (@Column(columnDefinition = "JSON")) - Rolling window config JSON
- `externalFeatures` (@Column(columnDefinition = "JSON")) - External data config JSON
- `advancedFeatures` (@Column(columnDefinition = "JSON")) - Advanced features JSON
- `selectedFeatureCount` (Integer) - Number of selected features
- `estimatedTrainingTime` (Integer) - Estimated training time in minutes
- `featureImportanceThreshold` (BigDecimal, precision=3, scale=2) - Importance threshold

**Validation Rules**:
- Minimum 1 feature required
- Feature importance threshold 0.0-1.0
- Maximum 50 features for performance

#### 8. AggregationConfiguration (aggregation_configuration)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Data aggregation and forecasting scope settings
**Table**: `aggregation_configuration`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `geographicalAggregationLevels` (@Column(columnDefinition = "JSON")) - Geographic config JSON
- `productAggregationLevels` (@Column(columnDefinition = "JSON")) - Product config JSON
- `temporalAggregationLevel` (@Column(columnDefinition = "JSON")) - Temporal config JSON
- `geographicalAggregationDetails` (@Column(columnDefinition = "JSON")) - Detailed geo config
- `productAggregationDetails` (@Column(columnDefinition = "JSON")) - Detailed product config
- `temporalAggregationDetails` (@Column(columnDefinition = "JSON")) - Detailed temporal config
- `forecastHorizon` (@Column(columnDefinition = "JSON")) - Forecast horizon config
- `selectedSKUCount` (Integer) - Number of selected SKUs
- `selectedDepotCount` (Integer) - Number of selected depots
- `minimumThresholdDropDown` (@Column(columnDefinition = "JSON")) - Threshold options

**Validation Rules**:
- Maximum 5 SKUs, 5 depots
- Forecast horizon 30-60 days
- Valid aggregation combinations

#### 9. DateRangeConfiguration (date_range_configuration)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Training and testing date range configuration
**Table**: `date_range_configuration`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `trainingDateRange` (@Column(columnDefinition = "JSON")) - Training period JSON
- `testingDateRange` (@Column(columnDefinition = "JSON")) - Testing period JSON
- `validationSplit` (BigDecimal, precision=3, scale=2) - Validation split percentage
- `crossValidationFolds` (Integer) - Number of CV folds
- `timeSeriesValidation` (Boolean) - Time series specific validation
- `seasonalAdjustment` (Boolean) - Apply seasonal adjustment
- `dataUtilizationRatio` (BigDecimal, precision=3, scale=2) - Percentage of data used

**Global Considerations**:
- Timezone-aware date handling
- Regional calendar support (holidays, business days)

### Results Domain

#### 10. ForecastResult (forecast_result)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Comprehensive forecast results and analysis
**Table**: `forecast_result`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `modelResultAnalysis` (@Column(columnDefinition = "JSON")) - Model analysis JSON
- `modelPerformanceSummary` (@Column(columnDefinition = "JSON")) - Performance summary JSON
- `historicalModelFit` (@Column(columnDefinition = "JSON")) - Historical fit data JSON
- `forecastData` (@Column(columnDefinition = "JSON")) - Forecast predictions JSON
- `confidenceIntervals` (@Column(columnDefinition = "JSON")) - Confidence bands JSON
- `featureImportance` (@Column(columnDefinition = "JSON")) - Feature importance JSON
- `insights` (@Column(columnDefinition = "JSON")) - AI insights JSON
- `generatedAt` (LocalDateTime) - Result generation timestamp
- `expiresAt` (LocalDateTime) - Result expiry timestamp

**Performance Metrics**:
- `accuracy` (BigDecimal, precision=5, scale=2) - Overall accuracy %
- `mae` (BigDecimal, precision=10, scale=4) - Mean Absolute Error
- `rmse` (BigDecimal, precision=10, scale=4) - Root Mean Square Error
- `mape` (BigDecimal, precision=5, scale=2) - Mean Absolute Percentage Error
- `r2Score` (BigDecimal, precision=5, scale=4) - R-squared score

**Relationships**:
- `forecastId` (Long, @OneToOne) - FK to Forecast

**Data Retention**: Results retained based on business requirements

#### 11. ValidationResults (validation_results)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Data validation and quality assessment results
**Table**: `validation_results`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `totalRows` (Long) - Total data rows
- `validRows` (Long) - Valid data rows
- `missingValues` (Long) - Count of missing values
- `detectedOutliers` (Long) - Count of outliers
- `outlierColumns` (String) - Columns with outliers
- `additionalColumns` (String) - Extra columns found
- `qualityScore` (BigDecimal, precision=5, scale=2) - Overall quality score 0-100
- `completenessScore` (BigDecimal, precision=5, scale=2) - Data completeness %
- `consistencyScore` (BigDecimal, precision=5, scale=2) - Data consistency %
- `accuracyScore` (BigDecimal, precision=5, scale=2) - Data accuracy %
- `validationErrors` (@Column(columnDefinition = "JSON")) - Error details JSON
- `validationWarnings` (@Column(columnDefinition = "JSON")) - Warning details JSON

**Relationships**:
- `uploadedDataId` (Long, @OneToOne) - FK to UploadedData

### Integration Domain

#### 12. WorkflowStep (workflow_step)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Track workflow step completion and validation
**Table**: `workflow_step`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `stepNumber` (Integer, 1-8) - Step number in workflow
- `stepName` (String) - Step display name
- `status` (StepStatus enum) - PENDING, IN_PROGRESS, COMPLETED, SKIPPED, ERROR
- `isRequired` (Boolean) - Whether step is mandatory
- `isAccessible` (Boolean) - Whether step can be accessed
- `completedAt` (LocalDateTime) - Completion timestamp
- `validationErrors` (@Column(columnDefinition = "JSON")) - Validation errors JSON
- `stepData` (@Column(columnDefinition = "JSON")) - Step-specific data JSON
- `progressPercentage` (Integer, 0-100) - Step completion progress

**Relationships**:
- `forecastId` (Long, @ManyToOne) - FK to Forecast

**Business Rules**:
- Step 1 always accessible
- Subsequent steps require previous step completion
- Error status prevents progression

#### 13. TrainingProgress (training_progress)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Real-time model training progress tracking
**Table**: `training_progress`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `status` (TrainingStatus enum) - INITIALIZING, TRAINING, VALIDATING, COMPLETED, FAILED
- `progressPercentage` (Integer, 0-100) - Training completion %
- `currentIteration` (Integer) - Current training iteration
- `totalIterations` (Integer) - Total training iterations
- `elapsedTime` (Long) - Elapsed time in seconds
- `estimatedTimeRemaining` (Long) - ETA in seconds
- `currentMetrics` (@Column(columnDefinition = "JSON")) - Current metrics JSON
- `resourceUtilization` (@Column(columnDefinition = "JSON")) - Resource usage JSON
- `trainingLogs` (@Column(columnDefinition = "JSON")) - Training logs JSON
- `lastUpdated` (LocalDateTime) - Last progress update

**Relationships**:
- `forecastId` (Long, @OneToOne) - FK to Forecast

**Real-time Updates**: WebSocket integration for live progress

### System Domain

#### 14. SystemConfiguration (system_configuration)
**Base Class**: `BaseEntity`
**Purpose**: System-wide configuration settings
**Table**: `system_configuration`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `configKey` (String, @NotBlank, unique) - Configuration key
- `configValue` (String, @NotBlank) - Configuration value
- `configType` (ConfigType enum) - STRING, INTEGER, BOOLEAN, JSON
- `description` (String) - Configuration description
- `isEditable` (Boolean, default false) - Whether user can modify
- `category` (String) - Configuration category
- `defaultValue` (String) - Default value
- `validationRule` (String) - Validation regex or rule

**Examples**:
- `max_file_size_mb` = "100"
- `forecast_horizon_days_max` = "60"
- `supported_file_formats` = "csv,xlsx,xls"

#### 15. AuditLog (audit_log)
**Base Class**: `AbstractTenancyDomain`
**Purpose**: Comprehensive audit trail for forecasting operations
**Table**: `audit_log`

**Core Attributes**:
- `id` (Long) - Auto-generated primary key
- `entityType` (String) - Entity class name
- `entityId` (String) - Entity ID
- `operation` (AuditOperation enum) - CREATE, UPDATE, DELETE, VIEW, EXPORT
- `operationDetails` (@Column(columnDefinition = "JSON")) - Operation details JSON
- `oldValues` (@Column(columnDefinition = "JSON")) - Previous values JSON
- `newValues` (@Column(columnDefinition = "JSON")) - New values JSON
- `ipAddress` (String) - Client IP address
- `userAgent` (String) - Client user agent
- `sessionId` (String) - User session ID
- `operationResult` (OperationResult enum) - SUCCESS, FAILURE, WARNING
- `errorMessage` (String) - Error details if applicable

**Relationships**:
- `userId` (Long, @ManyToOne) - FK to User

**Compliance**: GDPR, SOX compliance support

---

## Technical Specifications

### Expected Data Volumes and Growth Patterns

**Activity & Forecast Entities**:
- Activities: ~1,000 per tenant per year
- Forecasts: ~5,000 per tenant per year (avg 5 forecasts per activity)
- Growth: 25% annually

**Data Processing Entities**:
- UploadedData: ~5,000 files per tenant per year (avg 50MB each)
- TransformedData: 1:1 ratio with uploads
- Growth: 30% annually with larger files

**Analysis & Results**:
- ExploratoryAnalytics: 1:1 with forecasts
- ForecastResults: 1:1 with completed forecasts
- JSON data size: avg 10KB per analysis, 50KB per result

### Performance Considerations

**Database Optimization**:
- Partitioning strategy: By tenant and date ranges
- Read replicas for analysis queries
- Connection pooling: HikariCP with 20-50 connections
- Query optimization: Native queries for complex analytics

**Caching Strategies**:
- Redis for session data and frequently accessed configurations
- Application-level caching for model definitions and system configurations
- CDN for static resources and file downloads

**JSON/JSONB Fields**:
- PostgreSQL JSONB for optimal performance
- GIN indexes on frequently queried JSON paths
- JSON schema validation at application level

**Computed/Derived Fields**:
- `forecastCount` in Activity (updated via triggers)
- `qualityScore` calculations cached
- Performance metrics calculated during model training

### Indexing Requirements

**Primary Indexes**:
- All entities: (id, tenantId)
- Time-based queries: (createdDate, tenantId)
- Status queries: (status, tenantId)

**Composite Indexes**:
- Activity: (clientId, projectId, tenantId), (status, priority, tenantId)
- Forecast: (activityId, status, tenantId), (version, tenantId)
- Audit: (entityType, entityId, tenantId), (userId, createdDate)

**Performance Indexes**:
- Full-text search on names/descriptions
- Spatial indexes if geographical data included
- Partial indexes on active records only

### Global Usage Considerations

**Multi-Currency Support**:
- All monetary fields use BigDecimal with currency code
- Exchange rate tables for financial forecasting
- Regional number formatting in presentation layer

**Multi-Timezone Support**:
- All timestamps stored in UTC
- User timezone preferences in User entity
- Business day calculations per region

**Localization Support**:
- Translatable fields in separate translation tables
- Locale-specific validation rules
- Regional data format requirements

**Regulatory Compliance**:
- GDPR: Data retention policies, consent tracking
- SOX: Audit trails, data integrity controls
- Industry-specific: Pharmaceutical, Financial services compliance

---

## Business Logic Mapping

### Screen Requirements Mapping

**Activity Dashboard**:
- Activity, Forecast (summary), ValidationResults (quality indicators)
- Statistics calculations from multiple entities
- Real-time updates via WebSocket

**8-Step Workflow**:
1. **Data Upload**: UploadedData, ValidationResults
2. **Data Analysis**: TransformedData, ExploratoryDataAnalytics
3. **Model Selection**: Models (system data), AI recommendations
4. **Feature Selection**: FeatureConfiguration
5. **Aggregation**: AggregationConfiguration
6. **Date Ranges**: DateRangeConfiguration
7. **Training**: TrainingProgress (real-time), Models execution
8. **Results**: ForecastResult, performance metrics

### State Transitions and Workflows

**Activity Lifecycle**:
DRAFT → ACTIVE → (COMPLETED | ON_HOLD | ARCHIVED)

**Forecast Workflow**:
DRAFT → IN_PROGRESS → (COMPLETED | FAILED | CANCELLED)

**Training States**:
INITIALIZING → TRAINING → VALIDATING → (COMPLETED | FAILED)

### Business Rules and Constraints

**Data Quality Rules**:
- Minimum 100 rows required for forecasting
- Maximum file size: 100MB
- Required columns: Date, SKUID, Depot, Demand, Region
- Data quality score >= 70% to proceed

**Forecasting Constraints**:
- Maximum 5 SKUs and 5 depots per forecast
- Forecast horizon: 30-60 days
- Minimum training data: 6 months
- Model accuracy tracking with thresholds

**Tenant Isolation**:
- All business data filtered by tenantId
- Cross-tenant data access prevented at entity level
- Audit trails per tenant

---

## Migration Considerations

### From Existing Systems

**Data Migration Strategy**:
- Staged migration approach (activities first, then forecasts)
- Data quality validation during migration
- Rollback procedures for each migration phase

**Legacy System Integration**:
- API adapters for existing forecast systems
- Data format transformation utilities
- Gradual transition with parallel running

### Performance Migration

**Database Migration**:
- Progressive schema updates
- Index creation during low-traffic periods
- Monitoring during migration phases

**Application Migration**:
- Feature flags for new entity usage
- A/B testing for performance comparison
- Gradual rollout per tenant

---

This comprehensive entity design provides the foundation for implementing a scalable, multi-tenant Dynamic Forecasting Service that supports global deployment while maintaining high performance and data integrity standards.