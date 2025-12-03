package com.reverso;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String hashedPassword = encoder.encode(rawPassword);
        
        System.out.println("=================================");
        System.out.println("Password: " + rawPassword);
        System.out.println("Hash BCrypt:");
        System.out.println(hashedPassword);
        System.out.println("=================================");
        System.out.println("\nSQL para actualizar:");
        System.out.println("UPDATE users SET password = '" + hashedPassword + "' WHERE email = 'admin@reversosocial.com';");
        System.out.println("UPDATE users SET password = '" + hashedPassword + "' WHERE email = 'editor@reversosocial.com';");
    }
}