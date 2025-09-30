# Enhanced Spring Boot Demand Forecasting Service - Implementation Prompt (Gradle + PostgreSQL)

You are an expert backend developer tasked with creating a comprehensive Spring Boot 3.4.6 service project for demand forecasting with Java 21. This service will expose REST APIs for forecasting operations and chart data visualization, integrating with the **datashastra-common-library** for shared components.

## Project Context & Documentation

You have access to the following documentation that defines the complete requirements:

1. **@document\Library-Usage-Prompt.md** - Common library implementation guidelines and utilities that must be integrated
2. **@document\demand-forecasting-api-reference.md** - Complete API specifications for all endpoints you need to implement
3. **@document\mock-data-entities.md** - Data models and mock data structures for entities
4. **@document\chart-json-documentation.md** - Chart response format and structure specifications
5. **@document\API-Guidelines.md** - API design standards and best practices to follow
6. **@document\Initial-prompt.txt** - Base reference for project structure and approach

## Enhanced Implementation Requirements

### 1. DataShastra Common Library Integration (MANDATORY)

**From Library-Usage-Prompt.md - Follow these requirements exactly:**

#### Gradle Dependency Configuration
```gradle
// build.gradle
plugins {
    id 'org.springframework.boot' version '3.4.6'
    id 'io.spring.dependency-management' version '1.1.4'
    id 'java'
    id 'org.sonarqube' version '4.4.1.3373'
}

group = 'tech.oorjaa.datashastra'
version = '0.0.1-SNAPSHOT'
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/Yatnavat/datashastra-common-library")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    // DataShastra Common Library (MANDATORY)
    implementation 'tech.oorjaa:datashastra-common-library:0.0.1-SNAPSHOT'

    // Spring Boot Starters
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'

    // PostgreSQL Database
    runtimeOnly 'org.postgresql:postgresql'

    // MapStruct
    implementation 'org.mapstruct:mapstruct:1.5.5.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'

    // OpenAPI Documentation
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'

    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
    testImplementation 'com.h2database:h2'
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:postgresql'
}

// Gradle Tasks for Development
task refreshCommonLibrary {
    doLast {
        exec {
            workingDir '../datashastra-common-library'
            commandLine './gradlew', 'clean', 'build', 'publishToMavenLocal'
        }
    }
}

// Auto-refresh common library before build
build.dependsOn refreshCommonLibrary

tasks.named('test') {
    useJUnitPlatform()
}
```

#### MANDATORY Common Library Usage Patterns
- **ALL API responses MUST use StandardResponse<T>** wrapper from common library
- **ALL entities MUST extend BaseEntity** for audit support (created/modified dates and users)
- **Use shared DTOs** where applicable (ErrorResponse, UserDto, etc.)
- **Enable JPA Auditing** with @EnableJpaAuditing annotation
- **Import and use shared User and Tenant entities** for multi-tenancy support

### 2. PostgreSQL Database Configuration

#### Application Configuration
```yaml
# application.yml
spring:
  application:
    name: datashastra-forecasting-service
  profiles:
    active: local

  # PostgreSQL Configuration
  datasource:
    url: jdbc:postgresql://localhost:5432/datashastra_forecasting
    username: ${DB_USERNAME:datashastra}
    password: ${DB_PASSWORD:password}
    driver-class-name: org.postgresql.Driver
    hikari:
      connection-timeout: 60000
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      max-lifetime: 1200000

  # JPA Configuration for PostgreSQL
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate
      naming:
        physical-strategy: org.hibernate.boot.model.naming.SnakeCasePhysicalNamingStrategy
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        jdbc:
          time_zone: UTC
        temp:
          use_jdbc_metadata_defaults: false

  # Liquibase for Database Migrations
  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.xml

# Enable JPA Auditing (MANDATORY for BaseEntity)
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when_authorized

logging:
  level:
    tech.oorjaa.datashastra: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
```

