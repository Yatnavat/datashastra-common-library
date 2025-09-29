# Activity Dashboard - Screen Documentation

## 1. Screen Overview

- **Primary purpose and user goals**: Main landing screen for managing demand forecasting activities, tracking progress across multiple client projects, and creating new forecasting activities
- **User role/permissions required**: Authenticated users with access to demand forecasting system - all user roles (Data Analyst, Project Manager, Business User)
- **Entry points**: Application startup (default screen), back navigation from Activity Workflow, breadcrumb navigation from any workflow step
- **Screen priority**: Core feature - primary entry point for all application functionality

## 2. Visual Layout

### DASHBOARD LAYOUT Structure
- **Layout structure**: `<div className="space-y-6 p-6">` with vertical sections
- **Main Container**: `<div className="max-w-7xl mx-auto">` for content width control
- **Section Organization**: 
  ```tsx
  <div className="space-y-6">
    <header className="space-y-4" />
    <section className="statistics-section" />
    <section className="main-content grid grid-cols-1 lg:grid-cols-3 gap-6" />
  </div>
  ```

### Responsive Breakpoints
```css
/* Mobile First Approach */
Base (< 768px):     Single column, stacked layout, activities full-width
Tablet (768px+):    2-column layout for activities list and details panel
Desktop (1024px+):  3-column optimal layout: activities (2 cols) + details (1 col)
```

### RESPONSIVE GRID LAYOUT Specifications
- **Statistics Cards**: `grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4`
- **Main Content Area**: `grid grid-cols-1 lg:grid-cols-3 gap-6`
- **Activities List**: `space-y-4` with responsive card layout
- **Implementation**:
```tsx
<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
  {/* Statistics cards */}
</div>
<div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
  <div className="lg:col-span-2">
    {/* Activities list */}
  </div>
  <div className="lg:col-span-1">
    {/* Activity details panel */}
  </div>
</div>
```

### Typography System (14px base - NO OVERRIDES)
- **Main Title**: 20px (h1 default) + font-medium "Demand Forecasting Activities"
- **Section Titles**: 18px (h2 default) + font-medium for section headers
- **Card Headers**: 16px (h3 default) + font-medium for activity names
- **Body Text**: 14px (p default) + font-normal for descriptions
- **Labels**: 14px (label default) + font-medium for form labels

### Color Scheme
- **Background**: `bg-background` (white)
- **Cards**: `bg-card` with `hover:shadow-lg transition-shadow`
- **Primary Actions**: `bg-primary text-primary-foreground`
- **Statistics**: `text-chart-1` through `text-chart-5` for different metrics

## 3. Components Inventory

### Header Section - DASHBOARD LAYOUT
- **Main Title**: "Demand Forecasting Activities" (h1 with default styling)
- **Create Activity Button**: Primary button with `<Plus />` icon, triggers Create Activity Dialog
- **Search and Filter Bar**: 
  ```tsx
  <div className="flex flex-col sm:flex-row gap-4">
    <Input placeholder="Search activities..." className="flex-1" />
    <Select placeholder="Filter by status">
      <SelectTrigger className="w-48">
        <SelectValue />
      </SelectTrigger>
    </Select>
  </div>
  ```

### Statistics Cards Section - METRIC CARDS WITH LARGE NUMBERS
- **Component**: 4-COLUMN RESPONSIVE GRID using shadcn/ui Card components
- **Layout Pattern**: `grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4`
- **Card Structure**:
```tsx
<Card className="hover:shadow-lg transition-shadow">
  <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
    <CardTitle className="text-sm font-medium">Total Activities</CardTitle>
    <Activity className="h-4 w-4 text-muted-foreground" />
  </CardHeader>
  <CardContent>
    <div className="text-2xl font-bold">124</div>
    <p className="text-xs text-muted-foreground">+12% from last month</p>
  </CardContent>
</Card>
```

