package tech.oorjaa.datashastra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.oorjaa.datashastra.entity.Activity;
import tech.oorjaa.datashastra.entity.enums.Priority;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByTenantId(Long tenantId);

    List<Activity> findByClientId(Long clientId);

    List<Activity> findByProjectId(Long projectId);

    List<Activity> findByPriority(Priority priority);

    List<Activity> findByClientIdAndProjectId(Long clientId, Long projectId);

    List<Activity> findByNameContainingIgnoreCase(String name);
}