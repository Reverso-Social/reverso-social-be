package com.reverso.service.impl;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.response.ContactResponse;
import com.reverso.mapper.ContactMapper;
import com.reverso.model.Contact;
import com.reverso.model.enums.ContactStatus;
import com.reverso.repository.ContactRepository;
import com.reverso.service.interfaces.ContactService;
import com.reverso.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactMapper mapper;
    private final EmailService emailService;

    @Override
    public ContactResponse create(ContactCreateRequest dto) {
        validateRequest(dto);
        log.info("Creando nuevo contacto desde: {}", dto.getEmail());

        Contact entity = mapper.toEntity(dto);
        // repository.save() is transactional by default, so we don't need
        // @Transactional on this method
        // This keeps the DB transaction short and separate from email sending.
        Contact saved = repository.save(entity);
        log.info("Contacto guardado en BD con ID: {}", saved.getId());

        try {
            emailService.sendEmailToAdmin(dto);
            log.info("Email enviado al admin correctamente");
        } catch (Exception e) {
            log.error("Error enviando email al admin (el contacto se guardó igualmente): {}", e.getMessage());
        }

        try {
            emailService.sendConfirmationToUser(dto);
            log.info("Email de confirmación enviado al usuario correctamente");
        } catch (Exception e) {
            log.error("Error enviando confirmación al usuario (el contacto se guardó igualmente): {}", e.getMessage());
        }

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> getAll() {
        log.info("Obteniendo todos los contactos");
        List<Contact> contacts = repository.findAll();
        log.info("Encontrados {} contactos en la BD", contacts.size());

        return contacts.stream()
                .map(contact -> {
                    try {
                        return mapper.toResponse(contact);
                    } catch (Exception e) {
                        log.error("Error mapeando contacto {}: {}", contact.getId(), e.getMessage());
                        throw e;
                    }
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getById(UUID id) {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,
                        "Contacto no encontrado"));
        return mapper.toResponse(contact);
    }

    @Override
    @Transactional
    public ContactResponse updateStatus(UUID id, String status) {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,
                        "Contacto no encontrado"));

        ContactStatus newStatus;
        try {
            newStatus = ContactStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Estado inválido: " + status);
        }

        contact.setStatus(newStatus);
        Contact saved = repository.save(contact);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Contacto no encontrado");
        }
        repository.deleteById(id);
    }

    private void validateRequest(ContactCreateRequest dto) {
        if (dto == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El cuerpo de la solicitud es obligatorio");
        }
        if (!StringUtils.hasText(dto.getFullName())) {
            throw new ResponseStatusException(BAD_REQUEST, "El nombre completo es obligatorio");
        }
        if (!StringUtils.hasText(dto.getEmail())) {
            throw new ResponseStatusException(BAD_REQUEST, "El email es obligatorio");
        }
        if (!StringUtils.hasText(dto.getMessage())) {
            throw new ResponseStatusException(BAD_REQUEST, "El mensaje es obligatorio");
        }
        // Fix: acceptsPrivacy must be strictly true
        if (!Boolean.TRUE.equals(dto.getAcceptsPrivacy())) {
            throw new ResponseStatusException(BAD_REQUEST, "Debe aceptar la política de privacidad");
        }
    }
}
