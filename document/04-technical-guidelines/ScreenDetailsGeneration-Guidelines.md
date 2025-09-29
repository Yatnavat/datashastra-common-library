# Figma Screen Documentation Generation Prompt

Analyze the Screen and create a detailed description.md file that documents every aspect of this screen for Angular development.

## ⚠️ CRITICAL: AVOID GENERIC IMPLEMENTATIONS

### Common Documentation Mistakes That Lead to Generic UIs:

❌ **WRONG**: "Statistics display cards"
✅ **CORRECT**: "Statistics Cards Section - RESPONSIVE GRID LAYOUT with 4-COLUMN GRID"

❌ **WRONG**: "Interface with tabs for different views"  
✅ **CORRECT**: "Data Overview & Statistics - TABBED INTERFACE with EXACT 5-TAB STRUCTURE: Overview → Descriptive → Temporal → Categorical → Quality"

❌ **WRONG**: "Progress indicators for data quality"
✅ **CORRECT**: "Quality scores with CIRCULAR PROGRESS INDICATORS: `<CircularProgress value={98.5} label="Completeness" />`"

❌ **WRONG**: "Grid layout for projects"
✅ **CORRECT**: "Project Grid Section - RESPONSIVE CARD GRID: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6`"

❌ **WRONG**: "Chart visualization area"
✅ **CORRECT**: "Main Forecast Visualization - INTERACTIVE CHART DASHBOARD with h-96 time series chart and toolbar controls"

### MANDATORY Keywords to Use:
- **"TABBED INTERFACE"** for any tab-based UI
- **"X-TAB STRUCTURE"** specifying exact number of tabs
- **"RESPONSIVE GRID LAYOUT"** for grid-based layouts
- **"CIRCULAR PROGRESS INDICATORS"** for circular progress
- **"HORIZONTAL PROGRESS BARS"** for linear progress
- **"DASHBOARD LAYOUT"** for multi-section interfaces
- **"INTERACTIVE CHART DASHBOARD"** for chart-heavy interfaces

Structure the markdown file with the following sections:

# Screen Documentation

## 1. Screen Overview
- Primary purpose and user goals
- User role/permissions required
- Entry points (how users navigate to this screen)
- Screen priority (core/secondary/optional feature)

## 2. Visual Layout
- Layout structure (grid/flex arrangement) WITH EXACT CSS CLASSES
- Responsive breakpoints and behavior WITH SPECIFIC BREAKPOINTS
- Color scheme and theming WITH CSS VARIABLE REFERENCES
- Typography hierarchy WITH EXACT FONT SIZES (NO OVERRIDES)
- Spacing and padding system WITH TAILWIND CLASSES

### CRITICAL: Include Exact Layout Specifications

#### Responsive Breakpoints (Use These Exact Patterns):
```css
/* Mobile First Approach */
Base (< 768px):     Single column, full width components
Tablet (768px+):    2-column layout with consistent spacing  
Desktop (1024px+):  3-column optimal layout with enhanced spacing
```

#### Grid Patterns (Always Specify Exact Classes):
- **"RESPONSIVE GRID"**: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4`
- **"DASHBOARD GRID"**: `grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4`
- **"CHART GRID"**: `grid grid-cols-1 lg:grid-cols-2 gap-6`

#### Typography (Use Default System - NO OVERRIDES):
```css
/* Based on 14px base font size - DO NOT OVERRIDE */
Main Title:         20px (h1 default) + font-medium
Section Titles:     18px (h2 default) + font-medium  
Subsection:         16px (h3 default) + font-medium
Body Text:          14px (p default) + font-normal
Labels:             14px (label default) + font-medium
```

#### Layout Structure (Always Include Exact Implementation):
```tsx
<div className="space-y-6 p-6">
  <section className="space-y-4">
    <!-- Exact structure with classes -->
  </section>
