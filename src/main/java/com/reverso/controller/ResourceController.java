package com.reverso.controller;

import com.reverso.dto.ResourceCreateDto;
import com.reverso.dto.ResourceDto;
import com.reverso.dto.ResourceUpdateDto;
import com.reverso.service.interfaces.ResourceService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @PostMapping
    public ResourceDto create(@RequestBody ResourceCreateDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ResourceDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/public")
    public List<ResourceDto> getPublic() {
        return service.getPublic();
    }

    @GetMapping("/type/{type}")
    public List<ResourceDto> getByType(@PathVariable String type) {
        return service.getByType(type.toUpperCase());
    }

    @GetMapping("/{id}")
    public ResourceDto get(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public ResourceDto update(@PathVariable UUID id, @RequestBody ResourceUpdateDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}