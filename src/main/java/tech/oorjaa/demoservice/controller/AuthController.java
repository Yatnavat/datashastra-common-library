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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.oorjaa.demoservice.dto.LoginRequest;
import tech.oorjaa.demoservice.dto.LoginResponse;
import tech.oorjaa.demoservice.dto.RefreshTokenRequest;
import tech.oorjaa.demoservice.dto.StandardResponse;
import tech.oorjaa.demoservice.exception.AuthenticationException;
import tech.oorjaa.demoservice.service.AuthService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API endpoints for user authentication")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user with email and password and returns access and refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public ResponseEntity<StandardResponse<LoginResponse>> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = authService.authenticateUser(loginRequest);
            StandardResponse<LoginResponse> response = StandardResponse.success(loginResponse, "Login successful");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // Log the exception for debugging
            log.error("Authentication failed: {}", e.getMessage(), e);
            StandardResponse<LoginResponse> errorResponse = StandardResponse.error("Authentication failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Refreshes an expired access token using a valid refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token successfully refreshed"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    public ResponseEntity<StandardResponse<LoginResponse>> refreshToken(
            @Parameter(description = "Refresh token", required = true)
            @RequestBody RefreshTokenRequest request) {
        try {
            LoginResponse response = authService.refreshToken(request.getRefreshToken());
            StandardResponse<LoginResponse> apiResponse = StandardResponse.success(response, "Token refreshed successfully");
            return ResponseEntity.ok(apiResponse);
        } catch (AuthenticationException e) {
            log.error("Token refresh failed: {}", e.getMessage(), e);
            StandardResponse<LoginResponse> errorResponse = StandardResponse.error("Token refresh failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
