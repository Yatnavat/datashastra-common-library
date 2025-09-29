# Version History Dialog - Screen Documentation

## 1. Screen Overview

- **Primary purpose and user goals**: View, compare, and restore previous versions of forecasting activities and model configurations with comprehensive change tracking
- **User role/permissions required**: Authenticated users with version history access (Data Analyst, Project Manager with history view rights)
- **Entry points**: Activity Dashboard version history button, Activity Workflow "View History" action, keyboard shortcut (Ctrl+H)
- **Screen priority**: Secondary feature - important for audit trail, version management, and model governance

## 2. Visual Layout

### VERSION HISTORY MODAL INTERFACE Layout Structure
- **Layout structure**: Large modal dialog with timeline-based version display
- **Modal Container**: `<Dialog>` component with wide layout for version comparison
- **Content Organization**:
```tsx
<Dialog open={isVersionHistoryOpen} onOpenChange={setIsVersionHistoryOpen}>
  <DialogOverlay className="bg-black/60" />
  <DialogContent className="max-w-6xl max-h-[90vh] overflow-hidden">
    <DialogHeader>
      <DialogTitle>Version History - {activityName}</DialogTitle>
      <DialogDescription>
        View and manage activity versions and forecast configurations
      </DialogDescription>
    </DialogHeader>
    <div className="flex h-[calc(90vh-120px)]">
      <aside className="w-80 border-r bg-muted/20" />
      <main className="flex-1 flex flex-col" />
    </div>
  </DialogContent>
</Dialog>
```

### Responsive Breakpoints
```css
/* Modal Responsive Behavior */
Base (< 768px):     Full-screen modal, single column layout, simplified version list
Tablet (768px+):    Large modal (90% width), side-by-side version comparison
Desktop (1024px+):  Extra-wide modal (max-w-6xl), full timeline and comparison features
```

### VERSION TIMELINE LAYOUT with Dual-Pane Interface
- **Layout Structure**: DUAL-PANE LAYOUT with version list and detail view
- **Layout Pattern**: `<div className="flex h-full">` with sidebar and main content
- **Version Organization**: Chronological timeline with visual change indicators
- **Implementation**:
```tsx
<div className="flex h-full">
  {/* Version Timeline Sidebar */}
  <aside className="w-80 border-r bg-muted/20 overflow-y-auto">
    <div className="p-4 border-b">
      <div className="flex items-center justify-between">
        <h3 className="font-medium">Version Timeline</h3>
        <Badge variant="outline" className="text-xs">
          {versions.length} versions
        </Badge>
      </div>
      <div className="mt-2 flex items-center gap-2">
        <Input
          placeholder="Search versions..."
          className="text-sm h-8"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <Button variant="outline" size="sm" onClick={handleFilterToggle}>
          <Filter className="w-3 h-3" />
        </Button>
      </div>
    </div>
    
    <div className="relative">
      {/* Timeline Line */}
      <div className="absolute left-8 top-0 bottom-0 w-0.5 bg-border" />
      
      <div className="space-y-1 p-4">
        {filteredVersions.map((version, index) => (
          <div
            key={version.id}
            className={`relative cursor-pointer p-3 rounded-lg transition-all duration-200 ${
              selectedVersion?.id === version.id 
                ? 'bg-primary/10 border border-primary/20' 
                : 'hover:bg-muted/50'
            }`}
            onClick={() => handleVersionSelect(version)}
          >
            {/* Timeline Dot */}
            <div className={`absolute left-[-20px] w-4 h-4 rounded-full border-2 bg-background ${
              index === 0 
                ? 'border-green-500' 
                : version.type === 'major' 
                  ? 'border-blue-500' 
                  : 'border-muted-foreground'
            }`}>
              {index === 0 && (
                <div className="w-2 h-2 bg-green-500 rounded-full m-0.5" />
              )}
            </div>
            
            <div className="ml-2">
              <div className="flex items-center justify-between mb-1">
                <div className="flex items-center gap-2">
                  <Badge 
                    variant={version.type === 'major' ? 'default' : 'secondary'}
                    className="text-xs"
                  >
                    v{version.number}
                  </Badge>
                  {index === 0 && (
                    <Badge variant="outline" className="text-xs">Current</Badge>
                  )}
                </div>
                <span className="text-xs text-muted-foreground">
                  {formatDistanceToNow(new Date(version.createdAt), { addSuffix: true })}
                </span>
              </div>
              
              <div className="text-sm font-medium mb-1">{version.title}</div>
              
              <div className="text-xs text-muted-foreground mb-2">
                {version.description || 'No description provided'}
              </div>
              
              <div className="flex items-center gap-2 text-xs">
                <div className="flex items-center gap-1">
                  <User className="w-3 h-3" />
                  {version.createdBy}
                </div>
                <Separator orientation="vertical" className="h-3" />
                <div className="flex items-center gap-1">
                  <Clock className="w-3 h-3" />
                  {format(new Date(version.createdAt), 'MMM d, HH:mm')}
                </div>
              </div>
              
              {version.changes && version.changes.length > 0 && (
                <div className="mt-2 flex flex-wrap gap-1">
                  {version.changes.slice(0, 3).map((change, idx) => (
                    <Badge key={idx} variant="outline" className="text-xs">
                      {change.type}
                    </Badge>
                  ))}
                  {version.changes.length > 3 && (
                    <Badge variant="outline" className="text-xs">
                      +{version.changes.length - 3} more
                    </Badge>
                  )}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  </aside>
  
  {/* Version Detail View */}
  <main className="flex-1 flex flex-col overflow-hidden">
    {selectedVersion ? (
      <VersionDetailView version={selectedVersion} />
    ) : (
      <div className="flex-1 flex items-center justify-center text-muted-foreground">
        <div className="text-center">
          <History className="w-12 h-12 mx-auto mb-3 text-muted-foreground/50" />
          <p>Select a version to view details</p>
        </div>
      </div>
    )}
  </main>
</div>
```

### Typography System (14px base - NO OVERRIDES)
- **Dialog Title**: 20px (h1 default) + font-medium "Version History - [Activity Name]"
- **Section Headers**: 18px (h2 default) + font-medium for major sections
- **Version Titles**: 16px (h3 default) + font-medium for version names
- **Change Descriptions**: 14px (p default) + font-normal for change details
- **Metadata Labels**: 14px (label default) + font-medium for timestamps and users

### Color Scheme
- **Current Version**: `border-green-500` with green dot indicator
- **Major Versions**: `border-blue-500` for significant changes
- **Minor Versions**: `border-muted-foreground` for incremental changes
- **Selected Version**: `bg-primary/10 border border-primary/20` for selection state
- **Change Types**: Color-coded badges for different change categories

## 3. Components Inventory

### Dialog Header - VERSION HISTORY HEADER
- **Dialog Title**: "Version History - [Activity Name]"
- **Dialog Description**: Context about version management and operations
- **Action Buttons**: Close dialog, export history, create new version
- **Activity Context**: Display current activity name and project information

