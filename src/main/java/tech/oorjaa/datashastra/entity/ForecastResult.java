package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "forecast_result")
@SQLDelete(sql = "UPDATE forecast_result SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class ForecastResult extends BaseEntity {

    @Type(JsonBinaryType.class)
    @Column(name = "model_result_analysis", columnDefinition = "JSONB")
    private JsonNode modelResultAnalysis; // From MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "model_performance_summary", columnDefinition = "JSONB")
    private JsonNode modelPerformanceSummary; // From MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "historical_model_fit", columnDefinition = "JSONB")
    private JsonNode historicalModelFit; // From MLDataProcessor

    // Relationships
    @OneToOne(mappedBy = "forecastResult", cascade = CascadeType.ALL)
    private Forecast forecast;
}