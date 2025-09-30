package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.TenantDto;
import tech.oorjaa.datashastra.entity.Tenant;
import tech.oorjaa.datashastra.enums.TenantStatus;

import java.util.List;

/**
 * MapStruct mapper for converting between Tenant entity and TenantDto
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantMapper {

    /**
     * Convert Tenant entity to TenantDto
     */
    @Mapping(target = "isActive", expression = "java(tenant.isActive())")
    @Mapping(target = "statusBadgeColor", expression = "java(getStatusBadgeColor(tenant.getStatus()))")
    @Mapping(target = "displayName", source = "name")
    @Mapping(target = "keycloakClientSecret", ignore = true) // Never expose secret
    TenantDto toDto(Tenant tenant);

    /**
     * Convert TenantDto to Tenant entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Tenant toEntity(TenantDto dto);

    /**
     * Convert list of Tenant entities to DTOs
     */
    List<TenantDto> toDtoList(List<Tenant> tenants);

    /**
     * Update existing entity from DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    void updateEntityFromDto(TenantDto dto, @MappingTarget Tenant entity);

    /**
     * Helper method to determine status badge color
     */
    default String getStatusBadgeColor(TenantStatus status) {
        if (status == null) return "gray";
        return switch (status) {
            case ACTIVE -> "green";
            case TRIAL -> "blue";
            case SUSPENDED -> "orange";
            case INACTIVE -> "gray";
            case EXPIRED -> "red";
        };
    }
}