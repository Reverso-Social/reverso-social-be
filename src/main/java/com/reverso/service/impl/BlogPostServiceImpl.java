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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;

    // Inyección opcional hasta que exista la implementación real
    @Autowired(required = false)
    private FileStorageService fileStorageService;

    public BlogPostServiceImpl(
            BlogPostRepository blogPostRepository,
            BlogPostMapper blogPostMapper
    ) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
    }

    @Override
    public BlogPostResponse create(BlogPostCreateRequest request, MultipartFile image) {

        BlogPost entity = blogPostMapper.toEntity(request);

        entity.setSlug(generateSlug(request.getTitle()));

        if (image != null && !image.isEmpty() && fileStorageService != null) {
            String path = fileStorageService.saveBlogImage(image);
            entity.setCoverImagePath(path);
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

        if (image != null && !image.isEmpty() && fileStorageService != null) {
            String path = fileStorageService.saveBlogImage(image);
            entity.setCoverImagePath(path);
        }

        blogPostRepository.save(entity);
        return blogPostMapper.toResponse(entity);
    }

    @Override
    public BlogPostResponse findById(UUID id) {
        BlogPost entity = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost not found: " + id));
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

    private String generateSlug(String title) {
        String normalized = Normalizer.normalize(
                        title.toLowerCase(),
                        Normalizer.Form.NFD
                )
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return normalized
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-");
    }
}
