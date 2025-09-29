package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.Where;
import tech.oorjaa.datashastra.entity.enums.UserStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Setter
public class User extends BaseEntity {

    @NotBlank
    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;

    @NotBlank
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "username", nullable = false)
    private String username; // defaults to email

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(name = "designation")
    private String designation;

    @Column(name = "address")
    private String address;

    @Column(name = "pincode")
    private String pincode;

    // Tenant isolation
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Client> clients = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;
}
