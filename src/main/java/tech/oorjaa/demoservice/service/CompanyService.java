package tech.oorjaa.demoservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.oorjaa.demoservice.dto.CompanyDto;
import tech.oorjaa.demoservice.entity.User;
import tech.oorjaa.demoservice.mapper.CompanyMapper;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final UserContextService userContextService;
    private final CompanyMapper companyMapper;

    public CompanyDto getCurrentCompany() {
        User user = userContextService.getCurrentUser();
        return companyMapper.toDto(user.getCompany());
    }
}
