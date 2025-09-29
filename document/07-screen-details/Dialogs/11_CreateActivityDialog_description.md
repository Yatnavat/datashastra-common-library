# Create Activity Dialog - Screen Documentation

## 1. Screen Overview

- **Primary purpose and user goals**: Create new demand forecasting activities with client-project selection and activity configuration
- **User role/permissions required**: Authenticated users with activity creation permissions (Data Analyst, Project Manager with create rights)
- **Entry points**: From Activity Dashboard "New Activity" button, keyboard shortcut (Ctrl+N), plus icon in navigation
- **Screen priority**: Core feature - essential dialog for initiating new forecasting workflows

## 2. Visual Layout

### MODAL DIALOG INTERFACE Layout Structure
- **Layout structure**: Centered modal overlay with form-based activity creation
- **Modal Container**: `<Dialog>` component with backdrop and centered positioning
- **Form Organization**:
```tsx
<Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
  <DialogOverlay className="bg-black/50" />
  <DialogContent className="max-w-2xl">
    <DialogHeader>
      <DialogTitle>Create New Forecasting Activity</DialogTitle>
      <DialogDescription>
        Set up a new demand forecasting activity for your project
      </DialogDescription>
    </DialogHeader>
    <div className="space-y-6">
      {/* Form content */}
    </div>
  </DialogContent>
</Dialog>
```

### Responsive Breakpoints
```css
/* Modal Responsive Behavior */
Base (< 768px):     Full-screen modal, stacked form fields, simplified layout
Tablet (768px+):    Large modal (80% width), 2-column form layout where appropriate
Desktop (1024px+):  Fixed width modal (max-w-2xl), optimal form spacing
```

### FORM LAYOUT with Hierarchical Selection
- **Form Structure**: HIERARCHICAL FORM INTERFACE with Client → Project → Activity flow
- **Layout Pattern**: Vertical form with grouped sections and clear visual hierarchy
- **Field Organization**: Required fields first, optional fields in expandable section
- **Implementation**:
```tsx
<form onSubmit={handleSubmit} className="space-y-6">
  {/* Client Selection Section */}
  <div className="space-y-4">
    <div className="flex items-center gap-2">
      <Building className="w-5 h-5 text-blue-600" />
      <h3 className="font-medium">Client & Project Information</h3>
      <div className="text-xs text-red-500">* Required</div>
    </div>
    
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div className="space-y-2">
        <label className="text-sm font-medium" htmlFor="client-select">
          Client <span className="text-red-500">*</span>
        </label>
        <Select 
          value={selectedClient} 
          onValueChange={handleClientChange}
          required
        >
          <SelectTrigger id="client-select">
            <SelectValue placeholder="Select a client" />
          </SelectTrigger>
          <SelectContent>
            {clients.map((client) => (
              <SelectItem key={client.id} value={client.id}>
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 bg-blue-500 rounded-full" />
                  {client.name}
                </div>
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        {errors.client && (
          <p className="text-sm text-red-500 flex items-center gap-1">
            <AlertCircle className="w-3 h-3" />
            {errors.client}
          </p>
        )}
      </div>
      
      <div className="space-y-2">
        <label className="text-sm font-medium" htmlFor="project-select">
          Project <span className="text-red-500">*</span>
        </label>
        <Select 
          value={selectedProject} 
          onValueChange={setSelectedProject}
          disabled={!selectedClient}
          required
        >
          <SelectTrigger id="project-select">
            <SelectValue placeholder={selectedClient ? "Select a project" : "Select client first"} />
          </SelectTrigger>
          <SelectContent>
            {availableProjects.map((project) => (
              <SelectItem key={project.id} value={project.id}>
                <div className="flex items-center gap-2">
                  <Folder className="w-3 h-3" />
                  {project.name}
                </div>
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        {errors.project && (
          <p className="text-sm text-red-500 flex items-center gap-1">
            <AlertCircle className="w-3 h-3" />
            {errors.project}
          </p>
        )}
      </div>
    </div>
    
    {selectedClient && selectedProject && (
      <div className="p-3 bg-blue-50 rounded-lg border border-blue-200">
        <div className="flex items-center gap-2 text-sm">
          <Info className="w-4 h-4 text-blue-600" />
          <span className="text-blue-800">
            <strong>{clients.find(c => c.id === selectedClient)?.name}</strong> → 
            <strong className="ml-1">{availableProjects.find(p => p.id === selectedProject)?.name}</strong>
          </span>
        </div>
      </div>
    )}
  </div>
  
  <Separator />
  
  {/* Activity Details Section */}
  <div className="space-y-4">
    <div className="flex items-center gap-2">
      <Activity className="w-5 h-5 text-green-600" />
      <h3 className="font-medium">Activity Details</h3>
    </div>
    
    <div className="space-y-4">
      <div className="space-y-2">
        <label className="text-sm font-medium" htmlFor="activity-name">
          Activity Name <span className="text-red-500">*</span>
        </label>
        <Input
          id="activity-name"
          value={activityName}
          onChange={(e) => setActivityName(e.target.value)}
          placeholder="Enter a descriptive name for your forecasting activity"
          className={errors.activityName ? "border-red-500" : ""}
          required
        />
        {errors.activityName && (
          <p className="text-sm text-red-500 flex items-center gap-1">
            <AlertCircle className="w-3 h-3" />
            {errors.activityName}
          </p>
        )}
        <p className="text-xs text-muted-foreground">
          Choose a name that clearly identifies the purpose and scope of this forecast
        </p>
      </div>
      
      <div className="space-y-2">
        <label className="text-sm font-medium" htmlFor="activity-description">
          Description
        </label>
        <Textarea
          id="activity-description"
          value={activityDescription}
          onChange={(e) => setActivityDescription(e.target.value)}
          placeholder="Provide additional details about this forecasting activity (optional)"
          rows={3}
          className="resize-none"
        />
        <p className="text-xs text-muted-foreground">
          Provide context and objectives for this forecasting activity
        </p>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="space-y-2">
          <label className="text-sm font-medium" htmlFor="industry-select">
            Industry
          </label>
          <Select value={selectedIndustry} onValueChange={setSelectedIndustry}>
            <SelectTrigger id="industry-select">
              <SelectValue placeholder="Select industry (optional)" />
            </SelectTrigger>
            <SelectContent>
              {industries.map((industry) => (
                <SelectItem key={industry.id} value={industry.id}>
                  {industry.name}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        
        <div className="space-y-2">
          <label className="text-sm font-medium" htmlFor="priority-select">
            Priority
          </label>
          <Select value={selectedPriority} onValueChange={setSelectedPriority}>
            <SelectTrigger id="priority-select">
              <SelectValue placeholder="Set priority level" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="high">
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 bg-red-500 rounded-full" />
                  High Priority
                </div>
              </SelectItem>
              <SelectItem value="medium">
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 bg-yellow-500 rounded-full" />
                  Medium Priority
                </div>
              </SelectItem>
              <SelectItem value="low">
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 bg-green-500 rounded-full" />
                  Low Priority
                </div>
              </SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>
    </div>
  </div>
  
  <Separator />
  
  {/* Advanced Options (Collapsible) */}
  <div className="space-y-4">
    <Button
      type="button"
      variant="ghost"
      onClick={() => setShowAdvancedOptions(!showAdvancedOptions)}
      className="flex items-center gap-2 text-sm"
    >
      <ChevronRight className={`w-4 h-4 transition-transform ${showAdvancedOptions ? 'rotate-90' : ''}`} />
      Advanced Options
    </Button>
    
    {showAdvancedOptions && (
      <div className="space-y-4 pl-6 border-l-2 border-muted">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="space-y-2">
            <label className="text-sm font-medium" htmlFor="forecast-horizon">
              Default Forecast Horizon (days)
            </label>
            <Input
              id="forecast-horizon"
              type="number"
              value={forecastHorizon}
              onChange={(e) => setForecastHorizon(parseInt(e.target.value))}
              placeholder="90"
              min="30"
              max="365"
            />
          </div>
          
          <div className="space-y-2">
            <label className="text-sm font-medium" htmlFor="auto-start">
              Workflow Configuration
            </label>
            <div className="space-y-2">
              <div className="flex items-center space-x-2">
                <Checkbox
                  id="auto-start"
                  checked={autoStartWorkflow}
                  onCheckedChange={setAutoStartWorkflow}
                />
                <label htmlFor="auto-start" className="text-sm">
                  Auto-start workflow after creation
                </label>
              </div>
              <div className="flex items-center space-x-2">
                <Checkbox
                  id="template-mode"
                  checked={templateMode}
                  onCheckedChange={setTemplateMode}
                />
                <label htmlFor="template-mode" className="text-sm">
                  Create as template for future use
                </label>
              </div>
            </div>
          </div>
        </div>
        
        <div className="space-y-2">
          <label className="text-sm font-medium" htmlFor="tags-input">
            Tags
          </label>
          <Input
            id="tags-input"
            value={tags}
            onChange={(e) => setTags(e.target.value)}
            placeholder="Add tags separated by commas (e.g., quarterly, electronics, high-volume)"
          />
          <p className="text-xs text-muted-foreground">
            Tags help organize and search activities later
          </p>
        </div>
      </div>
    )}
  </div>
  
  {/* Form Actions */}
  <div className="flex items-center justify-between pt-6 border-t">
    <div className="text-sm text-muted-foreground">
      <span className="text-red-500">*</span> Required fields
    </div>
    <div className="flex items-center gap-3">
      <Button 
        type="button" 
        variant="outline" 
        onClick={handleCancel}
      >
        Cancel
      </Button>
      <Button 
        type="submit" 
        disabled={!isFormValid || isCreating}
        className="min-w-32"
      >
        {isCreating ? (
          <>
            <div className="w-4 h-4 mr-2">
              <div className="animate-spin rounded-full h-4 w-4 border-2 border-white border-t-transparent" />
            </div>
            Creating...
          </>
        ) : (
          <>
            <Plus className="w-4 h-4 mr-2" />
            Create Activity
          </>
        )}
      </Button>
    </div>
  </div>
</form>
```

