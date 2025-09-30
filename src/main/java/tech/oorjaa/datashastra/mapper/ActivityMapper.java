package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.ActivityDto;
import tech.oorjaa.datashastra.entity.Activity;

import java.util.List;

/**
 * MapStruct mapper for converting between Activity entity and ActivityDto.
 * Provides type-safe bi-directional mapping with custom logic.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ActivityMapper {

    /**
     * Convert Activity entity to ActivityDto
     * @param activity the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "canCreateForecasts", expression = "java(activity.canCreateForecasts())")
    @Mapping(target = "isActive", expression = "java(activity.isActive())")
    @Mapping(target = "statusBadgeColor", expression = "java(getStatusBadgeColor(activity.getStatus()))")
    @Mapping(target = "priorityBadgeColor", expression = "java(getPriorityBadgeColor(activity.getPriority()))")
    @Mapping(target = "displayName", expression = "java(activity.getName())")
    ActivityDto toDto(Activity activity);

    /**
     * Convert ActivityDto to Activity entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "forecasts", ignore = true)
    Activity toEntity(ActivityDto dto);

    /**
     * Convert list of Activity entities to list of DTOs
     * @param activities the entities to convert
     * @return the list of converted DTOs
     */
    List<ActivityDto> toDtoList(List<Activity> activities);

    /**
     * Convert list of ActivityDto to list of entities
     * @param dtos the DTOs to convert
     * @return the list of converted entities
     */
    List<Activity> toEntityList(List<ActivityDto> dtos);

    /**
     * Update existing entity from DTO
     * @param dto the source DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "forecasts", ignore = true)
    void updateEntityFromDto(ActivityDto dto, @MappingTarget Activity entity);

    /**
     * Create a summary DTO with limited fields
     * @param activity the source entity
     * @return summary DTO
     */
    @Mapping(target = "configurations", ignore = true)
    @Mapping(target = "businessContext", ignore = true)
    @Mapping(target = "forecastingRequirements", ignore = true)
    @Named("toSummaryDto")
    ActivityDto toSummaryDto(Activity activity);

    /**
     * Helper method to determine status badge color
     */
    default String getStatusBadgeColor(tech.oorjaa.datashastra.enums.ActivityStatus status) {
        if (status == null) return "gray";
        return switch (status) {
            case ACTIVE -> "green";
            case DRAFT -> "yellow";
            case COMPLETED -> "blue";
            case ON_HOLD -> "orange";
            case ARCHIVED -> "gray";
        };
    }

    /**
     * Helper method to determine priority badge color
     */
    default String getPriorityBadgeColor(tech.oorjaa.datashastra.enums.ActivityPriority priority) {
        if (priority == null) return "gray";
        return switch (priority) {
            case HIGH -> "red";
            case MEDIUM -> "yellow";
            case LOW -> "green";
        };
    }


}