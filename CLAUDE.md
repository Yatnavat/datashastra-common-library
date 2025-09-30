# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**datashastra-common-library** is a comprehensive Java library built with Spring Boot 3.5.4 and Java 21, designed to provide shared components for DataShastra microservices. This library contains reusable JPA entities, DTOs, mappers, repositories, constants, and utilities that can be consumed by multiple Spring Boot applications within the DataShastra ecosystem.

**Key Library Features:**
- **Core Entities**: Common JPA entities with audit support (BaseEntity, User, Tenant)
- **Forecasting Domain**: Complete 15-entity model for Business Intelligence and Predictive Analytics
- **Standardized DTOs**: Response models, error handling, authentication, and forecasting DTOs
- **MapStruct Mappers**: Type-safe bi-directional mapping utilities with enrichment
- **JPA Repositories**: 160+ custom queries for efficient data access
- **Domain Enums**: 8 type-safe enumerations for forecasting workflows
- **Constants**: Shared application constants and service definitions
- **Global Support**: Multi-timezone, multi-currency, GDPR compliance
- **Performance**: Strategic indexing for sub-500ms response times

**Library Usage:**
This library is published to GitHub Packages and can be imported as a dependency in other DataShastra projects. It eliminates code duplication across microservices and ensures consistent data models and response formats.

## Development Commands

### Build and Publish
```bash
# Build the library
./gradlew build

# Publish to local Maven repository
./gradlew publishToMavenLocal

# Publish to GitHub Packages
./gradlew publish

# Clean build directory
./gradlew clean
```

### Testing
```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test

# Compile test classes only
./gradlew compileTestJava
```

## Library Components

This library is organized into the following packages under `tech.oorjaa.datashastra`:

### Core Entities (`entity/`)
- **BaseEntity**: Abstract base class providing audit fields (created/modified dates and users)
- **User**: User entity with Keycloak integration and tenant support
- **Tenant**: Multi-tenant support entity

### Forecasting Entities (`entity/`) - NEW
- **Activity**: Core forecasting activity with multi-tenant support and global configurations
- **Forecast**: Individual forecast instances with 8-step workflow tracking (Draft → In Progress → Completed)
- **Models**: ML model definitions with capabilities, resource requirements, and performance metrics
- **UploadedData**: File metadata with processing status, data quality metrics, and GDPR compliance
- **TransformedData**: Processed data with transformation history and quality assessments
- **ExploratoryDataAnalytics**: EDA results with statistical summaries and data insights
- **ValidationResults**: Data validation assessments with error/warning tracking
- **FeatureConfiguration**: Feature engineering configurations for model training
- **WorkflowStep**: Workflow progress tracking across 8-step forecasting process
- **TrainingProgress**: Real-time training monitoring with resource utilization
- **AggregationConfiguration**: Data aggregation settings for time series
- **DateRangeConfiguration**: Date range specifications for forecasting
- **ForecastResult**: Forecast outputs with performance metrics and visualizations
- **SystemConfiguration**: Global system settings and preferences
- **NotificationSettings**: User notification preferences and delivery settings

### Data Transfer Objects (`dto/`)
- **StandardResponse<T>**: Generic response wrapper with success/error states
- **ErrorResponse**: Standardized error response format
- **UserDto**: User data transfer object
- **LoginRequest/LoginResponse**: Authentication DTOs
- **RefreshTokenRequest**: Token refresh DTO
- **KeycloakTokenResponse**: Keycloak integration DTO
- **CompanyDto**: Company/organization DTO

### Forecasting DTOs (`dto/`) - NEW
- **ActivityDto**: Rich activity data with UI display fields, validation, and computed properties
- **ForecastDto**: Complete forecast data with 8-step workflow state and performance metrics
- **ModelsDto**: Model information with capabilities, suitability scoring, and usage statistics
- **UploadedDataDto**: File metadata with processing status, quality indicators, and security flags

### Mappers (`mapper/`)
- **UserMapper**: MapStruct mapper for User entity/DTO conversion
- **CompanyMapper**: MapStruct mapper for Company entity/DTO conversion

