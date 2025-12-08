package com.reverso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReversoSocialBeApplication {

    public static void main(String[] args) {
        System.out.println("\n" +
        "╔══════════════════════════════════════════════════════════════════╗\n" +
        "║                     REVERSO SOCIAL - BACKEND API                 ║\n" +
        "║                Consultoría Feminista Sociopolítica               ║\n" +
        "╠══════════════════════════════════════════════════════════════════╣\n" +
        "║  API Base:           http://localhost:8080/api                   ║\n" +
        "║  Auth Login:         http://localhost:8080/api/auth/login        ║\n" +
        "║  Database:           PostgreSQL (reversodb)                      ║\n" +
        "║  Seguridad:          JWT Authentication + Roles (ADMIN/EDITOR)   ║\n" +
        "╠══════════════════════════════════════════════════════════════════╣\n" +
        "║                       ENDPOINTS PRINCIPALES                      ║\n" +
        "╠══════════════════════════════════════════════════════════════════╣\n" +
        "║  Usuarios:            /api/users               (ADMIN)           ║\n" +
        "║  Categorías:          /api/service-categories  (GET público)     ║\n" +
        "║  Servicios:           /api/services            (GET público)     ║\n" +
        "║  Características:     /api/service-features    (GET público)     ║\n" +
        "║  Recursos:            /api/resources           (protegido)       ║\n" +
        "║  Descargas:           /api/resource-downloads  (protegido)       ║\n" +
        "║  Contactos:           /api/contacts            (POST público)    ║\n" +
        "║  Blog:                /api/blog-posts          (GET público)     ║\n" +
        "╠══════════════════════════════════════════════════════════════════╣\n" +
        "║                      CUENTAS DE ACCESO (DEV)                     ║\n" +
        "╠══════════════════════════════════════════════════════════════════╣\n" +
        "║  ADMIN:   admin@reversosocial.com / password123                  ║\n" +
        "║  EDITOR:  editor@reversosocial.com / password123                 ║\n" +
        "╚══════════════════════════════════════════════════════════════════╝\n"
        );
    }
}