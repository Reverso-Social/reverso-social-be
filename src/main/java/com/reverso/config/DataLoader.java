package com.reverso.config;

import com.reverso.model.User;
import com.reverso.model.enums.Role;
import com.reverso.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DataLoader para inicializar usuarios por defecto en la base de datos.
 * Este componente se ejecuta automáticamente al iniciar la aplicación.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Inicializando usuarios por defecto...");
        
        createDefaultUsers();
        
        log.info("Usuarios por defecto inicializados correctamente");
    }

    /**
     * Crea los usuarios ADMIN y EDITOR si no existen en la base de datos.
     * Las contraseñas se hashean automáticamente con BCrypt.
     */
    private void createDefaultUsers() {
        if (!userRepository.existsByEmail("admin@reversosocial.com")) {
            User admin = User.builder()
                    .id(UUID.fromString("a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d"))
                    .fullName("Equipo Reverso Social")
                    .email("admin@reversosocial.com")
                    .password(passwordEncoder.encode("password123"))  // Se hashea automáticamente
                    .phone("+34 900 000 000")
                    .companyName("Reverso Social")
                    .role(Role.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            userRepository.save(admin);
            log.info("Usuario ADMIN creado: admin@reversosocial.com");
        } else {
            log.info("ℹUsuario ADMIN ya existe");
        }
        if (!userRepository.existsByEmail("editor@reversosocial.com")) {
            User editor = User.builder()
                    .id(UUID.fromString("b2c3d4e5-f6a7-4b8c-9d0e-1f2a3b4c5d6e"))
                    .fullName("Colaboradora Externa")
                    .email("editor@reversosocial.com")
                    .password(passwordEncoder.encode("password123"))  // Se hashea automáticamente
                    .phone("+34 900 000 001")
                    .companyName("Reverso Social")
                    .role(Role.EDITOR)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            userRepository.save(editor);
            log.info("Usuario EDITOR creado: editor@reversosocial.com");
        } else {
            log.info("ℹUsuario EDITOR ya existe");
        }
    }
}