package com.reverso.service.interfaces;

import com.reverso.dto.request.ResourceCreateRequest;
import com.reverso.dto.request.ResourceUpdateRequest;
import com.reverso.dto.response.ResourceResponse;

import java.util.List;
import java.util.UUID;

public interface ResourceService {
    
    ResourceResponse create(ResourceCreateRequest dto);
    
    List<ResourceResponse> getAll();
    
    List<ResourceResponse> getPublic();
    
    List<ResourceResponse> getByType(String type);
    
    ResourceResponse getById(UUID id);
    
    ResourceResponse update(UUID id, ResourceUpdateRequest dto);
    
    void delete(UUID id);
}