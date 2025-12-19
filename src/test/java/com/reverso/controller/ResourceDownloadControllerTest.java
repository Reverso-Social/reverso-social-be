package com.reverso.controller;

import com.reverso.dto.request.ResourceDownloadRequest;
import com.reverso.dto.response.ResourceDownloadResponse;
import com.reverso.service.interfaces.ResourceDownloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ResourceDownloadControllerTest {

    @Mock
    private ResourceDownloadService service;

    @InjectMocks
    private ResourceDownloadController controller;

    private ResourceDownloadRequest request;
    private ResourceDownloadResponse response;

    private UUID userId;
    private UUID resourceId;
    private UUID downloadId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        resourceId = UUID.randomUUID();
        downloadId = UUID.randomUUID();

        request = ResourceDownloadRequest.builder()
                .userId(userId)
                .resourceId(resourceId)
                .build();

        response = ResourceDownloadResponse.builder()
                .id(downloadId)
                .userId(userId)
                .resourceId(resourceId)
                .build();
    }

    @Test
    void createDownload_shouldReturnCreatedDownload() {
        when(service.createDownload(request)).thenReturn(response);

        ResponseEntity<ResourceDownloadResponse> result = controller.createDownload(request);

        assertThat(result.getStatusCodeValue()).isEqualTo(201);
        assertThat(result.getBody()).isEqualTo(response);
        verify(service).createDownload(request);
    }

    @Test
    void getAllDownloads_shouldReturnListOfDownloads() {
        when(service.getAllDownloads()).thenReturn(List.of(response));

        ResponseEntity<List<ResourceDownloadResponse>> result = controller.getAllDownloads();

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).contains(response);
        verify(service).getAllDownloads();
    }

    @Test
    void getDownloadsByUser_shouldReturnListForUser() {
        when(service.getDownloadsByUser(userId)).thenReturn(List.of(response));

        ResponseEntity<List<ResourceDownloadResponse>> result = controller.getDownloadsByUser(userId);

        assertThat(result.getBody()).contains(response);
        verify(service).getDownloadsByUser(userId);
    }

    @Test
    void getDownloadsByResource_shouldReturnListForResource() {
        when(service.getDownloadsByResource(resourceId)).thenReturn(List.of(response));

        ResponseEntity<List<ResourceDownloadResponse>> result = controller.getDownloadsByResource(resourceId);

        assertThat(result.getBody()).contains(response);
        verify(service).getDownloadsByResource(resourceId);
    }

    @Test
    void getDownloadById_shouldReturnDownload() {
        when(service.getDownloadById(downloadId)).thenReturn(response);

        ResponseEntity<ResourceDownloadResponse> result = controller.getDownloadById(downloadId);

        assertThat(result.getBody()).isEqualTo(response);
        verify(service).getDownloadById(downloadId);
    }

    @Test
    void countDownloadsByResource_shouldReturnCount() {
        when(service.countDownloadsByResource(resourceId)).thenReturn(5L);

        ResponseEntity<Long> result = controller.countDownloadsByResource(resourceId);

        assertThat(result.getBody()).isEqualTo(5L);
        verify(service).countDownloadsByResource(resourceId);
    }

    @Test
    void hasUserDownloadedResource_shouldReturnBoolean() {
        when(service.hasUserDownloadedResource(userId, resourceId)).thenReturn(true);

        ResponseEntity<Boolean> result = controller.hasUserDownloadedResource(userId, resourceId);

        assertThat(result.getBody()).isTrue();
        verify(service).hasUserDownloadedResource(userId, resourceId);
    }

    @Test
    void deleteDownload_shouldCallServiceAndReturnNoContent() {
        ResponseEntity<Void> result = controller.deleteDownload(downloadId);

        assertThat(result.getStatusCodeValue()).isEqualTo(204);
        verify(service).deleteDownload(downloadId);
    }
}