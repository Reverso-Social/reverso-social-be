package com.reverso.dto;

import lombok.Data;

@Data
public class ResourceDto {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String fileUrl;
    private String previewImageUrl;
    private Boolean isPublic;
    private Long userId;
}
