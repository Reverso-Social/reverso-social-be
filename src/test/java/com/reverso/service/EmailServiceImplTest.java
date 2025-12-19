package com.reverso.service;

import com.reverso.service.impl.EmailServiceImpl;
import com.reverso.dto.request.ContactCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.ReflectionUtils;

import static org.mockito.Mockito.*;

@SuppressWarnings("null")
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private ContactCreateRequest dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dto = ContactCreateRequest.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .message("Test message")
                .build();
        // Setear variables de entorno simuladas
        setField(emailService, "mailUsername", "noreply@example.com");
        setField(emailService, "adminEmail", "admin@example.com");
    }

    private void setField(Object target, String fieldName, String value) {
        var field = ReflectionUtils.findField(target.getClass(), fieldName);
        field.setAccessible(true);
        ReflectionUtils.setField(field, target, value);
    }

    @Test
    void sendEmailToAdmin_shouldSendEmail_whenAdminEmailConfigured() {
        emailService.sendEmailToAdmin(dto);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assert sent.getTo()[0].equals("admin@example.com");
        assert sent.getFrom().equals("noreply@example.com");
        assert sent.getText().contains("John Doe");
        assert sent.getText().contains("Test message");
    }

    @Test
    void sendEmailToAdmin_shouldNotSendEmail_whenAdminEmailMissing() {
        setField(emailService, "adminEmail", null);

        emailService.sendEmailToAdmin(dto);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmailToAdmin_shouldNotSendEmail_whenMailUsernameMissing() {
        setField(emailService, "mailUsername", null);

        emailService.sendEmailToAdmin(dto);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendConfirmationToUser_shouldSendEmail_whenMailUsernameConfigured() {
        emailService.sendConfirmationToUser(dto);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assert sent.getTo()[0].equals("john@example.com");
        assert sent.getFrom().equals("noreply@example.com");
        assert sent.getText().contains("John Doe");
    }

    @Test
    void sendConfirmationToUser_shouldNotSendEmail_whenMailUsernameMissing() {
        setField(emailService, "mailUsername", null);

        emailService.sendConfirmationToUser(dto);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmailToAdmin_shouldThrowRuntimeException_whenMailSenderThrows() {
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        boolean thrown = false;
        try {
            emailService.sendEmailToAdmin(dto);
        } catch (RuntimeException e) {
            thrown = true;
            assert e.getMessage().contains("Error enviando email al administrador");
        }
        assert thrown;
    }

    @Test
    void sendConfirmationToUser_shouldThrowRuntimeException_whenMailSenderThrows() {
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        boolean thrown = false;
        try {
            emailService.sendConfirmationToUser(dto);
        } catch (RuntimeException e) {
            thrown = true;
            assert e.getMessage().contains("Error enviando email de confirmación");
        }
        assert thrown;
    }
}