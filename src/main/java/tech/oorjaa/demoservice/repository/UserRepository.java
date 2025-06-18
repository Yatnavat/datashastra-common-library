package tech.oorjaa.demoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.oorjaa.demoservice.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByCompanyId(Long companyId);
    boolean existsByEmail(String email);
    Optional<User> findByIdAndCompanyId(Long id, Long companyId);
    Optional<User> findByKeycloakId(String keycloakId);
}