### Activities List Section - RESPONSIVE CARD GRID
- **Component**: Scrollable activity cards list
- **Layout Pattern**: `space-y-4` with individual card hover states
- **Card Structure**:
```tsx
<Card className="cursor-pointer hover:shadow-md transition-shadow duration-200 border-l-4 border-l-transparent data-[selected=true]:border-l-primary">
  <CardHeader>
    <div className="flex items-center justify-between">
      <CardTitle>{activity.name}</CardTitle>
      <Badge variant={statusVariant}>{activity.status}</Badge>
    </div>
    <CardDescription>{client} • {project}</CardDescription>
  </CardHeader>
  <CardContent>
    <div className="grid grid-cols-2 gap-4 text-sm">
      <div>
        <span className="text-muted-foreground">Industry:</span>
        <span className="ml-2">{activity.industry}</span>
      </div>
      <div>
        <span className="text-muted-foreground">Priority:</span>
        <Badge variant="outline" className="ml-2">{activity.priority}</Badge>
      </div>
    </div>
  </CardContent>
</Card>
```

### Activity Details Panel - DETAILED CARD LAYOUT
- **Component**: Fixed-position details panel showing selected activity information
- **Position**: Right sidebar in desktop, overlay on mobile
- **Structure**:
```tsx
<Card className="sticky top-6">
  <CardHeader>
    <CardTitle>Activity Details</CardTitle>
  </CardHeader>
  <CardContent className="space-y-4">
    <div className="grid grid-cols-2 gap-4">
      <div>
        <label className="text-sm font-medium">Client</label>
        <p>{selectedActivity.client}</p>
      </div>
      <div>
        <label className="text-sm font-medium">Project</label>
        <p>{selectedActivity.project}</p>
      </div>
    </div>
    <Separator />
    <div>
      <label className="text-sm font-medium">Forecast History</label>
      <div className="mt-2 space-y-2">
        {/* Version history items */}
      </div>
    </div>
  </CardContent>
  <CardFooter>
    <Button className="w-full">Open Activity</Button>
  </CardFooter>
</Card>
```

## 4. Data Display Elements

### Statistics Cards Data
- **Total Activities**: Number, source: `/api/activities/count`, format: integer with percentage change
- **Total Forecasts**: Number, source: `/api/forecasts/count`, format: integer with trend indicator
- **Active Activities**: Number, source: `/api/activities?status=active`, format: integer
- **Unique Clients**: Number, source: `/api/clients/count`, format: integer with growth indicator

### Activity List Data
- **Activity Name**: String, source: `activity.name`, required field
- **Client**: String, source: `activity.client`, dropdown selection
- **Project**: String, source: `activity.project`, dropdown selection
- **Status**: Enum, source: `activity.status`, values: ['Active', 'Completed', 'On-hold', 'Draft']
- **Priority**: Enum, source: `activity.priority`, values: ['High', 'Medium', 'Low']
- **Industry**: String, source: `activity.industry`, optional categorization
- **Created Date**: ISO Date, source: `activity.createdAt`, format: "MMM DD, YYYY"
- **Last Modified**: ISO Date, source: `activity.lastModified`, format: "MMM DD, YYYY HH:mm"
- **Forecast Count**: Number, source: `activity.forecasts.length`, derived field

### Empty State Handling
- **No Activities**: Display empty state with illustration and "Create First Activity" CTA
- **No Search Results**: Display "No activities found" with clear filters option
- **Loading State**: Skeleton cards with shimmer animation using shadcn/ui Skeleton

### Error States
- **API Error**: Toast notification with retry option
- **Network Error**: Offline indicator with cached data display
- **Permission Error**: Access denied message with contact admin option

## 5. Interactive Features

### Create Activity Button - PRIMARY ACTION
- **Label**: "New Activity" with `<Plus />` icon
- **Action**: Opens Create Activity Dialog
- **Visual States**:
  - Default: Primary button styling
  - Hover: Increased shadow and slight scale
  - Loading: Spinner icon replacement
- **Implementation**:
```tsx
<Button onClick={() => setCreateDialogOpen(true)} className="gap-2">
  <Plus className="h-4 w-4" />
  New Activity
</Button>
```

### Activity Card Selection - STATE BUTTONS
- **Interaction**: Click to select activity
- **Visual States**:
  - Default: `border-l-transparent`
  - Selected: `border-l-primary` with enhanced shadow
  - Hover: `hover:shadow-md transition-shadow duration-200`
- **State Changes**: Updates details panel, enables action buttons

