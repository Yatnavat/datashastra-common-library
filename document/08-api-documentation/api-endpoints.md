# Dynamic Forecasting API Endpoints Documentation

## Overview

This document outlines the API implementation for the Dynamic Forecasting Engine feature in the STOCKSENSE Angular application. The implementation follows existing patterns in the codebase and provides comprehensive endpoints for managing demand forecasting activities and workflows.

## Architecture Alignment

### Consistency with Existing STOCKSENSE Patterns

The Dynamic Forecasting implementation maintains consistency with the existing codebase architecture:

1. **Service Injection Pattern**: Uses Angular's `inject()` function following the same pattern as `ReportService`
2. **State Management**: Integrates with `StateService` for configuration management
3. **HTTP Client Usage**: Follows identical patterns for API calls with proper error handling
4. **Observable Pattern**: Returns RxJS Observables consistent with existing services
5. **Component Structure**: Standalone components with PrimeNG imports matching dashboard patterns
6. **Routing**: Lazy-loaded feature module following protected route structure

### Component Architecture

```typescript
// Follows existing dashboard component pattern
@Component({
  selector: 'app-activity-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    I18NextModule,
    UserAccessDirective,
    // PrimeNG components matching existing usage
    CardModule,
    ButtonModule,
    InputTextModule,
    // ... other imports
  ],
  templateUrl: './activity-dashboard.component.html',
  styleUrls: ['./activity-dashboard.component.scss']
})
```

## API Endpoint Structure

### Base Configuration

```typescript
// Service follows existing pattern from ReportService
@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  private http = inject(HttpClient);
  private state = inject(StateService);
  private appConfig = this.state.getOption('appConfig');
  
  // Base URL follows existing API structure
  private readonly baseUrl = `${this.appConfig.apiUrl}/api/demand-forecasting`;
}
```

## Core API Endpoints

### 1. Activity Management

#### GET /api/demand-forecasting/activities
**Purpose**: Retrieve activities with filtering and pagination  
**Parameters**:
- `search` (string): Search term for activity names, clients, projects
- `status` (string[]): Filter by activity status (Active, Completed, On-hold, Draft)
- `priority` (string[]): Filter by priority (High, Medium, Low)
- `client` (string[]): Filter by client names
- `project` (string[]): Filter by project names
- `industry` (string[]): Filter by industry classification
- `dateFrom` (string): Start date for creation date filter (ISO format)
- `dateTo` (string): End date for creation date filter (ISO format)
- `sortBy` (string): Sort field (name, createdAt, lastModified, priority, status)
- `sortOrder` (string): Sort direction (asc, desc)

**Response**:
```typescript
{
  data: Activity[],
  success: boolean,
  message: string
}
```

**Implementation Pattern** (follows ReportService):
```typescript
getActivities(filters?: ActivityFilters): Observable<ApiResponse<Activity[]>> {
  let params = new HttpParams();
  
  if (filters) {
    if (filters.search) params = params.set('search', filters.search);
    if (filters.status?.length) params = params.set('status', filters.status.join(','));
    // ... other parameters
  }

  return this.http.get<ApiResponse<Activity[]>>(`${this.baseUrl}/activities`, { params });
}
```

#### GET /api/demand-forecasting/activities/paginated
**Purpose**: Get paginated activities for large datasets  
**Parameters**:
- `page` (number): Page number (1-based)
- `limit` (number): Items per page
- Plus all filters from above endpoint

**Response**:
```typescript
{
  data: Activity[],
  pagination: {
    currentPage: number,
    totalPages: number,
    pageSize: number,
    totalCount: number,
    hasNext: boolean,
    hasPrevious: boolean
  },
  totalCount: number,
  hasMore: boolean
}
```

#### GET /api/demand-forecasting/activities/{id}
**Purpose**: Get specific activity by ID  
**Response**: Single Activity object with complete details

