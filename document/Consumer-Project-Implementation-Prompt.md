# Consumer Project Implementation Prompt

## 🎯 For Projects Using datashastra-common-library

This prompt is for DataShastra projects that import the common library and need to implement repository, service, and controller layers using the shared entities.

## 📦 Project Setup

```
I'm working on [PROJECT_NAME] for DataShastra - a [DOMAIN_DESCRIPTION] application that uses the datashastra-common-library.

Technical Stack:
- Java 21
- Spring Boot 3.5.4
- PostgreSQL 15 with JSONB support
- Spring Data JPA with Hibernate 6.6.3
- Multi-tenant architecture using @TenantId annotation
- datashastra-common-library (imported dependency)
- Spring Security with JWT/OAuth2 and Keycloak integration

Multi-Tenant SaaS Architecture:
- Tenant Isolation: @TenantId annotation for row-level security
- Data Segregation: Hibernate tenant filtering with CurrentTenantIdentifierResolver
- Security Context: JWT token contains tenant information
- Service Layer: Responsible for setting tenantId from security context

Library Integration:
- Using shared entities: Client, Project, Activity, Forecast, Features, etc.
- Using shared DTOs: StandardResponse, ErrorResponse, UserDto, etc.
- Using shared components: BaseEntity, Tenant, User entities
- Following StandardResponse pattern for all API responses
```

## 🏗️ Implementation Requirements

### **1. Dependency Configuration**
```gradle
dependencies {
    implementation 'tech.oorjaa:datashastra-common-library:0.0.1-SNAPSHOT'

    // Your project-specific dependencies
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Database
    runtimeOnly 'org.postgresql:postgresql'

    // MapStruct for DTOs
    implementation 'org.mapstruct:mapstruct:1.5.3.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.3.Final'
}

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

### **2. JPA Configuration**
```java
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
    "tech.oorjaa.datashastra.repository", // Common library repositories if any
    "tech.oorjaa.[YOUR_PROJECT].repository" // Your project repositories
})
@EntityScan(basePackages = {
    "tech.oorjaa.datashastra.entity", // Common library entities
    "tech.oorjaa.[YOUR_PROJECT].entity" // Your project-specific entities if any
})
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### **3. Multi-Tenant Configuration**
```java
@Configuration
public class MultiTenantConfig {

    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new TenantIdentifierResolver();
    }
}

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    @Override
    public String resolveCurrentTenantIdentifier() {
        return TenantContext.getCurrentTenant();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

@Component
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}
```

## 📋 Repository Organization by Service Domain

### **Client Management Service Repositories**
```java
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Automatic tenant filtering via @TenantId
    List<Client> findByStatus(ClientStatus status);

    Optional<Client> findByName(String name);

    List<Client> findByStatusOrderByNameAsc(ClientStatus status);

    @Query("SELECT c FROM Client c WHERE c.name LIKE %:name%")
    List<Client> findByNameContaining(@Param("name") String name);

    boolean existsByEmailAndTenantId(String email, Long tenantId);

    @Query("SELECT COUNT(c) FROM Client c WHERE c.status = :status")
    long countByStatus(@Param("status") ClientStatus status);
}

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByClientId(Long clientId);

    List<Project> findByIndustryAndStatus(Industry industry, ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE p.client.id = :clientId AND p.status = :status")
    List<Project> findActiveProjectsByClient(@Param("clientId") Long clientId, @Param("status") ProjectStatus status);

    long countByClientIdAndStatus(Long clientId, ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE p.status = :status ORDER BY p.createdDate DESC")
    Page<Project> findRecentProjectsByStatus(@Param("status") ProjectStatus status, Pageable pageable);
}
```

### **Forecasting Service Repositories**
```java
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByProjectId(Long projectId);

    List<Activity> findByStatusOrderByCreatedDateDesc(ActivityStatus status);

    @Query("SELECT a FROM Activity a WHERE a.project.client.id = :clientId")
    List<Activity> findByClientId(@Param("clientId") Long clientId);

    long countByProjectIdAndStatus(Long projectId, ActivityStatus status);

    @Query("SELECT a FROM Activity a WHERE a.project.id = :projectId AND a.status IN :statuses")
    List<Activity> findByProjectAndStatuses(@Param("projectId") Long projectId, @Param("statuses") List<ActivityStatus> statuses);
}

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    List<Forecast> findByActivityId(Long activityId);

    List<Forecast> findByStatusOrderByCreatedDateDesc(ForecastStatus status);

    @Query("SELECT f FROM Forecast f WHERE f.activity.project.client.id = :clientId")
    List<Forecast> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT f FROM Forecast f WHERE f.status IN :statuses")
    List<Forecast> findByStatusIn(@Param("statuses") List<ForecastStatus> statuses);

    @Query("SELECT f FROM Forecast f WHERE f.activity.id = :activityId AND f.status = :status ORDER BY f.createdDate DESC")
    Page<Forecast> findLatestForecastsByActivity(@Param("activityId") Long activityId, @Param("status") ForecastStatus status, Pageable pageable);
}

@Repository
public interface ForecastResultRepository extends JpaRepository<ForecastResult, Long> {

    Optional<ForecastResult> findByForecastId(Long forecastId);

    @Query("SELECT fr FROM ForecastResult fr WHERE fr.forecast.activity.project.client.id = :clientId")
    List<ForecastResult> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT fr FROM ForecastResult fr WHERE fr.forecast.status = :forecastStatus")
    List<ForecastResult> findByForecastStatus(@Param("forecastStatus") ForecastStatus forecastStatus);
}
```