#### Environment-Specific Configurations
```yaml
# application-local.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/datashastra_forecasting_local
    username: datashastra_user
    password: local_password
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

# application-dev.yml
spring:
  datasource:
    url: jdbc:postgresql://dev-db-server:5432/datashastra_forecasting_dev
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate

# application-stage.yml
spring:
  datasource:
    url: jdbc:postgresql://stage-db-server:5432/datashastra_forecasting_stage
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate

# application-test.yml (for testing with H2)
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
  h2:
    console:
      enabled: true
```

### 3. Database Schema & Liquibase Migration

#### Liquibase Master Changelog
```xml
<!-- src/main/resources/db/changelog/db.changelog-master.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
    http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <include file="db/changelog/001-create-activities-table.xml"/>
    <include file="db/changelog/002-create-forecasts-table.xml"/>
    <include file="db/changelog/003-create-data-uploads-table.xml"/>
    <include file="db/changelog/004-create-model-configurations-table.xml"/>
    <include file="db/changelog/005-create-training-sessions-table.xml"/>
    <include file="db/changelog/006-create-forecast-results-table.xml"/>
    <include file="db/changelog/999-insert-initial-data.xml"/>
</databaseChangeLog>
```

#### Sample Table Creation
```xml
<!-- src/main/resources/db/changelog/001-create-activities-table.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
    http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <changeSet id="001-create-activities-table" author="datashastra">
        <createTable tableName="activities">
            <column name="id" type="BIGSERIAL">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="tenant_id" type="BIGINT">
                <constraints nullable="false"/>
            </column>
            <column name="name" type="VARCHAR(255)">
                <constraints nullable="false"/>
            </column>
            <column name="client" type="VARCHAR(255)">
                <constraints nullable="false"/>
            </column>
            <column name="project" type="VARCHAR(255)">
                <constraints nullable="false"/>
            </column>
            <column name="description" type="TEXT"/>
            <column name="industry" type="VARCHAR(100)"/>
            <column name="priority" type="VARCHAR(20)">
                <constraints nullable="false"/>
            </column>
            <column name="status" type="VARCHAR(20)">
                <constraints nullable="false"/>
            </column>
            <column name="tags" type="TEXT"/>
            <column name="created_date" type="TIMESTAMP WITH TIME ZONE">
                <constraints nullable="false"/>
            </column>
            <column name="last_modified_date" type="TIMESTAMP WITH TIME ZONE">
                <constraints nullable="false"/>
            </column>
            <column name="created_by" type="VARCHAR(255)"/>
            <column name="last_modified_by" type="VARCHAR(255)"/>
        </createTable>

        <createIndex tableName="activities" indexName="idx_activities_tenant_id">
            <column name="tenant_id"/>
        </createIndex>

        <createIndex tableName="activities" indexName="idx_activities_status">
            <column name="status"/>
        </createIndex>
    </changeSet>
</databaseChangeLog>
```

### 4. Project Architecture & Structure

