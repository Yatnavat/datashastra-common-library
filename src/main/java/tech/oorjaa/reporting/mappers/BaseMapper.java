package tech.oorjaa.reporting.mappers;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<ENTITY, MODEL> {
    MODEL toModel(ENTITY entity);

    ENTITY toEntity(MODEL model);

    List<MODEL> toModelList(List<ENTITY> entities);

    List<ENTITY> toEntityList(List<MODEL> models);

    void updateEntityFromModel(MODEL model, @MappingTarget ENTITY entity);
}


