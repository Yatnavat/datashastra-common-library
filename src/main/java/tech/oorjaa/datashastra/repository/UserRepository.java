package tech.oorjaa.datashastra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.oorjaa.datashastra.entity.User;
import tech.oorjaa.datashastra.entity.enums.UserStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByKeycloakId(String keycloakId);

    Optional<User> findByUsername(String username);

    List<User> findByTenantId(Long tenantId);

    List<User> findByUserStatusAndTenantId(UserStatus status, Long tenantId);

    boolean existsByEmail(String email);

    boolean existsByKeycloakId(String keycloakId);
}