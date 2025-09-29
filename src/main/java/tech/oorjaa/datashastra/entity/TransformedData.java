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
@Table(name = "transformed_data")
@SQLDelete(sql = "UPDATE transformed_data SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class TransformedData extends BaseEntity {

    @Type(JsonBinaryType.class)
    @Column(name = "data_overview", columnDefinition = "JSONB")
    private JsonNode dataOverview; // JSON from MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "data_statistics", columnDefinition = "JSONB")
    private JsonNode dataStatistics; // JSON from MLDataProcessor

    @Type(JsonBinaryType.class)
    @Column(name = "data_sample", columnDefinition = "JSONB")
    private JsonNode dataSample; // JSON from MLDataProcessor

    // Relationships
    @OneToMany(mappedBy = "transformedData", cascade = CascadeType.ALL)
    private List<Forecast> forecasts = new ArrayList<>();
}