</div>
```

## 3. Components Inventory
List every UI component with EXPLICIT UI PATTERNS:
- Component name and type WITH SPECIFIC UI STRUCTURE KEYWORDS
- Position in layout
- Visual properties (size, color, style)
- Responsive behavior with EXACT CSS CLASSES
- Reusable vs unique component

### CRITICAL: Use These UI Pattern Keywords to Prevent Generic Implementations

#### For Tabbed Interfaces:
- **"TABBED INTERFACE"** - Always specify this explicitly
- **"EXACT X-TAB STRUCTURE"** - Specify exact number of tabs
- **"TAB SEQUENCE: Tab1 → Tab2 → Tab3"** - List exact tab order
- Include shadcn/ui Tabs implementation code:
```tsx
<Tabs defaultValue="tab1" className="w-full">
  <TabsList className="grid w-full grid-cols-X">
    <TabsTrigger value="tab1">Tab1</TabsTrigger>
    // ... exact tabs
  </TabsList>
</Tabs>
```

#### For Grid Layouts:
- **"X-COLUMN GRID"** - Specify exact grid structure
- **"RESPONSIVE GRID LAYOUT"** - Always include exact breakpoint classes
- **Layout Pattern**: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4`
- Include responsive behavior: "1 column mobile → 2 columns tablet → 3 columns desktop"

#### For Progress Indicators:
- **"CIRCULAR PROGRESS INDICATORS"** - For circular progress
- **"HORIZONTAL PROGRESS BARS"** - For linear progress
- **"PROGRESS BARS SHOWING PERCENTAGE"** - For percentage displays
- Include component structure:
```tsx
<Progress value={percentage} className="h-2 mb-1" />
<CircularProgress value={98.5} label="Completeness" />
```

#### For Card Layouts:
- **"METRIC CARDS WITH LARGE NUMBERS"** - For dashboard metrics
- **"DETAILED CARD LAYOUT"** - For complex card content
- **Structure with exact sections**: Header, Content, Footer
```tsx
<Card className="hover:shadow-lg transition-shadow">
  <CardHeader>...</CardHeader>
  <CardContent>...</CardContent>
  <CardFooter>...</CardFooter>
</Card>
```

#### For Dashboard Layouts:
- **"DASHBOARD LAYOUT"** - For multi-section dashboards
- **"METRIC DASHBOARD"** - For statistics displays
- **"INTERACTIVE CHART DASHBOARD"** - For chart-heavy interfaces

## 4. Data Display Elements
For each data element shown:
- Field name and data type
- Source (API endpoint/database table)
- Format and validation rules
- Empty/null state handling
- Loading states
- Error states

## 5. Interactive Features
Document all interactions WITH SPECIFIC UI PATTERNS:
- Buttons (label, action, destination) WITH STATE DESCRIPTIONS
- Forms (fields, validation, submission) WITH EXACT FORM STRUCTURE
- Filters and search functionality WITH UI COMPONENT SPECIFICATIONS
- Sort options WITH INTERACTION PATTERNS
- Hover states and tooltips WITH EXACT STYLING
- Click/tap behaviors WITH VISUAL FEEDBACK DESCRIPTIONS
- Swipe gestures (if mobile)
- Keyboard shortcuts

### CRITICAL: Include Exact Interactive Patterns

#### For Tab Navigation:
- **"TAB NAVIGATION - X-TAB STRUCTURE"**
- **Default State**: "Opens on 'TabName' tab"
- **Visual Feedback**: "Active tab highlighting with underline"
- **Interaction**: "Click to switch between analysis views"

#### For Button States:
- **"STATE BUTTONS"** - For buttons that change appearance
- **Visual States**: 
  - Default: "Button Text" 
  - Selected: "✓ Selected" with success styling
- **State Changes**: Describe exact state transitions

#### For Chart Interactions:
- **"INTERACTIVE CHART CONTROLS"**
- **"CHART TOOLBAR"** - For chart control interfaces
- Include specific controls: zoom, pan, export, toggle series

## 6. Navigation Elements
- Links to other screens
- Back/forward navigation
- Breadcrumbs
- Tab navigation
- Modal/drawer triggers

## 7. Dynamic Behaviors
- Real-time updates
- Auto-refresh intervals
- Animations and transitions
- Conditional rendering logic
- Progressive disclosure patterns

