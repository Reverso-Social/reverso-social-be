package com.reverso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.ResourceCreateRequest;
import com.reverso.dto.request.ResourceUpdateRequest;
import com.reverso.dto.response.ResourceResponse;
import com.reverso.service.interfaces.FileStorageService;
import com.reverso.service.interfaces.ResourceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de integración para ResourceController
 * Usa @WebMvcTest para cargar solo el controller
 * @Import para cargar TestSecurityConfig que deshabilita Security
 */
@WebMvcTest(ResourceController.class)
@Import(com.reverso.config.TestSecurityConfig.class)
@ActiveProfiles("test")
@DisplayName("ResourceController - Tests de Integración")
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    @DisplayName("POST /api/resources - Crear recurso - Returns 201")
    void testCreate_ValidData_Returns201() throws Exception {
        // Arrange
        ResourceCreateRequest request = TestDataFactory.createValidResourceCreateRequest();
        ResourceResponse response = ResourceResponse.builder()
                .id(UUID.randomUUID())
                .title(request.getTitle())
                .description(request.getDescription())
                .type("GUIDE")
                .fileUrl(request.getFileUrl())
                .isPublic(true)
                .downloadCount(0L)
                .build();

        when(resourceService.create(any(ResourceCreateRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.type").value("GUIDE"));

        verify(resourceService, times(1)).create(any(ResourceCreateRequest.class));
    }

    @Test
    @DisplayName("POST /api/resources - Sin título - Returns 400")
    void testCreate_WithoutTitle_Returns400() throws Exception {
        // Arrange
        ResourceCreateRequest request = TestDataFactory.createResourceRequestWithoutTitle();

        // Act & Assert
        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(resourceService, never()).create(any());
    }

    @Test
    @DisplayName("GET /api/resources - Obtener todos - Returns 200")
    void testGetAll_ReturnsAllResources() throws Exception {
        // Arrange
        List<ResourceResponse> resources = Arrays.asList(
                ResourceResponse.builder()
                        .id(UUID.randomUUID())
                        .title("Resource 1")
                        .type("GUIDE")
                        .build(),
                ResourceResponse.builder()
                        .id(UUID.randomUUID())
                        .title("Resource 2")
                        .type("REPORT")
                        .build()
        );

        when(resourceService.getAll()).thenReturn(resources);

        // Act & Assert
        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Resource 1"))
                .andExpect(jsonPath("$[1].title").value("Resource 2"));

        verify(resourceService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /api/resources/public - Obtener públicos - Returns 200")
    void testGetPublic_ReturnsPublicResources() throws Exception {
        // Arrange
        List<ResourceResponse> resources = Arrays.asList(
                ResourceResponse.builder()
                        .id(UUID.randomUUID())
                        .title("Public Resource")
                        .isPublic(true)
                        .build()
        );

        when(resourceService.getPublic()).thenReturn(resources);

        // Act & Assert
        mockMvc.perform(get("/api/resources/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isPublic").value(true));

        verify(resourceService, times(1)).getPublic();
    }

    @Test
    @DisplayName("GET /api/resources/type/{type} - Filtrar por tipo - Returns 200")
    void testGetByType_ReturnsFilteredResources() throws Exception {
        // Arrange
        List<ResourceResponse> resources = Arrays.asList(
                ResourceResponse.builder()
                        .id(UUID.randomUUID())
                        .title("Guide Resource")
                        .type("GUIDE")
                        .build()
        );

        when(resourceService.getByType("GUIDE")).thenReturn(resources);

        // Act & Assert
        mockMvc.perform(get("/api/resources/type/{type}", "GUIDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("GUIDE"));

        verify(resourceService, times(1)).getByType("GUIDE");
    }

    @Test
    @DisplayName("GET /api/resources/{id} - ID existente - Returns 200")
    void testGet_ExistingId_Returns200() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        ResourceResponse response = ResourceResponse.builder()
                .id(id)
                .title("Test Resource")
                .type("GUIDE")
                .build();

        when(resourceService.getById(id)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/resources/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Test Resource"));

        verify(resourceService, times(1)).getById(id);
    }

    @Test
    @DisplayName("GET /api/resources/{id} - ID inexistente - Returns 500")
    void testGet_NonExistingId_Returns500() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        when(resourceService.getById(id))
                .thenThrow(new RuntimeException("Recurso no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/resources/{id}", id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("PATCH /api/resources/{id} - Actualizar recurso - Returns 200")
    void testUpdate_ValidData_Returns200() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        ResourceUpdateRequest updateRequest = new ResourceUpdateRequest();
        updateRequest.setTitle("Updated Title");
        
        ResourceResponse response = ResourceResponse.builder()
                .id(id)
                .title("Updated Title")
                .build();

        when(resourceService.update(eq(id), any(ResourceUpdateRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/api/resources/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(resourceService, times(1)).update(eq(id), any(ResourceUpdateRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/resources/{id} - Returns 204")
    void testDelete_ValidId_Returns204() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(resourceService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api/resources/{id}", id))
                .andExpect(status().isNoContent());

        verify(resourceService, times(1)).delete(id);
    }

    @Test
    @DisplayName("GET /api/resources - Lista vacía - Returns 200")
    void testGetAll_EmptyList_Returns200() throws Exception {
        // Arrange
        when(resourceService.getAll()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}