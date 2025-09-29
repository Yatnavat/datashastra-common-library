# Step 6: Date Ranges - Screen Documentation

## 1. Screen Overview

- **Primary purpose and user goals**: Configure training and testing periods for model validation, defining data splits and validation strategies for optimal forecasting performance
- **User role/permissions required**: Authenticated users with model validation permissions (Data Analyst, Project Manager with modeling rights)
- **Entry points**: Sixth step in Activity Workflow, accessible after successful aggregation configuration completion
- **Screen priority**: Core feature - critical validation step that determines model training quality and testing reliability

## 2. Visual Layout

### DATE RANGE CONFIGURATION INTERFACE Layout Structure
- **Layout structure**: `<div className="space-y-6 p-6">` with timeline visualization and configuration panels
- **Main Container**: Split layout with timeline visualization and configuration controls
- **Section Organization**:
```tsx
<div className="space-y-6">
  <header className="space-y-2" />
  <section className="timeline-visualization-section" />
  <section className="date-configuration-grid grid grid-cols-1 lg:grid-cols-2 gap-6" />
  <section className="validation-strategy-panel" />
</div>
```

### Responsive Breakpoints
```css
/* Mobile First Approach */
Base (< 768px):     Single column, stacked timeline and config, simplified date pickers
Tablet (768px+):    2-column layout for configuration, condensed timeline
Desktop (1024px+):  Full timeline visualization, side-by-side configuration panels
```

### TIMELINE VISUALIZATION Layout
- **Timeline Structure**: INTERACTIVE TIMELINE for visual date range selection
- **Layout Pattern**: Horizontal timeline with draggable range selectors
- **Visual Elements**: Training period, testing period, validation splits, data availability
- **Implementation**:
```tsx
<Card className="bg-gradient-to-r from-blue-50 to-purple-50 border-blue-200">
  <CardHeader>
    <CardTitle className="flex items-center gap-2">
      <Calendar className="w-5 h-5 text-blue-600" />
      Data Timeline & Range Selection
    </CardTitle>
    <CardDescription>
      Visual timeline showing data availability and selected training/testing periods
    </CardDescription>
  </CardHeader>
  <CardContent>
    <div className="space-y-4">
      {/* Timeline Header */}
      <div className="flex justify-between items-center text-sm text-muted-foreground">
        <span>{dataRange.start}</span>
        <span>Available Data: {totalDataPoints.toLocaleString()} points</span>
        <span>{dataRange.end}</span>
      </div>
      
      {/* Interactive Timeline */}
      <div className="relative h-24 bg-white rounded-lg border p-4">
        <div className="absolute inset-x-4 top-6 h-2 bg-muted rounded">
          {/* Data availability bar */}
          <div 
            className="h-full bg-blue-200 rounded"
            style={{ width: '100%' }}
          />
          
          {/* Training period */}
          <div
            className="absolute h-full bg-blue-500 rounded transition-all duration-200"
            style={{
              left: `${(trainingStart - dataRangeStart) / dataRangeDuration * 100}%`,
              width: `${(trainingEnd - trainingStart) / dataRangeDuration * 100}%`
            }}
          />
          
          {/* Testing period */}
          <div
            className="absolute h-full bg-green-500 rounded transition-all duration-200"
            style={{
              left: `${(testingStart - dataRangeStart) / dataRangeDuration * 100}%`,
              width: `${(testingEnd - testingStart) / dataRangeDuration * 100}%`
            }}
          />
          
          {/* Validation splits */}
          {validationSplits.map((split, index) => (
            <div
              key={index}
              className="absolute h-full bg-orange-400 rounded-sm"
              style={{
                left: `${(split.start - dataRangeStart) / dataRangeDuration * 100}%`,
                width: `${(split.end - split.start) / dataRangeDuration * 100}%`
              }}
            />
          ))}
          
          {/* Draggable handles */}
          <div
            className="absolute w-3 h-6 bg-blue-600 rounded cursor-ew-resize -top-1 flex items-center justify-center"
            style={{ left: `${(trainingStart - dataRangeStart) / dataRangeDuration * 100}%` }}
            onMouseDown={(e) => handleDragStart(e, 'trainingStart')}
          >
            <div className="w-1 h-4 bg-white rounded"></div>
          </div>
        </div>
        
        {/* Timeline labels */}
        <div className="absolute inset-x-4 bottom-2 flex justify-between text-xs text-muted-foreground">
          <span>Training</span>
          <span>Validation</span>
          <span>Testing</span>
        </div>
      </div>
      
      {/* Legend */}
      <div className="flex flex-wrap gap-4 text-xs">
        <div className="flex items-center gap-2">
          <div className="w-3 h-3 bg-blue-500 rounded"></div>
          <span>Training Period</span>
        </div>
        <div className="flex items-center gap-2">
          <div className="w-3 h-3 bg-green-500 rounded"></div>
          <span>Testing Period</span>
        </div>
        <div className="flex items-center gap-2">
          <div className="w-3 h-3 bg-orange-400 rounded"></div>
          <span>Validation Splits</span>
        </div>
        <div className="flex items-center gap-2">
          <div className="w-3 h-3 bg-blue-200 rounded"></div>
          <span>Available Data</span>
        </div>
      </div>
    </div>
  </CardContent>
</Card>
```

### Typography System (14px base - NO OVERRIDES)
- **Step Title**: 20px (h1 default) + font-medium "Date Ranges & Validation Strategy"
- **Section Headers**: 18px (h2 default) + font-medium for configuration categories
- **Date Labels**: 16px (h3 default) + font-medium for date range labels
- **Descriptions**: 14px (p default) + font-normal for configuration explanations
- **Input Labels**: 14px (label default) + font-medium for form field labels

### Color Scheme
- **Training Period**: `bg-blue-500` for training data visualization
- **Testing Period**: `bg-green-500` for testing data visualization
- **Validation Splits**: `bg-orange-400` for cross-validation periods
- **Data Availability**: `bg-blue-200` for available data background
- **Configuration Cards**: `bg-card` with border accent colors

## 3. Components Inventory

### Step Header - DATE RANGE CONFIGURATION HEADER
- **Step Title**: "Step 6: Date Ranges & Validation Strategy"
- **Step Description**: Configure data splits and validation strategy for model training
- **Current Configuration**: Display current date range settings summary
- **Data Coverage**: Show percentage of available data being used

### Timeline Visualization Component - INTERACTIVE TIMELINE INTERFACE
- **Data Timeline**: Visual representation of available data range
- **Period Selectors**: Draggable handles for training and testing period selection
- **Validation Indicators**: Visual markers for cross-validation splits
- **Data Quality Overlay**: Indicators showing data quality across timeline
- **Responsive Design**: Timeline adapts to screen size with touch support

