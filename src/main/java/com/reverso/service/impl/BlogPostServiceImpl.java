package com.reverso.service.impl;

import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.dto.response.BlogPostResponse;
import com.reverso.exception.ResourceNotFoundException;
import com.reverso.mapper.BlogPostMapper;
import com.reverso.model.BlogPost;
import com.reverso.model.enums.BlogPostStatus;
import com.reverso.repository.BlogPostRepository;
import com.reverso.service.interfaces.BlogPostService;
import com.reverso.service.interfaces.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;
    private final FileStorageService fileStorageService;

    public BlogPostServiceImpl(
            BlogPostRepository blogPostRepository,
            BlogPostMapper blogPostMapper,
            FileStorageService fileStorageService
    ) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public BlogPostResponse createBlogPost(BlogPostCreateRequest request, MultipartFile image) {

        BlogPost entity = blogPostMapper.toEntity(request);

        entity.setSlug(generateSlug(request.getTitle()));

        if (image != null && !image.isEmpty()) {
            validateImage(image);
            String url = fileStorageService.store(image, "blog");
            entity.setCoverImageUrl(url);
        }

        blogPostRepository.save(entity);
        return blogPostMapper.toResponse(entity);
    }

    @Override
    public BlogPostResponse update(UUID id, BlogPostUpdateRequest request, MultipartFile image) {

        BlogPost entity = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost not found: " + id));

        blogPostMapper.updateEntityFromDto(request, entity);

        if (request.getTitle() != null) {
            entity.setSlug(generateSlug(request.getTitle()));
        }

        if (image != null && !image.isEmpty()) {
            validateImage(image);

            if (entity.getCoverImageUrl() != null) {
                fileStorageService.delete(entity.getCoverImageUrl());
            }

            String url = fileStorageService.store(image, "blog");
            entity.setCoverImageUrl(url);
        }

        blogPostRepository.save(entity);
        return blogPostMapper.toResponse(entity);
    }

    @Override
    public BlogPostResponse findById(UUID id) {
        return blogPostRepository.findById(id)
                .map(blogPostMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost not found: " + id));
    }

    @Override
    public BlogPostResponse findBySlug(String slug) {
        BlogPost entity = blogPostRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost not found with slug: " + slug));

        return blogPostMapper.toResponse(entity);
    }

    @Override
    public List<BlogPostResponse> findAll(String status, String category) {

        List<BlogPost> posts;

        if (status != null && category != null) {
            posts = blogPostRepository.findByStatusAndCategoryIgnoreCase(
                    BlogPostStatus.valueOf(status.toUpperCase()),
                    category
            );
        } else if (status != null) {
            posts = blogPostRepository.findByStatus(
                    BlogPostStatus.valueOf(status.toUpperCase())
            );
        } else if (category != null) {
            posts = blogPostRepository.findByCategoryIgnoreCase(category);
        } else {
            posts = blogPostRepository.findAll();
        }

        return posts.stream().map(blogPostMapper::toResponse).toList();
    }

    @Override
    public List<BlogPostResponse> findLatestPublished(int limit) {
        List<BlogPost> posts =
                blogPostRepository.findTop5ByStatusOrderByPublishedAtDesc(
                        BlogPostStatus.PUBLISHED
                );

        return posts.stream()
                .map(blogPostMapper::toResponse)
                .limit(limit)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!blogPostRepository.existsById(id)) {
            throw new ResourceNotFoundException("BlogPost not found: " + id);
        }
        blogPostRepository.deleteById(id);
    }
    @Override
    public Map<String, String> uploadImage(UUID id, MultipartFile file) {

        BlogPost blog = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost not found: " + id));

        validateImage(file);

        if (blog.getCoverImageUrl() != null) {
            fileStorageService.delete(blog.getCoverImageUrl());
        }

        String url = fileStorageService.store(file, "blog");

        blog.setCoverImageUrl(url);
        blogPostRepository.save(blog);

        return Map.of("imageUrl", url);
    }

    @Override
    public void deleteImage(UUID id) {

        BlogPost blog = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost not found: " + id));

        if (blog.getCoverImageUrl() != null) {
            fileStorageService.delete(blog.getCoverImageUrl());
            blog.setCoverImageUrl(null);
            blogPostRepository.save(blog);
        }
    }

    private void validateImage(MultipartFile image) {
        String type = image.getContentType();

        if (!List.of("image/jpeg", "image/png", "image/webp").contains(type)) {
            throw new IllegalArgumentException("Formato de imagen no permitido");
        }

        if (image.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("La imagen supera el tamaño máximo (5MB)");
        }
    }
    private String generateSlug(String title) {
        String normalized = Normalizer.normalize(
                title.toLowerCase(),
                Normalizer.Form.NFD
        ).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return normalized
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-");
    }
}
