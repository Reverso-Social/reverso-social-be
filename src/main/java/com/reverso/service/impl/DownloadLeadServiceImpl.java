package com.reverso.service.impl;

import com.reverso.dto.request.DownloadLeadRequest;
import com.reverso.dto.response.DownloadLeadResponse;
import com.reverso.mapper.DownloadLeadMapper;
import com.reverso.model.DownloadLead;
import com.reverso.model.Resource;
import com.reverso.repository.DownloadLeadRepository;
import com.reverso.repository.ResourceRepository;
import com.reverso.service.interfaces.DownloadLeadService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DownloadLeadServiceImpl implements DownloadLeadService {

    private final DownloadLeadRepository leadRepository;
    private final ResourceRepository resourceRepository;
    private final DownloadLeadMapper mapper;

    @Override
    public DownloadLeadResponse createLead(DownloadLeadRequest request) {
        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + request.getResourceId()));

        Optional<DownloadLead> existingLead = leadRepository.findByEmailAndResourceId(request.getEmail(),
                request.getResourceId());

        DownloadLead lead;
        if (existingLead.isPresent()) {
            lead = existingLead.get();
            lead.setLastDownloadedAt(java.time.LocalDateTime.now());
            if (!lead.getName().equals(request.getName())) {
                lead.setName(request.getName());
            }
        } else {
            lead = DownloadLead.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .resource(resource)
                    .build();
        }

        DownloadLead savedLead = leadRepository.save(lead);
        return mapper.toResponse(savedLead);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DownloadLeadResponse> getAllLeads() {
        return leadRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DownloadLeadResponse> getLeadsByResource(UUID resourceId) {
        return leadRepository.findByResourceId(resourceId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DownloadLeadResponse getLeadById(UUID id) {
        DownloadLead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
        return mapper.toResponse(lead);
    }

    @Override
    public void deleteLead(UUID id) {
        if (!leadRepository.existsById(id)) {
            throw new RuntimeException("Lead not found with id: " + id);
        }
        leadRepository.deleteById(id);
    }
}