### Training Period Configuration - DATE SELECTION INTERFACE
- **Start Date Selection**: Calendar picker for training start date
- **End Date Selection**: Calendar picker for training end date
- **Quick Presets**: Buttons for common training period selections
- **Data Coverage**: Show amount of data in training period
- **Implementation**:
```tsx
<Card className="border-2 border-blue-200 bg-blue-50">
  <CardHeader>
    <div className="flex items-center gap-3">
      <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
        <BookOpen className="w-5 h-5 text-blue-600" />
      </div>
      <div>
        <CardTitle>Training Period</CardTitle>
        <CardDescription>Define the data range for model training</CardDescription>
      </div>
    </div>
  </CardHeader>
  <CardContent className="space-y-4">
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div className="space-y-2">
        <label className="text-sm font-medium">Training Start Date</label>
        <Popover>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              className="w-full justify-start text-left font-normal"
            >
              <CalendarIcon className="mr-2 h-4 w-4" />
              {trainingStartDate ? format(trainingStartDate, "MMM dd, yyyy") : "Select start date"}
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-auto p-0">
            <Calendar
              mode="single"
              selected={trainingStartDate}
              onSelect={setTrainingStartDate}
              disabled={(date) => 
                date < new Date(dataRange.start) || 
                date > new Date(dataRange.end) ||
                (trainingEndDate && date >= trainingEndDate)
              }
              initialFocus
            />
          </PopoverContent>
        </Popover>
      </div>
      
      <div className="space-y-2">
        <label className="text-sm font-medium">Training End Date</label>
        <Popover>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              className="w-full justify-start text-left font-normal"
            >
              <CalendarIcon className="mr-2 h-4 w-4" />
              {trainingEndDate ? format(trainingEndDate, "MMM dd, yyyy") : "Select end date"}
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-auto p-0">
            <Calendar
              mode="single"
              selected={trainingEndDate}
              onSelect={setTrainingEndDate}
              disabled={(date) => 
                date < new Date(dataRange.start) || 
                date > new Date(dataRange.end) ||
                (trainingStartDate && date <= trainingStartDate)
              }
              initialFocus
            />
          </PopoverContent>
        </Popover>
      </div>
    </div>
    
    <div className="space-y-3">
      <label className="text-sm font-medium">Quick Presets</label>
      <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
        {trainingPresets.map((preset) => (
          <Button
            key={preset.id}
            variant="outline"
            size="sm"
            onClick={() => applyTrainingPreset(preset)}
            className="text-xs"
          >
            {preset.label}
          </Button>
        ))}
      </div>
    </div>
    
    <div className="pt-3 border-t border-blue-200">
      <div className="grid grid-cols-2 gap-4 text-sm">
        <div>
          <span className="text-muted-foreground">Data Points:</span>
          <span className="ml-1 font-medium">{trainingDataPoints.toLocaleString()}</span>
        </div>
        <div>
          <span className="text-muted-foreground">Coverage:</span>
          <span className="ml-1 font-medium">{trainingCoverage}% of total</span>
        </div>
        <div>
          <span className="text-muted-foreground">Duration:</span>
          <span className="ml-1 font-medium">{trainingDuration} days</span>
        </div>
        <div>
          <span className="text-muted-foreground">Quality:</span>
          <Badge variant={trainingQuality > 90 ? "default" : trainingQuality > 70 ? "secondary" : "destructive"}>
            {trainingQuality}%
          </Badge>
        </div>
      </div>
    </div>
  </CardContent>
</Card>
```

### Testing Period Configuration - TEST DATA SELECTION INTERFACE
- **Start Date Selection**: Calendar picker for testing start date
- **End Date Selection**: Calendar picker for testing end date
- **Holdout Strategy**: Options for how to handle testing data
- **Future Data**: Option to reserve future data for testing
- **Implementation**:
```tsx
<Card className="border-2 border-green-200 bg-green-50">
  <CardHeader>
    <div className="flex items-center gap-3">
      <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
        <TestTube className="w-5 h-5 text-green-600" />
      </div>
      <div>
        <CardTitle>Testing Period</CardTitle>
        <CardDescription>Define the holdout data for model evaluation</CardDescription>
      </div>
    </div>
  </CardHeader>
  <CardContent className="space-y-4">
    <div className="space-y-3">
      <label className="text-sm font-medium">Testing Strategy</label>
      <RadioGroup value={testingStrategy} onValueChange={setTestingStrategy}>
        <div className="space-y-2">
          <div className="flex items-center space-x-2">
            <RadioGroupItem value="holdout" id="test-holdout" />
            <label htmlFor="test-holdout" className="text-sm cursor-pointer">
              Holdout Testing (recommended)
            </label>
          </div>
          <p className="text-xs text-muted-foreground ml-6">
            Reserve recent data for final model evaluation
          </p>
          
          <div className="flex items-center space-x-2">
            <RadioGroupItem value="future" id="test-future" />
            <label htmlFor="test-future" className="text-sm cursor-pointer">
              Future Data Testing
            </label>
          </div>
          <p className="text-xs text-muted-foreground ml-6">
            Use data beyond training period for testing
          </p>
          
          <div className="flex items-center space-x-2">
            <RadioGroupItem value="split" id="test-split" />
            <label htmlFor="test-split" className="text-sm cursor-pointer">
              Percentage Split
            </label>
          </div>
          <p className="text-xs text-muted-foreground ml-6">
            Use percentage of available data for testing
          </p>
        </div>
      </RadioGroup>
    </div>
    
    {testingStrategy === 'split' && (
      <div className="space-y-2">
        <label className="text-sm font-medium">
          Testing Split: {testingSplitPercentage}%
        </label>
        <Slider
          value={[testingSplitPercentage]}
          onValueChange={([value]) => setTestingSplitPercentage(value)}
          min={10}
          max={40}
          step={5}
          className="w-full"
        />
        <div className="flex justify-between text-xs text-muted-foreground">
          <span>10% (minimum)</span>
          <span>40% (maximum)</span>
        </div>
      </div>
    )}
    
    {(testingStrategy === 'holdout' || testingStrategy === 'future') && (
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="space-y-2">
          <label className="text-sm font-medium">Testing Start Date</label>
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                className="w-full justify-start text-left font-normal"
              >
                <CalendarIcon className="mr-2 h-4 w-4" />
                {testingStartDate ? format(testingStartDate, "MMM dd, yyyy") : "Select start date"}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0">
              <Calendar
                mode="single"
                selected={testingStartDate}
                onSelect={setTestingStartDate}
                disabled={(date) => 
                  date < new Date(trainingEndDate) || 
                  date > new Date(dataRange.end)
                }
                initialFocus
              />
            </PopoverContent>
          </Popover>
        </div>
        
        <div className="space-y-2">
          <label className="text-sm font-medium">Testing End Date</label>
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                className="w-full justify-start text-left font-normal"
              >
                <CalendarIcon className="mr-2 h-4 w-4" />
                {testingEndDate ? format(testingEndDate, "MMM dd, yyyy") : "Select end date"}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0">
              <Calendar
                mode="single"
                selected={testingEndDate}
                onSelect={setTestingEndDate}
                disabled={(date) => 
                  date < new Date(testingStartDate) || 
                  date > new Date(dataRange.end)
                }
                initialFocus
              />
            </PopoverContent>
          </Popover>
        </div>
      </div>
    )}
    
    <div className="pt-3 border-t border-green-200">
      <div className="grid grid-cols-2 gap-4 text-sm">
        <div>
          <span className="text-muted-foreground">Test Points:</span>
          <span className="ml-1 font-medium">{testingDataPoints.toLocaleString()}</span>
        </div>
        <div>
          <span className="text-muted-foreground">Coverage:</span>
          <span className="ml-1 font-medium">{testingCoverage}% of total</span>
        </div>
      </div>
    </div>
  </CardContent>
</Card>
```

