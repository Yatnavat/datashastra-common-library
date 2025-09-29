package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tenant")
@SQLDelete(sql = "UPDATE tenant SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class Tenant extends BaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "legal_name", nullable = false)
    private String legalName;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_number")
    private String contactNumber;

    @Email
    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "keycloak_server_url")
    private String keycloakServerUrl;

    @Column(name = "keycloak_realm")
    private String keycloakRealm;

    @Column(name = "keycloak_client_id")
    private String keycloakClientId;

    // Relationships
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<Client> clients = new ArrayList<>();

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();
}
