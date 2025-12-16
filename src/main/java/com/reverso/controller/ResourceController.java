package com.reverso.controller;

import com.reverso.dto.request.ResourceCreateRequest;
import com.reverso.dto.request.ResourceUpdateRequest;
import com.reverso.dto.response.ResourceResponse;
import com.reverso.exception.BadRequestException;
import com.reverso.service.interfaces.FileStorageService;
import com.reverso.service.interfaces.ResourceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<ResourceResponse> create(@Valid @RequestBody ResourceCreateRequest dto) {
        ResourceResponse resource = service.create(dto);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/public")
    public ResponseEntity<List<ResourceResponse>> getPublic() {
        return ResponseEntity.ok(service.getPublic());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ResourceResponse>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(service.getByType(type.toUpperCase()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResourceResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ResourceUpdateRequest dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------------
    // UPLOAD DE ARCHIVOS (PDF/IMG)
    // -----------------------------
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> uploadResourceFile(
            @RequestParam("file") MultipartFile file
    ) {

        String contentType = file.getContentType();

        if (!List.of(
                "application/pdf",
                "image/jpeg",
                "image/png",
                "image/webp"
        ).contains(contentType)) {
            throw new BadRequestException("Formato no permitido");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BadRequestException("El archivo supera el tamaño máximo (5MB)");
        }

        String fileUrl = fileStorageService.store(file, "resources");

        return ResponseEntity.ok(Map.of("fileUrl", fileUrl));
    }
}
