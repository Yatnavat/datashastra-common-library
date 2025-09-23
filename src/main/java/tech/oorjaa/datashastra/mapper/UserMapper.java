package tech.oorjaa.datashastra.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tech.oorjaa.datashastra.dto.UserDto;
import tech.oorjaa.datashastra.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Converts a User entity to a UserDto
     *
     * @param user the source entity
     * @return the mapped DTO
     */
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "password", ignore = true) // Don't include password in DTO for security
    UserDto toDto(User user);

    /**
     * Converts a UserDto to a new User entity
     * Note: This does not handle password encoding, which should be done in the service
     *
     * @param userDto the source DTO
     * @return the mapped entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    User toEntity(UserDto userDto);

    /**
     * Updates an existing User entity from a UserDto
     * Note: This does not handle password encoding, which should be done in the service
     *
     * @param userDto the source DTO with updated values
     * @param user the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "password", ignore = true) // Handle password separately in service
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