```
src/main/java/tech/oorjaa/datashastra/forecasting/
├── ForecastingServiceApplication.java  (Main application class)
├── controller/                          (REST endpoints - ALL must return StandardResponse<T>)
│   ├── ActivityController.java
│   ├── DashboardController.java
│   ├── DataUploadController.java
│   ├── ModelConfigurationController.java
│   ├── TrainingController.java
│   ├── ResultsController.java
│   └── ConfigurationController.java
├── service/                            (Service orchestration layer)
│   ├── ActivityService.java
│   ├── DashboardService.java
│   ├── DataValidationService.java
│   ├── ModelConfigurationService.java
│   ├── TrainingService.java
│   └── ResultsService.java
├── business/                           (Core business logic for forecasting)
│   ├── ForecastingEngine.java
│   ├── DataAnalysisEngine.java
│   ├── ModelTrainingEngine.java
│   └── ChartDataGenerator.java
├── model/                              (Request/Response DTOs with Jakarta validation)
│   ├── entity/                         (JPA entities extending BaseEntity)
│   │   ├── Activity.java
│   │   ├── Forecast.java
│   │   ├── DataUpload.java
│   │   ├── ModelConfiguration.java
│   │   ├── TrainingSession.java
│   │   └── ForecastResult.java
│   ├── dto/                           (DTOs)
│   │   ├── request/
│   │   │   ├── ActivityCreateRequest.java
│   │   │   ├── ActivityUpdateRequest.java
│   │   │   ├── TrainingRequest.java
│   │   │   └── ConfigurationRequest.java
│   │   └── response/
│   │       ├── ActivityResponse.java
│   │       ├── DashboardStatisticsResponse.java
│   │       ├── ValidationResultsResponse.java
│   │       ├── TrainingProgressResponse.java
│   │       ├── ForecastResultsResponse.java
│   │       └── ChartResponse.java
├── mapper/                             (MapStruct mappers)
│   ├── ActivityMapper.java
│   ├── ForecastMapper.java
│   ├── DataUploadMapper.java
│   └── ConfigurationMapper.java
├── repository/                         (JPA repositories)
│   ├── ActivityRepository.java
│   ├── ForecastRepository.java
│   ├── DataUploadRepository.java
│   ├── ModelConfigurationRepository.java
│   ├── TrainingSessionRepository.java
│   └── ForecastResultRepository.java
├── exception/                          (Custom exceptions and global handler)
│   ├── ForecastingException.java
│   ├── DataValidationException.java
│   ├── ModelTrainingException.java
│   └── GlobalExceptionHandler.java
├── config/                             (Configuration classes)
│   ├── DatabaseConfig.java
│   ├── SecurityConfig.java
│   ├── OpenApiConfig.java
│   ├── MapperConfig.java
│   └── AuditConfig.java
└── util/                              (Utility classes)
    ├── SecurityUtils.java
    ├── DateUtils.java
    └── ValidationUtils.java

src/main/resources/
├── application.yml                     (Base configuration)
├── application-local.yml               (Local development)
├── application-dev.yml                 (Development environment)
├── application-stage.yml               (Staging environment)
├── application-test.yml                (Testing configuration)
└── db/
    └── changelog/
        ├── db.changelog-master.xml
        ├── 001-create-activities-table.xml
        ├── 002-create-forecasts-table.xml
        ├── 003-create-data-uploads-table.xml
        ├── 004-create-model-configurations-table.xml
        ├── 005-create-training-sessions-table.xml
        ├── 006-create-forecast-results-table.xml
        └── 999-insert-initial-data.xml
```

### 5. Core Implementation Requirements

#### A. Main Application Class
```java
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@OpenAPIDefinition(
    info = @Info(
        title = "DataShastra Demand Forecasting API",
        version = "1.0.0",
        description = "REST APIs for demand forecasting operations and chart data visualization"
    )
)
public class ForecastingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForecastingServiceApplication.class, args);
    }
}
```

#### B. Entity Design (Following BaseEntity Pattern)
```java
// MANDATORY: All entities must extend BaseEntity from common library
@Entity
@Table(name = "activities")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Add @TenantId for multi-tenant support
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Activity name is required")
    @Size(max = 255, message = "Activity name must not exceed 255 characters")
    private String name;

    @Column(name = "client", nullable = false)
    @NotBlank(message = "Client is required")
    private String client;

    @Column(name = "project", nullable = false)
    @NotBlank(message = "Project is required")
    private String project;

    @Column(name = "description")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Column(name = "industry")
    private String industry;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActivityStatus status;

    @Column(name = "tags")
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Forecast> forecasts;

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    public enum ActivityStatus {
        ACTIVE, COMPLETED, DRAFT
    }
}
```

