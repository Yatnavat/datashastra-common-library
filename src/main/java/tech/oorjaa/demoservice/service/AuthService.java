package tech.oorjaa.demoservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tech.oorjaa.demoservice.dto.KeycloakTokenResponse;
import tech.oorjaa.demoservice.dto.LoginRequest;
import tech.oorjaa.demoservice.dto.LoginResponse;
import tech.oorjaa.demoservice.entity.User;
import tech.oorjaa.demoservice.exception.AuthenticationException;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.keycloak.OAuth2Constants.*;

@Service
public class AuthService {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.resourceserver.jwt.token-url}")
    private String tokenUrl;

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public AuthService(RestTemplate restTemplate, UserService userService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    /**
     * Get the current user's roles from the security context
     *
     * @return A collection of role names
     */
    public java.util.Collection<String> getCurrentUserRoles() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    /**
     * Authenticate a user using Keycloak and return user details with token
     *
     * @param loginRequest The login request containing email and password
     * @return A LoginResponse containing user details and tokens
     * @throws AuthenticationException If authentication fails
     */
    public LoginResponse authenticateUser(LoginRequest loginRequest) throws AuthenticationException {
        // Create request body for Keycloak token endpoint
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "password");
        formData.add("username", loginRequest.getUsername());
        formData.add("password", loginRequest.getPassword());

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create HTTP entity with headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            // Make the request to Keycloak token endpoint
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.postForEntity(
                    tokenUrl,
                    requestEntity,
                    KeycloakTokenResponse.class
            );

            // Extract tokens and other info from response
            KeycloakTokenResponse responseBody = response.getBody();
            if (responseBody != null && responseBody.getToken() != null) {
                // Get user details from our database
                User user = userService.findByEmail(loginRequest.getUsername());

                // Create response object
                LoginResponse loginResponse = getLoginResponse(user, responseBody);

                // Add role information
                setUserPermissions(loginResponse, responseBody.getToken());

                return loginResponse;
            } else {
                throw new AuthenticationException("Invalid credentials or authentication failed");
            }
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed: " + e.getMessage(), e);
        }
    }

    private static LoginResponse getLoginResponse(User user, KeycloakTokenResponse responseBody) {
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setUserId(user.getId());
        loginResponse.setFirstName(user.getFirstName());
        loginResponse.setLastName(user.getLastName() != null ? user.getLastName() : "");
        loginResponse.setEmail(user.getEmail());
        loginResponse.setCompanyId(user.getCompany().getId());
        loginResponse.setCompanyName(user.getCompany().getName());

        // Set Keycloak fields
        loginResponse.setSessionToken(responseBody.getToken());
        loginResponse.setRefreshToken(responseBody.getRefreshToken());
        loginResponse.setSessionExpire(responseBody.getExpiresIn());
        loginResponse.setRefreshExpire(responseBody.getRefreshExpiresIn());
        return loginResponse;
    }

    /**
     * Refresh an access token using a refresh token
     *
     * @param refreshToken The refresh token
     * @return A new LoginResponse with updated tokens
     * @throws AuthenticationException If token refresh fails
     */
    public LoginResponse refreshToken(String refreshToken) throws AuthenticationException {
        // Create request body for Keycloak token endpoint
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create HTTP entity with headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            // Make the request to Keycloak token endpoint
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.postForEntity(
                    tokenUrl,
                    requestEntity,
                    KeycloakTokenResponse.class
            );

            // Extract tokens and other info from response
            KeycloakTokenResponse responseBody = response.getBody();
            if (responseBody != null && responseBody.getToken() != null) {
                // We don't have the user info in this context, so return a minimal response
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setSessionToken(responseBody.getToken());
                loginResponse.setRefreshToken(responseBody.getRefreshToken());
                loginResponse.setSessionExpire(responseBody.getRefreshExpiresIn());
                loginResponse.setRefreshExpire(responseBody.getRefreshExpiresIn());
                return loginResponse;
            } else {
                throw new AuthenticationException("Failed to refresh token");
            }
        } catch (Exception e) {
            throw new AuthenticationException("Token refresh failed: " + e.getMessage(), e);
        }
    }

    private void setUserPermissions(LoginResponse loginResponse, String token) throws JsonProcessingException {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add(AUDIENCE, clientId);
        request.add(GRANT_TYPE, UMA_GRANT_TYPE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);

        HttpEntity<Object> entity = new HttpEntity<>(request, headers);

        KeycloakTokenResponse response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                KeycloakTokenResponse.class
        ).getBody();

        String payloadJson = new String(Base64.getUrlDecoder().decode(response.getToken().split("\\.")[1]));
        JsonNode payload = objectMapper.readValue(payloadJson, JsonNode.class);
        List<JsonNode> scopes = payload.get("authorization").get("permissions").findParents("scopes");

        List<String> scopeList = scopes.stream()
                .flatMap(scope -> StreamSupport.stream(scope.get("scopes").spliterator(), false))
                .map(JsonNode::asText)
                .collect(Collectors.toList());

        JsonNode roles = payload.get("realm_access").get("roles");
        List<String> roleList = StreamSupport.stream(roles.spliterator(), false).map(JsonNode::asText).collect(Collectors.toList());

        loginResponse.setScopes(scopeList);
        loginResponse.setUserRoles(roleList);
    }
}
