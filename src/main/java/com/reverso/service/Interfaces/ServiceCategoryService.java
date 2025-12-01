package com.reverso.service.Interfaces;

import com.reverso.dto.request.ServiceCategoryRequest;
import com.reverso.dto.response.ServiceCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ServiceCategoryService {
    
    Page<ServiceCategoryResponse> getAllCategories(Pageable pageable);
    
    List<ServiceCategoryResponse> getActiveCategories();
    
    ServiceCategoryResponse getCategoryById(UUID id);
    
    ServiceCategoryResponse createCategory(ServiceCategoryRequest request);
    
    ServiceCategoryResponse updateCategory(UUID id, ServiceCategoryRequest request);
    
    void deleteCategory(UUID id);
    
    Page<ServiceCategoryResponse> searchCategories(String name, Pageable pageable);
}