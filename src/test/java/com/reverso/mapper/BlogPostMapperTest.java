package com.reverso.mapper;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.dto.response.BlogPostResponse;
import com.reverso.model.BlogPost;
import com.reverso.model.enums.BlogPostStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {BlogPostMapperImpl.class})
@DisplayName("BlogPostMapper - Tests")
class BlogPostMapperTest {

    @Autowired
    private BlogPostMapper blogPostMapper;

    @Test
    @DisplayName("Mapear CreateRequest a Entity - Success")
    void testToEntity_ValidCreateRequest_ReturnsEntity() {
    
        BlogPostCreateRequest request = TestDataFactory.createValidBlogPostRequest();

        
        BlogPost entity = blogPostMapper.toEntity(request);

    
        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo(request.getTitle());
        assertThat(entity.getSubtitle()).isEqualTo(request.getSubtitle());
        assertThat(entity.getContent()).isEqualTo(request.getContent());
        assertThat(entity.getCategory()).isEqualTo(request.getCategory());
        assertThat(entity.getStatus()).isEqualTo(request.getStatus());

        assertThat(entity.getId()).isNull();
        assertThat(entity.getSlug()).isNull();
        assertThat(entity.getCoverImageUrl()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getPublishedAt()).isNull();
    }

    @Test
    @DisplayName("Mapear Entity a Response - Success")
    void testToResponse_ValidEntity_ReturnsResponse() {
    
        BlogPost entity = TestDataFactory.createValidBlogPost();

        
        BlogPostResponse response = blogPostMapper.toResponse(entity);

    
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(entity.getId());
        assertThat(response.getTitle()).isEqualTo(entity.getTitle());
        assertThat(response.getSubtitle()).isEqualTo(entity.getSubtitle());
        assertThat(response.getSlug()).isEqualTo(entity.getSlug());
        assertThat(response.getContent()).isEqualTo(entity.getContent());
        assertThat(response.getCategory()).isEqualTo(entity.getCategory());
        assertThat(response.getStatus()).isEqualTo(entity.getStatus().name());
        assertThat(response.getCoverImageUrl()).isEqualTo(entity.getCoverImageUrl());
        assertThat(response.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
        assertThat(response.getPublishedAt()).isEqualTo(entity.getPublishedAt());
    }

    @Test
    @DisplayName("Mapear Entity con status DRAFT a Response - Success")
    void testToResponse_DraftStatus_ReturnsResponseWithDraft() {
    
        BlogPost entity = TestDataFactory.createBlogPostWithStatus(BlogPostStatus.DRAFT);

        
        BlogPostResponse response = blogPostMapper.toResponse(entity);

    
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("DRAFT");
        assertThat(response.getPublishedAt()).isNull();
    }

    @Test
    @DisplayName("Mapear Entity con status PUBLISHED a Response - Success")
    void testToResponse_PublishedStatus_ReturnsResponseWithPublished() {
    
        BlogPost entity = TestDataFactory.createBlogPostWithStatus(BlogPostStatus.PUBLISHED);

        
        BlogPostResponse response = blogPostMapper.toResponse(entity);

    
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("PUBLISHED");
        assertThat(response.getPublishedAt()).isNotNull();
    }

    @Test
    @DisplayName("Mapear Entity con status ARCHIVED a Response - Success")
    void testToResponse_ArchivedStatus_ReturnsResponseWithArchived() {
    
        BlogPost entity = TestDataFactory.createBlogPostWithStatus(BlogPostStatus.ARCHIVED);

        
        BlogPostResponse response = blogPostMapper.toResponse(entity);

    
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("ARCHIVED");
    }

    @Test
    @DisplayName("Actualizar Entity desde UpdateRequest - Success")
    void testUpdateEntityFromDto_ValidRequest_UpdatesEntity() {
    
        BlogPost entity = TestDataFactory.createValidBlogPost();
        String originalSlug = entity.getSlug();
        BlogPostUpdateRequest updateRequest = TestDataFactory.createValidBlogPostUpdateRequest();

        
        blogPostMapper.updateEntityFromDto(updateRequest, entity);

    
        assertThat(entity.getTitle()).isEqualTo(updateRequest.getTitle());
        assertThat(entity.getSubtitle()).isEqualTo(updateRequest.getSubtitle());
        assertThat(entity.getContent()).isEqualTo(updateRequest.getContent());
        assertThat(entity.getCategory()).isEqualTo(updateRequest.getCategory());
        assertThat(entity.getStatus()).isEqualTo(updateRequest.getStatus());
        
        assertThat(entity.getSlug()).isEqualTo(originalSlug);
    }

    @Test
    @DisplayName("Actualizar Entity con campos null en UpdateRequest - No sobrescribe")
    void testUpdateEntityFromDto_NullFields_DoesNotOverwrite() {
    
        BlogPost entity = TestDataFactory.createValidBlogPost();
        String originalTitle = entity.getTitle();
        String originalContent = entity.getContent();
        
        BlogPostUpdateRequest updateRequest = BlogPostUpdateRequest.builder()
                .title(null)  
                .subtitle("Nuevo subtítulo")
                .content(null)  
                .category("Nueva categoría")
                .status(BlogPostStatus.PUBLISHED)
                .build();

        
        blogPostMapper.updateEntityFromDto(updateRequest, entity);

    
        assertThat(entity.getTitle()).isEqualTo(originalTitle);
        assertThat(entity.getContent()).isEqualTo(originalContent);
        
        assertThat(entity.getSubtitle()).isEqualTo("Nuevo subtítulo");
        assertThat(entity.getCategory()).isEqualTo("Nueva categoría");
        assertThat(entity.getStatus()).isEqualTo(BlogPostStatus.PUBLISHED);
    }

    @Test
    @DisplayName("Mapear Entity con imagen a Response - Success")
    void testToResponse_EntityWithImage_ReturnsResponseWithImageUrl() {
    
        BlogPost entity = TestDataFactory.createValidBlogPost();
        entity.setCoverImageUrl("http://localhost/images/blog/test.jpg");

        
        BlogPostResponse response = blogPostMapper.toResponse(entity);

    
        assertThat(response).isNotNull();
        assertThat(response.getCoverImageUrl()).isEqualTo("http://localhost/images/blog/test.jpg");
    }

    @Test
    @DisplayName("Mapear Entity sin imagen a Response - Success")
    void testToResponse_EntityWithoutImage_ReturnsResponseWithNullImageUrl() {
    
        BlogPost entity = TestDataFactory.createValidBlogPost();
        entity.setCoverImageUrl(null);

        
        BlogPostResponse response = blogPostMapper.toResponse(entity);

    
        assertThat(response).isNotNull();
        assertThat(response.getCoverImageUrl()).isNull();
    }

    @Test
    @DisplayName("Mapear CreateRequest con todos los campos - Success")
    void testToEntity_RequestWithAllFields_MapsAllFields() {
    
        BlogPostCreateRequest request = BlogPostCreateRequest.builder()
                .title("Título completo del post")
                .subtitle("Subtítulo descriptivo")
                .content("Contenido extenso del post de blog con información relevante.")
                .category("Tecnología")
                .status(BlogPostStatus.DRAFT)
                .build();

        
        BlogPost entity = blogPostMapper.toEntity(request);

    
        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo("Título completo del post");
        assertThat(entity.getSubtitle()).isEqualTo("Subtítulo descriptivo");
        assertThat(entity.getContent()).isEqualTo("Contenido extenso del post de blog con información relevante.");
        assertThat(entity.getCategory()).isEqualTo("Tecnología");
        assertThat(entity.getStatus()).isEqualTo(BlogPostStatus.DRAFT);
    }

    @Test
    @DisplayName("Mapear CreateRequest sin subtitle - Success")
    void testToEntity_RequestWithoutSubtitle_MapsWithNullSubtitle() {
    
        BlogPostCreateRequest request = BlogPostCreateRequest.builder()
                .title("Título sin subtítulo")
                .subtitle(null)
                .content("Contenido del post")
                .category("General")
                .status(BlogPostStatus.DRAFT)
                .build();

        
        BlogPost entity = blogPostMapper.toEntity(request);

    
        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo("Título sin subtítulo");
        assertThat(entity.getSubtitle()).isNull();
    }

    @Test
    @DisplayName("Mapear CreateRequest sin category - Success")
    void testToEntity_RequestWithoutCategory_MapsWithNullCategory() {
    
        BlogPostCreateRequest request = BlogPostCreateRequest.builder()
                .title("Post sin categoría")
                .subtitle("Subtítulo")
                .content("Contenido del post")
                .category(null)
                .status(BlogPostStatus.DRAFT)
                .build();

        
        BlogPost entity = blogPostMapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getCategory()).isNull();
    }
}