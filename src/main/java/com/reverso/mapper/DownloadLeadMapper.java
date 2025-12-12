package com.reverso.mapper;

import com.reverso.dto.response.DownloadLeadResponse;
import com.reverso.model.DownloadLead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DownloadLeadMapper {

    @Mapping(target = "resourceId", source = "resource.id")
    @Mapping(target = "resourceTitle", source = "resource.title")
    DownloadLeadResponse toResponse(DownloadLead lead);
}