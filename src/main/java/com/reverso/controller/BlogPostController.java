package com.reverso.controller;
import java.util.Map;
import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.dto.response.BlogPostResponse;
import com.reverso.service.interfaces.BlogPostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blogposts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BlogPostResponse> createBlogPost(
            @Valid @RequestPart("data") BlogPostCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        BlogPostResponse response = blogPostService.createBlogPost(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BlogPostResponse> updateBlogPost(
            @PathVariable UUID id,
            @Valid @RequestPart("data") BlogPostUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        BlogPostResponse response = blogPostService.update(id, request, image);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogPostResponse> findById(@PathVariable UUID id) {
        BlogPostResponse response = blogPostService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/slug/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlogPostResponse> findBySlug(@PathVariable String slug) {
        BlogPostResponse response = blogPostService.findBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BlogPostResponse>> findAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category
    ) {
        List<BlogPostResponse> responses = blogPostService.findAll(status, category);
        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BlogPostResponse>> findLatest(
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<BlogPostResponse> responses = blogPostService.findLatestPublished(limit);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        blogPostService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
