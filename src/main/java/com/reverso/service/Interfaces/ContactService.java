package com.reverso.service.interfaces;

import com.reverso.dto.ContactCreateDto;
import com.reverso.dto.ContactDto;

import java.util.List;
import java.util.UUID;

public interface ContactService {
    ContactDto create(ContactCreateDto dto);
    List<ContactDto> getAll();
    ContactDto getById(UUID id);
    ContactDto updateStatus(UUID id, String status);
    void delete(UUID id);
}