### Typography System (14px base - NO OVERRIDES)
- **Dialog Title**: 20px (h1 default) + font-medium "Create New Forecasting Activity"
- **Section Headers**: 16px (h3 default) + font-medium for Client & Project, Activity Details
- **Field Labels**: 14px (label default) + font-medium for form field labels
- **Field Descriptions**: 14px (p default) + font-normal for help text
- **Error Messages**: 14px (p default) + font-normal for validation feedback

### Color Scheme
- **Modal Backdrop**: `bg-black/50` semi-transparent overlay
- **Modal Content**: `bg-background` with standard card styling
- **Required Field Indicators**: `text-red-500` for asterisks and error states
- **Section Icons**: Category-specific colors (blue for client, green for activity)
- **Validation States**: Red borders for errors, green highlights for valid required fields

## 3. Components Inventory

### Dialog Container - MODAL DIALOG STRUCTURE
- **Modal Overlay**: Semi-transparent backdrop with click-to-close functionality
- **Dialog Content**: Centered modal with maximum width constraint
- **Close Button**: X button in top-right corner for dialog dismissal
- **Responsive Sizing**: Adapts from full-screen on mobile to fixed-width on desktop

### Client Selection Section - HIERARCHICAL DROPDOWN INTERFACE
- **Client Dropdown**: Primary selection that determines available projects
- **Project Dropdown**: Secondary selection dependent on client choice
- **Selection Validation**: Real-time validation of required selections
- **Context Display**: Shows selected client-project hierarchy with visual confirmation
- **Implementation**:
```tsx
<div className="space-y-4">
  <div className="flex items-center gap-2 mb-4">
    <Building className="w-5 h-5 text-blue-600" />
    <h3 className="font-medium">Client & Project Information</h3>
    <Badge variant="destructive" className="text-xs">Required</Badge>
  </div>
  
  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
    <FormField
      control={form.control}
      name="client"
      render={({ field }) => (
        <FormItem>
          <FormLabel>Client *</FormLabel>
          <Select onValueChange={(value) => {
            field.onChange(value);
            handleClientChange(value);
          }} value={field.value}>
            <FormControl>
              <SelectTrigger className={errors.client ? "border-red-500" : ""}>
                <SelectValue placeholder="Choose your client" />
              </SelectTrigger>
            </FormControl>
            <SelectContent>
              <SelectGroup>
                <SelectLabel>Available Clients</SelectLabel>
                {clients.map((client) => (
                  <SelectItem key={client.id} value={client.id}>
                    <div className="flex items-center gap-3">
                      <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                        <Building className="w-4 h-4 text-blue-600" />
                      </div>
                      <div>
                        <div className="font-medium">{client.name}</div>
                        <div className="text-xs text-muted-foreground">
                          {client.projectCount} projects
                        </div>
                      </div>
                    </div>
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
          <FormMessage />
        </FormItem>
      )}
    />
    
    <FormField
      control={form.control}
      name="project"
      render={({ field }) => (
        <FormItem>
          <FormLabel>Project *</FormLabel>
          <Select 
            onValueChange={field.onChange} 
            value={field.value}
            disabled={!selectedClient}
          >
            <FormControl>
              <SelectTrigger className={errors.project ? "border-red-500" : ""}>
                <SelectValue placeholder={
                  selectedClient ? "Choose your project" : "Select client first"
                } />
              </SelectTrigger>
            </FormControl>
            <SelectContent>
              <SelectGroup>
                <SelectLabel>Available Projects</SelectLabel>
                {availableProjects.map((project) => (
                  <SelectItem key={project.id} value={project.id}>
                    <div className="flex items-center gap-3">
                      <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
                        <Folder className="w-4 h-4 text-green-600" />
                      </div>
                      <div>
                        <div className="font-medium">{project.name}</div>
                        <div className="text-xs text-muted-foreground">
                          {project.activityCount} activities
                        </div>
                      </div>
                    </div>
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
          <FormMessage />
        </FormItem>
      )}
    />
  </div>
  
  {/* Selection Confirmation */}
  {selectedClient && selectedProject && (
    <div className="p-4 bg-gradient-to-r from-blue-50 to-green-50 rounded-lg border">
      <div className="flex items-center gap-3">
        <CheckCircle className="w-5 h-5 text-green-600" />
        <div>
          <div className="font-medium">Selected Configuration</div>
          <div className="text-sm text-muted-foreground">
            {clients.find(c => c.id === selectedClient)?.name} → {availableProjects.find(p => p.id === selectedProject)?.name}
          </div>
        </div>
      </div>
    </div>
  )}
</div>
```

### Activity Configuration Section - ACTIVITY DETAILS FORM
- **Activity Name Input**: Required text field with real-time validation
- **Description Textarea**: Optional longer description with character limit
- **Industry Selection**: Optional industry categorization dropdown
- **Priority Selection**: Priority level selection with visual indicators
- **Tags Input**: Comma-separated tags for organization and search

### Advanced Options Section - EXPANDABLE CONFIGURATION
- **Collapsible Section**: Expandable section for advanced settings
- **Default Parameters**: Pre-configure default forecasting parameters
- **Workflow Options**: Options for post-creation workflow behavior
- **Template Options**: Option to create activity as reusable template

