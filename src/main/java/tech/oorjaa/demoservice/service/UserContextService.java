package tech.oorjaa.demoservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import tech.oorjaa.demoservice.entity.User;
import tech.oorjaa.demoservice.repository.UserRepository;

/**
 * Service to extract user details from the security context
 */
@Service
@RequiredArgsConstructor
public class UserContextService {

    private final UserRepository userRepository;

    /**
     * Get the company ID of the currently authenticated user
     *
     * @return the company ID from the database based on Keycloak user ID
     * @throws IllegalStateException if user not found or authentication is not available
     */
    public Long getCurrentUserCompanyId() {
        return getCurrentUser().getCompany().getId();
    }

    /**
     * Get the username of the currently authenticated user
     *
     * @return the username
     * @throws IllegalStateException if user not found or authentication is not available
     */
    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * Get the user ID of the currently authenticated user
     *
     * @return the user ID from the database based on Keycloak user ID
     * @throws IllegalStateException if user not found or authentication is not available
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Get the user of the currently authenticated user
     *
     * @return the user from the database based on Keycloak user ID
     * @throws IllegalStateException if user not found or authentication is not available
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken bearerAuth) {
            // Get the subject (user ID) from token
            String keycloakUserId = bearerAuth.getTokenAttributes().get("sub").toString();

            // Find user in database by Keycloak ID
            return userRepository.findByKeycloakId(keycloakUserId)
                    .orElseThrow(() -> new IllegalStateException("User not found in database for Keycloak ID: " + keycloakUserId));
        }

        throw new IllegalStateException("Unable to determine user ID from authentication");
    }
}
