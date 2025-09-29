package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "features")
@SQLDelete(sql = "UPDATE features SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class Features extends BaseEntity {

    @Type(JsonBinaryType.class)
    @Column(name = "features", columnDefinition = "JSONB")
    private JsonNode features; // JSON containing feature data and metadata

    // Foreign Keys
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private Models model;

    // Relationships
    @OneToMany(mappedBy = "features", cascade = CascadeType.ALL)
    private List<Forecast> forecasts = new ArrayList<>();
}