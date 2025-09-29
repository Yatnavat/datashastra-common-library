package tech.oorjaa.datashastra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.oorjaa.datashastra.entity.Forecast;
import tech.oorjaa.datashastra.entity.enums.ForecastStatus;
import tech.oorjaa.datashastra.entity.enums.ForecastType;

import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    List<Forecast> findByActivityIdAndStatus(Long activityId, ForecastStatus status);

    List<Forecast> findByTenantId(Long tenantId);

    List<Forecast> findByStatus(ForecastStatus status);

    List<Forecast> findByForecastType(ForecastType forecastType);

    List<Forecast> findByActivityId(Long activityId);

    List<Forecast> findByModelId(Long modelId);

    long countByActivityIdAndStatus(Long activityId, ForecastStatus status);

    long countByTenantId(Long tenantId);
}