### Form Validation Display - VALIDATION FEEDBACK INTERFACE
- **Real-time Validation**: Immediate validation feedback as user types
- **Error Highlighting**: Red borders and error messages for invalid fields
- **Success Indicators**: Green highlights for valid required fields
- **Form Completion**: Overall form validity indicator and submission readiness

## 4. Data Display Elements

### Client and Project Data
- **Available Clients**: Array of client objects with names and project counts
- **Available Projects**: Array of project objects filtered by selected client
- **Client Metadata**: Additional client information (industry, location, etc.)
- **Project Metadata**: Project details including activity count and status
- **Selection Hierarchy**: Visual representation of client-project relationship

### Form Field Data
- **Activity Name**: String, user input with uniqueness validation
- **Activity Description**: String, optional longer description
- **Selected Industry**: String from predefined industry list
- **Selected Priority**: Enum, values: ['high', 'medium', 'low']
- **Tags**: Array of strings from comma-separated input

### Advanced Configuration Data
- **Forecast Horizon**: Number, default horizon for new activity (30-365 days)
- **Auto-start Workflow**: Boolean, whether to automatically start workflow
- **Template Mode**: Boolean, whether to create as reusable template
- **Default Model**: Optional pre-selected model for quick setup
- **Configuration Presets**: Available configuration templates

### Validation State Data
- **Form Validity**: Object with validity status per field and overall
- **Error Messages**: Object with specific error messages per field
- **Field States**: Object with validation states (valid, invalid, pending)
- **Submission State**: Boolean indicating if form can be submitted
- **Real-time Feedback**: Object with immediate validation feedback

### Loading States
- **Client Loading**: Loading state while fetching available clients
- **Project Loading**: Loading state while fetching projects for selected client
- **Creation Loading**: Loading state during activity creation process
- **Validation Loading**: Loading state during name uniqueness validation

### Error States
- **Network Errors**: Handle failures in client/project data loading
- **Validation Errors**: Display field-specific validation errors
- **Creation Failures**: Handle failures during activity creation
- **Permission Errors**: Handle insufficient permissions for creation

## 5. Interactive Features

### Hierarchical Selection - CASCADING DROPDOWN INTERFACE
- **Client Selection**: Primary dropdown that filters available projects
- **Project Dependency**: Project dropdown enabled only after client selection
- **Dynamic Loading**: Load projects dynamically based on client selection
- **Selection Validation**: Ensure both client and project are selected
- **Implementation**:
```tsx
const handleClientChange = async (clientId) => {
  setSelectedClient(clientId);
  setSelectedProject(''); // Reset project selection
  setIsLoadingProjects(true);
  
  try {
    const projects = await fetchProjectsForClient(clientId);
    setAvailableProjects(projects);
  } catch (error) {
    toast.error('Failed to load projects for selected client');
    setAvailableProjects([]);
  } finally {
    setIsLoadingProjects(false);
  }
  
  // Validate form after client change
  validateForm();
};
```

### Real-time Form Validation - COMPREHENSIVE VALIDATION SYSTEM
- **Field-level Validation**: Immediate validation as user types or selects
- **Cross-field Validation**: Validate field dependencies and combinations
- **Uniqueness Validation**: Check activity name uniqueness within project
- **Format Validation**: Validate input formats and constraints
- **Implementation**:
```tsx
const validateField = (fieldName, value) => {
  const newErrors = { ...errors };
  
  switch (fieldName) {
    case 'activityName':
      if (!value.trim()) {
        newErrors.activityName = 'Activity name is required';
      } else if (value.length < 3) {
        newErrors.activityName = 'Activity name must be at least 3 characters';
      } else if (value.length > 100) {
        newErrors.activityName = 'Activity name must be less than 100 characters';
      } else {
        delete newErrors.activityName;
        // Check uniqueness
        checkNameUniqueness(value, selectedClient, selectedProject);
      }
      break;
      
    case 'client':
      if (!value) {
        newErrors.client = 'Client selection is required';
      } else {
        delete newErrors.client;
      }
      break;
      
    case 'project':
      if (!value) {
        newErrors.project = 'Project selection is required';
      } else {
        delete newErrors.project;
      }
      break;
  }
  
  setErrors(newErrors);
  setIsFormValid(Object.keys(newErrors).length === 0 && isRequiredFieldsComplete());
};

const checkNameUniqueness = async (name, clientId, projectId) => {
  if (!name || !clientId || !projectId) return;
  
  try {
    const isUnique = await validateActivityNameUniqueness({
      name,
      clientId,
      projectId
    });
    
    if (!isUnique) {
      setErrors(prev => ({
        ...prev,
        activityName: 'An activity with this name already exists in the selected project'
      }));
    }
  } catch (error) {
    // Handle validation error silently
  }
};
```

### Advanced Options Toggle - PROGRESSIVE DISCLOSURE
- **Expandable Section**: Collapsible advanced options with smooth animation
- **Visual Indicators**: Chevron rotation and section highlighting
- **State Persistence**: Remember expanded state during session
- **Default Values**: Sensible defaults for advanced options

### Form Submission - CREATION PROCESS MANAGEMENT
- **Submission Validation**: Final validation before creation attempt
- **Loading States**: Visual feedback during creation process
- **Success Handling**: Success confirmation and automatic navigation
- **Error Recovery**: Clear error handling with retry options
- **Implementation**:
```tsx
const handleSubmit = async (event) => {
  event.preventDefault();
  
  if (!isFormValid) {
    toast.error('Please correct the form errors before submitting');
    return;
  }
  
  setIsCreating(true);
  
  try {
    const activityData = {
      name: activityName.trim(),
      description: activityDescription.trim(),
      client: selectedClient,
      project: selectedProject,
      industry: selectedIndustry,
      priority: selectedPriority,
      tags: tags.split(',').map(tag => tag.trim()).filter(Boolean),
      configuration: {
        forecastHorizon: forecastHorizon,
        autoStartWorkflow: autoStartWorkflow,
        templateMode: templateMode
      }
    };
    
    const newActivity = await createActivity(activityData);
    
    toast.success('Activity created successfully!');
    
    // Close dialog and navigate
    setIsCreateDialogOpen(false);
    
    if (autoStartWorkflow) {
      navigate(`/activities/${newActivity.id}/workflow`);
    } else {
      // Refresh activity list and show new activity
      refreshActivityList();
      setSelectedActivity(newActivity);
    }
    
  } catch (error) {
    toast.error(`Failed to create activity: ${error.message}`);
  } finally {
    setIsCreating(false);
  }
};
```

### Form Reset and Cancel - DIALOG MANAGEMENT
- **Cancel Confirmation**: Confirm cancellation if user has entered data
- **Form Reset**: Clear all form data and return to initial state
- **Escape Key Handling**: Close dialog with Escape key
- **Click-outside Dismissal**: Close dialog when clicking outside (with confirmation)

### Template Integration - TEMPLATE SYSTEM
- **Template Selection**: Option to start from existing activity template
- **Template Preview**: Preview template configuration before application
- **Template Saving**: Save current configuration as template
- **Template Management**: Access to saved templates and sharing

## 6. Navigation Elements

### Dialog Navigation
- **Modal Focus**: Trap focus within modal dialog
- **Tab Order**: Logical progression through form fields
- **Keyboard Shortcuts**: Enter to submit, Escape to close
- **Field Navigation**: Smooth navigation between form fields

