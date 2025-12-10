package com.reverso.dto.request;

import com.reverso.model.enums.BlogPostStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostUpdateRequest {

    @Size(max = 120, message = "El título no puede superar 120 caracteres")
    private String title;

    private String content;

    private String category;

    private BlogPostStatus status;
}
