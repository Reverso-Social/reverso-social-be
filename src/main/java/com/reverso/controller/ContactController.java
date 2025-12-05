package com.reverso.controller;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.response.ContactResponse;
import com.reverso.service.interfaces.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // CREATE
    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody ContactCreateRequest dto) {
        return ResponseEntity.ok(contactService.create(dto));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAll() {
        return ResponseEntity.ok(contactService.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(contactService.getById(id));
    }

    // UPDATE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<ContactResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(contactService.updateStatus(id, status));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
