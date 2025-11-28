package com.reverso.service;

import com.reverso.dto.ContactCreateDto;
import com.reverso.dto.ContactDto;

import java.util.List;

public interface ContactService {

    ContactDto create(ContactCreateDto dto);
    List<ContactDto> getAll();
    ContactDto getById(Long id);
    ContactDto updateStatus(Long id, String status);
    void delete(Long id);
}
