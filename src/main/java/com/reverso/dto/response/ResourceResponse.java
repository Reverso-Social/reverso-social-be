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
public class ResourceResponse {
    
    private UUID id;
    private String title;
    private String description;
    private String type;
    private String fileUrl;
    private String previewImageUrl;
    private Boolean isPublic;
    private UUID userId;
    private String userName;
    private Long downloadCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}