package tech.oorjaa.datashastra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Login request with email and password")
public class LoginRequest {
    
    @NotBlank
    @Email
    @Schema(description = "User's username", example = "user@example.com")
    private String username;

    @NotBlank
    @Schema(description = "User password", example = "password", format = "password")
    private String password;
}
