package com.reverso.controller;

import com.reverso.dto.request.ServiceCategoryRequest;
import com.reverso.dto.response.ServiceCategoryResponse;
import com.reverso.service.Interfaces.ServiceCategoryService;
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
@RequestMapping("/api/service-categories")
public class ServiceCategoryController {
    
    private final ServiceCategoryService categoryService;
    
    public ServiceCategoryController(ServiceCategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @GetMapping
    public ResponseEntity<Page<ServiceCategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "sortOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), sort);
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ServiceCategoryResponse>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategoryResponse> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ServiceCategoryResponse>> searchCategories(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50));
        return ResponseEntity.ok(categoryService.searchCategories(name, pageable));
    }
    
    @PostMapping
    public ResponseEntity<ServiceCategoryResponse> createCategory(@Valid @RequestBody ServiceCategoryRequest request) {
        ServiceCategoryResponse newCategory = categoryService.createCategory(request);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategoryResponse> updateCategory(
            @PathVariable UUID id, 
            @Valid @RequestBody ServiceCategoryRequest request) {
        
        ServiceCategoryResponse updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}