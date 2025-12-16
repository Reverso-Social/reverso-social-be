package com.reverso.mapper;

import com.reverso.dto.request.ResourceCreateRequest;
import com.reverso.dto.request.ResourceUpdateRequest;
import com.reverso.dto.response.ResourceResponse;
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
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "downloadCount", expression = "java(resource.getDownloads() != null ? (long)resource.getDownloads().size() : 0L)")
    ResourceResponse toResponse(Resource resource);

    @Mapping(target = "type", source = "type", qualifiedByName = "stringToType")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "downloads", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Resource toEntity(ResourceCreateRequest dto);

    @Mapping(target = "type", source = "type", qualifiedByName = "stringToType")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "downloads", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(ResourceUpdateRequest dto, @MappingTarget Resource resource);

    @Named("typeToString")
    default String typeToString(ResourceType type) {
        return type != null ? type.name() : null;
    }

    @Named("stringToType")
    default ResourceType stringToType(String type) {
        return type != null ? ResourceType.valueOf(type.toUpperCase()) : null;
    }
}