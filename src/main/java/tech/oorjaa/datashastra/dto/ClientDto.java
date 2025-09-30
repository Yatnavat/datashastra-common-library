package tech.oorjaa.datashastra.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import tech.oorjaa.datashastra.enums.ClientStatus;

import java.time.LocalDateTime;

/**
 * Client Data Transfer Object
 * Used for API communication and client management
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

    private Long id;

    private Integer tenantId;

    @NotBlank(message = "Client name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    private String industry;

    @Email(message = "Valid email is required")
    private String contactEmail;

    private String contactPhone;

    @NotNull(message = "Status is required")
    private ClientStatus status;

    // Computed/Display Fields
    private Boolean isActive;

    private String statusBadgeColor;

    // Audit Fields
    private String createdBy;

    private LocalDateTime createdDate;

    private String modifiedBy;

    private LocalDateTime modifiedDate;
}