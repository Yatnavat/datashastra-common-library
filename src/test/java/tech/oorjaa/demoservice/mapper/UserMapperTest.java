package tech.oorjaa.demoservice.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import tech.oorjaa.demoservice.dto.UserDto;
import tech.oorjaa.demoservice.entity.Company;
import tech.oorjaa.demoservice.entity.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Mapper Tests")
class UserMapperTest {

    private UserMapper userMapper;
    private User user;
    private UserDto userDto;
    private Company company;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);

        // Set up company
        company = new Company();
        company.setId(1L);
        company.setName("Tech Corp");
        company.setLegalName("Tech Corporation Ltd");

        // Set up user entity
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setMobileNumber("1234567890");
        user.setUsername("johndoe");
        user.setPassword("encodedPassword123");
        user.setKeycloakId("keycloak-123");
        user.setCompany(company);
        user.setCreatedDate(LocalDateTime.now().minusDays(1));
        user.setLastModifiedDate(LocalDateTime.now());
        user.setCreatedBy("system");
        user.setLastModifiedBy("admin");

        // Set up user DTO
        userDto = new UserDto();
        userDto.setId(2L);
        userDto.setFirstName("Jane");
        userDto.setLastName("Smith");
        userDto.setEmail("jane.smith@example.com");
        userDto.setMobileNumber("9876543210");
        userDto.setPassword("plainPassword456");
        userDto.setCompanyId(2L);
        userDto.setCompanyName("Another Corp");
        userDto.setRole("ADMIN");
    }

    @Test
    @DisplayName("Should map User entity to UserDto correctly")
    void toDto_ShouldMapUserEntityToUserDto() {
        // When
        UserDto result = userMapper.toDto(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getMobileNumber()).isEqualTo(user.getMobileNumber());
        assertThat(result.getCompanyId()).isEqualTo(company.getId());
        assertThat(result.getCompanyName()).isEqualTo(company.getName());
        
        // Password should be ignored (null)
        assertThat(result.getPassword()).isNull();
    }

    @Test
    @DisplayName("Should handle null User entity gracefully")
    void toDto_WithNullUser_ShouldReturnNull() {
        // When
        UserDto result = userMapper.toDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle User with null company")
    void toDto_WithNullCompany_ShouldMapWithNullCompanyFields() {
        // Given
        user.setCompany(null);

        // When
        UserDto result = userMapper.toDto(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getCompanyId()).isNull();
        assertThat(result.getCompanyName()).isNull();
    }

    @Test
    @DisplayName("Should map UserDto to User entity correctly")
    void toEntity_ShouldMapUserDtoToUserEntity() {
        // When
        User result = userMapper.toEntity(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(result.getMobileNumber()).isEqualTo(userDto.getMobileNumber());
        assertThat(result.getPassword()).isEqualTo(userDto.getPassword());

        // Ignored fields should be null
        assertThat(result.getId()).isNull();
        assertThat(result.getCompany()).isNull();
        assertThat(result.getCreatedDate()).isNull();
        assertThat(result.getLastModifiedDate()).isNull();
        assertThat(result.getCreatedBy()).isNull();
        assertThat(result.getLastModifiedBy()).isNull();
    }

    @Test
    @DisplayName("Should handle null UserDto gracefully")
    void toEntity_WithNullUserDto_ShouldReturnNull() {
        // When
        User result = userMapper.toEntity(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle UserDto with null values")
    void toEntity_WithNullValues_ShouldMapCorrectly() {
        // Given
        UserDto dtoWithNulls = new UserDto();
        dtoWithNulls.setFirstName("John");
        dtoWithNulls.setEmail("john@example.com");
        // Other fields are null

        // When
        User result = userMapper.toEntity(dtoWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getLastName()).isNull();
        assertThat(result.getMobileNumber()).isNull();
        assertThat(result.getPassword()).isNull();
    }

    @Test
    @DisplayName("Should update User entity from UserDto correctly")
    void updateUserFromDto_ShouldUpdateExistingUser() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Original");
        existingUser.setLastName("User");
        existingUser.setEmail("original@example.com");
        existingUser.setPassword("originalPassword");
        existingUser.setCompany(company);
        existingUser.setCreatedDate(LocalDateTime.now().minusDays(5));
        existingUser.setCreatedBy("originalCreator");

        // When
        userMapper.updateUserFromDto(userDto, existingUser);

        // Then
        assertThat(existingUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(existingUser.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(existingUser.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(existingUser.getMobileNumber()).isEqualTo(userDto.getMobileNumber());

        // Ignored fields should remain unchanged
        assertThat(existingUser.getId()).isEqualTo(1L); // Original ID preserved
        assertThat(existingUser.getCompany()).isEqualTo(company); // Original company preserved
        assertThat(existingUser.getPassword()).isEqualTo("originalPassword"); // Password ignored
        assertThat(existingUser.getCreatedDate()).isNotNull(); // Audit fields preserved
        assertThat(existingUser.getCreatedBy()).isEqualTo("originalCreator");
    }

    @Test
    @DisplayName("Should handle update with null DTO values")
    void updateUserFromDto_WithNullDtoValues_ShouldUpdateOnlyNonNullFields() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Original");
        existingUser.setLastName("User");
        existingUser.setEmail("original@example.com");
        existingUser.setMobileNumber("123456789");

        UserDto partialDto = new UserDto();
        partialDto.setFirstName("Updated");
        partialDto.setEmail("updated@example.com");
        // lastName and mobileNumber are null

        // When
        userMapper.updateUserFromDto(partialDto, existingUser);

        // Then
        assertThat(existingUser.getFirstName()).isEqualTo("Updated");
        assertThat(existingUser.getEmail()).isEqualTo("updated@example.com");
        // Null values in DTO should set corresponding fields to null
        assertThat(existingUser.getLastName()).isNull();
        assertThat(existingUser.getMobileNumber()).isNull();
    }

    @Test
    @DisplayName("Should handle update with null UserDto")
    void updateUserFromDto_WithNullDto_ShouldNotThrowException() {
        // Given
        User existingUser = new User();
        existingUser.setFirstName("Original");

        // When & Then
        // Should not throw exception
        userMapper.updateUserFromDto(null, existingUser);
        
        // Original values should remain unchanged
        assertThat(existingUser.getFirstName()).isEqualTo("Original");
    }

    @Test
    @DisplayName("Should handle mapping with empty strings")
    void mapping_WithEmptyStrings_ShouldPreserveEmptyStrings() {
        // Given
        UserDto dtoWithEmptyStrings = new UserDto();
        dtoWithEmptyStrings.setFirstName("");
        dtoWithEmptyStrings.setLastName("");
        dtoWithEmptyStrings.setEmail("test@example.com");
        dtoWithEmptyStrings.setMobileNumber("");

        // When
        User result = userMapper.toEntity(dtoWithEmptyStrings);

        // Then
        assertThat(result.getFirstName()).isEqualTo("");
        assertThat(result.getLastName()).isEqualTo("");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getMobileNumber()).isEqualTo("");
    }

    @Test
    @DisplayName("Should maintain data integrity in bidirectional mapping")
    void bidirectionalMapping_ShouldMaintainDataIntegrity() {
        // Given - Start with a UserDto
        UserDto originalDto = new UserDto();
        originalDto.setFirstName("John");
        originalDto.setLastName("Doe");
        originalDto.setEmail("john@example.com");
        originalDto.setMobileNumber("123456789");

        // When - Convert to entity and back to DTO
        User entity = userMapper.toEntity(originalDto);
        entity.setId(1L); // Simulate persisted entity
        entity.setCompany(company); // Set company for mapping back
        UserDto resultDto = userMapper.toDto(entity);

        // Then - Key data should be preserved
        assertThat(resultDto.getFirstName()).isEqualTo(originalDto.getFirstName());
        assertThat(resultDto.getLastName()).isEqualTo(originalDto.getLastName());
        assertThat(resultDto.getEmail()).isEqualTo(originalDto.getEmail());
        assertThat(resultDto.getMobileNumber()).isEqualTo(originalDto.getMobileNumber());
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(resultDto.getCompanyId()).isEqualTo(company.getId());
        assertThat(resultDto.getCompanyName()).isEqualTo(company.getName());
    }
}