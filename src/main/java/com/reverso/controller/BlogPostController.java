package com.reverso.controller;

import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.dto.response.BlogPostResponse;
import com.reverso.service.interfaces.BlogPostService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/blogposts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BlogPostResponse create(
            @RequestPart("data") BlogPostCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return blogPostService.create(request, image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BlogPostResponse update(
            @PathVariable UUID id,
            @RequestPart("data") BlogPostUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return blogPostService.update(id, request, image);
    }

    @GetMapping("/{id}")
    public BlogPostResponse findById(@PathVariable UUID id) {
        return blogPostService.findById(id);
    }

    @GetMapping("/slug/{slug}")
    public BlogPostResponse findBySlug(@PathVariable String slug) {
        return blogPostService.findBySlug(slug);
    }

    @GetMapping
    public List<BlogPostResponse> findAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category
    ) {
        return blogPostService.findAll(status, category);
    }

    @GetMapping("/latest")
    public List<BlogPostResponse> findLatest(@RequestParam(defaultValue = "5") int limit) {
        return blogPostService.findLatestPublished(limit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        blogPostService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------------
    // SUBIR IMAGEN DE PORTADA
    // -----------------------------
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {
        return blogPostService.uploadImage(id, file);
    }

    // -----------------------------
    // BORRAR IMAGEN DE PORTADA
    // -----------------------------
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        blogPostService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
