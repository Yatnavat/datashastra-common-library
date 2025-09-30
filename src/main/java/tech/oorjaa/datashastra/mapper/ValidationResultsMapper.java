package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.ValidationResultsDto;
import tech.oorjaa.datashastra.entity.ValidationResults;

import java.util.List;

/**
 * MapStruct mapper for converting between ValidationResults entity and ValidationResultsDto.
 * Handles validation results and quality metrics transformations.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ValidationResultsMapper {

    /**
     * Convert ValidationResults entity to ValidationResultsDto
     * @param validationResults the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "qualityScoreBadge", expression = "java(getQualityScoreBadge(validationResults.getQualityScore()))")
    @Mapping(target = "validationStatusBadge", expression = "java(validationResults.getIsValid() ? \"green\" : \"red\")")
    @Mapping(target = "passesQualityThreshold", expression = "java(validationResults.passesQualityThreshold())")
    @Mapping(target = "hasAcceptableCompleteness", expression = "java(validationResults.hasAcceptableCompleteness())")
    @Mapping(target = "hasMinimumDataForForecasting", expression = "java(validationResults.hasMinimumDataForForecasting())")
    @Mapping(target = "exceedsBusinessLimits", expression = "java(validationResults.exceedsBusinessLimits())")
    @Mapping(target = "hasSchemaViolations", expression = "java(validationResults.hasSchemaViolations())")
    @Mapping(target = "hasBusinessRuleIssues", expression = "java(validationResults.hasBusinessRuleIssues())")
    @Mapping(target = "hasDataQualityIssues", expression = "java(validationResults.hasDataQualityIssues())")
    @Mapping(target = "isReadyForProcessing", expression = "java(validationResults.isReadyForProcessing())")
    @Mapping(target = "requiresDataCleaning", expression = "java(validationResults.requiresDataCleaning())")
    @Mapping(target = "hasTimeSeriesData", expression = "java(validationResults.hasTimeSeriesData())")
    @Mapping(target = "hasSufficientTimeRange", expression = "java(validationResults.hasSufficientTimeRange())")
    @Mapping(target = "timeRangeDays", expression = "java(validationResults.getTimeRangeDays())")
    ValidationResultsDto toDto(ValidationResults validationResults);

    /**
     * Convert ValidationResultsDto to ValidationResults entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    ValidationResults toEntity(ValidationResultsDto dto);

    /**
     * Convert list of ValidationResults entities to list of DTOs
     * @param validationResults the entities to convert
     * @return the list of converted DTOs
     */
    List<ValidationResultsDto> toDtoList(List<ValidationResults> validationResults);

    /**
     * Convert list of ValidationResultsDto to list of entities
     * @param dtos the DTOs to convert
     * @return the list of converted entities
     */
    List<ValidationResults> toEntityList(List<ValidationResultsDto> dtos);

    /**
     * Update existing entity from DTO
     * @param dto the source DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "validatedAt", ignore = true)
    void updateEntityFromDto(ValidationResultsDto dto, @MappingTarget ValidationResults entity);

    /**
     * Create a summary DTO with limited fields for list views
     * @param validationResults the source entity
     * @return summary DTO
     */
    @Mapping(target = "validationErrors", ignore = true)
    @Mapping(target = "validationWarnings", ignore = true)
    @Mapping(target = "validationConfig", ignore = true)
    @Named("toSummaryDto")
    ValidationResultsDto toSummaryDto(ValidationResults validationResults);

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

    /**
     * After mapping callback for entity to DTO
     */
    @AfterMapping
    default void enrichDto(@MappingTarget ValidationResultsDto dto, ValidationResults entity) {
        // Set UI display fields
        if (entity.getTotalRows() != null) {
            dto.setValidRowsPercentageText(String.format("%.1f%%", dto.getValidRowsPercentage()));
            dto.setMissingValuesPercentageText(String.format("%.1f%%", dto.getMissingValuesPercentage()));
            dto.setOutlierPercentageText(String.format("%.1f%%", dto.getOutlierPercentage()));
        }

        // Set warning and error flags
        dto.setShowWarnings(entity.getValidationWarnings() != null && !entity.getValidationWarnings().isEmpty());
        dto.setShowErrors(entity.getValidationErrors() != null && !entity.getValidationErrors().isEmpty() || 
                         Boolean.FALSE.equals(entity.getIsValid()));
    }
}