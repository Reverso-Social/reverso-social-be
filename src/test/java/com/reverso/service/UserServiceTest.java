package com.reverso.service;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.UserCreateRequest;
import com.reverso.dto.response.UserResponse;
import com.reverso.mapper.UserMapper;
import com.reverso.model.User;
import com.reverso.model.enums.Role;
import com.reverso.repository.UserRepository;
import com.reverso.security.UserDetailsImpl;
import com.reverso.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
@DisplayName("UserService - Tests Unitarios")
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User validUser;
    private UserCreateRequest validCreateRequest;
    private UserResponse validResponse;

    @BeforeEach
    void setUp() {
        validUser = TestDataFactory.createValidUser();
        validCreateRequest = TestDataFactory.createValidUserCreateRequest();
        
        validResponse = UserResponse.builder()
                .id(validUser.getId())
                .fullName(validUser.getFullName())
                .email(validUser.getEmail())
                .phone(validUser.getPhone())
                .companyName(validUser.getCompanyName())
                .role(validUser.getRole().name())
                .createdAt(validUser.getCreatedAt())
                .updatedAt(validUser.getUpdatedAt())
                .build();
    }


    @Test
    @DisplayName("Cargar usuario por email existente - Success")
    void testLoadUserByUsername_ExistingEmail_ReturnsUserDetails() {
        
        String email = "admin@reverso.com";
        when(repository.findByEmail(email)).thenReturn(Optional.of(validUser));

        
        UserDetails userDetails = userService.loadUserByUsername(email);

        
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(UserDetailsImpl.class);
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(validUser.getPassword());
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_ADMIN");
        
        verify(repository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Cargar usuario por email inexistente - Throws UsernameNotFoundException")
    void testLoadUserByUsername_NonExistingEmail_ThrowsException() {
        
        String nonExistingEmail = "notfound@example.com";
        when(repository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        
        assertThatThrownBy(() -> userService.loadUserByUsername(nonExistingEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado con email");
        
        verify(repository, times(1)).findByEmail(nonExistingEmail);
    }

    @Test
    @DisplayName("Cargar usuario con rol USER - Retorna authority correcta")
    void testLoadUserByUsername_UserRole_ReturnsCorrectAuthority() {
        
        User userWithUserRole = TestDataFactory.createUserWithRole(Role.USER);
        when(repository.findByEmail(userWithUserRole.getEmail()))
                .thenReturn(Optional.of(userWithUserRole));

       
        UserDetails userDetails = userService.loadUserByUsername(userWithUserRole.getEmail());

        
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Cargar usuario con rol EDITOR - Retorna authority correcta")
    void testLoadUserByUsername_EditorRole_ReturnsCorrectAuthority() {
        
        User editorUser = TestDataFactory.createUserWithRole(Role.EDITOR);
        when(repository.findByEmail(editorUser.getEmail()))
                .thenReturn(Optional.of(editorUser));

        
        UserDetails userDetails = userService.loadUserByUsername(editorUser.getEmail());

        
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_EDITOR");
    }


    @Test
    @DisplayName("Crear usuario - Contraseña es encriptada")
    void testCreateUser_PasswordIsEncoded() {
        
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$encodedPasswordHash";
        
        validCreateRequest.setPassword(rawPassword);
        validUser.setPassword(null); 
        
        when(mapper.toEntity(any(UserCreateRequest.class))).thenReturn(validUser);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertThat(user.getPassword()).isEqualTo(encodedPassword);
            return user;
        });
        when(mapper.toResponse(any(User.class))).thenReturn(validResponse);

        
        userService.createUser(validCreateRequest);

        
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    @DisplayName("Crear usuario con rol ADMIN - Success")
    void testCreateUser_AdminRole_CreatesAdmin() {
        
        UserCreateRequest adminRequest = TestDataFactory.createAdminUserRequest();
        User adminUser = TestDataFactory.createUserWithRole(Role.ADMIN);
        UserResponse adminResponse = UserResponse.builder()
                .id(adminUser.getId())
                .email(adminUser.getEmail())
                .role("ADMIN")
                .build();
        
        when(mapper.toEntity(any(UserCreateRequest.class))).thenReturn(adminUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(repository.save(any(User.class))).thenReturn(adminUser);
        when(mapper.toResponse(any(User.class))).thenReturn(adminResponse);

    
        UserResponse result = userService.createUser(adminRequest);

        
        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Crear usuario con datos completos - Success")
    void testCreateUser_WithAllFields_Success() {
        
        UserCreateRequest fullRequest = UserCreateRequest.builder()
                .fullName("Complete User")
                .email("complete@example.com")
                .phone("+34666777888")
                .companyName("Complete Company")
                .password("password123")
                .role("USER")
                .build();
        
        when(mapper.toEntity(any(UserCreateRequest.class))).thenReturn(validUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(repository.save(any(User.class))).thenReturn(validUser);
        when(mapper.toResponse(any(User.class))).thenReturn(validResponse);

        
        UserResponse result = userService.createUser(fullRequest);

        
        assertThat(result).isNotNull();
        verify(repository, times(1)).save(any(User.class));
    }

   

    @Test
    @DisplayName("Obtener todos los usuarios - Success")
    void testGetAll_ReturnsListOfUsers() {
        
        User user1 = TestDataFactory.createValidUser();
        User user2 = TestDataFactory.createUserWithRole(Role.USER);
        List<User> users = Arrays.asList(user1, user2);

        UserResponse response1 = UserResponse.builder()
                .id(user1.getId())
                .email(user1.getEmail())
                .role("ADMIN")
                .build();
        UserResponse response2 = UserResponse.builder()
                .id(user2.getId())
                .email(user2.getEmail())
                .role("USER")
                .build();

        when(repository.findAll()).thenReturn(users);
        when(mapper.toResponse(user1)).thenReturn(response1);
        when(mapper.toResponse(user2)).thenReturn(response2);

        
        List<UserResponse> result = userService.getAll();

        
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(response1, response2);
        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Obtener todos los usuarios - Lista vacía")
    void testGetAll_EmptyList_ReturnsEmptyList() {
        
        when(repository.findAll()).thenReturn(List.of());

        
        List<UserResponse> result = userService.getAll();

        
        assertThat(result).isEmpty();
        verify(repository, times(1)).findAll();
    }


    @Test
    @DisplayName("Obtener usuario por ID existente - Success")
    void testGetById_ExistingId_ReturnsUser() {
        
        UUID id = validUser.getId();
        when(repository.findById(id)).thenReturn(Optional.of(validUser));
        when(mapper.toResponse(validUser)).thenReturn(validResponse);

        
        UserResponse result = userService.getById(id);

        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo(validUser.getEmail());
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).toResponse(validUser);
    }

    @Test
    @DisplayName("Obtener usuario por ID inexistente - Throws RuntimeException")
    void testGetById_NonExistingId_ThrowsException() {
        
        UUID nonExistingId = UUID.randomUUID();
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        
        assertThatThrownBy(() -> userService.getById(nonExistingId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
        
        verify(repository, times(1)).findById(nonExistingId);
        verify(mapper, never()).toResponse(any());
    }


    @Test
    @DisplayName("Eliminar usuario por ID - Success")
    void testDelete_ValidId_DeletesUser() {
        
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(id);

        
        userService.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar usuario - Llama al repositorio")
    void testDelete_CallsRepository() {
        
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(id);


        userService.delete(id);


        verify(repository, times(1)).deleteById(id);
    }


    @Test
    @DisplayName("Verificar que passwordEncoder se llama con contraseña correcta")
    void testCreateUser_CallsPasswordEncoderWithCorrectPassword() {
        
        String rawPassword = "mySecurePassword123";
        validCreateRequest.setPassword(rawPassword);
        
        when(mapper.toEntity(any(UserCreateRequest.class))).thenReturn(validUser);
        when(passwordEncoder.encode(rawPassword)).thenReturn("$2a$10$encoded");
        when(repository.save(any(User.class))).thenReturn(validUser);
        when(mapper.toResponse(any(User.class))).thenReturn(validResponse);


        userService.createUser(validCreateRequest);

        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    @DisplayName("Usuario guardado tiene contraseña encriptada, no plana")
    void testCreateUser_SavedUserHasEncodedPassword() {
        
        String rawPassword = "plainPassword";
        String encodedPassword = "$2a$10$hashedPassword";
        
        validCreateRequest.setPassword(rawPassword);
        
        when(mapper.toEntity(any(UserCreateRequest.class))).thenReturn(validUser);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);

            assertThat(savedUser.getPassword()).isNotEqualTo(rawPassword);
            assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
            return savedUser;
        });
        when(mapper.toResponse(any(User.class))).thenReturn(validResponse);


        userService.createUser(validCreateRequest);

        verify(repository, times(1)).save(any(User.class));
    }
}
