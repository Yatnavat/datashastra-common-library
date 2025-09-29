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
@Table(name = "date_range_configuration")
@SQLDelete(sql = "UPDATE date_range_configuration SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class DateRangeConfiguration extends BaseEntity {

    @Type(JsonBinaryType.class)
    @Column(name = "training_date_range", columnDefinition = "JSONB")
    private JsonNode trainingDateRange; // DateRangeObject JSON

    @Type(JsonBinaryType.class)
    @Column(name = "testing_date_range", columnDefinition = "JSONB")
    private JsonNode testingDateRange; // DateRangeObject JSON

    // Relationships
    @OneToMany(mappedBy = "dateRangeConfiguration", cascade = CascadeType.ALL)
    private List<Forecast> forecasts = new ArrayList<>();
}