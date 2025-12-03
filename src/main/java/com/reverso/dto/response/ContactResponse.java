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
public class ContactResponse {
    
    private UUID id;
    private String fullName;
    private String email;
    private String message;
    private Boolean acceptsPrivacy;
    private String status;
    private UUID userId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}