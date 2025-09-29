package tech.oorjaa.datashastra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import tech.oorjaa.datashastra.entity.enums.UserStatus;

/**
 * Data Transfer Object for User entity
 */
@Getter
@Setter
public class UserDto {

    private Long id;

    private String keycloakId;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Size(max = 20, message = "Mobile number must be less than 20 characters")
    private String mobileNumber;

    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;

    private String password;

    private UserStatus userStatus;

    @Size(max = 100, message = "Designation must be less than 100 characters")
    private String designation;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @Size(max = 10, message = "Pincode must be less than 10 characters")
    private String pincode;

    private Long tenantId;

    private String tenantName;

    // For convenience - derived fields
    private String firstName;

    private String lastName;
}
