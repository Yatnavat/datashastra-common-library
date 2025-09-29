# User Management and Authentication Entities Documentation

This document contains all entities related to User management, Keycloak integration, Tenant management, Client, and Project management extracted from the TMS Common Entity library.

## Core Base Classes

### 1. BaseEntity
**Package**: `com.lkart.common.entitys`
**Purpose**: Base class for entities using UUID as primary key with audit fields

```java
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    
    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;
    
    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdDate;
    
    @LastModifiedBy
    protected String updatedBy;
    
    @LastModifiedDate
    protected LocalDateTime updatedDate;
}
```

### 2. AbstractTenancyDomain
**Package**: `com.lkart.common.entitys`
**Purpose**: Base class for multi-tenant entities with audit fields

```java
@MappedSuperclass
public class AbstractTenancyDomain implements Serializable {
    @TenantId
    @Column(name = "tenant_id")
    private Integer tenantId;
    
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;
    
    private String createdBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedDate;
    
    private String updatedBy;
}
```

## User Management Entities

### 3. User
**Package**: `com.lkart.common.entitys`
**Extends**: `AbstractTenancyDomain`
**Purpose**: Main user entity for authentication and user management

#### Key Fields:
- **Identity**: id (Long), username, email, keycloakUserId, keycloakUsername
- **Personal Info**: firstName, lastName, middleName, employeeId
- **Contact**: phoneNumber, alternatePhoneNumber
- **Authentication**: password, authToken, fcmToken, otp, messageId
- **User Classification**: userType (enum), userSubType (enum), status
- **Organization**: department, designation, authorities (ManyToMany)
- **Banking**: bankName, accountNumber, accountType, ifscCode, branch, upiPhoneNumber
- **Profile**: profilePicture (FileUrl), profileImage
- **Global Fields**:
  - dateOfBirth, gender, nationality
  - Email/SMS/Push notification preferences
  - GDPR consent fields
  - appVersion (TMS_V1, etc.)
- **Relationships**:
  - `@OneToOne` Department
  - `@OneToOne` Designation
  - `@ManyToMany` List<Authorities>
  - `@OneToOne` FileUrl (profilePicture)

### 4. Authorities
**Package**: `com.lkart.common.entitys`
**Purpose**: Role/Authority management for users

```java
@Entity
public class Authorities implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
}
```

### 5. UserAccessLevel
**Package**: `com.lkart.common.entitys`
**Purpose**: Define user access levels across different organizational scopes

```java
@Entity
public class UserAccessLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID Id;
    
    @OneToOne
    private User user;
    
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;
    
    @ManyToOne
    private Application application;
    
    @ElementCollection
    private List<Integer> zones;
    
    @ManyToMany
    private List<City> cities;
    
    @ManyToMany
    private List<Client> clients;
    
    @ManyToMany
    private List<ClientProject> clientProjects;
    
    @ManyToMany
    private List<DeliveryCenter> deliveryCenters;
}
```

### 6. Application
**Package**: `com.lkart.common.entitys`
**Purpose**: Define applications in the system

```java
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private VisibilityScope visibilityScope;
    
    @TenantId
    private Integer tenantId;
}
```

## Tenant Management Entities

### 7. Tenant
**Package**: `com.lkart.common.entitys`
**Purpose**: Multi-tenant support for SaaS architecture

```java
@Entity
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    
    private String name;
    private String tenantCode;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "JSON")
    private JsonNode attributes;
    
    @ManyToOne
    @JoinColumn(name = "organization_client_id")
    private OrganizationClient organizationClient;
}
```

### 8. Organization
**Package**: `com.lkart.common.entitys`
**Purpose**: Top-level organization entity

```java
@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    
    private String name;
}
```

### 9. OrganizationClient
**Package**: `com.lkart.common.entitys`
**Purpose**: Link between Organization and its clients

```java
@Entity
public class OrganizationClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
```

## Client Management Entities

### 10. Client
**Package**: `com.lkart.common.entitys`
**Extends**: `AbstractTenancyDomain`
**Purpose**: Client/Customer entity in the logistics system

#### Key Fields:
- **Identity**: id (Long), clientName, clientCode
- **Contact Info**: 
  - contactName, ownerName
  - phoneNumber, secondaryPhoneNumber
  - email, secondaryEmail
  - Multiple POC fields (primary, operation, finance)
- **Address**: address1, address2, address3, city, state, country, zipCode, landmark
- **Location**: latitude, longitude, cityId, stateId, countryId (UUIDs)
- **Business Info**: 
  - panCard, gstin
  - registeredUnder, contractWith
  - openTime, closeTime
- **Banking**: bankName, accountNumber, accountType, ifscCode, branch
- **Global Fields**:
  - globalBusinessRegistrationNumber
  - globalTaxRegistrationNumber
  - globalNationalBusinessId
  - bankingSystemType (IFSC, SWIFT, IBAN, etc.)
  - bankRoutingCode, bankSwiftCode, ibanNumber
- **Documents**: panCardFrontPhoto, panCardBackPhoto, gstinPhoto, agreementPhoto (FileUrl)
- **Relationships**:
  - `@ManyToOne` User
  - `@ManyToOne` ClientMaster
  - `@OneToOne` FileUrl (multiple document fields)

### 11. ClientProject
**Package**: `com.lkart.common.entitys`
**Extends**: `AbstractTenancyDomain`
**Purpose**: Projects associated with clients

```java
@Entity
public class ClientProject extends AbstractTenancyDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String projectName;
    
    @Enumerated(EnumType.STRING)
    private ClientProjectType projectType;
    
    private String primaryContactName;
    private String primaryContactNumber;
    private String primaryEmailId;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ManyToOne(cascade = CascadeType.DETACH)
    private Client client;
    
    private String contractRateVariationLevel;
    private LocalDateTime deactivatedDateTime;
    private Boolean isGpsCompulsory;
}
```