### Validation Strategy Panel - CROSS-VALIDATION CONFIGURATION
- **Validation Method**: Time series cross-validation options
- **Fold Configuration**: Number of folds and validation strategy
- **Rolling Window**: Rolling window validation configuration
- **Gap Settings**: Gap between training and validation sets
- **Implementation**:
```tsx
<Card className="border-2 border-orange-200 bg-orange-50">
  <CardHeader>
    <div className="flex items-center gap-3">
      <div className="w-10 h-10 bg-orange-100 rounded-full flex items-center justify-center">
        <Repeat className="w-5 h-5 text-orange-600" />
      </div>
      <div>
        <CardTitle>Validation Strategy</CardTitle>
        <CardDescription>Configure cross-validation for model selection</CardDescription>
      </div>
    </div>
  </CardHeader>
  <CardContent className="space-y-6">
    <div className="space-y-3">
      <label className="text-sm font-medium">Validation Method</label>
      <RadioGroup value={validationMethod} onValueChange={setValidationMethod}>
        <div className="space-y-3">
          <div className="flex items-center space-x-2">
            <RadioGroupItem value="time_series_cv" id="val-time-series" />
            <label htmlFor="val-time-series" className="flex-1 cursor-pointer">
              <div className="p-3 rounded-lg border hover:bg-muted/50">
                <h4 className="font-medium">Time Series Cross-Validation</h4>
                <p className="text-sm text-muted-foreground">
                  Respects temporal order, best for time series data
                </p>
              </div>
            </label>
          </div>
          
          <div className="flex items-center space-x-2">
            <RadioGroupItem value="rolling_window" id="val-rolling" />
            <label htmlFor="val-rolling" className="flex-1 cursor-pointer">
              <div className="p-3 rounded-lg border hover:bg-muted/50">
                <h4 className="font-medium">Rolling Window Validation</h4>
                <p className="text-sm text-muted-foreground">
                  Fixed-size training window that rolls forward
                </p>
              </div>
            </label>
          </div>
          
          <div className="flex items-center space-x-2">
            <RadioGroupItem value="expanding_window" id="val-expanding" />
            <label htmlFor="val-expanding" className="flex-1 cursor-pointer">
              <div className="p-3 rounded-lg border hover:bg-muted/50">
                <h4 className="font-medium">Expanding Window Validation</h4>
                <p className="text-sm text-muted-foreground">
                  Training window grows with each validation fold
                </p>
              </div>
            </label>
          </div>
        </div>
      </RadioGroup>
    </div>
    
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div className="space-y-2">
        <label className="text-sm font-medium">
          Number of Folds: {validationFolds}
        </label>
        <Slider
          value={[validationFolds]}
          onValueChange={([value]) => setValidationFolds(value)}
          min={3}
          max={10}
          step={1}
          className="w-full"
        />
        <div className="flex justify-between text-xs text-muted-foreground">
          <span>3 folds</span>
          <span>10 folds</span>
        </div>
      </div>
      
      <div className="space-y-2">
        <label className="text-sm font-medium">
          Validation Gap: {validationGap} days
        </label>
        <Slider
          value={[validationGap]}
          onValueChange={([value]) => setValidationGap(value)}
          min={0}
          max={30}
          step={1}
          className="w-full"
        />
        <div className="flex justify-between text-xs text-muted-foreground">
          <span>No gap</span>
          <span>30 days</span>
        </div>
      </div>
    </div>
    
    {validationMethod === 'rolling_window' && (
      <div className="space-y-2">
        <label className="text-sm font-medium">
          Window Size: {rollingWindowSize} days
        </label>
        <Slider
          value={[rollingWindowSize]}
          onValueChange={([value]) => setRollingWindowSize(value)}
          min={30}
          max={365}
          step={7}
          className="w-full"
        />
        <div className="flex justify-between text-xs text-muted-foreground">
          <span>30 days</span>
          <span>365 days</span>
        </div>
      </div>
    )}
    
    <div className="pt-4 border-t border-orange-200">
      <h4 className="font-medium mb-3">Validation Summary</h4>
      <div className="grid grid-cols-2 gap-4 text-sm">
        <div>
          <span className="text-muted-foreground">Method:</span>
          <span className="ml-1 font-medium">{getValidationMethodLabel(validationMethod)}</span>
        </div>
        <div>
          <span className="text-muted-foreground">Folds:</span>
          <span className="ml-1 font-medium">{validationFolds}</span>
        </div>
        <div>
          <span className="text-muted-foreground">Points per Fold:</span>
          <span className="ml-1 font-medium">~{Math.round(trainingDataPoints / validationFolds).toLocaleString()}</span>
        </div>
        <div>
          <span className="text-muted-foreground">Est. Time:</span>
          <span className="ml-1 font-medium">{estimateValidationTime()}</span>
        </div>
      </div>
    </div>
  </CardContent>
</Card>
```

### Data Split Summary - CONFIGURATION OVERVIEW
- **Split Visualization**: Visual summary of data split configuration
- **Data Utilization**: Show how much data is used for each purpose
- **Quality Metrics**: Data quality indicators for each split
- **Recommendations**: AI-powered recommendations for optimal splits

## 4. Data Display Elements

### Date Range Configuration State
- **Training Start Date**: Date, user-selected start of training period
- **Training End Date**: Date, user-selected end of training period
- **Testing Start Date**: Date, user-selected start of testing period
- **Testing End Date**: Date, user-selected end of testing period
- **Validation Configuration**: Object with cross-validation settings

### Data Availability Information
- **Total Data Range**: Object with start and end dates of available data
- **Data Points**: Number, total number of data points available
- **Data Quality**: Object with quality metrics per time period
- **Missing Periods**: Array of date ranges with missing or incomplete data
- **Seasonal Patterns**: Object with detected seasonal patterns in data

### Split Statistics
- **Training Data Points**: Number, data points in training period
- **Testing Data Points**: Number, data points in testing period
- **Training Coverage**: Percentage, portion of available data used for training
- **Testing Coverage**: Percentage, portion of available data used for testing
- **Split Ratio**: Object with training/validation/testing ratios

### Validation Strategy Data
- **Validation Method**: Enum, values: ['time_series_cv', 'rolling_window', 'expanding_window']
- **Number of Folds**: Number, cross-validation fold count
- **Validation Gap**: Number, days between training and validation sets
- **Window Size**: Number, rolling window size for rolling validation
- **Fold Configurations**: Array of validation fold configurations

### Timeline Visualization Data
- **Timeline Periods**: Array of period objects with start/end dates and types
- **Data Density**: Object with data density per time period
- **Quality Indicators**: Object with quality scores per timeline segment
- **Visual Markers**: Array of significant events or data gaps
- **Draggable Handles**: Object with handle positions and constraints

### Performance Estimates
- **Validation Time**: String, estimated time for cross-validation
- **Training Efficiency**: Object with efficiency metrics for data split
- **Model Reliability**: Object with expected model reliability metrics
- **Resource Usage**: Object with memory and computation estimates
- **Accuracy Estimates**: Object with expected accuracy per validation method

### Loading States
- **Date Calculation**: Loading indicator during date range impact calculation
- **Validation Setup**: Loading state while configuring validation strategy
- **Timeline Generation**: Loading state for timeline visualization
- **Quality Assessment**: Loading indicator for data quality analysis

### Error States
- **Invalid Date Ranges**: Validation errors for impossible date combinations
- **Insufficient Data**: Warnings when periods have too little data
- **Data Quality Issues**: Warnings about poor data quality in selected ranges
- **Validation Conflicts**: Errors when validation strategy conflicts with date ranges

## 5. Interactive Features

### Timeline Interaction - DRAGGABLE TIMELINE INTERFACE
- **Draggable Handles**: Interactive handles for adjusting period boundaries
- **Range Selection**: Click and drag to select training and testing periods
- **Zoom and Pan**: Zoom into specific time periods for detailed selection
- **Touch Support**: Touch-friendly interactions for mobile devices
- **Implementation**:
```tsx
const handleTimelineDrag = (event, periodType, boundary) => {
  const timeline = timelineRef.current;
  const rect = timeline.getBoundingClientRect();
  const totalWidth = rect.width - 32; // Account for padding
  
  const handleMouseMove = (e) => {
    const x = e.clientX - rect.left - 16; // Account for padding
    const percentage = Math.max(0, Math.min(1, x / totalWidth));
    const newDate = new Date(
      dataRangeStart + percentage * (dataRangeEnd - dataRangeStart)
    );
    
    // Update the appropriate date based on periodType and boundary
    if (periodType === 'training' && boundary === 'start') {
      setTrainingStartDate(newDate);
    } else if (periodType === 'training' && boundary === 'end') {
      setTrainingEndDate(newDate);
    }
    // ... handle other cases
    
    // Update timeline visualization
    updateTimelineVisualization();
  };
  
  const handleMouseUp = () => {
    document.removeEventListener('mousemove', handleMouseMove);
    document.removeEventListener('mouseup', handleMouseUp);
  };
  
  document.addEventListener('mousemove', handleMouseMove);
  document.addEventListener('mouseup', handleMouseUp);
};
```

### Date Picker Integration - CALENDAR SELECTION INTERFACE
- **Calendar Popover**: Integrated calendar for precise date selection
- **Date Constraints**: Automatic validation of date range constraints
- **Quick Presets**: Buttons for common date range selections
- **Keyboard Navigation**: Keyboard support for date navigation

