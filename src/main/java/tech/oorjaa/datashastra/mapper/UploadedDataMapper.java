package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.UploadedDataDto;
import tech.oorjaa.datashastra.entity.UploadedData;

import java.util.List;

/**
 * MapStruct mapper for converting between UploadedData entity and UploadedDataDto.
 * Handles file metadata and processing status transformations.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface UploadedDataMapper {

    /**
     * Convert UploadedData entity to UploadedDataDto
     * @param uploadedData the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "fileSizeDisplay", expression = "java(formatFileSize(uploadedData.getFileSize()))")
    @Mapping(target = "processingStatusBadge", expression = "java(getProcessingStatusBadge(uploadedData))")
    @Mapping(target = "qualityScoreBadge", expression = "java(getQualityScoreBadge(uploadedData.getDataQualityScore()))")
    @Mapping(target = "canProcess", expression = "java(!uploadedData.getIsProcessed() && uploadedData.getProcessingError() == null)")
    @Mapping(target = "canDelete", expression = "java(!uploadedData.getLegalHold())")
    @Mapping(target = "hasErrors", expression = "java(uploadedData.getProcessingError() != null)")
    @Mapping(target = "hasWarnings", expression = "java(false)")
    UploadedDataDto toDto(UploadedData uploadedData);

    /**
     * Convert UploadedDataDto to UploadedData entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    UploadedData toEntity(UploadedDataDto dto);

    /**
     * Convert list of UploadedData entities to list of DTOs
     * @param uploadedDataList the entities to convert
     * @return the list of converted DTOs
     */
    List<UploadedDataDto> toDtoList(List<UploadedData> uploadedDataList);

    /**
     * Convert list of UploadedDataDto to list of entities
     * @param dtos the DTOs to convert
     * @return the list of converted entities
     */
    List<UploadedData> toEntityList(List<UploadedDataDto> dtos);

    /**
     * Update existing entity from DTO
     * @param dto the source DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "checksum", ignore = true) // Don't allow checksum updates
    void updateEntityFromDto(UploadedDataDto dto, @MappingTarget UploadedData entity);

    /**
     * Create a minimal DTO for list views
     * @param uploadedData the source entity
     * @return minimal DTO
     */
    @Mapping(target = "dataSchema", ignore = true)
    @Mapping(target = "columnTypes", ignore = true)
    @Mapping(target = "validationMetrics", ignore = true)
    @Named("toListDto")
    UploadedDataDto toListDto(UploadedData uploadedData);

    /**
     * Helper method to format file size
     */
    default String formatFileSize(Long fileSize) {
        if (fileSize == null) return "0 B";
        
        double size = fileSize.doubleValue();
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    /**
     * Helper method to get processing status badge
     */
    default String getProcessingStatusBadge(UploadedData entity) {
        if (entity.getProcessingError() != null) return "red";
        if (entity.getIsProcessed()) return "green";
        if (entity.getProcessingStartedAt() != null) return "blue";
        return "gray";
    }

    /**
     * Helper method to get quality score badge color
     */
    default String getQualityScoreBadge(java.math.BigDecimal qualityScore) {
        if (qualityScore == null) return "gray";
        double score = qualityScore.doubleValue();
        if (score >= 80) return "green";
        if (score >= 60) return "yellow";
        if (score >= 40) return "orange";
        return "red";
    }

}