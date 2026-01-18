package com.reverso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReversoSocialBeApplication {

    public static void main(String[] args) {
        org.springframework.context.ConfigurableApplicationContext context = SpringApplication
                .run(ReversoSocialBeApplication.class, args);

        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        boolean isDev = java.util.Arrays.asList(activeProfiles).contains("dev");

        if (isDev) {
            System.out.println("\n" +
                    "╔══════════════════════════════════════════════════════════════════╗\n" +
                    "║              REVERSO SOCIAL - BACKEND API (MODO DEV)             ║\n" +
                    "╠══════════════════════════════════════════════════════════════════╣\n" +
                    "║  API Base:           http://localhost:8080/api                   ║\n" +
                    "║  Database:           PostgreSQL / Cloudinary (Conectado)         ║\n" +
                    "╠══════════════════════════════════════════════════════════════════╣\n" +
                    "║  ⚠️  ATENCIÓN: Estás en modo DESARROLLO.                         ║\n" +
                    "║      No subas credenciales al repositorio.                       ║\n" +
                    "╚══════════════════════════════════════════════════════════════════╝\n");
        }
    }
}