### Form Navigation
- **Section Navigation**: Navigate between client selection and activity details
- **Field Focus**: Automatic focus on first empty required field
- **Error Navigation**: Jump to first field with validation error
- **Advanced Options**: Expand/collapse advanced configuration section

### Post-Creation Navigation
- **Activity Dashboard**: Return to dashboard with new activity highlighted
- **Workflow Start**: Automatically navigate to workflow if auto-start enabled
- **Activity Details**: Navigate to activity details view
- **Template Management**: Access template management for saved configurations

### Help Navigation
- **Field Help**: Contextual help for form fields and selections
- **Best Practices**: Links to activity creation best practices
- **Tutorial Access**: Guided tutorial for first-time users
- **Support Links**: Contact information for creation assistance

## 7. Dynamic Behaviors

### Cascading Selection Updates - DYNAMIC FORM BEHAVIOR
- **Project Filtering**: Dynamically filter projects based on client selection
- **Real-time Loading**: Show loading states during project fetching
- **Selection Preservation**: Maintain selections during navigation
- **Context Updates**: Update context information as selections change

### Form Validation Feedback - IMMEDIATE VALIDATION RESPONSE
- **Real-time Validation**: Validate fields as user types or selects
- **Visual Feedback**: Immediate visual feedback for validation states
- **Error Resolution**: Clear errors as soon as they are resolved
- **Success Indication**: Positive feedback for completed required fields

### Advanced Options Animation - SMOOTH PROGRESSIVE DISCLOSURE
- **Expand/Collapse Animation**: Smooth animation for advanced options section
- **Height Transitions**: Smooth height transitions during expand/collapse
- **Content Fade**: Fade in/out animation for advanced option content
- **Icon Rotation**: Rotating chevron icon for expand/collapse state

### Submission Process - CREATION FLOW MANAGEMENT
- **Loading Animation**: Loading spinner and disabled state during creation
- **Progress Feedback**: Show creation progress for long-running operations
- **Success Animation**: Success confirmation with positive feedback
- **Error Handling**: Clear error display with recovery options

### Responsive Adaptation - DEVICE-SPECIFIC BEHAVIOR
- **Mobile Modal**: Full-screen modal experience on mobile devices
- **Touch Optimization**: Touch-friendly form controls and interactions
- **Keyboard Adaptation**: Enhanced keyboard support for desktop users
- **Screen Size Adaptation**: Responsive form layout for different screen sizes

## 8. State Management

### Form Data State - FORM INPUT TRACKING
- **Form Values**: `formValues` object with all form field values
- **Selected Client**: `selectedClient` string with client ID
- **Selected Project**: `selectedProject` string with project ID
- **Activity Configuration**: `activityConfig` object with activity details
- **Advanced Options**: `advancedOptions` object with advanced configuration

### Validation State - FORM VALIDATION TRACKING
- **Form Validity**: `isFormValid` boolean indicating overall form validity
- **Field Errors**: `fieldErrors` object with validation errors per field
- **Field States**: `fieldStates` object with validation states per field
- **Validation History**: `validationHistory` array tracking validation attempts
- **Submission Ready**: `submissionReady` boolean indicating readiness to submit

### Loading State - ASYNC OPERATION TRACKING
- **Client Loading**: `isLoadingClients` boolean for client data loading
- **Project Loading**: `isLoadingProjects` boolean for project data loading
- **Creation Loading**: `isCreating` boolean for activity creation process
- **Validation Loading**: `isValidating` boolean for async validation operations
- **Data Dependencies**: `dataDependencies` object tracking data loading states

### Dialog State - MODAL MANAGEMENT
- **Dialog Open**: `isDialogOpen` boolean controlling dialog visibility
- **Dialog History**: `dialogHistory` array tracking dialog interaction history
- **Focus State**: `focusState` object managing focus within dialog
- **Escape Handling**: `escapeHandling` object managing dialog dismissal
- **Backdrop Behavior**: `backdropBehavior` object controlling backdrop interactions

### Template State - TEMPLATE SYSTEM MANAGEMENT
- **Available Templates**: `availableTemplates` array of activity templates
- **Selected Template**: `selectedTemplate` string with template ID
- **Template Data**: `templateData` object with template configuration
- **Template History**: `templateHistory` array of recently used templates
- **Template Sharing**: `templateSharing` object with sharing configurations

## 9. API Requirements

### Client and Project Data Endpoints
- **GET** `/api/clients` - Get available clients for activity creation
  - Response: `{ clients: Client[], totalCount: number }`

- **GET** `/api/clients/:clientId/projects` - Get projects for specific client
  - Response: `{ projects: Project[], clientInfo: object }`

- **GET** `/api/projects/:projectId/details` - Get detailed project information
  - Response: `{ project: Project, activityCount: number, permissions: object }`

### Activity Creation Endpoints
- **POST** `/api/activities` - Create new forecasting activity
  - Body: `{ name: string, description?: string, client: string, project: string, industry?: string, priority: string, tags?: string[], configuration?: object }`
  - Response: `{ activity: Activity, created: boolean, warnings?: string[] }`

- **GET** `/api/activities/validate-name` - Validate activity name uniqueness
  - Query: `{ name: string, clientId: string, projectId: string }`
  - Response: `{ isUnique: boolean, suggestions?: string[], conflicts?: object[] }`

### Template Management Endpoints
- **GET** `/api/activity-templates` - Get available activity templates
  - Query: `{ category?: string, industry?: string, clientId?: string }`
  - Response: `{ templates: Template[], categories: string[] }`

- **POST** `/api/activity-templates` - Save activity configuration as template
  - Body: `{ name: string, description: string, configuration: object, metadata: object }`
  - Response: `{ template: Template, saved: boolean }`

### Configuration Endpoints
- **GET** `/api/system/defaults` - Get system default configurations
  - Response: `{ defaults: object, industries: string[], priorities: string[] }`

- **GET** `/api/system/options` - Get available options for form fields
  - Response: `{ industries: string[], priorities: object[], tags: string[] }`

## 10. Business Logic

### Activity Creation Logic - COMPREHENSIVE CREATION ENGINE
- **Name Uniqueness Validation**: Ensure activity names are unique within project scope
- **Permission Validation**: Verify user has permission to create activities in selected project
- **Configuration Validation**: Validate activity configuration for consistency
- **Resource Allocation**: Check and allocate necessary resources for new activity
- **Audit Trail Creation**: Create audit trail for new activity creation

### Client-Project Relationship Logic - HIERARCHICAL DATA MANAGEMENT
- **Client Filtering**: Filter and display only accessible clients for user
- **Project Dependency**: Ensure project belongs to selected client
- **Permission Inheritance**: Apply client and project permissions to new activity
- **Data Validation**: Validate client-project relationship integrity
- **Access Control**: Enforce access controls based on user permissions

### Form Validation Logic - COMPREHENSIVE VALIDATION ENGINE
- **Required Field Validation**: Enforce required fields with clear error messages
- **Format Validation**: Validate input formats and constraints
- **Business Rule Validation**: Apply business rules to form data
- **Cross-field Validation**: Validate dependencies between form fields
- **Real-time Feedback**: Provide immediate validation feedback

### Template Integration Logic - TEMPLATE SYSTEM ENGINE
- **Template Application**: Apply template configurations to form
- **Template Validation**: Validate template compatibility with selected client/project
- **Template Customization**: Allow customization of applied templates
- **Template Saving**: Save current form configuration as new template
- **Template Sharing**: Handle template sharing with team members

