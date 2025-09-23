# DataShastra Common Library Integration Prompt

## 🔧 For Other DataShastra Projects

When working on DataShastra microservices, you are using the **datashastra-common-library** as a shared dependency. This library contains common entities, DTOs, mappers, and utilities that should be reused across all DataShastra projects.

## 📦 What This Library Provides

This library (`tech.oorjaa:datashastra-common-library`) contains:

### Core Entities
- **BaseEntity**: Audit fields (created/modified dates and users) - extend this for all your entities
- **User**: Standard user entity with Keycloak integration and tenant support
- **Tenant**: Multi-tenancy support entity

### Standard DTOs
- **StandardResponse<T>**: Use this for ALL API responses (success/error wrapper)
- **ErrorResponse**: Standardized error response format
- **UserDto**: User data transfer object
- **Authentication DTOs**: LoginRequest, LoginResponse, RefreshTokenRequest, KeycloakTokenResponse

### Mappers
- **UserMapper**: MapStruct mapper for User entity/DTO conversion
- **CompanyMapper**: MapStruct mapper for Company entity/DTO conversion

### Constants
- **ServiceConstants**: Shared application constants across all services

## 🚨 When to Modify the Common Library

**ADD to common library when:**
- You need a new entity that will be shared across multiple services
- You're creating DTOs that multiple services will use
- You need shared constants or enums used by multiple services
- You're adding utility classes that benefit multiple projects
- You need standardized response formats or error handling

**Examples of what belongs in common library:**
```java
// Shared entities that multiple services need
@Entity
public class Organization extends BaseEntity { ... }

// Common response DTOs
public class PaginatedResponse<T> extends StandardResponse<T> { ... }

// Shared constants
public class CommonConstants {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
}

// Utility classes used across services
public class DateUtil {
    public static LocalDateTime parseStandardDate(String date) { ... }
}
```

**DON'T add to common library:**
- Service-specific business logic
- Service-specific entities that won't be shared
- Private configuration classes
- Service-specific controllers or services

## 📋 Integration Checklist for Your Project

### 1. Dependency Setup
Add to your `build.gradle`:
```gradle
dependencies {
    implementation 'tech.oorjaa:datashastra-common-library:0.0.1-SNAPSHOT'
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

### 2. Use Standard Response Format
**✅ DO THIS:**
```java
@RestController
public class YourController {
    @GetMapping("/api/data")
    public StandardResponse<List<YourDto>> getData() {
        List<YourDto> data = yourService.getData();
        return StandardResponse.success(data, "Data retrieved successfully");
    }

    @PostMapping("/api/data")
    public StandardResponse<YourDto> createData(@RequestBody YourCreateDto request) {
        try {
            YourDto created = yourService.create(request);
            return StandardResponse.success(created, "Data created successfully");
        } catch (ValidationException e) {
            return StandardResponse.error("Validation failed", e.getMessage());
        }
    }
}
```

### 3. Extend BaseEntity for Audit Support
**✅ DO THIS:**
```java
@Entity
@Table(name = "your_entity")
public class YourEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Your specific fields...
}
```

### 4. Configure JPA Auditing
Add to your main application class:
```java
@SpringBootApplication
@EnableJpaAuditing
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### 5. Use Shared User Entity
**✅ DO THIS if you need user management:**
```java
// Import and use the shared User entity
import tech.oorjaa.datashastra.entity.User;
import tech.oorjaa.datashastra.dto.UserDto;
import tech.oorjaa.datashastra.mapper.UserMapper;

@Service
public class YourUserService {
    @Autowired
    private UserMapper userMapper;

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return userMapper.toDto(user);
    }
}
```

## 🔄 When You Need to Modify the Common Library

### Step 1: Identify if it belongs in common library
Ask yourself:
- Will this be used by multiple DataShastra services?
- Is this a shared data model or utility?
- Does this provide standardization across services?

### Step 2: Make changes in the common library project
1. Navigate to `datashastra-common-library` project
2. Add your new entities, DTOs, mappers, or utilities
3. Follow the existing patterns and conventions
4. Update version in `build.gradle`
5. Test your changes

### Step 3: Automated Local Development Workflow

#### Option A: IntelliJ IDEA (Recommended)
**In datashastra-common-library project:**
1. Open Gradle panel (View → Tool Windows → Gradle)
2. Navigate to: `datashastra-common-library → Tasks → publishing → publishToMavenLocal`
3. Double-click `publishToMavenLocal` to run
4. Or use IntelliJ terminal: `./gradlew clean build publishToMavenLocal`

**In consuming project:**
1. Open Gradle panel → Click "Refresh Gradle Dependencies" icon (🔄)
2. Or right-click on project → Gradle → Refresh Gradle Project
3. Or IntelliJ terminal: `./gradlew clean build --refresh-dependencies`

#### Option B: Command Line
```bash
cd datashastra-common-library

# Quick local development cycle
./gradlew clean build publishToMavenLocal

# This publishes to your local Maven repository (~/.m2/repository)
# Other projects can immediately use the updated library
```

