# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.4.6 application with Java 21, designed as a starter template for enterprise services. It includes authentication via Keycloak, PostgreSQL integration, and follows clean architecture patterns with comprehensive security configuration.

## Development Commands

### Build and Run
```bash
# Build the application
./gradlew build

# Run the application (default profile: local)
./gradlew bootRun

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Create executable JAR
./gradlew bootJar

# Build Docker image
./gradlew bootBuildImage
```

### Testing
```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test

# Run single test class
./gradlew test --tests "tech.oorjaa.demoservice.controller.AuthControllerTest"

# Compile test classes only
./gradlew compileTestJava
```

### Test Configuration
- **Test Database**: H2 in-memory database (automatically configured)
- **Test Profile**: Uses `application-test.yml` with profile `test`
- **Security**: Simplified security configuration for testing via `TestConfigurationSetup`
- **Test Data**: `TestDataBuilder` utility class provides pre-configured test entities and DTOs

### Security and Quality
```bash
# Run OWASP dependency vulnerability scan
./gradlew dependencyCheckAnalyze

# Update CVE database
./gradlew dependencyCheckUpdate

# Clean build directory
./gradlew clean
```

## Architecture Overview

### Core Components

**Authentication & Security**
- `SecurityConfig.java` - Main security configuration with JWT/OAuth2 setup
- `KeycloakRoleConverter.java` - Converts Keycloak roles to Spring Security authorities
- `AuthService.java` - Handles authentication with Keycloak token exchange
- JWT-based authentication with Keycloak integration

**Data Layer**
- `BaseEntity.java` - Provides audit fields (created/modified dates and users) for all entities
- All entities extend BaseEntity for consistent audit tracking
- PostgreSQL with JPA/Hibernate
- Repository pattern with Spring Data JPA

**Service Layer**
- Services handle business logic and transaction management
- `UserContextService.java` - Manages current user context from security context
- Services interact with repositories and external systems (Keycloak)

**Web Layer**
- Controllers handle HTTP requests and responses
- `GlobalExceptionHandler.java` - Centralized exception handling
- DTOs for request/response objects

**Configuration**
- `AppProperties.java` - Application-specific configuration properties
- `CommonBeanConfig.java` - Common bean definitions
- `OpenApiConfig.java` - Swagger/OpenAPI configuration
- `MapperConfig.java` - MapStruct configuration

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

## Database

**Connection**: PostgreSQL database configured per environment profile

**Schema Management**: SQL scripts in `src/main/resources/sql/` for initial data loading

**Auditing**: All entities automatically track creation/modification timestamps and user information via BaseEntity

## Environment Profiles

- `local` - Default profile for local development
- `dev` - Development environment with external dependencies
- `stage` - Staging environment configuration

## API Documentation

Swagger UI is available at `/swagger-ui.html` when `app.exposeSwagger=true`

## Security Notes

- CORS configured to allow all origins (should be restricted in production)
- JWT tokens validated against Keycloak
- Password fields are never included in API responses
- OWASP dependency scanning configured with quality gates

## Development Guidelines

### API Development
Follow the comprehensive API Guidelines in `document/API-Guidelines.md` for consistent API development:
- **Layered Architecture**: API (Controller) → Service → Repository → Mapper
- RESTful conventions with proper HTTP methods and status codes
- OpenAPI documentation with comprehensive annotations
- Standardized error handling and response formats
- Security best practices with JWT authentication
- Input validation using Jakarta validation annotations

### Entity Modeling
Follow the MDM Entity Guidelines in `document/MDM-Entity-Guidelines.md` for consistent entity modeling:
- All entities extend BaseEntity for audit fields
- Use business keys for natural identifiers
- Implement proper validation annotations
- Follow hierarchical relationship patterns
- Include status and versioning fields where appropriate