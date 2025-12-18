package com.reverso.service;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.response.ContactResponse;
import com.reverso.mapper.ContactMapper;
import com.reverso.model.Contact;
import com.reverso.repository.ContactRepository;
import com.reverso.service.impl.ContactServiceImpl;
import com.reverso.service.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContactService - Tests Unitarios")
@SuppressWarnings("null")
class ContactServiceTest {

    @Mock
    private ContactRepository repository;

    @Mock
    private ContactMapper mapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ContactServiceImpl contactService;

    private Contact validContact;
    private ContactCreateRequest validRequest;
    private ContactResponse validResponse;

    @BeforeEach
    void setUp() {
        validContact = TestDataFactory.createValidContact();
        validRequest = TestDataFactory.createValidContactRequest();
        
        validResponse = ContactResponse.builder()
                .id(validContact.getId())
                .fullName(validContact.getFullName())
                .email(validContact.getEmail())
                .message(validContact.getMessage())
                .acceptsPrivacy(validContact.getAcceptsPrivacy())
                .status(validContact.getStatus().name())
                .createdAt(validContact.getCreatedAt())
                .updatedAt(validContact.getUpdatedAt())
                .build();
    }


    @Test
    @DisplayName("Crear contacto con datos válidos - Success")
    void testCreate_WithValidData_ReturnsContactResponse() {

        when(mapper.toEntity(any(ContactCreateRequest.class))).thenReturn(validContact);
        when(repository.save(any(Contact.class))).thenReturn(validContact);
        when(mapper.toResponse(any(Contact.class))).thenReturn(validResponse);
        doNothing().when(emailService).sendEmailToAdmin(any(ContactCreateRequest.class));
        doNothing().when(emailService).sendConfirmationToUser(any(ContactCreateRequest.class));

        ContactResponse result = contactService.create(validRequest);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo(validRequest.getFullName());
        assertThat(result.getEmail()).isEqualTo(validRequest.getEmail());
        assertThat(result.getMessage()).isEqualTo(validRequest.getMessage());

        verify(mapper, times(1)).toEntity(validRequest);
        verify(repository, times(1)).save(any(Contact.class));
        verify(mapper, times(1)).toResponse(validContact);
        verify(emailService, times(1)).sendEmailToAdmin(validRequest);
        verify(emailService, times(1)).sendConfirmationToUser(validRequest);
    }

    @Test
    @DisplayName("Crear contacto - Email al admin falla pero contacto se guarda")
    void testCreate_EmailToAdminFails_ContactStillSaved() {

        when(mapper.toEntity(any(ContactCreateRequest.class))).thenReturn(validContact);
        when(repository.save(any(Contact.class))).thenReturn(validContact);
        when(mapper.toResponse(any(Contact.class))).thenReturn(validResponse);
        doThrow(new RuntimeException("Error enviando email")).when(emailService).sendEmailToAdmin(any());

        ContactResponse result = contactService.create(validRequest);

        assertThat(result).isNotNull();
        verify(repository, times(1)).save(any(Contact.class));
        verify(emailService, times(1)).sendConfirmationToUser(any());
    }

    @Test
    @DisplayName("Crear contacto sin nombre - Throws BadRequestException")
    void testCreate_WithoutFullName_ThrowsBadRequestException() {

        ContactCreateRequest invalidRequest = TestDataFactory.createContactRequestWithoutName();

        assertThatThrownBy(() -> contactService.create(invalidRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("El nombre completo es obligatorio");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Crear contacto sin email - Throws BadRequestException")
    void testCreate_WithoutEmail_ThrowsBadRequestException() {

        ContactCreateRequest invalidRequest = TestDataFactory.createContactRequestWithoutEmail();

        assertThatThrownBy(() -> contactService.create(invalidRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("El email es obligatorio");
    }

    @Test
    @DisplayName("Crear contacto sin mensaje - Throws BadRequestException")
    void testCreate_WithoutMessage_ThrowsBadRequestException() {

        ContactCreateRequest invalidRequest = TestDataFactory.createContactRequestWithoutMessage();

        assertThatThrownBy(() -> contactService.create(invalidRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("El mensaje es obligatorio");
    }

    @Test
    @DisplayName("Crear contacto sin aceptar privacidad - Throws BadRequestException")
    void testCreate_WithoutPrivacyAcceptance_ThrowsBadRequestException() {

        ContactCreateRequest invalidRequest = TestDataFactory.createContactRequestWithoutPrivacy();

        assertThatThrownBy(() -> contactService.create(invalidRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Debe aceptar la política de privacidad");
    }

    @Test
    @DisplayName("Crear contacto con request null - Throws BadRequestException")
    void testCreate_WithNullRequest_ThrowsBadRequestException() {

        assertThatThrownBy(() -> contactService.create(null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("El cuerpo de la solicitud es obligatorio");
    }


    @Test
    @DisplayName("Obtener todos los contactos - Success")
    void testGetAll_ReturnsListOfContacts() {

        Contact contact1 = TestDataFactory.createValidContact();
        Contact contact2 = TestDataFactory.createValidContact();
        List<Contact> contacts = Arrays.asList(contact1, contact2);

        ContactResponse response1 = ContactResponse.builder()
                .id(contact1.getId())
                .fullName(contact1.getFullName())
                .build();
        ContactResponse response2 = ContactResponse.builder()
                .id(contact2.getId())
                .fullName(contact2.getFullName())
                .build();

        when(repository.findAll()).thenReturn(contacts);
        when(mapper.toResponse(contact1)).thenReturn(response1);
        when(mapper.toResponse(contact2)).thenReturn(response2);

        List<ContactResponse> result = contactService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(response1, response2);
        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toResponse(any(Contact.class));
    }

    @Test
    @DisplayName("Obtener todos los contactos - Lista vacía")
    void testGetAll_EmptyList_ReturnsEmptyList() {

        when(repository.findAll()).thenReturn(List.of());

        List<ContactResponse> result = contactService.getAll();

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAll();
    }


    @Test
    @DisplayName("Obtener contacto por ID existente - Success")
    void testGetById_ExistingId_ReturnsContact() {

        UUID id = validContact.getId();
        when(repository.findById(id)).thenReturn(Optional.of(validContact));
        when(mapper.toResponse(validContact)).thenReturn(validResponse);

        ContactResponse result = contactService.getById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Obtener contacto por ID inexistente - Throws RuntimeException")
    void testGetById_NonExistingId_ThrowsRuntimeException() {
        UUID nonExistingId = UUID.randomUUID();
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.getById(nonExistingId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Contacto no encontrado");
    }

    @Test
    @DisplayName("Actualizar estado con ID inexistente - Throws RuntimeException")
    void testUpdateStatus_NonExistingId_ThrowsRuntimeException() {
        UUID nonExistingId = UUID.randomUUID();
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.updateStatus(nonExistingId, "RESPONDED"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Contacto no encontrado");
    }

    @Test
    @DisplayName("Actualizar estado con valor inválido - Throws RuntimeException")
    void testUpdateStatus_InvalidStatus_ThrowsRuntimeException() {

        UUID id = validContact.getId();
        String invalidStatus = "INVALID_STATUS";
        when(repository.findById(id)).thenReturn(Optional.of(validContact));

        assertThatThrownBy(() -> contactService.updateStatus(id, invalidStatus))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Estado inválido");
    }

}