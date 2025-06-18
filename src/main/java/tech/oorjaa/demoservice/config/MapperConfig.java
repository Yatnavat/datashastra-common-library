package tech.oorjaa.demoservice.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for handling lazy loading in MapStruct mappers.
 * Provides access to EntityManager for resolving lazy-loaded relationships
 * when mapping entities to DTOs.
 */
@Configuration
public class MapperConfig {

    /**
     * Entity manager for accessing JPA functionality in mappers
     */
    @PersistenceContext
    private EntityManager entityManager;
}
