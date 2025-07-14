package tech.oorjaa.demoservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Keycloak properties configuration class
 * Contains configuration properties with prefix "keycloak"
 */
@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        /**
         * Base URL of the Keycloak server
         */
        String baseUrl,
        
        /**
         * Keycloak realm name
         */
        String realm,
        
        /**
         * Client ID for the application
         */
        String clientId,
        
        /**
         * Client secret for the application
         */
        String clientSecret) {
}