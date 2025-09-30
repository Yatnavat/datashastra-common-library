package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.WorkflowStepDto;
import tech.oorjaa.datashastra.entity.WorkflowStep;

import java.util.List;

/**
 * MapStruct mapper for converting between WorkflowStep entity and WorkflowStepDto.
 * Handles 8-step workflow progress tracking and step management.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface WorkflowStepMapper {

    /**
     * Convert WorkflowStep entity to WorkflowStepDto
     * @param workflowStep the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "statusBadgeColor", expression = "java(getStatusBadgeColor(workflowStep.getStatus()))")
    @Mapping(target = "progressBarColor", expression = "java(getProgressBarColor(workflowStep.getProgressPercentage()))")
    @Mapping(target = "stepIcon", expression = "java(getStepIcon(workflowStep.getStepNumber()))")
    @Mapping(target = "isPending", expression = "java(workflowStep.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.PENDING)")
    @Mapping(target = "isInProgress", expression = "java(workflowStep.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.IN_PROGRESS)")
    @Mapping(target = "isFailed", expression = "java(workflowStep.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.ERROR)")
    @Mapping(target = "hasErrors", expression = "java(workflowStep.getErrorCount() != null && workflowStep.getErrorCount() > 0)")
    @Mapping(target = "hasWarnings", ignore = true)
    WorkflowStepDto toDto(WorkflowStep workflowStep);

    /**
     * Convert WorkflowStepDto to WorkflowStep entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    WorkflowStep toEntity(WorkflowStepDto dto);

    /**
     * Convert list of WorkflowStep entities to list of DTOs
     * @param workflowSteps the entities to convert
     * @return the list of converted DTOs
     */
    List<WorkflowStepDto> toDtoList(List<WorkflowStep> workflowSteps);

    /**
     * Convert list of WorkflowStepDto to list of entities
     * @param dtos the DTOs to convert
     * @return the list of converted entities
     */
    List<WorkflowStep> toEntityList(List<WorkflowStepDto> dtos);

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
    @Mapping(target = "forecastId", ignore = true)
    @Mapping(target = "stepNumber", ignore = true)
    void updateEntityFromDto(WorkflowStepDto dto, @MappingTarget WorkflowStep entity);

    /**
     * Create a minimal DTO for workflow overview
     * @param workflowStep the source entity
     * @return minimal DTO
     */
    @Mapping(target = "stepData", ignore = true)
    @Mapping(target = "validationResults", ignore = true)
    @Mapping(target = "errorMessages", ignore = true)
    @Named("toOverviewDto")
    WorkflowStepDto toOverviewDto(WorkflowStep workflowStep);

    /**
     * Helper method to determine status badge color
     */
    default String getStatusBadgeColor(tech.oorjaa.datashastra.enums.StepStatus status) {
        if (status == null) return "gray";
        return switch (status) {
            case PENDING -> "gray";
            case IN_PROGRESS -> "blue";
            case COMPLETED -> "green";
            case ERROR -> "red";
            case SKIPPED -> "yellow";
        };
    }

    /**
     * Helper method to determine progress bar color
     */
    default String getProgressBarColor(Integer progressPercentage) {
        if (progressPercentage == null || progressPercentage == 0) return "gray";
        if (progressPercentage < 25) return "red";
        if (progressPercentage < 50) return "yellow";
        if (progressPercentage < 75) return "blue";
        if (progressPercentage < 100) return "indigo";
        return "green";
    }

    /**
     * Helper method to get step icon based on step number
     */
    default String getStepIcon(Integer stepNumber) {
        if (stepNumber == null) return "question-circle";
        return switch (stepNumber) {
            case 1 -> "upload";
            case 2 -> "chart-line";
            case 3 -> "cog";
            case 4 -> "list";
            case 5 -> "layer-group";
            case 6 -> "calendar";
            case 7 -> "play";
            case 8 -> "chart-bar";
            default -> "question-circle";
        };
    }

    /**
     * After mapping callback for entity to DTO
     */
    @AfterMapping
    default void enrichDto(@MappingTarget WorkflowStepDto dto, WorkflowStep entity) {
        // Set display text fields
        dto.setDurationText(dto.getDurationDisplay());
        dto.setCompletionTimeText(dto.getCompletionTimeDisplay());

        // Set UI flags based on status and conditions
        dto.setShowErrors(Boolean.TRUE.equals(dto.getHasErrors()));
        dto.setShowWarnings(Boolean.TRUE.equals(dto.getHasWarnings()));
        
        // Set action permissions
        dto.setCanEdit(entity.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.PENDING || 
                      entity.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.ERROR);
        dto.setCanSkip(entity.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.PENDING);
        dto.setCanReset(entity.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.COMPLETED ||
                       entity.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.ERROR);

        // Set workflow progression flags
        dto.setCanProceedToNext(entity.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.COMPLETED ||
                               entity.getStatus() == tech.oorjaa.datashastra.enums.StepStatus.SKIPPED);
    }

    /**
     * Before mapping callback for DTO to entity
     */
    @BeforeMapping
    default void prepareEntity(WorkflowStepDto dto, @MappingTarget WorkflowStep entity) {
        // Set default values
        if (dto.getStatus() == null) {
            entity.setStatus(tech.oorjaa.datashastra.enums.StepStatus.PENDING);
        }
        if (dto.getProgressPercentage() == null) {
            entity.setProgressPercentage(0);
        }
        if (dto.getErrorCount() == null) {
            entity.setErrorCount(0);
        }
    }
}