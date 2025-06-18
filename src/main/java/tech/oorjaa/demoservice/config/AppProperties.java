package tech.oorjaa.demoservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application properties configuration class
 * Contains configuration properties with prefix "app"
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        /**
         * Flag to control exposure of Swagger UI and API documentation
         */
        boolean exposeSwagger) {
}