### Version Timeline Sidebar - CHRONOLOGICAL VERSION LIST
- **Version Search**: Search versions by title, description, or change type
- **Filter Controls**: Filter by version type, date range, or change category
- **Timeline Visualization**: Visual timeline with connection lines and status dots
- **Version Cards**: Compact cards with essential version information
- **Implementation**:
```tsx
<div className="space-y-1">
  {filteredVersions.map((version, index) => (
    <div
      key={version.id}
      className={`relative cursor-pointer p-3 rounded-lg transition-all duration-200 ${
        selectedVersion?.id === version.id 
          ? 'bg-primary/10 border border-primary/20 shadow-sm' 
          : 'hover:bg-muted/50'
      }`}
      onClick={() => handleVersionSelect(version)}
    >
      {/* Timeline Connection */}
      <div className={`absolute left-[-20px] w-4 h-4 rounded-full border-2 bg-background ${
        index === 0 
          ? 'border-green-500' 
          : version.type === 'major' 
            ? 'border-blue-500' 
            : version.type === 'minor'
              ? 'border-yellow-500'
              : 'border-muted-foreground'
      }`}>
        {index === 0 && (
          <div className="w-2 h-2 bg-green-500 rounded-full m-0.5" />
        )}
        {version.type === 'major' && index > 0 && (
          <div className="w-2 h-2 bg-blue-500 rounded-full m-0.5" />
        )}
      </div>
      
      <div className="ml-2">
        {/* Version Header */}
        <div className="flex items-center justify-between mb-2">
          <div className="flex items-center gap-2">
            <Badge 
              variant={
                version.type === 'major' ? 'default' : 
                version.type === 'minor' ? 'secondary' : 'outline'
              }
              className="text-xs"
            >
              v{version.number}
            </Badge>
            {index === 0 && (
              <Badge className="bg-green-100 text-green-800 text-xs">
                Current
              </Badge>
            )}
            {version.isAutoSaved && (
              <Badge variant="outline" className="text-xs">
                Auto-saved
              </Badge>
            )}
          </div>
          <span className="text-xs text-muted-foreground">
            {formatDistanceToNow(new Date(version.createdAt), { addSuffix: true })}
          </span>
        </div>
        
        {/* Version Details */}
        <div className="space-y-1">
          <div className="font-medium text-sm">{version.title}</div>
          {version.description && (
            <div className="text-xs text-muted-foreground line-clamp-2">
              {version.description}
            </div>
          )}
        </div>
        
        {/* Change Summary */}
        {version.changes && version.changes.length > 0 && (
          <div className="mt-2">
            <div className="flex flex-wrap gap-1">
              {version.changes.slice(0, 3).map((change, idx) => (
                <Badge 
                  key={idx} 
                  variant="outline" 
                  className={`text-xs ${getChangeTypeColor(change.type)}`}
                >
                  {change.type}
                </Badge>
              ))}
              {version.changes.length > 3 && (
                <Badge variant="outline" className="text-xs">
                  +{version.changes.length - 3}
                </Badge>
              )}
            </div>
          </div>
        )}
        
        {/* Metadata */}
        <div className="flex items-center gap-2 mt-2 text-xs text-muted-foreground">
          <div className="flex items-center gap-1">
            <User className="w-3 h-3" />
            {version.createdBy}
          </div>
          <Separator orientation="vertical" className="h-3" />
          <div className="flex items-center gap-1">
            <FileText className="w-3 h-3" />
            {version.size} KB
          </div>
          {version.accuracy && (
            <>
              <Separator orientation="vertical" className="h-3" />
              <div className="flex items-center gap-1">
                <Target className="w-3 h-3" />
                {version.accuracy}% acc
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  ))}
</div>
```

### Version Detail View - COMPREHENSIVE VERSION DISPLAY
- **Version Header**: Detailed version information with metadata
- **Change Summary**: Comprehensive list of changes in this version
- **Configuration Diff**: Side-by-side comparison of configuration changes
- **Performance Metrics**: Model performance comparison with previous versions
- **Actions Panel**: Version-specific actions (restore, compare, export)
- **Implementation**:
```tsx
const VersionDetailView = ({ version }) => {
  const [activeTab, setActiveTab] = useState('overview');
  
  return (
    <div className="flex-1 flex flex-col">
      {/* Version Header */}
      <div className="p-6 border-b bg-muted/20">
        <div className="flex items-start justify-between">
          <div>
            <div className="flex items-center gap-3 mb-2">
              <Badge 
                variant={version.type === 'major' ? 'default' : 'secondary'}
                className="text-sm"
              >
                Version {version.number}
              </Badge>
              {version.id === currentVersion?.id && (
                <Badge className="bg-green-100 text-green-800">Current</Badge>
              )}
              <span className="text-sm text-muted-foreground">
                {format(new Date(version.createdAt), 'MMMM d, yyyy at h:mm a')}
              </span>
            </div>
            <h2 className="text-xl font-medium mb-1">{version.title}</h2>
            {version.description && (
              <p className="text-muted-foreground">{version.description}</p>
            )}
          </div>
          
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => handleCompareVersion(version)}
            >
              <GitCompare className="w-3 h-3 mr-1" />
              Compare
            </Button>
            {version.id !== currentVersion?.id && (
              <Button
                variant="outline"
                size="sm"
                onClick={() => handleRestoreVersion(version)}
              >
                <RotateCcw className="w-3 h-3 mr-1" />
                Restore
              </Button>
            )}
            <Button
              variant="outline"
              size="sm"
              onClick={() => handleExportVersion(version)}
            >
              <Download className="w-3 h-3 mr-1" />
              Export
            </Button>
          </div>
        </div>
        
        {/* Version Metrics */}
        <div className="grid grid-cols-4 gap-4 mt-4">
          <div className="text-center p-3 bg-background rounded-lg border">
            <div className="text-lg font-medium">{version.changes?.length || 0}</div>
            <div className="text-xs text-muted-foreground">Changes</div>
          </div>
          <div className="text-center p-3 bg-background rounded-lg border">
            <div className="text-lg font-medium">{version.accuracy || 'N/A'}</div>
            <div className="text-xs text-muted-foreground">Accuracy</div>
          </div>
          <div className="text-center p-3 bg-background rounded-lg border">
            <div className="text-lg font-medium">{version.size} KB</div>
            <div className="text-xs text-muted-foreground">Size</div>
          </div>
          <div className="text-center p-3 bg-background rounded-lg border">
            <div className="text-lg font-medium">{version.createdBy}</div>
            <div className="text-xs text-muted-foreground">Created By</div>
          </div>
        </div>
      </div>
      
      {/* Version Content Tabs */}
      <Tabs value={activeTab} onValueChange={setActiveTab} className="flex-1 flex flex-col">
        <TabsList className="mx-6 mt-4 grid w-fit grid-cols-4">
          <TabsTrigger value="overview">Overview</TabsTrigger>
          <TabsTrigger value="changes">Changes</TabsTrigger>
          <TabsTrigger value="configuration">Configuration</TabsTrigger>
          <TabsTrigger value="performance">Performance</TabsTrigger>
        </TabsList>
        
        <div className="flex-1 overflow-y-auto">
          <TabsContent value="overview" className="p-6 space-y-6">
            <VersionOverviewContent version={version} />
          </TabsContent>
          
          <TabsContent value="changes" className="p-6">
            <VersionChangesContent version={version} />
          </TabsContent>
          
          <TabsContent value="configuration" className="p-6">
            <VersionConfigurationContent version={version} />
          </TabsContent>
          
          <TabsContent value="performance" className="p-6">
            <VersionPerformanceContent version={version} />
          </TabsContent>
        </div>
      </Tabs>
    </div>
  );
};
```

