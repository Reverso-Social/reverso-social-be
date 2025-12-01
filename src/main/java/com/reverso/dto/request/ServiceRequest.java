package com.reverso.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequest {
    
    @NotNull(message = "Category ID is required")
    private UUID categoryId;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 200, message = "Name must be between 2 and 200 characters")
    private String name;
    
    @Size(max = 500, message = "Short description cannot exceed 500 characters")
    private String shortDescription;
    
    @Size(max = 5000, message = "Full description cannot exceed 5000 characters")
    private String fullDescription;
    
    @Size(max = 500, message = "Icon URL cannot exceed 500 characters")
    private String iconUrl;
    
    private Integer sortOrder;
    
    private Boolean active;
    
    private UUID createdByUserId;
}