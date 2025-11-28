package com.reverso.service;

import com.reverso.dto.UserCreateDto;
import com.reverso.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserCreateDto dto);
    List<UserDto> getAll();
    UserDto getById(Long id);
    void delete(Long id);
}
