package com.reverso.mapper;

import com.reverso.dto.request.ServiceFeatureRequest;
import com.reverso.dto.response.ServiceFeatureResponse;
import com.reverso.model.Service;
import com.reverso.model.ServiceFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceFeatureMapperTest {

    private ServiceFeatureMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ServiceFeatureMapperImpl();
    }

    @Test
    void toResponse_shouldMapAllFieldsIncludingServiceData() {
        UUID serviceId = UUID.randomUUID();
        UUID featureId = UUID.randomUUID();

        Service service = Service.builder()
                .id(serviceId)
                .name("Web Development")
                .build();

        ServiceFeature feature = ServiceFeature.builder()
                .id(featureId)
                .title("Feature title")
                .description("Feature description")
                .sortOrder(1)
                .service(service)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ServiceFeatureResponse response = mapper.toResponse(feature);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(featureId);
        assertThat(response.getTitle()).isEqualTo("Feature title");
        assertThat(response.getDescription()).isEqualTo("Feature description");
        assertThat(response.getSortOrder()).isEqualTo(1);
        assertThat(response.getServiceId()).isEqualTo(serviceId);
        assertThat(response.getServiceName()).isEqualTo("Web Development");
        assertThat(response.getCreatedAt()).isEqualTo(feature.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(feature.getUpdatedAt());
    }

    @Test
    void toResponse_shouldReturnNull_whenFeatureIsNull() {
        ServiceFeatureResponse response = mapper.toResponse(null);
        assertThat(response).isNull();
    }

    @Test
    void toEntity_shouldMapRequestToEntity() {
        ServiceFeatureRequest request = ServiceFeatureRequest.builder()
                .title("New feature")
                .description("New description")
                .sortOrder(2)
                .build();

        ServiceFeature feature = mapper.toEntity(request);

        assertThat(feature).isNotNull();
        assertThat(feature.getTitle()).isEqualTo("New feature");
        assertThat(feature.getDescription()).isEqualTo("New description");
        assertThat(feature.getSortOrder()).isEqualTo(2);
        assertThat(feature.getService()).isNull();
    }

    @Test
    void toEntity_shouldReturnNull_whenRequestIsNull() {
        ServiceFeature feature = mapper.toEntity(null);
        assertThat(feature).isNull();
    }

    @Test
    void updateFromRequest_shouldUpdateOnlyNonNullFields() {
        ServiceFeature feature = ServiceFeature.builder()
                .title("Old title")
                .description("Old description")
                .sortOrder(1)
                .build();

        ServiceFeatureRequest request = ServiceFeatureRequest.builder()
                .title("Updated title")
                .description(null)
                .sortOrder(3)
                .build();

        mapper.updateFromRequest(request, feature);

        assertThat(feature.getTitle()).isEqualTo("Updated title");
        assertThat(feature.getDescription()).isEqualTo("Old description");
        assertThat(feature.getSortOrder()).isEqualTo(3);
    }

    @Test
    void updateFromRequest_shouldDoNothing_whenRequestIsNull() {
        ServiceFeature feature = ServiceFeature.builder()
                .title("Title")
                .build();

        mapper.updateFromRequest(null, feature);

        assertThat(feature.getTitle()).isEqualTo("Title");
    }
}
