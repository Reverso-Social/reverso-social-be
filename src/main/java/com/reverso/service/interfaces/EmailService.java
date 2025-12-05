package com.reverso.service.interfaces;

import com.reverso.dto.request.ContactCreateRequest;

public interface EmailService {

    void sendEmailToAdmin(ContactCreateRequest dto);

    void sendConfirmationToUser(ContactCreateRequest dto);
}
