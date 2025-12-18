package com.reverso.config;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.model.Contact;
import com.reverso.model.User;
import com.reverso.model.BlogPost;
import com.reverso.model.enums.ContactStatus;
import com.reverso.model.enums.Role;
import com.reverso.model.enums.BlogPostStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;


public class TestDataFactory {

    // ========== CONTACT TEST DATA ==========

    public static ContactCreateRequest createValidContactRequest() {
        return ContactCreateRequest.builder()
                .fullName("Juan Pérez")
                .email("juan.perez@example.com")
                .message("Este es un mensaje de prueba")
                .acceptsPrivacy(true)
                .build();
    }

    public static ContactCreateRequest createContactRequestWithoutName() {
        return ContactCreateRequest.builder()
                .fullName("")
                .email("test@example.com")
                .message("Mensaje de prueba")
                .acceptsPrivacy(true)
                .build();
    }

    public static ContactCreateRequest createContactRequestWithoutEmail() {
        return ContactCreateRequest.builder()
                .fullName("Juan Pérez")
                .email("")
                .message("Mensaje de prueba")
                .acceptsPrivacy(true)
                .build();
    }

    public static ContactCreateRequest createContactRequestWithoutMessage() {
        return ContactCreateRequest.builder()
                .fullName("Juan Pérez")
                .email("test@example.com")
                .message("")
                .acceptsPrivacy(true)
                .build();
    }

    public static ContactCreateRequest createContactRequestWithoutPrivacy() {
        return ContactCreateRequest.builder()
                .fullName("Juan Pérez")
                .email("test@example.com")
                .message("Mensaje de prueba")
                .acceptsPrivacy(null)
                .build();
    }

    public static Contact createValidContact() {
        return Contact.builder()
                .id(UUID.randomUUID())
                .fullName("Juan Pérez")
                .email("juan.perez@example.com")
                .message("Este es un mensaje de prueba")
                .acceptsPrivacy(true)
                .status(ContactStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Contact createContactWithStatus(ContactStatus status) {
        Contact contact = createValidContact();
        contact.setStatus(status);
        return contact;
    }

    // ========== USER TEST DATA ==========

    public static User createValidUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .fullName("Admin User")
                .email("admin@reverso.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
    }

    // ========== BLOGPOST TEST DATA ==========

    public static BlogPostCreateRequest createValidBlogPostRequest() {
        return BlogPostCreateRequest.builder()
                .title("Mi primer post de blog")
                .subtitle("Un subtítulo interesante")
                .content("Este es el contenido completo del post de blog con más de 10 caracteres.")
                .category("Tecnología")
                .status(BlogPostStatus.DRAFT)
                .build();
    }

    public static BlogPostCreateRequest createBlogPostRequestWithoutTitle() {
        return BlogPostCreateRequest.builder()
                .title("")
                .subtitle("Subtítulo")
                .content("Contenido del post")
                .category("Tecnología")
                .status(BlogPostStatus.DRAFT)
                .build();
    }

    public static BlogPostCreateRequest createBlogPostRequestWithShortTitle() {
        return BlogPostCreateRequest.builder()
                .title("ABC") 
                .subtitle("Subtítulo")
                .content("Contenido del post")
                .category("Tecnología")
                .status(BlogPostStatus.DRAFT)
                .build();
    }

    public static BlogPostCreateRequest createBlogPostRequestWithoutContent() {
        return BlogPostCreateRequest.builder()
                .title("Título válido")
                .subtitle("Subtítulo")
                .content("")
                .category("Tecnología")
                .status(BlogPostStatus.DRAFT)
                .build();
    }

    public static BlogPostUpdateRequest createValidBlogPostUpdateRequest() {
        return BlogPostUpdateRequest.builder()
                .title("Título actualizado")
                .subtitle("Subtítulo actualizado")
                .content("Contenido actualizado del post")
                .category("Tecnología")
                .status(BlogPostStatus.PUBLISHED)
                .build();
    }

    public static BlogPost createValidBlogPost() {
        return BlogPost.builder()
                .id(UUID.randomUUID())
                .title("Mi primer post de blog")
                .subtitle("Un subtítulo interesante")
                .slug("mi-primer-post-de-blog")
                .content("Este es el contenido completo del post de blog.")
                .category("Tecnología")
                .status(BlogPostStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static BlogPost createBlogPostWithStatus(BlogPostStatus status) {
        BlogPost post = createValidBlogPost();
        post.setStatus(status);
        if (status == BlogPostStatus.PUBLISHED) {
            post.setPublishedAt(LocalDateTime.now());
        }
        return post;
    }

    public static BlogPost createBlogPostWithCategory(String category) {
        BlogPost post = createValidBlogPost();
        post.setCategory(category);
        return post;
    }

    public static MockMultipartFile createValidImageFile() {
        return new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );
    }

    public static MockMultipartFile createInvalidImageFile() {
        return new MockMultipartFile(
                "image",
                "test-file.txt",
                "text/plain",
                "not an image".getBytes()
        );
    }

    public static MockMultipartFile createOversizedImageFile() {
        byte[] largeContent = new byte[6 * 1024 * 1024];
        return new MockMultipartFile(
                "image",
                "large-image.jpg",
                "image/jpeg",
                largeContent
        );
    }
}