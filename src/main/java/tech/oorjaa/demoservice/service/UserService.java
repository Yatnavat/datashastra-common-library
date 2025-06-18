package tech.oorjaa.demoservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.oorjaa.demoservice.dto.UserDto;
import tech.oorjaa.demoservice.entity.Company;
import tech.oorjaa.demoservice.entity.User;
import tech.oorjaa.demoservice.exception.ResourceNotFoundException;
import tech.oorjaa.demoservice.mapper.UserMapper;
import tech.oorjaa.demoservice.repository.CompanyRepository;
import tech.oorjaa.demoservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Find a user by email
     *
     * @param email The email to search for
     * @return The user entity
     * @throws ResourceNotFoundException if the user is not found
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Check if a user exists with the given email
     *
     * @param email The email to check
     * @return true if a user exists with this email, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Get a user by ID
     *
     * @param id The user ID
     * @return The user DTO
     * @throws ResourceNotFoundException if the user is not found
     */
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    /**
     * Get users by company ID
     *
     * @param companyId The company ID
     * @return List of user DTOs belonging to the company
     */
    public List<UserDto> getUsersByCompany(Long companyId) {
        List<User> users = userRepository.findByCompanyId(companyId);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Create a new user
     *
     * @param userDto The user data
     * @return The created user DTO
     * @throws ResourceNotFoundException if the company is not found
     */
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + userDto.getEmail());
        }

        User user = userMapper.toEntity(userDto);

        // Encode password
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Set company
        Company company = companyRepository.findById(userDto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + userDto.getCompanyId()));
        user.setCompany(company);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    /**
     * Update an existing user
     *
     * @param id The user ID
     * @param userDto The updated user data
     * @return The updated user DTO
     * @throws ResourceNotFoundException if the user is not found
     */
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if email is changed and already exists
        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + userDto.getEmail());
        }

        userMapper.updateUserFromDto(userDto, user);

        // Handle password update if provided
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        // Update company if provided and different
        if (userDto.getCompanyId() != null && !user.getCompany().getId().equals(userDto.getCompanyId())) {
            Company company = companyRepository.findById(userDto.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + userDto.getCompanyId()));
            user.setCompany(company);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Delete a user by ID
     *
     * @param id The user ID
     * @throws ResourceNotFoundException if the user is not found
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Find a user by ID and company ID
     *
     * @param id The user ID
     * @param companyId The company ID
     * @return The user entity
     * @throws ResourceNotFoundException if the user is not found
     */
    public User findByIdAndCompanyId(Long id, Long companyId) {
        return userRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id + " and company id: " + companyId));
    }
}