### Search and Filter Controls - INTERACTIVE FORM ELEMENTS
- **Search Input**: Real-time filtering with debounced API calls
- **Status Filter**: Dropdown with multi-select capability
- **Priority Filter**: Radio group for single selection
- **Date Range Filter**: Calendar picker for creation/modification dates
- **Implementation**:
```tsx
<div className="flex flex-col sm:flex-row gap-4 mb-6">
  <div className="relative flex-1">
    <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
    <Input 
      placeholder="Search activities..." 
      className="pl-10"
      value={searchTerm}
      onChange={(e) => setSearchTerm(e.target.value)}
    />
  </div>
  <Select value={statusFilter} onValueChange={setStatusFilter}>
    <SelectTrigger className="w-48">
      <SelectValue placeholder="Filter by status" />
    </SelectTrigger>
    <SelectContent>
      <SelectItem value="all">All Statuses</SelectItem>
      <SelectItem value="active">Active</SelectItem>
      <SelectItem value="completed">Completed</SelectItem>
      <SelectItem value="on-hold">On Hold</SelectItem>
    </SelectContent>
  </Select>
</div>
```

### Activity Actions - CONTEXTUAL BUTTONS
- **Open Activity**: Navigate to Activity Workflow
- **Edit Activity**: Open edit dialog (inline editing)
- **Archive Activity**: Soft delete with confirmation
- **Clone Activity**: Duplicate activity with new name
- **Quick Actions Menu**: 3-dot menu with additional options

### Sorting and Organization
- **Sort Options**: Name, Created Date, Last Modified, Priority, Status
- **Sort Direction**: Ascending/Descending with visual indicators
- **Drag and Drop**: Reorder activities for custom organization
- **Bulk Actions**: Multi-select with batch operations

## 6. Navigation Elements

### Primary Navigation
- **Create Activity Dialog**: `setCurrentView('create-dialog')` → Modal overlay
- **Activity Workflow**: `setCurrentView('workflow')` → Full-screen workflow
- **Activity Details Panel**: Inline expansion/collapse

### Breadcrumb Navigation
- **Current Location**: "Dashboard" (root level)
- **No parent breadcrumbs**: This is the top-level screen

### Quick Navigation
- **Activity Cards**: Click to select, double-click to open
- **Search Results**: Direct navigation to filtered activities
- **Recent Activities**: Quick access to recently modified activities

## 7. Dynamic Behaviors

### Real-time Updates
- **Activity Status Changes**: WebSocket updates for status modifications
- **New Activity Creation**: Instant addition to list without page refresh
- **Forecast Completion**: Status badge updates when forecasts complete
- **Collaborative Editing**: Real-time indicators when others modify activities

### Auto-refresh
- **Statistics Cards**: Update every 30 seconds
- **Activity List**: Refresh on window focus
- **Status Polling**: Check for updates every 60 seconds

### Animations and Transitions
- **Card Hover**: `hover:shadow-md transition-shadow duration-200`
- **Selection State**: Smooth border and shadow transitions
- **Loading States**: Skeleton shimmer animations
- **Page Transitions**: Fade-in/fade-out for view changes
- **Staggered Loading**: Cards appear with slight delay for visual flow

### Progressive Disclosure
- **Activity Details**: Expand panel on selection
- **Advanced Filters**: Collapsible filter section
- **Bulk Actions**: Show when multiple items selected
- **Search Suggestions**: Dropdown with recent searches and suggestions

## 8. State Management

### Component States
- **Loading**: `isLoading` for initial data fetch, skeleton display
- **Empty**: `activities.length === 0`, show empty state with CTA
- **Error**: `error` state with retry functionality and error message
- **Success**: Normal loaded state with data display

### User Interaction States
- **Selected Activity**: `selectedActivity` object, updates details panel
- **Search State**: `searchTerm`, `filteredActivities` derived state
- **Filter State**: `statusFilter`, `priorityFilter`, `dateRange` objects
- **Sort State**: `sortBy`, `sortDirection` for list ordering

### Data Refresh Triggers
- **Manual Refresh**: Pull-to-refresh on mobile, refresh button on desktop
- **Auto Refresh**: Timer-based updates for statistics and status
- **Event-driven**: WebSocket events for real-time updates
- **Window Focus**: Refresh data when user returns to tab

### Cache Requirements
- **Activities List**: Cache for 5 minutes, invalidate on mutations
- **Statistics Data**: Cache for 1 minute, background refresh
- **User Preferences**: Persist filter and sort preferences locally
- **Search History**: Store recent searches in localStorage

