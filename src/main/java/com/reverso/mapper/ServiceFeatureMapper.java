package com.reverso.mapper;

import com.reverso.dto.request.ServiceFeatureRequest;
import com.reverso.dto.response.ServiceFeatureResponse;
import com.reverso.model.ServiceFeature;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ServiceFeatureMapper {
    
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.name")
    ServiceFeatureResponse toResponse(ServiceFeature feature);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ServiceFeature toEntity(ServiceFeatureRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(ServiceFeatureRequest request, @MappingTarget ServiceFeature feature);
}