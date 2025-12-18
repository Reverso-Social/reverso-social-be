package com.reverso.service;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.ServiceRequest;
import com.reverso.dto.response.ServiceResponse;
import com.reverso.mapper.ServiceMapper;
import com.reverso.model.Service;
import com.reverso.model.ServiceCategory;
import com.reverso.repository.ServiceCategoryRepository;
import com.reverso.repository.ServiceRepository;
import com.reverso.repository.UserRepository;
import com.reverso.service.impl.ServiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceServiceImpl - Tests Unitarios")
@SuppressWarnings("null")
class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceCategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceMapper serviceMapper;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    private ServiceCategory validCategory;
    private Service validService;
    private ServiceRequest validRequest;
    private ServiceResponse validResponse;

    @BeforeEach
    void setUp() {
        validCategory = TestDataFactory.createValidServiceCategory();
        validService = TestDataFactory.createValidService(validCategory);
        validRequest = TestDataFactory.createValidServiceRequest(validCategory.getId());
        validResponse = TestDataFactory.createValidServiceResponse(validService.getId());
    }


    @Test
    @DisplayName("Obtener servicio por ID existente - Success")
    void testGetServiceById_ExistingId_Success() {
        when(serviceRepository.findById(validService.getId())).thenReturn(Optional.of(validService));
        when(serviceMapper.toResponse(validService)).thenReturn(validResponse);

        ServiceResponse result = serviceService.getServiceById(validService.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validService.getId());
        verify(serviceRepository, times(1)).findById(validService.getId());
    }


    @Test
    @DisplayName("Obtener todos los servicios paginados - Success")
    void testGetAllServices_Success() {
        Page<Service> page = new PageImpl<>(Arrays.asList(validService));
        when(serviceRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(serviceMapper.toResponse(validService)).thenReturn(validResponse);

        Page<ServiceResponse> result = serviceService.getAllServices(Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        verify(serviceRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Obtener servicios por categoría paginados - Success")
    void testGetServicesByCategory_Success() {
        Page<Service> page = new PageImpl<>(Arrays.asList(validService));
        when(serviceRepository.findByCategoryId(validCategory.getId(), Pageable.unpaged())).thenReturn(page);
        when(serviceMapper.toResponse(validService)).thenReturn(validResponse);

        Page<ServiceResponse> result = serviceService.getServicesByCategory(validCategory.getId(), Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        verify(serviceRepository, times(1)).findByCategoryId(validCategory.getId(), Pageable.unpaged());
    }

    @Test
    @DisplayName("Obtener servicios activos por categoría - Success")
    void testGetActiveServicesByCategory_Success() {
        when(serviceRepository.findByCategoryIdAndActiveOrderBySortOrderAsc(validCategory.getId(), true))
                .thenReturn(Arrays.asList(validService));
        when(serviceMapper.toResponse(validService)).thenReturn(validResponse);

        List<ServiceResponse> result = serviceService.getActiveServicesByCategory(validCategory.getId());

        assertThat(result).hasSize(1);
        verify(serviceRepository, times(1)).findByCategoryIdAndActiveOrderBySortOrderAsc(validCategory.getId(), true);
    }


    @Test
    @DisplayName("Eliminar servicio existente - Success")
    void testDeleteService_Existing_Success() {
        when(serviceRepository.findById(validService.getId())).thenReturn(Optional.of(validService));
        doNothing().when(serviceRepository).delete(validService);

        serviceService.deleteService(validService.getId());

        verify(serviceRepository, times(1)).delete(validService);
    }


    @Test
    @DisplayName("Filtrar servicios - Success")
    void testFilterServices_Success() {
        Page<Service> page = new PageImpl<>(Arrays.asList(validService));
        when(serviceRepository.findByFilters(validCategory.getId(), true, "Web", Pageable.unpaged()))
                .thenReturn(page);
        when(serviceMapper.toResponse(validService)).thenReturn(validResponse);

        Page<ServiceResponse> result = serviceService.filterServices(validCategory.getId(), true, "Web", Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        verify(serviceRepository, times(1)).findByFilters(validCategory.getId(), true, "Web", Pageable.unpaged());
    }
}