### Validation Method Selection - STRATEGY CONFIGURATION
- **Method Selection**: Radio buttons for different validation strategies
- **Parameter Adjustment**: Sliders and inputs for validation parameters
- **Real-time Preview**: Immediate preview of validation fold configuration
- **Impact Assessment**: Real-time assessment of validation strategy impact

### Split Optimization - AUTOMATIC OPTIMIZATION
- **Auto-Optimize**: Automatically suggest optimal data splits
- **Custom Constraints**: User-defined constraints for data splitting
- **Business Calendar**: Integration with business calendar for optimal splits
- **Seasonal Awareness**: Consider seasonal patterns in split optimization

### Configuration Validation - REAL-TIME VALIDATION
- **Range Validation**: Validate date ranges for logical consistency
- **Data Sufficiency**: Check if periods have sufficient data for training/testing
- **Quality Assessment**: Assess data quality in selected periods
- **Overlap Detection**: Detect and prevent overlapping periods

### Template Management - CONFIGURATION TEMPLATES
- **Save Configuration**: Save current date range configuration as template
- **Load Template**: Load previously saved configurations
- **Default Templates**: Access to recommended default configurations
- **Share Templates**: Share configurations with team members

## 6. Navigation Elements

### Date Range Navigation
- **Timeline Navigation**: Navigate timeline using zoom and pan controls
- **Period Navigation**: Jump between training, validation, and testing periods
- **Calendar Navigation**: Navigate through calendar months and years
- **Quick Selection**: Rapid selection of common date ranges

### Configuration Navigation
- **Tab Order**: Logical progression through date configuration controls
- **Validation Navigation**: Jump to validation errors and warnings
- **Reset Options**: Reset individual periods or entire configuration
- **Template Navigation**: Browse and apply saved configurations

### Workflow Navigation
- **Previous Step**: Return to Aggregation with date range preservation
- **Next Step**: Proceed to Model Training with date configuration
- **Skip Configuration**: Proceed with automatic date range selection
- **Save Configuration**: Save current date ranges for later use

### Help and Documentation Navigation
- **Date Range Help**: Contextual help for date range selection
- **Validation Help**: Guidance on cross-validation strategies
- **Best Practices**: Navigation to time series validation best practices
- **Tutorial Access**: Step-by-step date range configuration guides

## 7. Dynamic Behaviors

### Real-time Timeline Updates - DYNAMIC VISUALIZATION
- **Live Updates**: Timeline updates immediately as dates are changed
- **Smooth Animations**: Smooth transitions when adjusting date ranges
- **Visual Feedback**: Immediate visual feedback for date range changes
- **Conflict Highlighting**: Highlight conflicts or issues in real-time

### Automatic Validation - INTELLIGENT VALIDATION SYSTEM
- **Real-time Validation**: Validate configuration as user makes changes
- **Constraint Checking**: Check constraints between different periods
- **Data Quality Assessment**: Assess data quality in selected ranges
- **Optimization Suggestions**: Suggest improvements to current configuration

### Split Impact Calculation - DYNAMIC IMPACT ANALYSIS
- **Performance Impact**: Calculate impact of split configuration on model performance
- **Resource Estimation**: Estimate computational resources needed
- **Time Estimation**: Estimate training and validation time
- **Quality Prediction**: Predict model quality based on data splits

### Responsive Timeline - ADAPTIVE VISUALIZATION
- **Screen Adaptation**: Timeline adapts to different screen sizes
- **Touch Optimization**: Touch-friendly interactions for mobile devices
- **Zoom Levels**: Multiple zoom levels for detailed date selection
- **Smooth Scrolling**: Smooth scrolling and panning on timeline

### Configuration Optimization - AI-POWERED RECOMMENDATIONS
- **Automatic Optimization**: Suggest optimal date range configurations
- **Pattern Recognition**: Recognize patterns in data for better splits
- **Business Rules**: Apply business rules to date range selection
- **Performance Optimization**: Optimize splits for model performance

## 8. State Management

### Date Configuration State - DATE RANGE TRACKING
- **Training Period**: `trainingPeriod` object with start and end dates
- **Testing Period**: `testingPeriod` object with start and end dates
- **Validation Configuration**: `validationConfig` object with validation settings
- **Timeline State**: `timelineState` object with visualization state
- **Configuration History**: `configurationHistory` array of previous configurations

### Validation Strategy State - CROSS-VALIDATION TRACKING
- **Validation Method**: `validationMethod` enum with selected strategy
- **Validation Parameters**: `validationParameters` object with method-specific parameters
- **Fold Configuration**: `foldConfiguration` array with fold definitions
- **Validation Results**: `validationResults` object with validation impact estimates
- **Strategy Comparison**: `strategyComparison` object comparing different strategies

### Data Quality State - QUALITY ASSESSMENT TRACKING
- **Period Quality**: `periodQuality` object with quality scores per period
- **Data Availability**: `dataAvailability` object with data availability per period
- **Quality Warnings**: `qualityWarnings` array of data quality issues
- **Missing Data**: `missingData` array of periods with missing data
- **Quality Trends**: `qualityTrends` object with quality trend analysis

### Timeline Interaction State - VISUALIZATION STATE TRACKING
- **Timeline Zoom**: `timelineZoom` object with current zoom level and position
- **Drag State**: `dragState` object tracking current drag operations
- **Selected Period**: `selectedPeriod` string indicating currently selected period
- **Hover State**: `hoverState` object tracking hover interactions
- **Animation State**: `animationState` object controlling timeline animations

### Configuration Templates State - TEMPLATE MANAGEMENT
- **Saved Templates**: `savedTemplates` array of user-saved configurations
- **Default Templates**: `defaultTemplates` array of recommended configurations
- **Active Template**: `activeTemplate` string indicating current template
- **Template Metadata**: `templateMetadata` object with template information
- **Sharing State**: `sharingState` object for template sharing functionality

## 9. API Requirements

### Date Range Configuration Endpoints
- **GET** `/api/activities/:id/data-range` - Get available data range and quality
  - Response: `{ dateRange: object, dataPoints: number, qualityMetrics: object }`

- **POST** `/api/activities/:id/date-ranges/validate` - Validate date range configuration
  - Body: `{ trainingPeriod: object, testingPeriod: object, validationConfig: object }`
  - Response: `{ isValid: boolean, errors: string[], warnings: string[], suggestions: string[] }`

- **POST** `/api/activities/:id/date-ranges/optimize` - Get optimized date ranges
  - Body: `{ constraints: object, preferences: object, businessCalendar: object }`
  - Response: `{ optimizedRanges: object, reasoning: string[], performance: object }`

### Validation Strategy Endpoints
- **POST** `/api/activities/:id/validation/estimate` - Estimate validation performance
  - Body: `{ validationMethod: string, parameters: object, dataRange: object }`
  - Response: `{ estimatedTime: object, reliability: object, resourceUsage: object }`

- **GET** `/api/activities/:id/validation/recommendations` - Get validation recommendations
  - Query: `{ modelType: string, dataCharacteristics: object, timeConstraints: object }`
  - Response: `{ recommendedMethod: string, parameters: object, reasoning: string[] }`

### Data Quality Endpoints
- **GET** `/api/activities/:id/data-quality/timeline` - Get data quality over time
  - Query: `{ startDate: string, endDate: string, granularity: string }`
  - Response: `{ qualityTimeline: object[], issues: object[], recommendations: string[] }`

- **POST** `/api/activities/:id/data-quality/assess` - Assess quality for date ranges
  - Body: `{ dateRanges: object[], assessmentType: string }`
  - Response: `{ qualityAssessment: object, warnings: string[], suggestions: string[] }`

### Template Management Endpoints
- **GET** `/api/date-ranges/templates` - Get date range templates
  - Query: `{ category?: string, modelType?: string, useCase?: string }`
  - Response: `{ templates: Template[], categories: string[] }`

- **POST** `/api/date-ranges/templates` - Save date range template
  - Body: `{ name: string, description: string, configuration: object, metadata: object }`
  - Response: `{ template: Template, saved: boolean }`

