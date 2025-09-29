package tech.oorjaa.datashastra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.oorjaa.datashastra.entity.Forecast;
import tech.oorjaa.datashastra.enums.ForecastStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Forecast entity operations.
 * Provides custom queries for forecast management, version control, and performance tracking.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long>, JpaSpecificationExecutor<Forecast> {

    // Basic Tenant-Aware Queries
    List<Forecast> findByTenantId(Integer tenantId);
    
    Optional<Forecast> findByIdAndTenantId(Long id, Integer tenantId);
    
    @Query("SELECT COUNT(f) FROM Forecast f WHERE f.tenantId = :tenantId")
    long countByTenantId(@Param("tenantId") Integer tenantId);

    // Activity-Related Queries
    List<Forecast> findByActivityIdAndTenantIdOrderByVersionDesc(Long activityId, Integer tenantId);
    
    Optional<Forecast> findByActivityIdAndVersionAndTenantId(Long activityId, String version, Integer tenantId);
    
    @Query("SELECT COUNT(f) FROM Forecast f WHERE f.activityId = :activityId AND f.tenantId = :tenantId")
    long countByActivityIdAndTenantId(@Param("activityId") Long activityId, @Param("tenantId") Integer tenantId);

    // Status-Based Queries
    List<Forecast> findByStatusAndTenantIdOrderByCreatedDateDesc(ForecastStatus status, Integer tenantId);
    
    List<Forecast> findByStatusInAndTenantId(List<ForecastStatus> statuses, Integer tenantId);
    
    @Query("SELECT COUNT(f) FROM Forecast f WHERE f.status = :status AND f.tenantId = :tenantId")
    long countByStatusAndTenantId(@Param("status") ForecastStatus status, @Param("tenantId") Integer tenantId);

    // Version Management Queries
    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.activityId = :activityId 
        AND f.tenantId = :tenantId 
        ORDER BY f.createdDate DESC
        """)
    List<Forecast> findVersionsByActivity(@Param("activityId") Long activityId, @Param("tenantId") Integer tenantId);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.activityId = :activityId 
        AND f.status = 'COMPLETED' 
        AND f.tenantId = :tenantId 
        ORDER BY f.accuracy DESC
        """)
    List<Forecast> findBestVersionsByActivity(@Param("activityId") Long activityId, @Param("tenantId") Integer tenantId);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.activityId = :activityId 
        AND f.tenantId = :tenantId 
        ORDER BY f.createdDate DESC 
        LIMIT 1
        """)
    Optional<Forecast> findLatestVersionByActivity(@Param("activityId") Long activityId, @Param("tenantId") Integer tenantId);

    // Performance and Analytics Queries
    @Query("""
        SELECT AVG(f.accuracy) 
        FROM Forecast f 
        WHERE f.status = 'COMPLETED' 
        AND f.tenantId = :tenantId 
        AND f.accuracy IS NOT NULL
        """)
    Optional<BigDecimal> getAverageAccuracy(@Param("tenantId") Integer tenantId);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.status = 'COMPLETED' 
        AND f.accuracy >= :minAccuracy 
        AND f.tenantId = :tenantId 
        ORDER BY f.accuracy DESC
        """)
    List<Forecast> findHighPerformingForecasts(@Param("minAccuracy") BigDecimal minAccuracy, 
                                              @Param("tenantId") Integer tenantId);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.status = 'COMPLETED' 
        AND f.tenantId = :tenantId 
        ORDER BY f.accuracy DESC
        """)
    Page<Forecast> findTopPerformingForecasts(@Param("tenantId") Integer tenantId, Pageable pageable);

    // Model-Based Queries
    List<Forecast> findByModelIdAndTenantId(Long modelId, Integer tenantId);
    
    @Query("""
        SELECT f.modelName, COUNT(f), AVG(f.accuracy) 
        FROM Forecast f 
        WHERE f.status = 'COMPLETED' 
        AND f.tenantId = :tenantId 
        AND f.modelName IS NOT NULL 
        GROUP BY f.modelName 
        ORDER BY AVG(f.accuracy) DESC
        """)
    List<Object[]> getModelPerformanceStatistics(@Param("tenantId") Integer tenantId);

    // Training and Progress Queries
    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.status IN ('IN_PROGRESS') 
        AND f.tenantId = :tenantId 
        ORDER BY f.updatedDate ASC
        """)
    List<Forecast> findRunningForecasts(@Param("tenantId") Integer tenantId);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.status = 'FAILED' 
        AND f.tenantId = :tenantId 
        AND f.updatedDate >= :since 
        ORDER BY f.updatedDate DESC
        """)
    List<Forecast> findRecentFailedForecasts(@Param("tenantId") Integer tenantId, 
                                           @Param("since") LocalDateTime since);

    // Step-Based Progress Queries
    @Query("SELECT COUNT(f) FROM Forecast f WHERE f.currentStep = :stepNumber AND f.tenantId = :tenantId")
    long countByCurrentStep(@Param("stepNumber") Integer stepNumber, @Param("tenantId") Integer tenantId);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.currentStep BETWEEN :minStep AND :maxStep 
        AND f.status = 'DRAFT' 
        AND f.tenantId = :tenantId
        """)
    List<Forecast> findForecastsInStepRange(@Param("minStep") Integer minStep, 
                                          @Param("maxStep") Integer maxStep, 
                                          @Param("tenantId") Integer tenantId);

    // Time-Based Queries
    @Query("""
        SELECT COUNT(f) 
        FROM Forecast f 
        WHERE f.status = 'COMPLETED' 
        AND f.completedAt >= :fromDate 
        AND f.tenantId = :tenantId
        """)
    long countCompletedSince(@Param("tenantId") Integer tenantId, @Param("fromDate") LocalDateTime fromDate);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.status = 'IN_PROGRESS' 
        AND f.updatedDate < :cutoffTime 
        AND f.tenantId = :tenantId
        """)
    List<Forecast> findStuckForecasts(@Param("tenantId") Integer tenantId, 
                                    @Param("cutoffTime") LocalDateTime cutoffTime);

    // Performance Metrics Queries
    @Query("""
        SELECT AVG(f.trainingDuration) 
        FROM Forecast f 
        WHERE f.status = 'COMPLETED' 
        AND f.trainingDuration IS NOT NULL 
        AND f.tenantId = :tenantId
        """)
    Optional<Double> getAverageTrainingDuration(@Param("tenantId") Integer tenantId);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.trainingDuration > :maxDuration 
        AND f.status = 'COMPLETED' 
        AND f.tenantId = :tenantId 
        ORDER BY f.trainingDuration DESC
        """)
    List<Forecast> findSlowForecasts(@Param("maxDuration") Long maxDuration, 
                                   @Param("tenantId") Integer tenantId);

    // Data Volume and Configuration Queries
    @Query("""
        SELECT AVG(f.trainingDataPoints) 
        FROM Forecast f 
        WHERE f.status = 'COMPLETED' 
        AND f.trainingDataPoints IS NOT NULL 
        AND f.tenantId = :tenantId
        """)
    Optional<Double> getAverageDataPoints(@Param("tenantId") Integer tenantId);

    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.featureCount > :minFeatures 
        AND f.status = 'COMPLETED' 
        AND f.tenantId = :tenantId 
        ORDER BY f.accuracy DESC
        """)
    List<Forecast> findHighFeatureForecasts(@Param("minFeatures") Integer minFeatures, 
                                          @Param("tenantId") Integer tenantId);

    // Update Operations
    @Modifying
    @Transactional
    @Query("""
        UPDATE Forecast f 
        SET f.status = :newStatus, 
            f.updatedDate = CURRENT_TIMESTAMP, 
            f.updatedBy = :updatedBy 
        WHERE f.id = :forecastId
        """)
    int updateForecastStatus(@Param("forecastId") Long forecastId, 
                           @Param("newStatus") ForecastStatus newStatus, 
                           @Param("updatedBy") String updatedBy);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Forecast f 
        SET f.currentStep = :stepNumber, 
            f.updatedDate = CURRENT_TIMESTAMP, 
            f.updatedBy = :updatedBy 
        WHERE f.id = :forecastId
        """)
    int updateCurrentStep(@Param("forecastId") Long forecastId, 
                        @Param("stepNumber") Integer stepNumber, 
                        @Param("updatedBy") String updatedBy);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Forecast f 
        SET f.accuracy = :accuracy, 
            f.mae = :mae, 
            f.rmse = :rmse, 
            f.mape = :mape, 
            f.r2Score = :r2Score,
            f.status = 'COMPLETED',
            f.completedAt = CURRENT_TIMESTAMP,
            f.updatedDate = CURRENT_TIMESTAMP,
            f.updatedBy = :updatedBy
        WHERE f.id = :forecastId
        """)
    int updatePerformanceMetrics(@Param("forecastId") Long forecastId,
                               @Param("accuracy") BigDecimal accuracy,
                               @Param("mae") BigDecimal mae,
                               @Param("rmse") BigDecimal rmse,
                               @Param("mape") BigDecimal mape,
                               @Param("r2Score") BigDecimal r2Score,
                               @Param("updatedBy") String updatedBy);

    // Cleanup Operations
    @Modifying
    @Transactional
    @Query("DELETE FROM Forecast f WHERE f.status = 'DRAFT' AND f.updatedDate < :cutoffDate AND f.tenantId = :tenantId")
    int deleteDraftForecastsOlderThan(@Param("tenantId") Integer tenantId, 
                                    @Param("cutoffDate") LocalDateTime cutoffDate);

    @Modifying
    @Transactional
    @Query("UPDATE Forecast f SET f.status = 'CANCELLED' WHERE f.status = 'IN_PROGRESS' AND f.updatedDate < :cutoffDate AND f.tenantId = :tenantId")
    int cancelStuckForecasts(@Param("tenantId") Integer tenantId, 
                           @Param("cutoffDate") LocalDateTime cutoffDate);

    // Validation and Existence Queries
    boolean existsByActivityIdAndVersionAndTenantId(Long activityId, String version, Integer tenantId);
    
    boolean existsByIdAndTenantId(Long id, Integer tenantId);

    @Query("SELECT f.version FROM Forecast f WHERE f.activityId = :activityId AND f.tenantId = :tenantId ORDER BY f.createdDate")
    List<String> findVersionsByActivityId(@Param("activityId") Long activityId, @Param("tenantId") Integer tenantId);

    // Dashboard Statistics
    @Query("""
        SELECT 
            COUNT(f) as total,
            COUNT(CASE WHEN f.status = 'COMPLETED' THEN 1 END) as completed,
            COUNT(CASE WHEN f.status = 'IN_PROGRESS' THEN 1 END) as inProgress,
            COUNT(CASE WHEN f.status = 'FAILED' THEN 1 END) as failed,
            AVG(CASE WHEN f.status = 'COMPLETED' THEN f.accuracy END) as avgAccuracy
        FROM Forecast f 
        WHERE f.tenantId = :tenantId
        """)
    Object getForecastStatistics(@Param("tenantId") Integer tenantId);

    // Global Currency and Locale Queries
    List<Forecast> findByForecastCurrencyAndTenantId(String currency, Integer tenantId);
    
    List<Forecast> findByForecastTimezoneAndTenantId(String timezone, Integer tenantId);

    @Query("SELECT DISTINCT f.forecastCurrency FROM Forecast f WHERE f.tenantId = :tenantId AND f.forecastCurrency IS NOT NULL")
    List<String> findDistinctCurrencies(@Param("tenantId") Integer tenantId);

    // Advanced Search
    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.tenantId = :tenantId 
        AND (:activityId IS NULL OR f.activityId = :activityId)
        AND (:status IS NULL OR f.status = :status)
        AND (:modelId IS NULL OR f.modelId = :modelId)
        AND (:minAccuracy IS NULL OR f.accuracy >= :minAccuracy)
        AND (:maxAccuracy IS NULL OR f.accuracy <= :maxAccuracy)
        ORDER BY f.updatedDate DESC
        """)
    Page<Forecast> findForecastsWithFilters(@Param("tenantId") Integer tenantId,
                                          @Param("activityId") Long activityId,
                                          @Param("status") ForecastStatus status,
                                          @Param("modelId") Long modelId,
                                          @Param("minAccuracy") BigDecimal minAccuracy,
                                          @Param("maxAccuracy") BigDecimal maxAccuracy,
                                          Pageable pageable);

    // Version Comparison Queries
    @Query("""
        SELECT f FROM Forecast f 
        WHERE f.id IN :forecastIds 
        AND f.tenantId = :tenantId 
        ORDER BY f.accuracy DESC
        """)
    List<Forecast> findForecastsForComparison(@Param("forecastIds") List<Long> forecastIds, 
                                            @Param("tenantId") Integer tenantId);
}