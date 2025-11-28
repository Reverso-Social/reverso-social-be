package com.reverso.service;

import com.reverso.dto.ResourceCreateDto;
import com.reverso.dto.ResourceDto;
import com.reverso.dto.ResourceUpdateDto;

import java.util.List;

public interface ResourceService {

    ResourceDto create(ResourceCreateDto dto);
    List<ResourceDto> getAll();
    List<ResourceDto> getPublic();
    List<ResourceDto> getByType(String type);
    ResourceDto getById(Long id);
    ResourceDto update(Long id, ResourceUpdateDto dto);
    void delete(Long id);
}
