package com.reverso.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceCategoryTest {

    @Test
    void builder_shouldCreateServiceCategoryWithGivenValues() {
        ServiceCategory category = ServiceCategory.builder()
                .name("Development")
                .description("Category for development services")
                .iconUrl("icon.png")
                .sortOrder(1)
                .active(true)
                .build();

        assertThat(category.getName()).isEqualTo("Development");
        assertThat(category.getDescription()).isEqualTo("Category for development services");
        assertThat(category.getIconUrl()).isEqualTo("icon.png");
        assertThat(category.getSortOrder()).isEqualTo(1);
        assertThat(category.getActive()).isTrue();
        assertThat(category.getServices()).isEmpty();
    }

    @Test
    void onCreate_shouldSetCreatedAtUpdatedAtAndDefaultActive() {
        ServiceCategory category = new ServiceCategory();
        category.setActive(null);
        category.onCreate();

        assertThat(category.getCreatedAt()).isNotNull();
        assertThat(category.getUpdatedAt()).isNotNull();
        assertThat(category.getActive()).isTrue();
    }

    @Test
    void onUpdate_shouldUpdateUpdatedAt() throws InterruptedException {
        ServiceCategory category = new ServiceCategory();
        category.onCreate();
        LocalDateTime createdAt = category.getCreatedAt();

        Thread.sleep(10);
        category.onUpdate();

        assertThat(category.getUpdatedAt()).isAfter(createdAt);
    }
}