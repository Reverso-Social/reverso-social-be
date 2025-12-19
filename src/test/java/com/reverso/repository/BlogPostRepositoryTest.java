package com.reverso.repository;

import com.reverso.config.TestDataFactory;
import com.reverso.model.BlogPost;
import com.reverso.model.enums.BlogPostStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de integración para BlogPostRepository
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("BlogPostRepository - Tests de Integración")
@SuppressWarnings("null")
class BlogPostRepositoryTest {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Test
    @DisplayName("Guardar blog post - Success")
    void testSave_NewBlogPost_ReturnsSavedPost() {
        // Arrange
        BlogPost post = TestDataFactory.createValidBlogPost();
        post.setId(null);
        post.setSlug("test-post-" + System.currentTimeMillis()); // Slug único

        // Act
        BlogPost saved = blogPostRepository.save(post);

        // Assert
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(post.getTitle());
        assertThat(saved.getSlug()).isEqualTo(post.getSlug());
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Buscar por ID existente - Returns BlogPost")
    void testFindById_ExistingId_ReturnsPost() {
        // Arrange
        BlogPost post = TestDataFactory.createValidBlogPost();
        post.setId(null);
        post.setSlug("find-by-id-" + System.currentTimeMillis());
        BlogPost saved = blogPostRepository.save(post);

        // Act
        Optional<BlogPost> found = blogPostRepository.findById(saved.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getTitle()).isEqualTo(saved.getTitle());
    }

    @Test
    @DisplayName("Buscar por ID inexistente - Returns Empty")
    void testFindById_NonExistingId_ReturnsEmpty() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Act
        Optional<BlogPost> found = blogPostRepository.findById(nonExistingId);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Buscar por slug existente - Returns BlogPost")
    void testFindBySlug_ExistingSlug_ReturnsPost() {
        // Arrange
        BlogPost post = TestDataFactory.createValidBlogPost();
        post.setId(null);
        String uniqueSlug = "unique-slug-" + System.currentTimeMillis();
        post.setSlug(uniqueSlug);
        blogPostRepository.save(post);

        // Act
        Optional<BlogPost> found = blogPostRepository.findBySlug(uniqueSlug);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getSlug()).isEqualTo(uniqueSlug);
    }