### Default Configuration Logic - INTELLIGENT DEFAULTS ENGINE
- **Context-aware Defaults**: Apply defaults based on client/project context
- **User Preference Application**: Apply user's preferred default settings
- **Industry-specific Defaults**: Apply industry-appropriate default configurations
- **Learning from History**: Learn from user's previous activity configurations
- **Optimization**: Optimize defaults based on successful past activities

## 11. Accessibility Requirements

### ARIA Labels and Roles
- **Modal Dialog**: `role="dialog" aria-labelledby="dialog-title" aria-describedby="dialog-description"`
- **Form Controls**: Proper labels and descriptions for all form elements
- **Required Fields**: `aria-required="true"` for mandatory fields
- **Error Messages**: `aria-describedby` linking to error messages
- **Dynamic Content**: `aria-live="polite"` for validation feedback

### Keyboard Navigation Flow
1. **Tab Order**: Client selection → Project selection → Activity name → Description → Industry → Priority
2. **Modal Focus**: Focus trapped within modal dialog
3. **Dropdown Navigation**: Arrow keys for option navigation, Enter to select
4. **Form Submission**: Enter to submit when form is valid, Escape to cancel
5. **Advanced Options**: Tab into and through advanced configuration options

### Screen Reader Considerations
- **Field Descriptions**: Comprehensive descriptions for all form fields
- **Selection Announcements**: Announce client and project selections
- **Validation Announcements**: Announce validation errors and successes
- **Progress Updates**: Announce creation progress and completion
- **Error Communication**: Clear communication of errors and resolution steps

### Focus Management
- **Initial Focus**: Focus on first form field when dialog opens
- **Error Focus**: Move focus to first error field when validation fails
- **Modal Trap**: Trap focus within dialog, return to trigger on close
- **Dynamic Focus**: Manage focus as form sections expand or collapse
- **Success Focus**: Focus on success message or next action after creation

### Visual Accessibility
- **High Contrast Forms**: Ensure form controls meet accessibility contrast requirements
- **Error Indicators**: Use color, icons, and text for error indication
- **Focus Indicators**: Clear, high-contrast focus indicators for all interactive elements
- **Text Scaling**: Support text scaling up to 200% without horizontal scrolling
- **Required Field Marking**: Clear visual indication of required fields

## 12. Performance Considerations

### Form Performance - OPTIMIZED FORM HANDLING
- **Debounced Validation**: Debounce validation to prevent excessive API calls
- **Lazy Loading**: Load form data only when dialog is opened
- **Caching**: Cache client and project data to avoid repeated requests
- **Optimistic Updates**: Update UI immediately for better perceived performance
- **Background Validation**: Perform expensive validation in background

### Modal Rendering - EFFICIENT MODAL DISPLAY
- **Lazy Rendering**: Render modal content only when needed
- **Animation Optimization**: Optimize modal animations for smooth performance
- **Focus Management**: Efficient focus management without performance impact
- **Memory Cleanup**: Proper cleanup when modal is closed
- **Event Handling**: Efficient event handling for modal interactions

### Data Loading Performance - OPTIMIZED DATA RETRIEVAL
- **Progressive Loading**: Load client data first, then projects on selection
- **Parallel Requests**: Load independent data in parallel where possible
- **Request Batching**: Batch related API requests for efficiency
- **Error Recovery**: Fast recovery from failed data loading attempts
- **Timeout Handling**: Appropriate timeouts for data loading operations

### Validation Performance - EFFICIENT VALIDATION ENGINE
- **Client-side Validation**: Perform basic validation on client for immediate feedback
- **Async Validation**: Handle expensive validation (uniqueness checks) asynchronously
- **Validation Caching**: Cache validation results for identical inputs
- **Batch Validation**: Batch multiple validation requests when possible
- **Progressive Validation**: Validate most critical fields first

### Mobile Performance - DEVICE-OPTIMIZED EXPERIENCE
- **Touch Optimization**: Optimize form controls for touch interaction
- **Keyboard Optimization**: Optimize virtual keyboard behavior on mobile
- **Loading Optimization**: Minimize loading time for mobile users
- **Memory Usage**: Optimize memory usage for mobile devices
- **Network Efficiency**: Minimize network requests for mobile users

## 13. Edge Cases

### Data Loading Edge Cases - ROBUST DATA HANDLING
- **No Available Clients**: Handle cases where user has no accessible clients
- **Empty Project Lists**: Handle clients with no available projects
- **Network Failures**: Handle network failures during data loading
- **Slow Responses**: Handle slow API responses with appropriate timeouts
- **Concurrent Updates**: Handle data changes during form completion

### Form Validation Edge Cases - VALIDATION ROBUSTNESS
- **Rapid Input Changes**: Handle rapid input changes without validation conflicts
- **Special Characters**: Handle special characters in activity names and descriptions
- **Unicode Support**: Support international characters and names
- **Length Limits**: Handle input length limits gracefully
- **Invalid Characters**: Handle invalid characters with clear error messages

### Creation Process Edge Cases - CREATION ROBUSTNESS
- **Concurrent Creation**: Handle multiple users creating activities simultaneously
- **Creation Timeouts**: Handle timeouts during activity creation process
- **Partial Creation**: Handle partial creation failures with cleanup
- **Duplicate Prevention**: Prevent duplicate submissions during creation
- **Resource Exhaustion**: Handle resource exhaustion during creation

### User Experience Edge Cases - INTERFACE ROBUSTNESS
- **Rapid Dialog Actions**: Handle rapid opening/closing of dialog
- **Browser Limitations**: Handle browser-specific limitations and quirks
- **Session Interruption**: Handle session timeouts during form completion
- **Data Loss Prevention**: Prevent data loss from accidental dialog closure
- **Recovery Options**: Provide recovery options for interrupted creation

### Permission Edge Cases - ACCESS CONTROL ROBUSTNESS
- **Permission Changes**: Handle permission changes during form completion
- **Access Revocation**: Handle access revocation during creation process
- **Role Changes**: Handle user role changes affecting creation permissions
- **Client Access**: Handle loss of client access during form completion
- **Project Permissions**: Handle project permission changes during creation

## 14. Sample Data Structure

