package com.reverso.model;

import com.reverso.model.enums.ContactStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ContactTest {

    @Test
    void builder_shouldCreateContactWithGivenValues() {
        Contact contact = Contact.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .message("Test message")
                .acceptsPrivacy(true)
                .status(ContactStatus.PENDING)
                .build();

        assertThat(contact.getFullName()).isEqualTo("John Doe");
        assertThat(contact.getEmail()).isEqualTo("john@example.com");
        assertThat(contact.getMessage()).isEqualTo("Test message");
        assertThat(contact.getAcceptsPrivacy()).isTrue();
        assertThat(contact.getStatus()).isEqualTo(ContactStatus.PENDING);
    }

    @Test
    void onCreate_shouldSetCreatedAndUpdatedAtAndDefaultStatus() {
        Contact contact = new Contact();
        contact.onCreate();

        assertThat(contact.getCreatedAt()).isNotNull();
        assertThat(contact.getUpdatedAt()).isNotNull();
        assertThat(contact.getStatus()).isEqualTo(ContactStatus.PENDING);
    }

    @Test
    void onUpdate_shouldUpdateUpdatedAt() throws InterruptedException {
        Contact contact = new Contact();
        contact.onCreate();

        LocalDateTime createdAt = contact.getCreatedAt();
        Thread.sleep(10); 
        contact.onUpdate();

        assertThat(contact.getUpdatedAt()).isAfter(createdAt);
    }
}