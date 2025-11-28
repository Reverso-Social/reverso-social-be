package com.reverso.dto;

import lombok.Data;

@Data
public class ContactDto {

    private Long id;
    private String fullName;
    private String email;
    private String message;
    private Boolean acceptsPrivacy;
    private String status;
    private Long userId;
}