### Offline Behavior
- **Cached Data**: Display last known state with offline indicator
- **Queued Actions**: Store create/edit actions for sync when online
- **Sync Conflicts**: Resolve conflicts with user confirmation
- **Connection Status**: Visual indicator for network state

## 9. API Requirements

### Primary Endpoints
- **GET** `/api/activities` - Fetch activities list with pagination
  - Query params: `page`, `limit`, `search`, `status`, `priority`, `sortBy`, `sortOrder`
  - Response: `{ activities: Activity[], totalCount: number, hasMore: boolean }`

- **GET** `/api/activities/stats` - Dashboard statistics
  - Response: `{ totalActivities: number, totalForecasts: number, activeActivities: number, uniqueClients: number, trends: object }`

- **POST** `/api/activities` - Create new activity
  - Body: `{ name: string, client: string, project: string, description?: string, industry?: string, priority: string }`
  - Response: `Activity` object

- **PUT** `/api/activities/:id` - Update activity
  - Body: Partial `Activity` object
  - Response: Updated `Activity` object

- **DELETE** `/api/activities/:id` - Archive activity
  - Response: `{ success: boolean }`

### Pagination Details
- **Page Size**: 20 activities per page
- **Infinite Scroll**: Load more on scroll to bottom
- **Virtual Scrolling**: For large datasets (>1000 activities)

### Filter/Sort Parameters
- **Search**: Full-text search across name, client, project, description
- **Status Filter**: Array of status values
- **Priority Filter**: Single priority value
- **Date Range**: `{ from: string, to: string }` ISO dates
- **Sort Options**: `name`, `createdAt`, `lastModified`, `priority`, `status`

### WebSocket Connections
- **Activity Updates**: `/ws/activities` - Real-time activity changes
- **User Presence**: `/ws/presence` - Show who's viewing/editing activities
- **System Notifications**: `/ws/notifications` - System-wide announcements

## 10. Business Logic

### Activity Creation Rules
- **Name Uniqueness**: Activity names must be unique within client-project combination
- **Required Fields**: Client, Project, Activity Name are mandatory
- **Auto-generated Fields**: ID, creation timestamp, initial status ('Draft')
- **Default Values**: Priority defaults to 'Medium', Industry defaults to 'General'

### Status Validation
- **Status Transitions**: Draft → Active → Completed/On-hold
- **Restriction Rules**: Cannot delete activities with active forecasts
- **Permission Checks**: Only activity owners or admins can modify status

### Search and Filter Logic
- **Search Algorithm**: Fuzzy matching with relevance scoring
- **Filter Combination**: AND logic between different filter types
- **Sort Priority**: Primary sort with secondary fallbacks
- **Performance**: Server-side filtering for large datasets

### Data Dependencies
- **Client-Project Relationship**: Projects must belong to selected client
- **Forecast Dependencies**: Activity cannot be deleted if forecasts exist
- **Permission Inheritance**: Activity permissions inherit from project permissions

## 11. Accessibility Requirements

### ARIA Labels
- **Search Input**: `aria-label="Search activities"`
- **Filter Controls**: `aria-label="Filter by status"`, `aria-label="Filter by priority"`
- **Activity Cards**: `aria-label="Activity: {name}, Client: {client}, Status: {status}"`
- **Action Buttons**: `aria-label="Create new activity"`, `aria-label="Open activity details"`

### Keyboard Navigation Flow
1. **Tab Order**: Create button → Search → Filters → Activity cards → Details panel actions
2. **Activity List**: Arrow keys for navigation, Enter to select, Space to open
3. **Card Navigation**: `tabIndex={0}` for keyboard focus, visual focus indicators
4. **Skip Links**: Skip to main content, skip to activity list

### Screen Reader Considerations
- **Live Regions**: Announce search results, filter changes, status updates
- **Descriptive Text**: Comprehensive descriptions for statistics and status changes
- **Loading States**: Announce when data is loading or has loaded
- **Error Messages**: Clear error announcements with actionable guidance

### Focus Management
- **Modal Opening**: Focus moves to dialog, trapped within modal
- **Page Navigation**: Focus preserved on back navigation
- **Dynamic Content**: Focus management for lazy-loaded content
- **Interactive Elements**: Visible focus indicators for all interactive elements

