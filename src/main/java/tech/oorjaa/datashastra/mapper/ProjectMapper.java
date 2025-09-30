package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.ProjectDto;
import tech.oorjaa.datashastra.entity.Project;
import tech.oorjaa.datashastra.enums.ProjectStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * MapStruct mapper for converting between Project entity and ProjectDto
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ProjectMapper {

    /**
     * Convert Project entity to ProjectDto
     */
    @Mapping(target = "isActive", expression = "java(project.isActive())")
    @Mapping(target = "statusBadgeColor", expression = "java(getStatusBadgeColor(project.getStatus()))")
    @Mapping(target = "formattedBudget", expression = "java(formatBudget(project.getBudget(), project.getCurrencyCode()))")
    ProjectDto toDto(Project project);

    /**
     * Convert ProjectDto to Project entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Project toEntity(ProjectDto dto);

    /**
     * Convert list of Project entities to DTOs
     */
    List<ProjectDto> toDtoList(List<Project> projects);

    /**
     * Update existing entity from DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    void updateEntityFromDto(ProjectDto dto, @MappingTarget Project entity);

    /**
     * Helper method to determine status badge color
     */
    default String getStatusBadgeColor(ProjectStatus status) {
        if (status == null) return "gray";
        return switch (status) {
            case ACTIVE -> "green";
            case PLANNING -> "blue";
            case ON_HOLD -> "yellow";
            case COMPLETED -> "indigo";
            case CANCELLED -> "red";
        };
    }

    /**
     * Helper method to format budget with currency
     */
    default String formatBudget(BigDecimal budget, String currencyCode) {
        if (budget == null) return null;
        String currency = currencyCode != null ? currencyCode : "USD";
        return String.format("%s %.2f", currency, budget);
    }
}