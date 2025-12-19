package com.reverso.service;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.ResourceCreateRequest;
import com.reverso.dto.request.ResourceUpdateRequest;
import com.reverso.dto.response.ResourceResponse;
import com.reverso.mapper.ResourceMapper;
import com.reverso.model.Resource;
import com.reverso.model.enums.ResourceType;
import com.reverso.repository.ResourceRepository;
import com.reverso.service.impl.ResourceServiceImpl;
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
@DisplayName("ResourceService - Tests Unitarios")
@SuppressWarnings("null")
class ResourceServiceTest {

    @Mock
    private ResourceRepository repository;

    @Mock
    private ResourceMapper mapper;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private Resource validResource;
    private ResourceCreateRequest validCreateRequest;
    private ResourceResponse validResponse;

    @BeforeEach
    void setUp() {
        validResource = TestDataFactory.createValidResource();
        validCreateRequest = TestDataFactory.createValidResourceCreateRequest();
        
        validResponse = ResourceResponse.builder()
                .id(validResource.getId())
                .title(validResource.getTitle())
                .description(validResource.getDescription())
                .type(validResource.getType().name())
                .fileUrl(validResource.getFileUrl())
                .previewImageUrl(validResource.getPreviewImageUrl())
                .isPublic(validResource.getIsPublic())
                .downloadCount(0L)
                .build();
    }

    @Test
    @DisplayName("Crear recurso - Success")
    void testCreate_ValidData_ReturnsResourceResponse() {
        when(mapper.toEntity(any(ResourceCreateRequest.class))).thenReturn(validResource);
        when(repository.save(any(Resource.class))).thenReturn(validResource);
        when(mapper.toResponse(any(Resource.class))).thenReturn(validResponse);

        ResourceResponse result = resourceService.create(validCreateRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(validCreateRequest.getTitle());
        verify(repository, times(1)).save(any(Resource.class));
    }

    @Test
    @DisplayName("Obtener todos los recursos - Success")
    void testGetAll_ReturnsListOfResources() {
        List<Resource> resources = Arrays.asList(
                TestDataFactory.createValidResource(),
                TestDataFactory.createValidResource()
        );
        when(repository.findAll()).thenReturn(resources);
        when(mapper.toResponse(any(Resource.class))).thenReturn(validResponse);

        List<ResourceResponse> result = resourceService.getAll();

        assertThat(result).hasSize(2);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener recursos públicos - Success")
    void testGetPublic_ReturnsPublicResources() {
        List<Resource> publicResources = Arrays.asList(
                TestDataFactory.createPublicResource()
        );
        when(repository.findByIsPublicTrue()).thenReturn(publicResources);
        when(mapper.toResponse(any(Resource.class))).thenReturn(validResponse);

        List<ResourceResponse> result = resourceService.getPublic();

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findByIsPublicTrue();
    }

    @Test
    @DisplayName("Obtener por tipo - Success")
    void testGetByType_ReturnsFilteredResources() {
        List<Resource> guides = Arrays.asList(
                TestDataFactory.createResourceWithType(ResourceType.GUIDE)
        );
        when(repository.findByType("GUIDE")).thenReturn(guides);
        when(mapper.toResponse(any(Resource.class))).thenReturn(validResponse);

        List<ResourceResponse> result = resourceService.getByType("GUIDE");

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findByType("GUIDE");
    }

    @Test
    @DisplayName("Obtener por ID existente - Success")
    void testGetById_ExistingId_ReturnsResource() {
        UUID id = validResource.getId();
        when(repository.findById(id)).thenReturn(Optional.of(validResource));
        when(mapper.toResponse(validResource)).thenReturn(validResponse);

        ResourceResponse result = resourceService.getById(id);

        assertThat(result).isNotNull();
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Obtener por ID inexistente - Throws Exception")
    void testGetById_NonExistingId_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resourceService.getById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recurso no encontrado");
    }

    @Test
    @DisplayName("Actualizar recurso - Success")
    void testUpdate_ExistingResource_ReturnsUpdated() {
        UUID id = validResource.getId();
        ResourceUpdateRequest updateRequest = TestDataFactory.createValidResourceUpdateRequest();
        
        when(repository.findById(id)).thenReturn(Optional.of(validResource));
        when(repository.save(any(Resource.class))).thenReturn(validResource);
        when(mapper.toResponse(any(Resource.class))).thenReturn(validResponse);
        doNothing().when(mapper).updateFromRequest(any(), any());

        ResourceResponse result = resourceService.update(id, updateRequest);

        assertThat(result).isNotNull();
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any(Resource.class));
    }

    @Test
    @DisplayName("Eliminar recurso - Success")
    void testDelete_ValidId_DeletesResource() {
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(id);

        resourceService.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}