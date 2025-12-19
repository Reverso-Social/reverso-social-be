package com.reverso.mapper;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.ContactCreateRequest;
import com.reverso.dto.response.ContactResponse;
import com.reverso.model.Contact;
import com.reverso.model.User;
import com.reverso.model.enums.ContactStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ContactMapper - Tests")
class ContactMapperTest {

    @Autowired
    private ContactMapper contactMapper;

    @Test
    @DisplayName("Mapear Request a Entity - Success")
    void testToEntity_ValidRequest_ReturnsEntity() {
        ContactCreateRequest request = TestDataFactory.createValidContactRequest();

        
        Contact entity = contactMapper.toEntity(request);

        
        assertThat(entity).isNotNull();
        assertThat(entity.getFullName()).isEqualTo(request.getFullName());
        assertThat(entity.getEmail()).isEqualTo(request.getEmail());
        assertThat(entity.getMessage()).isEqualTo(request.getMessage());
        assertThat(entity.getAcceptsPrivacy()).isEqualTo(request.getAcceptsPrivacy());

        assertThat(entity.getId()).isNull();
        assertThat(entity.getStatus()).isEqualTo(ContactStatus.PENDING);
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getHandledByUser()).isNull();
    }

    @Test
    @DisplayName("Mapear Entity a Response sin usuario - Success")
    void testToResponse_WithoutUser_ReturnsResponse() {
        Contact contact = TestDataFactory.createValidContact();
        contact.setHandledByUser(null); 

        
        ContactResponse response = contactMapper.toResponse(contact);

     
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(contact.getId());
        assertThat(response.getFullName()).isEqualTo(contact.getFullName());
        assertThat(response.getEmail()).isEqualTo(contact.getEmail());
        assertThat(response.getMessage()).isEqualTo(contact.getMessage());
        assertThat(response.getAcceptsPrivacy()).isEqualTo(contact.getAcceptsPrivacy());
        assertThat(response.getStatus()).isEqualTo(contact.getStatus().name());
        assertThat(response.getCreatedAt()).isEqualTo(contact.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(contact.getUpdatedAt());
        
        assertThat(response.getUserId()).isNull();
        assertThat(response.getUserName()).isNull();
    }

    @Test
    @DisplayName("Mapear Entity a Response con usuario - Success")
    void testToResponse_WithUser_ReturnsResponseWithUserData() {

        Contact contact = TestDataFactory.createValidContact();
        
        User user = User.builder()
                .id(UUID.randomUUID())
                .fullName("Admin Usuario")
                .email("admin@reverso.com")
                .build();
        
        contact.setHandledByUser(user);

        
        ContactResponse response = contactMapper.toResponse(contact);

        
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getUserName()).isEqualTo(user.getFullName());
    }

    @Test
    @DisplayName("Mapear status PENDING a String - Success")
    void testStatusToString_PendingStatus_ReturnsPENDING() {
        
        String result = contactMapper.statusToString(ContactStatus.PENDING);

        
        assertThat(result).isEqualTo("PENDING");
    }

   

    @Test
    @DisplayName("Mapear status null - Returns null")
    void testStatusToString_NullStatus_ReturnsNull() {
        
        String result = contactMapper.statusToString(null);

        
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Obtener User ID cuando contact tiene usuario - Success")
    void testGetUserId_ContactWithUser_ReturnsUserId() {

        Contact contact = TestDataFactory.createValidContact();
        User user = User.builder()
                .id(UUID.randomUUID())
                .fullName("Admin Usuario")
                .build();
        contact.setHandledByUser(user);

        
        UUID result = contactMapper.getUserId(contact);

        
        assertThat(result).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Obtener User ID cuando contact no tiene usuario - Returns null")
    void testGetUserId_ContactWithoutUser_ReturnsNull() {

        Contact contact = TestDataFactory.createValidContact();
        contact.setHandledByUser(null);

        
        UUID result = contactMapper.getUserId(contact);

        
        assertThat(result).isNull();
    }
}