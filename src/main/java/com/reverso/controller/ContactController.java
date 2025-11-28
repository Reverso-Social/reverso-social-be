package com.reverso.controller;

import com.reverso.dto.ContactCreateDto;
import com.reverso.dto.ContactDto;
import com.reverso.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ContactDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}/status")
    public ContactDto updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
