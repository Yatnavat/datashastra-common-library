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
@Table(name = "exploratory_data_analytics")
@SQLDelete(sql = "UPDATE exploratory_data_analytics SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class ExploratoryDataAnalytics extends BaseEntity {

    @Type(JsonBinaryType.class)
    @Column(name = "data_overview", columnDefinition = "JSONB")
    private JsonNode dataOverview; // From MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "data_quality", columnDefinition = "JSONB")
    private JsonNode dataQuality; // From MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "temporal_analysis", columnDefinition = "JSONB")
    private JsonNode temporalAnalysis; // From MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "distributional_analysis", columnDefinition = "JSONB")
    private JsonNode distributionalAnalysis; // From MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "categorical_analysis", columnDefinition = "JSONB")
    private JsonNode categoricalAnalysis; // From MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "relationship_correlations", columnDefinition = "JSONB")
    private JsonNode relationshipCorrelations; // From MLDataProcessor

    // Relationships
    @OneToMany(mappedBy = "exploratoryDataAnalytics", cascade = CascadeType.ALL)
    private List<Forecast> forecasts = new ArrayList<>();
}