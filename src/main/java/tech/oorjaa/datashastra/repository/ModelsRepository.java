package tech.oorjaa.datashastra.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.oorjaa.datashastra.entity.Models;
import tech.oorjaa.datashastra.enums.ModelComplexity;
import tech.oorjaa.datashastra.enums.ModelType;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Models entity operations.
 * Provides caching for frequently accessed model data.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Repository
public interface ModelsRepository extends JpaRepository<Models, Long> {

    // Basic Active Models Queries
    @Cacheable(value = "models", key = "'active'")
    @Query("SELECT m FROM Models m WHERE m.isActive = true ORDER BY m.modelName")
    List<Models> findAllActive();
    
    @Cacheable(value = "models", key = "'recommended'")
    @Query("SELECT m FROM Models m WHERE m.isActive = true AND m.isRecommended = true ORDER BY m.modelName")
    List<Models> findRecommended();

    // Model Type Queries
    List<Models> findByModelTypeAndIsActiveTrue(ModelType modelType);
    
    Optional<Models> findByModelTypeAndIsActiveTrue(ModelType modelType, boolean isActive);

    // Complexity-Based Queries
    @Cacheable(value = "models", key = "#complexity.name()")
    List<Models> findByComplexityAndIsActiveTrueOrderByModelName(ModelComplexity complexity);
    
    @Query("SELECT m FROM Models m WHERE m.complexity IN :complexities AND m.isActive = true ORDER BY m.modelName")
    List<Models> findByComplexitiesAndIsActiveTrue(@Param("complexities") List<ModelComplexity> complexities);

    // Capability-Based Queries
    @Query("SELECT m FROM Models m WHERE m.handlesSeasonality = :handlesSeasonality AND m.isActive = true")
    List<Models> findBySeasonalityCapability(@Param("handlesSeasonality") boolean handlesSeasonality);
    
    @Query("SELECT m FROM Models m WHERE m.handlesMissingValues = :handlesMissingValues AND m.isActive = true")
    List<Models> findByMissingValuesCapability(@Param("handlesMissingValues") boolean handlesMissingValues);
    
    @Query("SELECT m FROM Models m WHERE m.handlesNonlinearity = :handlesNonlinearity AND m.isActive = true")
    List<Models> findByNonlinearityCapability(@Param("handlesNonlinearity") boolean handlesNonlinearity);

    // Performance-Based Queries
    @Query("""
        SELECT m FROM Models m 
        WHERE m.isActive = true 
        AND m.minDataPoints <= :dataPoints 
        ORDER BY m.complexity, m.modelName
        """)
    List<Models> findSuitableForDataSize(@Param("dataPoints") Integer dataPoints);
    
    @Query("""
        SELECT m FROM Models m 
        WHERE m.isActive = true 
        AND m.cpuIntensive = :cpuIntensive 
        ORDER BY m.trainingTimeMinutes
        """)
    List<Models> findByCpuRequirement(@Param("cpuIntensive") boolean cpuIntensive);
    
    @Query("SELECT m FROM Models m WHERE m.isActive = true AND m.gpuAccelerated = true")
    List<Models> findGpuAcceleratedModels();

    // Recommendation Queries
    @Query("""
        SELECT m FROM Models m 
        WHERE m.isActive = true 
        AND m.minDataPoints <= :dataPoints
        AND (:needsSeasonality = false OR m.handlesSeasonality = true)
        AND (:needsMissingValueHandling = false OR m.handlesMissingValues = true)
        AND (:needsNonlinearity = false OR m.handlesNonlinearity = true)
        ORDER BY m.isRecommended DESC, m.complexity, m.modelName
        """)
    List<Models> findRecommendedModelsForRequirements(
        @Param("dataPoints") Integer dataPoints,
        @Param("needsSeasonality") boolean needsSeasonality,
        @Param("needsMissingValueHandling") boolean needsMissingValueHandling,
        @Param("needsNonlinearity") boolean needsNonlinearity
    );

    // Model Selection AI Query
    @Query("""
        SELECT m FROM Models m 
        WHERE m.isActive = true 
        AND (:complexity IS NULL OR m.complexity = :complexity)
        AND (:maxTrainingTime IS NULL OR m.trainingTimeMinutes <= :maxTrainingTime)
        AND (:requireGpu = false OR m.gpuAccelerated = true)
        AND (:maxMemory IS NULL OR m.memoryRequirementsMb <= :maxMemory)
        ORDER BY m.isRecommended DESC, 
                 CASE WHEN :complexity IS NOT NULL THEN 0 ELSE 1 END,
                 m.trainingTimeMinutes
        """)
    List<Models> findModelsWithConstraints(
        @Param("complexity") ModelComplexity complexity,
        @Param("maxTrainingTime") Integer maxTrainingTime,
        @Param("requireGpu") boolean requireGpu,
        @Param("maxMemory") Integer maxMemory
    );

    // Global Support Queries
    @Query("SELECT m FROM Models m WHERE m.isActive = true AND m.supportsMultiCurrency = true")
    List<Models> findMultiCurrencyModels();
    
    @Query("SELECT m FROM Models m WHERE m.isActive = true AND m.supportsMultiTimezone = true")
    List<Models> findMultiTimezoneModels();

    // Statistics Queries
    @Query("SELECT COUNT(m) FROM Models m WHERE m.isActive = true")
    long countActiveModels();
    
    @Query("SELECT COUNT(m) FROM Models m WHERE m.isActive = true AND m.isRecommended = true")
    long countRecommendedModels();
    
    @Query("""
        SELECT m.complexity, COUNT(m) 
        FROM Models m 
        WHERE m.isActive = true 
        GROUP BY m.complexity 
        ORDER BY m.complexity
        """)
    List<Object[]> getComplexityDistribution();

    // Search Queries
    @Query("""
        SELECT m FROM Models m 
        WHERE m.isActive = true 
        AND (LOWER(m.modelName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
             OR LOWER(m.modelDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
             OR LOWER(m.bestFor) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
        ORDER BY m.isRecommended DESC, m.modelName
        """)
    List<Models> searchModels(@Param("searchTerm") String searchTerm);

    // Model Performance Analytics (would be populated by usage statistics)
    @Query("""
        SELECT m, AVG(f.accuracy), COUNT(f) 
        FROM Models m 
        LEFT JOIN Forecast f ON f.modelId = m.id AND f.status = 'COMPLETED'
        WHERE m.isActive = true 
        GROUP BY m 
        HAVING COUNT(f) >= :minUsage
        ORDER BY AVG(f.accuracy) DESC
        """)
    List<Object[]> getModelPerformanceStatistics(@Param("minUsage") long minUsage);

    // Validation Queries
    boolean existsByModelTypeAndIsActiveTrue(ModelType modelType);
    
    Optional<Models> findByModelNameAndIsActiveTrue(String modelName);

    // Configuration Queries for Admin
    @Query("SELECT m FROM Models m ORDER BY m.isActive DESC, m.modelName")
    List<Models> findAllForAdmin();
    
    @Query("SELECT DISTINCT m.accuracy FROM Models m WHERE m.isActive = true ORDER BY m.accuracy")
    List<String> findDistinctAccuracyTiers();
}