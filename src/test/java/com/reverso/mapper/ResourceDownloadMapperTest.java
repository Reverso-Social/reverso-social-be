package com.reverso.mapper;

import com.reverso.dto.request.ResourceDownloadRequest;
import com.reverso.dto.response.ResourceDownloadResponse;
import com.reverso.model.Resource;
import com.reverso.model.ResourceDownload;
import com.reverso.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceDownloadMapperTest {

    private ResourceDownloadMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ResourceDownloadMapperImpl();
    }

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {
        UUID userId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        UUID downloadId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .fullName("Ana López")
                .build();

        Resource resource = Resource.builder()
                .id(resourceId)
                .title("Guía Reverso")
                .build();

        LocalDateTime createdAt = LocalDateTime.now();

        ResourceDownload download = ResourceDownload.builder()
                .id(downloadId)
                .user(user)
                .resource(resource)
                .createdAt(createdAt)
                .build();

        ResourceDownloadResponse response = mapper.toResponse(download);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(downloadId);
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getUserName()).isEqualTo("Ana López");
        assertThat(response.getResourceId()).isEqualTo(resourceId);
        assertThat(response.getResourceTitle()).isEqualTo("Guía Reverso");
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void toResponse_shouldReturnNull_whenInputIsNull() {
        ResourceDownloadResponse response = mapper.toResponse(null);
        assertThat(response).isNull();
    }

    @Test
    void toResponse_shouldHandleNullUserAndResource() {
        ResourceDownload download = ResourceDownload.builder().build();

        ResourceDownloadResponse response = mapper.toResponse(download);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isNull();
        assertThat(response.getUserName()).isNull();
        assertThat(response.getResourceId()).isNull();
        assertThat(response.getResourceTitle()).isNull();
    }

    @Test
    void toEntity_shouldReturnEmptyEntity_whenRequestIsProvided() {
        ResourceDownloadRequest request = new ResourceDownloadRequest();

        ResourceDownload entity = mapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getUser()).isNull();
        assertThat(entity.getResource()).isNull();
    }

    @Test
    void toEntity_shouldReturnNull_whenRequestIsNull() {
        ResourceDownload entity = mapper.toEntity(null);
        assertThat(entity).isNull();
    }
}