package com.reverso.service.impl;

import com.reverso.dto.request.ServiceFeatureRequest;
import com.reverso.dto.response.ServiceFeatureResponse;
import com.reverso.exception.ResourceNotFoundException;
import com.reverso.mapper.ServiceFeatureMapper;
import com.reverso.model.Service;
import com.reverso.model.ServiceFeature;
import com.reverso.repository.ServiceFeatureRepository;
import com.reverso.repository.ServiceRepository;
import com.reverso.service.Interfaces.ServiceFeatureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class ServiceFeatureServiceImpl implements ServiceFeatureService {
    
    private final ServiceFeatureRepository featureRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceFeatureMapper featureMapper;
    
    public ServiceFeatureServiceImpl(ServiceFeatureRepository featureRepository,
                                    ServiceRepository serviceRepository,
                                    ServiceFeatureMapper featureMapper) {
        this.featureRepository = featureRepository;
        this.serviceRepository = serviceRepository;
        this.featureMapper = featureMapper;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceFeatureResponse> getAllFeatures(Pageable pageable) {
        return featureRepository.findAll(pageable)
            .map(featureMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ServiceFeatureResponse getFeatureById(UUID id) {
        ServiceFeature feature = featureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ServiceFeature", "id", id));
        
        return featureMapper.toResponse(feature);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceFeatureResponse> getFeaturesByService(UUID serviceId, Pageable pageable) {
        return featureRepository.findByServiceId(serviceId, pageable)
            .map(featureMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ServiceFeatureResponse> getFeaturesByServiceOrdered(UUID serviceId) {
        return featureRepository.findByServiceIdOrderBySortOrderAsc(serviceId)
            .stream()
            .map(featureMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public ServiceFeatureResponse createFeature(ServiceFeatureRequest request) {
        // Verificar que el servicio existe
        Service service = serviceRepository.findById(request.getServiceId())
            .orElseThrow(() -> new ResourceNotFoundException("Service", "id", request.getServiceId()));
        
        ServiceFeature feature = featureMapper.toEntity(request);
        feature.setService(service);
        
        ServiceFeature savedFeature = featureRepository.save(feature);
        return featureMapper.toResponse(savedFeature);
    }
    
    @Override
    public ServiceFeatureResponse updateFeature(UUID id, ServiceFeatureRequest request) {
        ServiceFeature feature = featureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ServiceFeature", "id", id));
        
        // Si se actualiza el servicio, verificar que existe
        if (request.getServiceId() != null && !request.getServiceId().equals(feature.getService().getId())) {
            Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", request.getServiceId()));
            feature.setService(service);
        }
        
        featureMapper.updateFromRequest(request, feature);
        
        ServiceFeature updatedFeature = featureRepository.save(feature);
        return featureMapper.toResponse(updatedFeature);
    }
    
    @Override
    public void deleteFeature(UUID id) {
        ServiceFeature feature = featureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ServiceFeature", "id", id));
        
        featureRepository.delete(feature);
    }
}