### Version Comparison Modal - SIDE-BY-SIDE COMPARISON INTERFACE
- **Version Selection**: Choose two versions for detailed comparison
- **Configuration Diff**: Highlight differences in model configuration
- **Performance Comparison**: Side-by-side performance metrics
- **Change Visualization**: Visual representation of changes between versions
- **Implementation**:
```tsx
<Dialog open={isComparing} onOpenChange={setIsComparing}>
  <DialogContent className="max-w-7xl max-h-[95vh]">
    <DialogHeader>
      <DialogTitle>Compare Versions</DialogTitle>
      <DialogDescription>
        Side-by-side comparison of configuration and performance
      </DialogDescription>
    </DialogHeader>
    
    <div className="flex h-[calc(95vh-120px)]">
      {/* Version A */}
      <div className="flex-1 border-r">
        <div className="p-4 border-b bg-blue-50">
          <div className="flex items-center gap-2">
            <Badge className="bg-blue-100 text-blue-800">
              Version {comparisonVersions.versionA.number}
            </Badge>
            <span className="font-medium">{comparisonVersions.versionA.title}</span>
          </div>
        </div>
        <div className="p-4 overflow-y-auto">
          <VersionComparisonContent version={comparisonVersions.versionA} />
        </div>
      </div>
      
      {/* Version B */}
      <div className="flex-1">
        <div className="p-4 border-b bg-green-50">
          <div className="flex items-center gap-2">
            <Badge className="bg-green-100 text-green-800">
              Version {comparisonVersions.versionB.number}
            </Badge>
            <span className="font-medium">{comparisonVersions.versionB.title}</span>
          </div>
        </div>
        <div className="p-4 overflow-y-auto">
          <VersionComparisonContent version={comparisonVersions.versionB} />
        </div>
      </div>
    </div>
  </DialogContent>
</Dialog>
```

### Version Actions Panel - ACTION CONTROLS
- **Restore Version**: Restore selected version as current with confirmation
- **Compare Versions**: Launch comparison modal for detailed diff
- **Export Version**: Export version configuration and data
- **Version Notes**: Add or edit notes for selected version
- **Delete Version**: Remove version with appropriate warnings

## 4. Data Display Elements

### Version Metadata
- **Version Number**: String, semantic version (1.0.0, 1.1.0, 2.0.0)
- **Version Title**: String, user-provided descriptive title
- **Version Description**: String, detailed description of changes
- **Created Date**: ISO string, version creation timestamp
- **Created By**: String, user who created the version
- **Version Type**: Enum, values: ['major', 'minor', 'patch', 'auto']

### Change Tracking Data
- **Change List**: Array of change objects with type, description, and impact
- **Configuration Diff**: Object with before/after configuration states
- **Parameter Changes**: Object with modified parameters and values
- **Model Changes**: Object with model selection and parameter changes
- **Data Changes**: Object with dataset modifications and feature changes

### Performance Metrics
- **Model Accuracy**: Number, model accuracy percentage for this version
- **Training Time**: Number, time taken to train model in seconds
- **Validation Metrics**: Object with various validation metrics (MAE, RMSE, etc.)
- **Resource Usage**: Object with CPU, memory, and storage usage
- **Forecast Quality**: Object with forecast quality indicators

### Version Comparison Data
- **Configuration Differences**: Object highlighting differences between versions
- **Performance Comparison**: Object with side-by-side performance metrics
- **Change Impact**: Object with estimated impact of changes
- **Regression Analysis**: Object indicating potential regressions
- **Improvement Summary**: Object summarizing improvements between versions

### Timeline and History Data
- **Version Timeline**: Array of versions in chronological order
- **Branch Information**: Object with version branching and merging data
- **Rollback History**: Array of version rollback operations
- **Auto-save Versions**: Array of automatically saved version snapshots
- **Manual Versions**: Array of manually created version checkpoints

### Loading States
- **Version List Loading**: Loading state while fetching version history
- **Version Detail Loading**: Loading state while fetching version details
- **Comparison Loading**: Loading state during version comparison
- **Restore Loading**: Loading state during version restoration

### Error States
- **Version Load Error**: Handle failures in loading version data
- **Comparison Error**: Handle failures in version comparison
- **Restore Error**: Handle failures in version restoration
- **Permission Error**: Handle insufficient permissions for version operations

## 5. Interactive Features

### Version Selection and Navigation - TIMELINE INTERACTION
- **Version Click Selection**: Click version cards to view details
- **Keyboard Navigation**: Arrow keys to navigate through version timeline
- **Search and Filter**: Search versions by content and filter by criteria
- **Timeline Zoom**: Zoom in/out of timeline for different time periods
- **Implementation**:
```tsx
const handleVersionSelect = (version) => {
  setSelectedVersion(version);
  
  // Update URL for deep linking
  updateURL({ versionId: version.id });
  
  // Track version selection for analytics
  trackVersionView(version.id);
  
  // Load detailed version data if not already loaded
  if (!version.detailsLoaded) {
    loadVersionDetails(version.id);
  }
};

const handleKeyboardNavigation = (event) => {
  if (!selectedVersion) return;
  
  const currentIndex = filteredVersions.findIndex(v => v.id === selectedVersion.id);
  
  switch (event.key) {
    case 'ArrowUp':
      if (currentIndex > 0) {
        handleVersionSelect(filteredVersions[currentIndex - 1]);
      }
      break;
    case 'ArrowDown':
      if (currentIndex < filteredVersions.length - 1) {
        handleVersionSelect(filteredVersions[currentIndex + 1]);
      }
      break;
    case 'Enter':
      if (selectedVersion) {
        handleRestoreVersion(selectedVersion);
      }
      break;
  }
};
```

### Version Comparison - DIFF INTERFACE
- **Two-Version Selection**: Select two versions for detailed comparison
- **Configuration Diff**: Highlight differences in configuration
- **Performance Diff**: Compare performance metrics side-by-side
- **Visual Diff Display**: Color-coded differences with expand/collapse
- **Implementation**:
```tsx
const handleCompareVersion = (primaryVersion) => {
  setComparisonVersions({
    versionA: primaryVersion,
    versionB: currentVersion
  });
  
  // Load comparison data
  loadVersionComparison(primaryVersion.id, currentVersion.id);
  
  setIsComparing(true);
};

const renderConfigurationDiff = (versionA, versionB) => {
  const diff = calculateConfigDiff(versionA.configuration, versionB.configuration);
  
  return (
    <div className="space-y-4">
      {Object.entries(diff).map(([section, changes]) => (
        <div key={section} className="border rounded-lg">
          <div className="p-3 border-b bg-muted/20">
            <h4 className="font-medium">{section}</h4>
          </div>
          <div className="p-3 space-y-2">
            {changes.map((change, index) => (
              <div key={index} className={`p-2 rounded text-sm ${
                change.type === 'added' ? 'bg-green-50 border border-green-200' :
                change.type === 'removed' ? 'bg-red-50 border border-red-200' :
                change.type === 'modified' ? 'bg-yellow-50 border border-yellow-200' :
                'bg-muted/20'
              }`}>
                <div className="flex items-center gap-2">
                  {change.type === 'added' && <Plus className="w-3 h-3 text-green-600" />}
                  {change.type === 'removed' && <Minus className="w-3 h-3 text-red-600" />}
                  {change.type === 'modified' && <Edit className="w-3 h-3 text-yellow-600" />}
                  <span className="font-medium">{change.field}</span>
                </div>
                <div className="mt-1 text-xs">
                  {change.type === 'modified' ? (
                    <div>
                      <span className="text-red-600">- {change.oldValue}</span>
                      <br />
                      <span className="text-green-600">+ {change.newValue}</span>
                    </div>
                  ) : (
                    <span className={
                      change.type === 'added' ? 'text-green-600' : 'text-red-600'
                    }>
                      {change.value}
                    </span>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};
```

