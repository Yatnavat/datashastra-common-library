package tech.oorjaa.demoservice.util;

import tech.oorjaa.demoservice.dto.CompanyDto;
import tech.oorjaa.demoservice.dto.LoginRequest;
import tech.oorjaa.demoservice.dto.LoginResponse;
import tech.oorjaa.demoservice.dto.UserDto;
import tech.oorjaa.demoservice.entity.Company;
import tech.oorjaa.demoservice.entity.User;

import java.time.LocalDateTime;

/**
 * Utility class for building test data objects
 * Provides convenient methods to create test entities and DTOs with default values
 */
public class TestDataBuilder {

    private TestDataBuilder() {
        // Utility class - prevent instantiation
    }

    // Company Builders
    public static Company defaultCompany() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Tech Corp");
        company.setDescription("A leading technology company");
        company.setLegalName("Tech Corporation Ltd");
        company.setAddress("123 Tech Street, Silicon Valley, CA");
        company.setContactNumber("+1-555-0123");
        company.setContactEmail("contact@techcorp.com");
        company.setLogoUrl("https://techcorp.com/logo.png");
        company.setPrimaryColor("#007bff");
        company.setSecondaryColor("#6c757d");
        return company;
    }

    public static CompanyDto defaultCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1);
        companyDto.setName("Tech Corp");
        companyDto.setDescription("A leading technology company");
        companyDto.setLegalName("Tech Corporation Ltd");
        companyDto.setAddress("123 Tech Street, Silicon Valley, CA");
        companyDto.setContactNumber("+1-555-0123");
        companyDto.setContactEmail("contact@techcorp.com");
        companyDto.setLogoUrl("https://techcorp.com/logo.png");
        companyDto.setPrimaryColor("#007bff");
        companyDto.setSecondaryColor("#6c757d");
        return companyDto;
    }

    // User Builders
    public static User defaultUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setMobileNumber("1234567890");
        user.setUsername("johndoe");
        user.setPassword("encodedPassword123");
        user.setKeycloakId("keycloak-123");
        user.setCompany(defaultCompany());
        user.setCreatedDate(LocalDateTime.now().minusDays(1));
        user.setLastModifiedDate(LocalDateTime.now());
        user.setCreatedBy("system");
        user.setLastModifiedBy("admin");
        return user;
    }

    public static UserDto defaultUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setMobileNumber("1234567890");
        userDto.setPassword("plainPassword123");
        userDto.setCompanyId(1L);
        userDto.setCompanyName("Tech Corp");
        userDto.setRole("USER");
        return userDto;
    }

    public static User userWithCompany(Company company) {
        User user = defaultUser();
        user.setCompany(company);
        return user;
    }

    public static UserDto userDtoWithCompany(Long companyId, String companyName) {
        UserDto userDto = defaultUserDto();
        userDto.setCompanyId(companyId);
        userDto.setCompanyName(companyName);
        return userDto;
    }

    // Auth Builders
    public static LoginRequest defaultLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john.doe@example.com");
        loginRequest.setPassword("password123");
        return loginRequest;
    }

    public static LoginResponse defaultLoginResponse() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(1L);
        loginResponse.setFirstName("John");
        loginResponse.setLastName("Doe");
        loginResponse.setEmail("john.doe@example.com");
        loginResponse.setCompanyId(1L);
        loginResponse.setCompanyName("Tech Corp");
        loginResponse.setSessionToken("mock-jwt-token");
        loginResponse.setRefreshToken("mock-refresh-token");
        loginResponse.setSessionExpire(3600);
        loginResponse.setRefreshExpire(86400);
        return loginResponse;
    }

    // Test Scenarios
    public static class TestScenarios {
        
        public static User createUserForTesting(String email, Company company) {
            User user = defaultUser();
            user.setEmail(email);
            user.setUsername(email.split("@")[0]);
            user.setCompany(company);
            return user;
        }

        public static UserDto createUserDtoForTesting(String email, Long companyId) {
            UserDto userDto = defaultUserDto();
            userDto.setEmail(email);
            userDto.setCompanyId(companyId);
            return userDto;
        }

        public static Company createCompanyForTesting(String name, String legalName) {
            Company company = defaultCompany();
            company.setName(name);
            company.setLegalName(legalName);
            company.setContactEmail(name.toLowerCase().replace(" ", "") + "@example.com");
            return company;
        }

        public static User createMinimalUser(String email) {
            User user = new User();
            user.setFirstName("Test");
            user.setLastName("User");
            user.setEmail(email);
            user.setPassword("password");
            return user;
        }

        public static Company createMinimalCompany(String name) {
            Company company = new Company();
            company.setName(name);
            company.setLegalName(name + " Ltd");
            return company;
        }

        public static UserDto createInvalidUserDto() {
            UserDto userDto = new UserDto();
            userDto.setFirstName(""); // Invalid - empty
            userDto.setEmail("invalid-email"); // Invalid format
            return userDto;
        }

        public static LoginRequest createInvalidLoginRequest() {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(""); // Invalid - empty
            loginRequest.setPassword(""); // Invalid - empty
            return loginRequest;
        }
    }

    // Helper methods for bulk test data
    public static class BulkData {
        
        public static User[] createMultipleUsers(Company company, int count) {
            User[] users = new User[count];
            for (int i = 0; i < count; i++) {
                User user = defaultUser();
                user.setId((long) (i + 1));
                user.setFirstName("User" + (i + 1));
                user.setEmail("user" + (i + 1) + "@example.com");
                user.setUsername("user" + (i + 1));
                user.setCompany(company);
                users[i] = user;
            }
            return users;
        }

        public static UserDto[] createMultipleUserDtos(Long companyId, String companyName, int count) {
            UserDto[] users = new UserDto[count];
            for (int i = 0; i < count; i++) {
                UserDto userDto = defaultUserDto();
                userDto.setId((long) (i + 1));
                userDto.setFirstName("User" + (i + 1));
                userDto.setEmail("user" + (i + 1) + "@example.com");
                userDto.setCompanyId(companyId);
                userDto.setCompanyName(companyName);
                users[i] = userDto;
            }
            return users;
        }

        public static Company[] createMultipleCompanies(int count) {
            Company[] companies = new Company[count];
            for (int i = 0; i < count; i++) {
                Company company = defaultCompany();
                company.setId((long) (i + 1));
                company.setName("Company" + (i + 1));
                company.setLegalName("Company" + (i + 1) + " Ltd");
                company.setContactEmail("company" + (i + 1) + "@example.com");
                companies[i] = company;
            }
            return companies;
        }
    }

    // Common test data sets
    public static class CommonTestData {
        
        public static final String VALID_EMAIL = "test@example.com";
        public static final String INVALID_EMAIL = "invalid-email";
        public static final String VALID_PASSWORD = "password123";
        public static final String INVALID_PASSWORD = "";
        public static final String VALID_PHONE = "+1-555-0123";
        public static final String INVALID_PHONE = "invalid-phone";
        
        public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        public static final String REFRESH_TOKEN = "mock-refresh-token-123456789";
        
        public static final Long VALID_COMPANY_ID = 1L;
        public static final Long INVALID_COMPANY_ID = 999L;
        public static final Long VALID_USER_ID = 1L;
        public static final Long INVALID_USER_ID = 999L;
    }
}