package com.reverso.mapper;

import com.reverso.dto.request.ServiceCategoryRequest;
import com.reverso.dto.response.ServiceCategoryResponse;
import com.reverso.model.Service;
import com.reverso.model.ServiceCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceCategoryMapperImplTest {

    private ServiceCategoryMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new ServiceCategoryMapperImpl();
    }

    @Test
    void toResponse_shouldMapAllFieldsAndServicesCount() {
        ServiceCategory category = ServiceCategory.builder()
                .id(UUID.randomUUID())
                .name("Category name")
                .description("Description")
                .iconUrl("icon.png")
                .sortOrder(1)
                .active(true)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .services(List.of(
                        Service.builder().build(),
                        Service.builder().build()
                ))
                .build();

        ServiceCategoryResponse response = mapper.toResponse(category);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(category.getId());
        assertThat(response.getName()).isEqualTo(category.getName());
        assertThat(response.getDescription()).isEqualTo(category.getDescription());
        assertThat(response.getIconUrl()).isEqualTo(category.getIconUrl());
        assertThat(response.getSortOrder()).isEqualTo(category.getSortOrder());
        assertThat(response.getActive()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(category.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(response.getServicesCount()).isEqualTo(2);
    }

    @Test
    void toResponse_shouldReturnZeroServicesCountWhenServicesIsNull() {
        ServiceCategory category = ServiceCategory.builder()
                .name("Category")
                .services(null)
                .build();

        ServiceCategoryResponse response = mapper.toResponse(category);

        assertThat(response.getServicesCount()).isZero();
    }

    @Test
    void toResponse_shouldReturnNullWhenCategoryIsNull() {
        assertThat(mapper.toResponse(null)).isNull();
    }

    @Test
    void toEntity_shouldMapFieldsFromRequest() {
        ServiceCategoryRequest request = ServiceCategoryRequest.builder()
                .name("New category")
                .description("New description")
                .iconUrl("icon.svg")
                .sortOrder(3)
                .active(false)
                .build();

        ServiceCategory entity = mapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo(request.getName());
        assertThat(entity.getDescription()).isEqualTo(request.getDescription());
        assertThat(entity.getIconUrl()).isEqualTo(request.getIconUrl());
        assertThat(entity.getSortOrder()).isEqualTo(request.getSortOrder());
        assertThat(entity.getActive()).isFalse();
    }

    @Test
    void toEntity_shouldReturnNullWhenRequestIsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void updateFromRequest_shouldUpdateOnlyNonNullFields() {
        ServiceCategory category = ServiceCategory.builder()
                .name("Old name")
                .description("Old description")
                .active(true)
                .sortOrder(1)
                .build();

        ServiceCategoryRequest request = ServiceCategoryRequest.builder()
                .name("New name")
                .description(null)
                .active(false)
                .build();

        mapper.updateFromRequest(request, category);

        assertThat(category.getName()).isEqualTo("New name");
        assertThat(category.getDescription()).isEqualTo("Old description"); // no se pisa
        assertThat(category.getActive()).isFalse();
        assertThat(category.getSortOrder()).isEqualTo(1);
    }

    @Test
    void updateFromRequest_shouldDoNothingWhenRequestIsNull() {
        ServiceCategory category = ServiceCategory.builder()
                .name("Name")
                .build();

        mapper.updateFromRequest(null, category);

        assertThat(category.getName()).isEqualTo("Name");
    }
}