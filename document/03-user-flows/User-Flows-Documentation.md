# User Flows Documentation

## Overview
This document outlines the key user flows and navigation logic in the Demand Forecasting Engine application, describing how users move between screens and complete various tasks.

---

## Primary User Flows

### 1. Application Entry Flow
**Flow**: Application Launch → Activity Dashboard

**Description**: Users enter the application and land on the main dashboard where they can view existing activities and create new ones.

**Steps**:
1. Application loads
2. Display Activity Dashboard with:
   - Statistics summary (activities, forecasts, clients)
   - List of existing activities
   - Create new activity button
   - Activity details panel (if activity selected)

**Navigation Logic**:
- Default view shows all activities
- Auto-load dashboard statistics
- Initialize empty state if no activities exist

---

### 2. Create New Activity Flow
**Flow**: Activity Dashboard → Create Activity Dialog → Activity Dashboard

**Description**: Users create a new forecasting activity by selecting client and project, then providing activity details.

**Steps**:
1. User clicks "New Activity" button on dashboard
2. Create Activity Dialog opens with form fields:
   - Client dropdown (required)
   - Project dropdown (required)
   - Activity name text field (required)
   - Description text area (optional)
   - Industry dropdown (optional)
   - Priority dropdown (default: medium)
3. User fills required fields and submits
4. System validates form data
5. New activity created and added to dashboard
6. Dialog closes, returns to dashboard
7. Dashboard refreshes with new activity

**Validation Rules**:
- Client must be selected
- Project must be selected
- Activity name must be provided and unique
- Form submission disabled until required fields completed

**Error Handling**:
- Display validation errors for missing required fields
- Show error message if activity name already exists
- Prevent submission with incomplete data

---

### 3. Open Existing Activity Flow
**Flow**: Activity Dashboard → Activity Selection → Activity Workflow

**Description**: Users select an existing activity to view details or create new forecasts.

**Steps**:
1. User browses activity list on dashboard
2. User clicks on activity card to select it
3. Activity details panel updates with:
   - Client and project information
   - Activity metadata (dates, status)
   - Version history of forecasts
   - "Open" or "New Forecast" button
4. User clicks "Open" button
5. Navigate to Activity Workflow
6. Workflow loads with activity context

**Selection Logic**:
- Single activity selection only
- Selected activity highlighted visually
- Details panel updates immediately
- Action buttons become available

---

### 4. Complete Forecast Creation Flow
**Flow**: Activity Dashboard → Activity Workflow → 8-Step Process → Results → Dashboard

**Description**: End-to-end flow for creating a complete demand forecast within an activity.

**Detailed Steps**:

#### Step 1: Data Upload
1. User selects upload type (Raw or Pre-processed)
2. User uploads data file (CSV/Excel)
3. System validates file format and schema
4. Display validation results and data preview
5. User reviews requirements and proceeds
6. Auto-save progress and advance to next step

#### Step 2: Data Analysis (EDA)
1. System generates automatic data analysis
2. User explores 6 analysis tabs:
   - Overview: Summary statistics
   - Temporal: Time series patterns
   - Distribution: Data distribution analysis
   - Categorical: Category breakdowns
   - Relationships: Correlation analysis
   - Data Quality: Issues and recommendations
3. User reviews insights and data quality
4. User confirms analysis and proceeds

#### Step 3: Model Selection
1. System displays available forecasting models
2. AI recommendations highlighted based on data characteristics
3. User reviews model options:
   - Model descriptions and capabilities
   - Complexity and training time estimates
   - Accuracy expectations
   - Best use cases
4. User selects single model for training
5. System validates selection and proceeds

#### Step 4: Feature Selection
1. System displays available feature categories
2. User configures feature engineering:
   - Temporal features (dates, seasonality)
   - Lag features (historical patterns)
   - Rolling statistics (moving averages)
   - External features (promotions, events)
   - Advanced features (custom variables)
3. User toggles features on/off
4. System validates feature configuration
5. User confirms selection and proceeds

#### Step 5: Aggregation
1. User defines aggregation settings:
   - Geographical level (depot, region, country)
   - Product level (SKU, category, brand)
   - Time granularity (daily, weekly, monthly)
   - Forecast horizon (30-60 days)
2. User sets confidence interval preferences
3. System validates aggregation configuration
4. User confirms settings and proceeds

#### Step 6: Date Ranges
1. User configures training/testing periods:
   - Training data start and end dates
   - Testing period definition
   - Validation split percentage
   - Cross-validation settings
2. System validates date ranges
3. User reviews data utilization summary
4. User confirms configuration and proceeds

#### Step 7: Model Training
1. System initiates model training with user configuration
2. Real-time progress tracking displayed:
   - Training progress percentage
   - Estimated time remaining
   - Current performance metrics
   - Resource utilization
3. User monitors training progress
4. System completes training and validates results
5. Auto-advance to results when training complete

#### Step 8: Results & Visualization
1. System displays comprehensive results:
   - Model performance summary
   - Interactive forecast visualizations
   - Detailed performance metrics
   - AI-generated insights and recommendations
2. User explores 4 analysis tabs:
   - Overview: Key performance metrics
   - Forecast: Interactive charts with confidence intervals
   - Performance: Detailed evaluation metrics
   - Insights: Recommendations and feature importance
3. User reviews and validates results
4. User can export results or return to dashboard

**Workflow Navigation Logic**:
- Linear progression through steps
- Previous steps accessible once completed
- Auto-save progress at each step
- Step validation before advancement
- Skip navigation to completed steps
- Back to dashboard available at any time

---

### 5. Version Management Flow
**Flow**: Activity Workflow → Version History Dialog → Action Selection

