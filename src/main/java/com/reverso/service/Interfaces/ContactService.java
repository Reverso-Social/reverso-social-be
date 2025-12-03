package com.reverso.service.interfaces;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.response.ContactResponse;

import java.util.List;
import java.util.UUID;

public interface ContactService {
    
    ContactResponse create(ContactCreateRequest dto);
    
    List<ContactResponse> getAll();
    
    ContactResponse getById(UUID id);
    
    ContactResponse updateStatus(UUID id, String status);
    
    void delete(UUID id);
}