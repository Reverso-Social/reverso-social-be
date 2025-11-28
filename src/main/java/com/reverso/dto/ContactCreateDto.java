package com.reverso.dto;

import lombok.Data;

@Data
public class ContactCreateDto {

    private String fullName;
    private String email;
    private String message;
    private Boolean acceptsPrivacy;
}
