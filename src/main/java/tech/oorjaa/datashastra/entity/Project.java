package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.Where;
import tech.oorjaa.datashastra.entity.enums.Industry;
import tech.oorjaa.datashastra.entity.enums.ProjectStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@SQLDelete(sql = "UPDATE project SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class Project extends BaseEntity {

    @Column(name = "project_name")
    private String projectName;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_type")
    private Industry industryType;

    @Column(name = "primary_contact_name")
    private String primaryContactName;

    @Column(name = "primary_contact_number")
    private String primaryContactNumber;

    @Email
    @Column(name = "primary_email_id")
    private String primaryEmailId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status;

    @Column(name = "data_schema_requirement", columnDefinition = "TEXT")
    private String dataSchemaRequirement; // From MLDataProcessor

    // Foreign Keys
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    // Tenant isolation
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;

    // Relationships
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();
}