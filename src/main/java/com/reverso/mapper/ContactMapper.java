package com.reverso.mapper;

import com.reverso.dto.ContactCreateDto;
import com.reverso.dto.ContactDto;
import com.reverso.model.Contact;
import com.reverso.model.enums.ContactStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "userId", source = "handledByUser.id")
    ContactDto toDto(Contact contact);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "handledByUser", ignore = true)
    Contact toEntity(ContactCreateDto dto);

    @Named("statusToString")
    default String statusToString(ContactStatus status) {
        return status != null ? status.name() : null;
    }
}