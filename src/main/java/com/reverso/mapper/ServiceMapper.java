package com.reverso.mapper;

import com.reverso.dto.request.ServiceRequest;
import com.reverso.dto.response.ServiceResponse;
import com.reverso.model.Service;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ServiceMapper {
    
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "featuresCount", expression = "java(service.getFeatures() != null ? service.getFeatures().size() : 0)")
    @Mapping(target = "createdByUserId", source = "createdByUser.id")
    @Mapping(target = "createdByUserName", source = "createdByUser.fullName")
    ServiceResponse toResponse(Service service);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "features", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Service toEntity(ServiceRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "features", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(ServiceRequest request, @MappingTarget Service service);
}