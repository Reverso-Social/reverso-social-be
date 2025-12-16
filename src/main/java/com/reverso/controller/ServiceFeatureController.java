package com.reverso.controller;

import com.reverso.dto.request.ServiceFeatureRequest;
import com.reverso.dto.response.ServiceFeatureResponse;
import com.reverso.service.interfaces.ServiceFeatureService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-features")
public class ServiceFeatureController {
    
    private final ServiceFeatureService featureService;
    
    public ServiceFeatureController(ServiceFeatureService featureService) {
        this.featureService = featureService;
    }
    
    @GetMapping
    public ResponseEntity<Page<ServiceFeatureResponse>> getAllFeatures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "sortOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), sort);
        return ResponseEntity.ok(featureService.getAllFeatures(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ServiceFeatureResponse> getFeatureById(@PathVariable UUID id) {
        return ResponseEntity.ok(featureService.getFeatureById(id));
    }
    
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<Page<ServiceFeatureResponse>> getFeaturesByService(
            @PathVariable UUID serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), 
                                          Sort.by("sortOrder").ascending());
        return ResponseEntity.ok(featureService.getFeaturesByService(serviceId, pageable));
    }
    
    @GetMapping("/service/{serviceId}/ordered")
    public ResponseEntity<List<ServiceFeatureResponse>> getFeaturesByServiceOrdered(@PathVariable UUID serviceId) {
        return ResponseEntity.ok(featureService.getFeaturesByServiceOrdered(serviceId));
    }
    
    @PostMapping
    public ResponseEntity<ServiceFeatureResponse> createFeature(@Valid @RequestBody ServiceFeatureRequest request) {
        ServiceFeatureResponse newFeature = featureService.createFeature(request);
        return new ResponseEntity<>(newFeature, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ServiceFeatureResponse> updateFeature(
            @PathVariable UUID id, 
            @Valid @RequestBody ServiceFeatureRequest request) {
        
        ServiceFeatureResponse updatedFeature = featureService.updateFeature(id, request);
        return ResponseEntity.ok(updatedFeature);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable UUID id) {
        featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }
}