package com.reverso.mapper;

import com.reverso.dto.request.ResourceDownloadRequest;
import com.reverso.dto.response.ResourceDownloadResponse;
import com.reverso.model.ResourceDownload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResourceDownloadMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "resourceId", source = "resource.id")
    @Mapping(target = "resourceTitle", source = "resource.title")
    ResourceDownloadResponse toResponse(ResourceDownload download);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "resource", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ResourceDownload toEntity(ResourceDownloadRequest request);
}