**For consuming projects to pick up changes:**
```bash
cd your-project
./gradlew clean build --refresh-dependencies
# The --refresh-dependencies flag forces Gradle to check for updated SNAPSHOT versions
```

### Step 4: Automated Gradle Task (Recommended)
Add this to your consuming project's `build.gradle` for easier development:

```gradle
// Add this task to automatically refresh common library
task refreshCommonLibrary {
    doLast {
        exec {
            workingDir '../datashastra-common-library'
            commandLine './gradlew', 'clean', 'build', 'publishToMavenLocal'
        }
    }
}

// Make your build depend on refreshing the library
build.dependsOn refreshCommonLibrary
```

**Usage:**
```bash
# In your project, this will auto-update the common library
./gradlew refreshCommonLibrary
./gradlew build
```

### Step 5: Version Management for Production
Only increment version for production releases:

```bash
# For local development - keep using SNAPSHOT
# No version change needed

# For production release:
# 1. Update version in build.gradle (e.g., 0.0.1-SNAPSHOT → 0.0.2)
# 2. Build and publish
./gradlew clean build test
./gradlew publish  # To GitHub Packages

# 3. Update all consuming projects to use new version
# 4. Create git tag for the release
git tag v0.0.2
git push origin v0.0.2
```

## 🎯 Claude AI Instructions

When Claude AI is working on your DataShastra project:

**Tell Claude to:**
- "Check if the functionality I need already exists in datashastra-common-library"
- "Use StandardResponse for all API responses"
- "Extend BaseEntity for any new entities that need audit support"
- "If I need shared functionality, modify the common library first, then update this project"
- "Follow the patterns established in the common library"
- "After modifying the common library, run `./gradlew clean build publishToMavenLocal` to make changes available locally"
- "Use `--refresh-dependencies` when building consuming projects to pick up library changes"

**Automated Development Workflow for Claude:**

#### Using IntelliJ IDEA (Recommended):
```
When I need to modify the common library:

1. Navigate to datashastra-common-library project in IntelliJ
2. Make the necessary changes (entities, DTOs, mappers, etc.)
3. Open Gradle panel → Tasks → publishing → double-click "publishToMavenLocal"
4. Navigate back to my consuming project in IntelliJ
5. Open Gradle panel → Click "Refresh Gradle Dependencies" (🔄 icon)
6. Now I can use the updated library components (IntelliJ will auto-import)
```

#### Using Command Line:
```
When I need to modify the common library:

1. Navigate to datashastra-common-library project
2. Make the necessary changes (entities, DTOs, mappers, etc.)
3. Run: ./gradlew clean build publishToMavenLocal
4. Navigate back to my project
5. Run: ./gradlew clean build --refresh-dependencies
6. Now I can use the updated library components
```

**Example prompt:**
```
I need to add user management to my service. First check what's available in the
datashastra-common-library, then use those components. If I need additional
shared functionality, add it to the common library first, publish it to Maven local,
then update this project to use the new components.
```

**For Complex Changes:**
```
I need to add a new shared entity called "Product" that multiple services will use.
Please:
1. Add the Product entity to datashastra-common-library
2. Create corresponding ProductDto and ProductMapper
3. Publish the library locally with publishToMavenLocal
4. Then help me use these new components in my current service
```

## 🔧 IntelliJ IDEA Troubleshooting

### Common Issues and Solutions:

**Problem: IntelliJ doesn't recognize updated library classes**
```
Solution:
1. File → Invalidate Caches and Restart
2. Or: Gradle panel → Refresh Gradle Dependencies (🔄)
3. Or: Build → Rebuild Project
```

**Problem: Import statements not working after library update**
```
Solution:
1. Alt + Enter on red import → Add dependency to module
2. Or: File → Project Structure → Modules → Dependencies → Add library
3. Or: Gradle panel → Refresh and rebuild
```

**Problem: Old library classes still showing in autocomplete**
```
Solution:
1. File → Invalidate Caches and Restart
2. Delete .idea folder and reimport project
3. Gradle clean and refresh dependencies
```

**IntelliJ Run Configurations:**
Create run configurations for common tasks:
1. Run → Edit Configurations → Add New → Gradle
2. **Name**: "Publish Common Library"
3. **Gradle project**: datashastra-common-library
4. **Tasks**: clean build publishToMavenLocal
5. Save and use from toolbar

## 📚 Reference

- **Common Library Location**: `D:\DataShastraDevelopment\datashastra-common-library`
- **GitHub Repository**: https://github.com/Yatnavat/datashastra-common-library
- **Package Namespace**: `tech.oorjaa.datashastra.*`
- **Current Version**: `0.0.1-SNAPSHOT`
- **Local Maven Repository**: `~/.m2/repository/tech/oorjaa/datashastra-common-library/`

### IntelliJ Shortcuts:
- **Gradle Panel**: `Ctrl+Shift+A` → "Gradle"
- **Refresh Dependencies**: Gradle panel → 🔄 icon
- **Terminal**: `Alt+F12`
- **Project Structure**: `Ctrl+Alt+Shift+S`

Remember: The goal is to maximize code reuse and maintain consistency across all DataShastra microservices!