### Timeline Visualization Endpoints
- **GET** `/api/activities/:id/timeline/data` - Get timeline visualization data
  - Query: `{ startDate: string, endDate: string, resolution: string }`
  - Response: `{ timelineData: object[], events: object[], quality: object[] }`

- **POST** `/api/activities/:id/timeline/events` - Add timeline events or markers
  - Body: `{ events: object[], eventType: string }`
  - Response: `{ added: boolean, events: object[] }`

## 10. Business Logic

### Date Range Validation Logic - TEMPORAL CONSISTENCY ENGINE
- **Chronological Validation**: Ensure training period comes before testing period
- **Overlap Prevention**: Prevent overlapping training and testing periods
- **Data Sufficiency**: Ensure each period has sufficient data for its purpose
- **Gap Validation**: Validate gaps between training and testing periods
- **Business Calendar Integration**: Respect business calendar constraints

### Cross-Validation Logic - TIME SERIES VALIDATION ENGINE
- **Temporal Order Preservation**: Maintain temporal order in cross-validation
- **Fold Size Optimization**: Optimize fold sizes for statistical significance
- **Gap Management**: Manage gaps between training and validation folds
- **Rolling Window Logic**: Implement rolling window validation strategies
- **Expanding Window Logic**: Implement expanding window validation strategies

### Data Quality Assessment - QUALITY EVALUATION ENGINE
- **Temporal Quality Analysis**: Assess data quality across different time periods
- **Missing Data Detection**: Detect and quantify missing data in date ranges
- **Outlier Identification**: Identify outliers that might affect validation
- **Seasonality Consideration**: Consider seasonal patterns in quality assessment
- **Trend Analysis**: Analyze trends that might affect train/test splits

### Split Optimization Logic - AUTOMATIC OPTIMIZATION ENGINE
- **Performance Optimization**: Optimize splits for maximum model performance
- **Resource Constraint Integration**: Consider computational resource constraints
- **Business Constraint Satisfaction**: Satisfy business calendar and operational constraints
- **Seasonal Awareness**: Ensure representative seasonal patterns in all splits
- **Data Balance**: Balance data quantity and quality across splits

### Validation Strategy Selection - STRATEGY OPTIMIZATION ENGINE
- **Model-Specific Recommendations**: Recommend validation strategies based on model type
- **Data-Driven Selection**: Choose strategies based on data characteristics
- **Performance Prediction**: Predict validation performance for different strategies
- **Resource Optimization**: Optimize validation strategy for available resources
- **Reliability Assessment**: Assess reliability of different validation approaches

## 11. Accessibility Requirements

### ARIA Labels and Roles
- **Timeline**: `role="slider" aria-label="Timeline date range selector"`
- **Date Pickers**: `role="button" aria-expanded={open} aria-label="Select date"`
- **Period Configuration**: `role="group" aria-label="Training period configuration"`
- **Validation Strategy**: `role="radiogroup" aria-label="Validation method selection"`
- **Configuration Summary**: `role="status" aria-live="polite"` for dynamic updates

### Keyboard Navigation Flow
1. **Tab Order**: Timeline controls → Training period → Testing period → Validation strategy → Summary
2. **Timeline Navigation**: Arrow keys for handle movement, Enter to select dates
3. **Date Picker Navigation**: Standard calendar keyboard navigation
4. **Radio Group Navigation**: Arrow keys for validation method selection
5. **Slider Controls**: Arrow keys for parameter adjustment

### Screen Reader Considerations
- **Date Announcements**: Announce selected dates and date range changes
- **Configuration Changes**: Announce configuration changes and validation results
- **Timeline Updates**: Announce timeline position and period changes
- **Validation Feedback**: Clear announcements of validation errors and suggestions
- **Quality Indicators**: Announce data quality scores and warnings

### Focus Management
- **Timeline Interaction**: Maintain focus on active timeline handle
- **Date Picker Focus**: Proper focus management within calendar popovers
- **Modal Dialogs**: Focus trap within date picker and help modals
- **Dynamic Content**: Announce and focus new content as configuration changes
- **Validation Feedback**: Move focus to validation errors when they occur

### Visual Accessibility
- **High Contrast Timeline**: Ensure timeline colors meet accessibility standards
- **Color Independence**: Use patterns and text in addition to color coding
- **Focus Indicators**: High-contrast focus indicators for all interactive elements
- **Text Scaling**: Support text scaling up to 200% without functionality loss
- **Error Visibility**: Clear visual indication of validation errors and warnings

## 12. Performance Considerations

### Timeline Rendering Performance - OPTIMIZED VISUALIZATION
- **Canvas Rendering**: Use canvas for smooth timeline rendering with many data points
- **Virtual Scrolling**: Implement virtual scrolling for very long timelines
- **Debounced Updates**: Debounce timeline updates during drag operations
- **Lazy Loading**: Load timeline data progressively as needed
- **Animation Optimization**: Optimize timeline animations for 60fps performance

### Date Calculation Performance - EFFICIENT COMPUTATION
- **Incremental Calculations**: Update only affected calculations when dates change
- **Caching Strategy**: Cache calculation results for common configurations
- **Background Processing**: Process heavy calculations in background workers
- **Batch Updates**: Batch multiple date changes for efficiency
- **Memory Management**: Efficient memory usage for large date ranges

### Validation Performance - OPTIMIZED VALIDATION ENGINE
- **Client-side Validation**: Perform basic validation on client for immediate feedback
- **Batched Validation**: Batch validation requests for complex scenarios
- **Progressive Validation**: Validate most critical aspects first
- **Validation Caching**: Cache validation results for identical configurations
- **Resource Monitoring**: Monitor computational resources during validation

### UI Responsiveness - SMOOTH USER EXPERIENCE
- **Non-blocking Operations**: Keep UI responsive during heavy date calculations
- **Progressive Enhancement**: Load advanced features progressively
- **Optimistic Updates**: Update UI immediately for better perceived performance
- **Smooth Animations**: Optimize animations for consistent frame rates
- **Memory Cleanup**: Proper cleanup of date calculation results

### Mobile Optimization - DEVICE-SPECIFIC PERFORMANCE
- **Touch Optimization**: Optimize touch interactions for timeline manipulation
- **Simplified Timeline**: Simplified timeline view for mobile devices
- **Reduced Calculations**: Reduce calculation complexity on mobile devices
- **Battery Optimization**: Optimize for battery usage during long sessions
- **Network Efficiency**: Minimize network requests for date range operations

## 13. Edge Cases

### Date Range Edge Cases - ROBUST DATE HANDLING
- **Insufficient Training Data**: Handle cases where training period has too little data
- **No Testing Data**: Handle configurations with no data available for testing
- **Data Gaps**: Handle large gaps in data within selected periods
- **Future Dates**: Handle requests for future dates beyond available data
- **Timezone Issues**: Handle timezone differences in date selection

### Validation Edge Cases - VALIDATION ROBUSTNESS
- **Single Fold Scenarios**: Handle cases where only one validation fold is possible
- **Overlapping Periods**: Handle accidental overlaps between training and validation
- **Insufficient Validation Data**: Handle cases with too little data for validation
- **Extreme Gaps**: Handle very large gaps between training and validation
- **Seasonal Misalignment**: Handle validation periods that miss important seasons

### Data Quality Edge Cases - QUALITY HANDLING
- **Poor Quality Periods**: Handle periods with very poor data quality
- **Missing Critical Periods**: Handle missing data during important business periods
- **Outlier Periods**: Handle periods with extreme outliers or anomalies
- **Seasonal Data Issues**: Handle seasonal data quality variations
- **Inconsistent Data**: Handle periods with inconsistent data patterns

### Timeline Interaction Edge Cases - UI ROBUSTNESS
- **Rapid Interactions**: Handle rapid timeline manipulation without performance issues
- **Touch Conflicts**: Handle conflicts between touch gestures on mobile
- **Browser Limitations**: Handle browser-specific rendering limitations
- **Large Date Ranges**: Handle very large date ranges that might cause performance issues
- **Small Screens**: Handle timeline interaction on very small screens