### Color Contrast Requirements
- **Status Indicators**: High contrast colors for status badges
- **Interactive Elements**: 4.5:1 contrast ratio for text
- **Focus Indicators**: High contrast focus rings
- **Error States**: Color + text/icon combinations for accessibility

## 12. Performance Considerations

### Lazy Loading Requirements
- **Activity Cards**: Virtualized scrolling for large lists
- **Images/Avatars**: Lazy load user avatars and project images
- **Details Panel**: Load activity details on selection
- **Statistics**: Background loading with cached display

### Image Optimization
- **User Avatars**: Responsive images with fallback initials
- **Project Logos**: Optimized SVG or WebP formats
- **Empty State Illustrations**: Inline SVG for fast loading

### Data Pagination
- **Initial Load**: Load first 20 activities
- **Infinite Scroll**: Load 20 more on scroll trigger
- **Search Results**: Separate pagination for search
- **Cache Strategy**: Cache loaded pages, invalidate on updates

### Cache Strategies
- **Browser Cache**: Static assets cached for 1 year
- **API Cache**: Response caching with ETags
- **Memory Cache**: Keep frequently accessed data in memory
- **localStorage**: Persist user preferences and search history

### Bundle Size Concerns
- **Code Splitting**: Separate chunks for create dialog and details panel
- **Tree Shaking**: Remove unused components and utilities
- **Dynamic Imports**: Load heavy components on demand
- **Critical CSS**: Inline critical styles for above-fold content

## 13. Edge Cases

### Empty States
- **No Activities**: Welcome message with guided onboarding
- **Search No Results**: Clear filters option and search suggestions
- **Network Error**: Offline mode with cached data display
- **Permission Denied**: Appropriate error message with contact information

### Error Scenarios
- **API Timeout**: Retry mechanism with exponential backoff
- **Invalid Data**: Client-side validation with helpful error messages
- **Concurrent Modifications**: Conflict resolution with user choice
- **Browser Compatibility**: Graceful degradation for older browsers

### Maximum Data Limits
- **Large Activity Lists**: Virtual scrolling and pagination
- **Long Activity Names**: Text truncation with tooltip
- **Many Filters**: Collapsible filter sections
- **High Frequency Updates**: Rate limiting and batching

### Timeout Handling
- **Session Timeout**: Automatic token refresh
- **Request Timeout**: 30-second timeout with retry option
- **Idle Timeout**: Warning before automatic logout
- **WebSocket Reconnection**: Automatic reconnection with backoff

### Offline Functionality
- **Cache Strategy**: Service worker for offline access
- **Sync Queue**: Queue actions for online sync
- **Offline Indicator**: Clear visual indication of offline state
- **Data Conflicts**: Resolution strategy for offline/online data conflicts

## 14. Sample Data Structure

```json
{
  "statistics": {
    "totalActivities": 124,
    "totalForecasts": 89,
    "activeActivities": 23,
    "uniqueClients": 15,
    "trends": {
      "activitiesGrowth": 12.5,
      "forecastsGrowth": 8.3,
      "clientsGrowth": 25.0
    }
  },
  "activities": [
    {
      "id": "act_001",
      "name": "Q4 Demand Forecast - Electronics",
      "client": "TechCorp Solutions",
      "project": "Product Demand Optimization",
      "description": "Quarterly demand forecasting for consumer electronics portfolio",
      "industry": "Technology",
      "priority": "High",
      "status": "Active",
      "createdAt": "2024-01-15T09:30:00Z",
      "lastModified": "2024-01-20T14:45:00Z",
      "createdBy": "john.smith@example.com",
      "forecasts": [
        {
          "id": "forecast_001",
          "version": "v1.0",
          "status": "Completed",
          "accuracy": 94.2,
          "createdAt": "2024-01-18T11:20:00Z"
        }
      ],
      "tags": ["quarterly", "electronics", "high-priority"],
      "permissions": {
        "canEdit": true,
        "canDelete": false,
        "canView": true
      }
    }
  ],
  "pagination": {
    "currentPage": 1,
    "totalPages": 7,
    "totalCount": 124,
    "hasNext": true,
    "hasPrevious": false
  },
  "filters": {
    "availableStatuses": ["Active", "Completed", "On-hold", "Draft"],
    "availablePriorities": ["High", "Medium", "Low"],
    "availableIndustries": ["Technology", "Retail", "Manufacturing", "Healthcare"],
    "availableClients": ["TechCorp Solutions", "RetailMax Inc", "Global Manufacturing"]
  }
}
```

