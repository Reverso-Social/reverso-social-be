package com.reverso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceCategoryResponse {
    
    private UUID id;
    private String name;
    private String description;
    private String iconUrl;
    private Integer sortOrder;
    private Boolean active;
    private Integer servicesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}