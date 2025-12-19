package com.reverso.service;

import com.reverso.service.impl.ServiceCategoryServiceImpl;
import com.reverso.dto.request.ServiceCategoryRequest;
import com.reverso.dto.response.ServiceCategoryResponse;
import com.reverso.exception.ResourceNotFoundException;
import com.reverso.mapper.ServiceCategoryMapper;
import com.reverso.model.ServiceCategory;
import com.reverso.repository.ServiceCategoryRepository;
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
class ServiceCategoryServiceImplTest {

    @Mock
    private ServiceCategoryRepository repository;

    @Mock
    private ServiceCategoryMapper mapper;

    @InjectMocks
    private ServiceCategoryServiceImpl service;

    private ServiceCategory category;
    private ServiceCategoryResponse response;
    private ServiceCategoryRequest request;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        category = new ServiceCategory();
        request = ServiceCategoryRequest.builder().name("Test").build();
        response = new ServiceCategoryResponse();
    }

    @Test
    void getAllCategories_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ServiceCategory> page = new PageImpl<>(Arrays.asList(category));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(category)).thenReturn(response);

        Page<ServiceCategoryResponse> result = service.getAllCategories(pageable);

        assertThat(result.getContent()).containsExactly(response);
        verify(repository).findAll(pageable);
        verify(mapper).toResponse(category);
    }

    @Test
    void getActiveCategories_shouldReturnMappedList() {
        when(repository.findByActiveOrderBySortOrderAsc(true))
                .thenReturn(Arrays.asList(category));
        when(mapper.toResponse(category)).thenReturn(response);

        List<ServiceCategoryResponse> result = service.getActiveCategories();

        assertThat(result).containsExactly(response);
        verify(repository).findByActiveOrderBySortOrderAsc(true);
        verify(mapper).toResponse(category);
    }

    @Test
    void getCategoryById_shouldReturnResponse_whenCategoryExists() {
        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(mapper.toResponse(category)).thenReturn(response);

        ServiceCategoryResponse result = service.getCategoryById(id);

        assertThat(result).isEqualTo(response);
        verify(repository).findById(id);
        verify(mapper).toResponse(category);
    }

    @Test
    void getCategoryById_shouldThrow_whenCategoryNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCategoryById(id));
    }

    @Test
    void createCategory_shouldSaveAndReturnResponse() {
        when(mapper.toEntity(request)).thenReturn(category);
        category.setActive(null);
        when(repository.save(category)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(response);

        ServiceCategoryResponse result = service.createCategory(request);

        assertThat(result).isEqualTo(response);
        assertThat(category.getActive()).isTrue();
        verify(repository).save(category);
        verify(mapper).toResponse(category);
    }

    @Test
    void updateCategory_shouldUpdateAndReturnResponse_whenCategoryExists() {
        when(repository.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(mapper).updateFromRequest(request, category);
        when(repository.save(category)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(response);

        ServiceCategoryResponse result = service.updateCategory(id, request);

        assertThat(result).isEqualTo(response);
        verify(repository).findById(id);
        verify(mapper).updateFromRequest(request, category);
        verify(repository).save(category);
        verify(mapper).toResponse(category);
    }

    @Test
    void updateCategory_shouldThrow_whenCategoryNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateCategory(id, request));
    }

    @Test
    void deleteCategory_shouldCallDelete_whenCategoryExists() {
        when(repository.findById(id)).thenReturn(Optional.of(category));

        service.deleteCategory(id);

        verify(repository).delete(category);
    }

    @Test
    void deleteCategory_shouldThrow_whenCategoryNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCategory(id));
    }

    @Test
    void searchCategories_shouldReturnMappedPage() {
        String name = "test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<ServiceCategory> page = new PageImpl<>(Arrays.asList(category));

        when(repository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(page);
        when(mapper.toResponse(category)).thenReturn(response);

        Page<ServiceCategoryResponse> result = service.searchCategories(name, pageable);

        assertThat(result.getContent()).containsExactly(response);
        verify(repository).findByNameContainingIgnoreCase(name, pageable);
        verify(mapper).toResponse(category);
    }
}
