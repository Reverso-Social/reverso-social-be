package com.reverso.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ContactDto {
    private UUID id;
    private String fullName;
    private String email;
    private String message;
    private Boolean acceptsPrivacy;
    private String status;
    private UUID userId;
}