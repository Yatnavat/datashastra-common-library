package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import tech.oorjaa.datashastra.enums.ConfigDataType;

/**
 * TenantConfiguration Entity - Dynamic Key-Value Settings per Tenant
 * Allows flexible tenant-specific configuration without schema changes
 */
@Entity
@Table(name = "tenant_configuration", indexes = {
    @Index(name = "idx_tenant_config_key", columnList = "tenant_id, config_key", unique = true),
    @Index(name = "idx_tenant_config_category", columnList = "tenant_id, category")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantConfiguration extends BaseEntity {

    @NotBlank(message = "Config key is required")
    @Size(max = 100)
    @Column(name = "config_key", nullable = false, length = 100)
    private String configKey;

    @NotBlank(message = "Config value is required")
    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue;

    @Size(max = 50)
    @Column(name = "category", length = 50)
    private String category;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "Data type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 20)
    @Builder.Default
    private ConfigDataType dataType = ConfigDataType.STRING;

    // Business logic methods

    public String getStringValue() {
        return configValue;
    }

    public Integer getIntValue() {
        return Integer.parseInt(configValue);
    }

    public Boolean getBooleanValue() {
        return Boolean.parseBoolean(configValue);
    }

    public Double getDoubleValue() {
        return Double.parseDouble(configValue);
    }
}