```json
{
  "dialogState": {
    "isOpen": true,
    "openedAt": "2024-01-20T20:15:00Z",
    "focusedField": "activityName",
    "showAdvancedOptions": false,
    "hasUnsavedChanges": true
  },
  "formData": {
    "selectedClient": "client_001",
    "selectedProject": "project_045",
    "activityName": "Q1 2024 Electronics Forecast",
    "activityDescription": "Quarterly demand forecasting for consumer electronics product line focusing on seasonal patterns and promotional impacts",
    "selectedIndustry": "technology",
    "selectedPriority": "high",
    "tags": ["quarterly", "electronics", "seasonal", "promotions"],
    "advancedOptions": {
      "forecastHorizon": 90,
      "autoStartWorkflow": true,
      "templateMode": false,
      "defaultModel": null
    }
  },
  "availableData": {
    "clients": [
      {
        "id": "client_001",
        "name": "TechCorp Solutions",
        "industry": "Technology",
        "projectCount": 8,
        "permissions": ["read", "write", "create"]
      },
      {
        "id": "client_002",
        "name": "RetailMax Inc",
        "industry": "Retail",
        "projectCount": 12,
        "permissions": ["read", "write"]
      }
    ],
    "availableProjects": [
      {
        "id": "project_045",
        "name": "Product Demand Optimization",
        "clientId": "client_001",
        "description": "Comprehensive demand forecasting and optimization initiative",
        "activityCount": 5,
        "status": "active",
        "permissions": ["read", "write", "create", "execute"]
      },
      {
        "id": "project_046",
        "name": "Inventory Management Enhancement",
        "clientId": "client_001",
        "description": "Strategic inventory management with demand-driven planning",
        "activityCount": 3,
        "status": "active",
        "permissions": ["read", "write", "create"]
      }
    ],
    "industries": [
      { "id": "technology", "name": "Technology", "activityCount": 45 },
      { "id": "retail", "name": "Retail", "activityCount": 67 },
      { "id": "manufacturing", "name": "Manufacturing", "activityCount": 34 },
      { "id": "healthcare", "name": "Healthcare", "activityCount": 23 }
    ],
    "priorities": [
      { "value": "high", "label": "High Priority", "color": "#ef4444" },
      { "value": "medium", "label": "Medium Priority", "color": "#f59e0b" },
      { "value": "low", "label": "Low Priority", "color": "#10b981" }
    ]
  },
  "validationState": {
    "isFormValid": false,
    "fieldErrors": {
      "activityName": "Activity name must be at least 3 characters long"
    },
    "fieldStates": {
      "selectedClient": "valid",
      "selectedProject": "valid",
      "activityName": "invalid",
      "activityDescription": "valid",
      "selectedIndustry": "optional",
      "selectedPriority": "valid"
    },
    "lastValidation": "2024-01-20T20:16:30Z",
    "validationInProgress": false
  },
  "creationState": {
    "isCreating": false,
    "creationStarted": null,
    "creationProgress": 0,
    "creationError": null,
    "lastCreationAttempt": null
  },
  "templateState": {
    "availableTemplates": [
      {
        "id": "template_001",
        "name": "Standard Quarterly Forecast",
        "description": "Standard template for quarterly demand forecasting",
        "configuration": {
          "forecastHorizon": 90,
          "priority": "medium",
          "industry": "general"
        },
        "usageCount": 23,
        "createdBy": "system"
      },
      {
        "id": "template_002",
        "name": "High-Frequency Retail Forecast",
        "description": "Template optimized for high-frequency retail demand patterns",
        "configuration": {
          "forecastHorizon": 30,
          "priority": "high",
          "industry": "retail"
        },
        "usageCount": 12,
        "createdBy": "user_456"
      }
    ],
    "selectedTemplate": null,
    "templatePreview": null
  },
  "systemOptions": {
    "defaultForecastHorizon": 90,
    "maxActivityNameLength": 100,
    "maxDescriptionLength": 500,
    "maxTagsCount": 10,
    "allowedTagCharacters": "alphanumeric, spaces, hyphens",
    "requiredFields": ["client", "project", "activityName"],
    "validationRules": {
      "activityName": {
        "minLength": 3,
        "maxLength": 100,
        "pattern": "^[a-zA-Z0-9\\s\\-_]+$"
      }
    }
  }
}
```

## 15. Implementation Notes

### Recommended Libraries
- **react-hook-form**: Advanced form handling with validation
- **zod**: Schema validation for form data
- **react-select**: Enhanced dropdown components with search
- **framer-motion**: Smooth animations for modal and form transitions
- **use-debounce**: Debouncing for validation and API calls
- **react-hotkeys-hook**: Keyboard shortcut handling for dialog

### Complex Implementation Areas
- **Hierarchical Dropdown Logic**: Managing dependent dropdown selections with data loading
- **Real-time Form Validation**: Implementing comprehensive validation with immediate feedback
- **Modal State Management**: Managing complex modal state and focus trapping
- **Template System Integration**: Implementing template selection and application
- **Responsive Form Layout**: Creating forms that work well across all device sizes

### Potential Technical Challenges
- **Focus Management**: Proper focus management within modal dialogs
- **Validation Performance**: Maintaining smooth UX during intensive validation
- **Data Synchronization**: Keeping form data synchronized with external changes
- **Cross-browser Compatibility**: Ensuring consistent form behavior across browsers
- **Mobile Form Experience**: Optimizing form experience for mobile devices

### Performance Optimization Opportunities
- **Form Data Caching**: Cache frequently accessed form data
- **Validation Optimization**: Optimize validation algorithms for real-time feedback
- **Modal Rendering**: Optimize modal rendering and animation performance
- **Background Data Loading**: Load non-critical data in background
- **Memory Management**: Efficient cleanup of form state and validation data

### Testing Considerations
- **Form Validation Testing**: Comprehensive testing of all validation scenarios
- **Modal Interaction Testing**: Test modal opening, closing, and focus management
- **Keyboard Navigation Testing**: Test keyboard accessibility and navigation
- **Cross-browser Testing**: Ensure consistent form behavior across browsers
- **Mobile Testing**: Test form usability on various mobile devices
- **Error Scenario Testing**: Test error handling and recovery scenarios

## 16. UI Pattern Reference

