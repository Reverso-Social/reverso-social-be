package com.reverso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReversoSocialBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReversoSocialBeApplication.class, args);
        System.out.println("\n" +
            "╔═══════════════════════════════════════════════════════╗\n" +
            "║                                                       ║\n" +
            "║      Reverso Social Backend Running! 💜              ║\n" +
            "║                                                       ║\n" +
            "║  API:           http://localhost:8080/api             ║\n" +
            "║  H2 Console:    http://localhost:8080/h2-console      ║\n" +
            "║                                                       ║\n" +
            "║  Database Info (H2):                                  ║\n" +
            "║  JDBC URL:     jdbc:h2:mem:reversodb                  ║\n" +
            "║  Username:     sa                                     ║\n" +
            "║  Password:     (empty)                                ║\n" +
            "║                                                       ║\n" +
            "║  Endpoints disponibles:                               ║\n" +
            "║  - /api/users                                         ║\n" +
            "║  - /api/service-categories                            ║\n" +
            "║  - /api/services                                      ║\n" +
            "║  - /api/service-features                              ║\n" +
            "║  - /api/resources                                     ║\n" +
            "║  - /api/resource-downloads                            ║\n" +
            "║  - /api/contacts                                      ║\n" +
            "║                                                       ║\n" +
            "╚═══════════════════════════════════════════════════════╝\n"
        );
    }
}