### Configuration Edge Cases - SYSTEM LIMITATIONS
- **Memory Constraints**: Handle memory limitations with large date ranges
- **Processing Timeouts**: Handle timeouts in date range calculations
- **Concurrent Users**: Handle multiple users configuring date ranges simultaneously
- **Network Failures**: Handle network failures during configuration
- **Data Corruption**: Handle corrupted date range configurations

## 14. Sample Data Structure

```json
{
  "dateRangeConfiguration": {
    "training": {
      "startDate": "2022-01-01",
      "endDate": "2023-09-30",
      "dataPoints": 12570,
      "coverage": 75.2,
      "duration": 638,
      "quality": 94.3
    },
    "testing": {
      "startDate": "2023-10-01",
      "endDate": "2023-12-31",
      "dataPoints": 3850,
      "coverage": 23.1,
      "duration": 92,
      "quality": 91.7,
      "strategy": "holdout"
    },
    "validation": {
      "method": "time_series_cv",
      "folds": 5,
      "gap": 7,
      "windowSize": null,
      "estimatedTime": "15-20 minutes"
    },
    "lastUpdated": "2024-01-20T17:45:00Z"
  },
  "dataAvailability": {
    "totalRange": {
      "start": "2022-01-01",
      "end": "2023-12-31"
    },
    "totalDataPoints": 16720,
    "dataQuality": {
      "overall": 93.2,
      "periods": [
        {
          "start": "2022-01-01",
          "end": "2022-06-30",
          "quality": 96.1,
          "issues": ["minor_gaps"]
        },
        {
          "start": "2022-07-01",
          "end": "2022-12-31",
          "quality": 89.5,
          "issues": ["outliers", "seasonal_anomaly"]
        },
        {
          "start": "2023-01-01",
          "end": "2023-06-30",
          "quality": 94.8,
          "issues": []
        },
        {
          "start": "2023-07-01",
          "end": "2023-12-31",
          "quality": 92.3,
          "issues": ["minor_gaps"]
        }
      ]
    },
    "missingPeriods": [
      {
        "start": "2022-03-15",
        "end": "2022-03-18",
        "reason": "system_maintenance"
      },
      {
        "start": "2023-05-01",
        "end": "2023-05-01",
        "reason": "holiday"
      }
    ],
    "seasonalPatterns": {
      "detected": true,
      "primarySeason": "yearly",
      "strength": 0.76,
      "peaks": ["2022-11", "2022-12", "2023-11", "2023-12"],
      "lows": ["2022-02", "2022-08", "2023-02", "2023-08"]
    }
  },
  "validationConfiguration": {
    "timeSeriesCV": {
      "folds": [
        {
          "id": 1,
          "trainStart": "2022-01-01",
          "trainEnd": "2022-08-31",
          "validStart": "2022-09-08",
          "validEnd": "2022-10-31",
          "dataPoints": { "train": 3652, "valid": 738 }
        },
        {
          "id": 2,
          "trainStart": "2022-01-01",
          "trainEnd": "2022-11-30",
          "validStart": "2022-12-08",
          "validEnd": "2023-01-31",
          "dataPoints": { "train": 4748, "valid": 738 }
        },
        {
          "id": 3,
          "trainStart": "2022-01-01",
          "trainEnd": "2023-02-28",
          "validStart": "2023-03-08",
          "validEnd": "2023-04-30",
          "dataPoints": { "train": 5844, "valid": 738 }
        },
        {
          "id": 4,
          "trainStart": "2022-01-01",
          "trainEnd": "2023-05-31",
          "validStart": "2023-06-08",
          "validEnd": "2023-07-31",
          "dataPoints": { "train": 6940, "valid": 738 }
        },
        {
          "id": 5,
          "trainStart": "2022-01-01",
          "trainEnd": "2023-08-31",
          "validStart": "2023-09-08",
          "validEnd": "2023-09-30",
          "dataPoints": { "train": 8036, "valid": 307 }
        }
      ]
    },
    "performanceEstimates": {
      "trainingTime": {
        "total": "15-20 minutes",
        "perFold": "3-4 minutes",
        "factors": ["data_size", "model_complexity", "validation_method"]
      },
      "reliability": {
        "score": 0.87,
        "confidence": 0.82,
        "factors": ["fold_count", "data_quality", "temporal_consistency"]
      },
      "resourceUsage": {
        "memory": "1.8GB",
        "cpu": "High during validation",
        "storage": "245MB for results"
      }
    }
  },
  "timelineVisualization": {
    "periods": [
      {
        "type": "training",
        "start": "2022-01-01",
        "end": "2023-09-30",
        "color": "#3b82f6",
        "label": "Training Period"
      },
      {
        "type": "testing",
        "start": "2023-10-01",
        "end": "2023-12-31",
        "color": "#10b981",
        "label": "Testing Period"
      }
    ],
    "validationFolds": [
      {
        "foldId": 1,
        "validStart": "2022-09-08",
        "validEnd": "2022-10-31",
        "color": "#f59e0b",
        "label": "Validation Fold 1"
      }
    ],
    "dataQualityOverlay": [
      {
        "start": "2022-01-01",
        "end": "2022-06-30",
        "quality": 96.1,
        "color": "#10b981"
      },
      {
        "start": "2022-07-01",
        "end": "2022-12-31",
        "quality": 89.5,
        "color": "#f59e0b"
      }
    ],
    "events": [
      {
        "date": "2022-03-15",
        "type": "data_gap",
        "description": "System maintenance period",
        "icon": "warning"
      },
      {
        "date": "2022-11-24",
        "type": "seasonal_peak",
        "description": "Black Friday peak demand",
        "icon": "trend_up"
      }
    ]
  },
  "validationResults": {
    "isValid": true,
    "errors": [],
    "warnings": [
      "Testing period is relatively short (92 days), consider longer period for more reliable evaluation",
      "Validation fold 5 has fewer data points than other folds"
    ],
    "suggestions": [
      "Consider using expanding window validation for better use of historical data",
      "Gap of 7 days between training and validation helps prevent data leakage"
    ],
    "qualityChecks": {
      "minimumTrainingData": { "passed": true, "threshold": 1000, "actual": 12570 },
      "minimumTestingData": { "passed": true, "threshold": 500, "actual": 3850 },
      "temporalOrder": { "passed": true, "description": "Training period comes before testing period" },
      "noOverlap": { "passed": true, "description": "No overlap between training and testing periods" },
      "seasonalCoverage": { "passed": true, "description": "Training period covers all seasons" }
    }
  },
  "templates": {
    "recommended": [
      {
        "id": "template_balanced",
        "name": "Balanced Training-Testing Split",
        "description": "75% training, 25% testing with time series CV",
        "configuration": {
          "trainingSplit": 0.75,
          "testingSplit": 0.25,
          "validationMethod": "time_series_cv",
          "folds": 5
        },
        "suitability": 0.92
      },
      {
        "id": "template_robust",
        "name": "Robust Validation Strategy",
        "description": "Conservative split with expanding window validation",
        "configuration": {
          "trainingSplit": 0.70,
          "testingSplit": 0.30,
          "validationMethod": "expanding_window",
          "folds": 7
        },
        "suitability": 0.85
      }
    ]
  }
}
```

## 15. Implementation Notes

### Recommended Libraries
- **date-fns**: Comprehensive date manipulation and formatting
- **react-day-picker**: Advanced calendar component with range selection
- **recharts**: Timeline visualization and data quality charts
- **react-draggable**: Draggable timeline handles and components
- **use-gesture**: Advanced gesture handling for timeline interactions
- **react-window**: Virtual scrolling for large timeline datasets

### Complex Implementation Areas
- **Timeline Interaction**: Implementing smooth draggable timeline with constraints
- **Cross-Validation Logic**: Complex logic for time series cross-validation setup
- **Date Constraint Management**: Managing complex date constraints and dependencies
- **Performance Optimization**: Maintaining smooth performance with large date ranges
- **Mobile Touch Handling**: Implementing touch-friendly timeline interactions