### Version Restoration - RESTORE WORKFLOW
- **Restore Confirmation**: Confirmation dialog with impact summary
- **Backup Current**: Automatic backup of current version before restore
- **Restore Process**: Step-by-step restoration with progress tracking
- **Post-restore Validation**: Validation and testing after restoration
- **Implementation**:
```tsx
const handleRestoreVersion = async (version) => {
  // Show confirmation dialog
  const confirmation = await showRestoreConfirmation({
    version,
    currentVersion,
    impactSummary: calculateRestoreImpact(version, currentVersion)
  });
  
  if (!confirmation.confirmed) return;
  
  setIsRestoring(true);
  
  try {
    // Create backup of current version
    const backup = await createVersionBackup(currentVersion);
    
    // Restore selected version
    const restoredActivity = await restoreVersion(version.id, {
      createBackup: true,
      validateAfterRestore: true,
      preserveData: confirmation.preserveData
    });
    
    // Update UI state
    setCurrentVersion(version);
    setSelectedVersion(version);
    
    toast.success(`Successfully restored to version ${version.number}`);
    
    // Optionally close dialog and navigate to workflow
    if (confirmation.goToWorkflow) {
      setIsVersionHistoryOpen(false);
      navigateToWorkflow(restoredActivity);
    }
    
  } catch (error) {
    toast.error(`Failed to restore version: ${error.message}`);
  } finally {
    setIsRestoring(false);
  }
};
```

### Search and Filter - VERSION DISCOVERY
- **Text Search**: Search version titles, descriptions, and change notes
- **Date Range Filter**: Filter versions by creation date range
- **Change Type Filter**: Filter by types of changes (model, data, config)
- **User Filter**: Filter versions by user who created them
- **Performance Filter**: Filter by performance criteria or improvements

### Export and Sharing - VERSION MANAGEMENT
- **Version Export**: Export version configuration and metadata
- **History Export**: Export complete version history
- **Comparison Export**: Export version comparison results
- **Share Version**: Share specific version with team members
- **Version Documentation**: Generate documentation for specific versions

## 6. Navigation Elements

### Timeline Navigation
- **Chronological Scrolling**: Scroll through versions chronologically
- **Jump to Date**: Quick navigation to specific date ranges
- **Version Bookmarks**: Bookmark important versions for quick access
- **Search Results Navigation**: Navigate through search result versions

### Detail View Navigation
- **Tab Navigation**: Navigate between overview, changes, config, performance
- **Previous/Next Version**: Navigate to adjacent versions in timeline
- **Related Versions**: Navigate to related or branched versions
- **Comparison Navigation**: Navigate between compared versions

### Modal Navigation
- **Dialog Management**: Proper focus management within version history modal
- **Nested Modal Handling**: Handle comparison and restoration modals
- **Keyboard Navigation**: Full keyboard support for navigation
- **Deep Linking**: URL-based navigation to specific versions

### Cross-Component Navigation
- **Back to Activity**: Return to main activity dashboard or workflow
- **View in Workflow**: Open selected version in workflow interface
- **Related Activities**: Navigate to related activities with shared versions
- **Team Collaboration**: Navigate to team sharing and collaboration features

## 7. Dynamic Behaviors

### Real-time Version Updates - LIVE VERSION TRACKING
- **Auto-refresh**: Automatically refresh version list when new versions are created
- **Collaborative Updates**: Show when other users create or modify versions
- **Real-time Notifications**: Notify about version-related activities
- **Conflict Detection**: Detect and handle version conflicts in collaborative environments

### Timeline Animations - SMOOTH VISUAL TRANSITIONS
- **Timeline Transitions**: Smooth animations when navigating timeline
- **Version Selection**: Animated transitions when selecting versions
- **Loading Animations**: Elegant loading states during version operations
- **Diff Animations**: Animated highlighting of changes in comparisons

### Progressive Loading - OPTIMIZED DATA LOADING
- **Lazy Loading**: Load version details only when needed
- **Background Loading**: Pre-load adjacent versions for smooth navigation
- **Incremental Search**: Progressive search results as user types
- **Caching Strategy**: Efficient caching of version data and comparisons

### Responsive Adaptation - DEVICE-SPECIFIC BEHAVIOR
- **Mobile Layout**: Optimized layout for mobile version browsing
- **Touch Interactions**: Touch-friendly version selection and navigation
- **Screen Size Adaptation**: Responsive layout for different screen sizes
- **Performance Optimization**: Optimize for different device capabilities

### State Synchronization - CONSISTENT STATE MANAGEMENT
- **Version State Sync**: Synchronize version state across components
- **Selection Persistence**: Persist selected version across interactions
- **Filter State**: Maintain filter and search state during session
- **Navigation State**: Track and restore navigation state

## 8. State Management

### Version History State - COMPREHENSIVE VERSION TRACKING
- **Available Versions**: `versions` array with all version metadata
- **Selected Version**: `selectedVersion` object with currently selected version
- **Current Version**: `currentVersion` object with active version
- **Filtered Versions**: `filteredVersions` array with search/filter applied
- **Loading States**: `loadingStates` object tracking loading per operation

### Comparison State - VERSION COMPARISON MANAGEMENT
- **Comparison Versions**: `comparisonVersions` object with versions being compared
- **Comparison Data**: `comparisonData` object with diff and analysis results
- **Comparison Mode**: `comparisonMode` enum for comparison type
- **Comparison Loading**: `comparisonLoading` boolean for comparison operations
- **Comparison History**: `comparisonHistory` array of previous comparisons

### Filter and Search State - DISCOVERY STATE MANAGEMENT
- **Search Query**: `searchQuery` string with current search terms
- **Active Filters**: `activeFilters` object with applied filter criteria
- **Filter Options**: `filterOptions` object with available filter choices
- **Search Results**: `searchResults` array with matching versions
- **Search History**: `searchHistory` array of previous search terms

### UI Interaction State - INTERFACE STATE TRACKING
- **Dialog State**: `isVersionHistoryOpen` boolean for modal visibility
- **Tab State**: `activeTab` string for selected detail view tab
- **Selection State**: `selectionState` object with selection context
- **Animation State**: `animationState` object for transition control
- **Error State**: `errorState` object with error information per operation

### Operation State - ACTION STATE MANAGEMENT
- **Restore State**: `restoreState` object with restoration operation status
- **Export State**: `exportState` object with export operation status
- **Backup State**: `backupState` object with backup operation status
- **Validation State**: `validationState` object with validation results
- **Notification State**: `notificationState` object with user notifications

## 9. API Requirements

