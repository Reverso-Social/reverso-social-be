package com.reverso.service.interfaces;

import com.reverso.dto.request.UserCreateRequest;
import com.reverso.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    
    UserResponse createUser(UserCreateRequest dto);
    
    List<UserResponse> getAll();
    
    UserResponse getById(UUID id);
    
    void delete(UUID id);
}