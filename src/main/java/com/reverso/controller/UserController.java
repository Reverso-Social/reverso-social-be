package com.reverso.controller;

import com.reverso.dto.UserCreateDto;
import com.reverso.dto.UserDto;
import com.reverso.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserDto create(@RequestBody UserCreateDto dto) {
        return service.createUser(dto);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable UUID id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}