package com.reverso.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResourceUpdateRequest {

    @Size(max = 60, message = "El título no puede superar 60 caracteres")
    private String title;

    @Size(max = 250, message = "La descripción no puede superar 250 caracteres")
    private String description;

    private String type;

    private String fileUrl;

    private String previewImageUrl;

    private Boolean isPublic;
}
