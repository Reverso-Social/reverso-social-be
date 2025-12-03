package com.reverso.mapper;

import com.reverso.dto.request.UserCreateRequest;
import com.reverso.dto.response.UserResponse;
import com.reverso.model.User;
import com.reverso.model.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    UserResponse toResponse(User user);

    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "message", ignore = true)
    @Mapping(target = "managedServices", ignore = true)
    @Mapping(target = "managedResources", ignore = true)
    @Mapping(target = "handledContacts", ignore = true)
    @Mapping(target = "downloads", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserCreateRequest dto);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }

    @Named("stringToRole")
    default Role stringToRole(String role) {
        return role != null ? Role.valueOf(role.toUpperCase()) : Role.USER;
    }
}