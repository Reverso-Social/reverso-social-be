package com.reverso.mapper;

import com.reverso.dto.request.ServiceCategoryRequest;
import com.reverso.dto.response.ServiceCategoryResponse;
import com.reverso.model.ServiceCategory;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ServiceCategoryMapper {
    
    @Mapping(target = "servicesCount", expression = "java(category.getServices() != null ? category.getServices().size() : 0)")
    ServiceCategoryResponse toResponse(ServiceCategory category);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ServiceCategory toEntity(ServiceCategoryRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(ServiceCategoryRequest request, @MappingTarget ServiceCategory category);
}