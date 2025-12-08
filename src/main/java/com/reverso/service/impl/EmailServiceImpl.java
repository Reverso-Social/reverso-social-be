package com.reverso.service.impl;

import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailToAdmin(ContactCreateRequest dto) {

        try {
            String adminEmail = System.getenv("ADMIN_EMAIL");
            String mailUsername = System.getenv("MAIL_USERNAME");

            if (adminEmail == null || adminEmail.isEmpty()) {
                log.warn("ADMIN_EMAIL no está configurado en .env — no se enviará email al admin.");
                return;
            }

            if (mailUsername == null || mailUsername.isEmpty()) {
                log.warn("MAIL_USERNAME no está configurado en .env — no se puede enviar email.");
                return;
            }

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(mailUsername);
            mail.setTo(adminEmail);
            mail.setSubject("Nuevo mensaje desde Reverso Social");

            mail.setText(
                "Has recibido un nuevo mensaje desde el formulario:\n\n" +
                "Nombre completo: " + dto.getFullName() + "\n" +
                "Email: " + dto.getEmail() + "\n\n" +
                "Mensaje:\n" + dto.getMessage() + "\n"
            );

            mailSender.send(mail);
            log.info("Email enviado al admin: {}", adminEmail);

        } catch (Exception e) {
            log.error("Error enviando email al admin: {}", e.getMessage(), e);
            throw new RuntimeException("Error enviando email al administrador", e);
        }
    }

    @Override
    public void sendConfirmationToUser(ContactCreateRequest dto) {

        try {
            SimpleMailMessage reply = new SimpleMailMessage();
            reply.setFrom("a9003728@outlook.es");  // Puedes parametrizar esto más adelante
            reply.setTo(dto.getEmail());
            reply.setSubject("Hemos recibido tu mensaje — Reverso Social");

            reply.setText(
                "Hola " + dto.getFullName() + ",\n\n" +
                "Gracias por ponerte en contacto con Reverso Social.\n\n" +
                "Hemos recibido tu mensaje y nuestro equipo lo está revisando.\n" +
                "Te responderemos lo antes posible.\n\n" +
                "Un abrazo,\n" +
                "El equipo de Reverso Social"
            );

            mailSender.send(reply);

            log.info("Email de confirmación enviado a: {}", dto.getEmail());

        } catch (Exception e) {
            log.error("Error enviando confirmación al usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error enviando email de confirmación", e);
        }
    }
}