### **Data Management Service Repositories**
```java
@Repository
public interface UploadedDataRepository extends JpaRepository<UploadedData, Long> {

    List<UploadedData> findByActivityId(Long activityId);

    List<UploadedData> findByValidationStatus(ValidationStatus validationStatus);

    @Query("SELECT ud FROM UploadedData ud WHERE ud.activity.project.client.id = :clientId")
    List<UploadedData> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT ud FROM UploadedData ud WHERE ud.uploadTimestamp >= :startDate ORDER BY ud.uploadTimestamp DESC")
    List<UploadedData> findRecentUploads(@Param("startDate") LocalDateTime startDate);

    long countByActivityIdAndValidationStatus(Long activityId, ValidationStatus validationStatus);
}

@Repository
public interface TransformedDataRepository extends JpaRepository<TransformedData, Long> {

    Optional<TransformedData> findByUploadedDataId(Long uploadedDataId);

    List<TransformedData> findByStatus(TransformationStatus status);

    @Query("SELECT td FROM TransformedData td WHERE td.uploadedData.activity.id = :activityId")
    List<TransformedData> findByActivityId(@Param("activityId") Long activityId);

    @Query("SELECT td FROM TransformedData td WHERE td.status = :status ORDER BY td.transformationTimestamp DESC")
    Page<TransformedData> findRecentTransformations(@Param("status") TransformationStatus status, Pageable pageable);
}

@Repository
public interface ValidationResultRepository extends JpaRepository<ValidationResult, Long> {

    Optional<ValidationResult> findByUploadedDataId(Long uploadedDataId);

    List<ValidationResult> findByStatus(ValidationStatus status);

    @Query("SELECT vr FROM ValidationResult vr WHERE vr.uploadedData.activity.project.client.id = :clientId")
    List<ValidationResult> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT COUNT(vr) FROM ValidationResult vr WHERE vr.status = :status")
    long countByStatus(@Param("status") ValidationStatus status);
}
```

### **Analytics Service Repositories**
```java
@Repository
public interface ExploratoryDataAnalyticsRepository extends JpaRepository<ExploratoryDataAnalytics, Long> {

    Optional<ExploratoryDataAnalytics> findByForecastId(Long forecastId);

    @Query("SELECT eda FROM ExploratoryDataAnalytics eda WHERE eda.forecast.activity.project.client.id = :clientId")
    List<ExploratoryDataAnalytics> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT eda FROM ExploratoryDataAnalytics eda WHERE eda.forecast.status = :forecastStatus")
    List<ExploratoryDataAnalytics> findByForecastStatus(@Param("forecastStatus") ForecastStatus forecastStatus);
}

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    List<Model> findByModelType(ModelType modelType);

    List<Model> findByStatusOrderByAccuracyDesc(ModelStatus status);

    @Query("SELECT m FROM Model m WHERE m.isRecommended = true AND m.status = :status")
    List<Model> findRecommendedModels(@Param("status") ModelStatus status);

    @Query("SELECT m FROM Model m WHERE m.complexity <= :maxComplexity ORDER BY m.accuracy DESC")
    List<Model> findModelsByComplexity(@Param("maxComplexity") Integer maxComplexity);
}
```

