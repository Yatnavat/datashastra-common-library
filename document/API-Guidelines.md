# API Guidelines

## Overview

This document defines the standard API development process and conventions for the Spring Boot application. The architecture follows a layered approach: **API (Controller) � Service � Repository � Mapper**.

## API Development Process

### 1. Controller Layer (API)
Controllers handle HTTP requests and responses, acting as the entry point for all API endpoints.

#### Standard Controller Structure
```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for user operations")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    // API endpoints here
}
```

#### Required Annotations
- `@RestController` - Mark as REST controller
- `@RequestMapping` - Base path for all endpoints
- `@Tag` - OpenAPI documentation grouping
- `@Slf4j` - Logging capability
- `@RequiredArgsConstructor` - Lombok constructor injection

#### Method-Level Annotations
- `@Operation` - Describe the API operation
- `@ApiResponses` - Define possible HTTP responses
- `@Parameter` - Document request parameters
- `@Valid` - Enable validation for request bodies

### 2. Service Layer
Services contain business logic and coordinate between controllers and repositories.

#### Standard Service Structure
```java
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    // Business logic methods here
}
```

#### Service Responsibilities
- Business rule validation
- Data transformation via mappers
- Transaction management
- External service integration
- Exception handling with meaningful messages

### 3. Repository Layer
Repositories handle data access and database operations.

#### Standard Repository Structure
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByCompanyId(Long companyId);
    boolean existsByEmail(String email);
    Optional<User> findByIdAndCompanyId(Long id, Long companyId);
}
```

#### Repository Guidelines
- Extend `JpaRepository<Entity, IdType>`
- Use descriptive method names following Spring Data conventions
- Avoid complex queries in method names; use `@Query` for complex operations
- Always return `Optional<T>` for single results that might not exist

### 4. Mapper Layer
Mappers handle conversion between DTOs and entities using MapStruct.

#### Standard Mapper Structure
```java
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    User toEntity(UserDto userDto);
    
    void updateEntityFromDto(UserDto userDto, @MappingTarget User user);
}
```

## API Design Standards

### 1. RESTful Conventions
- Use nouns for resource names: `/users`, `/companies`
- Use HTTP methods appropriately:
  - `GET` - Retrieve data
  - `POST` - Create new resources
  - `PUT` - Update entire resources
  - `PATCH` - Partial updates
  - `DELETE` - Remove resources

### 2. URL Structure
```
/api/v1/users                    # List all users
/api/v1/users/{id}               # Get specific user
/api/v1/users/{id}/companies     # Get user's companies
/api/v1/companies/{id}/users     # Get company's users
```

### 3. HTTP Status Codes
- `200 OK` - Successful GET, PUT, PATCH
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE
- `400 Bad Request` - Validation errors
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Access denied
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server errors

### 4. Request/Response Format

#### Standardized API Response Wrapper
All API endpoints MUST return responses wrapped in the `StandardResponse<T>` class to ensure consistency:

```java
@PostMapping
public ResponseEntity<StandardResponse<UserDto>> createUser(
    @Valid @RequestBody UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);
    StandardResponse<UserDto> response = StandardResponse.success(createdUser, "User created successfully");
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

#### StandardResponse Structure
```java
// Success Response
{
    "timestamp": "2024-01-01T12:00:00",
    "success": true,
    "message": "User created successfully",
    "data": {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "companyId": 1,
        "companyName": "Tech Corp"
    },
    "error": null,
    "path": null
}

// Error Response
{
    "timestamp": "2024-01-01T12:00:00",
    "success": false,
    "message": "Validation error",
    "data": {
        "email": "Invalid email format",
        "firstName": "First name is required"
    },
    "error": "Validation constraints violated",
    "path": "uri=/api/v1/users"
}
```

#### StandardResponse Usage Patterns
```java
// Success with data
StandardResponse<UserDto> response = StandardResponse.success(userData, "Operation successful");

// Success without data
StandardResponse<Void> response = StandardResponse.success("Operation completed successfully");

// Error response
StandardResponse<Void> response = StandardResponse.error("Operation failed", errorDetails);

// Error with path
StandardResponse<Void> response = StandardResponse.error("Not found", "Resource not found", requestPath);
```

## Data Transfer Objects (DTOs)

### 1. DTO Standards
- Use validation annotations from `jakarta.validation`
- Include only fields needed for the API contract
- Exclude sensitive data (passwords, internal IDs)
- Use meaningful validation messages

### 2. Common Validation Annotations
```java
@NotBlank(message = "Field is required")
@Size(max = 50, message = "Field must be less than 50 characters")
@Email(message = "Invalid email format")
@Pattern(regexp = "^[A-Z0-9]+$", message = "Must contain only uppercase letters and numbers")
```

## Security Considerations

### 1. Authentication
- All endpoints require authentication except `/auth/login` and `/auth/refresh`
- Use JWT tokens for stateless authentication
- Implement proper token validation

### 2. Authorization
- Use `@PreAuthorize` annotations for method-level security
- Implement role-based access control
- Validate user permissions in service layer

### 3. Data Protection
- Never expose passwords in API responses
- Use DTOs to control data exposure
- Implement proper input validation and sanitization

## Error Handling

### 1. Custom Exceptions
```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

### 2. Global Exception Handler
- Use `@RestControllerAdvice` for centralized error handling
- Provide consistent error response format
- Log errors appropriately for debugging
- Never expose sensitive information in error messages

## Documentation

### 1. OpenAPI Annotations
```java
@Operation(summary = "Create a new user", 
           description = "Creates a new user with the provided information")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "User created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
    @ApiResponse(responseCode = "409", description = "User already exists")
})
```

### 2. Documentation Requirements
- Document all public API endpoints
- Include request/response examples
- Document possible error scenarios
- Keep documentation updated with code changes

## Testing Guidelines

### 1. Controller Testing
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        // Test implementation
    }
}
```

### 2. Service Testing
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        // Test implementation
    }
}
```

## Performance Considerations

### 1. Database Queries
- Use `@Query` for complex queries instead of method name derivation
- Implement pagination for list endpoints
- Use appropriate fetch strategies (LAZY/EAGER)
- Consider using projections for read-only operations

### 2. Caching
- Cache frequently accessed data
- Use appropriate cache eviction strategies
- Consider Redis for distributed caching

### 3. Batch Operations
- Implement batch processing for bulk operations
- Use `@Transactional` appropriately
- Consider async processing for long-running operations

## Code Review Checklist

### API Layer
- [ ] Proper HTTP method usage
- [ ] Correct status code responses
- [ ] Input validation with `@Valid`
- [ ] OpenAPI documentation complete
- [ ] Error handling implemented

### Service Layer
- [ ] Business logic properly encapsulated
- [ ] Proper exception handling
- [ ] Transaction boundaries defined
- [ ] Security validations implemented

### Repository Layer
- [ ] Appropriate return types (Optional, List)
- [ ] Query methods follow Spring Data conventions
- [ ] Complex queries properly implemented

### Mapper Layer
- [ ] All necessary fields mapped
- [ ] Sensitive fields properly ignored
- [ ] Audit fields handled correctly
- [ ] Mapping configuration appropriate