### Forecasting Mappers (`mapper/`) - NEW
- **ActivityMapper**: Bi-directional Activity entity/DTO mapping with enrichment and UI helpers
- **ForecastMapper**: Complex forecast transformations with workflow state management
- **ModelsMapper**: Model configuration and capability mapping with performance scoring
- **UploadedDataMapper**: File metadata and processing status mapping with security handling

### Constants (`constant/`)
- **ServiceConstants**: Shared application constants and service definitions

### Enums (`enums/`) - NEW
- **ActivityStatus**: DRAFT, ACTIVE, COMPLETED, ON_HOLD, ARCHIVED with business logic
- **ActivityPriority**: HIGH, MEDIUM, LOW with display names and priority levels
- **ForecastStatus**: DRAFT, IN_PROGRESS, COMPLETED, FAILED, CANCELLED with state management
- **ModelType**: ARIMA, PROPHET, XGBOOST, LSTM, LIGHTGBM with model categorization
- **ModelComplexity**: LOW, MEDIUM, HIGH with performance characteristics
- **UploadDataType**: CSV, EXCEL, JSON, API with file type handling
- **TrainingStatus**: INITIALIZING, PREPARING_DATA, TRAINING, VALIDATING, etc.
- **WorkflowStepType**: DATA_UPLOAD, ANALYSIS, MODEL_SELECTION, FEATURES, etc.

### Repositories (`repository/`) - NEW
- **ActivityRepository**: 50+ queries for activity management, dashboard statistics, and search
- **ForecastRepository**: 45+ queries for version control, performance tracking, and analytics
- **ModelsRepository**: 25+ cached queries for model selection, recommendations, and filtering
- **UploadedDataRepository**: 40+ queries for file management, quality assessment, and cleanup

### Additional Components
- **Configuration classes**: Common Spring configuration beans
- **Exception handling**: Standardized error response structures
- **Utilities**: Helper classes for common operations

### Key Patterns

**Entity-DTO Mapping**
- Uses MapStruct for type-safe mapping between entities and DTOs
- Mappers are in `mapper/` package, configured as Spring components
- Password fields are explicitly ignored in DTO mappings for security

**Exception Handling**
- Custom exceptions extend appropriate base exceptions
- Global exception handler provides consistent error responses
- Error responses follow standardized format with `ErrorResponse.java`

**Multi-Profile Configuration**
- `application.yml` - Base configuration
- `application-local.yml` - Local development settings
- `application-dev.yml` - Development environment
- `application-stage.yml` - Staging environment

## Library Integration

### Adding to Your Project

**Gradle (build.gradle):**
```gradle
dependencies {
    implementation 'tech.oorjaa:datashastra-common-library:0.0.1-SNAPSHOT'
}
```

**Maven (pom.xml):**
```xml
<dependency>
    <groupId>tech.oorjaa</groupId>
    <artifactId>datashastra-common-library</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### GitHub Packages Authentication
This library is published to GitHub Packages. Configure your build tool with appropriate credentials:

**Gradle:**
```gradle
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/Yatnavat/datashastra-common-library")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

### Usage Examples

**Using StandardResponse:**
```java
@RestController
public class MyController {
    @GetMapping("/users")
    public StandardResponse<List<UserDto>> getUsers() {
        List<UserDto> users = userService.getAllUsers();
        return StandardResponse.success(users, "Users retrieved successfully");
    }
}
```

**Extending BaseEntity:**
```java
@Entity
@Table(name = "my_entity")
public class MyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Other fields...
}
```

