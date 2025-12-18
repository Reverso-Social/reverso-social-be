
package com.reverso.service;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.dto.response.BlogPostResponse;
import com.reverso.exception.ResourceNotFoundException;
import com.reverso.mapper.BlogPostMapper;
import com.reverso.model.BlogPost;
import com.reverso.model.enums.BlogPostStatus;
import com.reverso.repository.BlogPostRepository;
import com.reverso.service.impl.BlogPostServiceImpl;
import com.reverso.service.interfaces.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlogPostService - Tests Unitarios")
@SuppressWarnings("null")
class BlogPostServiceTest {

    @Mock
    private BlogPostRepository repository;

    @Mock
    private BlogPostMapper mapper;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    private BlogPost validBlogPost;
    private BlogPostCreateRequest validCreateRequest;
    private BlogPostResponse validResponse;
    private MockMultipartFile validImage;

    @BeforeEach
    void setUp() {
        validBlogPost = TestDataFactory.createValidBlogPost();
        validCreateRequest = TestDataFactory.createValidBlogPostRequest();
        validImage = TestDataFactory.createValidImageFile();
        
        validResponse = BlogPostResponse.builder()
                .id(validBlogPost.getId())
                .title(validBlogPost.getTitle())
                .subtitle(validBlogPost.getSubtitle())
                .slug(validBlogPost.getSlug())
                .content(validBlogPost.getContent())
                .category(validBlogPost.getCategory())
                .status(validBlogPost.getStatus().name())
                .coverImageUrl(validBlogPost.getCoverImageUrl())
                .createdAt(validBlogPost.getCreatedAt())
                .updatedAt(validBlogPost.getUpdatedAt())
                .build();
    }


    @Test
    @DisplayName("Crear blog post sin imagen - Success")
    void testCreate_WithoutImage_Success() {

        when(mapper.toEntity(any(BlogPostCreateRequest.class))).thenReturn(validBlogPost);
        when(repository.save(any(BlogPost.class))).thenReturn(validBlogPost);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);
        when(repository.findBySlug(anyString())).thenReturn(Optional.empty());

