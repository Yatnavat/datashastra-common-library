# Entity Design Best Practices
## DataShastra Common Library - Comprehensive Entity Architecture Guide

**Version**: 1.1
**Last Updated**: 2025-09-30
**Purpose**: Comprehensive guide combining best practices from both branches for new project entity design
**Changelog**: Updated @Where to @SQLRestriction (Hibernate 6.x), added @SoftDelete annotation documentation

---

## Table of Contents
1. [BaseEntity Foundation](#baseentity-foundation)
2. [Entity Design Patterns](#entity-design-patterns)
3. [Complete Entity Catalog](#complete-entity-catalog)
4. [Multi-Tenancy & Authentication](#multi-tenancy--authentication)
5. [Database Design Principles](#database-design-principles)
6. [Performance Optimization](#performance-optimization)
7. [Best Practices Summary](#best-practices-summary)

---

## BaseEntity Foundation

### Recommended BaseEntity Design

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base entity providing common fields for all entities:
 * - Auto-increment Long ID
 * - Tenant isolation via tenantId
 * - Audit fields (created/modified by and date)
 * - Soft delete support
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Tenant ID for multi-tenancy support
     * All queries must filter by this field
     */
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    protected String createdBy;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "modified_by", length = 100)
    protected String modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_date")
    protected LocalDateTime modifiedDate;

    /**
     * Soft delete flag - use instead of physical deletion
     */
    @Column(name = "deleted", nullable = false)
    protected Boolean deleted = false;

    /**
     * Mark entity as deleted (soft delete)
     */
    public void markAsDeleted() {
        this.deleted = true;
        this.modifiedDate = LocalDateTime.now();
    }

    /**
     * Check if entity is active (not deleted)
     */
    public boolean isActive() {
        return !deleted;
    }
}
```

### Key Design Decisions

| Aspect | Choice | Rationale |
|--------|--------|-----------|
| **ID Type** | `Long` with `@GeneratedValue(strategy = IDENTITY)` | Simple, performant, widely supported. Avoid UUID overhead unless distributed systems require it. |
| **Tenant Isolation** | `Integer tenantId` field in BaseEntity | All entities inherit tenant isolation. Enforced at query level. |
| **Soft Delete** | `Boolean deleted` field with helper methods | Preserves data for audit, recovery, and analytics. Use `@Where` clause for filtering. |
| **Audit Fields** | Created/Modified by and date | Spring Data JPA auditing with `@CreatedBy`, `@LastModifiedBy`, `@CreationTimestamp`, `@UpdateTimestamp` |
| **Serializable** | Implements `Serializable` | Required for JPA entities, especially when using caching |

---

## Entity Design Patterns

### Pattern 1: Aggregate Root (Activity)

**Use Case**: Core domain entity that owns a cluster of related entities

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity Entity - Aggregate Root
 * Represents a forecasting activity with owned child entities (Forecasts)
 * and references to cross-aggregate entities (Client, Project)
 */
@Entity
@Table(name = "activity", indexes = {
    @Index(name = "idx_activity_tenant_status", columnList = "tenant_id, status, deleted"),
    @Index(name = "idx_activity_client", columnList = "client_id"),
    @Index(name = "idx_activity_project", columnList = "project_id"),
    @Index(name = "idx_activity_dates", columnList = "start_date, end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class Activity extends BaseEntity {

    @NotBlank(message = "Activity name is required")
    @Size(max = 255, message = "Activity name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Foreign Key to Client (cross-aggregate reference)
     * NOT a JPA @ManyToOne - referenced by ID only to maintain bounded context
     */
    @NotNull(message = "Client ID is required")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    /**
     * Foreign Key to Project (cross-aggregate reference)
     */
    @Column(name = "project_id")
    private Long projectId;

    @NotBlank(message = "Industry is required")
    @Size(max = 100)
    @Column(name = "industry", nullable = false, length = 100)
    private String industry;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ActivityStatus status = ActivityStatus.DRAFT;

    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private ActivityPriority priority = ActivityPriority.MEDIUM;

    @DecimalMin(value = "0.0", message = "Budget must be positive")
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    @Size(max = 3)
    @Column(name = "currency_code", length = 3)
    private String currencyCode = "USD";

    /**
     * JPA relationship to owned child entities within aggregate
     * CascadeType.ALL ensures child lifecycle is managed by parent
     */
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private List<Forecast> forecasts = new ArrayList<>();

    // Business logic methods

    public void addForecast(Forecast forecast) {
        forecasts.add(forecast);
        forecast.setActivity(this);
        forecast.setTenantId(this.getTenantId());
    }

    public void removeForecast(Forecast forecast) {
        forecasts.remove(forecast);
        forecast.setActivity(null);
    }

    public boolean isActive() {
        return status == ActivityStatus.ACTIVE;
    }

    public boolean isWithinBudget(BigDecimal amount) {
        return budget == null || amount.compareTo(budget) <= 0;
    }

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalStateException("End date cannot be before start date");
        }
    }
}
```

### Pattern 2: Supporting Entity (Forecast)

**Use Case**: Child entity owned by an aggregate root

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Forecast Entity - Supporting Entity within Activity Aggregate
 * Owned by Activity, has JPA @ManyToOne relationship to parent
 * References cross-aggregate entities (Models, UploadedData) by Foreign Key
 */
@Entity
@Table(name = "forecast", indexes = {
    @Index(name = "idx_forecast_tenant_status", columnList = "tenant_id, status, deleted"),
    @Index(name = "idx_forecast_activity", columnList = "activity_id"),
    @Index(name = "idx_forecast_model", columnList = "model_id"),
    @Index(name = "idx_forecast_version", columnList = "activity_id, version")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class Forecast extends BaseEntity {

    /**
     * JPA relationship to parent aggregate root
     * Use @ManyToOne for within-aggregate relationships
     */
    @NotNull(message = "Activity is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false, foreignKey = @ForeignKey(name = "fk_forecast_activity"))
    private Activity activity;

    @NotBlank(message = "Forecast name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Foreign Key to Models (cross-aggregate reference)
     * NOT a JPA @ManyToOne - referenced by ID only
     */
    @Column(name = "model_id")
    private Long modelId;

    /**
     * Foreign Key to UploadedData (cross-aggregate reference)
     */
    @Column(name = "uploaded_data_id")
    private Long uploadedDataId;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ForecastStatus status = ForecastStatus.DRAFT;

    @Min(value = 1, message = "Version must be at least 1")
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Min(value = 1, message = "Current step must be at least 1")
    @Max(value = 8, message = "Current step must not exceed 8")
    @Column(name = "current_step", nullable = false)
    private Integer currentStep = 1;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * JSON storage for flexible configuration
     * Use String with @JdbcTypeCode for PostgreSQL JSONB support
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configuration", columnDefinition = "jsonb")
    private String configuration;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metrics", columnDefinition = "jsonb")
    private String metrics;

    // Business logic methods

    public void startForecast() {
        if (status == ForecastStatus.DRAFT) {
            this.status = ForecastStatus.IN_PROGRESS;
            this.startedAt = LocalDateTime.now();
        }
    }

    public void completeForecast() {
        if (status == ForecastStatus.IN_PROGRESS) {
            this.status = ForecastStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        }
    }

    public void advanceStep() {
        if (currentStep < 8) {
            this.currentStep++;
        }
    }

    public boolean isInProgress() {
        return status == ForecastStatus.IN_PROGRESS;
    }

    @PrePersist
    private void onCreate() {
        if (status == ForecastStatus.IN_PROGRESS && startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }
}
```

### Pattern 3: Standalone Entity (Models)

**Use Case**: Reference data entity with no JPA relationships, heavily cached

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

/**
 * Models Entity - Standalone Reference Entity
 * No JPA relationships - referenced by ID only from other entities
 * Heavily cached for performance
 */
@Entity
@Table(name = "models", indexes = {
    @Index(name = "idx_models_tenant_type", columnList = "tenant_id, model_type, deleted"),
    @Index(name = "idx_models_complexity", columnList = "complexity"),
    @Index(name = "idx_models_active", columnList = "is_active, deleted")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Models extends BaseEntity {

    @NotBlank(message = "Model name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull(message = "Model type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "model_type", nullable = false, length = 50)
    private ModelType modelType;

    @NotNull(message = "Complexity is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "complexity", nullable = false, length = 20)
    private ModelComplexity complexity;

    @NotBlank(message = "Version is required")
    @Size(max = 50)
    @Column(name = "version", nullable = false, length = 50)
    private String version;

    @NotNull(message = "Active status is required")
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @DecimalMin(value = "0.0", message = "Min data points must be positive")
    @Column(name = "min_data_points")
    private Integer minDataPoints;

    @DecimalMin(value = "0.0", message = "Max data points must be positive")
    @Column(name = "max_data_points")
    private Integer maxDataPoints;

    @DecimalMin(value = "0.0", inclusive = false, message = "Training time must be positive")
    @Column(name = "avg_training_time_minutes", precision = 10, scale = 2)
    private BigDecimal avgTrainingTimeMinutes;

    /**
     * JSON storage for flexible capabilities and requirements
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "capabilities", columnDefinition = "jsonb")
    private String capabilities;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "hyperparameters", columnDefinition = "jsonb")
    private String hyperparameters;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resource_requirements", columnDefinition = "jsonb")
    private String resourceRequirements;

    // Business logic methods

    public boolean isSuitableForDataSize(int dataPoints) {
        if (minDataPoints != null && dataPoints < minDataPoints) {
            return false;
        }
        if (maxDataPoints != null && dataPoints > maxDataPoints) {
            return false;
        }
        return true;
    }

    public boolean isHighComplexity() {
        return complexity == ModelComplexity.HIGH;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
```

---

## Complete Entity Catalog

### Core Forecasting Entities

| Entity | Type | Parent | Description | Key Indexes |
|--------|------|--------|-------------|-------------|
| **Activity** | Aggregate Root | - | Core forecasting activity with client, project, budget | tenant_id + status, client_id, project_id |
| **Forecast** | Supporting | Activity | Individual forecast instance with version control | tenant_id + status, activity_id, version |
| **Models** | Standalone | - | ML model definitions with capabilities (Cached) | tenant_id + model_type, complexity |
| **UploadedData** | Standalone | - | File upload metadata with processing status | tenant_id + status, file_type |
| **TransformedData** | Supporting | UploadedData | Processed data with transformation history | uploaded_data_id, tenant_id |
| **ExploratoryDataAnalytics** | Supporting | UploadedData | EDA results with statistical summaries | uploaded_data_id, tenant_id |
| **ValidationResults** | Supporting | UploadedData | Data validation assessments | uploaded_data_id, validation_status |
| **FeatureConfiguration** | Supporting | Forecast | Feature engineering configuration | forecast_id, tenant_id |
| **TrainingProgress** | Supporting | Forecast | Real-time training monitoring | forecast_id, status |
| **WorkflowStep** | Supporting | Forecast | Workflow progress tracking (8 steps) | forecast_id, step_number |
| **AggregationConfiguration** | Supporting | Forecast | Time series aggregation settings | forecast_id |
| **DateRangeConfiguration** | Supporting | Forecast | Date range specifications | forecast_id |
| **ForecastResult** | Supporting | Forecast | Forecast outputs with metrics | forecast_id, tenant_id |
| **SystemConfiguration** | Standalone | - | Global system settings (Cached) | config_key, tenant_id |
| **NotificationSettings** | Standalone | User | User notification preferences | user_id, tenant_id |

### Multi-Tenancy & Authentication Entities

| Entity | Type | Parent | Description | Key Indexes |
|--------|------|--------|-------------|-------------|
| **Tenant (Company)** | Aggregate Root | - | Tenant/Company with Keycloak configuration | name, status, keycloak_realm |
| **User** | Supporting | Tenant | User with Keycloak integration | tenant_id + email, keycloak_id |
| **TenantConfiguration** | Supporting | Tenant | Dynamic tenant settings (key-value) | tenant_id + config_key |
| **Client** | Supporting | Tenant | Customer/client entity | tenant_id + status |
| **Project** | Supporting | Client | Project entity under client | client_id, tenant_id + status |
| **Role** | Standalone | - | System roles (Admin, User, Viewer) | name |
| **UserRole** | Junction | - | User-Role many-to-many mapping | user_id, role_id |
| **AuditLog** | Standalone | - | System-wide audit trail | entity_type + entity_id, tenant_id |
| **Subscription** | Supporting | Tenant | Tenant subscription and billing | tenant_id, status |

---

## Multi-Tenancy & Authentication

### Tenant (Company) Entity

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

/**
 * Tenant (Company) Entity - Aggregate Root for Multi-Tenancy
 * Each tenant has isolated data and Keycloak realm configuration
 */
@Entity
@Table(name = "tenant", indexes = {
    @Index(name = "idx_tenant_name", columnList = "name", unique = true),
    @Index(name = "idx_tenant_status", columnList = "status, deleted"),
    @Index(name = "idx_tenant_realm", columnList = "keycloak_realm")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class Tenant extends BaseEntity {

    @NotBlank(message = "Tenant name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private TenantStatus status = TenantStatus.ACTIVE;

    // Keycloak Configuration

    @NotBlank(message = "Keycloak server URL is required")
    @Column(name = "keycloak_server_url", nullable = false, length = 500)
    private String keycloakServerUrl;

    @NotBlank(message = "Keycloak realm is required")
    @Size(max = 100)
    @Column(name = "keycloak_realm", nullable = false, length = 100)
    private String keycloakRealm;

    @NotBlank(message = "Keycloak client ID is required")
    @Size(max = 100)
    @Column(name = "keycloak_client_id", nullable = false, length = 100)
    private String keycloakClientId;

    @NotBlank(message = "Keycloak client secret is required")
    @Column(name = "keycloak_client_secret", nullable = false, length = 500)
    private String keycloakClientSecret;

    // Contact Information

    @Email(message = "Valid email is required")
    @Size(max = 255)
    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Size(max = 500)
    @Column(name = "address", length = 500)
    private String address;

    @Size(max = 100)
    @Column(name = "city", length = 100)
    private String city;

    @Size(max = 100)
    @Column(name = "country", length = 100)
    private String country;

    // Subscription & Limits

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_projects")
    private Integer maxProjects;

    @Column(name = "max_storage_gb")
    private Integer maxStorageGb;

    /**
     * JSON storage for flexible tenant configuration
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configuration", columnDefinition = "jsonb")
    private String configuration;

    // Business logic methods

    public boolean isActive() {
        return status == TenantStatus.ACTIVE;
    }

    public boolean canAddUser(int currentUserCount) {
        return maxUsers == null || currentUserCount < maxUsers;
    }

    public boolean canAddProject(int currentProjectCount) {
        return maxProjects == null || currentProjectCount < maxProjects;
    }

    public void activate() {
        this.status = TenantStatus.ACTIVE;
    }

    public void suspend() {
        this.status = TenantStatus.SUSPENDED;
    }

    public void deactivate() {
        this.status = TenantStatus.INACTIVE;
    }
}
```

### User Entity with Keycloak Integration

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * User Entity - Supporting Entity within Tenant Aggregate
 * Integrated with Keycloak for authentication
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_tenant_email", columnList = "tenant_id, email", unique = true),
    @Index(name = "idx_user_keycloak", columnList = "keycloak_id", unique = true),
    @Index(name = "idx_user_status", columnList = "tenant_id, status, deleted")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class User extends BaseEntity {

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    @Size(max = 255)
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @NotBlank(message = "Keycloak ID is required")
    @Size(max = 100)
    @Column(name = "keycloak_id", nullable = false, unique = true, length = 100)
    private String keycloakId;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @NotNull(message = "Email verified status is required")
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    // Business logic methods

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void lock() {
        this.status = UserStatus.LOCKED;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }
}
```

### TenantConfiguration Entity

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * TenantConfiguration Entity - Dynamic Key-Value Settings per Tenant
 * Allows flexible tenant-specific configuration without schema changes
 */
@Entity
@Table(name = "tenant_configuration", indexes = {
    @Index(name = "idx_tenant_config_key", columnList = "tenant_id, config_key", unique = true),
    @Index(name = "idx_tenant_config_category", columnList = "tenant_id, category")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantConfiguration extends BaseEntity {

    @NotBlank(message = "Config key is required")
    @Size(max = 100)
    @Column(name = "config_key", nullable = false, length = 100)
    private String configKey;

    @NotBlank(message = "Config value is required")
    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue;

    @Size(max = 50)
    @Column(name = "category", length = 50)
    private String category;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "Data type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 20)
    @Builder.Default
    private ConfigDataType dataType = ConfigDataType.STRING;

    // Business logic methods

    public String getStringValue() {
        return configValue;
    }

    public Integer getIntValue() {
        return Integer.parseInt(configValue);
    }

    public Boolean getBooleanValue() {
        return Boolean.parseBoolean(configValue);
    }

    public Double getDoubleValue() {
        return Double.parseDouble(configValue);
    }
}
```

### Client Entity

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

/**
 * Client Entity - Customer/Client under Tenant
 * References Tenant by Foreign Key (cross-aggregate)
 */
@Entity
@Table(name = "client", indexes = {
    @Index(name = "idx_client_tenant_status", columnList = "tenant_id, status, deleted"),
    @Index(name = "idx_client_name", columnList = "tenant_id, name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class Client extends BaseEntity {

    @NotBlank(message = "Client name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 100)
    @Column(name = "industry", length = 100)
    private String industry;

    @Email(message = "Valid email is required")
    @Size(max = 255)
    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ClientStatus status = ClientStatus.ACTIVE;

    // Business logic methods

    public boolean isActive() {
        return status == ClientStatus.ACTIVE;
    }

    public void activate() {
        this.status = ClientStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = ClientStatus.INACTIVE;
    }
}
```

### Project Entity

```java
package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Project Entity - Project under Client
 * References Client by Foreign Key (cross-aggregate)
 */
@Entity
@Table(name = "project", indexes = {
    @Index(name = "idx_project_client", columnList = "client_id"),
    @Index(name = "idx_project_tenant_status", columnList = "tenant_id, status, deleted"),
    @Index(name = "idx_project_dates", columnList = "start_date, end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted = false")
public class Project extends BaseEntity {

    @NotBlank(message = "Project name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Foreign Key to Client (cross-aggregate reference)
     */
    @NotNull(message = "Client ID is required")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @DecimalMin(value = "0.0", message = "Budget must be positive")
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    @Size(max = 3)
    @Column(name = "currency_code", length = 3)
    private String currencyCode = "USD";

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.ACTIVE;

    // Business logic methods

    public boolean isActive() {
        return status == ProjectStatus.ACTIVE;
    }

    public void activate() {
        this.status = ProjectStatus.ACTIVE;
    }

    public void complete() {
        this.status = ProjectStatus.COMPLETED;
    }

    public void hold() {
        this.status = ProjectStatus.ON_HOLD;
    }

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalStateException("End date cannot be before start date");
        }
    }
}
```

---

## Database Design Principles

### Relationship Strategy

| Scenario | Use | Example |
|----------|-----|---------|
| **Parent-Child in Same Aggregate** | `@ManyToOne` / `@OneToMany` with `CascadeType.ALL` | Activity → Forecast |
| **Cross-Aggregate Reference** | Foreign Key column (Long ID) | Forecast → Models |
| **Many-to-Many** | Junction table with `@ManyToMany` or explicit entity | User ↔ Role |
| **Reference Data** | Foreign Key only, no JPA relationship | Activity → Client |

### Indexing Strategy

```sql
-- Composite indexes for common query patterns
CREATE INDEX idx_entity_tenant_status ON entity(tenant_id, status, deleted);

-- Foreign key indexes (always index FK columns)
CREATE INDEX idx_forecast_activity ON forecast(activity_id);

-- Date range indexes for temporal queries
CREATE INDEX idx_activity_dates ON activity(start_date, end_date);

-- Unique constraints for business rules
CREATE UNIQUE INDEX idx_user_tenant_email ON users(tenant_id, email) WHERE deleted = false;
```

### Column Design Best Practices

| Data Type | PostgreSQL Type | JPA Annotation | Use Case |
|-----------|-----------------|----------------|----------|
| **Text (short)** | `VARCHAR(255)` | `@Column(length = 255)` | Names, titles |
| **Text (long)** | `TEXT` or `VARCHAR(1000)` | `@Column(length = 1000)` | Descriptions |
| **Enum** | `VARCHAR(50)` | `@Enumerated(EnumType.STRING)` | Status, type fields |
| **Money** | `NUMERIC(15,2)` | `@Column(precision = 15, scale = 2)` | Currency amounts |
| **Timestamp** | `TIMESTAMP` | `LocalDateTime` | Created/modified dates |
| **Date** | `DATE` | `LocalDate` | Start/end dates |
| **Boolean** | `BOOLEAN` | `Boolean` | Flags (deleted, active) |
| **JSON** | `JSONB` | `@JdbcTypeCode(SqlTypes.JSON)` + `String` | Flexible schema |

### Soft Delete Implementation

#### Approach 1: Manual Soft Delete with @SQLRestriction (Recommended)

```java
// Entity annotation - Use @SQLRestriction instead of deprecated @Where
@SQLRestriction("deleted = false")

// BaseEntity field
@Column(name = "deleted", nullable = false)
protected Boolean deleted = false;

// Repository query
@Query("SELECT e FROM Entity e WHERE e.tenantId = :tenantId AND e.deleted = false")
List<Entity> findActiveByTenant(@Param("tenantId") Integer tenantId);

// Service method
public void deleteEntity(Long id) {
    Entity entity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    entity.markAsDeleted();
    repository.save(entity);
}
```

#### Approach 2: Using @SoftDelete Annotation (Hibernate 6.4+)

```java
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

/**
 * @SoftDelete automatically handles soft delete behavior
 * - Converts repository.delete() to UPDATE deleted = true
 * - Automatically filters deleted records from queries
 * - Supports different column types (BOOLEAN, DELETED, ACTIVE)
 */
@Entity
@Table(name = "activity")
@SoftDelete(columnName = "deleted", strategy = SoftDeleteType.DELETED)
public class Activity extends BaseEntity {
    // No need for explicit @Column(name = "deleted") field
    // Hibernate manages it automatically
}

// Service method - Standard delete() now performs soft delete
public void deleteEntity(Long id) {
    Entity entity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    repository.delete(entity); // Automatically sets deleted = true
}
```

**@SoftDelete Strategy Options:**
- `SoftDeleteType.DELETED`: Uses boolean, true = deleted (default)
- `SoftDeleteType.ACTIVE`: Uses boolean, false = deleted (inverse logic)
- Custom converter for other column types (char, integer, timestamp)

**Comparison:**

| Aspect | @SQLRestriction | @SoftDelete |
|--------|-----------------|-------------|
| **Hibernate Version** | 6.0+ | 6.4+ |
| **Control** | Full manual control | Automatic behavior |
| **Field Visibility** | Explicit boolean field | Managed by Hibernate |
| **Custom Logic** | Easy to add business logic | Limited customization |
| **Migration** | Works with existing code | May require refactoring |
| **Recommendation** | Use for explicit control and business logic | Use for simple soft delete without custom logic |

**Recommended Approach**: Use `@SQLRestriction` with manual boolean field for DataShastra Common Library because:
1. Provides explicit control over soft delete logic
2. Allows custom business logic in `markAsDeleted()` method
3. Field is visible in entity for validation and auditing
4. Works consistently across all Hibernate 6.x versions
5. Easier to test and debug

---

## Performance Optimization

### Strategic Indexing

```java
@Table(name = "activity", indexes = {
    // Primary filter index - most common query pattern
    @Index(name = "idx_activity_tenant_status", columnList = "tenant_id, status, deleted"),

    // Foreign key indexes - ALWAYS index FK columns
    @Index(name = "idx_activity_client", columnList = "client_id"),
    @Index(name = "idx_activity_project", columnList = "project_id"),

    // Range query index - for date-based searches
    @Index(name = "idx_activity_dates", columnList = "start_date, end_date")
})
```

### Lazy Loading with Batch Fetching

```java
// Parent entity
@OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
@BatchSize(size = 20)  // Fetch up to 20 children in single query
private List<Forecast> forecasts = new ArrayList<>();

// Repository query with JOIN FETCH when needed
@Query("SELECT a FROM Activity a LEFT JOIN FETCH a.forecasts WHERE a.id = :id")
Optional<Activity> findByIdWithForecasts(@Param("id") Long id);
```

### Caching Reference Data

```java
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Models extends BaseEntity {
    // Frequently accessed, rarely modified data
}

// Repository method
@Cacheable("models-by-type")
List<Models> findByModelType(ModelType modelType);
```

### Query Optimization

```java
// Repository with custom queries
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // Efficient projection - select only needed columns
    @Query("SELECT new tech.oorjaa.datashastra.dto.ActivitySummaryDto(a.id, a.name, a.status) " +
           "FROM Activity a WHERE a.tenantId = :tenantId AND a.deleted = false")
    List<ActivitySummaryDto> findSummariesByTenant(@Param("tenantId") Integer tenantId);

    // Pagination for large datasets
    Page<Activity> findByTenantIdAndDeleted(Integer tenantId, Boolean deleted, Pageable pageable);

    // COUNT query optimization
    @Query("SELECT COUNT(a) FROM Activity a WHERE a.tenantId = :tenantId AND a.status = :status AND a.deleted = false")
    long countByTenantAndStatus(@Param("tenantId") Integer tenantId, @Param("status") ActivityStatus status);
}
```

---

## Best Practices Summary

### ✅ DO

1. **Extend BaseEntity** for all entities to ensure consistent audit fields and tenant isolation
2. **Use `@SQLRestriction("deleted = false")`** on all entities with soft delete (replaces deprecated `@Where`)
3. **Index foreign key columns** and common query filters (`tenant_id`, `status`, `deleted`)
4. **Use `@Enumerated(EnumType.STRING)`** for enum fields (avoid ordinal)
5. **Use `@JdbcTypeCode(SqlTypes.JSON)` with `String`** for JSON storage in PostgreSQL
6. **Use `@ManyToOne`/`@OneToMany`** for within-aggregate relationships
7. **Use Foreign Key columns (Long ID)** for cross-aggregate references
8. **Implement business logic methods** in entities for encapsulation
9. **Use `@PrePersist`/`@PreUpdate`** for validation and lifecycle hooks
10. **Apply `@NotNull`, `@NotBlank`, `@Size`** validation annotations
11. **Use `@BatchSize`** for `@OneToMany` collections to prevent N+1 queries
12. **Cache reference data** with `@Cacheable` and `@Cache`
13. **Use composite indexes** for multi-column query patterns
14. **Implement pagination** for large result sets
15. **Use DTOs for API layer** to control exposed data
16. **Include `tenantId`** in all entity queries for data isolation
17. **Use `LocalDate` and `LocalDateTime`** for temporal fields
18. **Use `BigDecimal`** for monetary values with `precision` and `scale`
19. **Document complex business rules** in entity Javadoc
20. **Follow naming conventions**: snake_case for DB, camelCase for Java

### ❌ DON'T

1. **Don't use UUID as primary key** unless distributed ID generation is required
2. **Don't use `@Enumerated(EnumType.ORDINAL)`** (breaks with enum reordering)
3. **Don't use bidirectional `@ManyToOne`/`@OneToMany`** across aggregates
4. **Don't use `CascadeType.ALL`** for cross-aggregate relationships
5. **Don't use `FetchType.EAGER`** for collections (causes N+1 problems)
6. **Don't expose entities directly** in REST APIs (use DTOs)
7. **Don't store sensitive data unencrypted** (passwords, tokens, secrets)
8. **Don't forget to index foreign keys** (major performance issue)
9. **Don't use `@JsonNode`** for JSON storage (use `String` with `@JdbcTypeCode`)
10. **Don't skip `@NotNull`/`@NotBlank`** validation on required fields

---

## Summary

This guide provides a comprehensive entity design architecture for the DataShastra Common Library, combining best practices from both branches:

- **BaseEntity** with Long ID, tenant isolation, audit fields, and soft delete
- **3 Entity Patterns**: Aggregate Root (Activity), Supporting Entity (Forecast), Standalone (Models)
- **15 Core Forecasting Entities** for complete BI and Predictive Analytics workflows
- **9 Multi-Tenancy & Authentication Entities** with Keycloak integration
- **Strategic Indexing** for sub-500ms query performance
- **Relationship Strategy**: JPA relationships within aggregates, Foreign Keys across aggregates
- **JSON Storage**: String with `@JdbcTypeCode(SqlTypes.JSON)` for PostgreSQL JSONB
- **Performance Optimization**: Batch fetching, caching, pagination, query optimization
- **30 DO/DON'T Best Practices** for maintainable, scalable entity design

All entities in this library follow these patterns and principles, ensuring consistency, performance, and maintainability across the DataShastra ecosystem.