package com.reverso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceDownloadResponse {
    private UUID id;
    private UUID userId;
    private String userName;
    private UUID resourceId;
    private String resourceTitle;
    private LocalDateTime createdAt;
}