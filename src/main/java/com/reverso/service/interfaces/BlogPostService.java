package com.reverso.service.interfaces;

import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.dto.response.BlogPostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BlogPostService {

    BlogPostResponse create(BlogPostCreateRequest request, MultipartFile image);

    BlogPostResponse update(UUID id, BlogPostUpdateRequest request, MultipartFile image);

    BlogPostResponse findById(UUID id);

    BlogPostResponse findBySlug(String slug);

    List<BlogPostResponse> findAll(String status, String category);

    List<BlogPostResponse> findLatestPublished(int limit);

    void delete(UUID id);

    // NUEVOS
    Map<String, String> uploadImage(UUID id, MultipartFile file);

    void deleteImage(UUID id);
}
