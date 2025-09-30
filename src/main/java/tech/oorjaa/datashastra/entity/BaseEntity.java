package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base entity providing common fields for all entities:
 * - Auto-increment Long ID
 * - Tenant isolation via tenantId
 * - Audit fields (created/modified by and date)
 * - Soft delete support
 *
 * All domain entities should extend this class.
 *
 * @author DataShastra Team
 * @version 2.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Tenant ID for multi-tenancy support
     * All queries must filter by this field
     */
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    protected String createdBy;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "modified_by", length = 100)
    protected String modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_date")
    protected LocalDateTime modifiedDate;

    /**
     * Soft delete flag - use instead of physical deletion
     */
    @Column(name = "deleted", nullable = false)
    protected Boolean deleted = false;

    /**
     * Mark entity as deleted (soft delete)
     */
    public void markAsDeleted() {
        this.deleted = true;
        this.modifiedDate = LocalDateTime.now();
    }

    /**
     * Check if entity is active (not deleted)
     */
    public boolean isActive() {
        return !deleted;
    }
}
