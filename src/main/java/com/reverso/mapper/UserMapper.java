package com.reverso.mapper;

import com.reverso.model.User;
import com.reverso.dto.UserDto;
import com.reverso.dto.UserCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User toEntity(UserCreateDto dto);
}
