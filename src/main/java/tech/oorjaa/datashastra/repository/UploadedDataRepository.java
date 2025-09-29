package tech.oorjaa.datashastra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.oorjaa.datashastra.entity.UploadedData;
import tech.oorjaa.datashastra.enums.UploadDataType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UploadedData entity operations.
 * Provides queries for file management, processing status, and cleanup operations.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Repository
public interface UploadedDataRepository extends JpaRepository<UploadedData, Long> {

    // Basic Tenant-Aware Queries
    List<UploadedData> findByTenantId(Integer tenantId);
    
    Optional<UploadedData> findByIdAndTenantId(Long id, Integer tenantId);

    // Activity-Related Queries
    List<UploadedData> findByActivityIdAndTenantId(Long activityId, Integer tenantId);
    
    Optional<UploadedData> findTopByActivityIdAndTenantIdOrderByTimestampDesc(Long activityId, Integer tenantId);

    // Upload Type Queries
    List<UploadedData> findByUploadDataTypeAndTenantId(UploadDataType uploadDataType, Integer tenantId);

    // Processing Status Queries
    List<UploadedData> findByIsProcessedAndTenantId(boolean isProcessed, Integer tenantId);
    
    @Query("SELECT u FROM UploadedData u WHERE u.isProcessed = false AND u.processingError IS NULL AND u.tenantId = :tenantId")
    List<UploadedData> findPendingProcessing(@Param("tenantId") Integer tenantId);
    
    @Query("SELECT u FROM UploadedData u WHERE u.processingError IS NOT NULL AND u.tenantId = :tenantId ORDER BY u.timestamp DESC")
    List<UploadedData> findProcessingErrors(@Param("tenantId") Integer tenantId);

    // Quality-Based Queries
    @Query("SELECT u FROM UploadedData u WHERE u.dataQualityScore >= :minScore AND u.tenantId = :tenantId ORDER BY u.dataQualityScore DESC")
    List<UploadedData> findByMinimumQuality(@Param("minScore") java.math.BigDecimal minScore, 
                                          @Param("tenantId") Integer tenantId);
    
    @Query("SELECT u FROM UploadedData u WHERE u.dataQualityScore < :maxScore AND u.tenantId = :tenantId ORDER BY u.dataQualityScore ASC")
    List<UploadedData> findLowQualityFiles(@Param("maxScore") java.math.BigDecimal maxScore, 
                                         @Param("tenantId") Integer tenantId);

    // File Size and Type Queries
    @Query("SELECT u FROM UploadedData u WHERE u.fileSize > :sizeThreshold AND u.tenantId = :tenantId ORDER BY u.fileSize DESC")
    List<UploadedData> findLargeFiles(@Param("sizeThreshold") Long sizeThreshold, 
                                    @Param("tenantId") Integer tenantId);
    
    @Query("SELECT u FROM UploadedData u WHERE u.contentType IN :contentTypes AND u.tenantId = :tenantId")
    List<UploadedData> findByContentTypes(@Param("contentTypes") List<String> contentTypes, 
                                        @Param("tenantId") Integer tenantId);

    // User-Based Queries
    List<UploadedData> findByUploadedByUserIdAndTenantIdOrderByTimestampDesc(Long uploadedByUserId, Integer tenantId);
    
    @Query("SELECT COUNT(u) FROM UploadedData u WHERE u.uploadedByUserId = :userId AND u.tenantId = :tenantId")
    long countByUploadedByUserIdAndTenantId(@Param("userId") Long userId, @Param("tenantId") Integer tenantId);

    // Time-Based Queries
    @Query("SELECT u FROM UploadedData u WHERE u.timestamp >= :fromDate AND u.tenantId = :tenantId ORDER BY u.timestamp DESC")
    List<UploadedData> findUploadedSince(@Param("fromDate") LocalDateTime fromDate, 
                                       @Param("tenantId") Integer tenantId);
    
    @Query("SELECT u FROM UploadedData u WHERE u.timestamp BETWEEN :startDate AND :endDate AND u.tenantId = :tenantId")
    List<UploadedData> findUploadedBetween(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate, 
                                         @Param("tenantId") Integer tenantId);

    // Retention and Cleanup Queries
    @Query("SELECT u FROM UploadedData u WHERE u.retentionExpiresAt <= :currentTime AND u.legalHold = false AND u.tenantId = :tenantId")
    List<UploadedData> findExpiredFiles(@Param("currentTime") LocalDateTime currentTime, 
                                       @Param("tenantId") Integer tenantId);
    
    @Query("SELECT u FROM UploadedData u WHERE u.legalHold = true AND u.tenantId = :tenantId")
    List<UploadedData> findFilesOnLegalHold(@Param("tenantId") Integer tenantId);

    // Data Residency Queries
    List<UploadedData> findByDataResidencyRegionAndTenantId(String dataResidencyRegion, Integer tenantId);
    
    @Query("SELECT u FROM UploadedData u WHERE u.crossBorderTransferApproved = false AND u.dataResidencyRegion != :targetRegion AND u.tenantId = :tenantId")
    List<UploadedData> findRestrictedForRegion(@Param("targetRegion") String targetRegion, 
                                             @Param("tenantId") Integer tenantId);

    // Statistics Queries
    @Query("SELECT COUNT(u) FROM UploadedData u WHERE u.tenantId = :tenantId")
    long getTotalFilesCount(@Param("tenantId") Integer tenantId);
    
    @Query("SELECT SUM(u.fileSize) FROM UploadedData u WHERE u.tenantId = :tenantId")
    Long getTotalStorageUsed(@Param("tenantId") Integer tenantId);
    