## 8. State Management
- Component states (loading, empty, error, success)
- User interaction states
- Data refresh triggers
- Cache requirements
- Offline behavior

## 9. API Requirements
- Endpoints needed
- Data structure expected
- Pagination details
- Filter/sort parameters
- WebSocket/real-time connections

## 10. Business Logic
- Calculations or data transformations
- Validation rules
- Permission-based visibility
- Conditional workflows
- Data dependencies

## 11. Accessibility Requirements
- ARIA labels needed
- Keyboard navigation flow
- Screen reader considerations
- Focus management
- Color contrast requirements

## 12. Performance Considerations
- Lazy loading requirements
- Image optimization needs
- Data pagination limits
- Cache strategies
- Bundle size concerns

## 13. Edge Cases
- Empty states
- Error scenarios
- Maximum data limits
- Timeout handling
- Offline functionality

## 14. Sample Data Structure
Provide JSON example of the data structure this screen expects

## 15. Implementation Notes
- Specific libraries recommended
- Complex implementation areas
- Potential technical challenges
- Performance optimization opportunities
- Testing considerations

## 16. UI PATTERN REFERENCE (MANDATORY FOR COMPLEX INTERFACES)

### Common UI Patterns That MUST Be Specified Explicitly:

#### A. Tabbed Interfaces:
```markdown
### Component Name - TABBED INTERFACE
- Component: shadcn/ui Tabs with **EXACT X-TAB STRUCTURE**
- **TAB SEQUENCE: Tab1 → Tab2 → Tab3 → Tab4**
- **Implementation**:
```tsx
<Tabs defaultValue="tab1" className="w-full">
  <TabsList className="grid w-full grid-cols-4">
    <TabsTrigger value="tab1">Tab1</TabsTrigger>
    <TabsTrigger value="tab2">Tab2</TabsTrigger>
    <TabsTrigger value="tab3">Tab3</TabsTrigger>
    <TabsTrigger value="tab4">Tab4</TabsTrigger>
  </TabsList>
  <TabsContent value="tab1">{/* Content */}</TabsContent>
</Tabs>
```

#### B. Progress Indicators:
```markdown
### Component Name - PROGRESS INDICATORS
- **Circular Progress**: `<CircularProgress value={98.5} label="Completeness" />`
- **Linear Progress**: `<Progress value={percentage} className="h-2 mb-1" />`
- **Horizontal Progress Bars**: For category breakdowns with percentages
```

#### C. Grid Layouts:
```markdown
### Component Name - RESPONSIVE GRID LAYOUT
- **Layout Pattern**: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4`
- **Responsive Behavior**: 1 column mobile → 2 columns tablet → 3 columns desktop
```

#### D. Dashboard Layouts:
```markdown
### Component Name - DASHBOARD LAYOUT
- **Summary Section**: Metric cards with large numbers
- **Main Content**: Interactive charts and visualizations
- **Sidebar**: Filters and controls
```

#### E. Chart Interfaces:
```markdown
### Component Name - INTERACTIVE CHART DASHBOARD
- **Chart Container**: `h-96 w-full` (384px height)
- **Chart Controls**: Toolbar with zoom, export, toggle controls
- **Implementation**: Recharts components with exact specifications
```

### REQUIRED DOCUMENTATION FOR EACH PATTERN:
1. **Exact Component Structure** - Complete TSX implementation
2. **Specific CSS Classes** - No generic layout descriptions
3. **Responsive Behavior** - Exact breakpoint specifications
4. **Visual Hierarchy** - Exact spacing, sizing, colors
5. **Interactive States** - Button states, hover effects, selection states

## 17. DOCUMENTATION QUALITY CHECKLIST

Before completing any screen documentation, verify ALL items:

