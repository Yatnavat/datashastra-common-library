package tech.oorjaa.demoservice.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import tech.oorjaa.demoservice.dto.CompanyDto;
import tech.oorjaa.demoservice.entity.Company;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company Mapper Tests")
class CompanyMapperTest {

    private CompanyMapper companyMapper;
    private Company company;
    private CompanyDto companyDto;

    @BeforeEach
    void setUp() {
        companyMapper = Mappers.getMapper(CompanyMapper.class);

        // Set up company entity
        company = new Company();
        company.setId(1L);
        company.setName("Tech Corp");
        company.setDescription("A leading technology company");
        company.setLegalName("Tech Corporation Ltd");
        company.setAddress("123 Tech Street, Silicon Valley, CA");
        company.setContactNumber("+1-555-0123");
        company.setContactEmail("contact@techcorp.com");
        company.setLogoUrl("https://techcorp.com/logo.png");
        company.setPrimaryColor("#007bff");
        company.setSecondaryColor("#6c757d");
        company.setCreatedDate(LocalDateTime.now().minusDays(30));
        company.setLastModifiedDate(LocalDateTime.now());
        company.setCreatedBy("system");
        company.setLastModifiedBy("admin");

        // Set up company DTO
        companyDto = new CompanyDto();
        companyDto.setId(2);
        companyDto.setName("Another Tech Corp");
        companyDto.setDescription("Another technology company");
        companyDto.setLegalName("Another Tech Corporation Inc");
        companyDto.setAddress("456 Innovation Blvd, Tech City, CA");
        companyDto.setContactNumber("+1-555-0456");
        companyDto.setContactEmail("info@anothertech.com");
        companyDto.setLogoUrl("https://anothertech.com/logo.png");
        companyDto.setPrimaryColor("#28a745");
        companyDto.setSecondaryColor("#ffc107");
    }

    @Test
    @DisplayName("Should map Company entity to CompanyDto correctly")
    void toDto_ShouldMapCompanyEntityToCompanyDto() {
        // When
        CompanyDto result = companyMapper.toDto(company);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(company.getId().intValue());
        assertThat(result.getName()).isEqualTo(company.getName());
        assertThat(result.getDescription()).isEqualTo(company.getDescription());
        assertThat(result.getLegalName()).isEqualTo(company.getLegalName());
        assertThat(result.getAddress()).isEqualTo(company.getAddress());
        assertThat(result.getContactNumber()).isEqualTo(company.getContactNumber());
        assertThat(result.getContactEmail()).isEqualTo(company.getContactEmail());
        assertThat(result.getLogoUrl()).isEqualTo(company.getLogoUrl());
        assertThat(result.getPrimaryColor()).isEqualTo(company.getPrimaryColor());
        assertThat(result.getSecondaryColor()).isEqualTo(company.getSecondaryColor());
    }

