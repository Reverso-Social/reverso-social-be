package com.reverso.controller;

import com.reverso.dto.request.ResourceDownloadRequest;
import com.reverso.dto.response.ResourceDownloadResponse;
import com.reverso.service.interfaces.ResourceDownloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resource-downloads")
@RequiredArgsConstructor
public class ResourceDownloadController {

    private final ResourceDownloadService service;

    @PostMapping
    public ResponseEntity<ResourceDownloadResponse> createDownload(@Valid @RequestBody ResourceDownloadRequest request) {
        ResourceDownloadResponse download = service.createDownload(request);
        return new ResponseEntity<>(download, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ResourceDownloadResponse>> getAllDownloads() {
        return ResponseEntity.ok(service.getAllDownloads());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResourceDownloadResponse>> getDownloadsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getDownloadsByUser(userId));
    }


    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<List<ResourceDownloadResponse>> getDownloadsByResource(@PathVariable UUID resourceId) {
        return ResponseEntity.ok(service.getDownloadsByResource(resourceId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDownloadResponse> getDownloadById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getDownloadById(id));
    }


    @GetMapping("/resource/{resourceId}/count")
    public ResponseEntity<Long> countDownloadsByResource(@PathVariable UUID resourceId) {
        return ResponseEntity.ok(service.countDownloadsByResource(resourceId));
    }


    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserDownloadedResource(
            @RequestParam UUID userId,
            @RequestParam UUID resourceId) {
        return ResponseEntity.ok(service.hasUserDownloadedResource(userId, resourceId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDownload(@PathVariable UUID id) {
        service.deleteDownload(id);
        return ResponseEntity.noContent().build();
    }
}