    @Query("SELECT AVG(u.dataQualityScore) FROM UploadedData u WHERE u.dataQualityScore IS NOT NULL AND u.tenantId = :tenantId")
    Optional<java.math.BigDecimal> getAverageDataQuality(@Param("tenantId") Integer tenantId);

    // Processing Performance Queries
    @Query("SELECT u FROM UploadedData u WHERE u.processingStartedAt IS NOT NULL AND u.processingCompletedAt IS NULL AND u.tenantId = :tenantId")
    List<UploadedData> findStuckInProcessing(@Param("tenantId") Integer tenantId);
    
    @Query("SELECT AVG(EXTRACT(EPOCH FROM (u.processingCompletedAt - u.processingStartedAt))) FROM UploadedData u WHERE u.processingCompletedAt IS NOT NULL AND u.processingStartedAt IS NOT NULL AND u.tenantId = :tenantId")
    Optional<Double> getAverageProcessingTimeSeconds(@Param("tenantId") Integer tenantId);

    // Search and Filter Queries
    @Query("""
        SELECT u FROM UploadedData u 
        WHERE u.tenantId = :tenantId 
        AND (:activityId IS NULL OR u.activityId = :activityId)
        AND (:uploadType IS NULL OR u.uploadDataType = :uploadType)
        AND (:isProcessed IS NULL OR u.isProcessed = :isProcessed)
        AND (:minQuality IS NULL OR u.dataQualityScore >= :minQuality)
        AND (:searchTerm IS NULL OR 
             LOWER(u.fileName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR 
             LOWER(u.originalFileName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
        ORDER BY u.timestamp DESC
        """)
    Page<UploadedData> findFilesWithFilters(
        @Param("tenantId") Integer tenantId,
        @Param("activityId") Long activityId,
        @Param("uploadType") UploadDataType uploadType,
        @Param("isProcessed") Boolean isProcessed,
        @Param("minQuality") java.math.BigDecimal minQuality,
        @Param("searchTerm") String searchTerm,
        Pageable pageable
    );

    // Update Operations
    @Modifying
    @Transactional
    @Query("UPDATE UploadedData u SET u.isProcessed = true, u.processingCompletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    int markAsProcessed(@Param("id") Long id);
    
    @Modifying
    @Transactional
    @Query("UPDATE UploadedData u SET u.processingError = :error, u.processingCompletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    int markAsProcessingFailed(@Param("id") Long id, @Param("error") String error);
    
    @Modifying
    @Transactional
    @Query("UPDATE UploadedData u SET u.dataQualityScore = :score WHERE u.id = :id")
    int updateDataQualityScore(@Param("id") Long id, @Param("score") java.math.BigDecimal score);

    // Bulk Operations
    @Modifying
    @Transactional
    @Query("DELETE FROM UploadedData u WHERE u.retentionExpiresAt <= :currentTime AND u.legalHold = false AND u.tenantId = :tenantId")
    int deleteExpiredFiles(@Param("currentTime") LocalDateTime currentTime, 
                          @Param("tenantId") Integer tenantId);
    
    @Modifying
    @Transactional
    @Query("UPDATE UploadedData u SET u.legalHold = :legalHold WHERE u.activityId = :activityId AND u.tenantId = :tenantId")
    int updateLegalHoldByActivity(@Param("activityId") Long activityId, 
                                 @Param("legalHold") boolean legalHold, 
                                 @Param("tenantId") Integer tenantId);

    // Validation Queries
    boolean existsByChecksumAndTenantId(String checksum, Integer tenantId);
    
    boolean existsByIdAndTenantId(Long id, Integer tenantId);
    
    @Query("SELECT u FROM UploadedData u WHERE u.checksum = :checksum AND u.tenantId = :tenantId")
    List<UploadedData> findDuplicateFiles(@Param("checksum") String checksum, 
                                        @Param("tenantId") Integer tenantId);

    // Dashboard Statistics
    @Query("""
        SELECT 
            COUNT(u) as totalFiles,
            COUNT(CASE WHEN u.isProcessed = true THEN 1 END) as processedFiles,
            COUNT(CASE WHEN u.processingError IS NOT NULL THEN 1 END) as errorFiles,
            SUM(u.fileSize) as totalSize,
            AVG(u.dataQualityScore) as avgQuality
        FROM UploadedData u 
        WHERE u.tenantId = :tenantId
        """)
    Object getUploadStatistics(@Param("tenantId") Integer tenantId);

    // Recent Activity Queries
    @Query("SELECT u FROM UploadedData u WHERE u.tenantId = :tenantId ORDER BY u.timestamp DESC")
    Page<UploadedData> findRecentUploads(@Param("tenantId") Integer tenantId, Pageable pageable);
    
    @Query("SELECT u FROM UploadedData u WHERE u.processingCompletedAt >= :since AND u.tenantId = :tenantId ORDER BY u.processingCompletedAt DESC")
    List<UploadedData> findRecentlyProcessed(@Param("since") LocalDateTime since, 
                                           @Param("tenantId") Integer tenantId);

    // File Extension Analysis
    @Query(value = """
        SELECT 
            LOWER(SUBSTRING(u.file_name FROM '\\.([^.]*)$')) as extension,
            COUNT(*) as count,
            AVG(u.data_quality_score) as avg_quality
        FROM uploaded_data u 
        WHERE u.tenant_id = :tenantId 
        AND u.file_name ~ '\\.[^.]*$'
        GROUP BY LOWER(SUBSTRING(u.file_name FROM '\\.([^.]*)$'))
        ORDER BY count DESC
        """, nativeQuery = true)
    List<Object[]> getFileExtensionStatistics(@Param("tenantId") Integer tenantId);
}