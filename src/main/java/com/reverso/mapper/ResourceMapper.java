package com.reverso.mapper;

import com.reverso.dto.ResourceCreateDto;
import com.reverso.dto.ResourceDto;
import com.reverso.dto.ResourceUpdateDto;
import com.reverso.model.Resource;
import com.reverso.model.enums.ResourceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    @Mapping(target = "type", source = "type", qualifiedByName = "typeToString")
    @Mapping(target = "userId", source = "user.id")
    ResourceDto toDto(Resource resource);

    @Mapping(target = "type", source = "type", qualifiedByName = "stringToType")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "downloads", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Resource toEntity(ResourceCreateDto dto);

    @Mapping(target = "type", source = "type", qualifiedByName = "stringToType")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "downloads", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ResourceUpdateDto dto, @MappingTarget Resource resource);

    @Named("typeToString")
    default String typeToString(ResourceType type) {
        return type != null ? type.name() : null;
    }

    @Named("stringToType")
    default ResourceType stringToType(String type) {
        return type != null ? ResourceType.valueOf(type.toUpperCase()) : null;
    }
}