package com.reverso.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostCreateRequest {
    @NotBlank
    private String title;
    private String subtitle;
    @NotBlank
    private String content;
    private String category;
    @NotNull
    private String status;
}
