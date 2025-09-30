package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.ForecastDto;
import tech.oorjaa.datashastra.entity.Forecast;

import java.util.List;

/**
 * MapStruct mapper for converting between Forecast entity and ForecastDto.
 * Handles complex forecast data transformations and workflow state.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ForecastMapper {

    /**
     * Convert Forecast entity to ForecastDto
     * @param forecast the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "statusBadgeColor", expression = "java(getStatusBadgeColor(forecast.getStatus()))")
    @Mapping(target = "progressBarColor", expression = "java(getProgressBarColor(forecast.getCurrentStep()))")
    @Mapping(target = "displayVersion", expression = "java(forecast.getVersion())")
    ForecastDto toDto(Forecast forecast);

    /**
     * Convert ForecastDto to Forecast entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "trainingProgress", ignore = true)
    Forecast toEntity(ForecastDto dto);

    /**
     * Convert list of Forecast entities to list of DTOs
     * @param forecasts the entities to convert
     * @return the list of converted DTOs
     */
    List<ForecastDto> toDtoList(List<Forecast> forecasts);

    /**
     * Convert list of ForecastDto to list of entities
     * @param dtos the DTOs to convert
     * @return the list of converted entities
     */
    List<Forecast> toEntityList(List<ForecastDto> dtos);

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
    @Mapping(target = "trainingProgress", ignore = true)
    void updateEntityFromDto(ForecastDto dto, @MappingTarget Forecast entity);

    /**
     * Create a summary DTO with limited fields for list views
     * @param forecast the source entity
     * @return summary DTO
     */
    @Mapping(target = "forecastConfiguration", ignore = true)
    @Mapping(target = "selectedFeatures", ignore = true)
    @Mapping(target = "hyperparameters", ignore = true)
    @Mapping(target = "forecastResults", ignore = true)
    @Mapping(target = "modelExplanation", ignore = true)
    @Mapping(target = "featureImportance", ignore = true)
    @Mapping(target = "errorAnalysis", ignore = true)
    @Named("toSummaryDto")
    ForecastDto toSummaryDto(Forecast forecast);

    /**
     * Helper method to determine status badge color
     */
    default String getStatusBadgeColor(tech.oorjaa.datashastra.enums.ForecastStatus status) {
        if (status == null) return "gray";
        return switch (status) {
            case DRAFT -> "yellow";
            case IN_PROGRESS -> "blue";
            case COMPLETED -> "green";
            case FAILED -> "red";
            case CANCELLED -> "orange";
        };
    }

    /**
     * Helper method to determine progress bar color based on step
     */
    default String getProgressBarColor(Integer step) {
        if (step == null) return "gray";
        if (step <= 2) return "yellow";
        if (step <= 5) return "blue";
        if (step <= 7) return "indigo";
        return "green";
    }

}