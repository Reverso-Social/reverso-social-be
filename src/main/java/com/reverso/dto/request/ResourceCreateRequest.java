package com.reverso.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceCreateRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Type is required")
    private String type;
    
    @NotBlank(message = "File URL is required")
    private String fileUrl;
    
    private String previewImageUrl;
    
    @NotNull(message = "Public visibility is required")
    private Boolean isPublic;
    
    private UUID userId;
}