## 15. Implementation Notes

### Recommended Libraries
- **shadcn/ui**: For all UI components (Card, Button, Input, Select, etc.)
- **lucide-react**: For icons (Plus, Search, Activity, etc.)
- **date-fns**: For date formatting and manipulation
- **react-query/tanstack-query**: For API state management and caching
- **react-hook-form**: For form handling in dialogs
- **react-virtual**: For virtualized scrolling in large lists

### Complex Implementation Areas
- **Real-time Updates**: WebSocket integration for live activity updates
- **Infinite Scrolling**: Performance optimization for large datasets
- **Search Debouncing**: Efficient search with API call optimization
- **State Synchronization**: Keeping UI state in sync with server state

### Potential Technical Challenges
- **Memory Management**: Large activity lists causing memory issues
- **WebSocket Scaling**: Handling connection drops and reconnections
- **Search Performance**: Fast search across large datasets
- **Responsive Design**: Complex layout changes across breakpoints

### Performance Optimization Opportunities
- **Memoization**: React.memo for activity cards to prevent unnecessary re-renders
- **Bundle Splitting**: Separate chunks for different sections
- **Prefetching**: Preload likely-to-be-accessed data
- **Image Optimization**: Responsive images with proper loading strategies

### Testing Considerations
- **Unit Tests**: Component testing with React Testing Library
- **Integration Tests**: API integration and data flow testing
- **E2E Tests**: Full user journey testing with Playwright/Cypress
- **Performance Tests**: Load testing for large datasets
- **Accessibility Tests**: Automated accessibility testing with axe-core

## 16. UI Pattern Reference

### DASHBOARD LAYOUT - Complete Implementation
```tsx
<div className="space-y-6 p-6">
  <div className="max-w-7xl mx-auto">
    {/* Header Section */}
    <header className="space-y-4">
      <div className="flex items-center justify-between">
        <h1>Demand Forecasting Activities</h1>
        <Button onClick={() => setCreateDialogOpen(true)} className="gap-2">
          <Plus className="h-4 w-4" />
          New Activity
        </Button>
      </div>
      {/* Search and Filter Bar */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
          <Input placeholder="Search activities..." className="pl-10" />
        </div>
        <Select>
          <SelectTrigger className="w-48">
            <SelectValue placeholder="Filter by status" />
          </SelectTrigger>
        </Select>
      </div>
    </header>

    {/* Statistics Cards - 4-COLUMN RESPONSIVE GRID */}
    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card className="hover:shadow-lg transition-shadow">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">Total Activities</CardTitle>
          <Activity className="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">124</div>
          <p className="text-xs text-muted-foreground">+12% from last month</p>
        </CardContent>
      </Card>
      {/* Repeat for other statistics */}
    </section>

    {/* Main Content - 3-COLUMN LAYOUT */}
    <section className="grid grid-cols-1 lg:grid-cols-3 gap-6">
      {/* Activities List - 2 columns on desktop */}
      <div className="lg:col-span-2 space-y-4">
        {activities.map((activity) => (
          <Card 
            key={activity.id}
            className="cursor-pointer hover:shadow-md transition-shadow duration-200 border-l-4 border-l-transparent data-[selected=true]:border-l-primary"
            onClick={() => setSelectedActivity(activity)}
          >
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle>{activity.name}</CardTitle>
                <Badge variant={getStatusVariant(activity.status)}>
                  {activity.status}
                </Badge>
              </div>
              <CardDescription>{activity.client} • {activity.project}</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <span className="text-muted-foreground">Industry:</span>
                  <span className="ml-2">{activity.industry}</span>
                </div>
                <div>
                  <span className="text-muted-foreground">Priority:</span>
                  <Badge variant="outline" className="ml-2">{activity.priority}</Badge>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Activity Details Panel - 1 column on desktop */}
      <div className="lg:col-span-1">
        <Card className="sticky top-6">
          <CardHeader>
            <CardTitle>Activity Details</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {selectedActivity ? (
              <>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium">Client</label>
                    <p>{selectedActivity.client}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium">Project</label>
                    <p>{selectedActivity.project}</p>
                  </div>
                </div>
                <Separator />
                <div>
                  <label className="text-sm font-medium">Forecast History</label>
                  <div className="mt-2 space-y-2">
                    {selectedActivity.forecasts?.map((forecast) => (
                      <div key={forecast.id} className="flex justify-between items-center p-2 bg-muted rounded">
                        <span className="text-sm">{forecast.version}</span>
                        <Badge variant="outline">{forecast.status}</Badge>
                      </div>
                    ))}
                  </div>
                </div>
              </>
            ) : (
              <p className="text-muted-foreground">Select an activity to view details</p>
            )}
          </CardContent>
          {selectedActivity && (
            <CardFooter>
              <Button className="w-full" onClick={() => handleOpenActivity(selectedActivity)}>
                Open Activity
              </Button>
            </CardFooter>
          )}
        </Card>
      </div>
    </section>
  </div>
</div>
```

