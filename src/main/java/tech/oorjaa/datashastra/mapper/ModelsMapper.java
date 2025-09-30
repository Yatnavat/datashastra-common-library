package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.ModelsDto;
import tech.oorjaa.datashastra.entity.Models;

import java.util.List;

/**
 * MapStruct mapper for converting between Models entity and ModelsDto.
 * Handles model configuration and capability mapping.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModelsMapper {

    /**
     * Convert Models entity to ModelsDto
     * @param models the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "complexityBadgeColor", expression = "java(getComplexityBadgeColor(models.getComplexity()))")
    @Mapping(target = "statusBadgeColor", expression = "java(models.getIsActive() ? \"green\" : \"gray\")")
    @Mapping(target = "showRecommendedBadge", expression = "java(models.getIsRecommended())")
    @Mapping(target = "showBetaBadge", ignore = true)
    ModelsDto toDto(Models models);

    /**
     * Convert ModelsDto to Models entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Models toEntity(ModelsDto dto);

    /**
     * Convert list of Models entities to list of DTOs
     * @param models the entities to convert
     * @return the list of converted DTOs
     */
    List<ModelsDto> toDtoList(List<Models> models);

    /**
     * Convert list of ModelsDto to list of entities
     * @param dtos the DTOs to convert
     * @return the list of converted entities
     */
    List<Models> toEntityList(List<ModelsDto> dtos);

    /**
     * Update existing entity from DTO
     * @param dto the source DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntityFromDto(ModelsDto dto, @MappingTarget Models entity);

    /**
     * Create a minimal DTO for selection lists
     * @param models the source entity
     * @return minimal DTO
     */
    @Mapping(target = "defaultHyperparameters", ignore = true)
    @Mapping(target = "hyperparameterRanges", ignore = true)
    @Mapping(target = "prerequisites", ignore = true)
    @Mapping(target = "limitations", ignore = true)
    @Mapping(target = "documentationUrl", ignore = true)
    @Named("toSelectionDto")
    ModelsDto toSelectionDto(Models models);

    /**
     * Helper method to determine complexity badge color
     */
    default String getComplexityBadgeColor(tech.oorjaa.datashastra.enums.ModelComplexity complexity) {
        if (complexity == null) return "gray";
        return switch (complexity) {
            case LOW -> "green";
            case MEDIUM -> "yellow";
            case HIGH -> "orange";
        };
    }

}