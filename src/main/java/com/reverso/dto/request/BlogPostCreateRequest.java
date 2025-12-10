package com.reverso.dto.request;

import com.reverso.model.enums.BlogPostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostCreateRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 120, message = "El título no puede superar 120 caracteres")
    private String title;

    @NotBlank(message = "El contenido es obligatorio")
    private String content;

    @NotBlank(message = "La categoría es obligatoria")
    private String category;

    @NotNull(message = "El estado es obligatorio")
    private BlogPostStatus status;
}
