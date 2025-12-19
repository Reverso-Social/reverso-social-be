package com.reverso.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceTest {

    @Test
    void builder_shouldCreateServiceWithGivenValues() {
        ServiceCategory category = ServiceCategory.builder().id(UUID.randomUUID()).build();
        User user = User.builder().id(UUID.randomUUID()).build();

        Service service = Service.builder()
                .category(category)
                .name("Web Development")
                .shortDescription("Short desc")
                .fullDescription("Full desc")
                .iconUrl("icon.png")
                .sortOrder(1)
                .active(true)
                .createdByUser(user)
                .build();

        assertThat(service.getCategory()).isEqualTo(category);
        assertThat(service.getName()).isEqualTo("Web Development");
        assertThat(service.getShortDescription()).isEqualTo("Short desc");
        assertThat(service.getFullDescription()).isEqualTo("Full desc");
        assertThat(service.getIconUrl()).isEqualTo("icon.png");
        assertThat(service.getSortOrder()).isEqualTo(1);
        assertThat(service.getActive()).isTrue();
        assertThat(service.getCreatedByUser()).isEqualTo(user);
        assertThat(service.getFeatures()).isEmpty();
    }

    @Test
    void onCreate_shouldSetCreatedAtUpdatedAtAndDefaultActive() {
        Service service = new Service();
        service.setActive(null);
        service.onCreate();

        assertThat(service.getCreatedAt()).isNotNull();
        assertThat(service.getUpdatedAt()).isNotNull();
        assertThat(service.getActive()).isTrue();
    }

    @Test
    void onUpdate_shouldUpdateUpdatedAt() throws InterruptedException {
        Service service = new Service();
        service.onCreate();
        LocalDateTime createdAt = service.getCreatedAt();

        Thread.sleep(10);
        service.onUpdate();

        assertThat(service.getUpdatedAt()).isAfter(createdAt);
    }
}