#### C. Controller Pattern (MANDATORY StandardResponse Usage)
```java
@RestController
@RequestMapping("/api/demand-forecasting")
@Tag(name = "Demand Forecasting", description = "APIs for demand forecasting operations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/dashboard/statistics")
    @Operation(summary = "Get dashboard statistics",
               description = "Retrieve overview statistics for the dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StandardResponse<DashboardStatisticsResponse>> getDashboardStatistics() {
        log.debug("Getting dashboard statistics");
        DashboardStatisticsResponse stats = activityService.getDashboardStatistics();
        StandardResponse<DashboardStatisticsResponse> response =
            StandardResponse.success(stats, "Dashboard statistics retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activities")
    @Operation(summary = "Get paginated list of activities")
    public ResponseEntity<StandardResponse<PaginatedResponse<ActivityResponse>>> getActivities(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority) {

        log.debug("Getting activities with page={}, limit={}, client={}, status={}, priority={}",
                  page, limit, client, status, priority);

        PaginatedResponse<ActivityResponse> activities = activityService.getActivities(
            page, limit, client, status, priority);

        StandardResponse<PaginatedResponse<ActivityResponse>> response =
            StandardResponse.success(activities, "Activities retrieved successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/activities")
    @Operation(summary = "Create new activity")
    public ResponseEntity<StandardResponse<ActivityResponse>> createActivity(
            @Valid @RequestBody ActivityCreateRequest request) {

        log.debug("Creating activity: {}", request.getName());
        ActivityResponse created = activityService.createActivity(request);
        StandardResponse<ActivityResponse> response =
            StandardResponse.success(created, "Activity created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ALL endpoints must follow this pattern with StandardResponse<T>
}
```

#### D. Service Layer Pattern
```java
@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final UserContextService userContextService; // From common library
    private final SecurityUtils securityUtils;

    @Transactional(readOnly = true)
    public DashboardStatisticsResponse getDashboardStatistics() {
        Long tenantId = userContextService.getCurrentTenantId();

        long totalActivities = activityRepository.countByTenantId(tenantId);
        long totalForecasts = activityRepository.countForecastsByTenantId(tenantId);
        long activeActivities = activityRepository.countByTenantIdAndStatus(
            tenantId, Activity.ActivityStatus.ACTIVE);
        long uniqueClients = activityRepository.countUniqueClientsByTenantId(tenantId);

        return DashboardStatisticsResponse.builder()
            .totalActivities(totalActivities)
            .totalForecasts(totalForecasts)
            .activeActivities(activeActivities)
            .uniqueClients(uniqueClients)
            .trends(calculateTrends())
            .build();
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<ActivityResponse> getActivities(
            int page, int limit, String client, String status, String priority) {

        Long tenantId = userContextService.getCurrentTenantId();
        Pageable pageable = PageRequest.of(page - 1, limit,
            Sort.by(Sort.Direction.DESC, "lastModifiedDate"));

        Page<Activity> activitiesPage = activityRepository.findActivitiesWithFilters(
            tenantId, client, status, priority, pageable);

        List<ActivityResponse> activities = activitiesPage.getContent()
            .stream()
            .map(activityMapper::toResponse)
            .toList();

        return PaginatedResponse.<ActivityResponse>builder()
            .items(activities)
            .totalCount(activitiesPage.getTotalElements())
            .page(page)
            .limit(limit)
            .hasNext(activitiesPage.hasNext())
            .hasPrevious(activitiesPage.hasPrevious())
            .build();
    }

    // Business orchestration methods
}
```

### 6. PostgreSQL-Specific Repository Implementation

