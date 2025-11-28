package com.reverso.mapper;

import com.reverso.dto.ResourceCreateDto;
import com.reverso.dto.ResourceDto;
import com.reverso.dto.ResourceUpdateDto;
import com.reverso.model.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);

    ResourceDto toDto(Resource resource);

    Resource toEntity(ResourceCreateDto dto);

    void updateEntityFromDto(ResourceUpdateDto dto, @MappingTarget Resource resource);
}
