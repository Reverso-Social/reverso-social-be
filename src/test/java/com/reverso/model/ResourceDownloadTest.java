package com.reverso.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceDownloadTest {

    @Test
    void builder_shouldCreateResourceDownloadWithGivenValues() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Resource resource = Resource.builder().id(UUID.randomUUID()).build();

        ResourceDownload download = ResourceDownload.builder()
                .user(user)
                .resource(resource)
                .build();

        assertThat(download.getUser()).isEqualTo(user);
        assertThat(download.getResource()).isEqualTo(resource);
    }

    @Test
    void onCreate_shouldSetCreatedAt() {
        ResourceDownload download = new ResourceDownload();
        download.onCreate();

        assertThat(download.getCreatedAt()).isNotNull();
    }
}