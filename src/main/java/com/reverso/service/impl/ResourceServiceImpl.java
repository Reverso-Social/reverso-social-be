package com.reverso.service.impl;

import com.reverso.dto.request.ResourceCreateRequest;
import com.reverso.dto.request.ResourceUpdateRequest;
import com.reverso.dto.response.ResourceResponse;
import com.reverso.mapper.ResourceMapper;
import com.reverso.model.Resource;
import com.reverso.repository.ResourceRepository;
import com.reverso.service.interfaces.ResourceService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    @Override
    public ResourceResponse create(ResourceCreateRequest dto) {
        Resource resource = mapper.toEntity(dto);
        repository.save(resource);
        return mapper.toResponse(resource);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getPublic() {
        return repository.findByIsPublicTrue()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getByType(String type) {
        return repository.findByType(type)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceResponse getById(UUID id) {
        Resource resource = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
        return mapper.toResponse(resource);
    }

    @Override
    public ResourceResponse update(UUID id, ResourceUpdateRequest dto) {
        Resource resource = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));

        mapper.updateFromRequest(dto, resource);
        repository.save(resource);

        return mapper.toResponse(resource);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}