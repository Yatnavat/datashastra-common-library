package tech.oorjaa.demoservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.oorjaa.demoservice.dto.UserDto;
import tech.oorjaa.demoservice.entity.Company;
import tech.oorjaa.demoservice.entity.User;
import tech.oorjaa.demoservice.exception.ResourceNotFoundException;
import tech.oorjaa.demoservice.mapper.UserMapper;
import tech.oorjaa.demoservice.repository.CompanyRepository;
import tech.oorjaa.demoservice.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("Tech Corp");
        company.setLegalName("Tech Corporation Ltd");

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword");
        user.setCompany(company);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("plainPassword");
        userDto.setCompanyId(1L);
        userDto.setCompanyName("Tech Corp");
    }

    @Test
    @DisplayName("Should find user by email when user exists")
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        // When
        User result = userService.findByEmail("john.doe@example.com");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by email")
    void findByEmail_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.findByEmail("nonexistent@example.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with email: nonexistent@example.com");
    }

    @Test
    @DisplayName("Should return true when user exists by email")
    void existsByEmail_WhenUserExists_ShouldReturnTrue() {
        // Given
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        // When
        boolean result = userService.existsByEmail("john.doe@example.com");

        // Then
        assertThat(result).isTrue();
        verify(userRepository).existsByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Should return false when user does not exist by email")
    void existsByEmail_WhenUserNotExists_ShouldReturnFalse() {
        // Given
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        // When
        boolean result = userService.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isFalse();
        verify(userRepository).existsByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should get user DTO when user exists")
    void getUser_WhenUserExists_ShouldReturnUserDto() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // When
        UserDto result = userService.getUser(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(userRepository).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getting non-existent user")
    void getUser_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUser(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with id: 999");
    }

    @Test
    @DisplayName("Should return users by company when company has users")
    void getUsersByCompany_WhenCompanyHasUsers_ShouldReturnUserList() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setEmail("jane@example.com");
        user2.setCompany(company);

        List<User> users = Arrays.asList(user, user2);
        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setFirstName("Jane");

        when(userRepository.findByCompanyId(1L)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(userMapper.toDto(user2)).thenReturn(userDto2);

        // When
        List<UserDto> result = userService.getUsersByCompany(1L);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(1).getFirstName()).isEqualTo("Jane");
        verify(userRepository).findByCompanyId(1L);
    }

    @Test
    @DisplayName("Should create user successfully with valid data")
    void createUser_WithValidData_ShouldReturnCreatedUser() {
        // Given
        UserDto createRequest = new UserDto();
        createRequest.setFirstName("John");
        createRequest.setEmail("john.new@example.com");
        createRequest.setPassword("plainPassword");
        createRequest.setCompanyId(1L);

        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setEmail("john.new@example.com");

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setFirstName("John");
        savedUser.setEmail("john.new@example.com");
        savedUser.setCompany(company);

        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(2L);
        savedUserDto.setFirstName("John");
        savedUserDto.setEmail("john.new@example.com");

        when(userRepository.existsByEmail("john.new@example.com")).thenReturn(false);
        when(userMapper.toEntity(createRequest)).thenReturn(newUser);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);

        // When
        UserDto result = userService.createUser(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getEmail()).isEqualTo("john.new@example.com");
        verify(userRepository).existsByEmail("john.new@example.com");
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when creating user with existing email")
    void createUser_WithExistingEmail_ShouldThrowException() {
        // Given
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already in use: john.doe@example.com");
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when creating user with non-existent company")
    void createUser_WithNonExistentCompany_ShouldThrowException() {
        // Given
        UserDto createRequest = new UserDto();
        createRequest.setEmail("new@example.com");
        createRequest.setCompanyId(999L);

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userMapper.toEntity(createRequest)).thenReturn(new User());
        when(companyRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Company not found with id: 999");
    }

    @Test
    @DisplayName("Should update user successfully with valid data")
    void updateUser_WithValidData_ShouldReturnUpdatedUser() {
        // Given
        UserDto updateRequest = new UserDto();
        updateRequest.setFirstName("John Updated");
        updateRequest.setEmail("john.updated@example.com");
        updateRequest.setCompanyId(1L);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setFirstName("John Updated");
        updatedUser.setEmail("john.updated@example.com");
        updatedUser.setCompany(company);

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(1L);
        updatedUserDto.setFirstName("John Updated");
        updatedUserDto.setEmail("john.updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("john.updated@example.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(updatedUserDto);

        // When
        UserDto result = userService.updateUser(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John Updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
        verify(userMapper).updateUserFromDto(updateRequest, user);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should delete user successfully when user exists")
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with id: 999");
        
        verify(userRepository, never()).deleteById(999L);
    }

    @Test
    @DisplayName("Should find user by ID and company ID when user exists")
    void findByIdAndCompanyId_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByIdAndCompanyId(1L, 1L)).thenReturn(Optional.of(user));

        // When
        User result = userService.findByIdAndCompanyId(1L, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCompany().getId()).isEqualTo(1L);
        verify(userRepository).findByIdAndCompanyId(1L, 1L);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID and company ID")
    void findByIdAndCompanyId_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findByIdAndCompanyId(1L, 999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.findByIdAndCompanyId(1L, 999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with id: 1 and company id: 999");
    }
}