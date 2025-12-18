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
public class DownloadLeadResponse {
    private UUID id;
    private String name;
    private String email;
    private UUID resourceId;
    private String resourceTitle;
    private LocalDateTime createdAt;
    private LocalDateTime lastDownloadedAt;
    private int downloadCount;
}