### Version History Endpoints
- **GET** `/api/activities/:id/versions` - Get version history for activity
  - Query: `{ limit?: number, offset?: number, filter?: object }`
  - Response: `{ versions: Version[], total: number, hasMore: boolean }`

- **GET** `/api/activities/:id/versions/:versionId` - Get detailed version information
  - Response: `{ version: Version, changes: Change[], configuration: object, performance: object }`

- **POST** `/api/activities/:id/versions/:versionId/restore` - Restore specific version
  - Body: `{ createBackup: boolean, validateAfterRestore: boolean }`
  - Response: `{ restored: boolean, backupId: string, validationResults: object }`

### Version Comparison Endpoints
- **POST** `/api/activities/:id/versions/compare` - Compare two versions
  - Body: `{ versionA: string, versionB: string, comparisonType: string }`
  - Response: `{ comparison: object, differences: object[], summary: object }`

- **GET** `/api/activities/:id/versions/:versionId/diff/:targetVersionId` - Get version diff
  - Response: `{ diff: object, changeAnalysis: object, impactAssessment: object }`

### Version Management Endpoints
- **POST** `/api/activities/:id/versions` - Create new version
  - Body: `{ title: string, description: string, type: string, configuration: object }`
  - Response: `{ version: Version, created: boolean }`

- **PUT** `/api/activities/:id/versions/:versionId` - Update version metadata
  - Body: `{ title?: string, description?: string, tags?: string[] }`
  - Response: `{ version: Version, updated: boolean }`

- **DELETE** `/api/activities/:id/versions/:versionId` - Delete version
  - Body: `{ confirmDeletion: boolean, reason?: string }`
  - Response: `{ deleted: boolean, affectedVersions: string[] }`

### Export and Backup Endpoints
- **POST** `/api/activities/:id/versions/:versionId/export` - Export version
  - Body: `{ format: string, includeData: boolean, includeMetadata: boolean }`
  - Response: File download or `{ downloadUrl: string, expiresAt: string }`

- **POST** `/api/activities/:id/versions/:versionId/backup` - Create version backup
  - Body: `{ backupType: string, retention: number }`
  - Response: `{ backup: object, backupId: string }`

### WebSocket Events
- **Version Created**: Real-time notification when new versions are created
- **Version Restored**: Notification when versions are restored by other users
- **Collaborative Changes**: Live updates when multiple users work on versions
- **Performance Updates**: Real-time performance metric updates

## 10. Business Logic

### Version Creation Logic - AUTOMATIC VERSIONING SYSTEM
- **Auto-versioning**: Automatically create versions at key workflow milestones
- **Manual Versioning**: Allow users to create versions with custom metadata
- **Version Numbering**: Implement semantic versioning (major.minor.patch)
- **Change Detection**: Automatically detect and categorize changes
- **Version Validation**: Validate version integrity and compatibility

### Version Comparison Logic - INTELLIGENT DIFF ANALYSIS
- **Configuration Diff**: Deep comparison of configuration objects
- **Performance Analysis**: Compare performance metrics and trends
- **Change Impact Assessment**: Analyze potential impact of changes
- **Regression Detection**: Identify potential performance regressions
- **Improvement Highlighting**: Highlight improvements and optimizations

### Restoration Logic - SAFE VERSION RESTORATION
- **Pre-restoration Validation**: Validate version compatibility before restore
- **Automatic Backup**: Create backup of current state before restoration
- **Incremental Restoration**: Support partial restoration of specific components
- **Post-restoration Validation**: Validate system state after restoration
- **Rollback Capability**: Ability to rollback failed restorations

### Change Tracking Logic - COMPREHENSIVE AUDIT TRAIL
- **Change Detection**: Automatically detect changes at all system levels
- **Change Categorization**: Classify changes by type and impact
- **Audit Trail**: Maintain comprehensive audit trail of all changes
- **Change Attribution**: Track who made changes and when
- **Change Approval**: Support for change approval workflows

### Version Lifecycle Management - VERSION GOVERNANCE
- **Retention Policies**: Implement version retention and cleanup policies
- **Archive Management**: Archive old versions while maintaining access
- **Permission Management**: Control who can create, restore, and delete versions
- **Version Branching**: Support branching and merging of configuration changes
- **Compliance Tracking**: Track compliance with governance requirements

## 11. Accessibility Requirements

### ARIA Labels and Roles
- **Version Timeline**: `role="timeline" aria-label="Activity version history"`
- **Version Cards**: `role="listitem" aria-label="Version details"`
- **Comparison Interface**: `role="region" aria-label="Version comparison"`
- **Action Buttons**: Appropriate labels and descriptions for all actions
- **Status Indicators**: `aria-live` regions for status updates

### Keyboard Navigation Flow
1. **Tab Order**: Search → Filter → Version list → Detail view → Actions
2. **Version Navigation**: Arrow keys for timeline navigation, Enter to select
3. **Detail Navigation**: Tab through detail tabs, Enter to activate
4. **Comparison Navigation**: Tab through comparison sections
5. **Action Navigation**: Tab to action buttons, Enter to execute

### Screen Reader Considerations
- **Version Descriptions**: Comprehensive descriptions of version content
- **Change Announcements**: Announce changes and updates as they occur
- **Navigation Feedback**: Clear feedback for navigation and selection
- **Comparison Results**: Detailed announcements of comparison results
- **Operation Status**: Announce operation progress and completion

### Focus Management
- **Modal Focus**: Trap focus within version history modal
- **Selection Focus**: Maintain focus on selected version
- **Comparison Focus**: Proper focus management in comparison interface
- **Operation Focus**: Focus management during version operations
- **Error Focus**: Move focus to error messages and recovery options

### Visual Accessibility
- **High Contrast Versions**: Clear visual distinction for version states
- **Color Independence**: Use patterns and text in addition to colors
- **Focus Indicators**: High-contrast focus indicators for all elements
- **Text Scaling**: Support text scaling up to 200% without functionality loss
- **Status Visibility**: Clear visual indication of operation status and errors

## 12. Performance Considerations

### Version Data Loading - OPTIMIZED DATA RETRIEVAL
- **Lazy Loading**: Load version details only when selected
- **Virtual Scrolling**: Use virtual scrolling for large version lists
- **Progressive Loading**: Load versions progressively as user scrolls
- **Background Loading**: Pre-load adjacent versions for smooth navigation
- **Caching Strategy**: Efficient caching of version data and metadata

### Comparison Performance - EFFICIENT DIFF COMPUTATION
- **Incremental Diff**: Compute diffs incrementally for better performance
- **Diff Caching**: Cache comparison results for frequently compared versions
- **Parallel Processing**: Use parallel processing for complex comparisons
- **Optimized Algorithms**: Use efficient algorithms for configuration diff
- **Memory Management**: Efficient memory usage during large comparisons

### Timeline Rendering - SMOOTH TIMELINE DISPLAY
- **Virtualized Timeline**: Use virtualization for large version timelines
- **Optimized Rendering**: Optimize React rendering for version cards
- **Animation Performance**: Smooth animations without performance impact
- **Scroll Optimization**: Optimize scroll performance for long timelines
- **Memory Cleanup**: Proper cleanup of timeline components