**Using Forecasting Components (NEW):**
```java
// Activity Management
@Service
public class ForecastingService {
    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private ActivityMapper activityMapper;

    public StandardResponse<ActivityDto> createActivity(ActivityDto request) {
        Activity activity = activityMapper.toEntity(request);
        activity.setTenantId(getCurrentTenantId());
        activity = activityRepository.save(activity);
        return StandardResponse.success(
            activityMapper.toDto(activity), 
            "Activity created successfully"
        );
    }
    
    // Use repository custom queries
    public StandardResponse<List<ActivityDto>> getHighPriorityActivities() {
        Integer tenantId = getCurrentTenantId();
        List<Activity> activities = activityRepository
            .findHighPriorityActiveActivities(tenantId);
        return StandardResponse.success(
            activityMapper.toDtoList(activities),
            "High priority activities retrieved"
        );
    }
}

// Model Selection with Caching
@Service
public class ModelSelectionService {
    @Autowired
    private ModelsRepository modelsRepository;
    
    @Cacheable("recommended-models")
    public List<ModelsDto> getRecommendedModels(int dataPoints) {
        var models = modelsRepository.findSuitableForDataSize(dataPoints);
        return modelsMapper.toDtoList(models);
    }
}

// 8-Step Workflow Management
@Service
public class ForecastWorkflowService {
    @Autowired
    private ForecastRepository forecastRepository;
    
    public ForecastDto advanceWorkflowStep(Long forecastId) {
        Forecast forecast = forecastRepository
            .findByIdAndTenantId(forecastId, getTenantId())
            .orElseThrow();
            
        // Advance to next step
        int currentStep = forecast.getCurrentStep();
        if (currentStep < 8) {
            forecast.setCurrentStep(currentStep + 1);
            
            // Update status when reaching training
            if (currentStep == 7) {
                forecast.setStatus(ForecastStatus.IN_PROGRESS);
            }
        }
        
        forecast = forecastRepository.save(forecast);
        return forecastMapper.toDto(forecast);
    }
}
```

## Database Integration

**Connection**: This library provides entities that work with PostgreSQL databases

**Auditing**: All entities extending BaseEntity automatically track creation/modification timestamps and user information

**Multi-tenancy**: Entities support tenant-based data isolation using `tenantId` fields

**Global Support (NEW)**: 
- **Multi-timezone**: UTC storage with timezone-aware conversion
- **Multi-currency**: BigDecimal precision with currency codes (baseCurrency, forecastCurrency)
- **Multi-locale**: Locale-specific formatting and localization support
- **Data Residency**: Configurable data residency regions for compliance
- **GDPR Compliance**: Legal hold flags, retention policies, and audit trails

**Performance Optimizations (NEW)**:
- **Strategic Indexing**: Composite indexes for sub-500ms query performance
- **Query Optimization**: 160+ custom repository queries with efficient JOINs
- **Caching**: @Cacheable annotations on frequently accessed model data
- **Lazy Loading**: Optimized entity relationships to prevent N+1 problems
- **Batch Processing**: Support for bulk operations and data processing

## Security Notes

- Password fields are explicitly ignored in DTO mappings for security
- Entities support Keycloak integration for authentication
- Standardized error responses prevent information leakage
- Input validation using Jakarta validation annotations

## Library Development Guidelines

### Adding New Components

**New Entities:**
- Always extend `BaseEntity` for audit support
- Use appropriate validation annotations (`@NotBlank`, `@Email`, `@DecimalMin`, etc.)
- Include `tenantId` field for multi-tenant isolation
- Add strategic indexes for performance (`@Index` annotations)
- Follow naming conventions and table mappings
- Include global support fields (currency, timezone, locale) when applicable
- Use JSON/JSONB fields for flexible schema storage

**New DTOs:**
- Create corresponding DTOs for entities with UI-friendly fields
- Include computed properties and business logic methods
- Add validation annotations for input validation
- Use MapStruct mappers for type-safe conversion
- Follow the `StandardResponse<T>` pattern for API responses
- Exclude sensitive fields (passwords, internal IDs) from DTOs
- Include display fields (badge colors, formatted text) for UI components

**New Mappers:**
- Use MapStruct for entity-DTO conversion with `componentModel = "spring"`
- Configure `nullValuePropertyMappingStrategy = IGNORE` and `unmappedTargetPolicy = IGNORE`
- Explicitly ignore sensitive fields and computed properties in mappings
- Use `@AfterMapping` and `@BeforeMapping` for complex transformations
- Handle nested object mappings and collections appropriately
- Avoid referencing non-existent entity fields

