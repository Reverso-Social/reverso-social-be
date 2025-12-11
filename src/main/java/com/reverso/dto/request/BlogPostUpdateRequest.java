package com.reverso.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.reverso.model.enums.BlogPostStatus;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostUpdateRequest {

    @Size(min = 5, max = 100, message = "Título debe tener entre 5 y 100 caracteres")
    private String title;
    @Size(min = 5, max = 250, message = "Subtítulo debe tener entre 5 y 250 caracteres")
    private String subtitle;
    @Size(min = 5, max = 5000, message = "Contenido debe tener entre 5 y 5000 caracteres")
    private String content;
    @Size(max = 50, message = "Categoría debe tener menos de 50 caracteres")    
    private String category;

    private BlogPostStatus status;
    @Size(max = 20, message = "Estado debe tener menos de 20 caracteres")
    private BlogPostStatus status;
}

