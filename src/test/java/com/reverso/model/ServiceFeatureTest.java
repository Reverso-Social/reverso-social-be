package com.reverso.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceFeatureTest {

    @Test
    void builder_shouldCreateServiceFeatureWithGivenValues() {
        Service dummyService = Service.builder().id(UUID.randomUUID()).name("Test Service").build();

        ServiceFeature feature = ServiceFeature.builder()
                .service(dummyService)
                .title("Feature 1")
                .description("Feature description")
                .sortOrder(1)
                .build();

        assertThat(feature.getService()).isEqualTo(dummyService);
        assertThat(feature.getTitle()).isEqualTo("Feature 1");
        assertThat(feature.getDescription()).isEqualTo("Feature description");
        assertThat(feature.getSortOrder()).isEqualTo(1);
    }

    @Test
    void onCreate_shouldSetCreatedAtAndUpdatedAt() {
        ServiceFeature feature = new ServiceFeature();
        feature.onCreate();

        assertThat(feature.getCreatedAt()).isNotNull();
        assertThat(feature.getUpdatedAt()).isNotNull();
    }

    @Test
    void onUpdate_shouldUpdateUpdatedAt() throws InterruptedException {
        ServiceFeature feature = new ServiceFeature();
        feature.onCreate();
        LocalDateTime beforeUpdate = feature.getUpdatedAt();

        Thread.sleep(10);
        feature.onUpdate();

        assertThat(feature.getUpdatedAt()).isAfter(beforeUpdate);
    }
}