### Modal Performance - EFFICIENT MODAL MANAGEMENT
- **Modal Optimization**: Optimize modal rendering and state management
- **Content Streaming**: Stream large version content for better performance
- **Background Operations**: Run operations in background without blocking UI
- **Memory Management**: Efficient memory usage for modal content
- **Resource Cleanup**: Proper cleanup when modal is closed

### Mobile Performance - DEVICE-SPECIFIC OPTIMIZATION
- **Simplified Interface**: Reduce complexity on mobile devices
- **Touch Optimization**: Optimize touch interactions for mobile
- **Reduced Animations**: Minimize animations on lower-powered devices
- **Bandwidth Optimization**: Minimize data usage for mobile users
- **Battery Optimization**: Optimize for battery usage on mobile devices

## 13. Edge Cases

### Version Management Edge Cases - ROBUST VERSION HANDLING
- **Empty Version History**: Handle activities with no version history
- **Corrupted Versions**: Handle corrupted or incomplete version data
- **Missing Versions**: Handle references to deleted or missing versions
- **Version Conflicts**: Handle conflicts when multiple users create versions
- **Large Version Lists**: Handle activities with very large version histories

### Restoration Edge Cases - COMPLEX RESTORATION SCENARIOS
- **Failed Restorations**: Handle restoration failures with proper recovery
- **Partial Restorations**: Handle scenarios where only parts of version can be restored
- **Compatibility Issues**: Handle restoration of incompatible versions
- **Data Conflicts**: Handle data conflicts during restoration
- **Permission Changes**: Handle cases where user loses permissions during restoration

### Comparison Edge Cases - COMPLEX COMPARISON SCENARIOS
- **Version Unavailability**: Handle comparisons when one version is unavailable
- **Large Configuration Diff**: Handle comparisons of very large configurations
- **Circular References**: Handle circular references in configuration objects
- **Performance Timeouts**: Handle timeouts during complex comparisons
- **Memory Limitations**: Handle memory limitations during large comparisons

### User Experience Edge Cases - INTERFACE ROBUSTNESS
- **Rapid Version Changes**: Handle rapid version selection changes
- **Network Interruptions**: Handle network failures during version operations
- **Browser Limitations**: Handle browser-specific limitations and quirks
- **Session Expiration**: Handle session timeouts during version history browsing
- **Concurrent Access**: Handle multiple users accessing same version history

### Data Integrity Edge Cases - SYSTEM RELIABILITY
- **Version Inconsistency**: Handle inconsistencies in version data
- **Metadata Corruption**: Handle corrupted version metadata
- **Reference Integrity**: Handle broken references between versions
- **Backup Failures**: Handle failures in backup creation during restoration
- **Audit Trail Gaps**: Handle gaps or inconsistencies in audit trail

## 14. Sample Data Structure

```json
{
  "versionHistory": {
    "activityId": "activity_123",
    "activityName": "Q4 Electronics Demand Forecast",
    "totalVersions": 12,
    "currentVersion": "v1.3.2",
    "lastModified": "2024-01-20T16:45:00Z"
  },
  "versions": [
    {
      "id": "version_456",
      "number": "1.3.2",
      "title": "Improved Feature Engineering",
      "description": "Enhanced lag features and rolling statistics for better accuracy",
      "type": "minor",
      "createdAt": "2024-01-20T16:45:00Z",
      "createdBy": "Sarah Johnson",
      "isAutoSaved": false,
      "isCurrent": true,
      "size": 245,
      "accuracy": 94.2,
      "changes": [
        {
          "type": "feature_engineering",
          "description": "Added 7-day and 30-day rolling averages",
          "impact": "medium",
          "category": "improvement"
        },
        {
          "type": "model_parameters",
          "description": "Adjusted seasonality parameters",
          "impact": "low",
          "category": "optimization"
        }
      ],
      "configuration": {
        "model": {
          "type": "prophet",
          "parameters": {
            "seasonality_mode": "multiplicative",
            "changepoint_prior_scale": 0.05,
            "seasonality_prior_scale": 10
          }
        },
        "features": {
          "temporal": {
            "seasonality": true,
            "holidays": true
          },
          "lag": {
            "periods": [1, 7, 14, 30],
            "statistics": ["mean", "std"]
          },
          "rolling": {
            "windows": [7, 30],
            "statistics": ["mean", "std"]
          }
        },
        "aggregation": {
          "geographical": "depot",
          "product": "sku",
          "temporal": "daily"
        }
      },
      "performance": {
        "accuracy": 94.2,
        "mae": 23.4,
        "rmse": 31.7,
        "mape": 12.8,
        "trainingTime": 180,
        "memoryUsage": "512MB"
      },
      "metadata": {
        "tags": ["feature-improvement", "accuracy-boost"],
        "notes": "Significant improvement in forecast accuracy",
        "validationStatus": "passed",
        "approvalStatus": "approved"
      }
    },
    {
      "id": "version_455",
      "number": "1.3.1",
      "title": "Holiday Calendar Update",
      "description": "Updated holiday calendar for 2024",
      "type": "patch",
      "createdAt": "2024-01-18T14:30:00Z",
      "createdBy": "Mike Chen",
      "isAutoSaved": false,
      "isCurrent": false,
      "size": 238,
      "accuracy": 93.1,
      "changes": [
        {
          "type": "external_data",
          "description": "Updated holiday calendar",
          "impact": "low",
          "category": "maintenance"
        }
      ]
    },
    {
      "id": "version_454",
      "number": "1.3.0",
      "title": "Model Selection Optimization",
      "description": "Switched from ARIMA to Prophet for better seasonal handling",
      "type": "major",
      "createdAt": "2024-01-15T10:15:00Z",
      "createdBy": "Sarah Johnson",
      "isAutoSaved": false,
      "isCurrent": false,
      "size": 235,
      "accuracy": 92.8,
      "changes": [
        {
          "type": "model_selection",
          "description": "Changed from ARIMA to Prophet",
          "impact": "high",
          "category": "improvement"
        },
        {
          "type": "feature_engineering",
          "description": "Removed ARIMA-specific features",
          "impact": "medium",
          "category": "cleanup"
        }
      ]
    }
  ],
  "comparisonData": {
    "versionA": "version_456",
    "versionB": "version_454",
    "differences": {
      "model": {
        "changed": true,
        "oldValue": "arima",
        "newValue": "prophet",
        "impact": "high"
      },
      "features": {
        "rolling": {
          "changed": true,
          "added": ["7-day mean", "30-day std"],
          "removed": [],
          "impact": "medium"
        }
      },
      "performance": {
        "accuracy": {
          "improvement": 1.4,
          "significance": "significant"
        },
        "trainingTime": {
          "change": -30,
          "improvement": true
        }
      }
    },
    "summary": {
      "totalChanges": 8,
      "improvementScore": 85,
      "regressionRisk": "low",
      "recommendedAction": "proceed"
    }
  },
  "filters": {
    "dateRange": {
      "start": "2024-01-01",
      "end": "2024-01-20"
    },
    "changeTypes": ["model_selection", "feature_engineering"],
    "users": ["Sarah Johnson", "Mike Chen"],
    "versionTypes": ["major", "minor"]
  },
  "searchResults": {
    "query": "feature engineering",
    "results": [
      {
        "versionId": "version_456",
        "relevance": 0.95,
        "matches": ["title", "description", "changes"]
      },
      {
        "versionId": "version_454",
        "relevance": 0.72,
        "matches": ["changes"]
      }
    ]
  }
}
```

