package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "validation_results")
@SQLDelete(sql = "UPDATE validation_results SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class ValidationResults extends BaseEntity {

    @Column(name = "total_rows")
    private Integer totalRows; // From MLDataProcessor

    @Column(name = "missing_values")
    private Integer missingValues; // From MLDataProcessor

    @Column(name = "detected_outliers")
    private Integer detectedOutliers; // From MLDataProcessor

    @Column(name = "outlier_columns", columnDefinition = "TEXT")
    private String outlierColumns; // From MLDataProcessor

    @Column(name = "additional_columns", columnDefinition = "TEXT")
    private String additionalColumns; // From MLDataProcessor
}