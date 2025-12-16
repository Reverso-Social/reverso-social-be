package com.reverso.config;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.model.Contact;
import com.reverso.model.enums.ContactStatus;

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
}