## 15. Implementation Notes

### Recommended Libraries
- **react-virtualized**: Virtual scrolling for large version lists
- **react-diff-viewer**: For displaying configuration diffs
- **recharts**: Charts for performance comparison visualization
- **date-fns**: Date formatting and manipulation for timeline
- **fuse.js**: Fuzzy search for version discovery
- **react-spring**: Smooth animations for timeline and transitions

### Complex Implementation Areas
- **Real-time Diff Computation**: Implementing efficient configuration diff algorithms
- **Version State Management**: Managing complex version state and relationships
- **Timeline Virtualization**: Efficiently rendering large version timelines
- **Comparison Interface**: Building intuitive side-by-side comparison views
- **Restoration Workflow**: Implementing safe and reliable version restoration

### Potential Technical Challenges
- **Memory Management**: Handling large version histories and comparison data
- **Performance Optimization**: Maintaining smooth performance with complex diff operations
- **State Synchronization**: Keeping version state consistent across components
- **Data Integrity**: Ensuring version data integrity during operations
- **Collaborative Features**: Managing version access in multi-user environments

### Performance Optimization Opportunities
- **Version Data Caching**: Cache frequently accessed version data
- **Diff Computation Optimization**: Optimize diff algorithms for better performance
- **Timeline Rendering**: Use virtualization and memoization for timeline rendering
- **Background Processing**: Process version operations in background
- **Memory Pooling**: Reuse objects and arrays for diff computations

### Testing Considerations
- **Version Operation Testing**: Test version creation, restoration, and comparison
- **Edge Case Testing**: Test with corrupted data, large histories, and edge cases
- **Performance Testing**: Test with large version datasets and complex diffs
- **Accessibility Testing**: Ensure keyboard navigation and screen reader compatibility
- **Integration Testing**: Test integration with activity workflow and dashboard
- **User Experience Testing**: Test user workflows for version management

## 16. UI Pattern Reference

### DUAL-PANE LAYOUT - Version History Interface Implementation
```tsx
<div className="flex h-full">
  {/* Left Sidebar - Version Timeline */}
  <aside className="w-80 border-r bg-muted/20 overflow-y-auto">
    <div className="sticky top-0 p-4 border-b bg-background/95 backdrop-blur">
      <div className="flex items-center justify-between mb-3">
        <h3 className="font-medium">Version Timeline</h3>
        <Badge variant="outline" className="text-xs">
          {versions.length} versions
        </Badge>
      </div>
      
      <div className="flex items-center gap-2">
        <Input
          placeholder="Search versions..."
          className="text-sm h-8 flex-1"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <Button variant="outline" size="sm" onClick={handleFilterToggle}>
          <Filter className="w-3 h-3" />
        </Button>
      </div>
      
      {showFilters && (
        <div className="mt-3 space-y-2">
          <Select value={filterType} onValueChange={setFilterType}>
            <SelectTrigger className="h-8 text-sm">
              <SelectValue placeholder="Filter by type" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">All versions</SelectItem>
              <SelectItem value="major">Major versions</SelectItem>
              <SelectItem value="minor">Minor versions</SelectItem>
              <SelectItem value="auto">Auto-saved</SelectItem>
            </SelectContent>
          </Select>
        </div>
      )}
    </div>
    
    <div className="relative">
      {/* Timeline Connection Line */}
      <div className="absolute left-8 top-0 bottom-0 w-0.5 bg-border" />
      
      <div className="space-y-1 p-4">
        {filteredVersions.map((version, index) => (
          <div
            key={version.id}
            className={`relative cursor-pointer p-3 rounded-lg transition-all duration-200 ${
              selectedVersion?.id === version.id 
                ? 'bg-primary/10 border border-primary/20 shadow-sm' 
                : 'hover:bg-muted/50'
            }`}
            onClick={() => handleVersionSelect(version)}
          >
            {/* Timeline Dot with Status */}
            <div className={`absolute left-[-20px] w-4 h-4 rounded-full border-2 bg-background flex items-center justify-center ${
              index === 0 
                ? 'border-green-500' 
                : version.type === 'major' 
                  ? 'border-blue-500' 
                  : version.type === 'minor'
                    ? 'border-yellow-500'
                    : 'border-muted-foreground'
            }`}>
              {index === 0 ? (
                <div className="w-2 h-2 bg-green-500 rounded-full" />
              ) : version.type === 'major' ? (
                <div className="w-2 h-2 bg-blue-500 rounded-full" />
              ) : null}
            </div>
            
            <div className="ml-2">
              {/* Version Header */}
              <div className="flex items-center justify-between mb-2">
                <div className="flex items-center gap-2">
                  <Badge 
                    variant={
                      version.type === 'major' ? 'default' : 
                      version.type === 'minor' ? 'secondary' : 'outline'
                    }
                    className="text-xs"
                  >
                    v{version.number}
                  </Badge>
                  {index === 0 && (
                    <Badge className="bg-green-100 text-green-800 text-xs">
                      Current
                    </Badge>
                  )}
                  {version.isAutoSaved && (
                    <Badge variant="outline" className="text-xs">
                      Auto
                    </Badge>
                  )}
                </div>
                <span className="text-xs text-muted-foreground">
                  {formatDistanceToNow(new Date(version.createdAt), { addSuffix: true })}
                </span>
              </div>
              
              {/* Version Content */}
              <div className="space-y-1">
                <div className="font-medium text-sm line-clamp-1">{version.title}</div>
                {version.description && (
                  <div className="text-xs text-muted-foreground line-clamp-2">
                    {version.description}
                  </div>
                )}
              </div>
              
              {/* Change Summary */}
              {version.changes && version.changes.length > 0 && (
                <div className="mt-2">
                  <div className="flex flex-wrap gap-1">
                    {version.changes.slice(0, 3).map((change, idx) => (
                      <Badge 
                        key={idx} 
                        variant="outline" 
                        className={`text-xs ${getChangeTypeColor(change.type)}`}
                      >
                        {change.type.replace('_', ' ')}
                      </Badge>
                    ))}
                    {version.changes.length > 3 && (
                      <Badge variant="outline" className="text-xs text-muted-foreground">
                        +{version.changes.length - 3}
                      </Badge>
                    )}
                  </div>
                </div>
              )}
              
              {/* Version Metadata */}
              <div className="flex items-center gap-2 mt-2 text-xs text-muted-foreground">
                <div className="flex items-center gap-1">
                  <User className="w-3 h-3" />
                  {version.createdBy}
                </div>
                <Separator orientation="vertical" className="h-3" />
                <div className="flex items-center gap-1">
                  <FileText className="w-3 h-3" />
                  {version.size} KB
                </div>
                {version.accuracy && (
                  <>
                    <Separator orientation="vertical" className="h-3" />
                    <div className="flex items-center gap-1">
                      <Target className="w-3 h-3" />
                      {version.accuracy}%
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  </aside>
  
  {/* Right Content - Version Details */}
  <main className="flex-1 flex flex-col overflow-hidden">
    {selectedVersion ? (
      <VersionDetailView version={selectedVersion} />
    ) : (
      <div className="flex-1 flex items-center justify-center text-muted-foreground">
        <div className="text-center">
          <History className="w-12 h-12 mx-auto mb-3 text-muted-foreground/50" />
          <p>Select a version to view details and changes</p>
          <p className="text-sm text-muted-foreground/70 mt-1">
            Use the timeline to explore your activity's evolution
          </p>
        </div>
      </div>
    )}
  </main>
</div>
```

