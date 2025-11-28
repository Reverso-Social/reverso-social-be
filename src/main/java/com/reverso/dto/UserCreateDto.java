package com.reverso.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    private String fullName;
    private String email;
    private String phone;
    private String companyName;
    private String password;
    private String role; 
}
