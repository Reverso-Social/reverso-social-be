package com.reverso.controller;

import com.reverso.dto.request.DownloadLeadRequest;
import com.reverso.dto.response.DownloadLeadResponse;
import com.reverso.service.interfaces.DownloadLeadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DownloadLeadControllerTest {

    @Mock
    private DownloadLeadService service;

    @InjectMocks
    private DownloadLeadController controller;

    private DownloadLeadRequest request;
    private DownloadLeadResponse response;
    private UUID resourceId;
    private UUID leadId;

    @BeforeEach
    void setUp() {

        resourceId = UUID.randomUUID();
        leadId = UUID.randomUUID();

        request = DownloadLeadRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .resourceId(resourceId)
                .build();

        response = DownloadLeadResponse.builder()
                .id(leadId)
                .name("John Doe")
                .email("john@example.com")
                .resourceId(resourceId)
                .build();
    }

    @Test
    void createLead_shouldReturnCreatedLead() {
        when(service.createLead(request)).thenReturn(response);

        ResponseEntity<DownloadLeadResponse> result = controller.createLead(request);

        assertThat(result.getStatusCode().value()).isEqualTo(201);
        assertThat(result.getBody()).isEqualTo(response);
        verify(service).createLead(request);
    }

    @Test
    void getAllLeads_shouldReturnListOfLeads() {
        when(service.getAllLeads()).thenReturn(List.of(response));

        ResponseEntity<List<DownloadLeadResponse>> result = controller.getAllLeads();

        assertThat(result.getBody()).contains(response);
        verify(service).getAllLeads();
    }

    @Test
    void getLeadsByResource_shouldReturnListForResource() {
        when(service.getLeadsByResource(resourceId)).thenReturn(List.of(response));

        ResponseEntity<List<DownloadLeadResponse>> result = controller.getLeadsByResource(resourceId);

        assertThat(result.getBody()).contains(response);
        verify(service).getLeadsByResource(resourceId);
    }

    @Test
    void getLeadById_shouldReturnLead() {
        when(service.getLeadById(leadId)).thenReturn(response);

        ResponseEntity<DownloadLeadResponse> result = controller.getLeadById(leadId);

        assertThat(result.getBody()).isEqualTo(response);
        verify(service).getLeadById(leadId);
    }

    @Test
    void deleteLead_shouldCallServiceAndReturnNoContent() {
        ResponseEntity<Void> result = controller.deleteLead(leadId);

        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(service).deleteLead(leadId);
    }
}