### ✅ UI Pattern Specification:
- [ ] All tabbed interfaces labeled as "TABBED INTERFACE with EXACT X-TAB STRUCTURE"
- [ ] Tab sequences specified: "Tab1 → Tab2 → Tab3"
- [ ] Grid layouts include exact CSS classes: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4`
- [ ] Progress indicators specified: "CIRCULAR PROGRESS" or "HORIZONTAL PROGRESS BARS"
- [ ] Dashboard layouts labeled as "DASHBOARD LAYOUT" with section breakdown

### ✅ Implementation Code:
- [ ] TSX code examples provided for complex UI patterns
- [ ] shadcn/ui component structure specified
- [ ] Exact CSS classes documented (no generic descriptions)
- [ ] Responsive behavior specified with breakpoint classes

### ✅ Visual Elements:
- [ ] Specific visual components described (progress bars, charts, indicators)
- [ ] Icon specifications included
- [ ] Color coding and styling specified
- [ ] Hover states and transitions documented

### ✅ Layout Structure:
- [ ] Exact grid specifications provided
- [ ] Responsive breakpoint behavior detailed
- [ ] Spacing and padding classes specified
- [ ] Component hierarchy clearly defined

### ✅ Interaction Patterns:
- [ ] Button states documented (default, selected, hover)
- [ ] Chart interaction controls specified
- [ ] Tab navigation behavior described
- [ ] Form interaction patterns detailed

### ❌ REJECT Documentation If It Contains:
- [ ] Generic terms like "statistics dashboard" without specifics
- [ ] Vague UI descriptions without exact patterns
- [ ] Missing tab counts or sequences
- [ ] No implementation code for complex patterns
- [ ] Generic "responsive layout" without exact classes

**Only approve documentation that eliminates ALL ambiguity and provides exact implementation guidance.**

Format the output as a well-structured Markdown file with:
- Clear headers and subheaders
- Bullet points for lists
- Code blocks for data structures AND UI IMPLEMENTATIONS
- Tables where appropriate
- Bold text for important elements
- Comments using <!-- --> for developer notes

## CRITICAL DOCUMENTATION REQUIREMENTS TO PREVENT GENERIC IMPLEMENTATIONS:

### 1. ALWAYS Use Explicit UI Pattern Keywords:
- **"TABBED INTERFACE"** instead of "interface with tabs"
- **"X-TAB STRUCTURE"** instead of "multiple tabs"
- **"RESPONSIVE GRID LAYOUT"** instead of "grid layout"
- **"CIRCULAR PROGRESS INDICATORS"** instead of "progress indicators"
- **"DASHBOARD LAYOUT"** instead of "layout with sections"

### 2. ALWAYS Include Exact Implementation Code:
```tsx
// Include specific shadcn/ui component structure
<Tabs defaultValue="overview">
  <TabsList className="grid w-full grid-cols-5">
    <TabsTrigger value="overview">Overview</TabsTrigger>
    <TabsTrigger value="descriptive">Descriptive</TabsTrigger>
    <TabsTrigger value="temporal">Temporal</TabsTrigger>
    <TabsTrigger value="categorical">Categorical</TabsTrigger>
    <TabsTrigger value="quality">Quality</TabsTrigger>
  </TabsList>
</Tabs>
```

### 3. ALWAYS Specify Exact CSS Classes:
- Grid layouts: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4`
- Responsive spacing: `space-y-4`, `space-y-6`, `space-y-8`
- Card styling: `hover:shadow-lg transition-shadow duration-200`

### 4. ALWAYS Include Visual Structure Descriptions:
```markdown
### Component Name - UI PATTERN TYPE
- Component: shadcn/ui Component with **EXACT STRUCTURE**
- **Layout Pattern**: `specific CSS classes`
- **Visual Elements**: Specific progress bars, charts, indicators
- **Implementation**: [Code example]
```

### 5. MANDATORY Sections for Complex UI:
- **Exact Tab Sequence**: "Tab1 → Tab2 → Tab3"
- **Specific Component Structure**: Complete component hierarchy
- **Responsive Behavior**: Exact breakpoint classes
- **Visual Hierarchy**: Exact spacing and sizing

### 6. Prevent Generic Descriptions By:
- Never using vague terms like "statistics dashboard"
- Always specifying exact number of tabs, columns, cards
- Including complete component implementation examples
- Describing exact visual elements (progress bars, charts, icons)

Be extremely detailed and specific with EXACT UI PATTERNS. This document will be the single source of truth for developers implementing this screen and must eliminate any ambiguity that leads to generic implementations.