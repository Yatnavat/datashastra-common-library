# Demand Forecast Engine MVP Component Specification

## Overview
### Application Navigation
**Left Sidebar Navigation** (Vertical Menu):
- **Electronics Q4 Analysis** (Active/Selected)
  - Acme Corporation • Q4 Demand Forecast
- **Data Upload** (Current Step - highlighted)
  - Upload and validate your data
- **Data Analysis** 
  - Explore data patterns and quality
- **Model Selection**
  - Choose forecasting models
- **Feature Selection**
  - Configure feature engineering
- **Aggregation**
  - Set aggregation level and scope
- **Date Ranges**
  - Configure training/testing periods
- **Model Training** (expandable section)

**Progress Tracking**:
- Progress: 0/8 steps
- **Save Progress** button
- **Version History** button

**Header Navigation**:
- **Application Title**: "Demand Forecast Engine MVP"
- **Current Activity**: "Electronics Q4 Analysis"
- **Action Button**: "New Forecast" (top-right)

## Activity Dashboard Module

### Dashboard Header
- **Title**: "Activity Dashboard"
- **Subtitle**: "Manage your demand forecasting activities and track version history"
- **Action Button**: "New Activity" (top-right, dark button with plus icon)

### Summary Cards (Top Row)
Four metric cards displaying key statistics:

1. **Total Activities**: 3
2. **Total Forecasts**: 6  
3. **Active Activities**: 2
4. **Unique Clients**: 3

Each card has a distinct icon and colored indicator.

### Activities List Panel
**Section Title**: "Activities"
**Description**: "Manage your demand forecasting activities"

#### Activity Items Structure:
Each activity displays as a card with:
- **Activity Name** (large text)
- **Priority Badge** (high/medium with colored background)
- **Status Badge** (active/completed with colored background)
- **Client Name** (with building icon)
- **Project Type** (with document icon)
- **Industry Category** (with category icon)
- **Forecast Count** (with chart icon)
- **Description** (brief summary text)
- **Open Button** (dark button, right-aligned)

#### Sample Activities:

1. **Electronics Q4 Analysis**
   - Priority: high (red badge)
   - Status: active (green badge)
   - Client: Acme Corporation
   - Project: Q4 Demand Forecast
   - Industry: Electronics
   - Forecasts: 3 forecasts
   - Description: "Quarterly demand forecasting for electronics division"

2. **Retail Chain Analysis**
   - Priority: high (red badge)
   - Status: active (green badge)
   - Client: RetailCo Inc.
   - Project: Holiday Season Planning
   - Industry: Retail
   - Forecasts: 2 forecasts
   - Description: "Holiday season demand forecasting for retail chain"

3. **Multi-Region Logistics**
   - Priority: medium (yellow badge)
   - Status: completed (blue badge)
   - Client: Global Logistics Ltd.
   - Project: Supply Chain Optimization
   - Industry: Logistics
   - Forecasts: 1 forecasts
   - Description: "Multi-region demand forecasting for supply chain optimization"

### Activity Details Panel (When Selected)
**Section Title**: "Activity Details"
**Selected Activity**: "Multi-Region Logistics"

#### Details Structure:
- **Client**: Global Logistics Ltd.
- **Project**: Supply Chain Optimization
- **Industry**: Logistics
- **Created**: 1/5/2024
- **Last Modified**: 1/12/2024

#### Version History Section:
- **Title**: "Version History"
- **Version Entry**: 
  - Version: v1.0
  - Date: 1/5/2024
  - Status: completed (with badge)
  - Accuracy: 92.1% accuracy
- **Action Button**: "New Forecast" (dark button with plus icon)

### Empty State (Right Panel)
When no activity is selected:
- **Icon**: Folder icon
- **Title**: "No Activity Selected"
- **Description**: "Select an activity to view details and version history"

## Data Upload & Transformation Module (Post-Upload State)

### Upload Completion Status
- **File Upload Success**: "Uploaded: BBD 25' MIS.xlsx - Srinivas.csv (Raw Data)" with green success checkmark
- **Processing Complete**: Data has been successfully uploaded and processed

### Upload Type Selection (Reference)
**Section Title**: "Data Upload & Transformation"
**Description**: "Choose your upload type and provide historical demand data for forecasting"

#### Upload Options Used:
1. **Raw Data Upload** (Selected - Recommended)
   - **Badge**: "Recommended" (green background)
   - **Description**: "Upload your original demand data. The system will automatically perform feature engineering and create derived columns like lag features, seasonal indicators, and temporal features."
   - **Features**: ✓ Automatic transformation ✓ Feature engineering ✓ Data validation

2. **Pre-processed Data Upload** (Advanced - Available)
   - **Badge**: "Advanced" (gray background)
   - **Description**: "Upload data that already includes derived features like lag variables, seasonal indicators, and temporal features. Requires specific column structure."
   - **Features**: ✓ Skip transformation ✓ Direct modeling ✓ Custom features

### Data Schema Requirements
**Section Title**: "Data Schema Requirements"
**Description**: "Required and optional columns for raw data"

#### Required Fields:
1. **Date** (Required)
   - Type: DATE (blue badge)
   - Description: "Sales date in ISO format"
   - Format: YYYY-MM-DD
   - Example: 2024-01-15
   - Validation: Green checkmark