#### POST /api/demand-forecasting/activities
**Purpose**: Create new activity  
**Request Body**:
```typescript
{
  name: string,           // Required, 3-100 characters
  client: string,         // Required
  project: string,        // Required
  description?: string,   // Optional, max 500 characters
  industry?: string,      // Optional
  priority: ActivityPriority  // Required (High, Medium, Low)
}
```

**Response**:
```typescript
{
  activity: Activity,
  success: boolean,
  message: string
}
```

#### PUT /api/demand-forecasting/activities/{id}
**Purpose**: Update existing activity  
**Request Body**: Partial Activity object with updatable fields

#### DELETE /api/demand-forecasting/activities/{id}
**Purpose**: Archive/delete activity (soft delete)  
**Validation**: Cannot delete activities with active forecasts

### 2. Dashboard Statistics

#### GET /api/demand-forecasting/dashboard/statistics
**Purpose**: Get dashboard overview statistics  
**Response**:
```typescript
{
  data: {
    totalActivities: number,
    totalForecasts: number,
    activeActivities: number,
    uniqueClients: number,
    trends: {
      activitiesGrowth: number,    // Percentage change
      forecastsGrowth: number,
      clientsGrowth: number,
      accuracyTrend: number
    }
  },
  success: boolean,
  message: string
}
```

### 3. Filter Options

#### GET /api/demand-forecasting/filter-options
**Purpose**: Get available filter options for dropdowns  
**Response**:
```typescript
{
  data: {
    statuses: ActivityStatus[],
    priorities: ActivityPriority[],
    clients: string[],
    projects: string[],
    industries: string[]
  },
  success: boolean,
  message: string
}
```

### 4. Version Management

#### GET /api/demand-forecasting/activities/{id}/versions
**Purpose**: Get version history for an activity  
**Response**:
```typescript
{
  data: {
    versions: ForecastVersion[],
    totalVersions: number,
    latestVersion: string
  },
  success: boolean,
  message: string
}
```

#### POST /api/demand-forecasting/activities/{id}/clone
**Purpose**: Clone existing activity  
**Request Body**:
```typescript
{
  name: string  // New activity name
}
```

## Workflow-Specific Endpoints

### 5. Data Upload (Step 1)

#### POST /api/demand-forecasting/activities/{id}/forecasts/{forecastId}/upload
**Purpose**: Upload data file for forecasting  
**Content-Type**: multipart/form-data  
**Form Data**:
- `file`: File (CSV/Excel, max 100MB)
- `uploadType`: string (raw | preprocessed)

**Response**:
```typescript
{
  fileId: string,
  validation: DataValidation,
  preview: DataPreview,
  success: boolean,
  message: string
}
```

#### GET /api/demand-forecasting/data-schema
**Purpose**: Get required data schema and constraints  
**Response**: DataSchema object with required/optional columns

### 6. Data Analysis (Step 2)

#### GET /api/demand-forecasting/forecasts/{forecastId}/analysis
**Purpose**: Get exploratory data analysis results  
**Response**: DataAnalysisStep object with all analysis tabs

#### POST /api/demand-forecasting/forecasts/{forecastId}/analysis/refresh
**Purpose**: Trigger re-analysis of uploaded data

### 7. Model Selection (Step 3)

#### GET /api/demand-forecasting/models/available
**Purpose**: Get available models with recommendations  
**Response**: ModelSelectionStep object

#### POST /api/demand-forecasting/forecasts/{forecastId}/model
**Purpose**: Select model for forecasting  
**Request Body**:
```typescript
{
  modelType: ModelType,
  parameters?: any
}
```

### 8. Feature Engineering (Step 4)

#### GET /api/demand-forecasting/forecasts/{forecastId}/features/available
**Purpose**: Get available features for selection

#### POST /api/demand-forecasting/forecasts/{forecastId}/features
**Purpose**: Configure feature selection  
**Request Body**: FeatureConfiguration object

### 9. Aggregation (Step 5)

#### POST /api/demand-forecasting/forecasts/{forecastId}/aggregation
**Purpose**: Configure aggregation settings  
**Request Body**: AggregationConfiguration object

### 10. Date Ranges (Step 6)

