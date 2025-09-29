package tech.oorjaa.datashastra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.oorjaa.datashastra.entity.Client;
import tech.oorjaa.datashastra.entity.enums.ClientStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByTenantId(Long tenantId);

    List<Client> findByStatus(ClientStatus status);

    List<Client> findByUserId(Long userId);

    Optional<Client> findByClientCode(String clientCode);

    List<Client> findByClientNameContainingIgnoreCase(String clientName);

    boolean existsByClientCode(String clientCode);
}