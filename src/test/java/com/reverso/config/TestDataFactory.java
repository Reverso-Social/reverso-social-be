package com.reverso.config;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.request.BlogPostCreateRequest;
import com.reverso.dto.request.BlogPostUpdateRequest;
import com.reverso.dto.request.UserCreateRequest;
import com.reverso.dto.request.LoginRequest;
import com.reverso.dto.request.ServiceRequest;
import com.reverso.dto.response.ServiceResponse;
import com.reverso.model.Service;
import com.reverso.model.ServiceCategory;
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

    public static User createValidUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .fullName("Admin User")
                .email("admin@reverso.com")
                .phone("+34612345678")
                .companyName("Reverso Social")
                .password("$2a$10$dummyHashedPassword")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User createUserWithRole(Role role) {
        User user = createValidUser();
        user.setRole(role);
        return user;
    }

    public static User createUserWithEmail(String email) {
        User user = createValidUser();
        user.setEmail(email);
        return user;
    }

    public static UserCreateRequest createValidUserCreateRequest() {
        return UserCreateRequest.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .phone("+34612345678")
                .companyName("Test Company")
                .password("password123")
                .role("USER")
                .build();
    }

    public static UserCreateRequest createAdminUserRequest() {
        return UserCreateRequest.builder()
                .fullName("Admin User")
                .email("admin@test.com")
                .phone("+34612345678")
                .companyName("Admin Company")
                .password("adminpass123")
                .role("ADMIN")
                .build();
    }

    public static UserCreateRequest createUserRequestWithoutEmail() {
        return UserCreateRequest.builder()
                .fullName("John Doe")
                .email("")
                .password("password123")
                .role("USER")
                .build();
    }

    public static UserCreateRequest createUserRequestWithShortPassword() {
        return UserCreateRequest.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .password("12345")
                .role("USER")
                .build();
    }

    public static UserCreateRequest createUserRequestWithInvalidEmail() {
        return UserCreateRequest.builder()
                .fullName("John Doe")
                .email("invalid-email")
                .password("password123")
                .role("USER")
                .build();
    }

    public static LoginRequest createValidLoginRequest() {
        return LoginRequest.builder()
                .email("admin@reverso.com")
                .password("password123")
                .build();
    }

    public static LoginRequest createInvalidLoginRequest() {
        return LoginRequest.builder()
                .email("admin@reverso.com")
                .password("wrongpassword")
                .build();
    }

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
                "fake image content".getBytes());
    }

    public static MockMultipartFile createInvalidImageFile() {
        return new MockMultipartFile(
                "image",
                "test-file.txt",
                "text/plain",
                "not an image".getBytes());
    }

    public static MockMultipartFile createOversizedImageFile() {
        byte[] largeContent = new byte[6 * 1024 * 1024];
        return new MockMultipartFile(
                "image",
                "large-image.jpg",
                "image/jpeg",
                largeContent);
    }

    public static ServiceCategory createValidServiceCategory() {
        return ServiceCategory.builder()
                .id(UUID.randomUUID())
                .name("Development")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Service createValidService(ServiceCategory category) {
        return Service.builder()
                .id(UUID.randomUUID())
                .name("Web Development")
                .active(true)
                .sortOrder(1)
                .category(category)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ServiceRequest createValidServiceRequest(UUID categoryId) {
        return ServiceRequest.builder()
                .name("Web Development")
                .categoryId(categoryId)
                .active(true)
                .build();
    }

    public static ServiceResponse createValidServiceResponse(UUID id) {
        return ServiceResponse.builder()
                .id(id)
                .name("Web Development")
                .active(true)
                .build();
    }
}