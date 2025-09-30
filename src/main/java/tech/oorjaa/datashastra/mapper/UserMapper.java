package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.UserDto;
import tech.oorjaa.datashastra.entity.User;
import tech.oorjaa.datashastra.enums.UserStatus;

import java.util.List;

/**
 * MapStruct mapper for converting between User entity and UserDto
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface UserMapper {

    /**
     * Convert User entity to UserDto
     */
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    @Mapping(target = "isActive", expression = "java(user.isActive())")
    @Mapping(target = "statusBadgeColor", expression = "java(getStatusBadgeColor(user.getStatus()))")
    UserDto toDto(User user);

    /**
     * Convert UserDto to User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toEntity(UserDto dto);

    /**
     * Convert list of User entities to DTOs
     */
    List<UserDto> toDtoList(List<User> users);

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
    @Mapping(target = "keycloakId", ignore = true) // Don't allow keycloak ID updates
    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);

    /**
     * Helper method to determine status badge color
     */
    default String getStatusBadgeColor(UserStatus status) {
        if (status == null) return "gray";
        return switch (status) {
            case ACTIVE -> "green";
            case PENDING -> "yellow";
            case SUSPENDED -> "orange";
            case INACTIVE -> "gray";
            case LOCKED -> "red";
        };
    }
}