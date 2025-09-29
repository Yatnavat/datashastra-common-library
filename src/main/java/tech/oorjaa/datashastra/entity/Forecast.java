package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.Where;
import tech.oorjaa.datashastra.entity.enums.ForecastStatus;
import tech.oorjaa.datashastra.entity.enums.ForecastType;

@Entity
@Table(name = "forecast", indexes = {
    @Index(name = "idx_forecast_activity", columnList = "activity_id"),
    @Index(name = "idx_forecast_tenant", columnList = "tenant_id"),
    @Index(name = "idx_forecast_status", columnList = "status")
})
@SQLDelete(sql = "UPDATE forecast SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class Forecast extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", nullable = false)
    private ForecastStatus status = ForecastStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "forecast_type")
    private ForecastType forecastType; // From MLDataProcessor

    // Foreign Key Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_data_id")
    private UploadedData uploadedData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformed_data_id")
    private TransformedData transformedData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Models model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id")
    private Features features;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aggregation_id")
    private Aggregation aggregation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_range_config_id")
    private DateRangeConfiguration dateRangeConfiguration;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private ForecastResult forecastResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exploratory_data_analytics_id")
    private ExploratoryDataAnalytics exploratoryDataAnalytics;

    // Tenant isolation
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;
}