package com.reverso.service.impl;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.response.ContactResponse;
import com.reverso.mapper.ContactMapper;
import com.reverso.model.Contact;
import com.reverso.model.enums.ContactStatus;
import com.reverso.repository.ContactRepository;
import com.reverso.service.interfaces.ContactService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactMapper mapper;

    @Override
    public ContactResponse create(ContactCreateRequest dto) {
        Contact contact = mapper.toEntity(dto);
        repository.save(contact);
        return mapper.toResponse(contact);
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
        
        try {
            ContactStatus newStatus = ContactStatus.valueOf(status.toUpperCase());
            contact.setStatus(newStatus);
            repository.save(contact);
            return mapper.toResponse(contact);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Valor de estado inválido: " + status + 
                ". Valores válidos son: PENDING, IN_PROGRESS, RESOLVED");
        }
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}