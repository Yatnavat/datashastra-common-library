package tech.oorjaa.datashastra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import tech.oorjaa.datashastra.entity.enums.UploadDataType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "uploaded_data")
@SQLDelete(sql = "UPDATE uploaded_data SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
public class UploadedData extends BaseEntity {

    @NotBlank
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotBlank
    @Column(name = "url", nullable = false)
    private String url; // File storage path/URL

    @NotNull
    @Column(name = "time_stamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "upload_data_type", nullable = false)
    private UploadDataType uploadDataType;

    @Column(name = "data_schema_requirement", columnDefinition = "TEXT")
    private String dataSchemaRequirement; // From MLDataProcessor validation

    @Column(name = "validation_results", columnDefinition = "TEXT")
    private String validationResults; // From MLDataProcessor validation

    // Relationships
    @OneToMany(mappedBy = "uploadedData", cascade = CascadeType.ALL)
    private List<Forecast> forecasts = new ArrayList<>();
}