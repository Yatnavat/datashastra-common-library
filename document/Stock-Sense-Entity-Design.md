# Stock Sense Entity Design - Complete Implementation Guide

## 📊 Entity Relationship Diagram with @TenantId

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│     Tenant      │     │      User       │     │   BaseEntity    │
│  (Common Lib)   │     │  (Common Lib)   │     │  (Common Lib)   │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ id (PK)         │     │ id (PK)         │     │ createdDate     │
│ name            │     │ email           │     │ modifiedDate    │
│ legalName       │     │ firstName       │     │ createdBy       │
│ contactEmail    │     │ lastName        │     │ modifiedBy      │
│ keycloakEnabled │     │                 │     └─────────────────┘
│ keycloakServerUrl│     │                 │             ▲
│ keycloakRealm   │     │                 │             │
│ keycloakClientId│     │                 │             │ extends
│ keycloakSecret  │     │                 │             │
│ keycloakAdminUser│    │                 │             │
│ keycloakAdminPwd│     │                 │             │
└─────────────────┘     └─────────────────┘
         │                       │                       ▲
         │                       │                       │
         │                       │                       │ extends
         ▼                       │                       │
┌─────────────────┐              │              ┌─────────────────┐
│     Client      │◄─────────────┘              │     Project     │
├─────────────────┤                             ├─────────────────┤
│ id (PK)         │                             │ id (PK)         │
│ @TenantId       │                             │ clientId (FK)   │
│ tenantId        │                             │ @TenantId       │
│ clientCode (UK) │                             │ tenantId        │
│ name            │◄────────────────────────────┤ projectCode(UK) │
│ email           │ 1:N                         │ name            │
│ phoneNumber     │                             │ industry (ENUM) │
│ status (ENUM)   │                             │ priority (ENUM) │
│ primaryContact  │                             │ status (ENUM)   │
└─────────────────┘                             └─────────────────┘
                                                          │
                                                          │ 1:N
                                                          ▼
                                                ┌─────────────────┐
                                                │    Activity     │
                                                ├─────────────────┤
                                                │ id (PK)         │
                                                │ projectId (FK)  │
                                                │ @TenantId       │
                                                │ tenantId        │
                                                │ activityCode(UK)│
                                                │ name            │
                                                │ description     │
                                                │ priority (ENUM) │
                                                │ forecastCount   │
                                                │ schemaReqs(JSON)│
                                                │ templateUrl     │
                                                │ status (ENUM)   │
                                                └─────────────────┘
                                                          │
                                                          │ 1:N
                                                          ▼
                        ┌─────────────────┐
                        │    Forecast     │
                        ├─────────────────┤
                        │ id (PK)         │
                        │ activityId (FK) │
                        │ @TenantId       │
                        │ tenantId        │
                        │ name            │
                        │ description     │
                        │ status (ENUM)   │
                        │ forecastType    │
                        │ modelId (FK)    │
                        │ uploadedDataId  │
                        └─────────────────┘
                                 │
                        ┌────────┼────────────────┐
                        │        │                │
                        ▼        ▼                ▼
              ┌─────────────┐ ┌─────────────┐ ┌─────────────────┐
              │  Features   │ │ Aggregation │ │DateRangeConfig  │
              ├─────────────┤ ├─────────────┤ ├─────────────────┤
              │ id (PK)     │ │ id (PK)     │ │ id (PK)         │
              │ forecastId  │ │ forecastId  │ │ forecastId (FK) │
              │ @TenantId   │ │ @TenantId   │ │ @TenantId       │
              │ tenantId    │ │ tenantId    │ │ tenantId        │
              │ features    │ │ geoLevels   │ │ trainingStart   │
              │ status      │ │ prodLevels  │ │ trainingEnd     │
              └─────────────┘ │ tempLevels  │ │ testingStart    │
                              │ thresholds  │ │ testingEnd      │
                              │ status      │ │ status (ENUM)   │
                              └─────────────┘ └─────────────────┘

                        ▼                    ▼
              ┌─────────────────┐      ┌─────────────────┐
              │ ExploratoryEDA  │      │ ForecastResult  │
              ├─────────────────┤      ├─────────────────┤
              │ id (PK)         │      │ id (PK)         │
              │ forecastId (FK) │      │ forecastId (FK) │
              │ @TenantId       │      │ @TenantId       │
              │ tenantId        │      │ tenantId        │
              │ dataOverview    │      │ modelAnalysis   │
              │ dataQuality     │      │ performanceSumm │
              │ temporalAnalysis│      │ historicalFit   │
              │ distributions   │      │ accuracy        │
              │ correlations    │      │ status (ENUM)   │
              │ status (ENUM)   │      └─────────────────┘
              └─────────────────┘

┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│     Model       │     │  UploadedData   │     │ValidationResult │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ id (PK)         │     │ id (PK)         │     │ id (PK)         │
│ @TenantId       │     │ activityId (FK) │────►│ uploadedDataId  │
│ tenantId        │     │ @TenantId       │     │ @TenantId       │
│ modelName       │     │ tenantId        │     │ tenantId        │
│ description     │     │ fileName        │     │ totalRows       │
│ modelType(ENUM) │     │ fileUrl         │     │ missingValues   │
│ complexity      │     │ fileSize        │     │ detectedOutlier │
│ accuracy        │     │ uploadTimestamp │     │ outlierColumns  │
│ estTime         │     │ uploadType(ENUM)│     │ additionalCols  │
│ bestFor         │     │ validationStatus│     │ validationPassed│
│ isRecommended   │     │ schemaReqs(JSON)│     │ status (ENUM)   │
│ status (ENUM)   │     └─────────────────┘     └─────────────────┘
└─────────────────┘              │
                                 │ 1:1
                                 ▼
                        ┌─────────────────┐
                        │ TransformedData │
                        ├─────────────────┤
                        │ id (PK)         │
                        │ uploadedDataId  │
                        │ @TenantId       │
                        │ tenantId        │
                        │ transformConfig │
                        │ transformedUrl  │
                        │ transformTime   │
                        │ status (ENUM)   │
                        └─────────────────┘
```

## 🎯 Final Development Prompt

```
I'm working on Stock Sense for DataShastra - a demand forecasting and analytics SaaS platform.

Technical Stack:
- Java 21
- Spring Boot 3.5.4
- PostgreSQL 15 with JSONB support
- Spring Data JPA with Hibernate 6.6.3
- Multi-tenant architecture using @TenantId annotation
- datashastra-common-library (BaseEntity, Tenant, User, StandardResponse)

Multi-Tenant SaaS Architecture:
- Tenant Isolation: @TenantId annotation for row-level security
- Data Segregation: Explicit tenantId column on ALL entities
- Service Layer: Responsible for setting tenantId from security context
- No @Formula derivations - direct tenant column approach

Entity Design Requirements:

1. **Standard Entity Structure:**
```java
@Entity
@Table(name = "entity_name")
@Getter
@Setter
public class EntityName extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "entity_code", unique = true, nullable = false)
    private String entityCode; // Business key

    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @Version
    private Long version; // For critical entities
}
```

2. **Entity Hierarchy to Create:**
   - Client (root entity with direct tenant relationship)
   - Project (belongs to Client)
   - Activity (belongs to Project)
   - Forecast (belongs to Activity)
   - UploadedData (belongs to Activity)
   - TransformedData (1:1 with UploadedData)
   - Model (global/tenant-specific ML models)
   - Features (feature engineering config)
   - Aggregation (aggregation settings)
   - DateRangeConfiguration (date range config)
   - ExploratoryDataAnalytics (EDA results)
   - ForecastResult (forecast outcomes)
   - ValidationResult (data validation results)

3. **Name-Based Identification:**
   - Client: Use meaningful names (e.g., "Acme Corporation")
   - Project: Use descriptive names (e.g., "Q4 Demand Forecasting")
   - Activity: Use clear names (e.g., "Weekly Sales Forecast")
   - Forecast: Use specific names (e.g., "December Sales Forecast")

4. **Status Enums to Define:**
```java
enum ClientStatus { ACTIVE, INACTIVE, SUSPENDED, PENDING_APPROVAL }
enum ProjectStatus { ACTIVE, INACTIVE, ON_HOLD, COMPLETED, CANCELLED }
enum ActivityStatus { CREATED, IN_PROGRESS, COMPLETED, ARCHIVED }
enum ForecastStatus { DRAFT, RUNNING, ACTIVE, COMPLETED, FAILED, CANCELLED }
enum ValidationStatus { PENDING, PASSED, FAILED, REVIEWING }
```

