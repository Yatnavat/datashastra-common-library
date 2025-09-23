package tech.oorjaa.datashastra.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tech.oorjaa.datashastra.dto.CompanyDto;
import tech.oorjaa.datashastra.entity.Tenant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    /**
     * Converts a Tenant entity to a CompanyDto
     *
     * @param tenant the source entity
     * @return the mapped DTO
     */
    CompanyDto toDto(Tenant tenant);

    /**
     * Converts a CompanyDto to a Tenant entity
     *
     * @param companyDto the source DTO
     * @return the mapped entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Tenant toEntity(CompanyDto companyDto);

    /**
     * Updates an existing Tenant entity with data from CompanyDto
     *
     * @param companyDto the source DTO
     * @param tenant the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateCompanyFromDto(CompanyDto companyDto, @MappingTarget Tenant tenant);
}
