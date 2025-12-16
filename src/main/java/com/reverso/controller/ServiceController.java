package com.reverso.controller;

import com.reverso.dto.request.ServiceRequest;
import com.reverso.dto.response.ServiceResponse;
import com.reverso.service.interfaces.ServiceService;

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
@RequestMapping("/api/services")
public class ServiceController {
    
    private final ServiceService serviceService;
    
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
    
    @GetMapping
    public ResponseEntity<Page<ServiceResponse>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "sortOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), sort);
        return ResponseEntity.ok(serviceService.getAllServices(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable UUID id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ServiceResponse>> getServicesByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), 
                                          Sort.by("sortOrder").ascending());
        return ResponseEntity.ok(serviceService.getServicesByCategory(categoryId, pageable));
    }
    
    @GetMapping("/category/{categoryId}/active")
    public ResponseEntity<List<ServiceResponse>> getActiveServicesByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(serviceService.getActiveServicesByCategory(categoryId));
    }
    
    @GetMapping("/filter")
    public ResponseEntity<Page<ServiceResponse>> filterServices(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), 
                                          Sort.by("sortOrder").ascending());
        
        return ResponseEntity.ok(serviceService.filterServices(categoryId, active, name, pageable));
    }
    
    @PostMapping
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody ServiceRequest request) {
        ServiceResponse newService = serviceService.createService(request);
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable UUID id, 
            @Valid @RequestBody ServiceRequest request) {
        
        ServiceResponse updatedService = serviceService.updateService(id, request);
        return ResponseEntity.ok(updatedService);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}