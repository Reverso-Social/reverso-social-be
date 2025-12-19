package com.reverso.mapper;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.ResourceCreateRequest;
import com.reverso.dto.response.ResourceResponse;
import com.reverso.mapper.ResourceMapperImpl;
import com.reverso.model.Resource;
import com.reverso.model.enums.ResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ResourceMapperImpl.class})
@DisplayName("ResourceMapper - Tests")
class ResourceMapperTest {

    @Autowired
    private ResourceMapper resourceMapper;

    @Test
    @DisplayName("Mapear Request a Entity - Success")
    void testToEntity_ValidRequest_ReturnsEntity() {
        ResourceCreateRequest request = TestDataFactory.createValidResourceCreateRequest();

        Resource entity = resourceMapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo(request.getTitle());
        assertThat(entity.getDescription()).isEqualTo(request.getDescription());
        assertThat(entity.getType()).isEqualTo(ResourceType.GUIDE);
        assertThat(entity.getFileUrl()).isEqualTo(request.getFileUrl());
        assertThat(entity.getIsPublic()).isEqualTo(request.getIsPublic());
    }

    @Test
    @DisplayName("Mapear Entity a Response - Success")
    void testToResponse_ValidEntity_ReturnsResponse() {
        Resource entity = TestDataFactory.createValidResource();

        ResourceResponse response = resourceMapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(entity.getId());
        assertThat(response.getTitle()).isEqualTo(entity.getTitle());
        assertThat(response.getType()).isEqualTo(entity.getType().name());
    }

    @Test
    @DisplayName("Mapear tipo GUIDE a String - Success")
    void testTypeToString_GuideType_ReturnsGUIDE() {
        String result = resourceMapper.typeToString(ResourceType.GUIDE);

        assertThat(result).isEqualTo("GUIDE");
    }

    @Test
    @DisplayName("Mapear String a tipo REPORT - Success")
    void testStringToType_ReportString_ReturnsREPORT() {
        ResourceType result = resourceMapper.stringToType("REPORT");

        assertThat(result).isEqualTo(ResourceType.REPORT);
    }
}