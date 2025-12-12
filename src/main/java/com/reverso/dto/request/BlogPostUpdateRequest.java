package com.reverso.dto.request;

import com.reverso.model.enums.BlogPostStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostUpdateRequest {


    @Size(max = 100, message = "Título no puede superar 100 caracteres")
    private String title;

    @Size(max = 250, message = "Subtítulo no puede superar 250 caracteres")
    private String subtitle;

    @Size(max = 5000, message = "Contenido no puede superar 5000 caracteres")
    private String content;

    @Size(max = 50, message = "Categoría debe tener menos de 50 caracteres")
    private String category;
    private BlogPostStatus status;
}