    @Test
    @DisplayName("Should handle null Company entity gracefully")
    void toDto_WithNullCompany_ShouldReturnNull() {
        // When
        CompanyDto result = companyMapper.toDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle Company with null values")
    void toDto_WithNullValues_ShouldMapCorrectly() {
        // Given
        Company companyWithNulls = new Company();
        companyWithNulls.setId(1L);
        companyWithNulls.setName("Minimal Corp");
        companyWithNulls.setLegalName("Minimal Corporation");
        // Other fields are null

        // When
        CompanyDto result = companyMapper.toDto(companyWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Minimal Corp");
        assertThat(result.getLegalName()).isEqualTo("Minimal Corporation");
        assertThat(result.getDescription()).isNull();
        assertThat(result.getAddress()).isNull();
        assertThat(result.getContactNumber()).isNull();
        assertThat(result.getContactEmail()).isNull();
        assertThat(result.getLogoUrl()).isNull();
        assertThat(result.getPrimaryColor()).isNull();
        assertThat(result.getSecondaryColor()).isNull();
    }

    @Test
    @DisplayName("Should map CompanyDto to Company entity correctly")
    void toEntity_ShouldMapCompanyDtoToCompanyEntity() {
        // When
        Company result = companyMapper.toEntity(companyDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(companyDto.getName());
        assertThat(result.getDescription()).isEqualTo(companyDto.getDescription());
        assertThat(result.getLegalName()).isEqualTo(companyDto.getLegalName());
        assertThat(result.getAddress()).isEqualTo(companyDto.getAddress());
        assertThat(result.getContactNumber()).isEqualTo(companyDto.getContactNumber());
        assertThat(result.getContactEmail()).isEqualTo(companyDto.getContactEmail());
        assertThat(result.getLogoUrl()).isEqualTo(companyDto.getLogoUrl());
        assertThat(result.getPrimaryColor()).isEqualTo(companyDto.getPrimaryColor());
        assertThat(result.getSecondaryColor()).isEqualTo(companyDto.getSecondaryColor());

        // Ignored fields should be null
        assertThat(result.getId()).isNull();
        assertThat(result.getCreatedDate()).isNull();
        assertThat(result.getLastModifiedDate()).isNull();
        assertThat(result.getCreatedBy()).isNull();
        assertThat(result.getLastModifiedBy()).isNull();
    }

    @Test
    @DisplayName("Should handle null CompanyDto gracefully")
    void toEntity_WithNullCompanyDto_ShouldReturnNull() {
        // When
        Company result = companyMapper.toEntity(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle CompanyDto with null values")
    void toEntity_WithNullValues_ShouldMapCorrectly() {
        // Given
        CompanyDto dtoWithNulls = new CompanyDto();
        dtoWithNulls.setName("Basic Corp");
        dtoWithNulls.setLegalName("Basic Corporation");
        // Other fields are null

        // When
        Company result = companyMapper.toEntity(dtoWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Basic Corp");
        assertThat(result.getLegalName()).isEqualTo("Basic Corporation");
        assertThat(result.getDescription()).isNull();
        assertThat(result.getAddress()).isNull();
        assertThat(result.getContactNumber()).isNull();
        assertThat(result.getContactEmail()).isNull();
        assertThat(result.getLogoUrl()).isNull();
        assertThat(result.getPrimaryColor()).isNull();
        assertThat(result.getSecondaryColor()).isNull();
    }

    @Test
    @DisplayName("Should update Company entity from CompanyDto correctly")
    void updateCompanyFromDto_ShouldUpdateExistingCompany() {
        // Given
        Company existingCompany = new Company();
        existingCompany.setId(1L);
        existingCompany.setName("Original Corp");
        existingCompany.setDescription("Original description");
        existingCompany.setLegalName("Original Corporation");
        existingCompany.setAddress("Original Address");
        existingCompany.setContactNumber("Original Number");
        existingCompany.setContactEmail("original@example.com");
        existingCompany.setLogoUrl("https://original.com/logo.png");
        existingCompany.setPrimaryColor("#000000");
        existingCompany.setSecondaryColor("#ffffff");
        existingCompany.setCreatedDate(LocalDateTime.now().minusDays(10));
        existingCompany.setCreatedBy("originalCreator");

        // When
        companyMapper.updateCompanyFromDto(companyDto, existingCompany);

        // Then
        assertThat(existingCompany.getName()).isEqualTo(companyDto.getName());
        assertThat(existingCompany.getDescription()).isEqualTo(companyDto.getDescription());
        assertThat(existingCompany.getLegalName()).isEqualTo(companyDto.getLegalName());
        assertThat(existingCompany.getAddress()).isEqualTo(companyDto.getAddress());
        assertThat(existingCompany.getContactNumber()).isEqualTo(companyDto.getContactNumber());
        assertThat(existingCompany.getContactEmail()).isEqualTo(companyDto.getContactEmail());
        assertThat(existingCompany.getLogoUrl()).isEqualTo(companyDto.getLogoUrl());
        assertThat(existingCompany.getPrimaryColor()).isEqualTo(companyDto.getPrimaryColor());
        assertThat(existingCompany.getSecondaryColor()).isEqualTo(companyDto.getSecondaryColor());

        // Ignored fields should remain unchanged
        assertThat(existingCompany.getId()).isEqualTo(1L); // Original ID preserved
        assertThat(existingCompany.getCreatedDate()).isNotNull(); // Audit fields preserved
        assertThat(existingCompany.getCreatedBy()).isEqualTo("originalCreator");
    }

    @Test
    @DisplayName("Should handle update with null DTO values")
    void updateCompanyFromDto_WithNullDtoValues_ShouldUpdateOnlyNonNullFields() {
        // Given
        Company existingCompany = new Company();
        existingCompany.setId(1L);
        existingCompany.setName("Original Corp");
        existingCompany.setDescription("Original description");
        existingCompany.setLegalName("Original Corporation");
        existingCompany.setContactEmail("original@example.com");

        CompanyDto partialDto = new CompanyDto();
        partialDto.setName("Updated Corp");
        partialDto.setLegalName("Updated Corporation");
        // Other fields are null

        // When
        companyMapper.updateCompanyFromDto(partialDto, existingCompany);

        // Then
        assertThat(existingCompany.getName()).isEqualTo("Updated Corp");
        assertThat(existingCompany.getLegalName()).isEqualTo("Updated Corporation");
        // Null values in DTO should set corresponding fields to null
        assertThat(existingCompany.getDescription()).isNull();
        assertThat(existingCompany.getContactEmail()).isNull();
    }

    @Test
    @DisplayName("Should handle update with null CompanyDto")
    void updateCompanyFromDto_WithNullDto_ShouldNotThrowException() {
        // Given
        Company existingCompany = new Company();
        existingCompany.setName("Original Corp");

        // When & Then
        // Should not throw exception
        companyMapper.updateCompanyFromDto(null, existingCompany);
        
        // Original values should remain unchanged
        assertThat(existingCompany.getName()).isEqualTo("Original Corp");
    }

    @Test
    @DisplayName("Should handle mapping with empty strings")
    void mapping_WithEmptyStrings_ShouldPreserveEmptyStrings() {
        // Given
        CompanyDto dtoWithEmptyStrings = new CompanyDto();
        dtoWithEmptyStrings.setName("");
        dtoWithEmptyStrings.setDescription("");
        dtoWithEmptyStrings.setLegalName("Valid Legal Name");
        dtoWithEmptyStrings.setAddress("");
        dtoWithEmptyStrings.setContactNumber("");
        dtoWithEmptyStrings.setContactEmail("test@example.com");

        // When
        Company result = companyMapper.toEntity(dtoWithEmptyStrings);

        // Then
        assertThat(result.getName()).isEqualTo("");
        assertThat(result.getDescription()).isEqualTo("");
        assertThat(result.getLegalName()).isEqualTo("Valid Legal Name");
        assertThat(result.getAddress()).isEqualTo("");
        assertThat(result.getContactNumber()).isEqualTo("");
        assertThat(result.getContactEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should maintain data integrity in bidirectional mapping")
    void bidirectionalMapping_ShouldMaintainDataIntegrity() {
        // Given - Start with a CompanyDto
        CompanyDto originalDto = new CompanyDto();
        originalDto.setName("Test Corp");
        originalDto.setDescription("Test Description");
        originalDto.setLegalName("Test Corporation");
        originalDto.setAddress("Test Address");
        originalDto.setContactNumber("123-456-7890");
        originalDto.setContactEmail("test@corp.com");
        originalDto.setLogoUrl("https://test.com/logo.png");
        originalDto.setPrimaryColor("#ff0000");
        originalDto.setSecondaryColor("#00ff00");

        // When - Convert to entity and back to DTO
        Company entity = companyMapper.toEntity(originalDto);
        entity.setId(1L); // Simulate persisted entity
        CompanyDto resultDto = companyMapper.toDto(entity);

        // Then - All data should be preserved
        assertThat(resultDto.getName()).isEqualTo(originalDto.getName());
        assertThat(resultDto.getDescription()).isEqualTo(originalDto.getDescription());
        assertThat(resultDto.getLegalName()).isEqualTo(originalDto.getLegalName());
        assertThat(resultDto.getAddress()).isEqualTo(originalDto.getAddress());
        assertThat(resultDto.getContactNumber()).isEqualTo(originalDto.getContactNumber());
        assertThat(resultDto.getContactEmail()).isEqualTo(originalDto.getContactEmail());
        assertThat(resultDto.getLogoUrl()).isEqualTo(originalDto.getLogoUrl());
        assertThat(resultDto.getPrimaryColor()).isEqualTo(originalDto.getPrimaryColor());
        assertThat(resultDto.getSecondaryColor()).isEqualTo(originalDto.getSecondaryColor());
        assertThat(resultDto.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle special characters in text fields")
    void mapping_WithSpecialCharacters_ShouldPreserveCharacters() {
        // Given
        CompanyDto dtoWithSpecialChars = new CompanyDto();
        dtoWithSpecialChars.setName("Tëch Çorp & Cø.");
        dtoWithSpecialChars.setDescription("A company with spécial charactërs: @#$%^&*()");
        dtoWithSpecialChars.setLegalName("Tëch Çorporation Ltd.");
        dtoWithSpecialChars.setAddress("123 Maîn St, Sãn Francísco, CA");

        // When
        Company result = companyMapper.toEntity(dtoWithSpecialChars);

        // Then
        assertThat(result.getName()).isEqualTo("Tëch Çorp & Cø.");
        assertThat(result.getDescription()).isEqualTo("A company with spécial charactërs: @#$%^&*()");
        assertThat(result.getLegalName()).isEqualTo("Tëch Çorporation Ltd.");
        assertThat(result.getAddress()).isEqualTo("123 Maîn St, Sãn Francísco, CA");
    }
}