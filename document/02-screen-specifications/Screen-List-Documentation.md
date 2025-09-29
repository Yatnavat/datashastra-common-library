# Screen List Documentation

## Overview
This document provides a comprehensive list of all screens in the Demand Forecasting Engine application, including their purposes, key components, and navigation relationships.

---

## Primary Screens

### 1. Activity Dashboard
**Purpose**: Main landing screen for managing demand forecasting activities and tracking progress across multiple client projects.

**Key Components**:
- Activity creation dialog
- Activity list with filtering and search
- Statistics cards (Total Activities, Total Forecasts, Active Activities, Unique Clients)
- Activity details panel with version history
- Quick action buttons for opening activities

**Data Displayed**:
- Activity name, client, project, status, priority
- Industry classification and description
- Creation and modification dates
- Forecast count and accuracy metrics
- Version history summary

**Navigation Entry Points**:
- Application startup (default screen)
- Back navigation from Activity Workflow
- Breadcrumb navigation from any workflow step

**Navigation Exit Points**:
- To Activity Workflow (when opening an activity)
- To Create Activity Dialog (when creating new activity)

---

### 2. Activity Workflow
**Purpose**: Guided 8-step workflow for creating, configuring, and executing demand forecasting models within a specific activity.

**Layout Structure**:
- Collapsible left sidebar with step navigation
- Main content area for current step
- Progress indicators and status tracking
- Version management and save functionality

**Key Components**:
- Step navigation sidebar with progress tracking
- Step completion indicators
- Auto-save functionality
- Version history access
- Back to dashboard navigation

**Navigation Entry Points**:
- From Activity Dashboard (when opening an activity)
- From Version History (when loading a specific forecast version)

**Navigation Exit Points**:
- Back to Activity Dashboard
- To Version History Dialog
- Step-by-step progression through workflow

---

## Workflow Steps (Sub-screens)

### Step 1: Data Upload
**Purpose**: Upload and validate historical demand data for forecasting analysis.

**Key Features**:
- File upload interface (CSV/Excel support)
- Data type selection (Raw vs Pre-processed)
- Schema validation and requirements display
- Data preview and statistics
- Template download functionality

**Validation Requirements**:
- Required columns verification
- Data quality checks
- File format validation
- Date range validation

---

### Step 2: Data Analysis (EDA)
**Purpose**: Exploratory data analysis with interactive visualizations and insights.

**Analysis Tabs**:
- **Overview**: Summary statistics and data quality metrics
- **Temporal**: Time series patterns and seasonal analysis
- **Distribution**: Demand distribution and statistical analysis
- **Categorical**: Category-wise analysis and breakdowns
- **Relationships**: Correlation analysis and feature relationships
- **Data Quality**: Missing values, outliers, and data issues

**Key Features**:
- Interactive charts and graphs
- Statistical summaries
- Data quality assessment
- Pattern recognition insights

---

### Step 3: Model Selection
**Purpose**: Choose appropriate forecasting model based on data characteristics and business requirements.

**Available Models**:
- **ARIMA**: Statistical time series forecasting
- **Prophet**: Robust seasonal forecasting
- **XGBoost**: Gradient boosting machine learning
- **LSTM**: Deep learning neural networks
- **LightGBM**: Fast gradient boosting

**Selection Criteria**:
- Model complexity and interpretability
- Training time and computational requirements
- Accuracy expectations
- Data size and pattern suitability
- AI-powered recommendations

---

### Step 4: Feature Selection
**Purpose**: Configure feature engineering and select relevant variables for model training.

**Feature Categories**:
- **Temporal Features**: Date-based variables (month, quarter, seasonality)
- **Lag Features**: Historical demand patterns
- **Rolling Statistics**: Moving averages and trends
- **External Features**: Promotional and environmental factors
- **Advanced Features**: Custom engineered variables

**Configuration Options**:
- Feature inclusion/exclusion toggles
- Parameter customization
- Automatic feature generation
- Feature importance preview

---

### Step 5: Aggregation
**Purpose**: Define data aggregation levels and forecasting scope.

