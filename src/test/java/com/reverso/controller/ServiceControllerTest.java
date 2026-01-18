package com.reverso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reverso.dto.request.ServiceRequest;
import com.reverso.dto.response.ServiceResponse;
import com.reverso.service.interfaces.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("null")
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceService serviceService;

    private ServiceRequest validRequest;
    private ServiceResponse validResponse;
    private UUID serviceId;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        serviceId = UUID.randomUUID();
        categoryId = UUID.randomUUID();

        validRequest = ServiceRequest.builder()
                .categoryId(categoryId)
                .name("Test Service")
                .shortDescription("Short description")
                .fullDescription("Full description")
                .iconUrl("http://example.com/icon.png")
                .sortOrder(1)
                .active(true)
                .createdByUserId(UUID.randomUUID())
                .build();

        validResponse = ServiceResponse.builder()
                .id(serviceId)
                .categoryId(categoryId)
                .categoryName("Category Name")
                .name("Test Service")
                .shortDescription("Short description")
                .fullDescription("Full description")
                .iconUrl("http://example.com/icon.png")
                .sortOrder(1)
                .active(true)
                .createdByUserId(validRequest.getCreatedByUserId())
                .createdByUserName("Admin User")
                .featuresCount(0)
                .build();
    }

    @Test
    void testGetAllServices() throws Exception {
        Page<ServiceResponse> page = new PageImpl<>(List.of(validResponse));
        Mockito.when(serviceService.getAllServices(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(serviceId.toString()));
    }

    @Test
    void testGetServiceById() throws Exception {
        Mockito.when(serviceService.getServiceById(serviceId)).thenReturn(validResponse);

        mockMvc.perform(get("/api/services/{id}", serviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(serviceId.toString()));
    }

    @Test
    void testGetServicesByCategory() throws Exception {
        Page<ServiceResponse> page = new PageImpl<>(List.of(validResponse));
        Mockito.when(serviceService.getServicesByCategory(eq(categoryId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/services/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(serviceId.toString()));
    }

    @Test
    void testGetActiveServicesByCategory() throws Exception {
        Mockito.when(serviceService.getActiveServicesByCategory(categoryId)).thenReturn(List.of(validResponse));

        mockMvc.perform(get("/api/services/category/{categoryId}/active", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(serviceId.toString()));
    }

    @Test
    void testFilterServices() throws Exception {
        Page<ServiceResponse> page = new PageImpl<>(List.of(validResponse));
        Mockito.when(serviceService.filterServices(eq(categoryId), eq(true), eq("Test"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/services/filter")
                .param("categoryId", categoryId.toString())
                .param("active", "true")
                .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(serviceId.toString()));
    }

}