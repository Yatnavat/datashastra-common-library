# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**datashastra-common-library** is a Java library built with Spring Boot 3.5.4 and Java 21, designed to provide shared components for DataShastra microservices. This library contains reusable JPA entities, DTOs, mappers, constants, and utilities that can be consumed by multiple Spring Boot applications within the DataShastra ecosystem.

**Key Library Features:**
- **Shared Entities**: Common JPA entities with audit support (BaseEntity)
- **Standardized DTOs**: Response models, error handling, and authentication DTOs
- **Mappers**: MapStruct-based type-safe mapping utilities
- **Constants**: Shared application constants and service definitions
- **Utilities**: Common helper classes and configuration beans

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

### Data Transfer Objects (`dto/`)
- **StandardResponse<T>**: Generic response wrapper with success/error states
- **ErrorResponse**: Standardized error response format
- **UserDto**: User data transfer object
- **LoginRequest/LoginResponse**: Authentication DTOs
- **RefreshTokenRequest**: Token refresh DTO
- **KeycloakTokenResponse**: Keycloak integration DTO
- **CompanyDto**: Company/organization DTO

### Mappers (`mapper/`)
- **UserMapper**: MapStruct mapper for User entity/DTO conversion
- **CompanyMapper**: MapStruct mapper for Company entity/DTO conversion

### Constants (`constant/`)
- **ServiceConstants**: Shared application constants and service definitions

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

## Database Integration

**Connection**: This library provides entities that work with PostgreSQL databases

**Auditing**: All entities extending BaseEntity automatically track creation/modification timestamps and user information

**Multi-tenancy**: Entities support tenant-based data isolation using `@TenantId` annotation

## Security Notes

- Password fields are explicitly ignored in DTO mappings for security
- Entities support Keycloak integration for authentication
- Standardized error responses prevent information leakage
- Input validation using Jakarta validation annotations

## Library Development Guidelines

### Adding New Components

**New Entities:**
- Always extend `BaseEntity` for audit support
- Use appropriate validation annotations (`@NotBlank`, `@Email`, etc.)
- Include `@TenantId` for multi-tenant entities
- Follow naming conventions and table mappings

**New DTOs:**
- Create corresponding DTOs for entities
- Use MapStruct mappers for type-safe conversion
- Follow the `StandardResponse<T>` pattern for API responses
- Exclude sensitive fields (passwords) from DTOs

**New Mappers:**
- Use MapStruct for entity-DTO conversion
- Configure as Spring components with `@Mapper(componentModel = "spring")`
- Explicitly ignore sensitive fields in mappings
- Handle nested object mappings appropriately

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
- Import only necessary components
- Configure Spring Data JPA auditing if using BaseEntity
- Set up proper database configuration for entities
- Configure MapStruct annotation processing
- Handle multi-tenancy if using tenant-aware entities

### Best Practices

- **Backward Compatibility**: Maintain API compatibility when adding new features
- **Documentation**: Update CLAUDE.md when adding new components
- **Testing**: Add comprehensive tests for new functionality
- **Security**: Never expose sensitive data in DTOs or responses
- **Dependencies**: Keep library dependencies minimal and well-documented