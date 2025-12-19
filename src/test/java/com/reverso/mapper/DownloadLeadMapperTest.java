package com.reverso.mapper;

import com.reverso.dto.response.DownloadLeadResponse;
import com.reverso.model.DownloadLead;
import com.reverso.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DownloadLeadMapperTest {

    private DownloadLeadMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DownloadLeadMapperImpl();
    }

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {
        UUID leadId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        Resource resource = Resource.builder()
                .id(resourceId)
                .title("Ebook Reverso")
                .build();

        LocalDateTime now = LocalDateTime.now();

        DownloadLead lead = DownloadLead.builder()
                .id(leadId)
                .name("Laura")
                .email("laura@test.com")
                .resource(resource)
                .createdAt(now)
                .lastDownloadedAt(now)
                .downloadCount(3)
                .build();

        DownloadLeadResponse response = mapper.toResponse(lead);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(leadId);
        assertThat(response.getName()).isEqualTo("Laura");
        assertThat(response.getEmail()).isEqualTo("laura@test.com");
        assertThat(response.getDownloadCount()).isEqualTo(3);
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getLastDownloadedAt()).isEqualTo(now);
        assertThat(response.getResourceId()).isEqualTo(resourceId);
        assertThat(response.getResourceTitle()).isEqualTo("Ebook Reverso");
    }

    @Test
    void toResponse_shouldReturnNull_whenInputIsNull() {
        DownloadLeadResponse response = mapper.toResponse(null);
        assertThat(response).isNull();
    }

    @Test
    void toResponse_shouldHandleNullResourceGracefully() {
        DownloadLead lead = DownloadLead.builder()
                .name("Laura")
                .email("laura@test.com")
                .build();

        DownloadLeadResponse response = mapper.toResponse(lead);

        assertThat(response).isNotNull();
        assertThat(response.getResourceId()).isNull();
        assertThat(response.getResourceTitle()).isNull();
    }
}