```java
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // Multi-tenant queries
    List<Activity> findByTenantIdOrderByLastModifiedDateDesc(Long tenantId);

    Optional<Activity> findByIdAndTenantId(Long id, Long tenantId);

    long countByTenantId(Long tenantId);

    long countByTenantIdAndStatus(Long tenantId, Activity.ActivityStatus status);

    @Query("SELECT COUNT(DISTINCT a.client) FROM Activity a WHERE a.tenantId = :tenantId")
    long countUniqueClientsByTenantId(@Param("tenantId") Long tenantId);

    @Query("SELECT COUNT(f) FROM Activity a JOIN a.forecasts f WHERE a.tenantId = :tenantId")
    long countForecastsByTenantId(@Param("tenantId") Long tenantId);

    // Dynamic filtering with PostgreSQL optimizations
    @Query(value = """
        SELECT a FROM Activity a
        WHERE a.tenantId = :tenantId
        AND (:client IS NULL OR LOWER(a.client) LIKE LOWER(CONCAT('%', :client, '%')))
        AND (:status IS NULL OR a.status = :status)
        AND (:priority IS NULL OR a.priority = :priority)
        """)
    Page<Activity> findActivitiesWithFilters(
        @Param("tenantId") Long tenantId,
        @Param("client") String client,
        @Param("status") String status,
        @Param("priority") String priority,
        Pageable pageable
    );

    // PostgreSQL-specific full-text search
    @Query(value = """
        SELECT * FROM activities a
        WHERE a.tenant_id = :tenantId
        AND to_tsvector('english', a.name || ' ' || a.description || ' ' || a.client)
        @@ plainto_tsquery('english', :searchTerm)
        ORDER BY ts_rank(to_tsvector('english', a.name || ' ' || a.description || ' ' || a.client),
                         plainto_tsquery('english', :searchTerm)) DESC
        """, nativeQuery = true)
    List<Activity> findByFullTextSearch(@Param("tenantId") Long tenantId,
                                      @Param("searchTerm") String searchTerm);
}
```

### 7. API Implementation Requirements

**From demand-forecasting-api-reference.md - Implement ALL endpoints:**

#### Core Endpoints (MANDATORY)
- Dashboard APIs (`/api/demand-forecasting/dashboard/*`)
- Activity Management APIs (`/api/demand-forecasting/activities/*`)
- Data Upload & Validation APIs (`/api/demand-forecasting/activities/{id}/upload`)
- Model Configuration APIs (`/api/demand-forecasting/models`, `/api/demand-forecasting/features`)
- Training & Execution APIs (`/api/demand-forecasting/activities/{id}/train`)
- Results & Analysis APIs (`/api/demand-forecasting/activities/{id}/results`)
- Configuration APIs (`/api/demand-forecasting/aggregation-config`)
- Version Management APIs (`/api/demand-forecasting/activities/{id}/forecasts`)
- Lookup Data APIs (`/api/demand-forecasting/clients`, `/api/demand-forecasting/projects`)

#### Response Format (MANDATORY)
ALL responses must follow StandardResponse<T> pattern:
```java
// Success Response
StandardResponse<ActivityDto> response = StandardResponse.success(
    activityData,
    "Activity retrieved successfully"
);

// Pagination Response
StandardResponse<PaginatedResponse<ActivityDto>> response = StandardResponse.success(
    paginatedData,
    "Activities retrieved successfully"
);

// Error Response (handled by Global Exception Handler)
StandardResponse<Void> response = StandardResponse.error(
    "Validation failed",
    validationErrors
);
```

### 8. Chart Data Implementation

**From chart-json-documentation.md - Implement chart response structures:**

#### Chart Response DTOs
```java
@Data
@Builder
public class ChartResponseDto {
    private String title;
    private String subtitle;
    private String chartType; // "line", "bar", "scatter", "pie"
    private ChartData chartData;
}

@Data
@Builder
public class ChartData {
    private List<String> labels;
    private List<ChartDataset> datasets;
}

@Data
@Builder
public class ChartDataset {
    private String label;
    private List<Number> data;
    private String borderColor;
    private String backgroundColor;
    private Integer borderWidth;
    private Double tension;
    private Boolean fill;
    private Integer pointRadius;
    private Integer pointHoverRadius;
    private List<Integer> borderDash;
    private String yAxisID;
    private String type; // For mixed charts
}
```

