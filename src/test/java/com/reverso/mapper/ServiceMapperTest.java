package com.reverso.mapper;

import com.reverso.dto.request.ServiceRequest;
import com.reverso.dto.response.ServiceResponse;
import com.reverso.model.Service;
import com.reverso.model.ServiceCategory;
import com.reverso.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceMapperTest {

    private ServiceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ServiceMapper.class);
    }

    @Test
    @DisplayName("Map Service to ServiceResponse")
    void testToResponse() {
        UUID serviceId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ServiceCategory category = new ServiceCategory();
        category.setId(categoryId);
        category.setName("Marketing");

        User user = new User();
        user.setId(userId);
        user.setFullName("Admin User");

        Service service = new Service();
        service.setId(serviceId);
        service.setName("Consultoría");
        service.setCategory(category);
        service.setCreatedByUser(user);
        service.setFeatures(Collections.emptyList());

        ServiceResponse response = mapper.toResponse(service);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(serviceId);
        assertThat(response.getCategoryId()).isEqualTo(categoryId);
        assertThat(response.getCategoryName()).isEqualTo("Marketing");
        assertThat(response.getCreatedByUserId()).isEqualTo(userId);
        assertThat(response.getCreatedByUserName()).isEqualTo("Admin User");
        assertThat(response.getFeaturesCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Map ServiceRequest to Service entity")
    void testToEntity() {
        ServiceRequest request = new ServiceRequest();
        request.setName("Desarrollo Web");

        Service service = mapper.toEntity(request);

        assertThat(service).isNotNull();
        assertThat(service.getName()).isEqualTo("Desarrollo Web");
        assertThat(service.getCategory()).isNull();
        assertThat(service.getCreatedByUser()).isNull();
    }

    @Test
    @DisplayName("Update Service entity from ServiceRequest")
    void testUpdateFromRequest() {
        Service service = new Service();
        service.setName("Old Name");

        ServiceRequest request = new ServiceRequest();
        request.setName("New Name");

        mapper.updateFromRequest(request, service);

        assertThat(service.getName()).isEqualTo("New Name");
    }
}