### MODAL DIALOG INTERFACE - Complete Create Activity Dialog Implementation
```tsx
<Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
  <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
    <DialogHeader>
      <DialogTitle className="flex items-center gap-2">
        <Plus className="w-5 h-5 text-primary" />
        Create New Forecasting Activity
      </DialogTitle>
      <DialogDescription>
        Set up a new demand forecasting activity within your selected client and project
      </DialogDescription>
    </DialogHeader>

    <form onSubmit={handleSubmit} className="space-y-6">
      {/* Client & Project Selection */}
      <div className="space-y-4">
        <div className="flex items-center gap-2">
          <Building className="w-5 h-5 text-blue-600" />
          <h3 className="font-medium">Client & Project Information</h3>
          <Badge variant="destructive" className="text-xs">Required</Badge>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField
            control={form.control}
            name="client"
            render={({ field }) => (
              <FormItem>
                <FormLabel>
                  Client <span className="text-red-500">*</span>
                </FormLabel>
                <Select 
                  onValueChange={(value) => {
                    field.onChange(value);
                    handleClientChange(value);
                  }} 
                  value={field.value}
                >
                  <FormControl>
                    <SelectTrigger className={errors.client ? "border-red-500" : ""}>
                      <SelectValue placeholder="Choose your client" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectGroup>
                      <SelectLabel>Available Clients</SelectLabel>
                      {clients.map((client) => (
                        <SelectItem key={client.id} value={client.id}>
                          <div className="flex items-center gap-3 py-1">
                            <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                              <Building className="w-4 h-4 text-blue-600" />
                            </div>
                            <div>
                              <div className="font-medium">{client.name}</div>
                              <div className="text-xs text-muted-foreground">
                                {client.projectCount} projects • {client.industry}
                              </div>
                            </div>
                          </div>
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
          
          <FormField
            control={form.control}
            name="project"
            render={({ field }) => (
              <FormItem>
                <FormLabel>
                  Project <span className="text-red-500">*</span>
                </FormLabel>
                <Select 
                  onValueChange={field.onChange} 
                  value={field.value}
                  disabled={!selectedClient || isLoadingProjects}
                >
                  <FormControl>
                    <SelectTrigger className={errors.project ? "border-red-500" : ""}>
                      <SelectValue placeholder={
                        !selectedClient ? "Select client first" :
                        isLoadingProjects ? "Loading projects..." :
                        "Choose your project"
                      } />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectGroup>
                      <SelectLabel>Available Projects</SelectLabel>
                      {availableProjects.map((project) => (
                        <SelectItem key={project.id} value={project.id}>
                          <div className="flex items-center gap-3 py-1">
                            <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
                              <Folder className="w-4 h-4 text-green-600" />
                            </div>
                            <div>
                              <div className="font-medium">{project.name}</div>
                              <div className="text-xs text-muted-foreground">
                                {project.activityCount} activities • {project.status}
                              </div>
                            </div>
                          </div>
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        
        {/* Selection Confirmation */}
        {selectedClient && selectedProject && (
          <div className="p-4 bg-gradient-to-r from-blue-50 to-green-50 rounded-lg border border-blue-200">
            <div className="flex items-center gap-3">
              <CheckCircle className="w-5 h-5 text-green-600" />
              <div>
                <div className="font-medium text-green-800">Selection Confirmed</div>
                <div className="text-sm text-green-700">
                  {clients.find(c => c.id === selectedClient)?.name} → 
                  {' '}{availableProjects.find(p => p.id === selectedProject)?.name}
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
      
      <Separator />
      
      {/* Activity Details */}
      <div className="space-y-4">
        <div className="flex items-center gap-2">
          <Activity className="w-5 h-5 text-green-600" />
          <h3 className="font-medium">Activity Details</h3>
        </div>
        
        <FormField
          control={form.control}
          name="activityName"
          render={({ field }) => (
            <FormItem>
              <FormLabel>
                Activity Name <span className="text-red-500">*</span>
              </FormLabel>
              <FormControl>
                <Input
                  {...field}
                  placeholder="Enter a descriptive name for your forecasting activity"
                  className={errors.activityName ? "border-red-500" : ""}
                />
              </FormControl>
              <FormDescription>
                Choose a name that clearly identifies the purpose and scope of this forecast
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        
        <FormField
          control={form.control}
          name="activityDescription"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Description</FormLabel>
              <FormControl>
                <Textarea
                  {...field}
                  placeholder="Provide additional details about this forecasting activity (optional)"
                  rows={3}
                  className="resize-none"
                />
              </FormControl>
              <FormDescription>
                Provide context, objectives, and any special requirements for this activity
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField
            control={form.control}
            name="industry"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Industry</FormLabel>
                <Select onValueChange={field.onChange} value={field.value}>
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select industry (optional)" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {industries.map((industry) => (
                      <SelectItem key={industry.id} value={industry.id}>
                        <div className="flex items-center gap-2">
                          <div className="w-2 h-2 bg-purple-500 rounded-full" />
                          {industry.name}
                        </div>
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
          
          <FormField
            control={form.control}
            name="priority"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Priority</FormLabel>
                <Select onValueChange={field.onChange} value={field.value} defaultValue="medium">
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value="high">
                      <div className="flex items-center gap-2">
                        <div className="w-3 h-3 bg-red-500 rounded-full" />
                        High Priority
                      </div>
                    </SelectItem>
                    <SelectItem value="medium">
                      <div className="flex items-center gap-2">
                        <div className="w-3 h-3 bg-yellow-500 rounded-full" />
                        Medium Priority
                      </div>
                    </SelectItem>
                    <SelectItem value="low">
                      <div className="flex items-center gap-2">
                        <div className="w-3 h-3 bg-green-500 rounded-full" />
                        Low Priority
                      </div>
                    </SelectItem>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
      </div>
      
      {/* Advanced Options */}
      <div className="space-y-4">
        <Button
          type="button"
          variant="ghost"
          onClick={() => setShowAdvancedOptions(!showAdvancedOptions)}
          className="flex items-center gap-2 text-sm h-8"
        >
          <ChevronRight 
            className={`w-4 h-4 transition-transform duration-200 ${
              showAdvancedOptions ? 'rotate-90' : ''
            }`} 
          />
          Advanced Options
          {showAdvancedOptions && (
            <Badge variant="outline" className="text-xs ml-2">
              {getAdvancedOptionsCount()} configured
            </Badge>
          )}
        </Button>
        
        {showAdvancedOptions && (
          <div className="space-y-4 pl-6 border-l-2 border-muted animate-in slide-in-from-top-2 duration-200">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="forecastHorizon"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Default Forecast Horizon (days)</FormLabel>
                    <FormControl>
                      <Input
                        {...field}
                        type="number"
                        min="30"
                        max="365"
                        placeholder="90"
                      />
                    </FormControl>
                    <FormDescription>
                      Set the default forecast horizon for this activity
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              
              <div className="space-y-3">
                <FormLabel>Workflow Configuration</FormLabel>
                <div className="space-y-2">
                  <div className="flex items-center space-x-2">
                    <Checkbox
                      id="auto-start"
                      checked={autoStartWorkflow}
                      onCheckedChange={setAutoStartWorkflow}
                    />
                    <label htmlFor="auto-start" className="text-sm">
                      Auto-start workflow after creation
                    </label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Checkbox
                      id="template-mode"
                      checked={templateMode}
                      onCheckedChange={setTemplateMode}
                    />
                    <label htmlFor="template-mode" className="text-sm">
                      Save as template for future use
                    </label>
                  </div>
                </div>
              </div>
            </div>
            
            <FormField
              control={form.control}
              name="tags"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Tags</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      placeholder="Add tags separated by commas (e.g., quarterly, electronics, high-volume)"
                    />
                  </FormControl>
                  <FormDescription>
                    Tags help organize and search activities later
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
        )}
      </div>
    </form>
    
    {/* Dialog Actions */}
    <DialogFooter className="flex items-center justify-between pt-6 border-t">
      <div className="text-sm text-muted-foreground">
        <span className="text-red-500">*</span> Required fields
      </div>
      <div className="flex items-center gap-3">
        <Button 
          type="button" 
          variant="outline" 
          onClick={handleCancel}
          disabled={isCreating}
        >
          Cancel
        </Button>
        <Button 
          type="submit" 
          disabled={!isFormValid || isCreating}
          className="min-w-32"
        >
          {isCreating ? (
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
              Creating...
            </div>
          ) : (
            <div className="flex items-center gap-2">
              <Plus className="w-4 h-4" />
              Create Activity
            </div>
          )}
        </Button>
      </div>
    </DialogFooter>
  </DialogContent>
</Dialog>
```

