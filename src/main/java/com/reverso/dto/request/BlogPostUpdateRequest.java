package com.reverso.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostUpdateRequest {

    private String title;
    private String subtitle;
    private String content;
    private String category;
    private String status;
}
