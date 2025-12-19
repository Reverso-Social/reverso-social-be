package com.reverso.service;

import com.reverso.service.impl.ServiceFeatureServiceImpl;
import com.reverso.dto.request.ServiceFeatureRequest;
import com.reverso.dto.response.ServiceFeatureResponse;
import com.reverso.exception.ResourceNotFoundException;
import com.reverso.mapper.ServiceFeatureMapper;
import com.reverso.model.Service;
import com.reverso.model.ServiceFeature;
import com.reverso.repository.ServiceFeatureRepository;
import com.reverso.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class ServiceFeatureServiceImplTest {

    @Mock
    private ServiceFeatureRepository featureRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceFeatureMapper featureMapper;

    @InjectMocks
    private ServiceFeatureServiceImpl service;

    private ServiceFeature feature;
    private ServiceFeatureResponse response;
    private ServiceFeatureRequest request;
    private Service serviceEntity;
    private UUID id;
    private UUID serviceId;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        serviceId = UUID.randomUUID();
        feature = new ServiceFeature();
        serviceEntity = new Service();
        feature.setService(serviceEntity);
        request = ServiceFeatureRequest.builder().serviceId(serviceId).build();
        response = new ServiceFeatureResponse();
    }

    @Test
    void getAllFeatures_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ServiceFeature> page = new PageImpl<>(Arrays.asList(feature));

        when(featureRepository.findAll(pageable)).thenReturn(page);
        when(featureMapper.toResponse(feature)).thenReturn(response);

        Page<ServiceFeatureResponse> result = service.getAllFeatures(pageable);

        assertThat(result.getContent()).containsExactly(response);
        verify(featureRepository).findAll(pageable);
        verify(featureMapper).toResponse(feature);
    }

    @Test
    void getFeatureById_shouldReturnResponse_whenFeatureExists() {
        when(featureRepository.findById(id)).thenReturn(Optional.of(feature));
        when(featureMapper.toResponse(feature)).thenReturn(response);

        ServiceFeatureResponse result = service.getFeatureById(id);

        assertThat(result).isEqualTo(response);
        verify(featureRepository).findById(id);
        verify(featureMapper).toResponse(feature);
    }

    @Test
    void getFeatureById_shouldThrow_whenFeatureNotFound() {
        when(featureRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getFeatureById(id));
    }

    @Test
    void getFeaturesByService_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ServiceFeature> page = new PageImpl<>(Arrays.asList(feature));

        when(featureRepository.findByServiceId(serviceId, pageable)).thenReturn(page);
        when(featureMapper.toResponse(feature)).thenReturn(response);

        Page<ServiceFeatureResponse> result = service.getFeaturesByService(serviceId, pageable);

        assertThat(result.getContent()).containsExactly(response);
        verify(featureRepository).findByServiceId(serviceId, pageable);
        verify(featureMapper).toResponse(feature);
    }

    @Test
    void getFeaturesByServiceOrdered_shouldReturnMappedList() {
        when(featureRepository.findByServiceIdOrderBySortOrderAsc(serviceId))
                .thenReturn(Arrays.asList(feature));
        when(featureMapper.toResponse(feature)).thenReturn(response);

        List<ServiceFeatureResponse> result = service.getFeaturesByServiceOrdered(serviceId);

        assertThat(result).containsExactly(response);
        verify(featureRepository).findByServiceIdOrderBySortOrderAsc(serviceId);
        verify(featureMapper).toResponse(feature);
    }

    @Test
    void createFeature_shouldSaveAndReturnResponse_whenServiceExists() {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(serviceEntity));
        when(featureMapper.toEntity(request)).thenReturn(feature);
        when(featureRepository.save(feature)).thenReturn(feature);
        when(featureMapper.toResponse(feature)).thenReturn(response);

        ServiceFeatureResponse result = service.createFeature(request);

        assertThat(result).isEqualTo(response);
        assertThat(feature.getService()).isEqualTo(serviceEntity);
        verify(featureRepository).save(feature);
        verify(featureMapper).toResponse(feature);
    }

    @Test
    void createFeature_shouldThrow_whenServiceNotFound() {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createFeature(request));
    }

    @Test
    void updateFeature_shouldUpdateAndReturnResponse_whenFeatureExists() {
        UUID newServiceId = UUID.randomUUID();
        Service newService = new Service();
        ServiceFeatureRequest updateRequest = ServiceFeatureRequest.builder()
                .serviceId(newServiceId)
                .build();

        feature.setService(serviceEntity);

        when(featureRepository.findById(id)).thenReturn(Optional.of(feature));
        when(serviceRepository.findById(newServiceId)).thenReturn(Optional.of(newService));
        doNothing().when(featureMapper).updateFromRequest(updateRequest, feature);
        when(featureRepository.save(feature)).thenReturn(feature);
        when(featureMapper.toResponse(feature)).thenReturn(response);

        ServiceFeatureResponse result = service.updateFeature(id, updateRequest);

        assertThat(result).isEqualTo(response);
        assertThat(feature.getService()).isEqualTo(newService);
        verify(featureRepository).save(feature);
        verify(featureMapper).toResponse(feature);
    }

    @Test
    void updateFeature_shouldThrow_whenFeatureNotFound() {
        when(featureRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateFeature(id, request));
    }

    @Test
    void updateFeature_shouldThrow_whenServiceNotFound() {
        ServiceFeatureRequest updateRequest = ServiceFeatureRequest.builder()
                .serviceId(UUID.randomUUID())
                .build();
        when(featureRepository.findById(id)).thenReturn(Optional.of(feature));
        when(serviceRepository.findById(updateRequest.getServiceId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateFeature(id, updateRequest));
    }

    @Test
    void deleteFeature_shouldCallDelete_whenFeatureExists() {
        when(featureRepository.findById(id)).thenReturn(Optional.of(feature));

        service.deleteFeature(id);

        verify(featureRepository).delete(feature);
    }

    @Test
    void deleteFeature_shouldThrow_whenFeatureNotFound() {
        when(featureRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteFeature(id));
    }
}