## Keycloak Integration Models

### 12. CreateUserRequest
**Package**: `com.lkart.common.models.keycloak`
**Purpose**: Request model for creating users in Keycloak

```java
public class CreateUserRequest {
    @NotEmpty
    private String userName;
    private String firstName;
    private String lastName;
    private String emailId;
    @NotEmpty
    private String password;
    private String phoneNumber;
    private List<String> roles;
    private List<String> keycloakRoles;
    private Long tmsId;
    private Long tmsVendorUserId;
    private Long tmsDriverUserId;
    private Long tmsDaUserId;
}
```

### 13. CreateUserResponse
**Package**: `com.lkart.common.models.keycloak`
**Purpose**: Response model after creating user in Keycloak

### 14. UpdateUserRequest
**Package**: `com.lkart.common.models.keycloak`
**Purpose**: Request model for updating user in Keycloak

### 15. UpdatePasswordRequest
**Package**: `com.lkart.common.models.keycloak`
**Purpose**: Request model for password updates in Keycloak

### 16. ValidateTokenResponse
**Package**: `com.lkart.common.models.keycloak`
**Purpose**: Response model for token validation from Keycloak

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateTokenResponse {
    private Integer exp;
    private Integer iat;
    private String iss;
    private String typ;
    private String azp;
    @JsonProperty("preferred_username")
    private String preferredUsername;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    @JsonProperty("realm_access")
    private RealmAccess realmAccess;
    @JsonProperty("resource_access")
    private Map<String, RealmAccess> resourceAccess;
    private String scope;
    @JsonProperty("client_id")
    private String clientId;
    private String username;
    private Boolean active;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("name")
    private String name;
}
```

### 17. RealmAccess
**Package**: `com.lkart.common.models.keycloak`
**Purpose**: Keycloak realm access information

## Related Enums

### UserType
```java
public enum UserType {
    LKARTADMIN("L-kart Admin"),
    DCADMIN("DC Admin"),
    VENDOR("Vendor"),
    DRIVER("Driver"),
    DRIVERASSISTANT("Driver Assistant"),
    DPMANAGER("Dp Manager"),
    CLIENT("Client"),
    SHARED_MOBILITY("Shared Mobility"),
    SHARED_MOBILITY_ADMIN("Shared Mobility Admin"),
    OMS_VIEW("Oms View"),
    SUPERVISOR("Supervisor"),
    MOBILE_REGISTERED("Mobile Registered"),
    VAS("Vas");
}
```

### UserSubType
```java
public enum UserSubType {
    // Contains sub-categorization of users
}
```

### UserStatus
```java
public enum UserStatus {
    // User status values like ACTIVE, INACTIVE, etc.
}
```

### Status
```java
public enum Status {
    // General status enum used across entities
}
```

### AccessLevel
```java
public enum AccessLevel {
    CLIENT("CLIENT"),
    ZONE("ZONE"),
    CITY("CITY"),
    DC("DC"),
    CLIENT_PROJECT("CLIENT_PROJECT");
}
```

### ClientProjectType
```java
public enum ClientProjectType {
    // Types of client projects
}
```

### AccountType
```java
public enum AccountType {
    // Bank account types
}
```

### BankingSystemType
```java
public enum BankingSystemType {
    IFSC, SWIFT, IBAN // etc.
}
```

## Key Design Patterns

1. **Multi-Tenancy**: All business entities extend `AbstractTenancyDomain` with `@TenantId` annotation for tenant isolation
2. **Audit Trail**: Automatic tracking of created/updated timestamps and users
3. **UUID vs Long IDs**: BaseEntity uses UUID, tenant-aware entities use Long with GenerationType.IDENTITY
4. **Keycloak Integration**: Separate models for Keycloak operations, User entity stores keycloakUserId for linking
5. **Global Support**: Entities include global fields for multi-country support (banking systems, tax IDs, etc.)
6. **Document Management**: FileUrl entities for document storage references
7. **JSON Support**: JsonNode fields for flexible attribute storage

## Dependencies Required

```xml
<!-- Spring Boot & JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Jakarta Persistence -->
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
</dependency>

<!-- Hibernate -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<!-- Jackson for JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
</dependency>
```

## Implementation Notes

1. **Tenant Isolation**: Use Hibernate's @TenantId for automatic tenant filtering
2. **Audit Configuration**: Enable JPA auditing with @EnableJpaAuditing
3. **JSON Columns**: Requires MySQL 5.7+ or PostgreSQL for native JSON support
4. **Keycloak Setup**: Configure Keycloak realm and client settings separately
5. **File Storage**: FileUrl references need separate file storage service implementation
6. **Migration**: isMigratedToGlobal flag helps track data migration to global fields

## Usage Example

```java
// Creating a new user
User user = new User();
user.setUsername("john.doe");
user.setEmail("john@example.com");
user.setFirstName("John");
user.setLastName("Doe");
user.setUserType(UserType.CLIENT);
user.setStatus(Status.ACTIVE);
user.setTenantId(tenantId); // Set from context

// Setting authorities
List<Authorities> authorities = new ArrayList<>();
authorities.add(authoritiesRepository.findByName("ROLE_USER"));
user.setAuthorities(authorities);

// Save user
userRepository.save(user);

// Keycloak integration
CreateUserRequest keycloakRequest = new CreateUserRequest();
keycloakRequest.setUserName(user.getUsername());
keycloakRequest.setEmailId(user.getEmail());
keycloakRequest.setPassword(password);
keycloakRequest.setRoles(Arrays.asList("user"));
// Call Keycloak service to create user
```