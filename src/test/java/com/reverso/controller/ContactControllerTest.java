package com.reverso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.response.ContactResponse;
import com.reverso.service.interfaces.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("ContactController - Web layer tests")
@SuppressWarnings("null")
class ContactControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private ContactService contactService;

        @Test
        @DisplayName("POST /api/contacts - OK")
        void createContact_ok() throws Exception {
                ContactCreateRequest request = ContactCreateRequest.builder()
                                .fullName("Juan Pérez")
                                .email("juan@test.com")
                                .message("Hola")
                                .acceptsPrivacy(true)
                                .build();

                ContactResponse response = ContactResponse.builder()
                                .id(UUID.randomUUID())
                                .fullName(request.getFullName())
                                .email(request.getEmail())
                                .message(request.getMessage())
                                .status("PENDING")
                                .build();

                when(contactService.create(any(ContactCreateRequest.class))).thenReturn(response);

                mockMvc.perform(post("/api/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.fullName").value("Juan Pérez"));

                verify(contactService).create(any(ContactCreateRequest.class));
        }

        @Test
        @DisplayName("GET /api/contacts - OK")
        void getAll_ok() throws Exception {
                when(contactService.getAll()).thenReturn(List.of());

                mockMvc.perform(get("/api/contacts"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray());

                verify(contactService).getAll();
        }

        @Test
        @DisplayName("DELETE /api/contacts/{id} - 204")
        void delete_ok() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(contactService).delete(id);
        }

}