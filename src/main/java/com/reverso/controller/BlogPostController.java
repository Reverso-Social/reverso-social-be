package com.reverso.controller;

import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.dto.response.BlogPostResponse;
import com.reverso.service.interfaces.BlogPostService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostController {

    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

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
    public void delete(@PathVariable UUID id) {
        blogPostService.delete(id);
    }
}
