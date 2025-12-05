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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactMapper mapper;
    private final EmailService emailService;

    @Override
    public ContactResponse create(ContactCreateRequest dto) {
        validateRequest(dto);
        Contact entity = mapper.toEntity(dto);
        Contact saved = repository.save(entity);

        emailService.sendEmailToAdmin(dto);
        emailService.sendConfirmationToUser(dto);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> getAll() {
        return repository.findAll()
            .stream()
            .map(mapper::toResponse)
            .toList();
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
            throw new RuntimeException("Estado invalido: " + status);
        }

        contact.setStatus(newStatus);
        Contact saved = repository.save(contact);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
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
        if (dto.getAcceptsPrivacy() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Debe aceptar la politica de privacidad");
        }
    }
}