**Description**: Users manage multiple forecast versions within an activity, including viewing, comparing, cloning, and deleting versions.

**Steps**:
1. User clicks "Version History" in Activity Workflow sidebar
2. Version History Dialog opens displaying:
   - Version statistics and summary
   - List of all forecast versions with details
   - Action buttons for each version
3. User can perform version actions:
   - **View**: Load specific version into workflow
   - **Clone**: Create new version based on existing one
   - **Delete**: Remove version permanently
   - **Download**: Export version results
   - **Compare**: Select multiple versions for comparison
4. User selects desired action
5. System executes action and updates interface
6. Dialog closes or updates based on action

**Version Actions Logic**:
- View: Loads version data into workflow and closes dialog
- Clone: Creates new version with copied configuration
- Delete: Removes version after confirmation
- Compare: Enables multi-select mode for version comparison
- Download: Exports version data in specified format

---

### 6. Workflow Step Navigation Flow
**Flow**: Within Activity Workflow → Step Selection → Step Content Display

**Description**: Users navigate between workflow steps using the sidebar navigation.

**Navigation Rules**:
- **Step 1 (Data Upload)**: Always accessible
- **Subsequent Steps**: Accessible only after previous step completion
- **Completed Steps**: Fully accessible for review and modification
- **Current Step**: Highlighted with special styling
- **Next Step**: Highlighted as "Next" when current step completed

**Step Access Logic**:
```
Step Access = Previous Step Completed OR Step Already Completed
Next Highlighting = Current Step Completed AND Not Last Step
Completion Status = Step Data Saved AND Validation Passed
```

**Visual Indicators**:
- Completed steps: Green checkmark icon
- Current step: Primary color highlighting
- Next step: "Next" badge display
- Locked steps: Grayed out with disabled state
- Progress bar: Shows overall completion percentage

---

### 7. Auto-Save and State Management Flow
**Flow**: Continuous Background Process Throughout Application

**Description**: Automatic saving of user progress and state management across navigation.

**Auto-Save Triggers**:
- Data entry completion in any step
- Step advancement
- Configuration changes
- File uploads and validations
- Model training completion

**State Persistence**:
- Workflow progress maintained across browser sessions
- Activity data synchronized with server
- Version history updated in real-time
- User preferences retained

**Recovery Logic**:
- Resume interrupted workflows
- Restore unsaved changes
- Handle connection failures gracefully
- Provide manual save options as backup

---

## Secondary User Flows

### 8. Activity Search and Filter Flow
**Flow**: Activity Dashboard → Search/Filter Application → Results Display

**Search Functionality**:
1. User enters search criteria in dashboard search bar
2. System filters activities in real-time
3. Results update dynamically as user types
4. Clear search option available

**Filter Options**:
- Client name
- Project name
- Activity status (Active, Completed, On-hold)
- Priority level (High, Medium, Low)
- Industry classification
- Date range (creation/modification)

---

### 9. Activity Details and Quick Actions Flow
**Flow**: Activity Dashboard → Activity Selection → Quick Actions

**Quick Actions Available**:
- **Open Activity**: Navigate to workflow
- **New Forecast**: Start new forecast in activity
- **View Details**: Expand activity information
- **Edit Activity**: Modify activity properties
- **Archive Activity**: Move to archived state

---

### 10. Export and Sharing Flow
**Flow**: Results Screen → Export Options → File Generation

**Export Options**:
- **Forecast Data**: CSV/Excel export of predictions
- **Model Report**: PDF summary of model performance
- **Visualizations**: PNG/SVG chart exports
- **Configuration**: JSON export of model settings

**Sharing Features**:
- Direct link sharing
- Email report generation
- Dashboard embedding options
- API access for integrations

---

## Navigation Error Handling

### Connection Failures
- Display offline notification
- Queue actions for retry
- Maintain local state
- Sync when connection restored

### Data Validation Errors
- Highlight problematic fields
- Show specific error messages
- Prevent invalid navigation
- Provide correction guidance

### Permission Errors
- Display access denied messages
- Redirect to appropriate screens
- Maintain user context
- Provide alternative actions

---

## User Experience Patterns

### Progressive Disclosure
- Show basic options first
- Expand advanced features on demand
- Provide contextual help
- Guide users through complex processes

### Responsive Navigation
- Adapt navigation to screen size
- Maintain functionality across devices
- Optimize touch interactions
- Preserve user context

### Contextual Actions
- Show relevant actions based on current state
- Hide unavailable options
- Provide action feedback
- Enable quick task completion

---

## Flow Decision Points

### Activity Creation
```
User Intent → New Activity Required?
├── Yes → Create Activity Flow
└── No → Browse Existing Activities Flow
```

### Workflow Entry
```
Activity Selected → Existing Forecasts Available?
├── Yes → Choose: New Forecast OR Load Existing
└── No → Start New Forecast Flow
```

### Step Navigation
```
Step Completion → Validation Passed?
├── Yes → Enable Next Step Navigation
└── No → Show Validation Errors, Remain on Current Step
```

### Result Actions
```
Training Complete → Results Satisfactory?
├── Yes → Save and Complete OR Export Results
└── No → Return to Previous Steps for Adjustment
```

---

## Performance Considerations

### Loading States
- Show progress indicators during data processing
- Provide estimated completion times
- Enable background processing where possible
- Maintain responsive interface during long operations

### Data Management
- Lazy load large datasets
- Cache frequently accessed data
- Optimize chart rendering
- Minimize server round trips

### User Feedback
- Immediate response to user actions
- Clear progress indication
- Error state communication
- Success confirmation messages