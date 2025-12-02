package com.reverso.service.interfaces;

import com.reverso.dto.UserCreateDto;
import com.reverso.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(UserCreateDto dto);
    List<UserDto> getAll();
    UserDto getById(UUID id);
    void delete(UUID id);
}