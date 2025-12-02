package com.reverso.service.interfaces;

import com.reverso.dto.request.ServiceRequest;
import com.reverso.dto.response.ServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ServiceService {
    
    Page<ServiceResponse> getAllServices(Pageable pageable);
    
    ServiceResponse getServiceById(UUID id);
    
    Page<ServiceResponse> getServicesByCategory(UUID categoryId, Pageable pageable);
    
    List<ServiceResponse> getActiveServicesByCategory(UUID categoryId);
    
    ServiceResponse createService(ServiceRequest request);
    
    ServiceResponse updateService(UUID id, ServiceRequest request);
    
    void deleteService(UUID id);
    
    Page<ServiceResponse> filterServices(UUID categoryId, Boolean active, String name, Pageable pageable);
}