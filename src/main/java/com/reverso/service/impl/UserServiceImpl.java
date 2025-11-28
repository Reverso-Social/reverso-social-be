package com.reverso.service.impl;

import com.reverso.dto.UserCreateDto;
import com.reverso.dto.UserDto;
import com.reverso.mapper.UserMapper;
import com.reverso.model.User;
import com.reverso.repository.UserRepository;
import com.reverso.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDto createUser(UserCreateDto dto) {
        User user = mapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));
        repository.save(user);
        return mapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public UserDto getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
