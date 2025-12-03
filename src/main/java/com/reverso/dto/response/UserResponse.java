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
public class UserResponse {
    
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String companyName;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}