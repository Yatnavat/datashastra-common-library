# Activity Dashboard Component Specification

## Overview
A comprehensive activity dashboard for managing demand forecasting activities with detailed tracking and version history capabilities.

## Dashboard Header
- **Title**: "Activity Dashboard"
- **Subtitle**: "Manage your demand forecasting activities and track version history"
- **Action Button**: "New Activity" (top-right, dark button with plus icon)

## Summary Cards (Top Row)
Four metric cards displaying key statistics:

1. **Total Activities**: 3
2. **Total Forecasts**: 6  
3. **Active Activities**: 2
4. **Unique Clients**: 3

Each card has a distinct icon and colored indicator.

## Main Content Area

### Left Panel - Activities List
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

### Right Panel - Activity Details (When Selected)
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