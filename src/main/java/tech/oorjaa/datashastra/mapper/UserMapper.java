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
    @Mapping(target = "id", source = "id")
    @Mapping(target = "keycloakId", source = "keycloakId")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "mobileNumber", source = "mobileNumber")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "userStatus", source = "userStatus")
    @Mapping(target = "designation", source = "designation")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "pincode", source = "pincode")
    @Mapping(target = "tenantId", source = "tenant.id")
    @Mapping(target = "tenantName", source = "tenant.name")
    @Mapping(target = "firstName", expression = "java(splitFirstName(user.getFullName()))")
    @Mapping(target = "lastName", expression = "java(splitLastName(user.getFullName()))")
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
    @Mapping(target = "keycloakId", source = "keycloakId")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "mobileNumber", source = "mobileNumber")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "userStatus", source = "userStatus")
    @Mapping(target = "designation", source = "designation")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "pincode", source = "pincode")
    @Mapping(target = "tenantId", source = "tenantId")
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "clients", ignore = true)
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
    @Mapping(target = "keycloakId", source = "keycloakId")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "mobileNumber", source = "mobileNumber")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "userStatus", source = "userStatus")
    @Mapping(target = "designation", source = "designation")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "pincode", source = "pincode")
    @Mapping(target = "tenantId", source = "tenantId")
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "password", ignore = true) // Handle password separately in service
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);

    default String splitFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return null;
        }
        String[] parts = fullName.trim().split("\\s+", 2);
        return parts[0];
    }

    default String splitLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return null;
        }
        String[] parts = fullName.trim().split("\\s+", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    default String combineFullName(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return lastName != null ? lastName.trim() : "";
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return firstName.trim();
        }
        return firstName.trim() + " " + lastName.trim();
    }
}
