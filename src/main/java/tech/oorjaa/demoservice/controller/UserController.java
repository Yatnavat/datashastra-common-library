package tech.oorjaa.demoservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.oorjaa.demoservice.dto.StandardResponse;
import tech.oorjaa.demoservice.dto.UserDto;
import tech.oorjaa.demoservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for user operations")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<UserDto>> getUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        UserDto user = userService.getUser(id);
        StandardResponse<UserDto> response = StandardResponse.success(user, "User retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get users by company", description = "Retrieves all users belonging to a specific company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<List<UserDto>>> getUsersByCompany(
            @Parameter(description = "Company ID", required = true)
            @PathVariable Long companyId) {
        List<UserDto> users = userService.getUsersByCompany(companyId);
        StandardResponse<List<UserDto>> response = StandardResponse.success(users, "Users retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<UserDto>> createUser(
            @Parameter(description = "User data", required = true)
            @Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        StandardResponse<UserDto> response = StandardResponse.success(createdUser, "User created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<UserDto>> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user data", required = true)
            @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        StandardResponse<UserDto> response = StandardResponse.success(updatedUser, "User updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        StandardResponse<Void> response = StandardResponse.success("User deleted successfully");
        return ResponseEntity.ok(response);
    }
}