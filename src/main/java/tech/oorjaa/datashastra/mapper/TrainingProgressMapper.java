package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.TrainingProgressDto;
import tech.oorjaa.datashastra.entity.TrainingProgress;

import java.util.List;

/**
 * MapStruct mapper for converting between TrainingProgress entity and TrainingProgressDto.
 * Handles real-time training progress tracking and monitoring data.
 * 
 * @author DataShastra Team
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface TrainingProgressMapper {

    /**
     * Convert TrainingProgress entity to TrainingProgressDto
     * @param trainingProgress the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "statusBadgeColor", expression = "java(getStatusBadgeColor(trainingProgress.getStatus()))")
    @Mapping(target = "progressBarColor", expression = "java(getProgressBarColor(trainingProgress.getProgressPercentage()))")
    @Mapping(target = "isRunning", expression = "java(trainingProgress.isRunning())")
    @Mapping(target = "isCompleted", expression = "java(trainingProgress.isCompleted())")
    @Mapping(target = "isFailed", expression = "java(trainingProgress.isFailed())")
    @Mapping(target = "isTerminal", expression = "java(trainingProgress.isTerminal())")
    @Mapping(target = "shouldEarlyStopping", expression = "java(trainingProgress.shouldEarlyStopping())")
    @Mapping(target = "hasResourceConstraints", expression = "java(trainingProgress.hasResourceConstraints())")
    @Mapping(target = "completionPercentage", expression = "java(trainingProgress.getCompletionPercentage())")
    TrainingProgressDto toDto(TrainingProgress trainingProgress);

    /**
     * Convert TrainingProgressDto to TrainingProgress entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    TrainingProgress toEntity(TrainingProgressDto dto);

    /**
     * Convert list of TrainingProgress entities to list of DTOs
     * @param trainingProgressList the entities to convert
     * @return the list of converted DTOs
     */
    List<TrainingProgressDto> toDtoList(List<TrainingProgress> trainingProgressList);

    /**
     * Convert list of TrainingProgressDto to list of entities
     * @param dtos the DTOs to convert
     * @return the list of converted entities
     */
    List<TrainingProgress> toEntityList(List<TrainingProgressDto> dtos);

    /**
     * Update existing entity from DTO
     * @param dto the source DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "forecastId", ignore = true)
    void updateEntityFromDto(TrainingProgressDto dto, @MappingTarget TrainingProgress entity);

    /**
     * Create a minimal DTO for monitoring views
     * @param trainingProgress the source entity
     * @return minimal DTO
     */
    @Mapping(target = "currentMetrics", ignore = true)
    @Mapping(target = "resourceUtilization", ignore = true)
    @Mapping(target = "trainingLogs", ignore = true)
    @Named("toMonitoringDto")
    TrainingProgressDto toMonitoringDto(TrainingProgress trainingProgress);

    /**
     * Helper method to determine status badge color
     */
    default String getStatusBadgeColor(tech.oorjaa.datashastra.enums.TrainingStatus status) {
        if (status == null) return "gray";
        return switch (status) {
            case INITIALIZING -> "blue";
            case TRAINING -> "blue";
            case VALIDATING -> "indigo";
            case COMPLETED -> "green";
            case FAILED -> "red";
        };
    }

    /**
     * Helper method to determine progress bar color based on percentage
     */
    default String getProgressBarColor(Integer progressPercentage) {
        if (progressPercentage == null) return "gray";
        if (progressPercentage < 25) return "red";
        if (progressPercentage < 50) return "yellow";
        if (progressPercentage < 75) return "blue";
        if (progressPercentage < 100) return "indigo";
        return "green";
    }

    /**
     * After mapping callback for entity to DTO
     */
    @AfterMapping
    default void enrichDto(@MappingTarget TrainingProgressDto dto, TrainingProgress entity) {
        // Set display text fields
        dto.setCompletionPercentageText(dto.getProgressDisplay());
        dto.setElapsedTimeText(dto.getElapsedTimeDisplay());
        dto.setEstimatedRemainingText(dto.getEstimatedRemainingDisplay());
        dto.setCurrentStageText(entity.getCurrentStage() != null ? entity.getCurrentStage() : "Unknown");

        // Set warning flags
        dto.setShowResourceWarning(Boolean.TRUE.equals(dto.getHasResourceConstraints()));
        dto.setShowConvergenceInfo(Boolean.TRUE.equals(entity.getConvergenceAchieved()));
    }

    /**
     * Before mapping callback for DTO to entity
     */
    @BeforeMapping
    default void prepareEntity(TrainingProgressDto dto, @MappingTarget TrainingProgress entity) {
        // Set default values
        if (dto.getStatus() == null) {
            entity.setStatus(tech.oorjaa.datashastra.enums.TrainingStatus.INITIALIZING);
        }
        if (dto.getProgressPercentage() == null) {
            entity.setProgressPercentage(0);
        }
        if (dto.getCurrentIteration() == null) {
            entity.setCurrentIteration(0);
        }
        if (dto.getElapsedTime() == null) {
            entity.setElapsedTime(0L);
        }
        if (dto.getStageProgress() == null) {
            entity.setStageProgress(0);
        }
        if (dto.getErrorCount() == null) {
            entity.setErrorCount(0);
        }
        if (dto.getWarningCount() == null) {
            entity.setWarningCount(0);
        }
        if (dto.getConvergenceAchieved() == null) {
            entity.setConvergenceAchieved(false);
        }
        if (dto.getEpochsWithoutImprovement() == null) {
            entity.setEpochsWithoutImprovement(0);
        }
    }
}