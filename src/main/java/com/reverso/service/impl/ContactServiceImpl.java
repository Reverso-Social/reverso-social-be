package com.reverso.service.impl;

import com.reverso.dto.ContactCreateDto;
import com.reverso.dto.ContactDto;
import com.reverso.mapper.ContactMapper;
import com.reverso.model.Contact;
import com.reverso.repository.ContactRepository;
import com.reverso.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactMapper mapper;

    @Override
    public ContactDto create(ContactCreateDto dto) {
        Contact contact = mapper.toEntity(dto);
        repository.save(contact);
        return mapper.toDto(contact);
    }

    @Override
    public List<ContactDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ContactDto getById(Long id) {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        return mapper.toDto(contact);
    }

    @Override
    public ContactDto updateStatus(Long id, String status) {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setStatus(status);
        repository.save(contact);
        return mapper.toDto(contact);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
