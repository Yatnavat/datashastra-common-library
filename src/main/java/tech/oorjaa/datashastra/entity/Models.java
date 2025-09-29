package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import tech.oorjaa.datashastra.entity.enums.ModelType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "models")
@SQLDelete(sql = "UPDATE models SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class Models extends BaseEntity {

    @NotBlank
    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "model_description", columnDefinition = "TEXT")
    private String modelDescription;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "model_type", nullable = false)
    private ModelType modelType;

    @Column(name = "model_icon")
    private String modelIcon; // URL or identifier

    @Column(name = "complexity")
    private String complexity;

    @Column(name = "accuracy")
    private String accuracy;

    @Column(name = "est_time")
    private String estTime; // Estimated execution time

    @Column(name = "best_for", columnDefinition = "TEXT")
    private String bestFor;

    @Column(name = "is_recommended", nullable = false)
    private Boolean isRecommended = false;

    // Relationships
    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<Features> features = new ArrayList<>();

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<Forecast> forecasts = new ArrayList<>();
}