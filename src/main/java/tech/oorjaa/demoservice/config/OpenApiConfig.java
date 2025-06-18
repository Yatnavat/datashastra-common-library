package tech.oorjaa.demoservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI (Swagger) documentation
 */
@Configuration
public class OpenApiConfig {

    @Value("${springdoc.oAuthFlow.authorizationUrl}")
    private String authorizationUrl;

    @Value("${springdoc.oAuthFlow.tokenUrl}")
    private String tokenUrl;

    final String securitySchemeName = "OAuth2";

    /**
     * Configures OpenAPI documentation with OAuth2 security scheme using Keycloak
     *
     * @return OpenAPI configuration instance with security requirements and OAuth2 flow settings
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Demo Service API")
                        .description("API documentation for the Demo Service application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Oorjaa Tech")
                                .url("https://oorjaa.tech")
                                .email("support@oorjaa.tech"))
                        .license(new License()
                                .name("Private")))
                .addSecurityItem(new SecurityRequirement().addList("keycloak"))
                .components(new Components()
                        .addSecuritySchemes("keycloak",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl(authorizationUrl)
                                                        .tokenUrl(tokenUrl)
                                                )
                                        )
                        )
                );
    }
}