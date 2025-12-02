package com.reverso.service.impl;

import com.reverso.dto.request.ServiceRequest;
import com.reverso.dto.response.ServiceResponse;
import com.reverso.exception.ResourceNotFoundException;
import com.reverso.mapper.ServiceMapper;
import com.reverso.model.Service;
import com.reverso.model.ServiceCategory;
import com.reverso.model.User;
import com.reverso.repository.ServiceCategoryRepository;
import com.reverso.repository.ServiceRepository;
import com.reverso.repository.UserRepository;
import com.reverso.service.interfaces.ServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class ServiceServiceImpl implements ServiceService {
    
    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ServiceMapper serviceMapper;
    
    public ServiceServiceImpl(ServiceRepository serviceRepository,
                             ServiceCategoryRepository categoryRepository,
                             UserRepository userRepository,
                             ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.serviceMapper = serviceMapper;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceResponse> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable)
            .map(serviceMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(UUID id) {
        Service service = serviceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
        
        return serviceMapper.toResponse(service);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceResponse> getServicesByCategory(UUID categoryId, Pageable pageable) {
        return serviceRepository.findByCategoryId(categoryId, pageable)
            .map(serviceMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getActiveServicesByCategory(UUID categoryId) {
        return serviceRepository.findByCategoryIdAndActiveOrderBySortOrderAsc(categoryId, true)
            .stream()
            .map(serviceMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public ServiceResponse createService(ServiceRequest request) {
        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("ServiceCategory", "id", request.getCategoryId()));
        
        Service service = serviceMapper.toEntity(request);
        service.setCategory(category);
        
        if (request.getCreatedByUserId() != null) {
            User user = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getCreatedByUserId()));
            service.setCreatedByUser(user);
        }
        
        if (service.getActive() == null) {
            service.setActive(true);
        }
        
        Service savedService = serviceRepository.save(service);
        return serviceMapper.toResponse(savedService);
    }
    
    @Override
    public ServiceResponse updateService(UUID id, ServiceRequest request) {
        Service service = serviceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        if (request.getCategoryId() != null && !request.getCategoryId().equals(service.getCategory().getId())) {
            ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ServiceCategory", "id", request.getCategoryId()));
            service.setCategory(category);
        }

        if (request.getCreatedByUserId() != null) {
            User user = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getCreatedByUserId()));
            service.setCreatedByUser(user);
        }
        
        serviceMapper.updateFromRequest(request, service);
        
        Service updatedService = serviceRepository.save(service);
        return serviceMapper.toResponse(updatedService);
    }
    
    @Override
    public void deleteService(UUID id) {
        Service service = serviceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
        
        serviceRepository.delete(service);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceResponse> filterServices(UUID categoryId, Boolean active, String name, Pageable pageable) {
        return serviceRepository.findByFilters(categoryId, active, name, pageable)
            .map(serviceMapper::toResponse);
    }
}