5. **JSON Field Structures:**
   - DataSchemaRequirement (embedded class for schema configs)
   - ValidationResults (embedded class for validation outcomes)
   - TransformationConfig (embedded class for data transformation)
   - Use @JdbcTypeCode(SqlTypes.JSON) for JSONB columns

6. **Relationship Patterns:**
   - Bidirectional relationships with proper cascade strategies
   - @OneToMany/@ManyToOne for parent-child hierarchies
   - @OneToOne for result relationships
   - LAZY fetching by default, EAGER only when necessary

7. **Integration Requirements:**
   - Extend BaseEntity from common library
   - Use existing Tenant entity for references
   - Use existing User entity for user associations
   - Follow StandardResponse pattern for API responses

8. **Service Layer Pattern:**
```java
@Service
@Transactional
public class EntityService {

    public EntityDto createEntity(CreateEntityDto dto) {
        Entity entity = new Entity();
        entity.setTenantId(getCurrentTenantId()); // Set from security context
        entity.setEntityCode(businessKeyGenerator.generateEntityCode());
        entity.setStatus(EntityStatus.ACTIVE);
        // ... other fields

        Entity saved = entityRepository.save(entity);
        return entityMapper.toDto(saved);
    }

    private Long getCurrentTenantId() {
        return Long.valueOf(TenantContext.getCurrentTenant());
    }
}
```

Please create all JPA entities following this exact pattern:
- @TenantId with explicit tenantId column (NO @Formula)
- Name-based identification (no complex business keys)
- Proper status enum management
- Structured embedded classes for JSON fields
- Bidirectional relationships with cascade strategies
- Forecast-related entities belong to Forecast entity
- Performance optimized with proper indexing
- Integration with datashastra-common-library components

Start with Client entity and work through the complete hierarchy systematically.
```

## 🏗️ Standard Entity Templates

### **Tenant Entity Template (Enhanced with Keycloak Configuration)**
```java
@Entity
@Table(name = "tenant", indexes = {
    @Index(name = "idx_tenant_name", columnList = "name"),
    @Index(name = "idx_tenant_keycloak_enabled", columnList = "keycloak_enabled")
})
@Getter
@Setter
public class Tenant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 200)
    @Column(name = "legal_name")
    private String legalName;

    @Email
    @Column(name = "contact_email")
    private String contactEmail;

    // Multi-Tenant Keycloak Configuration
    @Column(name = "keycloak_enabled", nullable = false)
    private Boolean keycloakEnabled = true;

    @Column(name = "keycloak_server_url", length = 500)
    private String keycloakServerUrl;

    @Column(name = "keycloak_realm", length = 100)
    private String keycloakRealm;

    @Column(name = "keycloak_client_id", length = 100)
    private String keycloakClientId;

    @Column(name = "keycloak_client_secret", length = 500)
    private String keycloakClientSecret;

    @Column(name = "keycloak_admin_username", length = 100)
    private String keycloakAdminUsername;

    @Column(name = "keycloak_admin_password", length = 500)
    private String keycloakAdminPassword;

    // Status and configuration
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TenantStatus status = TenantStatus.ACTIVE;

    // Version control for critical entity
    @Version
    private Long version;

    // Helper methods for Keycloak configuration
    public boolean isKeycloakConfigured() {
        return keycloakEnabled &&
               keycloakServerUrl != null &&
               keycloakRealm != null &&
               keycloakClientId != null;
    }

    public String buildKeycloakIssuerUrl() {
        if (!isKeycloakConfigured()) {
            return null;
        }
        return String.format("%s/realms/%s", keycloakServerUrl, keycloakRealm);
    }

    public String buildKeycloakJwkSetUrl() {
        String issuer = buildKeycloakIssuerUrl();
        return issuer != null ? issuer + "/protocol/openid-connect/certs" : null;
    }
}

public enum TenantStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    PENDING_SETUP("Pending Setup");

    private final String displayName;
    TenantStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
