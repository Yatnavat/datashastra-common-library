package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.TransformedDataDto;
import tech.oorjaa.datashastra.entity.TransformedData;

import java.util.List;

/**
 * MapStruct mapper for converting between TransformedData entity and TransformedDataDto.
 * Handles transformation results and processed data metrics.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface TransformedDataMapper {

    /**
     * Convert TransformedData entity to TransformedDataDto
     * @param transformedData the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "qualityScoreBadge", expression = "java(getQualityScoreBadge(transformedData.getDataQualityScore()))")
    @Mapping(target = "isReadyForModeling", expression = "java(transformedData.hasGoodDataQuality())")
    @Mapping(target = "originalDataRows", source = "recordsProcessed")
    @Mapping(target = "transformedDataRows", source = "rowCount")
    @Mapping(target = "originalColumns", ignore = true)
    @Mapping(target = "transformedColumns", source = "columnCount")
    @Mapping(target = "missingValues", source = "missingValuesCount")
    @Mapping(target = "outliersRemoved", source = "outliersCount")
    @Mapping(target = "duplicatesRemoved", source = "duplicatesRemoved")
    @Mapping(target = "dataStartDate", source = "dateRangeStart")
    @Mapping(target = "dataEndDate", source = "dateRangeEnd")
    @Mapping(target = "processingDurationMs", expression = "java(transformedData.getProcessingTimeSeconds() != null ? transformedData.getProcessingTimeSeconds() * 1000L : null)")
    @Mapping(target = "dataQualityScore", source = "dataQualityScore")
    @Mapping(target = "completenessPercentage", source = "dataCompleteness")
    TransformedDataDto toDto(TransformedData transformedData);

    /**
     * Convert TransformedDataDto to TransformedData entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    TransformedData toEntity(TransformedDataDto dto);

    /**
     * Convert list of TransformedData entities to list of DTOs
     * @param transformedDataList the entities to convert
     * @return the list of converted DTOs
     */
    List<TransformedDataDto> toDtoList(List<TransformedData> transformedDataList);

    /**
     * Convert list of TransformedDataDto to list of entities
     * @param dtos the DTOs to convert
     * @return the list of converted entities
     */
    List<TransformedData> toEntityList(List<TransformedDataDto> dtos);

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
    void updateEntityFromDto(TransformedDataDto dto, @MappingTarget TransformedData entity);

    /**
     * Create a summary DTO for list views
     * @param transformedData the source entity
     * @return summary DTO
     */
    @Mapping(target = "transformationSteps", ignore = true)
    @Mapping(target = "appliedFilters", ignore = true)
    @Mapping(target = "featureEngineering", ignore = true)
    @Mapping(target = "aggregationRules", ignore = true)
    @Mapping(target = "cleaningOperations", ignore = true)
    @Mapping(target = "outputSchema", ignore = true)
    @Mapping(target = "columnMappings", ignore = true)
    @Mapping(target = "dataTypes", ignore = true)
    @Mapping(target = "validationRules", ignore = true)
    @Mapping(target = "statisticalSummary", ignore = true)
    @Named("toSummaryDto")
    TransformedDataDto toSummaryDto(TransformedData transformedData);


    /**
     * Helper method to determine quality score badge color
     */
    default String getQualityScoreBadge(java.math.BigDecimal qualityScore) {
        if (qualityScore == null) return "gray";
        double score = qualityScore.doubleValue();
        if (score >= 90) return "green";
        if (score >= 80) return "blue";
        if (score >= 70) return "yellow";
        if (score >= 60) return "orange";
        return "red";
    }



}