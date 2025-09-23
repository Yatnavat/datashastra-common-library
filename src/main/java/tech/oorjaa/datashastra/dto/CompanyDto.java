package tech.oorjaa.datashastra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Tenant entity
 */
@Getter
@Setter
@Schema(description = "Tenant data transfer object")
public class CompanyDto {

    @Schema(description = "Unique identifier of the company")
    private Integer id;

    @Schema(description = "Name of the company")
    @NotBlank(message = "Tenant name is required")
    @Size(max = 100, message = "Tenant name must be less than 100 characters")
    private String name;

    @Schema(description = "Description of the company")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Schema(description = "Legal name of the company")
    @NotBlank(message = "Legal name is required")
    @Size(max = 100, message = "Legal name must be less than 100 characters")
    private String legalName;

    @Schema(description = "Address of the company")
    @Size(max = 500, message = "Address must be less than 500 characters")
    private String address;

    @Schema(description = "Contact number of the company")
    @Size(max = 20, message = "Contact number must be less than 20 characters")
    private String contactNumber;

    @Schema(description = "Contact email of the company")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String contactEmail;

    @Schema(description = "URL of the company logo")
    @Size(max = 255, message = "Logo URL must be less than 255 characters")
    private String logoUrl;

    @Schema(description = "Primary brand color of the company")
    @Size(max = 7, message = "Primary color must be less than 7 characters")
    private String primaryColor;

    @Schema(description = "Secondary brand color of the company")
    @Size(max = 7, message = "Secondary color must be less than 7 characters")
    private String secondaryColor;
}