### HIERARCHICAL FORM INTERFACE - Client-Project Selection Pattern
```tsx
const ClientProjectSelector = ({ onClientChange, onProjectChange, errors, loading }) => {
  return (
    <div className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {/* Client Selection */}
        <div className="space-y-2">
          <label className="text-sm font-medium flex items-center gap-2">
            <Building className="w-4 h-4 text-blue-600" />
            Client <span className="text-red-500">*</span>
          </label>
          <Select 
            value={selectedClient} 
            onValueChange={handleClientChange}
            disabled={loading.clients}
          >
            <SelectTrigger className={`transition-colors ${
              errors.client ? 'border-red-500 bg-red-50' : 
              selectedClient ? 'border-green-500 bg-green-50' : ''
            }`}>
              <SelectValue placeholder={
                loading.clients ? "Loading clients..." : "Choose your client"
              } />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectLabel className="flex items-center gap-2">
                  <Users className="w-3 h-3" />
                  Available Clients
                </SelectLabel>
                {clients.map((client) => (
                  <SelectItem key={client.id} value={client.id}>
                    <div className="flex items-center gap-3 py-2">
                      <div className="w-10 h-10 bg-gradient-to-br from-blue-100 to-blue-200 rounded-lg flex items-center justify-center">
                        <Building className="w-5 h-5 text-blue-600" />
                      </div>
                      <div className="flex-1">
                        <div className="font-medium">{client.name}</div>
                        <div className="text-xs text-muted-foreground flex items-center gap-4">
                          <span>{client.projectCount} projects</span>
                          <span>{client.industry}</span>
                          <Badge variant="outline" className="text-xs">
                            {client.status}
                          </Badge>
                        </div>
                      </div>
                    </div>
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
          {errors.client && (
            <div className="flex items-center gap-2 text-sm text-red-500">
              <AlertCircle className="w-3 h-3" />
              <span>{errors.client}</span>
            </div>
          )}
        </div>

        {/* Project Selection */}
        <div className="space-y-2">
          <label className="text-sm font-medium flex items-center gap-2">
            <Folder className="w-4 h-4 text-green-600" />
            Project <span className="text-red-500">*</span>
          </label>
          <Select 
            value={selectedProject} 
            onValueChange={onProjectChange}
            disabled={!selectedClient || loading.projects}
          >
            <SelectTrigger className={`transition-colors ${
              errors.project ? 'border-red-500 bg-red-50' : 
              selectedProject ? 'border-green-500 bg-green-50' : ''
            }`}>
              <SelectValue placeholder={
                !selectedClient ? "Select client first" :
                loading.projects ? "Loading projects..." :
                "Choose your project"
              } />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectLabel className="flex items-center gap-2">
                  <FolderOpen className="w-3 h-3" />
                  Available Projects
                </SelectLabel>
                {availableProjects.map((project) => (
                  <SelectItem key={project.id} value={project.id}>
                    <div className="flex items-center gap-3 py-2">
                      <div className="w-10 h-10 bg-gradient-to-br from-green-100 to-green-200 rounded-lg flex items-center justify-center">
                        <Folder className="w-5 h-5 text-green-600" />
                      </div>
                      <div className="flex-1">
                        <div className="font-medium">{project.name}</div>
                        <div className="text-xs text-muted-foreground flex items-center gap-4">
                          <span>{project.activityCount} activities</span>
                          <Badge 
                            variant={project.status === 'active' ? 'default' : 'secondary'} 
                            className="text-xs"
                          >
                            {project.status}
                          </Badge>
                        </div>
                      </div>
                    </div>
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
          {errors.project && (
            <div className="flex items-center gap-2 text-sm text-red-500">
              <AlertCircle className="w-3 h-3" />
              <span>{errors.project}</span>
            </div>
          )}
        </div>
      </div>
      
      {/* Hierarchy Visualization */}
      {selectedClient && selectedProject && (
        <div className="mt-4 p-4 bg-gradient-to-r from-blue-50 via-purple-50 to-green-50 rounded-lg border border-purple-200">
          <div className="flex items-center gap-2 text-sm">
            <CheckCircle className="w-4 h-4 text-green-600" />
            <span className="font-medium text-gray-800">Path:</span>
            <div className="flex items-center gap-2">
              <span className="font-medium text-blue-700">
                {clients.find(c => c.id === selectedClient)?.name}
              </span>
              <ChevronRight className="w-3 h-3 text-muted-foreground" />
              <span className="font-medium text-green-700">
                {availableProjects.find(p => p.id === selectedProject)?.name}
              </span>
              <ChevronRight className="w-3 h-3 text-muted-foreground" />
              <span className="text-muted-foreground">New Activity</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
```

### FORM VALIDATION DISPLAY - Real-time Validation Implementation
```tsx
const FormFieldWithValidation = ({ 
  name, 
  label, 
  required = false, 
  type = "text", 
  placeholder, 
  description, 
  validation,
  children 
}) => {
  const [fieldValue, setFieldValue] = useState('');
  const [fieldError, setFieldError] = useState('');
  const [isValidating, setIsValidating] = useState(false);
  const [isValid, setIsValid] = useState(false);
  
  const validateField = useMemo(
    () => debounce(async (value) => {
      if (!value && !required) {
        setFieldError('');
        setIsValid(true);
        return;
      }
      
      setIsValidating(true);
      try {
        await validation(value);
        setFieldError('');
        setIsValid(true);
      } catch (error) {
        setFieldError(error.message);
        setIsValid(false);
      } finally {
        setIsValidating(false);
      }
    }, 300),
    [validation, required]
  );
  
  useEffect(() => {
    validateField(fieldValue);
  }, [fieldValue, validateField]);
  
  return (
    <div className="space-y-2">
      <label className="text-sm font-medium flex items-center gap-2">
        {label}
        {required && <span className="text-red-500">*</span>}
        {isValidating && (
          <div className="w-3 h-3 border border-primary border-t-transparent rounded-full animate-spin" />
        )}
        {!isValidating && fieldValue && isValid && (
          <CheckCircle className="w-3 h-3 text-green-500" />
        )}
      </label>
      
      <div className="relative">
        {children ? (
          React.cloneElement(children, {
            value: fieldValue,
            onChange: (e) => setFieldValue(e.target.value),
            className: `${children.props.className || ''} transition-colors ${
              fieldError ? 'border-red-500 bg-red-50' : 
              isValid && fieldValue ? 'border-green-500 bg-green-50' : ''
            }`
          })
        ) : (
          <Input
            type={type}
            value={fieldValue}
            onChange={(e) => setFieldValue(e.target.value)}
            placeholder={placeholder}
            className={`transition-colors ${
              fieldError ? 'border-red-500 bg-red-50' : 
              isValid && fieldValue ? 'border-green-500 bg-green-50' : ''
            }`}
          />
        )}
      </div>
      
      {description && (
        <p className="text-xs text-muted-foreground">{description}</p>
      )}
      
      {fieldError && (
        <div className="flex items-center gap-2 text-sm text-red-500">
          <AlertCircle className="w-3 h-3" />
          <span>{fieldError}</span>
        </div>
      )}
    </div>
  );
};
```

## 17. Documentation Quality Checklist

### ✅ UI Pattern Specification:
- [x] Dialog interface labeled as "MODAL DIALOG INTERFACE" with structured layout
- [x] Form specified as "HIERARCHICAL FORM INTERFACE" with Client → Project → Activity flow
- [x] Modal layout includes exact responsive behavior and sizing constraints
- [x] Form validation specified with real-time feedback and visual states
- [x] Advanced options specified with progressive disclosure pattern

### ✅ Implementation Code:
- [x] Complete TSX code examples provided for all dialog components
- [x] shadcn/ui component structure specified (Dialog, Form, Select, Input, etc.)
- [x] Form validation with react-hook-form integration
- [x] Hierarchical dropdown logic with dependent selections
- [x] Modal management with focus trapping and proper accessibility

### ✅ Visual Elements:
- [x] Modal overlay with backdrop and centered positioning
- [x] Form sections with clear visual hierarchy and section headers
- [x] Validation states with color-coded borders and error messages
- [x] Selection confirmation with gradient background and checkmark
- [x] Icon specifications throughout (Building, Folder, Activity, Plus, etc.)

### ✅ Layout Structure:
- [x] Exact modal layout specifications with responsive sizing
- [x] Form grid layout with responsive breakpoints (1-2 column layout)
- [x] Section organization with proper spacing and separators
- [x] Advanced options with collapsible section and indentation
- [x] Dialog footer with action buttons and form status

### ✅ Interaction Patterns:
- [x] Hierarchical dropdown selection with cascading updates
- [x] Real-time form validation with immediate feedback
- [x] Advanced options toggle with smooth expand/collapse
- [x] Form submission with loading states and error handling
- [x] Modal dialog management with focus trapping and keyboard support

### ❌ Rejected Generic Terms:
- [x] No usage of "create dialog" - used "MODAL DIALOG INTERFACE" with specific structure
- [x] No vague form descriptions - specific "HIERARCHICAL FORM INTERFACE" with exact flow
- [x] All layout descriptions include exact CSS classes and responsive behavior
- [x] Implementation code provided for all complex form patterns
- [x] Modal dialog specified with exact positioning, sizing, and interaction behavior

**Documentation eliminates all ambiguity and provides exact implementation guidance for Create Activity Dialog with comprehensive form handling, hierarchical selection, real-time validation, and robust modal management.**