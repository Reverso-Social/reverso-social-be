package com.reverso.service;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.ResourceDownloadRequest;
import com.reverso.dto.response.ResourceDownloadResponse;
import com.reverso.mapper.ResourceDownloadMapper;
import com.reverso.model.Resource;
import com.reverso.model.ResourceDownload;
import com.reverso.model.User;
import com.reverso.repository.ResourceDownloadRepository;
import com.reverso.repository.ResourceRepository;
import com.reverso.repository.UserRepository;
import com.reverso.service.impl.ResourceDownloadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResourceDownloadService - Tests Unitarios")
class ResourceDownloadServiceTest {

    @Mock
    private ResourceDownloadRepository downloadRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceDownloadMapper mapper;

    @InjectMocks
    private ResourceDownloadServiceImpl downloadService;

    private ResourceDownload validDownload;
    private ResourceDownloadRequest validRequest;
    private User validUser;
    private Resource validResource;

    @BeforeEach
    void setUp() {
        validUser = TestDataFactory.createValidUser();
        validResource = TestDataFactory.createValidResource();
        validDownload = TestDataFactory.createValidResourceDownload();
        validRequest = TestDataFactory.createValidResourceDownloadRequest();
    }

    @Test
    @DisplayName("Crear descarga - Success")
    void testCreateDownload_ValidData_ReturnsDownloadResponse() {
        when(userRepository.findById(any())).thenReturn(Optional.of(validUser));
        when(resourceRepository.findById(any())).thenReturn(Optional.of(validResource));
        when(downloadRepository.existsByUserIdAndResourceId(any(), any())).thenReturn(false);
        when(downloadRepository.save(any(ResourceDownload.class))).thenReturn(validDownload);
        when(mapper.toResponse(any())).thenReturn(new ResourceDownloadResponse());

        ResourceDownloadResponse result = downloadService.createDownload(validRequest);

        assertThat(result).isNotNull();
        verify(downloadRepository, times(1)).save(any(ResourceDownload.class));
    }

    @Test
    @DisplayName("Crear descarga duplicada - Throws Exception")
    void testCreateDownload_AlreadyDownloaded_ThrowsException() {
        when(userRepository.findById(any())).thenReturn(Optional.of(validUser));
        when(resourceRepository.findById(any())).thenReturn(Optional.of(validResource));
        when(downloadRepository.existsByUserIdAndResourceId(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> downloadService.createDownload(validRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already downloaded");

        verify(downloadRepository, never()).save(any());
    }

    @Test
    @DisplayName("Obtener todas las descargas - Success")
    void testGetAllDownloads_ReturnsListOfDownloads() {
        List<ResourceDownload> downloads = Arrays.asList(validDownload);
        when(downloadRepository.findAll()).thenReturn(downloads);
        when(mapper.toResponse(any())).thenReturn(new ResourceDownloadResponse());

        List<ResourceDownloadResponse> result = downloadService.getAllDownloads();

        assertThat(result).hasSize(1);
        verify(downloadRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Contar descargas por recurso - Success")
    void testCountDownloadsByResource_ReturnsCount() {
        UUID resourceId = UUID.randomUUID();
        when(downloadRepository.countByResourceId(resourceId)).thenReturn(5L);

        long count = downloadService.countDownloadsByResource(resourceId);

        assertThat(count).isEqualTo(5L);
        verify(downloadRepository, times(1)).countByResourceId(resourceId);
    }

    @Test
    @DisplayName("Verificar si usuario descargó recurso - Returns true")
    void testHasUserDownloadedResource_ReturnsTrue() {
        UUID userId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        when(downloadRepository.existsByUserIdAndResourceId(userId, resourceId)).thenReturn(true);

        boolean result = downloadService.hasUserDownloadedResource(userId, resourceId);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Eliminar descarga - Success")
    void testDeleteDownload_ExistingId_DeletesDownload() {
        UUID id = UUID.randomUUID();
        when(downloadRepository.existsById(id)).thenReturn(true);
        doNothing().when(downloadRepository).deleteById(id);

        downloadService.deleteDownload(id);

        verify(downloadRepository, times(1)).deleteById(id);
    }
}