### **Configuration Service Repositories**
```java
@Repository
public interface FeaturesRepository extends JpaRepository<Features, Long> {

    Optional<Features> findByForecastId(Long forecastId);

    @Query("SELECT f FROM Features f WHERE f.forecast.activity.project.client.id = :clientId")
    List<Features> findByClientId(@Param("clientId") Long clientId);

    List<Features> findByStatus(FeatureStatus status);
}

@Repository
public interface AggregationRepository extends JpaRepository<Aggregation, Long> {

    Optional<Aggregation> findByForecastId(Long forecastId);

    @Query("SELECT a FROM Aggregation a WHERE a.forecast.activity.project.client.id = :clientId")
    List<Aggregation> findByClientId(@Param("clientId") Long clientId);
}

@Repository
public interface DateRangeConfigurationRepository extends JpaRepository<DateRangeConfiguration, Long> {

    Optional<DateRangeConfiguration> findByForecastId(Long forecastId);

    @Query("SELECT drc FROM DateRangeConfiguration drc WHERE drc.trainingStartDate >= :startDate")
    List<DateRangeConfiguration> findByTrainingStartAfter(@Param("startDate") LocalDate startDate);
}
```
```

## 🔧 Service Layer Implementation Pattern

```java
@Service
@Transactional
@Slf4j
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private UserRepository userRepository;

    public StandardResponse<ClientDto> createClient(CreateClientDto createDto) {
        try {
            // Get current tenant from security context
            Long currentTenantId = getCurrentTenantId();

            // Validate unique email within tenant
            if (clientRepository.existsByEmailAndTenantId(createDto.getEmail(), currentTenantId)) {
                return StandardResponse.error("Client with this email already exists", "DUPLICATE_EMAIL");
            }

            // Validate primary contact if provided
            User primaryContact = null;
            if (createDto.getPrimaryContactId() != null) {
                primaryContact = userRepository.findById(createDto.getPrimaryContactId())
                    .orElseThrow(() -> new EntityNotFoundException("Primary contact not found"));
            }

            // Create client entity
            Client client = new Client();
            client.setTenantId(currentTenantId);
            client.setName(createDto.getName());
            client.setEmail(createDto.getEmail());
            client.setPhoneNumber(createDto.getPhoneNumber());
            client.setPrimaryContact(primaryContact);
            client.setStatus(ClientStatus.ACTIVE);

            // Save client
            Client savedClient = clientRepository.save(client);

            // Convert to DTO and return
            ClientDto clientDto = clientMapper.toDto(savedClient);
            return StandardResponse.success(clientDto, "Client created successfully");

        } catch (Exception e) {
            log.error("Error creating client: {}", e.getMessage(), e);
            return StandardResponse.error("Failed to create client", e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public StandardResponse<List<ClientDto>> getAllClients() {
        try {
            // Repository automatically filters by tenant due to @TenantId
            List<Client> clients = clientRepository.findByStatusOrderByNameAsc(ClientStatus.ACTIVE);
            List<ClientDto> clientDtos = clientMapper.toDtoList(clients);

            return StandardResponse.success(clientDtos, "Clients retrieved successfully");

        } catch (Exception e) {
            log.error("Error retrieving clients: {}", e.getMessage(), e);
            return StandardResponse.error("Failed to retrieve clients", e.getMessage());
        }
    }

    public StandardResponse<ClientDto> updateClient(Long clientId, UpdateClientDto updateDto) {
        try {
            Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

            // Update fields
            client.setName(updateDto.getName());
            client.setEmail(updateDto.getEmail());
            client.setPhoneNumber(updateDto.getPhoneNumber());

            // Update primary contact if changed
            if (updateDto.getPrimaryContactId() != null) {
                User primaryContact = userRepository.findById(updateDto.getPrimaryContactId())
                    .orElseThrow(() -> new EntityNotFoundException("Primary contact not found"));
                client.setPrimaryContact(primaryContact);
            }

            Client savedClient = clientRepository.save(client);
            ClientDto clientDto = clientMapper.toDto(savedClient);

            return StandardResponse.success(clientDto, "Client updated successfully");

        } catch (Exception e) {
            log.error("Error updating client: {}", e.getMessage(), e);
            return StandardResponse.error("Failed to update client", e.getMessage());
        }
    }

    public StandardResponse<Void> deleteClient(Long clientId) {
        try {
            Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

            // Soft delete by setting status to INACTIVE
            client.setStatus(ClientStatus.INACTIVE);
            clientRepository.save(client);

            return StandardResponse.success(null, "Client deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting client: {}", e.getMessage(), e);
            return StandardResponse.error("Failed to delete client", e.getMessage());
        }
    }

    private Long getCurrentTenantId() {
        // Extract tenant ID from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication.getPrincipal();
            return Long.valueOf(jwtToken.getTokenAttributes().get("tenant_id").toString());
        }
        throw new SecurityException("Unable to determine current tenant");
    }
}
```

## 🌐 Controller Implementation Pattern

```java
@RestController
@RequestMapping("/api/clients")
@Validated
@Slf4j
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client within the current tenant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Client with email already exists")
    })
    public ResponseEntity<StandardResponse<ClientDto>> createClient(
            @Valid @RequestBody CreateClientDto createDto) {

        StandardResponse<ClientDto> response = clientService.createClient(createDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieves all active clients for the current tenant")
    public ResponseEntity<StandardResponse<List<ClientDto>>> getAllClients() {

        StandardResponse<List<ClientDto>> response = clientService.getAllClients();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Get client by ID", description = "Retrieves a specific client by ID")
    public ResponseEntity<StandardResponse<ClientDto>> getClientById(
            @PathVariable @Positive Long clientId) {

        StandardResponse<ClientDto> response = clientService.getClientById(clientId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{clientId}")
    @Operation(summary = "Update client", description = "Updates an existing client")
    public ResponseEntity<StandardResponse<ClientDto>> updateClient(
            @PathVariable @Positive Long clientId,
            @Valid @RequestBody UpdateClientDto updateDto) {

        StandardResponse<ClientDto> response = clientService.updateClient(clientId, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{clientId}")
    @Operation(summary = "Delete client", description = "Soft deletes a client by setting status to INACTIVE")
    public ResponseEntity<StandardResponse<Void>> deleteClient(
            @PathVariable @Positive Long clientId) {

        StandardResponse<Void> response = clientService.deleteClient(clientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clientId}/projects")
    @Operation(summary = "Get client projects", description = "Retrieves all projects for a specific client")
    public ResponseEntity<StandardResponse<List<ProjectDto>>> getClientProjects(
            @PathVariable @Positive Long clientId) {

        StandardResponse<List<ProjectDto>> response = clientService.getClientProjects(clientId);
        return ResponseEntity.ok(response);
    }
}
```

## 📝 DTO Implementation Pattern

```java
// Create DTOs
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClientDto {

    @NotBlank(message = "Client name is required")
    @Size(max = 100, message = "Client name must not exceed 100 characters")
    private String name;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;

    private Long primaryContactId;
}

// Update DTOs
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateClientDto {

    @NotBlank(message = "Client name is required")
    @Size(max = 100, message = "Client name must not exceed 100 characters")
    private String name;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;

    private Long primaryContactId;
}

// Response DTOs
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private ClientStatus status;
    private UserDto primaryContact;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    // Project summary for client details
    private Long totalProjects;
    private Long activeProjects;
}
```

## 🔄 Mapper Implementation Pattern

```java
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ClientMapper {

    @Mapping(target = "totalProjects", ignore = true)
    @Mapping(target = "activeProjects", ignore = true)
    ClientDto toDto(Client entity);

    List<ClientDto> toDtoList(List<Client> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Client toEntity(CreateClientDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateEntityFromDto(UpdateClientDto dto, @MappingTarget Client entity);
}
```

## 🔒 Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            )
            .addFilterBefore(tenantFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public TenantFilter tenantFilter() {
        return new TenantFilter();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}

@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenantId = extractTenantFromRequest(httpRequest);

        try {
            TenantContext.setCurrentTenant(tenantId);
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private String extractTenantFromRequest(HttpServletRequest request) {
        // Extract from JWT token or header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Decode JWT and extract tenant_id claim
            // Implementation depends on your JWT structure
        }
        return "1"; // Default tenant for demo
    }
}
```

## 🚀 Implementation Checklist

### **Setup Phase**
- [ ] Add datashastra-common-library dependency
- [ ] Configure GitHub Packages authentication
- [ ] Set up JPA entity scanning for common library entities
- [ ] Configure multi-tenant resolver and context

### **Repository Layer**
- [ ] Create repository interfaces extending JpaRepository
- [ ] Use common library entities (Client, Project, Activity, Forecast)
- [ ] Implement custom queries using @Query annotations
- [ ] Test automatic tenant filtering via @TenantId

### **Service Layer**
- [ ] Implement service classes with @Transactional
- [ ] Use StandardResponse for all return types
- [ ] Extract tenant ID from security context
- [ ] Handle exceptions with proper error responses
- [ ] Implement business logic and validation

### **Controller Layer**
- [ ] Create REST controllers with proper validation
- [ ] Use OpenAPI annotations for documentation
- [ ] Follow RESTful conventions
- [ ] Return StandardResponse consistently

### **DTOs and Mappers**
- [ ] Create request/response DTOs with validation
- [ ] Implement MapStruct mappers using common library components
- [ ] Handle entity-DTO conversion properly
- [ ] Ignore audit and system fields in create/update operations

### **Security Integration**
- [ ] Configure JWT token processing
- [ ] Set up tenant extraction from tokens
- [ ] Implement role-based access control
- [ ] Test multi-tenant data isolation

### **Testing**
- [ ] Write unit tests for services
- [ ] Write integration tests for repositories
- [ ] Test controller endpoints
- [ ] Verify tenant isolation works correctly

This comprehensive implementation guide ensures proper integration with the datashastra-common-library while maintaining clean separation of concerns and following enterprise patterns.