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
@Table(name = "aggregation")
@SQLDelete(sql = "UPDATE aggregation SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class Aggregation extends BaseEntity {

    @Type(JsonBinaryType.class)
    @Column(name = "geographic_aggregation_levels", columnDefinition = "JSONB")
    private JsonNode geographicAggregationLevels; // Individual, Regional, National

    @Type(JsonBinaryType.class)
    @Column(name = "product_aggregation_levels", columnDefinition = "JSONB")
    private JsonNode productAggregationLevels; // Individual, Product categories, Brand Level

    @Type(JsonBinaryType.class)
    @Column(name = "temporal_aggregation_level", columnDefinition = "JSONB")
    private JsonNode temporalAggregationLevel; // Daily, Weekly, Monthly

    @Type(JsonBinaryType.class)
    @Column(name = "minimum_threshold_dropdown", columnDefinition = "JSONB")
    private JsonNode minimumThresholdDropDown;

    @Type(JsonBinaryType.class)
    @Column(name = "geographic_aggregation_details", columnDefinition = "JSONB")
    private JsonNode geographicAggregationDetails; // Detailed configuration

    @Type(JsonBinaryType.class)
    @Column(name = "product_aggregation_details", columnDefinition = "JSONB")
    private JsonNode productAggregationDetails; // Detailed configuration

    @Type(JsonBinaryType.class)
    @Column(name = "temporal_aggregation_details", columnDefinition = "JSONB")
    private JsonNode temporalAggregationDetails; // Detailed configuration

    @Type(JsonBinaryType.class)
    @Column(name = "forecast_horizon", columnDefinition = "JSONB")
    private JsonNode forecastHorizon;

    // Relationships
    @OneToMany(mappedBy = "aggregation", cascade = CascadeType.ALL)
    private List<Forecast> forecasts = new ArrayList<>();
}