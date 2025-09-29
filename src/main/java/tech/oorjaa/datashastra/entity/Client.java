package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.Where;
import tech.oorjaa.datashastra.entity.enums.ClientStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@SQLDelete(sql = "UPDATE client SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class Client extends BaseEntity {

    @NotBlank
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private ClientStatus userStatus = ClientStatus.ACTIVE;

    @Column(name = "contact_name")
    private String contactName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ClientStatus status;

    @Column(name = "registered_under")
    private String registeredUnder;

    // Address fields
    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address3")
    private String address3;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "zip_code")
    private String zipCode;

    // Contact fields
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "secondary_phone_number")
    private String secondaryPhoneNumber;

    @Column(name = "single_point_of_contact")
    private String singlePointOfContact;

    @Email
    @Column(name = "email")
    private String email;

    @Email
    @Column(name = "secondary_email")
    private String secondaryEmail;

    // Business fields
    @Column(name = "pan_card")
    private String panCard;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "gstin")
    private String gstin;

    // Banking fields
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "branch")
    private String branch;

    @NotBlank
    @Column(name = "client_code", nullable = false, unique = true)
    private String clientCode; // Auto-generated

    // Foreign Keys
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Linked user (owner)

    // Tenant isolation
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;

    // Relationships
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();
}