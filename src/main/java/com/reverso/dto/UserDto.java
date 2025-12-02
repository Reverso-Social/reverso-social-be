package com.reverso.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String companyName;
    private String role;
}