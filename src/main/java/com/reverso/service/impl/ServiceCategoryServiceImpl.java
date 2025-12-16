package com.reverso.service.impl;

import com.reverso.dto.request.ServiceCategoryRequest;
import com.reverso.dto.response.ServiceCategoryResponse;
import com.reverso.exception.ResourceNotFoundException;
import com.reverso.mapper.ServiceCategoryMapper;
import com.reverso.model.ServiceCategory;
import com.reverso.repository.ServiceCategoryRepository;
import com.reverso.service.interfaces.ServiceCategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceCategoryServiceImpl implements ServiceCategoryService {
    
    private final ServiceCategoryRepository categoryRepository;
    private final ServiceCategoryMapper categoryMapper;
    
    public ServiceCategoryServiceImpl(ServiceCategoryRepository categoryRepository, 
                                     ServiceCategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceCategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
            .map(categoryMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ServiceCategoryResponse> getActiveCategories() {
        return categoryRepository.findByActiveOrderBySortOrderAsc(true)
            .stream()
            .map(categoryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ServiceCategoryResponse getCategoryById(UUID id) {
        ServiceCategory category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ServiceCategory", "id", id));
        
        return categoryMapper.toResponse(category);
    }
    
    @Override
    public ServiceCategoryResponse createCategory(ServiceCategoryRequest request) {
        ServiceCategory category = categoryMapper.toEntity(request);
        
        if (category.getActive() == null) {
            category.setActive(true);
        }
        
        ServiceCategory savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }
    
    @Override
    public ServiceCategoryResponse updateCategory(UUID id, ServiceCategoryRequest request) {
        ServiceCategory category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ServiceCategory", "id", id));
        
        categoryMapper.updateFromRequest(request, category);
        
        ServiceCategory updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }
    
    @Override
    public void deleteCategory(UUID id) {
        ServiceCategory category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ServiceCategory", "id", id));
        
        categoryRepository.delete(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceCategoryResponse> searchCategories(String name, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(name, pageable)
            .map(categoryMapper::toResponse);
    }
}