**Aggregation Options**:
- **Geographical**: Depot, region, country level aggregation
- **Product**: SKU, category, brand level aggregation
- **Temporal**: Daily, weekly, monthly aggregation
- **Custom**: User-defined aggregation combinations

**Scope Configuration**:
- Forecast horizon (30-60 days)
- Confidence interval settings
- Aggregation granularity
- Output format preferences

---

### Step 6: Date Ranges
**Purpose**: Configure training and testing periods for model validation.

**Date Configuration**:
- Training period selection
- Testing period definition
- Validation split configuration
- Historical data utilization

**Validation Settings**:
- Cross-validation parameters
- Holdout period specification
- Seasonal adjustment options
- Data splitting strategies

---

### Step 7: Model Training
**Purpose**: Execute model training and monitor performance in real-time.

**Training Process**:
- Model execution with progress tracking
- Real-time performance metrics
- Training time estimation
- Resource utilization monitoring

**Performance Metrics**:
- Accuracy percentages
- Error metrics (MAE, RMSE, MAPE)
- Model-specific performance indicators
- Convergence and stability metrics

---

### Step 8: Results & Visualization
**Purpose**: Comprehensive analysis and visualization of forecasting results.

**Analysis Tabs**:
- **Overview**: Performance summary and key metrics
- **Forecast**: Interactive forecast charts with confidence intervals
- **Performance**: Detailed model evaluation metrics
- **Insights**: AI-generated insights and recommendations

**Visualization Features**:
- Interactive forecast charts
- Historical fit analysis
- Feature importance charts
- Performance comparison displays
- Export and sharing capabilities

---

## Dialog Screens

### Create Activity Dialog
**Purpose**: Form-based interface for creating new forecasting activities.

**Form Fields**:
- Client selection (dropdown)
- Project selection (dropdown)
- Activity name (text input)
- Description (text area)
- Industry classification (dropdown)
- Priority level (dropdown)

**Validation Rules**:
- Client and project selection required
- Activity name required and unique
- Description optional but recommended
- Industry and priority with default values

---

### Version History Dialog
**Purpose**: Comprehensive version management for forecasting models within an activity.

**Key Features**:
- Version comparison interface
- Version statistics and metrics
- Actions: View, Clone, Delete, Download
- Version selection for loading
- Performance comparison charts

**Version Information**:
- Version numbers and timestamps
- Model configuration details
- Accuracy and performance metrics
- Training duration and status
- Feature configuration summary

---

## Navigation Patterns

### Primary Navigation
- **Dashboard ↔ Workflow**: Main application flow
- **Linear Workflow**: Step-by-step progression through 8 stages
- **Skip Navigation**: Jump to completed steps within workflow

### Secondary Navigation
- **Modal Dialogs**: Overlay screens for specific actions
- **Sidebar Navigation**: Collapsible step navigation in workflow
- **Breadcrumb Navigation**: Context-aware navigation indicators

### State Management
- **Auto-save**: Automatic progress saving throughout workflow
- **Session Persistence**: Maintain state across navigation
- **Version Control**: Track and manage multiple forecast versions

---

## Screen Relationships

```
Activity Dashboard
├── Create Activity Dialog → Activity Dashboard
├── Activity Workflow
│   ├── Step 1: Data Upload
│   ├── Step 2: Data Analysis
│   ├── Step 3: Model Selection
│   ├── Step 4: Feature Selection
│   ├── Step 5: Aggregation
│   ├── Step 6: Date Ranges
│   ├── Step 7: Model Training
│   ├── Step 8: Results & Visualization
│   └── Version History Dialog → Activity Workflow
└── Back to Dashboard
```

---

## Responsive Design Considerations

### Desktop Layout
- Full sidebar navigation with detailed step information
- Wide charts and data tables
- Multi-column layouts for data display
- Comprehensive form layouts

### Mobile Layout
- Collapsible navigation with icons only
- Single-column layouts
- Touch-optimized form controls
- Simplified chart displays

### Tablet Layout
- Adaptive sidebar with selective information display
- Flexible grid layouts
- Touch and mouse input support
- Optimized chart sizing