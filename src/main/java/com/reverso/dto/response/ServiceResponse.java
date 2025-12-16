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
public class ServiceResponse {
    
    private UUID id;
    private UUID categoryId;
    private String categoryName;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String iconUrl;
    private Integer sortOrder;
    private Boolean active;
    private UUID createdByUserId;
    private String createdByUserName;
    private Integer featuresCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}