package com.reverso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReversoSocialBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReversoSocialBeApplication.class, args);
        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════════╗\n" +
            "║                                                                  ║\n" +
            "║              REVERSO SOCIAL - BACKEND API                        ║\n" +
            "║              Consultoria Feminista Sociopolitica                 ║\n" +
            "║                                                                  ║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║                                                                  ║\n" +
            "║  API Base:           http://localhost:8080/api                   ║\n" +
            "║  Auth Login:         http://localhost:8080/api/auth/login        ║\n" +
            "║  Database:           PostgreSQL (reversodb)                      ║\n" +
            "║  Security:           JWT Authentication Enabled                  ║\n" +
            "║                                                                  ║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║  ENDPOINTS PRINCIPALES:                                          ║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║                                                                  ║\n" +
            "║  Usuarios:           /api/users                                  ║\n" +
            "║  Categorias:         /api/service-categories                     ║\n" +
            "║  Servicios:          /api/services                               ║\n" +
            "║  Caracteristicas:    /api/service-features                       ║\n" +
            "║  Recursos:           /api/resources                              ║\n" +
            "║  Descargas:          /api/resource-downloads                     ║\n" +
            "║  Contactos:          /api/contacts                               ║\n" +
            "║                                                                  ║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║  CUENTAS DE ACCESO:                                              ║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║                                                                  ║\n" +
            "║  ADMIN (Equipo):  admin@reversosocial.com / password123          ║\n" +
            "║  EDITOR:          editor@reversosocial.com / password123         ║\n" +
            "║                                                                  ║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║                                                                  ║\n" +
            "║  Desarrollado por el equipo de Reverso Social                    ║\n" +
            "║  Version 1.0.0 - Sistema de Seguridad JWT                        ║\n" +
            "║                                                                  ║\n" +
            "╚══════════════════════════════════════════════════════════════════╝\n"
        );
    }
}