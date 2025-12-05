package com.reverso.service.impl;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailToAdmin(ContactCreateRequest dto) {
        if (!StringUtils.hasText(dto.getEmail())) {
            throw new ResponseStatusException(BAD_REQUEST, "El email es obligatorio");
        }
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(System.getenv("MAIL_USERNAME"));

        mail.setTo(System.getenv("ADMIN_EMAIL"));

        mail.setSubject("Nuevo mensaje desde Reverso Social");
        mail.setText(
            "Has recibido un nuevo mensaje desde el formulario:\n\n" +
            "Nombre completo: " + dto.getFullName() + "\n" +
            "Email: " + dto.getEmail() + "\n\n" +
            "Mensaje:\n" + dto.getMessage() + "\n"
        );
        mailSender.send(mail);
    }

    @Override
    public void sendConfirmationToUser(ContactCreateRequest dto) {
        if (!StringUtils.hasText(dto.getEmail())) {
            throw new ResponseStatusException(BAD_REQUEST, "El email es obligatorio");
        }
        SimpleMailMessage reply = new SimpleMailMessage();
        reply.setFrom("a9003728@outlook.es");
        reply.setTo(dto.getEmail());
        reply.setSubject("Hemos recibido tu mensaje - Reverso Social");
        reply.setText(
            "Hola " + dto.getFullName() + ",\n\n" +
            "Gracias por ponerte en contacto con Reverso Social.\n\n" +
            "Hemos recibido tu mensaje y nuestro equipo lo esta revisando.\n" +
            "Te responderemos lo antes posible.\n\n" +
            "Un abrazo,\n" +
            "El equipo de Reverso Social"
        );
        mailSender.send(reply);
    }
}
