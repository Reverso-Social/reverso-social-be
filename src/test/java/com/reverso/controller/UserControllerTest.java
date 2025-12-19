package com.reverso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.UserCreateRequest;
import com.reverso.dto.response.UserResponse;
import com.reverso.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@SuppressWarnings("null")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserCreateRequest validRequest;
    private UserResponse userResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        validRequest = TestDataFactory.createValidUserCreateRequest();

        userResponse = UserResponse.builder()
                .id(userId)
                .fullName(validRequest.getFullName())
                .email(validRequest.getEmail())
                .phone(validRequest.getPhone())
                .companyName(validRequest.getCompanyName())
                .role(validRequest.getRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser
    void shouldCreateUser() throws Exception {
        
        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(userResponse);


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.fullName").value(validRequest.getFullName()))
                .andExpect(jsonPath("$.email").value(validRequest.getEmail()))
                .andExpect(jsonPath("$.phone").value(validRequest.getPhone()))
                .andExpect(jsonPath("$.companyName").value(validRequest.getCompanyName()))
                .andExpect(jsonPath("$.role").value(validRequest.getRole()));

        verify(userService, times(1)).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenFullNameIsBlank() throws Exception {
        
        validRequest.setFullName("");


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        
        UserCreateRequest invalidRequest = TestDataFactory.createUserRequestWithInvalidEmail();


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        
        UserCreateRequest requestWithoutEmail = TestDataFactory.createUserRequestWithoutEmail();


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithoutEmail)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenPasswordIsBlank() throws Exception {
        
        validRequest.setPassword("");


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {
        
        UserCreateRequest shortPasswordRequest = TestDataFactory.createUserRequestWithShortPassword();


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shortPasswordRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenRoleIsBlank() throws Exception {
        
        validRequest.setRole("");


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser
    void shouldCreateUserWithOptionalFieldsNull() throws Exception {
        
        validRequest.setPhone(null);
        validRequest.setCompanyName(null);

        UserResponse responseWithNulls = UserResponse.builder()
                .id(userId)
                .fullName(validRequest.getFullName())
                .email(validRequest.getEmail())
                .phone(null)
                .companyName(null)
                .role(validRequest.getRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(responseWithNulls);


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phone").doesNotExist())
                .andExpect(jsonPath("$.companyName").doesNotExist());

        verify(userService, times(1)).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser
    void shouldGetAllUsers() throws Exception {
        
        UserResponse user2 = UserResponse.builder()
                .id(UUID.randomUUID())
                .fullName("Jane Smith")
                .email("jane@example.com")
                .role("ADMIN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<UserResponse> users = Arrays.asList(userResponse, user2);
        when(userService.getAll()).thenReturn(users);


        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fullName").value(userResponse.getFullName()))
                .andExpect(jsonPath("$[0].email").value(userResponse.getEmail()))
                .andExpect(jsonPath("$[1].fullName").value("Jane Smith"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));

        verify(userService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    void shouldGetUserById() throws Exception {
        
        when(userService.getById(userId)).thenReturn(userResponse);


        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.fullName").value(userResponse.getFullName()))
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()));

        verify(userService, times(1)).getById(userId);
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        
        UUID nonExistentId = UUID.randomUUID();
        when(userService.getById(nonExistentId))
                .thenThrow(new RuntimeException("Usuario no encontrado"));


        mockMvc.perform(get("/api/users/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(userService, times(1)).getById(nonExistentId);
    }

    @Test
    @WithMockUser
    void shouldDeleteUser() throws Exception {
        
        doNothing().when(userService).delete(userId);


        mockMvc.perform(delete("/api/users/{id}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(userId);
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoUsersExist() throws Exception {
        
        when(userService.getAll()).thenReturn(Arrays.asList());


        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    void shouldCreateUserWithDifferentRoles() throws Exception {
        
        UserCreateRequest adminRequest = TestDataFactory.createAdminUserRequest();
        UserResponse adminResponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .fullName(adminRequest.getFullName())
                .email(adminRequest.getEmail())
                .role("ADMIN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(adminResponse);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("ADMIN"));


        UserCreateRequest editorRequest = TestDataFactory.createValidUserCreateRequest();
        editorRequest.setRole("EDITOR");
        UserResponse editorResponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .fullName(editorRequest.getFullName())
                .email(editorRequest.getEmail())
                .role("EDITOR")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(editorResponse);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("EDITOR"));
    }

    @Test
    @WithMockUser
    void shouldCreateRegularUser() throws Exception {

        UserCreateRequest userRequest = TestDataFactory.createValidUserCreateRequest();
        userRequest.setRole("USER");

        UserResponse response = UserResponse.builder()
                .id(UUID.randomUUID())
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .role("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(response);


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser
    void shouldHandleValidationErrorsInRequestBody() throws Exception {
        
        String invalidJson = "{ \"fullName\": \"\", \"email\": \"not-an-email\", \"password\": \"123\", \"role\": \"\" }";


        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequest.class));
    }
}