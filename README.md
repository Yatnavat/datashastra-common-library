# Demo Service

This is a standard Spring Boot application following best practices for enterprise Java development.

## Features

- Spring Boot 3.4.6 with Java 21
- PostgreSQL database integration
- Spring Security with OAuth2 and Keycloak integration
- API documentation with SpringDoc OpenAPI
- Dependency vulnerability scanning with OWASP
- JPA with Hibernate and auditing support
- Mapstruct for object mapping
- Lombok for reducing boilerplate code

## Project Structure

```
src/main/java/tech/oorjaa/demoservice/
├── config/       # Application configuration classes
├── constant/     # Application constants and enums
├── controller/   # REST API controllers
├── dto/          # Data Transfer Objects
├── entity/       # JPA entity classes
├── exception/    # Custom exceptions and error handling
├── mapper/       # MapStruct mappers
├── repository/   # Spring Data JPA repositories
├── service/      # Business logic services
└── DemoApplication.java # Main application class
```

## Setup and Configuration

### Prerequisites
- Java 21
- PostgreSQL
- Keycloak for authentication

### Environment Profiles
- `local` - Local development environment
- `dev` - Development environment
- `stage` - Staging environment

### Running the Application

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

API documentation is available at: http://localhost:8080/api/swagger-ui.html when running with `app.exposeSwagger=true`

## Security

The application uses Keycloak for authentication and authorization, with JWT token validation.

## Database

The application uses PostgreSQL with Hibernate as the JPA provider. Database migrations should be managed using the SQL scripts in the `src/main/resources/sql` directory.
