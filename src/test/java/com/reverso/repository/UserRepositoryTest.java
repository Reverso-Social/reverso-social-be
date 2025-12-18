package com.reverso.repository;

import com.reverso.config.TestDataFactory;
import com.reverso.model.User;
import com.reverso.model.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de integración para UserRepository
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("UserRepository - Tests de Integración")
@SuppressWarnings("null")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Guardar usuario - Success")
    void testSave_NewUser_ReturnsSavedUser() {
        // Arrange
        User user = TestDataFactory.createValidUser();
        user.setId(null);
        user.setEmail("new-user-" + System.currentTimeMillis() + "@example.com");

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFullName()).isEqualTo(user.getFullName());
        assertThat(saved.getEmail()).isEqualTo(user.getEmail());
        assertThat(saved.getRole()).isEqualTo(user.getRole());
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Buscar por ID existente - Returns User")
    void testFindById_ExistingId_ReturnsUser() {
        // Arrange
        User user = TestDataFactory.createValidUser();
        user.setId(null);
        user.setEmail("findbyid-" + System.currentTimeMillis() + "@example.com");
        User saved = userRepository.save(user);

        // Act
        Optional<User> found = userRepository.findById(saved.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getEmail()).isEqualTo(saved.getEmail());
    }

    @Test
    @DisplayName("Buscar por ID inexistente - Returns Empty")
    void testFindById_NonExistingId_ReturnsEmpty() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Act
        Optional<User> found = userRepository.findById(nonExistingId);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Buscar por email existente - Returns User")
    void testFindByEmail_ExistingEmail_ReturnsUser() {
        // Arrange
        User user = TestDataFactory.createValidUser();
        user.setId(null);
        String uniqueEmail = "findbyemail-" + System.currentTimeMillis() + "@example.com";
        user.setEmail(uniqueEmail);
        userRepository.save(user);

        // Act
        Optional<User> found = userRepository.findByEmail(uniqueEmail);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(uniqueEmail);
        assertThat(found.get().getFullName()).isEqualTo(user.getFullName());
    }

    @Test
    @DisplayName("Buscar por email inexistente - Returns Empty")
    void testFindByEmail_NonExistingEmail_ReturnsEmpty() {
        // Arrange
        String nonExistingEmail = "nonexistent-" + System.currentTimeMillis() + "@example.com";

        // Act
        Optional<User> found = userRepository.findByEmail(nonExistingEmail);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Buscar por email - Case sensitive")
    void testFindByEmail_CaseSensitive() {
        // Arrange
        User user = TestDataFactory.createValidUser();
        user.setId(null);
        String email = "testcase-" + System.currentTimeMillis() + "@example.com";
        user.setEmail(email.toLowerCase());
        userRepository.save(user);

        // Act - Buscar con mayúsculas
        Optional<User> found = userRepository.findByEmail(email.toUpperCase());

        // Assert - No debería encontrarlo (email es case sensitive en PostgreSQL por defecto)
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Verificar si existe por email - Returns true cuando existe")
    void testExistsByEmail_ExistingEmail_ReturnsTrue() {
        // Arrange
        User user = TestDataFactory.createValidUser();
        user.setId(null);
        String uniqueEmail = "exists-" + System.currentTimeMillis() + "@example.com";
        user.setEmail(uniqueEmail);
        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByEmail(uniqueEmail);

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Verificar si existe por email - Returns false cuando no existe")
    void testExistsByEmail_NonExistingEmail_ReturnsFalse() {
        // Arrange
        String nonExistingEmail = "notexists-" + System.currentTimeMillis() + "@example.com";

        // Act
        boolean exists = userRepository.existsByEmail(nonExistingEmail);

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Guardar usuario con rol USER - Success")
    void testSave_WithUserRole_Success() {
        // Arrange
        User user = TestDataFactory.createUserWithRole(Role.USER);
        user.setId(null);
        user.setEmail("user-role-" + System.currentTimeMillis() + "@example.com");

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Guardar usuario con rol ADMIN - Success")
    void testSave_WithAdminRole_Success() {
        // Arrange
        User user = TestDataFactory.createUserWithRole(Role.ADMIN);
        user.setId(null);
        user.setEmail("admin-role-" + System.currentTimeMillis() + "@example.com");

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("Guardar usuario con rol EDITOR - Success")
    void testSave_WithEditorRole_Success() {
        // Arrange
        User user = TestDataFactory.createUserWithRole(Role.EDITOR);
        user.setId(null);
        user.setEmail("editor-role-" + System.currentTimeMillis() + "@example.com");

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getRole()).isEqualTo(Role.EDITOR);
    }

    @Test
    @DisplayName("Buscar todos los usuarios - Returns List")
    void testFindAll_ReturnsAllUsers() {
        // Arrange
        long initialCount = userRepository.count();
        
        User user1 = TestDataFactory.createValidUser();
        user1.setId(null);
        user1.setEmail("all-user-1-" + System.currentTimeMillis() + "@example.com");
        
        User user2 = TestDataFactory.createValidUser();
        user2.setId(null);
        user2.setEmail("all-user-2-" + System.currentTimeMillis() + "@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertThat(users).hasSizeGreaterThanOrEqualTo((int) initialCount + 2);
    }

    @Test
    @DisplayName("Actualizar usuario existente - Success")
    void testUpdate_ExistingUser_ReturnsUpdatedUser() {
        // Arrange
        User user = TestDataFactory.createValidUser();
        user.setId(null);
        user.setEmail("update-user-" + System.currentTimeMillis() + "@example.com");
        User saved = userRepository.save(user);

        // Act
        saved.setFullName("Updated Name");
        saved.setPhone("+34999888777");
        saved.setCompanyName("Updated Company");
        User updated = userRepository.save(saved);

        // Assert
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getFullName()).isEqualTo("Updated Name");
        assertThat(updated.getPhone()).isEqualTo("+34999888777");
        assertThat(updated.getCompanyName()).isEqualTo("Updated Company");
    }

    @Test
    @DisplayName("Actualizar rol de usuario - Success")
    void testUpdate_ChangeRole_Success() {
        // Arrange
        User user = TestDataFactory.createUserWithRole(Role.USER);
        user.setId(null);
        user.setEmail("changerole-" + System.currentTimeMillis() + "@example.com");
        User saved = userRepository.save(user);

        // Act
        saved.setRole(Role.ADMIN);
        User updated = userRepository.save(saved);

        // Assert
        assertThat(updated.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("Eliminar usuario por ID - Success")
    void testDeleteById_ExistingId_DeletesUser() {
        // Arrange
        User user = TestDataFactory.createValidUser();
        user.setId(null);
        user.setEmail("delete-user-" + System.currentTimeMillis() + "@example.com");
        User saved = userRepository.save(user);
        UUID id = saved.getId();

        // Act
        userRepository.deleteById(id);

        // Assert
        Optional<User> found = userRepository.findById(id);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Verificar @PrePersist - Establece fechas automáticamente")
    void testPrePersist_SetsTimestamps() {
        // Arrange
        User user = User.builder()
                .fullName("Test User")
                .email("prepersist-" + System.currentTimeMillis() + "@example.com")
                .password("hashedPassword")
                .role(Role.USER)
                .build();

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }


    @Test
    @DisplayName("Guardar usuario con todos los campos - Success")
    void testSave_WithAllFields_Success() {
        // Arrange
        User user = User.builder()
                .fullName("Complete User")
                .email("complete-" + System.currentTimeMillis() + "@example.com")
                .phone("+34666777888")
                .companyName("Complete Company SL")
                .password("$2a$10$hashedPassword")
                .message("User registration message")
                .role(Role.USER)
                .build();

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFullName()).isEqualTo("Complete User");
        assertThat(saved.getEmail()).contains("complete-");
        assertThat(saved.getPhone()).isEqualTo("+34666777888");
        assertThat(saved.getCompanyName()).isEqualTo("Complete Company SL");
        assertThat(saved.getMessage()).isEqualTo("User registration message");
        assertThat(saved.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Guardar usuario con campos opcionales null - Success")
    void testSave_WithNullOptionalFields_Success() {
        // Arrange
        User user = User.builder()
                .fullName("Minimal User")
                .email("minimal-" + System.currentTimeMillis() + "@example.com")
                .password("$2a$10$hashedPassword")
                .role(Role.USER)
                .phone(null)
                .companyName(null)
                .message(null)
                .build();

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPhone()).isNull();
        assertThat(saved.getCompanyName()).isNull();
        assertThat(saved.getMessage()).isNull();
    }

    @Test
    @DisplayName("Contar usuarios - Returns correct count")
    void testCount_ReturnsCorrectCount() {
        // Arrange
        long initialCount = userRepository.count();
        
        User user1 = TestDataFactory.createValidUser();
        user1.setId(null);
        user1.setEmail("count-1-" + System.currentTimeMillis() + "@example.com");
        
        User user2 = TestDataFactory.createValidUser();
        user2.setId(null);
        user2.setEmail("count-2-" + System.currentTimeMillis() + "@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        long count = userRepository.count();

        // Assert
        assertThat(count).isEqualTo(initialCount + 2);
    }

    @Test
    @DisplayName("Verificar existencia por ID - Returns true")
    void testExistsById_ExistingId_ReturnsTrue() {
        // Arrange
        User user = TestDataFactory.createValidUser();
        user.setId(null);
        user.setEmail("existsbyid-" + System.currentTimeMillis() + "@example.com");
        User saved = userRepository.save(user);

        // Act
        boolean exists = userRepository.existsById(saved.getId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Verificar existencia por ID inexistente - Returns false")
    void testExistsById_NonExistingId_ReturnsFalse() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Act
        boolean exists = userRepository.existsById(nonExistingId);

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Email único - No permite duplicados")
    void testSave_DuplicateEmail_ThrowsException() {
        // Arrange
        String duplicateEmail = "duplicate-" + System.currentTimeMillis() + "@example.com";
        
        User user1 = TestDataFactory.createValidUser();
        user1.setId(null);
        user1.setEmail(duplicateEmail);
        userRepository.save(user1);

        User user2 = TestDataFactory.createValidUser();
        user2.setId(null);
        user2.setEmail(duplicateEmail);

        // Act & Assert
        assertThatThrownBy(() -> {
            userRepository.save(user2);
            userRepository.flush(); // Forzar la escritura a BD
        }).isInstanceOf(Exception.class);
    }
}