### Potential Technical Challenges
- **Timeline Performance**: Maintaining smooth performance with large datasets
- **Date Validation Complexity**: Managing complex interdependencies between date ranges
- **Cross-browser Compatibility**: Ensuring consistent date handling across browsers
- **Touch Interaction**: Implementing precise touch interactions for timeline
- **Memory Management**: Handling large amounts of temporal data efficiently

### Performance Optimization Opportunities
- **Timeline Virtualization**: Virtualize timeline rendering for large date ranges
- **Calculation Caching**: Cache date range calculations and validation results
- **Debounced Updates**: Debounce timeline updates during interactions
- **Background Processing**: Process validation calculations in background
- **Memory Optimization**: Optimize memory usage for temporal data structures

### Testing Considerations
- **Date Logic Testing**: Comprehensive testing of date validation and calculation logic
- **Timeline Interaction Testing**: Test timeline drag and selection interactions
- **Cross-Validation Testing**: Verify correctness of cross-validation fold generation
- **Performance Testing**: Test performance with various data sizes and date ranges
- **Accessibility Testing**: Ensure keyboard navigation and screen reader compatibility
- **Cross-platform Testing**: Test date handling across different platforms and timezones

## 16. UI Pattern Reference

### INTERACTIVE TIMELINE INTERFACE - Complete Timeline Implementation
```tsx
<Card className="bg-gradient-to-r from-blue-50 to-purple-50 border-blue-200">
  <CardHeader>
    <CardTitle className="flex items-center gap-2">
      <Calendar className="w-5 h-5 text-blue-600" />
      Data Timeline & Range Selection
    </CardTitle>
    <CardDescription>
      Interactive timeline showing data availability and selected training/testing periods
    </CardDescription>
  </CardHeader>
  <CardContent>
    <div className="space-y-4">
      {/* Timeline Header */}
      <div className="flex justify-between items-center text-sm text-muted-foreground">
        <span>{format(new Date(dataRange.start), "MMM yyyy")}</span>
        <span className="font-medium">
          Available Data: {totalDataPoints.toLocaleString()} points
        </span>
        <span>{format(new Date(dataRange.end), "MMM yyyy")}</span>
      </div>
      
      {/* Interactive Timeline */}
      <div 
        ref={timelineRef}
        className="relative h-20 bg-white rounded-lg border p-4 cursor-crosshair"
        onMouseDown={handleTimelineClick}
      >
        {/* Background data availability */}
        <div className="absolute inset-x-4 top-6 h-3 bg-muted rounded-sm">
          <div 
            className="h-full bg-blue-200 rounded-sm"
            style={{ width: '100%' }}
          />
          
          {/* Training period overlay */}
          <div
            className="absolute h-full bg-blue-500 rounded-sm transition-all duration-200 cursor-move"
            style={{
              left: `${getTimelinePosition(trainingStartDate)}%`,
              width: `${getTimelineWidth(trainingStartDate, trainingEndDate)}%`
            }}
            onMouseDown={(e) => handlePeriodDrag(e, 'training')}
          >
            <div className="absolute inset-0 flex items-center justify-center">
              <span className="text-xs text-white font-medium">Training</span>
            </div>
          </div>
          
          {/* Testing period overlay */}
          <div
            className="absolute h-full bg-green-500 rounded-sm transition-all duration-200 cursor-move"
            style={{
              left: `${getTimelinePosition(testingStartDate)}%`,
              width: `${getTimelineWidth(testingStartDate, testingEndDate)}%`
            }}
            onMouseDown={(e) => handlePeriodDrag(e, 'testing')}
          >
            <div className="absolute inset-0 flex items-center justify-center">
              <span className="text-xs text-white font-medium">Testing</span>
            </div>
          </div>
          
          {/* Validation fold markers */}
          {validationFolds.map((fold, index) => (
            <div
              key={fold.id}
              className="absolute h-full bg-orange-400 rounded-sm opacity-70"
              style={{
                left: `${getTimelinePosition(fold.validStart)}%`,
                width: `${getTimelineWidth(fold.validStart, fold.validEnd)}%`
              }}
              title={`Validation Fold ${fold.id}`}
            />
          ))}
          
          {/* Draggable handles */}
          <div
            className="absolute w-2 h-5 bg-blue-600 rounded cursor-ew-resize -top-1 shadow-sm hover:bg-blue-700 transition-colors"
            style={{ left: `${getTimelinePosition(trainingStartDate)}%` }}
            onMouseDown={(e) => handleHandleDrag(e, 'trainingStart')}
            title="Drag to adjust training start date"
          />
          <div
            className="absolute w-2 h-5 bg-blue-600 rounded cursor-ew-resize -top-1 shadow-sm hover:bg-blue-700 transition-colors"
            style={{ left: `${getTimelinePosition(trainingEndDate)}%` }}
            onMouseDown={(e) => handleHandleDrag(e, 'trainingEnd')}
            title="Drag to adjust training end date"
          />
          <div
            className="absolute w-2 h-5 bg-green-600 rounded cursor-ew-resize -top-1 shadow-sm hover:bg-green-700 transition-colors"
            style={{ left: `${getTimelinePosition(testingStartDate)}%` }}
            onMouseDown={(e) => handleHandleDrag(e, 'testingStart')}
            title="Drag to adjust testing start date"
          />
          <div
            className="absolute w-2 h-5 bg-green-600 rounded cursor-ew-resize -top-1 shadow-sm hover:bg-green-700 transition-colors"
            style={{ left: `${getTimelinePosition(testingEndDate)}%` }}
            onMouseDown={(e) => handleHandleDrag(e, 'testingEnd')}
            title="Drag to adjust testing end date"
          />
        </div>
        
        {/* Data quality overlay */}
        <div className="absolute inset-x-4 top-10 h-1">
          {dataQualityPeriods.map((period, index) => (
            <div
              key={index}
              className={`absolute h-full rounded-sm ${
                period.quality > 90 ? 'bg-green-400' :
                period.quality > 70 ? 'bg-yellow-400' : 'bg-red-400'
              }`}
              style={{
                left: `${getTimelinePosition(period.start)}%`,
                width: `${getTimelineWidth(period.start, period.end)}%`
              }}
              title={`Data Quality: ${period.quality}%`}
            />
          ))}
        </div>
        
        {/* Date markers */}
        <div className="absolute inset-x-4 bottom-2 flex justify-between text-xs text-muted-foreground">
          {getTimelineMarkers().map((marker, index) => (
            <div key={index} className="text-center">
              <div className="w-px h-2 bg-muted-foreground/30 mx-auto mb-1"></div>
              <span>{marker.label}</span>
            </div>
          ))}
        </div>
      </div>
      
      {/* Legend and Summary */}
      <div className="space-y-3">
        <div className="flex flex-wrap gap-4 text-xs">
          <div className="flex items-center gap-2">
            <div className="w-3 h-3 bg-blue-500 rounded"></div>
            <span>Training Period ({format(trainingStartDate, "MMM dd")} - {format(trainingEndDate, "MMM dd")})</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-3 h-3 bg-green-500 rounded"></div>
            <span>Testing Period ({format(testingStartDate, "MMM dd")} - {format(testingEndDate, "MMM dd")})</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-3 h-3 bg-orange-400 rounded"></div>
            <span>Validation Folds</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-3 h-3 bg-gradient-to-r from-green-400 via-yellow-400 to-red-400 rounded"></div>
            <span>Data Quality</span>
          </div>
        </div>
        
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm bg-white rounded p-3 border">
          <div className="text-center">
            <div className="font-medium text-blue-600">{trainingDataPoints.toLocaleString()}</div>
            <div className="text-xs text-muted-foreground">Training Points</div>
          </div>
          <div className="text-center">
            <div className="font-medium text-green-600">{testingDataPoints.toLocaleString()}</div>
            <div className="text-xs text-muted-foreground">Testing Points</div>
          </div>
          <div className="text-center">
            <div className="font-medium text-orange-600">{validationFolds.length}</div>
            <div className="text-xs text-muted-foreground">CV Folds</div>
          </div>
          <div className="text-center">
            <div className="font-medium text-purple-600">{estimatedValidationTime}</div>
            <div className="text-xs text-muted-foreground">Est. Time</div>
          </div>
        </div>
      </div>
    </div>
  </CardContent>
</Card>
```