#### POST /api/demand-forecasting/forecasts/{forecastId}/date-ranges
**Purpose**: Configure training/testing periods  
**Request Body**: DateRangeConfiguration object

### 11. Model Training (Step 7)

#### POST /api/demand-forecasting/forecasts/{forecastId}/train
**Purpose**: Start model training  
**Request Body**: ModelTrainingParams object

#### GET /api/demand-forecasting/forecasts/{forecastId}/training/progress
**Purpose**: Get real-time training progress  
**Response**: TrainingProgress object

#### WebSocket: /ws/training/{forecastId}
**Purpose**: Real-time training updates

### 12. Results (Step 8)

#### GET /api/demand-forecasting/forecasts/{forecastId}/results
**Purpose**: Get forecast results and visualizations  
**Response**: ForecastResults object

#### POST /api/demand-forecasting/forecasts/{forecastId}/export
**Purpose**: Export results in various formats  
**Request Body**:
```typescript
{
  format: 'csv' | 'excel' | 'json' | 'pdf',
  includeVisualizations: boolean,
  includeRawData: boolean,
  includeMetadata: boolean
}
```

## Mock Data Implementation

### Development Strategy

The implementation includes comprehensive mock data services for development and testing:

```typescript
@Injectable({
  providedIn: 'root'
})
export class MockDataService {
  // Generates realistic mock activities with proper relationships
  generateMockActivities(count: number = 20): Activity[]
  
  // Provides dashboard statistics with trends
  generateMockStatistics(): DashboardStatistics
  
  // Creates realistic data validation results
  generateMockValidation(fileName: string, isValid: boolean): DataValidation
  
  // Provides data preview with statistics
  generateMockDataPreview(): DataPreview
}
```

### Mock Data Features

1. **Realistic Data Generation**: Mock activities with proper client-project relationships
2. **Dynamic Statistics**: Trending data with realistic growth percentages
3. **Validation Simulation**: Configurable validation results for testing error scenarios
4. **Data Preview**: Sample data with column statistics and data quality metrics
5. **Workflow Simulation**: Step-by-step progress tracking with status updates

## Error Handling

### Consistent Error Response Format

```typescript
interface ApiErrorResponse {
  success: false,
  message: string,
  errors?: string[]
}
```

### Error Handling Pattern (Following Existing Service)

```typescript
private handleError(error: any): Observable<never> {
  console.error('API Error:', error);
  
  let errorMessage = 'An unexpected error occurred';
  if (error.error?.message) {
    errorMessage = error.error.message;
  } else if (error.message) {
    errorMessage = error.message;
  }

  return throwError(() => ({
    success: false,
    message: errorMessage,
    errors: error.error?.errors || [errorMessage]
  }));
}
```

## Authentication & Authorization

### Access Control

Following the existing `userAccessGuard` pattern:

```typescript
// In routing configuration
{
  path: 'dashboard',
  component: ActivityDashboardComponent,
  canActivate: [userAccessGuard],
  data: {
    requiredScope: 'demand_forecasting_view'
  }
}
```

### Required Scopes

- `demand_forecasting_view`: View activities and dashboard
- `demand_forecasting_edit`: Create/edit activities and forecasts
- `demand_forecasting_delete`: Delete/archive activities
- `demand_forecasting_admin`: Administrative functions

## Performance Considerations

### Pagination Strategy

```typescript
// Infinite scrolling implementation
getActivitiesPaginated(page: number = 1, limit: number = 20): Observable<PaginatedResponse<Activity>>

// Virtual scrolling for large datasets
// Implements lazy loading with 20 items per page
// Caching strategy: 5 minutes for activities, 1 minute for statistics
```

### Caching Implementation

Following existing patterns with appropriate cache durations:

- **Activities List**: 5 minutes cache with ETag support
- **Dashboard Statistics**: 1 minute cache with background refresh
- **Filter Options**: 15 minutes cache (relatively static data)
- **User Preferences**: localStorage persistence

### Real-time Updates

WebSocket integration for live updates:

