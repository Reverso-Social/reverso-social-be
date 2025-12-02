package com.reverso.service.impl;

import com.reverso.dto.ResourceCreateDto;
import com.reverso.dto.ResourceDto;
import com.reverso.dto.ResourceUpdateDto;
import com.reverso.mapper.ResourceMapper;
import com.reverso.model.Resource;
import com.reverso.repository.ResourceRepository;
import com.reverso.service.interfaces.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    @Override
    public ResourceDto create(ResourceCreateDto dto) {
        Resource resource = mapper.toEntity(dto);
        repository.save(resource);
        return mapper.toDto(resource);
    }

    @Override
    public List<ResourceDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ResourceDto> getPublic() {
        return repository.findByIsPublicTrue()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ResourceDto> getByType(String type) {
        return repository.findByType(type)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ResourceDto getById(UUID id) {
        Resource resource = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        return mapper.toDto(resource);
    }

    @Override
    public ResourceDto update(UUID id, ResourceUpdateDto dto) {
        Resource resource = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        mapper.updateEntityFromDto(dto, resource);
        repository.save(resource);

        return mapper.toDto(resource);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}