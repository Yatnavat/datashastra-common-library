package tech.oorjaa.datashastra.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import tech.oorjaa.datashastra.enums.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Project Data Transfer Object
 * Used for API communication and project management
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {

    private Long id;

    private Integer tenantId;

    @NotBlank(message = "Project name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @DecimalMin(value = "0.0", message = "Budget must be positive")
    private BigDecimal budget;

    private String currencyCode;

    @NotNull(message = "Status is required")
    private ProjectStatus status;

    // Computed/Display Fields
    private Boolean isActive;

    private String statusBadgeColor;

    private String formattedBudget;

    // Audit Fields
    private String createdBy;

    private LocalDateTime createdDate;

    private String modifiedBy;

    private LocalDateTime modifiedDate;
}