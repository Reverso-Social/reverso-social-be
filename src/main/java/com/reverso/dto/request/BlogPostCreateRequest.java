package com.reverso.dto.request;

import com.reverso.model.enums.BlogPostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostCreateRequest {
    @NotBlank(message = "Título es necesario")
    @Size(min = 5, max = 100, message = "Título debe tener entre 5 y 100 caracteres")
    private String title;
    @Size(max = 250, message = "Subtítulo debe tener menos de 250 caracteres")
    private String subtitle;
    @Size(min = 10, max = 5000, message = "Contenido debe tener entre 10 y 5000 caracteres")
    @NotBlank(message = "Contenido es necesario")
    private String content;
    @Size(max = 50, message = "Categoría debe tener menos de 50 caracteres")
    private String category;
    @NotNull(message = "Estado es necesario")
    private BlogPostStatus status;
}
