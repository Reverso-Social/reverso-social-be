package com.reverso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reverso.dto.response.ServiceCategoryResponse;
import com.reverso.service.interfaces.ServiceCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ServiceCategoryController.class)
@Import(com.reverso.config.TestSecurityConfig.class)
class ServiceCategoryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ServiceCategoryService categoryService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void getAllCategories_shouldReturnPage() throws Exception {
                ServiceCategoryResponse response = ServiceCategoryResponse.builder()
                                .id(UUID.randomUUID())
                                .name("Coaching")
                                .build();

                Page<ServiceCategoryResponse> page = new PageImpl<>(List.of(response));

                when(categoryService.getAllCategories(any())).thenReturn(page);

                mockMvc.perform(get("/api/service-categories")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sortBy", "name")
                                .param("sortDir", "ASC"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].name").value("Coaching"));
        }

        @Test
        void getActiveCategories_shouldReturnList() throws Exception {
                ServiceCategoryResponse response = ServiceCategoryResponse.builder()
                                .name("Activa")
                                .build();

                when(categoryService.getActiveCategories()).thenReturn(List.of(response));

                mockMvc.perform(get("/api/service-categories/active"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Activa"));
        }

        @Test
        void getCategoryById_shouldReturnCategory() throws Exception {
                UUID id = UUID.randomUUID();
                ServiceCategoryResponse response = ServiceCategoryResponse.builder()
                                .id(id)
                                .name("Marketing")
                                .build();

                when(categoryService.getCategoryById(id)).thenReturn(response);

                mockMvc.perform(get("/api/service-categories/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Marketing"));
        }

        @Test
        void createCategory_shouldReturn201() throws Exception {
                ServiceCategoryResponse response = ServiceCategoryResponse.builder()
                                .id(UUID.randomUUID())
                                .name("Nueva categoría")
                                .build();

                when(categoryService.createCategory(any())).thenReturn(response);

                String json = """
                                {
                                  "name": "Nueva categoría",
                                  "active": true
                                }
                                """;

                mockMvc.perform(post("/api/service-categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value("Nueva categoría"));
        }

        @Test
        void updateCategory_shouldReturn200() throws Exception {
                UUID id = UUID.randomUUID();
                ServiceCategoryResponse response = ServiceCategoryResponse.builder()
                                .id(id)
                                .name("Actualizada")
                                .build();

                when(categoryService.updateCategory(any(), any())).thenReturn(response);

                String json = """
                                {
                                  "name": "Actualizada",
                                  "active": true
                                }
                                """;

                mockMvc.perform(put("/api/service-categories/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Actualizada"));
        }

        @Test
        void deleteCategory_shouldReturn204() throws Exception {
                UUID id = UUID.randomUUID();
                doNothing().when(categoryService).deleteCategory(id);

                mockMvc.perform(delete("/api/service-categories/{id}", id))
                                .andExpect(status().isNoContent());
        }

        @Test
        void searchCategories_shouldReturnPage() throws Exception {
                ServiceCategoryResponse response = ServiceCategoryResponse.builder()
                                .name("Buscar")
                                .build();

                Page<ServiceCategoryResponse> page = new PageImpl<>(List.of(response));

                when(categoryService.searchCategories(any(), any(PageRequest.class))).thenReturn(page);

                mockMvc.perform(get("/api/service-categories/search")
                                .param("name", "Buscar")
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].name").value("Buscar"));
        }
}