### 9. Exception Handling (Following Common Library Patterns)

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardResponse<Map<String, String>>> handleValidation(
            ValidationException ex, HttpServletRequest request) {

        StandardResponse<Map<String, String>> response = StandardResponse.error(
            "Validation failed",
            ex.getValidationErrors(),
            request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        StandardResponse<Void> response = StandardResponse.error(
            "Resource not found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ForecastingException.class)
    public ResponseEntity<StandardResponse<Void>> handleForecastingException(
            ForecastingException ex, HttpServletRequest request) {

        log.error("Forecasting operation failed", ex);
        StandardResponse<Void> response = StandardResponse.error(
            "Forecasting operation failed",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<Map<String, String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));

        StandardResponse<Map<String, String>> response = StandardResponse.error(
            "Validation failed",
            errors,
            request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(response);
    }

    // Follow StandardResponse pattern for all exception handling
}
```

### 10. Development Commands & Testing

#### Gradle Development Tasks
```gradle
// Additional tasks in build.gradle

// Database tasks
task createLocalDb(type: Exec) {
    commandLine 'createdb', '-h', 'localhost', '-U', 'postgres', 'datashastra_forecasting_local'
}

task dropLocalDb(type: Exec) {
    commandLine 'dropdb', '-h', 'localhost', '-U', 'postgres', 'datashastra_forecasting_local'
}

// Development workflow
task devSetup {
    dependsOn 'refreshCommonLibrary'
    dependsOn 'createLocalDb'
    dependsOn 'bootRun'
}

// Testing with PostgreSQL testcontainers
test {
    useJUnitPlatform()
    systemProperty 'spring.profiles.active', 'test'
    testLogging {
        events "passed", "skipped", "failed"
        exceptionFormat "full"
    }
}
```

#### PostgreSQL Integration Tests
```java
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ActivityRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    void testMultiTenantQuery() {
        // Test tenant isolation with PostgreSQL
        Activity activity1 = createTestActivity(1L, "Tenant 1 Activity");
        Activity activity2 = createTestActivity(2L, "Tenant 2 Activity");

        activityRepository.saveAll(List.of(activity1, activity2));

        List<Activity> tenant1Activities = activityRepository.findByTenantIdOrderByLastModifiedDateDesc(1L);
        List<Activity> tenant2Activities = activityRepository.findByTenantIdOrderByLastModifiedDateDesc(2L);

        assertThat(tenant1Activities).hasSize(1);
        assertThat(tenant2Activities).hasSize(1);
        assertThat(tenant1Activities.get(0).getName()).isEqualTo("Tenant 1 Activity");
        assertThat(tenant2Activities.get(0).getName()).isEqualTo("Tenant 2 Activity");
    }
}
```

#### Controller Tests
```java
@WebMvcTest(ActivityController.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @Test
    void getDashboardStatistics_ShouldReturnStandardResponse() throws Exception {
        // Given
        DashboardStatisticsResponse stats = DashboardStatisticsResponse.builder()
            .totalActivities(10L)
            .totalForecasts(25L)
            .activeActivities(5L)
            .uniqueClients(3L)
            .build();

        when(activityService.getDashboardStatistics()).thenReturn(stats);

        // When & Then
        mockMvc.perform(get("/api/demand-forecasting/dashboard/statistics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Dashboard statistics retrieved successfully"))
                .andExpect(jsonPath("$.data.totalActivities").value(10))
                .andExpect(jsonPath("$.data.totalForecasts").value(25))
                .andExpect(jsonPath("$.data.activeActivities").value(5))
                .andExpect(jsonPath("$.data.uniqueClients").value(3));
    }

    @Test
    void createActivity_ShouldReturnStandardResponse() throws Exception {
        // Given
        ActivityCreateRequest request = ActivityCreateRequest.builder()
            .name("Test Activity")
            .client("Test Client")
            .project("Test Project")
            .priority("HIGH")
            .build();

        ActivityResponse response = ActivityResponse.builder()
            .id(1L)
            .name("Test Activity")
            .client("Test Client")
            .project("Test Project")
            .priority("HIGH")
            .status("DRAFT")
            .build();

        when(activityService.createActivity(any(ActivityCreateRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/demand-forecasting/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpected(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Activity created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Activity"));
    }
}
```

### 11. Development Commands

```bash
# Build and run
./gradlew clean build
./gradlew bootRun

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Refresh common library and build
./gradlew refreshCommonLibrary
./gradlew clean build --refresh-dependencies

# Database operations
./gradlew createLocalDb
./gradlew bootRun --args='--spring.profiles.active=local'

# Testing
./gradlew test
./gradlew test --tests "*ActivityServiceTest*"

# Build Docker image
./gradlew bootBuildImage

# Generate dependency report
./gradlew dependencies

# Database migration
./gradlew update
```

### 12. Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 13. Critical Implementation Notes

1. **NEVER create entities without extending BaseEntity** - Mandatory for audit support
2. **ALWAYS use StandardResponse<T>** for ALL API responses - No exceptions
3. **Implement multi-tenancy** using @TenantId annotation on entities with PostgreSQL row-level security
4. **Use Liquibase** for database schema management and migrations
5. **Follow PostgreSQL naming conventions** with snake_case column names
6. **Implement proper indexing** for tenant_id and frequently queried columns
7. **Use PostgreSQL-specific features** like full-text search where beneficial
8. **Configure connection pooling** properly for production environments
9. **Implement database health checks** via Spring Actuator
10. **Use Testcontainers** for integration testing with real PostgreSQL instances
11. **Follow API-Guidelines.md** for RESTful conventions and layered architecture
12. **Use mock data from mock-data-entities.md** for in-memory implementation
13. **Generate chart responses** following chart-json-documentation.md structure
14. **Enable JPA Auditing** in main application class with @EnableJpaAuditing
15. **Use MapStruct** for all entity-DTO conversions following common library patterns

### 14. Deliverables

Provide complete, production-ready code including:

1. **build.gradle** with all dependencies and PostgreSQL configuration
2. **Liquibase migration scripts** for database schema
3. **All application.yml** files for different environments
4. **Main Application Class** with proper annotations (@EnableJpaAuditing, etc.)
5. **All Controller Classes** implementing demand-forecasting-api-reference.md endpoints
6. **Service and Business Logic Layers** with proper transaction management
7. **Entity Classes** extending BaseEntity with multi-tenancy support
8. **DTO Classes** with Jakarta validation annotations
9. **Mapper Interfaces** using MapStruct
10. **Repository Interfaces** with PostgreSQL-specific queries
11. **Exception Handling** with GlobalExceptionHandler
12. **Configuration Classes** (Security, Database, OpenAPI)
13. **Integration tests** using Testcontainers with PostgreSQL
14. **Unit tests** for controllers and services
15. **README.md** with PostgreSQL setup and usage instructions

### 15. Final Implementation Checklist

- [ ] **Common Library Integration**: All entities extend BaseEntity, all responses use StandardResponse<T>
- [ ] **PostgreSQL Configuration**: Proper database setup with connection pooling and Liquibase
- [ ] **Multi-Tenancy**: @TenantId annotations and tenant-aware queries
- [ ] **API Endpoints**: All endpoints from demand-forecasting-api-reference.md implemented
- [ ] **Chart Data**: Responses following chart-json-documentation.md structure
- [ ] **Exception Handling**: GlobalExceptionHandler with StandardResponse error format
- [ ] **Security**: JWT authentication with Keycloak integration
- [ ] **Testing**: Unit tests and integration tests with Testcontainers
- [ ] **Documentation**: OpenAPI/Swagger documentation for all endpoints
- [ ] **Validation**: Jakarta validation on all request DTOs
- [ ] **Logging**: Proper logging configuration and usage
- [ ] **Monitoring**: Spring Actuator endpoints for health checks

Begin implementation ensuring strict adherence to the DataShastra common library integration patterns and the StandardResponse<T> wrapper for ALL API responses.