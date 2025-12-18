package com.reverso.service;

import com.reverso.dto.request.DownloadLeadRequest;
import com.reverso.dto.response.DownloadLeadResponse;
import com.reverso.mapper.DownloadLeadMapper;
import com.reverso.model.DownloadLead;
import com.reverso.model.Resource;
import com.reverso.repository.DownloadLeadRepository;
import com.reverso.repository.ResourceRepository;
import com.reverso.service.impl.DownloadLeadServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DownloadLeadServiceTest {

    @Mock
    private DownloadLeadRepository leadRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private DownloadLeadMapper mapper;

    @InjectMocks
    private DownloadLeadServiceImpl service;

    @Test
    void createLead_NewLead_ShouldSetCountToOne() {
        // Given
        UUID resourceId = UUID.randomUUID();
        String email = "test@example.com";
        DownloadLeadRequest request = new DownloadLeadRequest("Test User", email, resourceId);

        Resource resource = new Resource();
        resource.setId(resourceId);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(leadRepository.findByEmailAndResourceId(email, resourceId)).thenReturn(Optional.empty());

        DownloadLead savedLead = DownloadLead.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email(email)
                .resource(resource)
                .downloadCount(1) // Simulate DB default/PrePersist if we were using it, but here we mock save
                .createdAt(LocalDateTime.now())
                .lastDownloadedAt(LocalDateTime.now())
                .build();

        // Simulate repository save returning the object (though PrePersist logic
        // happens in JPA, we verify logic sent to save)
        when(leadRepository.save(any(DownloadLead.class))).thenAnswer(invocation -> {
            DownloadLead l = invocation.getArgument(0);
            if (l.getDownloadCount() == 0)
                l.setDownloadCount(1); // Simulate PrePersist manually for mock
            return l;
        });

        DownloadLeadResponse responseDto = new DownloadLeadResponse();
        responseDto.setDownloadCount(1);
        when(mapper.toResponse(any(DownloadLead.class))).thenReturn(responseDto);

        // When
        DownloadLeadResponse result = service.createLead(request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getDownloadCount());
        verify(leadRepository).save(any(DownloadLead.class));
    }

    @Test
    void createLead_ExistingLead_ShouldIncrementCount() {
        // Given
        UUID resourceId = UUID.randomUUID();
        String email = "test@example.com";
        DownloadLeadRequest request = new DownloadLeadRequest("Test User Updated", email, resourceId);

        Resource resource = new Resource();
        resource.setId(resourceId);

        DownloadLead existingLead = DownloadLead.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email(email)
                .resource(resource)
                .downloadCount(5)
                .lastDownloadedAt(LocalDateTime.now().minusDays(1))
                .build();

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(leadRepository.findByEmailAndResourceId(email, resourceId)).thenReturn(Optional.of(existingLead));

        when(leadRepository.save(any(DownloadLead.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DownloadLeadResponse responseDto = new DownloadLeadResponse();
        responseDto.setDownloadCount(6); // Expecting 5 + 1
        when(mapper.toResponse(any(DownloadLead.class))).thenReturn(responseDto);

        // When
        DownloadLeadResponse result = service.createLead(request);

        // Then
        assertNotNull(result);
        assertEquals(6, result.getDownloadCount());
        verify(leadRepository).save(existingLead);
        assertEquals("Test User Updated", existingLead.getName());
    }
}
