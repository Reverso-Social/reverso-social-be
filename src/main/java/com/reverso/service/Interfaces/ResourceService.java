package com.reverso.service.interfaces;

import com.reverso.dto.ResourceCreateDto;
import com.reverso.dto.ResourceDto;
import com.reverso.dto.ResourceUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ResourceService {
    ResourceDto create(ResourceCreateDto dto);
    List<ResourceDto> getAll();
    List<ResourceDto> getPublic();
    List<ResourceDto> getByType(String type);
    ResourceDto getById(UUID id);
    ResourceDto update(UUID id, ResourceUpdateDto dto);
    void delete(UUID id);
}