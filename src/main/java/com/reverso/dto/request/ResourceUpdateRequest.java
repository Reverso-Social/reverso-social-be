package com.reverso.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceUpdateRequest {
    
    private String title;
    private String description;
    private String type;
    private String fileUrl;
    private String previewImageUrl;
    private Boolean isPublic;
}