**New Repositories:**
- Extend `JpaRepository` and `JpaSpecificationExecutor` for flexibility
- Create tenant-aware queries with `tenantId` parameters
- Use `@Query` annotations for complex JPQL queries
- Implement `@Modifying` queries for updates and bulk operations
- Add proper indexing hints and optimization strategies
- Use native queries for PostgreSQL-specific features (full-text search)
- Cache frequently accessed data with `@Cacheable`

### Library Publishing

**Version Management:**
- Follow semantic versioning (MAJOR.MINOR.PATCH)
- Update version in `build.gradle` before publishing
- Create git tags for releases

**Publishing Process:**
```bash
# 1. Update version in build.gradle
# 2. Build and test
./gradlew clean build test

# 3. Publish to local for testing
./gradlew publishToMavenLocal

# 4. Publish to GitHub Packages
./gradlew publish
```

### Consumer Project Integration

When other projects consume this library:
- Import only necessary components to minimize dependencies
- Configure Spring Data JPA auditing with `@EnableJpaAuditing`
- Set up proper PostgreSQL database configuration for entities
- Configure MapStruct annotation processing in build.gradle
- Handle multi-tenancy with tenant-aware queries and security
- **For Forecasting Features:**
  - Enable Spring caching with `@EnableCaching` for model repositories
  - Configure timezone handling for global datetime fields
  - Set up currency conversion if using multi-currency features
  - Implement data residency controls for GDPR compliance
  - Configure file upload handling for UploadedData entities
  - Set up proper indexing strategies for large datasets
  - Implement batch processing for data transformation workflows

### Best Practices

- **Backward Compatibility**: Maintain API compatibility when adding new features
- **Documentation**: Update CLAUDE.md when adding new components
- **Testing**: Add comprehensive tests for new functionality
- **Security**: Never expose sensitive data in DTOs or responses
- **Dependencies**: Keep library dependencies minimal and well-documented

## Library Summary (Current Version: 0.0.1-SNAPSHOT)

### 📊 Component Statistics
- **15 JPA Entities**: Complete forecasting domain model with multi-tenant support
- **8 Domain Enums**: Type-safe enumerations for workflows and configurations
- **4 Rich DTOs**: UI-ready data transfer objects with validation and computed fields
- **4 MapStruct Mappers**: Bi-directional entity-DTO conversions with enrichment
- **4 JPA Repositories**: 160+ custom queries for efficient data operations
- **Global Support**: Multi-timezone, multi-currency, GDPR-compliant architecture
- **Performance**: Strategic indexing for sub-500ms response times

### 🎯 Key Use Cases
1. **Business Intelligence Platforms**: Complete forecasting workflow management
2. **Predictive Analytics Services**: ML model selection and training pipelines
3. **Data Processing Applications**: File upload, validation, and transformation
4. **Multi-tenant SaaS**: Tenant-isolated data with global compliance
5. **Dashboard Applications**: Rich DTOs with UI display helpers and computed metrics

### 🚀 Benefits for Consumer Projects
- **Rapid Development**: Pre-built domain model eliminates months of entity design
- **Consistent APIs**: Standardized response formats and error handling
- **Performance**: Optimized queries and caching strategies built-in
- **Compliance Ready**: GDPR, data residency, and audit trail support
- **Global Scale**: Multi-timezone, multi-currency, multi-locale support
- **Type Safety**: MapStruct mappers prevent runtime mapping errors
- **Maintainability**: Centralized business logic and validation rules

### 📈 Development Workflow
1. **Local Development**: `./gradlew publishToMavenLocal` for immediate testing
2. **Consumer Update**: `./gradlew build --refresh-dependencies` to pick up changes
3. **Production Release**: Version increment + `./gradlew publish` to GitHub Packages
4. **Documentation**: Update CLAUDE.md and Library-Usage-Prompt.md for new features

This library serves as the foundation for all DataShastra microservices, providing a robust, scalable, and compliant architecture for business intelligence and predictive analytics applications.