```

### **Root Entity Template (Client)**
```java
@Entity
@Table(name = "client", indexes = {
    @Index(name = "idx_client_tenant", columnList = "tenant_id"),
    @Index(name = "idx_client_name", columnList = "name"),
    @Index(name = "idx_client_status", columnList = "status")
})
@Getter
@Setter
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tenant isolation
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClientStatus status = ClientStatus.ACTIVE;

    // User relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_contact_id")
    private User primaryContact;

    // Relationships
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    // Version control
    @Version
    private Long version;

    // Helper methods
    public void addProject(Project project) {
        projects.add(project);
        project.setClient(this);
        project.setTenantId(this.tenantId);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.setClient(null);
    }
}
```

### **Child Entity Template (Project)**
```java
@Entity
@Table(name = "project", indexes = {
    @Index(name = "idx_project_tenant", columnList = "tenant_id"),
    @Index(name = "idx_project_client", columnList = "client_id"),
    @Index(name = "idx_project_name", columnList = "name"),
    @Index(name = "idx_project_status", columnList = "status")
})
@Getter
@Setter
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Parent relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Tenant isolation (inherited from parent)
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry")
    private Industry industry;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status = ProjectStatus.ACTIVE;

    // Child relationships
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Activity> activities = new ArrayList<>();

    // Version control
    @Version
    private Long version;

    // Helper methods
    public void addActivity(Activity activity) {
        activities.add(activity);
        activity.setProject(this);
        activity.setTenantId(this.tenantId);
    }
}
```

## 📋 Status Enums Definitions

```java
public enum ClientStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    PENDING_APPROVAL("Pending Approval");

    private final String displayName;
    ClientStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum ProjectStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ON_HOLD("On Hold"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;
    ProjectStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum ActivityStatus {
    CREATED("Created"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ARCHIVED("Archived");

    private final String displayName;
    ActivityStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum ForecastStatus {
    DRAFT("Draft"),
    RUNNING("Running"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELLED("Cancelled");

    private final String displayName;
    ForecastStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum Industry {
    RETAIL("Retail"),
    LOGISTICS("Logistics"),
    MANUFACTURING("Manufacturing"),
    HEALTHCARE("Healthcare"),
    FINANCE("Finance"),
    ECOMMERCE("E-Commerce"),
    OTHER("Other");

    private final String displayName;
    Industry(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum Priority {
    HIGH("High", 1),
    MEDIUM("Medium", 2),
    LOW("Low", 3);

    private final String displayName;
    private final int sortOrder;

    Priority(String displayName, int sortOrder) {
        this.displayName = displayName;
        this.sortOrder = sortOrder;
    }

    public String getDisplayName() { return displayName; }
    public int getSortOrder() { return sortOrder; }
}

public enum ForecastType {
    DEMAND_FORECAST("Demand Forecast"),
    INVENTORY_FORECAST("Inventory Forecast"),
    SALES_FORECAST("Sales Forecast"),
    REVENUE_FORECAST("Revenue Forecast");

    private final String displayName;
    ForecastType(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum ValidationStatus {
    PENDING("Pending"),
    PASSED("Passed"),
    FAILED("Failed"),
    REVIEWING("Under Review");

    private final String displayName;
    ValidationStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum UploadDataType {
    RAW("Raw Data"),
    PREPROCESSED("Pre-processed Data");

    private final String displayName;
    UploadDataType(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum ModelType {
    STATISTICAL("Statistical"),
    MACHINE_LEARNING("Machine Learning"),
    DEEP_LEARNING("Deep Learning"),
    HYBRID("Hybrid");

    private final String displayName;
    ModelType(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

public enum TransformationStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    FAILED("Failed");

    private final String displayName;
    TransformationStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
```

## 🗂️ JSON Field Embedded Classes

```java
@Embeddable
@Getter
@Setter
public class DataSchemaRequirement {

    @ElementCollection
    @CollectionTable(name = "schema_columns", joinColumns = @JoinColumn(name = "parent_id"))
    private List<ColumnDefinition> columns = new ArrayList<>();

    @Embedded
    private ValidationRules validationRules;

    @Column(name = "schema_version")
    private String schemaVersion;

    @Column(name = "is_strict_validation")
    private Boolean isStrictValidation = true;
}

@Embeddable
@Getter
@Setter
public class ColumnDefinition {

    @Column(name = "column_name", nullable = false)
    private String columnName;

    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "is_required")
    private Boolean isRequired = false;

    @Column(name = "constraints")
    private String constraints;

    @Column(name = "description")
    private String description;
}

@Embeddable
@Getter
@Setter
public class ValidationRules {

    @Column(name = "min_rows")
    private Integer minRows;

    @Column(name = "max_rows")
    private Integer maxRows;

    @Column(name = "allowed_null_percentage")
    private Double allowedNullPercentage;

    @Column(name = "outlier_detection_enabled")
    private Boolean outlierDetectionEnabled = true;

    @ElementCollection
    @CollectionTable(name = "validation_patterns")
    private List<String> customValidationPatterns = new ArrayList<>();
}

@Embeddable
@Getter
@Setter
public class ValidationResults {

    @Column(name = "total_rows")
    private Long totalRows;

    @Column(name = "total_columns")
    private Integer totalColumns;

    @Column(name = "missing_values_count")
    private Long missingValuesCount;

    @Column(name = "detected_outliers_count")
    private Long detectedOutliersCount;

    @ElementCollection
    @CollectionTable(name = "outlier_columns")
    private List<String> outlierColumns = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "additional_columns")
    private List<String> additionalColumns = new ArrayList<>();

    @Column(name = "validation_passed")
    private Boolean validationPassed;

    @Column(name = "validation_message", length = 1000)
    private String validationMessage;

    @Column(name = "validation_timestamp")
    private LocalDateTime validationTimestamp;
}

@Embeddable
@Getter
@Setter
public class TransformationConfig {

    @ElementCollection
    @CollectionTable(name = "transformation_steps")
    private List<TransformationStep> transformationSteps = new ArrayList<>();

    @Column(name = "transformation_type")
    private String transformationType;

    @Column(name = "configuration_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> configurationJson = new HashMap<>();
}

@Embeddable
@Getter
@Setter
public class TransformationStep {

    @Column(name = "step_name")
    private String stepName;

    @Column(name = "step_order")
    private Integer stepOrder;

    @Column(name = "step_description")
    private String stepDescription;

    @Column(name = "step_parameters", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> stepParameters = new HashMap<>();
}
```

## 🗂️ Forecast Entity with Children

```java
@Entity
@Table(name = "forecast", indexes = {
    @Index(name = "idx_forecast_tenant", columnList = "tenant_id"),
    @Index(name = "idx_forecast_activity", columnList = "activity_id"),
    @Index(name = "idx_forecast_status", columnList = "status")
})
@Getter
@Setter
public class Forecast extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private ForecastStatus status = ForecastStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    private ForecastType forecastType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_data_id")
    private UploadedData uploadedData;

    // Child entities (1:1 relationships)
    @OneToOne(mappedBy = "forecast", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Features features;

    @OneToOne(mappedBy = "forecast", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Aggregation aggregation;

    @OneToOne(mappedBy = "forecast", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DateRangeConfiguration dateRangeConfiguration;

    @OneToOne(mappedBy = "forecast", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ExploratoryDataAnalytics exploratoryDataAnalytics;

    @OneToOne(mappedBy = "forecast", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ForecastResult forecastResult;

    @Version
    private Long version;
}
```

### **Tenant Configuration Entity (Optional Multi-Tenant Settings)**
```java
@Entity
@Table(name = "tenant_configuration", indexes = {
    @Index(name = "idx_tenant_config_tenant", columnList = "tenant_id"),
    @Index(name = "idx_tenant_config_key", columnList = "key"),
    @Index(name = "idx_tenant_config_tenant_key", columnList = "tenant_id,key")
})
@Getter
@Setter
public class TenantConfiguration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "`key`", nullable = false)
    private String key;

    @Column(name = "`value`", length = 10000)
    private String value;

    @Column(name = "details", length = 1000)
    private String details;

    @Column(name = "data_type", length = 50)
    private String dataType;

    @Column(name = "data_sub_type", length = 50)
    private String dataSubType;

    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Helper methods for typed value access
    public String getStringValue() {
        return value;
    }

    public Integer getIntegerValue() {
        return value != null ? Integer.valueOf(value) : null;
    }

    public Boolean getBooleanValue() {
        return value != null ? Boolean.valueOf(value) : null;
    }

    public Double getDoubleValue() {
        return value != null ? Double.valueOf(value) : null;
    }
}
```

## 📋 Repository Interfaces

```java
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Automatic tenant filtering via @TenantId
    List<Client> findByStatus(ClientStatus status);

    Optional<Client> findByNameAndTenantId(String name, Long tenantId);

    List<Client> findByStatusOrderByNameAsc(ClientStatus status);

    @Query("SELECT COUNT(c) FROM Client c WHERE c.tenantId = :tenantId")
    long countByTenantId(@Param("tenantId") Long tenantId);
}

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    List<Forecast> findByActivityId(Long activityId);

    List<Forecast> findByStatus(ForecastStatus status);

    @Query("SELECT f FROM Forecast f WHERE f.activity.project.client.id = :clientId")
    List<Forecast> findByClientId(@Param("clientId") Long clientId);
}

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByName(String name);

    List<Tenant> findByStatus(TenantStatus status);

    List<Tenant> findByKeycloakEnabled(Boolean keycloakEnabled);

    @Query("SELECT t FROM Tenant t WHERE t.keycloakEnabled = true AND t.keycloakServerUrl IS NOT NULL")
    List<Tenant> findActiveKeycloakTenants();

    Optional<Tenant> findByKeycloakRealm(String realm);
}

@Repository
public interface TenantConfigurationRepository extends JpaRepository<TenantConfiguration, Long> {

    // Automatic tenant filtering via @TenantId
    List<TenantConfiguration> findByKey(String key);

    Optional<TenantConfiguration> findByTenantIdAndKey(Long tenantId, String key);

    List<TenantConfiguration> findByTenantIdAndIsActive(Long tenantId, Boolean isActive);

    @Query("SELECT tc FROM TenantConfiguration tc WHERE tc.tenantId = :tenantId AND tc.key LIKE :keyPattern")
    List<TenantConfiguration> findByTenantAndKeyPattern(@Param("tenantId") Long tenantId,
                                                       @Param("keyPattern") String keyPattern);

    @Modifying
    @Query("UPDATE TenantConfiguration tc SET tc.value = :value WHERE tc.tenantId = :tenantId AND tc.key = :key")
    int updateConfigValue(@Param("tenantId") Long tenantId,
                         @Param("key") String key,
                         @Param("value") String value);
}
```

## 📊 Implementation Phases

### **Phase 1: Core Business Hierarchy (Day 1)**
1. **Client Entity** - Root tenant entity with @TenantId, name-based identification, status management
2. **Project Entity** - Client child with explicit tenantId column, industry/priority enums
3. **Activity Entity** - Project child with schema requirements, template handling
4. **Forecast Entity** - Main business entity with workflow status, model relationships

### **Phase 2: Forecast Configuration Entities (Day 2)**
5. **Features Entity** - Feature engineering config belonging to specific forecast
6. **Aggregation Entity** - Geographic/Product/Temporal aggregation levels for forecast
7. **DateRangeConfiguration Entity** - Training/testing date ranges for forecast
8. **ExploratoryDataAnalytics Entity** - EDA results for specific forecast
9. **ForecastResult Entity** - Forecast outcomes and performance metrics

### **Phase 3: Data Management Entities (Day 3)**
10. **UploadedData Entity** - File management with S3 integration, validation status
11. **TransformedData Entity** - 1:1 with UploadedData, transformation configs
12. **ValidationResult Entity** - Validation outcomes belonging to UploadedData
13. **Model Entity** - ML/Statistical models with metadata, performance metrics

### **Phase 4: Supporting Components (Day 4)**
14. **DTOs and Mappers** - Data transfer objects and MapStruct mappers
15. **Constants and Enums** - Shared constants and status enumerations
16. **Embedded Classes** - JSON field structures and validation classes

## 🎯 Key Implementation Standards

### **Library Standards Applied:**
- ✅ @TenantId with explicit tenantId column on ALL entities
- ✅ Name-based identification (no complex business keys)
- ✅ Status enum management for all entities
- ✅ Structured @Embeddable classes replacing generic JSON
- ✅ Proper parent-child relationships (Forecast children belong to Forecast)
- ✅ Bidirectional relationships with proper cascades
- ✅ Integration with datashastra-common-library components
- ✅ Performance optimization with indexing strategies
- ✅ Validation annotations for data quality
- ✅ Version control for critical entities

### **Multi-Tenant Architecture:**
- ✅ Row-level security using @TenantId
- ✅ Explicit tenant column strategy
- ✅ Repository-level tenant filtering
- ✅ Library-focused entity definitions
- ✅ Clean separation of concerns

### **Performance Optimizations:**
- ✅ Strategic indexing on tenant_id, business keys, status
- ✅ LAZY fetch strategies by default
- ✅ Proper cascade configurations
- ✅ JSONB for structured data storage
- ✅ Version control with @Version annotation

This comprehensive design provides a solid foundation for implementing the Stock Sense entity model with enterprise-grade multi-tenant architecture and MDM compliance.