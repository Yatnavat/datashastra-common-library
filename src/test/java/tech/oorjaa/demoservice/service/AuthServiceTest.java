package tech.oorjaa.demoservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.oorjaa.demoservice.config.KeycloakProperties;
import org.springframework.web.client.RestTemplate;
import tech.oorjaa.demoservice.dto.KeycloakTokenResponse;
import tech.oorjaa.demoservice.dto.LoginRequest;
import tech.oorjaa.demoservice.dto.LoginResponse;
import tech.oorjaa.demoservice.entity.Company;
import tech.oorjaa.demoservice.entity.User;
import tech.oorjaa.demoservice.exception.AuthenticationException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Service Tests")
class AuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserService userService;

    @Mock
    private KeycloakProperties keycloakProperties;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private AuthService authService;

    private LoginRequest loginRequest;
    private KeycloakTokenResponse keycloakTokenResponse;
    private User user;
    private Company company;

    @BeforeEach
    void setUp() {
        // Initialize AuthService with mocked dependencies
        authService = new AuthService(keycloakProperties, restTemplate, userService, objectMapper);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password123");

        // Create a mock JWT token with proper structure (header.payload.signature)
        String mockJwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemF0aW9uIjp7InBlcm1pc3Npb25zIjpbeyJzY29wZXMiOlsicmVhZCIsIndyaXRlIl19XX0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJST0xFX1VTRVIiXX19.signature";
        
        keycloakTokenResponse = new KeycloakTokenResponse();
        keycloakTokenResponse.setToken(mockJwtToken);
        keycloakTokenResponse.setRefreshToken("mock-refresh-token");
        keycloakTokenResponse.setExpiresIn(3600);
        keycloakTokenResponse.setRefreshExpiresIn(86400);

        company = new Company();
        company.setId(1L);
        company.setName("Tech Corp");

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setCompany(company);
    }

    private void setupKeycloakPropertiesMock() {
        when(keycloakProperties.clientId()).thenReturn("demo-service");
        when(keycloakProperties.clientSecret()).thenReturn("test-secret");
        when(keycloakProperties.baseUrl()).thenReturn("http://localhost:8080/auth");
        when(keycloakProperties.realm()).thenReturn("test-realm");
    }

    @Test
    @DisplayName("Should return current user roles from security context")
    void getCurrentUserRoles_WithAuthenticatedUser_ShouldReturnRoles() {
        // Given
        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        SecurityContextHolder.setContext(securityContext);

        // When
        Collection<String> result = authService.getCurrentUserRoles();

        // Then
        assertThat(result).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    @DisplayName("Should authenticate user successfully with valid credentials")
    void authenticateUser_WithValidCredentials_ShouldReturnLoginResponse() {
        // Given
        setupKeycloakPropertiesMock();
        ResponseEntity<KeycloakTokenResponse> responseEntity = 
                new ResponseEntity<>(keycloakTokenResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(eq("http://localhost:8080/auth/realms/test-realm/protocol/openid-connect/token"), any(HttpEntity.class), eq(KeycloakTokenResponse.class)))
                .thenReturn(responseEntity);
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        
        // Mock the exchange call for permissions
        KeycloakTokenResponse permissionResponse = new KeycloakTokenResponse();
        permissionResponse.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemF0aW9uIjp7InBlcm1pc3Npb25zIjpbeyJzY29wZXMiOlsicmVhZCIsIndyaXRlIl19XX0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJST0xFX1VTRVIiXX19.signature");
        ResponseEntity<KeycloakTokenResponse> exchangeResponse = new ResponseEntity<>(permissionResponse, HttpStatus.OK);
        when(restTemplate.exchange(eq("http://localhost:8080/auth/realms/test-realm/protocol/openid-connect/token"), eq(HttpMethod.POST), any(HttpEntity.class), eq(KeycloakTokenResponse.class)))
                .thenReturn(exchangeResponse);
        

        // When
        LoginResponse result = authService.authenticateUser(loginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getCompanyId()).isEqualTo(1L);
        assertThat(result.getCompanyName()).isEqualTo("Tech Corp");
        assertThat(result.getSessionToken()).isEqualTo("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemF0aW9uIjp7InBlcm1pc3Npb25zIjpbeyJzY29wZXMiOlsicmVhZCIsIndyaXRlIl19XX0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJST0xFX1VTRVIiXX19.signature");
        assertThat(result.getRefreshToken()).isEqualTo("mock-refresh-token");
        assertThat(result.getSessionExpire()).isEqualTo(3600);
        assertThat(result.getRefreshExpire()).isEqualTo(86400);

        verify(restTemplate).postForEntity(anyString(), any(), eq(KeycloakTokenResponse.class));
        verify(userService).findByEmail("test@example.com");
    }


    @Test
    @DisplayName("Should throw AuthenticationException when Keycloak call fails")
    void authenticateUser_WhenKeycloakCallFails_ShouldThrowException() {
        // Given
        setupKeycloakPropertiesMock();
        when(restTemplate.postForEntity(anyString(), any(), eq(KeycloakTokenResponse.class)))
                .thenThrow(new RuntimeException("Connection error"));

        // When & Then
        assertThatThrownBy(() -> authService.authenticateUser(loginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Authentication failed:");
    }

    @Test
    @DisplayName("Should throw AuthenticationException when user not found in database")
    void authenticateUser_WhenUserNotFound_ShouldThrowException() {
        // Given
        setupKeycloakPropertiesMock();
        ResponseEntity<KeycloakTokenResponse> responseEntity = 
                new ResponseEntity<>(keycloakTokenResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(eq("http://localhost:8080/auth/realms/test-realm/protocol/openid-connect/token"), any(HttpEntity.class), eq(KeycloakTokenResponse.class)))
                .thenReturn(responseEntity);
        when(userService.findByEmail("test@example.com"))
                .thenThrow(new RuntimeException("User not found"));

        // When & Then
        assertThatThrownBy(() -> authService.authenticateUser(loginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Authentication failed:");
    }

    @Test
    @DisplayName("Should refresh token successfully with valid refresh token")
    void refreshToken_WithValidToken_ShouldReturnLoginResponse() {
        // Given
        setupKeycloakPropertiesMock();
        String refreshToken = "valid-refresh-token";
        KeycloakTokenResponse newTokenResponse = new KeycloakTokenResponse();
        newTokenResponse.setToken("new-jwt-token");
        newTokenResponse.setRefreshToken("new-refresh-token");
        newTokenResponse.setExpiresIn(3600);
        newTokenResponse.setRefreshExpiresIn(86400);

        ResponseEntity<KeycloakTokenResponse> responseEntity = 
                new ResponseEntity<>(newTokenResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(eq("http://localhost:8080/auth/realms/test-realm/protocol/openid-connect/token"), any(HttpEntity.class), eq(KeycloakTokenResponse.class)))
                .thenReturn(responseEntity);

        // When
        LoginResponse result = authService.refreshToken(refreshToken);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSessionToken()).isEqualTo("new-jwt-token");
        assertThat(result.getRefreshToken()).isEqualTo("new-refresh-token");
        assertThat(result.getSessionExpire()).isEqualTo(86400); // Note: This seems to be using refreshExpiresIn
        assertThat(result.getRefreshExpire()).isEqualTo(86400);

        verify(restTemplate).postForEntity(anyString(), any(), eq(KeycloakTokenResponse.class));
    }


    @Test
    @DisplayName("Should throw AuthenticationException when refresh token call fails")
    void refreshToken_WhenKeycloakCallFails_ShouldThrowException() {
        // Given
        setupKeycloakPropertiesMock();
        String refreshToken = "valid-refresh-token";
        when(restTemplate.postForEntity(anyString(), any(), eq(KeycloakTokenResponse.class)))
                .thenThrow(new RuntimeException("Connection error"));

        // When & Then
        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Token refresh failed:");
    }


}