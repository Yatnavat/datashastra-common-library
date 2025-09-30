package tech.oorjaa.datashastra.mapper;

import org.mapstruct.*;
import tech.oorjaa.datashastra.dto.ClientDto;
import tech.oorjaa.datashastra.entity.Client;
import tech.oorjaa.datashastra.enums.ClientStatus;

import java.util.List;

/**
 * MapStruct mapper for converting between Client entity and ClientDto
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ClientMapper {

    /**
     * Convert Client entity to ClientDto
     */
    @Mapping(target = "isActive", expression = "java(client.isActive())")
    @Mapping(target = "statusBadgeColor", expression = "java(getStatusBadgeColor(client.getStatus()))")
    ClientDto toDto(Client client);

    /**
     * Convert ClientDto to Client entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Client toEntity(ClientDto dto);

    /**
     * Convert list of Client entities to DTOs
     */
    List<ClientDto> toDtoList(List<Client> clients);

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
    void updateEntityFromDto(ClientDto dto, @MappingTarget Client entity);

    /**
     * Helper method to determine status badge color
     */
    default String getStatusBadgeColor(ClientStatus status) {
        if (status == null) return "gray";
        return switch (status) {
            case ACTIVE -> "green";
            case PROSPECT -> "blue";
            case INACTIVE -> "gray";
            case ARCHIVED -> "red";
        };
    }
}