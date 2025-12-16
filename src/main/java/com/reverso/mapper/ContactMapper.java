package com.reverso.mapper;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.response.ContactResponse;
import com.reverso.model.Contact;
import com.reverso.model.enums.ContactStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "userId", source = "contact", qualifiedByName = "getUserId")
    @Mapping(target = "userName", source = "contact", qualifiedByName = "getUserName")
    ContactResponse toResponse(Contact contact);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "handledByUser", ignore = true)
    Contact toEntity(ContactCreateRequest dto);

    @Named("statusToString")
    default String statusToString(ContactStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("getUserId")
    default java.util.UUID getUserId(Contact contact) {
        return contact.getHandledByUser() != null ? contact.getHandledByUser().getId() : null;
    }

    @Named("getUserName")
    default String getUserName(Contact contact) {
        return contact.getHandledByUser() != null ? contact.getHandledByUser().getFullName() : null;
    }
}