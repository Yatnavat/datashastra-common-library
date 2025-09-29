# Step 3: Model Selection - Screen Documentation

## 1. Screen Overview

- **Primary purpose and user goals**: Choose appropriate forecasting model based on data characteristics, business requirements, and performance expectations
- **User role/permissions required**: Authenticated users with model selection permissions (Data Analyst, Project Manager with modeling rights)
- **Entry points**: Third step in Activity Workflow, accessible after successful data analysis completion
- **Screen priority**: Core feature - critical modeling step that determines forecasting approach and accuracy

## 2. Visual Layout

### MODEL SELECTION INTERFACE Layout Structure
- **Layout structure**: `<div className="space-y-6 p-6">` with model comparison and selection interface
- **Main Container**: Two-column layout for model options and detailed comparison
- **Section Organization**:
```tsx
<div className="space-y-6">
  <header className="space-y-2" />
  <section className="model-recommendations-section" />
  <section className="model-comparison-grid grid grid-cols-1 lg:grid-cols-2 gap-6" />
  <section className="selection-confirmation-area" />
</div>
```

### Responsive Breakpoints
```css
/* Mobile First Approach */
Base (< 768px):     Single column, stacked model cards, simplified comparison
Tablet (768px+):    2-column layout for model grid, condensed details panel
Desktop (1024px+):  Multi-column model grid, full comparison features
```

### MODEL COMPARISON GRID Layout
- **Grid Structure**: RESPONSIVE GRID LAYOUT for model option cards
- **Layout Pattern**: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6`
- **Card Layout**: Each model in dedicated card with selection state
- **Implementation**:
```tsx
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
  {availableModels.map((model) => (
    <Card 
      key={model.id}
      className={`cursor-pointer transition-all duration-200 hover:shadow-lg ${
        selectedModel?.id === model.id 
          ? 'ring-2 ring-primary border-primary bg-primary/5' 
          : 'hover:border-primary/50'
      }`}
      onClick={() => handleModelSelection(model)}
    >
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="flex items-center gap-2">
            <model.icon className="w-5 h-5" />
            {model.name}
          </CardTitle>
          {model.recommended && (
            <Badge className="bg-green-100 text-green-800">Recommended</Badge>
          )}
        </div>
        <CardDescription>{model.description}</CardDescription>
      </CardHeader>
      <CardContent>
        {/* Model details */}
      </CardContent>
    </Card>
  ))}
</div>
```

### Typography System (14px base - NO OVERRIDES)
- **Step Title**: 20px (h1 default) + font-medium "Model Selection & Configuration"
- **Model Names**: 18px (h2 default) + font-medium for model titles
- **Section Headers**: 16px (h3 default) + font-medium for comparison categories
- **Model Descriptions**: 14px (p default) + font-normal for model explanations
- **Metric Labels**: 14px (label default) + font-medium for performance indicators

### Color Scheme
- **Selected Model**: `ring-2 ring-primary border-primary bg-primary/5` for selection state
- **Recommended Models**: `bg-green-100 text-green-800` for recommendation badges
- **Model Cards**: `bg-card` with `hover:shadow-lg` for interactive states
- **Performance Indicators**: Color-coded based on performance levels (green/yellow/red)
- **Comparison Tables**: Alternating row colors for easy reading

## 3. Components Inventory

### Step Header - MODEL SELECTION HEADER
- **Step Title**: "Step 3: Model Selection & Configuration"
- **Step Description**: Choose the optimal forecasting model for your data characteristics
- **AI Recommendation**: Highlighted recommended model based on data analysis
- **Model Count**: Display number of available models and selection status

### AI Recommendation Panel - RECOMMENDATION DASHBOARD
- **Recommended Model**: Primary recommendation with confidence score
- **Recommendation Reasoning**: Explanation of why this model is recommended
- **Alternative Options**: Secondary recommendations with reasoning
- **Data Compatibility Score**: How well each model fits the data characteristics
- **Implementation**:
```tsx
<Card className="bg-gradient-to-r from-blue-50 to-purple-50 border-blue-200">
  <CardHeader>
    <CardTitle className="flex items-center gap-2">
      <Brain className="w-5 h-5 text-blue-600" />
      AI Recommendation
    </CardTitle>
    <CardDescription>
      Based on your data analysis, we recommend the following model
    </CardDescription>
  </CardHeader>
  <CardContent>
    <div className="flex items-center gap-4 p-4 bg-white rounded-lg border">
      <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
        <recommendedModel.icon className="w-6 h-6 text-blue-600" />
      </div>
      <div className="flex-1">
        <h3 className="font-medium">{recommendedModel.name}</h3>
        <p className="text-sm text-muted-foreground">{recommendedModel.shortDescription}</p>
        <div className="flex items-center gap-2 mt-2">
          <Badge className="bg-green-100 text-green-800">
            {Math.round(recommendedModel.confidenceScore * 100)}% Match
          </Badge>
          <Badge variant="outline">
            {recommendedModel.complexity} Complexity
          </Badge>
        </div>
      </div>
      <Button 
        onClick={() => handleQuickSelect(recommendedModel)}
        className="shrink-0"
      >
        Select Model
      </Button>
    </div>
    
    <div className="mt-4 p-3 bg-blue-50 rounded-lg">
      <h4 className="font-medium text-sm mb-2">Why this model?</h4>
      <ul className="text-sm space-y-1">
        {recommendedModel.reasons.map((reason, index) => (
          <li key={index} className="flex items-center gap-2">
            <CheckCircle className="w-3 h-3 text-green-600" />
            {reason}
          </li>
        ))}
      </ul>
    </div>
  </CardContent>
</Card>
```

### Model Selection Grid - RESPONSIVE CARD GRID
- **Model Cards**: Individual cards for each available forecasting model
- **Selection States**: Visual indication of selected model with ring and background
- **Model Information**: Name, description, complexity, training time, accuracy expectations
- **Recommendation Badges**: Clear indication of AI-recommended models
- **Implementation**:
```tsx
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
  {models.map((model) => (
    <Card 
      key={model.id}
      className={`cursor-pointer transition-all duration-200 hover:shadow-lg ${
        selectedModel?.id === model.id 
          ? 'ring-2 ring-primary border-primary bg-primary/5' 
          : 'hover:border-primary/50'
      } ${!model.available ? 'opacity-60 cursor-not-allowed' : ''}`}
      onClick={() => model.available && handleModelSelection(model)}
    >
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="flex items-center gap-2">
            <model.icon className="w-5 h-5" />
            {model.name}
          </CardTitle>
          <div className="flex gap-1">
            {model.recommended && (
              <Badge className="bg-green-100 text-green-800 text-xs">
                Recommended
              </Badge>
            )}
            {!model.available && (
              <Badge variant="destructive" className="text-xs">
                Unavailable
              </Badge>
            )}
          </div>
        </div>
        <CardDescription>{model.description}</CardDescription>
      </CardHeader>
      
      <CardContent className="space-y-4">
        <div className="grid grid-cols-2 gap-3 text-sm">
          <div>
            <span className="text-muted-foreground">Complexity:</span>
            <div className="flex items-center gap-1 mt-1">
              {[...Array(3)].map((_, i) => (
                <div
                  key={i}
                  className={`w-2 h-2 rounded-full ${
                    i < model.complexityLevel 
                      ? 'bg-primary' 
                      : 'bg-muted'
                  }`}
                />
              ))}
              <span className="ml-1 text-xs">{model.complexity}</span>
            </div>
          </div>
          
          <div>
            <span className="text-muted-foreground">Training Time:</span>
            <p className="font-medium mt-1">{model.trainingTime}</p>
          </div>
          
          <div>
            <span className="text-muted-foreground">Accuracy:</span>
            <p className="font-medium mt-1">{model.expectedAccuracy}</p>
          </div>
          
          <div>
            <span className="text-muted-foreground">Data Fit:</span>
            <div className="flex items-center gap-1 mt-1">
              <Progress value={model.dataFitScore} className="h-1 flex-1" />
              <span className="text-xs">{model.dataFitScore}%</span>
            </div>
          </div>
        </div>
        
        {model.available && (
          <div className="pt-2 border-t">
            <h4 className="font-medium text-sm mb-2">Best for:</h4>
            <div className="flex flex-wrap gap-1">
              {model.bestFor.map((use, index) => (
                <Badge key={index} variant="outline" className="text-xs">
                  {use}
                </Badge>
              ))}
            </div>
          </div>
        )}
        
        {!model.available && (
          <div className="pt-2 border-t">
            <p className="text-xs text-destructive">
              {model.unavailableReason}
            </p>
          </div>
        )}
      </CardContent>
      
      {selectedModel?.id === model.id && (
        <CardFooter>
          <Button className="w-full gap-2">
            <CheckCircle className="w-4 h-4" />
            Selected
          </Button>
        </CardFooter>
      )}
    </Card>
  ))}
