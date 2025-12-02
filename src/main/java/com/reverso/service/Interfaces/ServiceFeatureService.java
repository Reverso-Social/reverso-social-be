package com.reverso.service.interfaces;

import com.reverso.dto.request.ServiceFeatureRequest;
import com.reverso.dto.response.ServiceFeatureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ServiceFeatureService {
    
    Page<ServiceFeatureResponse> getAllFeatures(Pageable pageable);
    
    ServiceFeatureResponse getFeatureById(UUID id);
    
    Page<ServiceFeatureResponse> getFeaturesByService(UUID serviceId, Pageable pageable);
    
    List<ServiceFeatureResponse> getFeaturesByServiceOrdered(UUID serviceId);
    
    ServiceFeatureResponse createFeature(ServiceFeatureRequest request);
    
    ServiceFeatureResponse updateFeature(UUID id, ServiceFeatureRequest request);
    
    void deleteFeature(UUID id);
}