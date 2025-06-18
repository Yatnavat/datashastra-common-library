package tech.oorjaa.demoservice.mapper;

import org.mapstruct.Mapper;
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
}
