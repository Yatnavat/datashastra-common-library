package tech.oorjaa.datashastra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.oorjaa.datashastra.entity.Tenant;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByName(String name);

    Optional<Tenant> findByLegalName(String legalName);

    boolean existsByName(String name);
}