### METRIC CARDS WITH LARGE NUMBERS - Statistics Implementation
```tsx
{statisticsData.map((stat, index) => (
  <Card key={stat.label} className="hover:shadow-lg transition-shadow">
    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
      <CardTitle className="text-sm font-medium">{stat.label}</CardTitle>
      <stat.icon className="h-4 w-4 text-muted-foreground" />
    </CardHeader>
    <CardContent>
      <div className="text-2xl font-bold">{stat.value}</div>
      <p className="text-xs text-muted-foreground">
        <span className={stat.trend > 0 ? "text-green-600" : "text-red-600"}>
          {stat.trend > 0 ? "+" : ""}{stat.trend}%
        </span>
        {" "}from last month
      </p>
    </CardContent>
  </Card>
))}
```

### RESPONSIVE GRID LAYOUT - Breakpoint Behavior
```tsx
// Statistics Cards - 4-column responsive grid
<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
  {/* 1 column on mobile, 2 on tablet, 4 on desktop */}
</div>

// Main Content - 3-column layout
<div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
  <div className="lg:col-span-2">
    {/* Activities list takes 2/3 width on desktop, full width on mobile */}
  </div>
  <div className="lg:col-span-1">
    {/* Details panel takes 1/3 width on desktop, full width on mobile */}
  </div>
</div>
```

## 17. Documentation Quality Checklist

### ✅ UI Pattern Specification:
- [x] Dashboard layout labeled as "DASHBOARD LAYOUT" with section breakdown
- [x] Statistics cards specified as "METRIC CARDS WITH LARGE NUMBERS"
- [x] Grid layouts include exact CSS classes: `grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4`
- [x] Responsive behavior specified with exact breakpoint classes
- [x] Card layouts specified as "DETAILED CARD LAYOUT" with sections

### ✅ Implementation Code:
- [x] Complete TSX code examples provided for complex UI patterns
- [x] shadcn/ui component structure specified (Card, Button, Input, etc.)
- [x] Exact CSS classes documented (no generic descriptions)
- [x] Responsive behavior specified with breakpoint classes
- [x] Component props and state management included

### ✅ Visual Elements:
- [x] Specific visual components described (statistics cards, activity cards, details panel)
- [x] Icon specifications included (Plus, Search, Activity icons)
- [x] Color coding and styling specified (status badges, priority indicators)
- [x] Hover states and transitions documented (shadow effects, border changes)

### ✅ Layout Structure:
- [x] Exact grid specifications provided (4-column statistics, 3-column main layout)
- [x] Responsive breakpoint behavior detailed (mobile/tablet/desktop)
- [x] Spacing and padding classes specified (space-y-6, p-6, gap-4)
- [x] Component hierarchy clearly defined (header/statistics/main content)

### ✅ Interaction Patterns:
- [x] Button states documented (default, selected, hover, loading)
- [x] Card selection behavior specified (border changes, shadow effects)
- [x] Search and filter interaction patterns detailed
- [x] Form interaction patterns detailed (create activity flow)

### ❌ Rejected Generic Terms:
- [x] No usage of "statistics dashboard" - used "METRIC CARDS WITH LARGE NUMBERS"
- [x] No vague UI descriptions - specific patterns documented
- [x] All layout descriptions include exact CSS classes
- [x] Implementation code provided for all complex patterns
- [x] Responsive behavior specified with exact breakpoint classes

**Documentation eliminates all ambiguity and provides exact implementation guidance for the Activity Dashboard screen.**