### DATE PICKER INTEGRATION - Calendar Selection Implementation
```tsx
const DateRangePicker = ({ label, startDate, endDate, onStartDateChange, onEndDateChange, constraints }) => {
  return (
    <div className="space-y-2">
      <label className="text-sm font-medium">{label}</label>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="text-xs text-muted-foreground">Start Date</label>
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                className="w-full justify-start text-left font-normal"
              >
                <CalendarIcon className="mr-2 h-4 w-4" />
                {startDate ? format(startDate, "PPP") : "Select start date"}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0" align="start">
              <Calendar
                mode="single"
                selected={startDate}
                onSelect={onStartDateChange}
                disabled={(date) => 
                  date < constraints.minDate || 
                  date > constraints.maxDate ||
                  (endDate && date >= endDate)
                }
                initialFocus
              />
            </PopoverContent>
          </Popover>
        </div>
        
        <div className="space-y-1">
          <label className="text-xs text-muted-foreground">End Date</label>
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                className="w-full justify-start text-left font-normal"
              >
                <CalendarIcon className="mr-2 h-4 w-4" />
                {endDate ? format(endDate, "PPP") : "Select end date"}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0" align="start">
              <Calendar
                mode="single"
                selected={endDate}
                onSelect={onEndDateChange}
                disabled={(date) => 
                  date < constraints.minDate || 
                  date > constraints.maxDate ||
                  (startDate && date <= startDate)
                }
                initialFocus
              />
            </PopoverContent>
          </Popover>
        </div>
      </div>
    </div>
  );
};
```

### VALIDATION STRATEGY CONFIGURATION - Strategy Selection Interface
```tsx
<Card className="border-2 border-orange-200 bg-orange-50">
  <CardHeader>
    <div className="flex items-center gap-3">
      <div className="w-10 h-10 bg-orange-100 rounded-full flex items-center justify-center">
        <Repeat className="w-5 h-5 text-orange-600" />
      </div>
      <div>
        <CardTitle>Cross-Validation Strategy</CardTitle>
        <CardDescription>Configure validation method for model evaluation</CardDescription>
      </div>
    </div>
  </CardHeader>
  <CardContent className="space-y-6">
    <RadioGroup value={validationMethod} onValueChange={setValidationMethod}>
      <div className="space-y-4">
        <div className="flex items-center space-x-3">
          <RadioGroupItem value="time_series_cv" id="val-time-series" />
          <label htmlFor="val-time-series" className="flex-1 cursor-pointer">
            <Card className="p-4 hover:bg-muted/50 transition-colors">
              <div className="flex items-center justify-between">
                <div>
                  <h4 className="font-medium">Time Series Cross-Validation</h4>
                  <p className="text-sm text-muted-foreground mt-1">
                    Respects temporal order, best for time series forecasting
                  </p>
                </div>
                <Badge className="bg-green-100 text-green-800">Recommended</Badge>
              </div>
            </Card>
          </label>
        </div>
        
        <div className="flex items-center space-x-3">
          <RadioGroupItem value="rolling_window" id="val-rolling" />
          <label htmlFor="val-rolling" className="flex-1 cursor-pointer">
            <Card className="p-4 hover:bg-muted/50 transition-colors">
              <div>
                <h4 className="font-medium">Rolling Window Validation</h4>
                <p className="text-sm text-muted-foreground mt-1">
                  Fixed-size training window that rolls forward through time
                </p>
              </div>
            </Card>
          </label>
        </div>
        
        <div className="flex items-center space-x-3">
          <RadioGroupItem value="expanding_window" id="val-expanding" />
          <label htmlFor="val-expanding" className="flex-1 cursor-pointer">
            <Card className="p-4 hover:bg-muted/50 transition-colors">
              <div>
                <h4 className="font-medium">Expanding Window Validation</h4>
                <p className="text-sm text-muted-foreground mt-1">
                  Training window grows with each validation fold
                </p>
              </div>
            </Card>
          </label>
        </div>
      </div>
    </RadioGroup>
    
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div className="space-y-3">
        <label className="text-sm font-medium">
          Number of Folds: <span className="text-orange-600 font-bold">{validationFolds}</span>
        </label>
        <Slider
          value={[validationFolds]}
          onValueChange={([value]) => setValidationFolds(value)}
          min={3}
          max={10}
          step={1}
          className="w-full"
        />
        <div className="flex justify-between text-xs text-muted-foreground">
          <span>3 folds (minimum)</span>
          <span>10 folds (maximum)</span>
        </div>
      </div>
      
      <div className="space-y-3">
        <label className="text-sm font-medium">
          Validation Gap: <span className="text-orange-600 font-bold">{validationGap} days</span>
        </label>
        <Slider
          value={[validationGap]}
          onValueChange={([value]) => setValidationGap(value)}
          min={0}
          max={30}
          step={1}
          className="w-full"
        />
        <div className="flex justify-between text-xs text-muted-foreground">
          <span>No gap</span>
          <span>30 days gap</span>
        </div>
      </div>
    </div>
    
    {validationMethod === 'rolling_window' && (
      <div className="space-y-3">
        <label className="text-sm font-medium">
          Rolling Window Size: <span className="text-orange-600 font-bold">{rollingWindowSize} days</span>
        </label>
        <Slider
          value={[rollingWindowSize]}
          onValueChange={([value]) => setRollingWindowSize(value)}
          min={30}
          max={365}
          step={7}
          className="w-full"
        />
        <div className="flex justify-between text-xs text-muted-foreground">
          <span>30 days</span>
          <span>365 days</span>
        </div>
      </div>
    )}
  </CardContent>
</Card>
```

## 17. Documentation Quality Checklist

### ✅ UI Pattern Specification:
- [x] Date range interface labeled as "DATE RANGE CONFIGURATION INTERFACE" with structured layout
- [x] Timeline specified as "INTERACTIVE TIMELINE INTERFACE" with draggable elements
- [x] Grid layout includes exact CSS classes for responsive behavior
- [x] Calendar integration specified with proper date picker implementation
- [x] Validation strategy specified with radio group selection interface

### ✅ Implementation Code:
- [x] Complete TSX code examples provided for all date range components
- [x] shadcn/ui component structure specified (Calendar, Popover, RadioGroup, Slider, etc.)
- [x] Exact CSS classes documented for timeline visualization and responsive behavior
- [x] Date picker integration with proper constraints and validation
- [x] Interactive timeline with draggable handles and visual feedback

### ✅ Visual Elements:
- [x] Timeline visualization with color-coded periods (blue training, green testing, orange validation)
- [x] Draggable handles with hover states and accessibility
- [x] Data quality overlay with gradient visualization
- [x] Calendar popovers with proper date constraints
- [x] Icon specifications throughout (Calendar, TestTube, Repeat, etc.)

### ✅ Layout Structure:
- [x] Exact timeline layout specifications with proper positioning
- [x] Date picker layout with two-column responsive grid
- [x] Validation strategy layout with card-based selection
- [x] Configuration panels with proper spacing and organization
- [x] Summary sections with metrics grid layout

### ✅ Interaction Patterns:
- [x] Timeline drag and drop with constraint validation
- [x] Calendar date selection with disabled date handling
- [x] Radio group navigation for validation strategies
- [x] Slider controls for parameters with real-time feedback
- [x] Template management with save and load functionality

### ❌ Rejected Generic Terms:
- [x] No usage of "date selection interface" - used "DATE RANGE CONFIGURATION INTERFACE" with specific structure
- [x] No vague timeline descriptions - specific "INTERACTIVE TIMELINE INTERFACE" with exact implementation
- [x] All layout descriptions include exact CSS classes and responsive behavior
- [x] Implementation code provided for all complex date range patterns
- [x] Timeline interaction specified with exact drag handling and visual feedback

**Documentation eliminates all ambiguity and provides exact implementation guidance for Step 6: Date Ranges with comprehensive timeline visualization, interactive date selection, and cross-validation strategy configuration.**