```typescript
// Activity updates
interface ActivityUpdateEvent {
  activityId: string,
  field: string,
  oldValue: any,
  newValue: any,
  updatedBy: string
}

// Training progress
interface TrainingProgressEvent {
  forecastId: string,
  progress: TrainingProgress,
  metrics: Partial<TrainingMetrics>
}
```

## Data Models

### Key Interfaces

The implementation includes comprehensive TypeScript interfaces:

1. **Core Models**: Activity, Forecast, WorkflowStep
2. **Workflow Steps**: DataUploadStep, DataAnalysisStep, ModelSelectionStep, etc.
3. **API Responses**: ApiResponse, PaginatedResponse, CreateActivityResponse
4. **Configuration**: FeatureConfiguration, AggregationConfiguration, DateRangeConfiguration
5. **Results**: ForecastResults, TrainingMetrics, ForecastVisualization

### Schema Validation

Data upload validation with fixed schema:

```typescript
// Required columns (as per product requirements)
const REQUIRED_COLUMNS = ['Date', 'SKUID', 'Depot', 'Demand', 'Region'];

// Constraints
const CONSTRAINTS = {
  maxFileSize: 100 * 1024 * 1024, // 100MB
  supportedFormats: ['csv', 'xlsx', 'xls'],
  maxSKUs: 5,
  maxDepots: 5,
  minRows: 100,
  maxRows: 1000000
};
```

## Integration Points

### Existing Service Integration

The implementation integrates with existing STOCKSENSE services:

1. **StateService**: Configuration and app state management
2. **UserAccessService**: Role-based access control
3. **I18NextModule**: Internationalization support
4. **InterceptService**: HTTP request/response handling

### PrimeNG Component Usage

Consistent with existing dashboard implementation:

- CardModule, ButtonModule, InputTextModule
- DropdownModule, BadgeModule, DialogModule
- SkeletonModule, TooltipModule, ProgressBarModule
- Same styling patterns and responsive design

## Testing Strategy

### Mock Service Configuration

```typescript
// Environment-based mock toggle
private readonly useMockData = environment.production ? false : true;

// Comprehensive mock scenarios
- Success responses with realistic data
- Error scenarios (validation, network, permission errors)
- Loading states with configurable delays
- WebSocket simulation for real-time updates
```

### Unit Testing

Test structure following Angular best practices:

```typescript
describe('ActivityService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ActivityService, MockDataService]
    });
  });

  it('should retrieve activities with filters', () => {
    // Test implementation
  });
});
```

## Deployment Considerations

### Environment Configuration

```typescript
// Production API URL configuration
"apiUrl": "https://api.stocksense.com"

// Development with mock data
"apiUrl": "http://localhost:3000"
"useMockData": true
```

### Feature Flags

Support for gradual rollout:

```typescript
// Feature flag integration
if (this.appConfig.features?.demandForecasting?.enabled) {
  // Load demand forecasting module
}
```

## Future Enhancements

### Planned API Extensions

1. **Batch Operations**: Bulk activity management
2. **Templates**: Activity templates for common scenarios
3. **Collaboration**: Multi-user activity editing
4. **Advanced Analytics**: Cross-activity insights
5. **Integration APIs**: External data source connectors

### Scalability Considerations

1. **Microservice Architecture**: Separate forecasting service
2. **Message Queues**: Async processing for large datasets
3. **Caching Layer**: Redis for high-frequency data
4. **CDN Integration**: Static asset optimization

## Documentation Maintenance

This API documentation should be updated when:

1. New endpoints are added
2. Request/response schemas change
3. Authentication requirements change
4. Performance characteristics are modified
5. Error handling patterns are updated

The implementation follows STOCKSENSE architectural patterns and provides a solid foundation for the Dynamic Forecasting Engine feature while maintaining consistency with the existing codebase.

---

**Implementation Status**: ✅ Complete - Core API structure, mock data, routing, and components implemented
**Testing Status**: 🔄 In Progress - Mock services ready for integration testing
**Documentation Status**: ✅ Complete - Comprehensive API documentation provided