</div>
```

### Model Comparison Table - DETAILED COMPARISON LAYOUT
- **Feature Comparison**: Side-by-side comparison of model capabilities
- **Performance Metrics**: Expected accuracy, training time, resource requirements
- **Use Case Suitability**: Specific scenarios where each model excels
- **Technical Requirements**: Computational and data requirements per model
- **Implementation**:
```tsx
<Card>
  <CardHeader>
    <CardTitle>Model Comparison</CardTitle>
    <CardDescription>
      Detailed comparison of available forecasting models
    </CardDescription>
  </CardHeader>
  <CardContent>
    <div className="overflow-x-auto">
      <table className="w-full text-sm">
        <thead>
          <tr className="border-b">
            <th className="text-left p-2 font-medium">Feature</th>
            {availableModels.map((model) => (
              <th key={model.id} className="text-left p-2 font-medium">
                <div className="flex items-center gap-2">
                  <model.icon className="w-4 h-4" />
                  {model.name}
                </div>
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          <tr className="border-b hover:bg-muted/50">
            <td className="p-2 font-medium">Training Time</td>
            {availableModels.map((model) => (
              <td key={model.id} className="p-2">{model.trainingTime}</td>
            ))}
          </tr>
          <tr className="border-b hover:bg-muted/50">
            <td className="p-2 font-medium">Expected Accuracy</td>
            {availableModels.map((model) => (
              <td key={model.id} className="p-2">
                <div className="flex items-center gap-2">
                  <Progress value={model.accuracyScore} className="h-2 flex-1" />
                  <span className="text-xs">{model.expectedAccuracy}</span>
                </div>
              </td>
            ))}
          </tr>
          <tr className="border-b hover:bg-muted/50">
            <td className="p-2 font-medium">Seasonality Support</td>
            {availableModels.map((model) => (
              <td key={model.id} className="p-2">
                {model.features.seasonality ? (
                  <CheckCircle className="w-4 h-4 text-green-600" />
                ) : (
                  <X className="w-4 h-4 text-red-600" />
                )}
              </td>
            ))}
          </tr>
          <tr className="border-b hover:bg-muted/50">
            <td className="p-2 font-medium">External Variables</td>
            {availableModels.map((model) => (
              <td key={model.id} className="p-2">
                {model.features.externalVariables ? (
                  <CheckCircle className="w-4 h-4 text-green-600" />
                ) : (
                  <X className="w-4 h-4 text-red-600" />
                )}
              </td>
            ))}
          </tr>
          <tr className="border-b hover:bg-muted/50">
            <td className="p-2 font-medium">Interpretability</td>
            {availableModels.map((model) => (
              <td key={model.id} className="p-2">
                <Badge 
                  variant={
                    model.interpretability === 'High' ? 'default' :
                    model.interpretability === 'Medium' ? 'secondary' : 'outline'
                  }
                  className="text-xs"
                >
                  {model.interpretability}
                </Badge>
              </td>
            ))}
          </tr>
        </tbody>
      </table>
    </div>
  </CardContent>
</Card>
```

### Model Configuration Panel - PARAMETER CONFIGURATION
- **Model Parameters**: Adjustable parameters for selected model
- **Advanced Settings**: Expert-level configuration options
- **Default Recommendations**: AI-suggested parameter values
- **Validation Feedback**: Real-time parameter validation
- **Implementation**:
```tsx
{selectedModel && (
  <Card>
    <CardHeader>
      <CardTitle className="flex items-center gap-2">
        <Settings className="w-5 h-5" />
        Model Configuration
      </CardTitle>
      <CardDescription>
        Configure parameters for {selectedModel.name}
      </CardDescription>
    </CardHeader>
    <CardContent className="space-y-4">
      {selectedModel.parameters.map((param) => (
        <div key={param.name} className="space-y-2">
          <div className="flex items-center justify-between">
            <label className="text-sm font-medium">{param.label}</label>
            {param.recommended && (
              <Badge variant="outline" className="text-xs">
                Recommended: {param.recommended}
              </Badge>
            )}
          </div>
          
          {param.type === 'range' ? (
            <div className="space-y-2">
              <Slider
                value={[parameterValues[param.name] || param.default]}
                onValueChange={(value) => setParameterValue(param.name, value[0])}
                min={param.min}
                max={param.max}
                step={param.step}
                className="w-full"
              />
              <div className="flex justify-between text-xs text-muted-foreground">
                <span>{param.min}</span>
                <span>{parameterValues[param.name] || param.default}</span>
                <span>{param.max}</span>
              </div>
            </div>
          ) : param.type === 'select' ? (
            <Select 
              value={parameterValues[param.name] || param.default}
              onValueChange={(value) => setParameterValue(param.name, value)}
            >
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {param.options.map((option) => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          ) : (
            <Input
              type={param.type}
              value={parameterValues[param.name] || param.default}
              onChange={(e) => setParameterValue(param.name, e.target.value)}
              placeholder={param.placeholder}
            />
          )}
          
          <p className="text-xs text-muted-foreground">{param.description}</p>
        </div>
      ))}
      
      <div className="pt-4 border-t">
        <div className="flex items-center gap-2">
          <Switch
            checked={showAdvancedSettings}
            onCheckedChange={setShowAdvancedSettings}
          />
          <label className="text-sm">Show advanced settings</label>
        </div>
      </div>
      
      {showAdvancedSettings && (
        <div className="space-y-4 pt-4 border-t">
          <h4 className="font-medium">Advanced Parameters</h4>
          {selectedModel.advancedParameters.map((param) => (
            <div key={param.name} className="space-y-2">
              <label className="text-sm font-medium">{param.label}</label>
              <Input
                type={param.type}
                value={advancedParameterValues[param.name] || param.default}
                onChange={(e) => setAdvancedParameterValue(param.name, e.target.value)}
                placeholder={param.placeholder}
              />
              <p className="text-xs text-muted-foreground">{param.description}</p>
            </div>
          ))}
        </div>
      )}
    </CardContent>
  </Card>
)}
```

### Selection Summary - CONFIRMATION PANEL
- **Selected Model Summary**: Overview of chosen model and configuration
- **Expected Outcomes**: Estimated accuracy, training time, resource usage
- **Next Steps**: Clear indication of what happens after confirmation
- **Validation Status**: Confirmation that configuration is valid for proceeding

## 4. Data Display Elements

### Available Models Information
- **Model Name**: String, source: model registry, display names like "ARIMA", "Prophet", "XGBoost"
- **Model Description**: String, source: model metadata, comprehensive model explanations
- **Complexity Level**: Enum, values: ['Low', 'Medium', 'High'] with visual indicator dots
- **Training Time**: String, source: performance benchmarks, format: "2-5 minutes", "10-30 minutes"
- **Expected Accuracy**: String, source: historical performance, format: "85-90%", "90-95%"
- **Data Fit Score**: Number, source: compatibility analysis, format: 0-100 percentage

### AI Recommendation Data
- **Recommended Model**: Object, source: recommendation engine, top model suggestion
- **Confidence Score**: Number, source: ML recommendation algorithm, format: 0-1 decimal
- **Recommendation Reasons**: Array of strings, source: analysis engine, justification points
- **Alternative Options**: Array of model objects, source: recommendation engine, secondary choices
- **Data Compatibility**: Number per model, source: analysis compatibility check

### Model Capabilities Matrix
- **Seasonality Support**: Boolean per model, source: model specifications
- **External Variables**: Boolean per model, source: model feature capabilities
- **Interpretability Level**: Enum per model, values: ['High', 'Medium', 'Low']
- **Computational Requirements**: Object per model with CPU, memory, time requirements
- **Data Size Limits**: Object per model with minimum and maximum data requirements

### Model Configuration Parameters
- **Parameter Values**: Object with current parameter settings per model
- **Default Values**: Object with recommended default values
- **Parameter Ranges**: Object with min/max values for each parameter
- **Validation Rules**: Object with validation constraints per parameter
- **Advanced Settings**: Object with expert-level configuration options

### Performance Expectations
- **Accuracy Estimates**: Range predictions based on data characteristics
- **Training Duration**: Estimated time for model training completion
- **Resource Usage**: Expected CPU, memory, and storage requirements
- **Forecast Horizon**: Maximum reliable forecasting period per model
- **Update Frequency**: Recommended model retraining frequency

### Loading States
- **Model Loading**: Skeleton cards while model information loads
- **Recommendation Loading**: Loading indicator for AI recommendation generation
- **Configuration Validation**: Loading state during parameter validation
- **Compatibility Check**: Loading indicator for data-model compatibility analysis

### Error States
- **Model Unavailable**: Clear indication when models cannot be used
- **Configuration Invalid**: Error messages for invalid parameter combinations
- **Compatibility Issues**: Warnings when model may not fit data well
- **Resource Constraints**: Notifications about insufficient computational resources

## 5. Interactive Features

### Model Selection - CARD SELECTION INTERFACE
- **Model Card Selection**: Click model cards to select with visual feedback
- **Selection States**: Clear visual indication of selected model with ring and background
- **Multi-Selection Prevention**: Only one model can be selected at a time
- **Quick Select**: Rapid selection via AI recommendation panel
- **Implementation**:
```tsx
const [selectedModel, setSelectedModel] = useState(null);
const [parameterValues, setParameterValues] = useState({});

const handleModelSelection = (model) => {
  if (!model.available) return;
  
  setSelectedModel(model);
  // Initialize parameters with defaults
  const defaultParams = {};
  model.parameters.forEach(param => {
    defaultParams[param.name] = param.default;
  });
  setParameterValues(defaultParams);
  
  // Trigger compatibility validation
  validateModelCompatibility(model);
};
```

### Parameter Configuration - DYNAMIC FORM CONTROLS
- **Slider Controls**: Interactive sliders for numeric parameters with real-time updates
- **Dropdown Selection**: Select controls for categorical parameters
- **Advanced Toggle**: Show/hide advanced configuration options
- **Default Reset**: Reset parameters to recommended default values
- **Real-time Validation**: Immediate feedback on parameter validity

### Model Comparison - INTERACTIVE COMPARISON TOOLS
- **Comparison Table**: Sort and filter model comparison table
- **Feature Highlighting**: Highlight specific capabilities across models
- **Performance Visualization**: Interactive charts comparing model performance
- **Filter Options**: Filter models by capabilities, complexity, or data requirements
- **Implementation**:
```tsx
const [comparisonFilters, setComparisonFilters] = useState({
  complexity: 'all',
  seasonality: 'all',
  interpretability: 'all'
});

const filteredModels = availableModels.filter(model => {
  if (comparisonFilters.complexity !== 'all' && model.complexity !== comparisonFilters.complexity) {
    return false;
  }
  if (comparisonFilters.seasonality !== 'all') {
    const hasSeasonality = model.features.seasonality;
    if (comparisonFilters.seasonality === 'required' && !hasSeasonality) return false;
    if (comparisonFilters.seasonality === 'not_required' && hasSeasonality) return false;
  }
  return true;
});
```

### AI Recommendation Interaction - INTELLIGENT GUIDANCE
- **Recommendation Acceptance**: Quick selection of AI-recommended model
- **Recommendation Explanation**: Expandable detailed reasoning for recommendations
- **Alternative Exploration**: Browse alternative recommendations with reasoning
- **Feedback Collection**: User feedback on recommendation quality
- **Custom Preferences**: Adjust recommendation criteria based on user priorities

### Configuration Validation - REAL-TIME FEEDBACK
- **Parameter Validation**: Immediate validation of parameter values
- **Configuration Conflicts**: Detection and resolution of conflicting parameters
- **Performance Impact**: Real-time estimation of configuration impact on performance
- **Optimization Suggestions**: AI-powered suggestions for parameter optimization
- **Implementation**:
```tsx
const validateConfiguration = (model, parameters) => {
  const validation = {
    isValid: true,
    errors: [],
    warnings: [],
    optimizations: []
  };

  // Validate individual parameters
  model.parameters.forEach(param => {
    const value = parameters[param.name];
    if (param.required && (value === undefined || value === null)) {
      validation.isValid = false;
      validation.errors.push(`${param.label} is required`);
    }
    if (param.min !== undefined && value < param.min) {
      validation.isValid = false;
      validation.errors.push(`${param.label} must be at least ${param.min}`);
    }
    if (param.max !== undefined && value > param.max) {
      validation.isValid = false;
      validation.errors.push(`${param.label} must be at most ${param.max}`);
    }
  });

  // Check for parameter conflicts
  if (parameters.autoSeasonality && parameters.seasonalPeriods) {
    validation.warnings.push('Auto seasonality is enabled but manual periods are set');
  }

  return validation;
};
```

### Export and Documentation - MODEL INFORMATION ACCESS
- **Model Documentation**: Access detailed documentation for each model
- **Configuration Export**: Export current model configuration for reference
- **Comparison Export**: Export model comparison table and analysis
- **Tutorial Access**: Step-by-step guides for specific model configurations

## 6. Navigation Elements

### Model Selection Navigation
- **Model Grid Navigation**: Browse available models with keyboard and mouse
- **Quick Selection**: Direct selection via AI recommendation panel
- **Comparison Navigation**: Navigate through detailed model comparisons
- **Configuration Access**: Navigate to parameter configuration for selected model

### Parameter Configuration Navigation
- **Parameter Navigation**: Tab through parameter controls in logical order
- **Section Navigation**: Navigate between basic and advanced parameter sections
- **Validation Navigation**: Navigate to validation errors and resolution suggestions
- **Default Reset**: Quick navigation to reset all parameters to defaults

### Workflow Navigation
- **Previous Step**: Return to Data Analysis with model selection preservation
- **Next Step**: Proceed to Feature Selection with selected model configuration
- **Skip Configuration**: Proceed with default model configuration
- **Save Configuration**: Save current model selection for later use

### Help and Documentation Navigation
- **Model Help**: Access help documentation for specific models
- **Parameter Help**: Contextual help for parameter configuration
- **Best Practices**: Navigation to model selection best practices
- **Tutorial Navigation**: Step-by-step model selection tutorials

## 7. Dynamic Behaviors

### AI Recommendation Updates - INTELLIGENT RECOMMENDATION ENGINE
- **Real-time Analysis**: Continuously analyze data characteristics for model recommendations
- **Confidence Updates**: Update recommendation confidence as more data is analyzed
- **Alternative Suggestions**: Dynamic generation of alternative model suggestions
- **Explanation Evolution**: Refine recommendation explanations based on data insights

### Model Availability Checking - DYNAMIC MODEL FILTERING
- **Resource Validation**: Real-time checking of computational resource availability
- **Data Compatibility**: Dynamic validation of model-data compatibility
- **License Verification**: Verify model licensing and availability
- **Performance Estimation**: Real-time estimation of model performance on current data

### Parameter Optimization - INTELLIGENT CONFIGURATION
- **Auto-optimization**: Automatically suggest optimal parameter values
- **Performance Prediction**: Real-time prediction of configuration performance impact
- **Conflict Resolution**: Automatic resolution of parameter conflicts
- **Best Practice Application**: Apply best practices to parameter configuration

### Visual Feedback Systems - RESPONSIVE USER INTERFACE
- **Selection Animations**: Smooth transitions for model selection states
- **Validation Indicators**: Real-time visual feedback for configuration validation
- **Progress Indicators**: Show configuration completion progress
- **Loading States**: Elegant loading animations during model analysis

### Responsive Adaptation - DEVICE-SPECIFIC OPTIMIZATION
- **Mobile Layout**: Simplified model selection interface for mobile devices
- **Touch Optimization**: Touch-friendly model selection and parameter adjustment
- **Screen Size Adaptation**: Responsive grid layout for different screen sizes
- **Performance Scaling**: Adjust interface complexity based on device capabilities

## 8. State Management

### Model Selection State - SELECTION TRACKING
- **Selected Model**: `selectedModel` object with complete model information
- **Available Models**: `availableModels` array with all model options
- **Model Filters**: `modelFilters` object with applied filter criteria
- **Selection History**: `selectionHistory` array tracking previous selections
- **Comparison State**: `comparisonState` object with comparison preferences

### Configuration State - PARAMETER MANAGEMENT
- **Parameter Values**: `parameterValues` object with current parameter settings
- **Advanced Parameters**: `advancedParameterValues` object with expert settings
- **Validation Results**: `validationResults` object with validation status
- **Default Values**: `defaultValues` object with recommended defaults
- **Configuration History**: `configurationHistory` array of previous configurations

### Recommendation State - AI GUIDANCE TRACKING
- **Current Recommendation**: `currentRecommendation` object with AI suggestion
- **Recommendation Confidence**: `recommendationConfidence` number (0-1)
- **Alternative Options**: `alternativeOptions` array of secondary recommendations
- **Recommendation Reasoning**: `recommendationReasoning` array of justification points
- **User Feedback**: `userFeedback` object with recommendation feedback

### UI Interaction State - INTERFACE STATE MANAGEMENT
- **Loading States**: `loadingStates` object tracking loading per component
- **Error States**: `errorStates` object with error information
- **Modal States**: `modalStates` object for various modal dialogs
- **Tooltip States**: `tooltipStates` object for contextual help
- **Animation States**: `animationStates` object for transition control

### Validation State - CONFIGURATION VALIDATION TRACKING
- **Validation Status**: `validationStatus` object with overall validation results
- **Parameter Errors**: `parameterErrors` object with field-specific errors
- **Configuration Warnings**: `configurationWarnings` array of non-critical issues
- **Optimization Suggestions**: `optimizationSuggestions` array of improvement suggestions
- **Validation History**: `validationHistory` array of previous validation results

## 9. API Requirements

### Model Information Endpoints
- **GET** `/api/models/available` - Get available forecasting models
  - Query: `{ dataCharacteristics: object, resourceConstraints: object }`
  - Response: `{ models: Model[], metadata: object }`

- **GET** `/api/models/:modelId/details` - Get detailed model information
  - Response: `{ model: Model, parameters: Parameter[], documentation: string }`

- **GET** `/api/models/comparison` - Get model comparison data
  - Query: `{ modelIds: string[], comparisonCriteria: string[] }`
  - Response: `{ comparison: object, capabilities: object, performance: object }`

### Recommendation Endpoints
- **POST** `/api/activities/:id/model-recommendation` - Get AI model recommendation
  - Body: `{ dataAnalysis: object, businessRequirements: object, preferences: object }`
  - Response: `{ recommendation: Model, confidence: number, reasoning: string[], alternatives: Model[] }`

- **PUT** `/api/activities/:id/model-recommendation/feedback` - Provide recommendation feedback
  - Body: `{ recommendationId: string, feedback: object, selectedModel: string }`
  - Response: `{ updated: boolean, improvedRecommendations: boolean }`

### Configuration Endpoints
- **POST** `/api/models/:modelId/validate-config` - Validate model configuration
  - Body: `{ parameters: object, dataCharacteristics: object }`
  - Response: `{ isValid: boolean, errors: string[], warnings: string[], optimizations: string[] }`

- **POST** `/api/models/:modelId/optimize-config` - Get optimized configuration
  - Body: `{ currentConfig: object, optimizationGoals: string[], constraints: object }`
  - Response: `{ optimizedConfig: object, expectedImprovement: object, explanation: string }`

### Performance Estimation Endpoints
- **POST** `/api/models/:modelId/estimate-performance` - Estimate model performance
  - Body: `{ configuration: object, dataSize: number, dataCharacteristics: object }`
  - Response: `{ accuracy: object, trainingTime: number, resourceUsage: object }`

- **GET** `/api/models/benchmarks` - Get model performance benchmarks
  - Query: `{ dataTypes: string[], dataSizes: string[], useCases: string[] }`
  - Response: `{ benchmarks: object[], comparisons: object }`

### Resource Management Endpoints
- **GET** `/api/system/resources` - Check available computational resources
  - Response: `{ cpu: object, memory: object, storage: object, availability: object }`

- **POST** `/api/models/:modelId/resource-requirements` - Get model resource requirements
  - Body: `{ configuration: object, dataSize: number }`
  - Response: `{ requirements: object, availability: boolean, alternatives: object[] }`

## 10. Business Logic

### Model Recommendation Logic - AI-POWERED MODEL SELECTION
- **Data Characteristic Analysis**: Analyze seasonality, trend, noise, and data volume
- **Business Requirement Matching**: Match model capabilities to business needs
- **Performance Prediction**: Estimate model performance based on similar datasets
- **Resource Constraint Consideration**: Factor in available computational resources
- **User Preference Integration**: Incorporate user preferences for interpretability and complexity

### Model Availability Rules - DYNAMIC MODEL FILTERING
- **Resource Availability**: Check computational resources before showing models
- **Data Size Validation**: Ensure models can handle the dataset size
- **License Verification**: Verify model licensing and usage permissions
- **Feature Compatibility**: Check if data features are compatible with model requirements
- **Performance Thresholds**: Hide models that won't meet minimum performance requirements

### Parameter Validation Logic - CONFIGURATION VALIDATION ENGINE
- **Range Validation**: Ensure parameters are within valid ranges
- **Dependency Checking**: Validate parameter dependencies and conflicts
- **Business Rule Validation**: Apply domain-specific parameter constraints
- **Performance Impact Assessment**: Evaluate configuration impact on model performance
- **Optimization Opportunity Detection**: Identify suboptimal parameter combinations

### Configuration Optimization - INTELLIGENT PARAMETER TUNING
- **Bayesian Optimization**: Use Bayesian methods for parameter optimization
- **Grid Search Alternatives**: Intelligent alternatives to exhaustive grid search
- **Performance-Resource Trade-offs**: Balance accuracy with computational efficiency
- **Robustness Considerations**: Ensure configurations are robust across data variations
- **Domain Knowledge Application**: Apply forecasting domain expertise to optimization

### Selection Validation Rules - COMPREHENSIVE VALIDATION SYSTEM
- **Model-Data Compatibility**: Comprehensive validation of model suitability for data
- **Configuration Completeness**: Ensure all required parameters are configured
- **Performance Expectation Validation**: Validate that expectations are realistic
- **Resource Allocation Verification**: Confirm sufficient resources for model training
- **Business Constraint Compliance**: Ensure selection meets business requirements

## 11. Accessibility Requirements

### ARIA Labels and Roles
- **Model Selection Grid**: `role="radiogroup" aria-label="Select forecasting model"`
- **Model Cards**: `role="radio" aria-checked={selected}` for each model option
- **Parameter Controls**: Appropriate labels and descriptions for all form controls
- **Comparison Table**: `role="table"` with proper column and row headers
- **Recommendation Panel**: `role="region" aria-label="AI model recommendation"`

### Keyboard Navigation Flow
1. **Tab Order**: AI recommendation → Model grid → Selected model configuration → Action buttons
2. **Model Selection**: Arrow keys for model navigation, Space/Enter to select
3. **Parameter Configuration**: Tab through controls, Enter to activate dropdowns
4. **Comparison Table**: Arrow keys for cell navigation, Enter for interactive elements
5. **Action Controls**: Tab to navigation buttons, Enter to activate

### Screen Reader Considerations
- **Model Descriptions**: Comprehensive descriptions of each model's capabilities
- **Selection Announcements**: Announce model selection changes and validation results
- **Parameter Guidance**: Read parameter descriptions and validation feedback
- **Recommendation Explanations**: Detailed explanations of AI recommendations
- **Configuration Feedback**: Announce parameter changes and validation results

### Focus Management
- **Model Selection**: Maintain focus on selected model after selection
- **Parameter Configuration**: Focus management within configuration panels
- **Modal Dialogs**: Focus trap within help and documentation modals
- **Validation Feedback**: Move focus to validation errors when they occur
- **Dynamic Content**: Announce and focus new content as it appears

### Visual Accessibility
- **High Contrast Selection**: Clear visual distinction for selected models
- **Color Independence**: Use patterns and text in addition to color coding
- **Focus Indicators**: High-contrast focus indicators for all interactive elements
- **Text Scaling**: Support text scaling up to 200% without loss of functionality
- **Error Visibility**: Clear visual indication of validation errors and warnings

## 12. Performance Considerations

### Model Information Loading - OPTIMIZED DATA RETRIEVAL
- **Lazy Loading**: Load detailed model information only when needed
- **Caching Strategy**: Cache model information and recommendations
- **Progressive Enhancement**: Load basic information first, details on demand
- **Background Loading**: Preload likely-to-be-needed model information
- **Compression**: Compress model documentation and detailed information

### Recommendation Generation - EFFICIENT AI PROCESSING
- **Background Processing**: Generate recommendations in background while user explores
- **Incremental Updates**: Update recommendations as more data becomes available
- **Caching**: Cache recommendation results for similar data characteristics
- **Parallel Processing**: Generate multiple recommendations simultaneously
- **Timeout Handling**: Graceful handling of slow recommendation generation

### Parameter Validation - OPTIMIZED VALIDATION ENGINE
- **Debounced Validation**: Debounce parameter validation to avoid excessive processing
- **Client-side Validation**: Perform basic validation on client side for immediate feedback
- **Batch Validation**: Batch multiple parameter validations for efficiency
- **Validation Caching**: Cache validation results for identical configurations
- **Progressive Validation**: Validate most critical parameters first

### UI Responsiveness - SMOOTH USER EXPERIENCE
- **Virtual Scrolling**: Use virtual scrolling for large model lists
- **Optimistic Updates**: Update UI immediately for better perceived performance
- **Skeleton Loading**: Show content structure while loading detailed information
- **Animation Optimization**: Optimize animations for 60fps performance
- **Memory Management**: Efficient cleanup of unused model information

### Mobile Optimization - DEVICE-SPECIFIC PERFORMANCE
- **Simplified Interface**: Reduce complexity on mobile devices
- **Touch Optimization**: Optimize touch interactions for model selection
- **Reduced Animations**: Minimize animations on lower-powered devices
- **Lazy Image Loading**: Load model icons and images on demand
- **Bandwidth Optimization**: Minimize data usage for mobile users

## 13. Edge Cases

### Model Availability Edge Cases - ROBUST MODEL HANDLING
- **No Available Models**: Handle scenarios where no models are compatible
- **Resource Constraints**: Handle insufficient computational resources
- **License Issues**: Handle model licensing and permission problems
- **Version Conflicts**: Handle model version compatibility issues
- **Network Failures**: Handle failures in model information retrieval

### Configuration Edge Cases - PARAMETER HANDLING
- **Invalid Parameter Combinations**: Handle conflicting parameter settings
- **Extreme Parameter Values**: Handle parameters at boundary conditions
- **Missing Required Parameters**: Handle incomplete configuration scenarios
- **Configuration Migration**: Handle outdated configuration formats
- **Default Value Failures**: Handle cases where default values are invalid

### Data Compatibility Edge Cases - COMPATIBILITY VALIDATION
- **Insufficient Data**: Handle datasets too small for certain models
- **Incompatible Data Types**: Handle data types not supported by models
- **Missing Required Features**: Handle datasets missing required features
- **Data Quality Issues**: Handle poor quality data affecting model selection
- **Schema Mismatches**: Handle data schema incompatibilities

### User Experience Edge Cases - INTERFACE ROBUSTNESS
- **Rapid Selection Changes**: Handle rapid model selection changes
- **Browser Limitations**: Handle browser-specific limitations and quirks
- **Session Interruption**: Handle session timeouts during model selection
- **Concurrent Users**: Handle multiple users selecting models simultaneously
- **Accessibility Edge Cases**: Handle screen reader and keyboard navigation edge cases

### Performance Edge Cases - SYSTEM LIMITATIONS
- **Large Model Catalogs**: Handle very large numbers of available models
- **Slow Networks**: Handle slow network connections gracefully
- **Memory Constraints**: Handle memory limitations on client devices
- **Processing Timeouts**: Handle timeouts in recommendation generation
- **Resource Competition**: Handle resource conflicts with other system processes

## 14. Sample Data Structure

```json
{
  "modelSelectionState": {
    "selectedModel": {
      "id": "prophet",
      "name": "Prophet",
      "description": "Robust seasonal forecasting with automatic changepoint detection",
      "icon": "TrendingUp",
      "complexity": "Low",
      "complexityLevel": 1,
      "trainingTime": "2-5 minutes",
      "expectedAccuracy": "85-92%",
      "accuracyScore": 88,
      "dataFitScore": 92,
      "available": true,
      "recommended": true,
      "confidenceScore": 0.89
    },
    "parameterValues": {
      "seasonality_mode": "multiplicative",
      "changepoint_prior_scale": 0.05,
      "seasonality_prior_scale": 10,
      "holidays_prior_scale": 10,
      "yearly_seasonality": "auto",
      "weekly_seasonality": "auto",
      "daily_seasonality": false
    },
    "validationStatus": {
      "isValid": true,
      "errors": [],
      "warnings": ["Consider enabling daily seasonality for hourly data"],
      "optimizations": ["Increase changepoint_prior_scale for more flexible trend"]
    }
  },
  "availableModels": [
    {
      "id": "prophet",
      "name": "Prophet",
      "description": "Facebook's robust seasonal forecasting algorithm with automatic changepoint detection",
      "icon": "TrendingUp",
      "complexity": "Low",
      "complexityLevel": 1,
      "trainingTime": "2-5 minutes",
      "expectedAccuracy": "85-92%",
      "accuracyScore": 88,
      "dataFitScore": 92,
      "available": true,
      "recommended": true,
      "bestFor": ["Seasonal data", "Holiday effects", "Missing values", "Trend changes"],
      "features": {
        "seasonality": true,
        "externalVariables": true,
        "missingValues": true,
        "holidays": true,
        "changepoints": true
      },
      "interpretability": "High",
      "parameters": [
        {
          "name": "seasonality_mode",
          "label": "Seasonality Mode",
          "type": "select",
          "options": [
            { "value": "additive", "label": "Additive" },
            { "value": "multiplicative", "label": "Multiplicative" }
          ],
          "default": "multiplicative",
          "recommended": "multiplicative",
          "description": "How seasonality components are combined with trend"
        },
        {
          "name": "changepoint_prior_scale",
          "label": "Changepoint Flexibility",
          "type": "range",
          "min": 0.001,
          "max": 0.5,
          "step": 0.001,
          "default": 0.05,
          "recommended": 0.05,
          "description": "Controls how flexible the trend is at adapting to changes"
        }
      ],
      "advancedParameters": [
        {
          "name": "mcmc_samples",
          "label": "MCMC Samples",
          "type": "number",
          "default": 0,
          "description": "Number of MCMC samples for uncertainty estimation"
        }
      ]
    },
    {
      "id": "arima",
      "name": "ARIMA",
      "description": "Classical statistical time series forecasting with autoregressive integrated moving average",
      "icon": "BarChart3",
      "complexity": "Medium",
      "complexityLevel": 2,
      "trainingTime": "5-15 minutes",
      "expectedAccuracy": "80-88%",
      "accuracyScore": 84,
      "dataFitScore": 78,
      "available": true,
      "recommended": false,
      "bestFor": ["Stationary data", "Short-term forecasts", "Statistical rigor"],
      "features": {
        "seasonality": true,
        "externalVariables": false,
        "missingValues": false,
        "holidays": false,
        "changepoints": false
      },
      "interpretability": "High",
      "parameters": [
        {
          "name": "auto_arima",
          "label": "Automatic Parameter Selection",
          "type": "boolean",
          "default": true,
          "recommended": true,
          "description": "Automatically select optimal ARIMA parameters"
        }
      ]
    },
    {
      "id": "xgboost",
      "name": "XGBoost",
      "description": "Gradient boosting machine learning algorithm optimized for time series forecasting",
      "icon": "Zap",
      "complexity": "High",
      "complexityLevel": 3,
      "trainingTime": "10-30 minutes",
      "expectedAccuracy": "88-95%",
      "accuracyScore": 92,
      "dataFitScore": 85,
      "available": true,
      "recommended": false,
      "bestFor": ["Complex patterns", "External variables", "High accuracy", "Large datasets"],
      "features": {
        "seasonality": true,
        "externalVariables": true,
        "missingValues": true,
        "holidays": true,
        "changepoints": true
      },
      "interpretability": "Medium",
      "parameters": [
        {
          "name": "n_estimators",
          "label": "Number of Trees",
          "type": "range",
          "min": 50,
          "max": 1000,
          "step": 50,
          "default": 100,
          "recommended": 200,
          "description": "Number of boosting rounds"
        }
      ]
    },
    {
      "id": "lstm",
      "name": "LSTM",
      "description": "Long Short-Term Memory neural network for complex sequential pattern learning",
      "icon": "Brain",
      "complexity": "High",
      "complexityLevel": 3,
      "trainingTime": "30-60 minutes",
      "expectedAccuracy": "90-96%",
      "accuracyScore": 93,
      "dataFitScore": 88,
      "available": false,
      "recommended": false,
      "unavailableReason": "Requires GPU resources not currently available",
      "bestFor": ["Complex patterns", "Long sequences", "Non-linear relationships"],
      "features": {
        "seasonality": true,
        "externalVariables": true,
        "missingValues": false,
        "holidays": true,
        "changepoints": true
      },
      "interpretability": "Low"
    }
  ],
  "aiRecommendation": {
    "recommendedModel": "prophet",
    "confidence": 0.89,
    "reasoning": [
      "Your data shows strong seasonal patterns which Prophet handles excellently",
      "Prophet automatically detects trend changes in your historical data",
      "The algorithm is robust to missing values and outliers in your dataset",
      "Holiday effects are detected and can be modeled effectively"
    ],
    "alternatives": [
      {
        "modelId": "xgboost",
        "confidence": 0.76,
        "reason": "Higher potential accuracy but requires more complex configuration"
      },
      {
        "modelId": "arima",
        "confidence": 0.62,
        "reason": "Traditional approach but may struggle with non-stationary patterns"
      }
    ],
    "dataCharacteristics": {
      "seasonality": "strong",
      "trend": "increasing",
      "changepoints": 3,
      "missingValues": 0.02,
      "outliers": 0.03,
      "dataPoints": 15000
    }
  },
  "modelComparison": {
    "criteria": [
      {
        "name": "Training Time",
        "prophet": "2-5 minutes",
        "arima": "5-15 minutes",
        "xgboost": "10-30 minutes",
        "lstm": "30-60 minutes"
      },
      {
        "name": "Expected Accuracy",
        "prophet": 88,
        "arima": 84,
        "xgboost": 92,
        "lstm": 93
      },
      {
        "name": "Seasonality Support",
        "prophet": true,
        "arima": true,
        "xgboost": true,
        "lstm": true
      },
      {
        "name": "External Variables",
        "prophet": true,
        "arima": false,
        "xgboost": true,
        "lstm": true
      },
      {
        "name": "Interpretability",
        "prophet": "High",
        "arima": "High",
        "xgboost": "Medium",
        "lstm": "Low"
      }
    ]
  },
  "performanceEstimates": {
    "prophet": {
      "accuracy": { "min": 85, "max": 92, "expected": 88 },
      "trainingTime": 180,
      "memoryUsage": "512MB",
      "cpuUsage": "Medium"
    },
    "xgboost": {
      "accuracy": { "min": 88, "max": 95, "expected": 92 },
      "trainingTime": 1200,
      "memoryUsage": "2GB",
      "cpuUsage": "High"
    }
  }
}
```

## 15. Implementation Notes

### Recommended Libraries
- **lucide-react**: Icons for model cards and interface elements
- **recharts**: Charts for model performance comparisons
- **react-hook-form**: Form handling for parameter configuration
- **zod**: Parameter validation and schema enforcement
- **framer-motion**: Smooth animations for model selection transitions
- **react-select**: Advanced dropdowns for parameter selection

### Complex Implementation Areas
- **Real-time Parameter Validation**: Implementing efficient validation with immediate feedback
- **AI Recommendation Engine**: Complex logic for model recommendation based on data analysis
- **Dynamic Model Loading**: Loading model information and capabilities dynamically
- **Configuration Optimization**: Implementing intelligent parameter optimization
- **Cross-Model Comparison**: Building flexible comparison systems for different model types

### Potential Technical Challenges
- **Model Information Management**: Managing diverse model specifications and capabilities
- **Parameter Validation Complexity**: Handling complex parameter dependencies and conflicts
- **Performance Estimation**: Accurately estimating model performance before training
- **Resource Management**: Managing computational resource allocation and availability
- **User Experience Consistency**: Maintaining consistent UX across different model types

### Performance Optimization Opportunities
- **Model Information Caching**: Cache model specifications and capabilities
- **Recommendation Caching**: Cache recommendation results for similar data characteristics
- **Parameter Validation Optimization**: Optimize validation algorithms for real-time feedback
- **Progressive Loading**: Load model information progressively based on user interest
- **Background Processing**: Process recommendations and validations in background

### Testing Considerations
- **Model Selection Logic**: Test model recommendation and selection algorithms
- **Parameter Validation**: Comprehensive testing of parameter validation rules
- **Configuration Scenarios**: Test various model configuration combinations
- **Performance Testing**: Test with different data characteristics and sizes
- **Accessibility Testing**: Ensure keyboard navigation and screen reader compatibility
- **Cross-Model Testing**: Test consistency across different model types

## 16. UI Pattern Reference

### MODEL COMPARISON GRID - Complete Selection Interface Implementation
```tsx
<div className="space-y-6">
  {/* AI Recommendation Panel */}
  <Card className="bg-gradient-to-r from-blue-50 to-purple-50 border-blue-200">
    <CardHeader>
      <CardTitle className="flex items-center gap-2">
        <Brain className="w-5 h-5 text-blue-600" />
        AI Model Recommendation
      </CardTitle>
      <CardDescription>
        Based on your data analysis, we recommend the following model
      </CardDescription>
    </CardHeader>
    <CardContent>
      <div className="flex items-center gap-4 p-4 bg-white rounded-lg border">
        <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
          <TrendingUp className="w-6 h-6 text-blue-600" />
        </div>
        <div className="flex-1">
          <h3 className="font-medium">Prophet</h3>
          <p className="text-sm text-muted-foreground">
            Robust seasonal forecasting with automatic changepoint detection
          </p>
          <div className="flex items-center gap-2 mt-2">
            <Badge className="bg-green-100 text-green-800">89% Match</Badge>
            <Badge variant="outline">Low Complexity</Badge>
          </div>
        </div>
        <Button onClick={() => handleQuickSelect(recommendedModel)}>
          Select Model
        </Button>
      </div>
      
      <div className="mt-4 p-3 bg-blue-50 rounded-lg">
        <h4 className="font-medium text-sm mb-2">Why Prophet?</h4>
        <ul className="text-sm space-y-1">
          <li className="flex items-center gap-2">
            <CheckCircle className="w-3 h-3 text-green-600" />
            Strong seasonal patterns detected in your data
          </li>
          <li className="flex items-center gap-2">
            <CheckCircle className="w-3 h-3 text-green-600" />
            Automatic trend change detection
          </li>
          <li className="flex items-center gap-2">
            <CheckCircle className="w-3 h-3 text-green-600" />
            Robust to missing values and outliers
          </li>
        </ul>
      </div>
    </CardContent>
  </Card>

  {/* Model Selection Grid - RESPONSIVE CARD GRID */}
  <div>
    <h2 className="mb-4">Available Models</h2>
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {availableModels.map((model) => (
        <Card 
          key={model.id}
          className={`cursor-pointer transition-all duration-200 hover:shadow-lg ${
            selectedModel?.id === model.id 
              ? 'ring-2 ring-primary border-primary bg-primary/5' 
              : 'hover:border-primary/50'
          } ${!model.available ? 'opacity-60 cursor-not-allowed' : ''}`}
          onClick={() => model.available && handleModelSelection(model)}
        >
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <model.icon className="w-5 h-5" />
                {model.name}
              </CardTitle>
              <div className="flex gap-1">
                {model.recommended && (
                  <Badge className="bg-green-100 text-green-800 text-xs">
                    Recommended
                  </Badge>
                )}
                {!model.available && (
                  <Badge variant="destructive" className="text-xs">
                    Unavailable
                  </Badge>
                )}
              </div>
            </div>
            <CardDescription>{model.description}</CardDescription>
          </CardHeader>
          
          <CardContent className="space-y-4">
            <div className="grid grid-cols-2 gap-3 text-sm">
              <div>
                <span className="text-muted-foreground">Complexity:</span>
                <div className="flex items-center gap-1 mt-1">
                  {[...Array(3)].map((_, i) => (
                    <div
                      key={i}
                      className={`w-2 h-2 rounded-full ${
                        i < model.complexityLevel 
                          ? 'bg-primary' 
                          : 'bg-muted'
                      }`}
                    />
                  ))}
                  <span className="ml-1 text-xs">{model.complexity}</span>
                </div>
              </div>
              
              <div>
                <span className="text-muted-foreground">Training Time:</span>
                <p className="font-medium mt-1">{model.trainingTime}</p>
              </div>
              
              <div>
                <span className="text-muted-foreground">Accuracy:</span>
                <p className="font-medium mt-1">{model.expectedAccuracy}</p>
              </div>
              
              <div>
                <span className="text-muted-foreground">Data Fit:</span>
                <div className="flex items-center gap-1 mt-1">
                  <Progress value={model.dataFitScore} className="h-1 flex-1" />
                  <span className="text-xs">{model.dataFitScore}%</span>
                </div>
              </div>
            </div>
            
            {model.available && (
              <div className="pt-2 border-t">
                <h4 className="font-medium text-sm mb-2">Best for:</h4>
                <div className="flex flex-wrap gap-1">
                  {model.bestFor.map((use, index) => (
                    <Badge key={index} variant="outline" className="text-xs">
                      {use}
                    </Badge>
                  ))}
                </div>
              </div>
            )}
          </CardContent>
          
          {selectedModel?.id === model.id && (
            <CardFooter>
              <Button className="w-full gap-2">
                <CheckCircle className="w-4 h-4" />
                Selected
              </Button>
            </CardFooter>
          )}
        </Card>
      ))}
    </div>
  </div>

  {/* Model Configuration Panel */}
  {selectedModel && (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Settings className="w-5 h-5" />
          Model Configuration - {selectedModel.name}
        </CardTitle>
        <CardDescription>
          Configure parameters for optimal performance
        </CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        {selectedModel.parameters.map((param) => (
          <div key={param.name} className="space-y-2">
            <div className="flex items-center justify-between">
              <label className="text-sm font-medium">{param.label}</label>
              {param.recommended && (
                <Badge variant="outline" className="text-xs">
                  Recommended: {param.recommended}
                </Badge>
              )}
            </div>
            
            {param.type === 'range' ? (
              <div className="space-y-2">
                <Slider
                  value={[parameterValues[param.name] || param.default]}
                  onValueChange={(value) => setParameterValue(param.name, value[0])}
                  min={param.min}
                  max={param.max}
                  step={param.step}
                  className="w-full"
                />
                <div className="flex justify-between text-xs text-muted-foreground">
                  <span>{param.min}</span>
                  <span>{parameterValues[param.name] || param.default}</span>
                  <span>{param.max}</span>
                </div>
              </div>
            ) : param.type === 'select' ? (
              <Select 
                value={parameterValues[param.name] || param.default}
                onValueChange={(value) => setParameterValue(param.name, value)}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {param.options.map((option) => (
                    <SelectItem key={option.value} value={option.value}>
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            ) : (
              <Input
                type={param.type}
                value={parameterValues[param.name] || param.default}
                onChange={(e) => setParameterValue(param.name, e.target.value)}
                placeholder={param.placeholder}
              />
            )}
            
            <p className="text-xs text-muted-foreground">{param.description}</p>
          </div>
        ))}
        
        <div className="pt-4 border-t">
          <div className="flex items-center gap-2">
            <Switch
              checked={showAdvancedSettings}
              onCheckedChange={setShowAdvancedSettings}
            />
            <label className="text-sm">Show advanced settings</label>
          </div>
        </div>
      </CardContent>
    </Card>
  )}
</div>
```

### RESPONSIVE GRID LAYOUT - Model Grid Implementation
```tsx
// Model grid with responsive breakpoints
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
  {/* 1 column on mobile, 2 on tablet, 3 on desktop */}
</div>

// Model selection state management
const [selectedModel, setSelectedModel] = useState(null);
const [parameterValues, setParameterValues] = useState({});

const handleModelSelection = (model) => {
  if (!model.available) {
    toast.error("This model is currently unavailable");
    return;
  }
  
  setSelectedModel(model);
  
  // Initialize parameters with defaults
  const defaultParams = {};
  model.parameters.forEach(param => {
    defaultParams[param.name] = param.default;
  });
  setParameterValues(defaultParams);
  
  // Trigger compatibility validation
  validateModelCompatibility(model);
  
  // Track selection for analytics
  trackModelSelection(model.id, model.recommended);
};

// Parameter configuration management
const setParameterValue = (paramName, value) => {
  setParameterValues(prev => ({
    ...prev,
    [paramName]: value
  }));
  
  // Trigger real-time validation
  validateParameter(paramName, value);
};
```

### STATE BUTTONS - Model Selection States
```tsx
// Visual states for model cards
const getModelCardClasses = (model, selectedModel) => {
  const baseClasses = "cursor-pointer transition-all duration-200 hover:shadow-lg";
  
  if (!model.available) {
    return `${baseClasses} opacity-60 cursor-not-allowed`;
  }
  
  if (selectedModel?.id === model.id) {
    return `${baseClasses} ring-2 ring-primary border-primary bg-primary/5`;
  }
  
  return `${baseClasses} hover:border-primary/50`;
};

// Model recommendation badge logic
const getRecommendationBadge = (model) => {
  if (model.recommended) {
    return (
      <Badge className="bg-green-100 text-green-800 text-xs">
        Recommended
      </Badge>
    );
  }
  
  if (!model.available) {
    return (
      <Badge variant="destructive" className="text-xs">
        Unavailable
      </Badge>
    );
  }
  
  return null;
};
```

## 17. Documentation Quality Checklist

### ✅ UI Pattern Specification:
- [x] Model selection interface labeled as "MODEL SELECTION INTERFACE" with structured layout
- [x] Model grid specified as "RESPONSIVE CARD GRID" with exact grid classes
- [x] Grid layout includes exact CSS classes: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6`
- [x] Selection states specified as "STATE BUTTONS" with visual feedback
- [x] AI recommendation panel with specific gradient and styling

### ✅ Implementation Code:
- [x] Complete TSX code examples provided for all model selection components
- [x] shadcn/ui component structure specified (Card, Button, Progress, Badge, etc.)
- [x] Exact CSS classes documented for responsive behavior and selection states
- [x] Parameter configuration with Slider, Select, and Input components
- [x] State management patterns with selection and validation logic

### ✅ Visual Elements:
- [x] Model cards with complexity indicators (dot visualization)
- [x] Progress bars for data fit scores and accuracy visualization
- [x] Recommendation badges with color-coded styling
- [x] Parameter configuration interface with sliders and dropdowns
- [x] Icon specifications throughout (Brain, TrendingUp, Settings, etc.)

### ✅ Layout Structure:
- [x] Exact grid specifications with responsive breakpoints (1-2-3 column layout)
- [x] Model card layout with header, content, and conditional footer
- [x] AI recommendation panel with gradient background and structured content
- [x] Parameter configuration with proper spacing and organization
- [x] Comparison table with proper table structure and styling

### ✅ Interaction Patterns:
- [x] Model selection states documented (available, selected, unavailable)
- [x] Parameter configuration with real-time validation
- [x] AI recommendation interaction with quick selection
- [x] Advanced settings toggle with conditional display
- [x] Quick select functionality with state management

### ❌ Rejected Generic Terms:
- [x] No usage of "model selection interface" - used "MODEL SELECTION INTERFACE" with specific structure
- [x] No vague grid descriptions - specific "RESPONSIVE CARD GRID" with exact classes
- [x] All layout descriptions include exact CSS classes and responsive behavior
- [x] Implementation code provided for all complex selection patterns
- [x] AI recommendation system specified with exact styling and behavior

**Documentation eliminates all ambiguity and provides exact implementation guidance for Step 3: Model Selection with comprehensive model comparison, AI recommendations, and parameter configuration systems.**