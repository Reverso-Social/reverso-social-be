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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactMapper mapper;
    private final EmailService emailService;

    @Override
    public ContactResponse create(ContactCreateRequest dto) {
        log.info("Creando nuevo contacto desde: {}", dto.getEmail());


        Contact entity = mapper.toEntity(dto);
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
        
        List<ContactResponse> responses = contacts.stream()
                .map(contact -> {
                    try {
                        return mapper.toResponse(contact);
                    } catch (Exception e) {
                        log.error("Error mapeando contacto {}: {}", contact.getId(), e.getMessage());
                        throw e;
                    }
                })
                .toList();
        
        log.info("{} contactos mapeados correctamente", responses.size());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getById(UUID id) {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));
        return mapper.toResponse(contact);
    }

    @Override
    public ContactResponse updateStatus(UUID id, String status) {

        Contact contact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));

        ContactStatus newStatus;
        try {
            newStatus = ContactStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + status);
        }

        contact.setStatus(newStatus);
        Contact saved = repository.save(contact);

        return mapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}