# Data Upload Component Specification (Pre-Upload)

## Overview
A data upload interface for demand forecasting with upload type selection, file upload area, and data schema requirements documentation.

## Page Header
- **Title**: "Data Upload"
- **Subtitle**: "Upload and validate your data"
- **Action Button**: "New Forecast" (top-right)

## Data Upload & Transformation Section

### Section Header
- **Title**: "Data Upload & Transformation"
- **Description**: "Choose your upload type and provide historical demand data for forecasting"

### Upload Type Selection
**Sub-section Title**: "Upload Type Selection"
**Description**: "Choose whether you want to upload raw data for transformation or pre-processed data ready for modeling"

#### Upload Option 1 - Raw Data Upload (Default Selection)
- **Radio Button**: Selected (filled black circle)
- **Icon**: Database/table icon
- **Title**: "Raw Data Upload"
- **Badge**: "Recommended" (gray background)
- **Description**: "Upload your original demand data. The system will automatically perform feature engineering and create derived columns like lag features, seasonal indicators, and temporal features."
- **Features List**: 
  - ✓ Automatic transformation
  - ✓ Feature engineering
  - ✓ Data validation

#### Upload Option 2 - Pre-processed Data Upload
- **Radio Button**: Unselected (empty circle)
- **Icon**: Lightning bolt icon
- **Title**: "Pre-processed Data Upload"
- **Badge**: "Advanced" (gray background)
- **Description**: "Upload data that already includes derived features like lag variables, seasonal indicators, and temporal features. Requires specific column structure."
- **Features List**:
  - ✓ Skip transformation
  - ✓ Direct modeling
  - ✓ Custom features

## File Upload Area

### Upload Section (Centered)
- **Icon**: Upload arrow icon (large, gray)
- **Title**: "Upload Raw Data File"
- **Description**: "Upload a CSV or Excel file containing your historical demand data"
- **File Size Limit**: "Maximum file size: 100MB"
- **Primary Button**: "Choose File" (black background, white text)
- **Secondary Button**: "Download Template" (white background, download icon)

## Data Schema Requirements Section

### Section Header
- **Title**: "Data Schema Requirements"
- **Description**: "Required and optional columns for raw data"

### Required Fields

#### 1. Date Field
- **Field Name**: "Date"
- **Requirement Badge**: "Required" (black background, white text)
- **Type Badge**: "DATE" (blue background, white text)
- **Description**: "Sales date in ISO format"
- **Format**: "YYYY-MM-DD"
- **Example**: "2024-01-15"

#### 2. SKUID Field
- **Field Name**: "SKUID"
- **Requirement Badge**: "Required" (black background, white text)
- **Type Badge**: "STRING" (green background, white text)
- **Description**: "Unique SKU identifier"
- **Format**: "Text"
- **Example**: "SKU001, ABC123"

#### 3. Depot Field
- **Field Name**: "Depot"
- **Requirement Badge**: "Required" (black background, white text)
- **Type Badge**: "STRING" (green background, white text)
- **Description**: "Depot/warehouse location code"
- **Format**: "Text"
- **Example**: "D001, WH_NORTH"

#### 4. Region Field
- **Field Name**: "Region"
- **Requirement Badge**: "Required" (black background, white text)
- **Type Badge**: "STRING" (green background, white text)
- **Description**: "Geographical region or territory code"
- **Format**: "Text"
- **Example**: "North, South, East, West"

#### 5. Demand Field
- **Field Name**: "Demand"
- **Requirement Badge**: "Required" (black background, white text)
- **Type Badge**: "INTEGER" (purple background, white text)
- **Description**: "Actual units demanded/sold"
- **Format**: "Positive numbers"
- **Example**: "150, 89, 234"

### Optional Fields

#### 6. Category Field
- **Field Name**: "Category"
- **Requirement Badge**: "Optional" (gray background, white text)
- **Type Badge**: "STRING" (green background, white text)
- **Description**: "Product category classification"
- **Format**: "Text"
- **Example**: "Electronics, Books"

#### 7. BackOrder Field
- **Field Name**: "BackOrder"
- **Requirement Badge**: "Optional" (gray background, white text)
- **Type Badge**: "INTEGER" (purple background, white text)
- **Description**: "Units in backorder/unfulfilled"
- **Format**: "Non-negative numbers"
- **Example**: "0, 5, 12"

#### 8. Price Field
- **Field Name**: "Price"
- **Requirement Badge**: "Optional" (gray background, white text)
- **Type Badge**: "DECIMAL" (orange background, white text)
- **Description**: "Unit price on sale date"
- **Format**: "Positive decimal"
- **Example**: "25.99, 15.50"

#### 9. Discount Field
- **Field Name**: "Discount"
- **Requirement Badge**: "Optional" (gray background, white text)
- **Type Badge**: "DECIMAL" (orange background, white text)
- **Description**: "Discount percentage as decimal"
- **Format**: "0.0 to 1.0"
- **Example**: "0.1 (10%), 0.05 (5%)"

## Navigation Footer
- **Button**: "Continue to EDA" (bottom-right, gray background)

## Design Specifications

### Layout Structure:
- **Container**: Full-width layout with centered content
- **Sections**: Clearly separated with spacing
- **Cards**: Upload options in bordered containers
- **Schema**: List format with consistent field structure

### Color Scheme:
- **Required badges**: Black background
- **Optional badges**: Gray background
- **Type badges**: Color-coded by data type (blue for DATE, green for STRING, purple for INTEGER, orange for DECIMAL)
- **Primary buttons**: Black background
- **Secondary buttons**: White background with borders

### Typography:
- **Section titles**: Large, bold text
- **Field names**: Medium, bold text
- **Descriptions**: Regular weight, gray color
- **Examples**: Monospace font for code/data examples

### Interactive Elements:
- **Radio buttons**: Standard form controls
- **Upload area**: Drag-and-drop visual styling
- **Buttons**: Standard hover and focus states
- **Field validation**: Visual indicators for required/optional status