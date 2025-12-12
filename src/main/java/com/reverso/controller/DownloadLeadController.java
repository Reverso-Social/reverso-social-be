package com.reverso.controller;

import com.reverso.dto.request.DownloadLeadRequest;
import com.reverso.dto.response.DownloadLeadResponse;
import com.reverso.service.interfaces.DownloadLeadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/download-leads")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DownloadLeadController {

    private final DownloadLeadService service;

    @PostMapping
    public ResponseEntity<DownloadLeadResponse> createLead(@Valid @RequestBody DownloadLeadRequest request) {
        DownloadLeadResponse lead = service.createLead(request);
        return new ResponseEntity<>(lead, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DownloadLeadResponse>> getAllLeads() {
        return ResponseEntity.ok(service.getAllLeads());
    }

    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<List<DownloadLeadResponse>> getLeadsByResource(@PathVariable UUID resourceId) {
        return ResponseEntity.ok(service.getLeadsByResource(resourceId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DownloadLeadResponse> getLeadById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getLeadById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable UUID id) {
        service.deleteLead(id);
        return ResponseEntity.noContent().build();
    }
}