        BlogPostResponse result = blogPostService.createBlogPost(validCreateRequest, null);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(validCreateRequest.getTitle());
        verify(repository, times(1)).save(any(BlogPost.class));
        verify(fileStorageService, never()).store(any(), any());
    }

    @Test
    @DisplayName("Crear blog post con imagen válida - Success")
    void testCreate_WithValidImage_Success() {

        String imageUrl = "http://localhost/images/blog/test.jpg";
        when(mapper.toEntity(any(BlogPostCreateRequest.class))).thenReturn(validBlogPost);
        when(repository.save(any(BlogPost.class))).thenReturn(validBlogPost);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);
        when(repository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(fileStorageService.store(any(), eq("blog"))).thenReturn(imageUrl);

        BlogPostResponse result = blogPostService.createBlogPost(validCreateRequest, validImage);

        assertThat(result).isNotNull();
        verify(fileStorageService, times(1)).store(validImage, "blog");
        verify(repository, times(1)).save(any(BlogPost.class));
    }

   

    @Test
    @DisplayName("Generar slug único cuando ya existe - Success")
    void testCreate_DuplicateSlug_GeneratesUniqueSlug() {

        when(mapper.toEntity(any(BlogPostCreateRequest.class))).thenReturn(validBlogPost);
        when(repository.save(any(BlogPost.class))).thenReturn(validBlogPost);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);
        
        when(repository.findBySlug("mi-primer-post-de-blog"))
                .thenReturn(Optional.of(validBlogPost));
        when(repository.findBySlug("mi-primer-post-de-blog-1"))
                .thenReturn(Optional.empty());

        BlogPostResponse result = blogPostService.createBlogPost(validCreateRequest, null);

        assertThat(result).isNotNull();
        verify(repository, atLeast(2)).findBySlug(anyString());
    }

    @Test
    @DisplayName("Actualizar blog post existente - Success")
    void testUpdate_ExistingPost_Success() {

        UUID id = validBlogPost.getId();
        BlogPostUpdateRequest updateRequest = TestDataFactory.createValidBlogPostUpdateRequest();
        
        when(repository.findById(id)).thenReturn(Optional.of(validBlogPost));
        when(repository.save(any(BlogPost.class))).thenReturn(validBlogPost);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);
        when(repository.findBySlug(anyString())).thenReturn(Optional.empty());
        doNothing().when(mapper).updateEntityFromDto(any(), any());

        BlogPostResponse result = blogPostService.update(id, updateRequest, null);

        assertThat(result).isNotNull();
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any(BlogPost.class));
    }

    @Test
    @DisplayName("Actualizar blog post inexistente - Throws Exception")
    void testUpdate_NonExistingPost_ThrowsException() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();
        BlogPostUpdateRequest updateRequest = TestDataFactory.createValidBlogPostUpdateRequest();
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> blogPostService.update(nonExistingId, updateRequest, null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("BlogPost not found");
    }

    @Test
    @DisplayName("Actualizar imagen de blog post - Success")
    void testUpdate_WithNewImage_ReplacesOldImage() {
        // Arrange
        UUID id = validBlogPost.getId();
        validBlogPost.setCoverImageUrl("http://localhost/old-image.jpg");
        BlogPostUpdateRequest updateRequest = TestDataFactory.createValidBlogPostUpdateRequest();
        String newImageUrl = "http://localhost/new-image.jpg";
        
        when(repository.findById(id)).thenReturn(Optional.of(validBlogPost));
        when(repository.save(any(BlogPost.class))).thenReturn(validBlogPost);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);
        when(fileStorageService.store(any(), eq("blog"))).thenReturn(newImageUrl);
        when(repository.findBySlug(anyString())).thenReturn(Optional.empty());
        doNothing().when(fileStorageService).delete(anyString());
        doNothing().when(mapper).updateEntityFromDto(any(), any());

        // Act
        BlogPostResponse result = blogPostService.update(id, updateRequest, validImage);

        // Assert
        assertThat(result).isNotNull();
        verify(fileStorageService, times(1)).delete("http://localhost/old-image.jpg");
        verify(fileStorageService, times(1)).store(validImage, "blog");
    }

    // ========== FIND BY ID TESTS ==========

    @Test
    @DisplayName("Buscar por ID existente - Success")
    void testFindById_ExistingId_ReturnsPost() {
        // Arrange
        UUID id = validBlogPost.getId();
        when(repository.findById(id)).thenReturn(Optional.of(validBlogPost));
        when(mapper.toResponse(validBlogPost)).thenReturn(validResponse);

        // Act
        BlogPostResponse result = blogPostService.findById(id);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Buscar por ID inexistente - Throws Exception")
    void testFindById_NonExistingId_ThrowsException() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> blogPostService.findById(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("BlogPost not found");
    }

    // ========== FIND BY SLUG TESTS ==========

    @Test
    @DisplayName("Buscar por slug existente - Success")
    void testFindBySlug_ExistingSlug_ReturnsPost() {
        // Arrange
        String slug = "mi-primer-post-de-blog";
        when(repository.findBySlug(slug)).thenReturn(Optional.of(validBlogPost));
        when(mapper.toResponse(validBlogPost)).thenReturn(validResponse);

        // Act
        BlogPostResponse result = blogPostService.findBySlug(slug);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getSlug()).isEqualTo(slug);
        verify(repository, times(1)).findBySlug(slug);
    }

    @Test
    @DisplayName("Buscar por slug inexistente - Throws Exception")
    void testFindBySlug_NonExistingSlug_ThrowsException() {
        // Arrange
        String nonExistingSlug = "slug-inexistente";
        when(repository.findBySlug(nonExistingSlug)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> blogPostService.findBySlug(nonExistingSlug))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("BlogPost not found with slug");
    }

    // ========== FIND ALL TESTS ==========

    @Test
    @DisplayName("Buscar todos sin filtros - Success")
    void testFindAll_NoFilters_ReturnsAllPosts() {
        // Arrange
        List<BlogPost> posts = Arrays.asList(
                TestDataFactory.createValidBlogPost(),
                TestDataFactory.createValidBlogPost()
        );
        when(repository.findAll()).thenReturn(posts);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);

        // Act
        List<BlogPostResponse> result = blogPostService.findAll(null, null);

        // Assert
        assertThat(result).hasSize(2);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Buscar por status PUBLISHED - Success")
    void testFindAll_FilterByStatus_ReturnsFilteredPosts() {
        // Arrange
        List<BlogPost> posts = Arrays.asList(
                TestDataFactory.createBlogPostWithStatus(BlogPostStatus.PUBLISHED)
        );
        when(repository.findByStatus(BlogPostStatus.PUBLISHED)).thenReturn(posts);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);

        // Act
        List<BlogPostResponse> result = blogPostService.findAll("PUBLISHED", null);

        // Assert
        assertThat(result).hasSize(1);
        verify(repository, times(1)).findByStatus(BlogPostStatus.PUBLISHED);
    }

    @Test
    @DisplayName("Buscar por categoría - Success")
    void testFindAll_FilterByCategory_ReturnsFilteredPosts() {
        // Arrange
        String category = "Tecnología";
        List<BlogPost> posts = Arrays.asList(
                TestDataFactory.createBlogPostWithCategory(category)
        );
        when(repository.findByCategoryIgnoreCase(category)).thenReturn(posts);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);

        // Act
        List<BlogPostResponse> result = blogPostService.findAll(null, category);

        // Assert
        assertThat(result).hasSize(1);
        verify(repository, times(1)).findByCategoryIgnoreCase(category);
    }

    @Test
    @DisplayName("Buscar por status y categoría - Success")
    void testFindAll_FilterByStatusAndCategory_ReturnsFilteredPosts() {
        // Arrange
        String category = "Tecnología";
        List<BlogPost> posts = Arrays.asList(validBlogPost);
        when(repository.findByStatusAndCategoryIgnoreCase(BlogPostStatus.PUBLISHED, category))
                .thenReturn(posts);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);

        // Act
        List<BlogPostResponse> result = blogPostService.findAll("PUBLISHED", category);

        // Assert
        assertThat(result).hasSize(1);
        verify(repository, times(1)).findByStatusAndCategoryIgnoreCase(BlogPostStatus.PUBLISHED, category);
    }

    // ========== FIND LATEST PUBLISHED TESTS ==========

    @Test
    @DisplayName("Buscar últimos 5 publicados - Success")
    void testFindLatestPublished_ReturnsLatestPosts() {
        // Arrange
        List<BlogPost> posts = Arrays.asList(
                TestDataFactory.createBlogPostWithStatus(BlogPostStatus.PUBLISHED),
                TestDataFactory.createBlogPostWithStatus(BlogPostStatus.PUBLISHED)
        );
        when(repository.findTop5ByStatusOrderByPublishedAtDesc(BlogPostStatus.PUBLISHED))
                .thenReturn(posts);
        when(mapper.toResponse(any(BlogPost.class))).thenReturn(validResponse);

        // Act
        List<BlogPostResponse> result = blogPostService.findLatestPublished(5);

        // Assert
        assertThat(result).hasSize(2);
        verify(repository, times(1)).findTop5ByStatusOrderByPublishedAtDesc(BlogPostStatus.PUBLISHED);
    }

    // ========== DELETE TESTS ==========

    @Test
    @DisplayName("Eliminar post existente - Success")
    void testDelete_ExistingPost_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        // Act
        blogPostService.delete(id);

        // Assert
        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar post inexistente - Throws Exception")
    void testDelete_NonExistingPost_ThrowsException() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();
        when(repository.existsById(nonExistingId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> blogPostService.delete(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("BlogPost not found");

        verify(repository, never()).deleteById(any());
    }

    // ========== UPLOAD IMAGE TESTS ==========

    @Test
    @DisplayName("Subir imagen a post existente - Success")
    void testUploadImage_ExistingPost_Success() {
        // Arrange
        UUID id = validBlogPost.getId();
        String imageUrl = "http://localhost/new-image.jpg";
        when(repository.findById(id)).thenReturn(Optional.of(validBlogPost));
        when(fileStorageService.store(any(), eq("blog"))).thenReturn(imageUrl);
        when(repository.save(any(BlogPost.class))).thenReturn(validBlogPost);

        // Act
        Map<String, String> result = blogPostService.uploadImage(id, validImage);

        // Assert
        assertThat(result).containsEntry("imageUrl", imageUrl);
        verify(fileStorageService, times(1)).store(validImage, "blog");
        verify(repository, times(1)).save(any(BlogPost.class));
    }

    @Test
    @DisplayName("Subir imagen reemplaza imagen anterior - Success")
    void testUploadImage_ReplacesOldImage_Success() {
        // Arrange
        UUID id = validBlogPost.getId();
        validBlogPost.setCoverImageUrl("http://localhost/old-image.jpg");
        String newImageUrl = "http://localhost/new-image.jpg";
        
        when(repository.findById(id)).thenReturn(Optional.of(validBlogPost));
        when(fileStorageService.store(any(), eq("blog"))).thenReturn(newImageUrl);
        when(repository.save(any(BlogPost.class))).thenReturn(validBlogPost);
        doNothing().when(fileStorageService).delete(anyString());

        // Act
        Map<String, String> result = blogPostService.uploadImage(id, validImage);

        // Assert
        assertThat(result).containsEntry("imageUrl", newImageUrl);
        verify(fileStorageService, times(1)).delete("http://localhost/old-image.jpg");
    }

    // ========== DELETE IMAGE TESTS ==========

    @Test
    @DisplayName("Eliminar imagen de post - Success")
    void testDeleteImage_PostWithImage_Success() {
        // Arrange
        UUID id = validBlogPost.getId();
        validBlogPost.setCoverImageUrl("http://localhost/image.jpg");
        when(repository.findById(id)).thenReturn(Optional.of(validBlogPost));
        when(repository.save(any(BlogPost.class))).thenReturn(validBlogPost);
        doNothing().when(fileStorageService).delete(anyString());

        // Act
        blogPostService.deleteImage(id);

        // Assert
        verify(fileStorageService, times(1)).delete("http://localhost/image.jpg");
        verify(repository, times(1)).save(any(BlogPost.class));
    }

    @Test
    @DisplayName("Eliminar imagen de post sin imagen - No hace nada")
    void testDeleteImage_PostWithoutImage_DoesNothing() {
        // Arrange
        UUID id = validBlogPost.getId();
        validBlogPost.setCoverImageUrl(null);
        when(repository.findById(id)).thenReturn(Optional.of(validBlogPost));

        // Act
        blogPostService.deleteImage(id);

        // Assert
        verify(fileStorageService, never()).delete(anyString());
        verify(repository, never()).save(any());
    }
}