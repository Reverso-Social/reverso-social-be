package com.reverso.controller;

import com.reverso.dto.ContactCreateDto;
import com.reverso.dto.ContactDto;
import com.reverso.service.interfaces.ContactService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;

    @PostMapping
    public ContactDto create(@RequestBody ContactCreateDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ContactDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ContactDto get(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}/status")
    public ContactDto updateStatus(@PathVariable UUID id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}