2. **SKUID** (Required)
   - Type: STRING (green badge)
   - Description: "Unique SKU identifier"
   - Format: Text
   - Example: SKU001, ABC123
   - Validation: Green checkmark

3. **Depot** (Required)
   - Type: STRING (green badge)
   - Description: "Depot/warehouse location code"
   - Format: Text
   - Example: D001, WH_NORTH
   - Validation: Green checkmark

4. **Region** (Required)
   - Type: STRING (green badge)
   - Description: "Geographical region or territory code"
   - Format: Text
   - Example: North, South, East, West
   - Validation: Green checkmark

5. **Demand** (Required)
   - Type: INTEGER (purple badge)
   - Description: "Actual units demanded/sold"
   - Format: Positive numbers
   - Example: 150, 89, 234
   - Validation: Green checkmark

#### Optional Fields:
6. **Category** (Optional)
   - Type: STRING (green badge)
   - Description: "Product category classification"
   - Format: Text
   - Example: Electronics, Books
   - Validation: Green checkmark

7. **BackOrder** (Optional)
   - Type: INTEGER (purple badge)
   - Description: "Units in backorder/unfulfilled"
   - Format: Non-negative numbers
   - Example: 0, 5, 12
   - Validation: Green checkmark

8. **Price** (Optional)
   - Type: DECIMAL (orange badge)
   - Description: "Unit price on sale date"
   - Format: Positive decimal
   - Example: 25.99, 15.50
   - Validation: Green checkmark

9. **Discount** (Optional)
   - Type: DECIMAL (orange badge)
   - Description: "Discount percentage as decimal"
   - Format: 0.0 to 1.0
   - Example: 0.1 (10%), 0.05 (5%)
   - Validation: Green checkmark

### Data Overview & Statistics Module

#### Tab Navigation:
Three tabs for data exploration:
1. **Overview** (Selected state)
2. **Statistics** 
3. **Data Sample**

#### Overview Tab Content:
**Summary Metrics** (4-card layout):
- **Total Records**: 100
- **Unique SKUs**: 3
- **Depots**: 3  
- **Days Span**: 100

**Data Preview Sections**:
1. **SKUs Preview**
   - List: SKU001, SKU002, SKU003

2. **Depots**
   - List: D001, D002, D003

#### Statistics Tab Content:
**Demand Statistics** (Left Column):
- Mean Demand: 118.2
- Median Demand: 133.0
- Std Deviation: 46.0
- Min Demand: 39
- Max Demand: 193
- Coefficient of Variation: 38.9%

**Price Statistics** (Right Column):
- Mean Price: $21.29
- Min Price: $12.64
- Max Price: $26.98

**Date Range**:
- From: 2023-12-31
- To: 2024-04-08
- Duration: 100 days

#### Data Sample Tab Content:
**Sample Data Table** (First 5 rows):
- Shows 8 columns out of 17 total
- Row count: 3 total rows
- Column expansion indicator: "+9" for hidden columns

**Table Structure**:
| Date | SKUID | Depot | Category | Demand | Year | Month | Quarter |
|------|-------|-------|----------|--------|------|-------|---------|
| 2024-01-01 | SKU001 | D001 | Electronics | 150.00 | 2024.00 | 1.00 | Q1 |
| 2024-01-02 | SKU001 | D001 | Electronics | 142.00 | 2024.00 | 1.00 | Q1 |
| 2024-01-03 | SKU002 | D002 | Home | 89.00 | 2024.00 | 1.00 | Q1 |

### Data Transformation Status
**Section Title**: "Data Transformation"
**Description**: "Automatic feature engineering and data transformation process"

#### Transformation Steps:
1. **Raw Data Processing** ✓
   - Status: Completed (green checkmark)
   - Description: "Data validation and quality checks completed"

2. **Feature Engineering Complete** ✓
   - Status: Completed (green checkmark)
   - Description: "Generated temporal features, lag variables, and seasonal indicators"

### Validation Results
**Section Title**: "Validation Results"
**Description**: "Data quality and schema validation summary"

#### Validation Metrics (4-card layout):
- **Total Rows**: 1,000
- **Missing Values**: 5
- **Detected Outliers**: 12
- **Additional Columns**: 1

#### Validation Status:
- ✓ All required columns are present. Data range: 2024-01-01 to 2024-12-31
- ℹ️ Additional columns detected: CustomField1
- Note: "These columns will be included in the analysis but may not be used by all forecasting models."

## Design Specifications

### Color Scheme:
- **Priority Badges**: 
  - High: Red background
  - Medium: Yellow background
- **Status Badges**:
  - Active: Green background
  - Completed: Blue background
- **Buttons**: Dark/black background with white text
- **Background**: Light gray/white with card-based layout

### Layout:
- **Grid System**: 4-column top metrics, 2-column main content (60/40 split)
- **Card Design**: Rounded corners, subtle shadows
- **Typography**: Clear hierarchy with large titles, medium body text
- **Spacing**: Consistent padding and margins throughout

### Interactive Elements:
- **Hover States**: Cards should have subtle hover effects
- **Selection State**: Selected activity should be highlighted/outlined
- **Buttons**: Standard button hover and active states
- **Navigation**: Smooth transitions between activity selections

## Responsive Considerations:
- **Mobile**: Stack cards vertically, collapsible panels
- **Tablet**: Adjust column ratios, maintain card layout
- **Desktop**: Full layout as shown in screenshots