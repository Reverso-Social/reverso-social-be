package com.reverso.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DownloadLeadTest {

    @Test
    void builder_shouldCreateDownloadLeadWithGivenValues() {
        Resource resource = Resource.builder().id(java.util.UUID.randomUUID()).build();

        DownloadLead lead = DownloadLead.builder()
                .name("John Doe")
                .email("john@example.com")
                .resource(resource)
                .downloadCount(5)
                .build();

        assertThat(lead.getName()).isEqualTo("John Doe");
        assertThat(lead.getEmail()).isEqualTo("john@example.com");
        assertThat(lead.getResource()).isEqualTo(resource);
        assertThat(lead.getDownloadCount()).isEqualTo(5);
    }

    @Test
    void onCreate_shouldSetCreatedAtLastDownloadedAtAndDownloadCount() {
        DownloadLead lead = new DownloadLead();
        lead.onCreate();

        assertThat(lead.getCreatedAt()).isNotNull();
        assertThat(lead.getLastDownloadedAt()).isNotNull();
        assertThat(lead.getDownloadCount()).isEqualTo(1);
    }
}