### VERSION COMPARISON INTERFACE - Side-by-Side Diff Display
```tsx
const VersionComparisonModal = ({ isOpen, onClose, versionA, versionB }) => {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-7xl max-h-[95vh] overflow-hidden">
        <DialogHeader>
          <DialogTitle>Compare Versions</DialogTitle>
          <DialogDescription>
            Side-by-side comparison of configuration and performance changes
          </DialogDescription>
        </DialogHeader>
        
        <div className="flex h-[calc(95vh-160px)]">
          {/* Version A */}
          <div className="flex-1 border-r">
            <div className="p-4 border-b bg-blue-50/50">
              <div className="flex items-center gap-3">
                <Badge className="bg-blue-100 text-blue-800">
                  Version {versionA.number}
                </Badge>
                <div>
                  <div className="font-medium">{versionA.title}</div>
                  <div className="text-sm text-muted-foreground">
                    {format(new Date(versionA.createdAt), 'MMM d, yyyy')}
                  </div>
                </div>
              </div>
            </div>
            <div className="p-4 overflow-y-auto h-full">
              <VersionConfigurationDisplay 
                version={versionA} 
                highlightDifferences={true}
                comparisonMode="source"
              />
            </div>
          </div>
          
          {/* Version B */}
          <div className="flex-1">
            <div className="p-4 border-b bg-green-50/50">
              <div className="flex items-center gap-3">
                <Badge className="bg-green-100 text-green-800">
                  Version {versionB.number}
                </Badge>
                <div>
                  <div className="font-medium">{versionB.title}</div>
                  <div className="text-sm text-muted-foreground">
                    {format(new Date(versionB.createdAt), 'MMM d, yyyy')}
                  </div>
                </div>
              </div>
            </div>
            <div className="p-4 overflow-y-auto h-full">
              <VersionConfigurationDisplay 
                version={versionB} 
                highlightDifferences={true}
                comparisonMode="target"
              />
            </div>
          </div>
        </div>
        
        <div className="flex items-center justify-between p-4 border-t bg-muted/20">
          <div className="flex items-center gap-4 text-sm">
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 bg-green-100 border border-green-300 rounded" />
              <span>Added</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 bg-red-100 border border-red-300 rounded" />
              <span>Removed</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 bg-yellow-100 border border-yellow-300 rounded" />
              <span>Modified</span>
            </div>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" size="sm" onClick={handleExportComparison}>
              <Download className="w-3 h-3 mr-1" />
              Export Diff
            </Button>
            <Button variant="outline" onClick={onClose}>
              Close
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};
```

### TIMELINE VISUALIZATION - Version Timeline with Status Indicators
```tsx
const VersionTimeline = ({ versions, selectedVersion, onVersionSelect }) => {
  return (
    <div className="relative">
      {/* Timeline Base Line */}
      <div className="absolute left-8 top-0 bottom-0 w-0.5 bg-gradient-to-b from-green-500 via-blue-500 to-muted" />
      
      <div className="space-y-1">
        {versions.map((version, index) => {
          const isSelected = selectedVersion?.id === version.id;
          const isCurrent = index === 0;
          const timelineDotColor = 
            isCurrent ? 'border-green-500 bg-green-50' :
            version.type === 'major' ? 'border-blue-500 bg-blue-50' :
            version.type === 'minor' ? 'border-yellow-500 bg-yellow-50' :
            'border-muted-foreground bg-muted';
          
          return (
            <div
              key={version.id}
              className={`relative cursor-pointer transition-all duration-200 ${
                isSelected 
                  ? 'transform scale-[1.02]' 
                  : ''
              }`}
              onClick={() => onVersionSelect(version)}
            >
              {/* Timeline Dot */}
              <div className={`absolute left-[-20px] w-4 h-4 rounded-full border-2 ${timelineDotColor} flex items-center justify-center z-10 shadow-sm`}>
                {isCurrent && (
                  <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse" />
                )}
                {version.type === 'major' && !isCurrent && (
                  <div className="w-2 h-2 bg-blue-500 rounded-full" />
                )}
              </div>
              
              {/* Version Card */}
              <div className={`ml-4 p-3 rounded-lg border transition-all duration-200 ${
                isSelected 
                  ? 'bg-primary/10 border-primary/20 shadow-md' 
                  : 'bg-background hover:bg-muted/50 border-border hover:border-primary/30'
              }`}>
                <VersionCard version={version} isSelected={isSelected} isCurrent={isCurrent} />
              </div>
              
              {/* Connection to Next Version */}
              {index < versions.length - 1 && (
                <div className="absolute left-[-16px] top-[60px] w-2 h-4 border-l-2 border-dashed border-muted opacity-50" />
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};
```

## 17. Documentation Quality Checklist

### ✅ UI Pattern Specification:
- [x] Version history interface labeled as "VERSION HISTORY MODAL INTERFACE" with structured layout
- [x] Timeline organization specified as "DUAL-PANE LAYOUT" with exact dimensions
- [x] Version comparison specified as side-by-side interface with diff highlighting
- [x] Layout includes exact CSS classes and responsive behavior specifications
- [x] Timeline visualization with status indicators and connection lines

### ✅ Implementation Code:
- [x] Complete TSX code examples provided for all version history components
- [x] shadcn/ui component structure specified (Dialog, Tabs, Badge, Progress, etc.)
- [x] Exact CSS classes documented for timeline states and version selection
- [x] Version comparison interface with side-by-side diff display
- [x] State management patterns with version tracking and operations

### ✅ Visual Elements:
- [x] Timeline visualization with color-coded version dots and connection lines
- [x] Version cards with status indicators (current, major, minor, auto-saved)
- [x] Change type badges with color-coded categorization
- [x] Performance metrics display with comparison indicators
- [x] Icon specifications throughout (History, User, Target, GitCompare, etc.)

### ✅ Layout Structure:
- [x] Exact dual-pane layout specifications with sidebar (w-80) and main content
- [x] Version timeline with sticky header and virtualized scrolling
- [x] Modal dialog with maximum width (max-w-6xl) and height constraints
- [x] Comparison interface with split-screen layout and legend
- [x] Responsive behavior for different screen sizes and device types

### ✅ Interaction Patterns:
- [x] Version selection from timeline with visual feedback
- [x] Version comparison with side-by-side configuration diff
- [x] Version restoration workflow with confirmation and backup
- [x] Search and filter functionality for version discovery
- [x] Export and sharing capabilities for version management

### ❌ Rejected Generic Terms:
- [x] No usage of "version history dialog" - used "VERSION HISTORY MODAL INTERFACE" with specific structure
- [x] No vague timeline descriptions - specific "DUAL-PANE LAYOUT" with exact measurements
- [x] All layout descriptions include exact CSS classes and responsive behavior
- [x] Implementation code provided for all complex version management patterns
- [x] Version comparison system specified with exact diff visualization and interaction

**Documentation eliminates all ambiguity and provides exact implementation guidance for Version History Dialog with comprehensive version management, comparison capabilities, and restoration workflows.**