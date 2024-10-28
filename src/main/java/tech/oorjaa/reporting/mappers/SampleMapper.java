package tech.oorjaa.reporting.mappers;

import org.mapstruct.Mapper;
import tech.oorjaa.reporting.dbentities.Sample;
import tech.oorjaa.reporting.models.SampleModel;

@Mapper(componentModel = "spring")
public interface SampleMapper extends BaseMapper<Sample, SampleModel> {

// Extend base mapping functionality with additional logic if needed

}
