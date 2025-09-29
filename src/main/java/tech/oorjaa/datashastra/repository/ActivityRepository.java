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
import tech.oorjaa.datashastra.entity.Activity;
import tech.oorjaa.datashastra.enums.ActivityPriority;
import tech.oorjaa.datashastra.enums.ActivityStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Activity entity operations.
 * Provides custom queries for activity management and dashboard statistics.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {

    // Basic Tenant-Aware Queries
    List<Activity> findByTenantId(Integer tenantId);
    
    Optional<Activity> findByIdAndTenantId(Long id, Integer tenantId);
    
    @Query("SELECT COUNT(a) FROM Activity a WHERE a.tenantId = :tenantId")
    long countByTenantId(@Param("tenantId") Integer tenantId);

    // Status-Based Queries
    List<Activity> findByStatusAndTenantId(ActivityStatus status, Integer tenantId);
    
    @Query("SELECT COUNT(a) FROM Activity a WHERE a.status = :status AND a.tenantId = :tenantId")
    long countByStatusAndTenantId(@Param("status") ActivityStatus status, @Param("tenantId") Integer tenantId);
    
    List<Activity> findByStatusInAndTenantId(List<ActivityStatus> statuses, Integer tenantId);

    // Client and Project Queries
    List<Activity> findByClientIdAndTenantId(Long clientId, Integer tenantId);
    
    List<Activity> findByProjectIdAndTenantId(Long projectId, Integer tenantId);
    
    List<Activity> findByClientIdAndProjectIdAndTenantId(Long clientId, Long projectId, Integer tenantId);
    
    @Query("SELECT a FROM Activity a WHERE a.clientId = :clientId AND a.projectId = :projectId AND a.name = :name AND a.tenantId = :tenantId")
    Optional<Activity> findByClientProjectAndName(@Param("clientId") Long clientId, 
                                                 @Param("projectId") Long projectId, 
                                                 @Param("name") String name, 
                                                 @Param("tenantId") Integer tenantId);

    // Priority-Based Queries
    List<Activity> findByPriorityAndTenantIdOrderByCreatedDateDesc(ActivityPriority priority, Integer tenantId);
    
    @Query("SELECT a FROM Activity a WHERE a.priority = 'HIGH' AND a.status = 'ACTIVE' AND a.tenantId = :tenantId ORDER BY a.createdDate DESC")
    List<Activity> findHighPriorityActiveActivities(@Param("tenantId") Integer tenantId);

    // Search and Filter Queries
    @Query("""
        SELECT a FROM Activity a 
        WHERE a.tenantId = :tenantId 
        AND (:status IS NULL OR a.status = :status)
        AND (:priority IS NULL OR a.priority = :priority)
        AND (:clientId IS NULL OR a.clientId = :clientId)
        AND (:projectId IS NULL OR a.projectId = :projectId)
        AND (:searchTerm IS NULL OR 
             LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR 
             LOWER(a.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
        ORDER BY a.updatedDate DESC
        """)
    Page<Activity> findActivitiesWithFilters(@Param("tenantId") Integer tenantId,
                                           @Param("status") ActivityStatus status,
                                           @Param("priority") ActivityPriority priority,
                                           @Param("clientId") Long clientId,
                                           @Param("projectId") Long projectId,
                                           @Param("searchTerm") String searchTerm,
                                           Pageable pageable);

    // Dashboard Statistics Queries
    @Query("""
        SELECT COUNT(DISTINCT a.id) 
        FROM Activity a 
        WHERE a.tenantId = :tenantId
        """)
    long getTotalActivitiesCount(@Param("tenantId") Integer tenantId);

    @Query("""
        SELECT COUNT(DISTINCT a.clientId) 
        FROM Activity a 
        WHERE a.tenantId = :tenantId
        """)
    long getUniqueClientsCount(@Param("tenantId") Integer tenantId);

    @Query("""
        SELECT SUM(a.forecastCount) 
        FROM Activity a 
        WHERE a.tenantId = :tenantId
        """)
    Long getTotalForecastsCount(@Param("tenantId") Integer tenantId);

    @Query("""
        SELECT COUNT(a) 
        FROM Activity a 
        WHERE a.status = 'ACTIVE' AND a.tenantId = :tenantId
        """)
    long getActiveActivitiesCount(@Param("tenantId") Integer tenantId);

    // Growth and Trend Queries
    @Query("""
        SELECT COUNT(a) 
        FROM Activity a 
        WHERE a.tenantId = :tenantId 
        AND a.createdDate >= :fromDate
        """)
    long countActivitiesCreatedSince(@Param("tenantId") Integer tenantId, 
                                    @Param("fromDate") LocalDateTime fromDate);

    @Query("""
        SELECT COUNT(a) 
        FROM Activity a 
        WHERE a.tenantId = :tenantId 
        AND a.createdDate BETWEEN :startDate AND :endDate
        """)
    long countActivitiesCreatedBetween(@Param("tenantId") Integer tenantId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    // Recent Activities Query
    @Query("""
        SELECT a FROM Activity a 
        WHERE a.tenantId = :tenantId 
        ORDER BY a.updatedDate DESC
        """)
    Page<Activity> findRecentActivities(@Param("tenantId") Integer tenantId, Pageable pageable);

    // User-Specific Queries
    List<Activity> findByCreatedByUserIdAndTenantIdOrderByCreatedDateDesc(Long createdByUserId, Integer tenantId);

    @Query("""
        SELECT a FROM Activity a 
        WHERE a.createdByUserId = :userId 
        AND a.tenantId = :tenantId 
        AND a.status IN ('ACTIVE', 'ON_HOLD')
        ORDER BY a.priority DESC, a.updatedDate DESC
        """)
    List<Activity> findUserActiveActivities(@Param("userId") Long userId, @Param("tenantId") Integer tenantId);

    // Industry-Based Queries
    List<Activity> findByIndustryAndTenantId(String industry, Integer tenantId);

    @Query("""
        SELECT a.industry, COUNT(a) 
        FROM Activity a 
        WHERE a.tenantId = :tenantId 
        AND a.industry IS NOT NULL
        GROUP BY a.industry 
        ORDER BY COUNT(a) DESC
        """)
    List<Object[]> getIndustryStatistics(@Param("tenantId") Integer tenantId);

    // Full-Text Search Query (PostgreSQL)
    @Query(value = """
        SELECT a.* FROM activity a 
        WHERE a.tenant_id = :tenantId 
        AND (
            to_tsvector('english', a.name || ' ' || COALESCE(a.description, '') || ' ' || COALESCE(a.industry, '')) 
            @@ plainto_tsquery('english', :searchTerm)
        )
        ORDER BY ts_rank(
            to_tsvector('english', a.name || ' ' || COALESCE(a.description, '') || ' ' || COALESCE(a.industry, '')), 
            plainto_tsquery('english', :searchTerm)
        ) DESC
        """, nativeQuery = true)
    List<Activity> searchActivitiesFullText(@Param("tenantId") Integer tenantId, 
                                          @Param("searchTerm") String searchTerm);

    // Update Queries
    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.forecastCount = a.forecastCount + 1 WHERE a.id = :activityId")
    int incrementForecastCount(@Param("activityId") Long activityId);

    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.forecastCount = a.forecastCount - 1 WHERE a.id = :activityId AND a.forecastCount > 0")
    int decrementForecastCount(@Param("activityId") Long activityId);

    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.status = :newStatus, a.updatedDate = CURRENT_TIMESTAMP, a.updatedBy = :updatedBy WHERE a.id = :activityId")
    int updateActivityStatus(@Param("activityId") Long activityId, 
                           @Param("newStatus") ActivityStatus newStatus, 
                           @Param("updatedBy") String updatedBy);

    // Bulk Operations
    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.status = 'ARCHIVED', a.updatedDate = CURRENT_TIMESTAMP WHERE a.clientId = :clientId AND a.tenantId = :tenantId")
    int archiveActivitiesByClient(@Param("clientId") Long clientId, @Param("tenantId") Integer tenantId);

    // Data Quality and Validation Queries
    @Query("SELECT a FROM Activity a WHERE a.tenantId = :tenantId AND (a.name IS NULL OR a.name = '' OR a.clientId IS NULL OR a.projectId IS NULL)")
    List<Activity> findIncompleteActivities(@Param("tenantId") Integer tenantId);

    @Query("SELECT a.name FROM Activity a WHERE a.clientId = :clientId AND a.projectId = :projectId AND a.tenantId = :tenantId")
    List<String> findActivityNamesByClientAndProject(@Param("clientId") Long clientId, 
                                                   @Param("projectId") Long projectId, 
                                                   @Param("tenantId") Integer tenantId);

    // Performance Monitoring Queries
    @Query("""
        SELECT a FROM Activity a 
        WHERE a.tenantId = :tenantId 
        AND a.updatedDate < :cutoffDate 
        AND a.status = 'ACTIVE'
        """)
    List<Activity> findStaleActiveActivities(@Param("tenantId") Integer tenantId, 
                                           @Param("cutoffDate") LocalDateTime cutoffDate);

    // Custom Summary Query (returns Object array for now - can be converted to DTO in service layer)
    @Query("""
        SELECT a.id, a.name, a.status, a.priority, a.forecastCount, 
               a.createdDate, a.updatedDate, a.clientId, a.projectId
        FROM Activity a 
        WHERE a.tenantId = :tenantId 
        ORDER BY a.updatedDate DESC
        """)
    List<Object[]> findActivitySummaries(@Param("tenantId") Integer tenantId);

    // Global Usage Queries
    @Query("SELECT a FROM Activity a WHERE a.baseCurrency = :currency AND a.tenantId = :tenantId")
    List<Activity> findByBaseCurrency(@Param("currency") String currency, @Param("tenantId") Integer tenantId);

    @Query("SELECT a FROM Activity a WHERE a.businessTimezone = :timezone AND a.tenantId = :tenantId")
    List<Activity> findByBusinessTimezone(@Param("timezone") String timezone, @Param("tenantId") Integer tenantId);

    // Exists Queries for Validation
    boolean existsByNameAndClientIdAndProjectIdAndTenantId(String name, Long clientId, Long projectId, Integer tenantId);
    
    boolean existsByIdAndTenantId(Long id, Integer tenantId);
}