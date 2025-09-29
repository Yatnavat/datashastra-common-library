package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.Where;
import tech.oorjaa.datashastra.entity.enums.Priority;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activity")
@SQLDelete(sql = "UPDATE activity SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class Activity extends BaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "forecast_count", nullable = false)
    private Integer forecastCount = 0;

    // Foreign Keys
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // Tenant isolation
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;

    // Relationships
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<Forecast> forecasts = new ArrayList<>();
}