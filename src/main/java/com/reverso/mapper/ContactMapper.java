package com.reverso.mapper;

import com.reverso.dto.ContactCreateDto;
import com.reverso.dto.ContactDto;
import com.reverso.model.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {


    ContactDto toDto(Contact contact);

    Contact toEntity(ContactCreateDto dto);
}