    @Test
    @DisplayName("Buscar por slug inexistente - Returns Empty")
    void testFindBySlug_NonExistingSlug_ReturnsEmpty() {
        // Arrange
        String nonExistingSlug = "slug-inexistente-" + System.currentTimeMillis();

        // Act
        Optional<BlogPost> found = blogPostRepository.findBySlug(nonExistingSlug);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Buscar por status DRAFT - Returns filtered posts")
    void testFindByStatus_DraftStatus_ReturnsDraftPosts() {
        // Arrange
        BlogPost draftPost = TestDataFactory.createBlogPostWithStatus(BlogPostStatus.DRAFT);
        draftPost.setId(null);
        draftPost.setSlug("draft-post-" + System.currentTimeMillis());
        blogPostRepository.save(draftPost);

        BlogPost publishedPost = TestDataFactory.createBlogPostWithStatus(BlogPostStatus.PUBLISHED);
        publishedPost.setId(null);
        publishedPost.setSlug("published-post-" + System.currentTimeMillis());
        publishedPost.setPublishedAt(LocalDateTime.now());
        blogPostRepository.save(publishedPost);

        // Act
        List<BlogPost> draftPosts = blogPostRepository.findByStatus(BlogPostStatus.DRAFT);

        // Assert
        assertThat(draftPosts).isNotEmpty();
        assertThat(draftPosts).allMatch(post -> post.getStatus() == BlogPostStatus.DRAFT);
    }

    @Test
    @DisplayName("Buscar por status PUBLISHED - Returns published posts")
    void testFindByStatus_PublishedStatus_ReturnsPublishedPosts() {
        // Arrange
        BlogPost publishedPost = TestDataFactory.createBlogPostWithStatus(BlogPostStatus.PUBLISHED);
        publishedPost.setId(null);
        publishedPost.setSlug("published-" + System.currentTimeMillis());
        publishedPost.setPublishedAt(LocalDateTime.now());
        blogPostRepository.save(publishedPost);

        // Act
        List<BlogPost> publishedPosts = blogPostRepository.findByStatus(BlogPostStatus.PUBLISHED);

        // Assert
        assertThat(publishedPosts).isNotEmpty();
        assertThat(publishedPosts).allMatch(post -> post.getStatus() == BlogPostStatus.PUBLISHED);
    }

    @Test
    @DisplayName("Buscar por categoría - Returns posts with category")
    void testFindByCategoryIgnoreCase_ExistingCategory_ReturnsPosts() {
        // Arrange
        String category = "Tecnología";
        BlogPost post1 = TestDataFactory.createBlogPostWithCategory(category);
        post1.setId(null);
        post1.setSlug("tech-post-1-" + System.currentTimeMillis());
        blogPostRepository.save(post1);

        BlogPost post2 = TestDataFactory.createBlogPostWithCategory("Diseño");
        post2.setId(null);
        post2.setSlug("design-post-" + System.currentTimeMillis());
        blogPostRepository.save(post2);

        // Act
        List<BlogPost> techPosts = blogPostRepository.findByCategoryIgnoreCase(category);

        // Assert
        assertThat(techPosts).isNotEmpty();
        assertThat(techPosts).allMatch(post -> 
            post.getCategory().equalsIgnoreCase(category));
    }

    @Test
    @DisplayName("Buscar por categoría ignora mayúsculas - Returns posts")
    void testFindByCategoryIgnoreCase_DifferentCase_ReturnsPosts() {
        // Arrange
        BlogPost post = TestDataFactory.createBlogPostWithCategory("Tecnología");
        post.setId(null);
        post.setSlug("tech-ignore-case-" + System.currentTimeMillis());
        blogPostRepository.save(post);

        // Act - Buscar con diferente case
        List<BlogPost> posts = blogPostRepository.findByCategoryIgnoreCase("TECNOLOGÍA");

        // Assert
        assertThat(posts).isNotEmpty();
    }

    @Test
    @DisplayName("Buscar por status y categoría - Returns filtered posts")
    void testFindByStatusAndCategory_ReturnsFilteredPosts() {
        // Arrange
        BlogPost post = BlogPost.builder()
                .title("Post filtrado")
                .subtitle("Subtítulo")
                .slug("filtered-post-" + System.currentTimeMillis())
                .content("Contenido del post")
                .category("Tecnología")
                .status(BlogPostStatus.PUBLISHED)
                .publishedAt(LocalDateTime.now())
                .build();
        blogPostRepository.save(post);

        // Act
        List<BlogPost> filtered = blogPostRepository.findByStatusAndCategoryIgnoreCase(
                BlogPostStatus.PUBLISHED, "Tecnología");

        // Assert
        assertThat(filtered).isNotEmpty();
        assertThat(filtered).allMatch(p -> 
            p.getStatus() == BlogPostStatus.PUBLISHED && 
            p.getCategory().equalsIgnoreCase("Tecnología"));
    }

    @Test
    @DisplayName("Buscar top 5 publicados ordenados por fecha - Returns latest posts")
    void testFindTop5ByStatusOrderByPublishedAtDesc_ReturnsLatestPublished() {
        // Arrange - Crear varios posts publicados
        for (int i = 0; i < 7; i++) {
            BlogPost post = TestDataFactory.createBlogPostWithStatus(BlogPostStatus.PUBLISHED);
            post.setId(null);
            post.setSlug("latest-post-" + i + "-" + System.currentTimeMillis());
            post.setPublishedAt(LocalDateTime.now().minusDays(i));
            blogPostRepository.save(post);
            
            // Pequeña pausa para asegurar diferentes timestamps
            try { Thread.sleep(10); } catch (InterruptedException e) {}
        }

        // Act
        List<BlogPost> latestPosts = blogPostRepository.findTop5ByStatusOrderByPublishedAtDesc(
                BlogPostStatus.PUBLISHED);

        // Assert
        assertThat(latestPosts).hasSizeLessThanOrEqualTo(5);
        assertThat(latestPosts).allMatch(post -> post.getStatus() == BlogPostStatus.PUBLISHED);
        
        // Verificar que están ordenados por publishedAt descendente
        if (latestPosts.size() > 1) {
            for (int i = 0; i < latestPosts.size() - 1; i++) {
                assertThat(latestPosts.get(i).getPublishedAt())
                    .isAfterOrEqualTo(latestPosts.get(i + 1).getPublishedAt());
            }
        }
    }

    @Test
    @DisplayName("Buscar todos los posts - Returns all posts")
    void testFindAll_ReturnsAllPosts() {
        // Arrange
        long initialCount = blogPostRepository.count();
        
        BlogPost post1 = TestDataFactory.createValidBlogPost();
        post1.setId(null);
        post1.setSlug("all-post-1-" + System.currentTimeMillis());
        
        BlogPost post2 = TestDataFactory.createValidBlogPost();
        post2.setId(null);
        post2.setSlug("all-post-2-" + System.currentTimeMillis());

        blogPostRepository.save(post1);
        blogPostRepository.save(post2);

        // Act
        List<BlogPost> allPosts = blogPostRepository.findAll();

        // Assert
        assertThat(allPosts).hasSizeGreaterThanOrEqualTo((int) initialCount + 2);
    }

    @Test
    @DisplayName("Actualizar post existente - Success")
    void testUpdate_ExistingPost_ReturnsUpdatedPost() {
        // Arrange
        BlogPost post = TestDataFactory.createValidBlogPost();
        post.setId(null);
        post.setSlug("update-post-" + System.currentTimeMillis());
        BlogPost saved = blogPostRepository.save(post);

        // Act
        saved.setTitle("Título actualizado");
        saved.setStatus(BlogPostStatus.PUBLISHED);
        saved.setPublishedAt(LocalDateTime.now());
        BlogPost updated = blogPostRepository.save(saved);

        // Assert
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getTitle()).isEqualTo("Título actualizado");
        assertThat(updated.getStatus()).isEqualTo(BlogPostStatus.PUBLISHED);
        assertThat(updated.getPublishedAt()).isNotNull();
    }

