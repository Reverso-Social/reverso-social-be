package com.reverso.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ResourceCreateDto {
    private String title;
    private String description;
    private String type;
    private String fileUrl;
    private String previewImageUrl;
    private Boolean isPublic;
    private UUID userId;
}