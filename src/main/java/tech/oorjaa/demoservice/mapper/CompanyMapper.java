package tech.oorjaa.demoservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tech.oorjaa.demoservice.dto.CompanyDto;
import tech.oorjaa.demoservice.entity.Company;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    /**
     * Converts a Company entity to a CompanyDto
     *
     * @param company the source entity
     * @return the mapped DTO
     */
    CompanyDto toDto(Company company);

    /**
     * Converts a CompanyDto to a Company entity
     *
     * @param companyDto the source DTO
     * @return the mapped entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Company toEntity(CompanyDto companyDto);

    /**
     * Updates an existing Company entity with data from CompanyDto
     *
     * @param companyDto the source DTO
     * @param company the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateCompanyFromDto(CompanyDto companyDto, @MappingTarget Company company);
}