    @Test
    @DisplayName("Eliminar post por ID - Success")
    void testDeleteById_ExistingId_DeletesPost() {
        // Arrange
        BlogPost post = TestDataFactory.createValidBlogPost();
        post.setId(null);
        post.setSlug("delete-post-" + System.currentTimeMillis());
        BlogPost saved = blogPostRepository.save(post);
        UUID id = saved.getId();

        // Act
        blogPostRepository.deleteById(id);

        // Assert
        Optional<BlogPost> found = blogPostRepository.findById(id);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Verificar @PrePersist - Establece fechas automáticamente")
    void testPrePersist_SetsTimestamps() {
        // Arrange
        BlogPost post = BlogPost.builder()
                .title("Test PrePersist")
                .subtitle("Subtítulo")
                .slug("prepersist-test-" + System.currentTimeMillis())
                .content("Contenido del post")
                .category("General")
                .status(BlogPostStatus.DRAFT)
                .build();

        // Act
        BlogPost saved = blogPostRepository.save(post);

        // Assert
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Verificar @PrePersist con PUBLISHED - Establece publishedAt")
    void testPrePersist_PublishedStatus_SetsPublishedAt() {
        // Arrange
        BlogPost post = BlogPost.builder()
                .title("Test Published")
                .subtitle("Subtítulo")
                .slug("published-prepersist-" + System.currentTimeMillis())
                .content("Contenido del post")
                .category("General")
                .status(BlogPostStatus.PUBLISHED)
                .build();

        // Act
        BlogPost saved = blogPostRepository.save(post);

        // Assert
        assertThat(saved.getPublishedAt()).isNotNull();
    }

    @Test
    @DisplayName("Verificar existencia por ID - Returns true")
    void testExistsById_ExistingId_ReturnsTrue() {
        // Arrange
        BlogPost post = TestDataFactory.createValidBlogPost();
        post.setId(null);
        post.setSlug("exists-test-" + System.currentTimeMillis());
        BlogPost saved = blogPostRepository.save(post);

        // Act
        boolean exists = blogPostRepository.existsById(saved.getId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Verificar existencia por ID inexistente - Returns false")
    void testExistsById_NonExistingId_ReturnsFalse() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Act
        boolean exists = blogPostRepository.existsById(nonExistingId);

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Contar posts - Returns correct count")
    void testCount_ReturnsCorrectCount() {
        // Arrange
        long initialCount = blogPostRepository.count();
        
        BlogPost post1 = TestDataFactory.createValidBlogPost();
        post1.setId(null);
        post1.setSlug("count-post-1-" + System.currentTimeMillis());
        
        BlogPost post2 = TestDataFactory.createValidBlogPost();
        post2.setId(null);
        post2.setSlug("count-post-2-" + System.currentTimeMillis());

        blogPostRepository.save(post1);
        blogPostRepository.save(post2);

        // Act
        long count = blogPostRepository.count();

        // Assert
        assertThat(count).isEqualTo(initialCount + 2);
    }
}