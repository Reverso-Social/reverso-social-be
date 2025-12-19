package com.reverso.mapper;

import com.reverso.config.TestDataFactory;
import com.reverso.dto.request.UserCreateRequest;
import com.reverso.dto.response.UserResponse;
import com.reverso.model.User;
import com.reverso.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserToResponse() {

        User user = TestDataFactory.createValidUser();

        UserResponse response = mapper.toResponse(user);


        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getFullName()).isEqualTo(user.getFullName());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getPhone()).isEqualTo(user.getPhone());
        assertThat(response.getCompanyName()).isEqualTo(user.getCompanyName());
        assertThat(response.getRole()).isEqualTo("ADMIN");
        assertThat(response.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    void shouldMapUserCreateRequestToEntity() {

        UserCreateRequest request = TestDataFactory.createValidUserCreateRequest();


        User user = mapper.toEntity(request);

        assertThat(user).isNotNull();
        assertThat(user.getFullName()).isEqualTo(request.getFullName());
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getPhone()).isEqualTo(request.getPhone());
        assertThat(user.getCompanyName()).isEqualTo(request.getCompanyName());
        assertThat(user.getPassword()).isEqualTo(request.getPassword());
        assertThat(user.getRole()).isEqualTo(Role.USER);
        
        assertThat(user.getId()).isNull();
        assertThat(user.getMessage()).isNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
    }

    @Test
    void shouldHandleNullRoleWhenMappingToResponse() {

        User user = TestDataFactory.createValidUser();
        user.setRole(null);


        UserResponse response = mapper.toResponse(user);


        assertThat(response).isNotNull();
        assertThat(response.getRole()).isNull();
    }

    @Test
    void shouldDefaultToUserRoleWhenRoleIsNull() {

        UserCreateRequest request = TestDataFactory.createValidUserCreateRequest();
        request.setRole(null);

        User user = mapper.toEntity(request);

        assertThat(user).isNotNull();
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void shouldMapRoleStringToEnumCaseInsensitive() {

        UserCreateRequest requestLower = TestDataFactory.createValidUserCreateRequest();
        requestLower.setRole("admin");

        UserCreateRequest requestUpper = TestDataFactory.createValidUserCreateRequest();
        requestUpper.setRole("ADMIN");

        UserCreateRequest requestMixed = TestDataFactory.createValidUserCreateRequest();
        requestMixed.setRole("AdMiN");

        User userLower = mapper.toEntity(requestLower);
        User userUpper = mapper.toEntity(requestUpper);
        User userMixed = mapper.toEntity(requestMixed);

        assertThat(userLower.getRole()).isEqualTo(Role.ADMIN);
        assertThat(userUpper.getRole()).isEqualTo(Role.ADMIN);
        assertThat(userMixed.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void shouldMapAllRoleTypes() {

        UserCreateRequest userRequest = TestDataFactory.createValidUserCreateRequest();
        userRequest.setRole("USER");
        User userEntity = mapper.toEntity(userRequest);
        assertThat(userEntity.getRole()).isEqualTo(Role.USER);


        UserCreateRequest adminRequest = TestDataFactory.createAdminUserRequest();
        User adminEntity = mapper.toEntity(adminRequest);
        assertThat(adminEntity.getRole()).isEqualTo(Role.ADMIN);

        UserCreateRequest editorRequest = TestDataFactory.createValidUserCreateRequest();
        editorRequest.setRole("EDITOR");
        User editorEntity = mapper.toEntity(editorRequest);
        assertThat(editorEntity.getRole()).isEqualTo(Role.EDITOR);
    }

    @Test
    void shouldMapUserWithMinimalFields() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .fullName("Minimal User")
                .email("minimal@example.com")
                .role(Role.USER)
                .build();


        UserResponse response = mapper.toResponse(user);


        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("Minimal User");
        assertThat(response.getEmail()).isEqualTo("minimal@example.com");
        assertThat(response.getPhone()).isNull();
        assertThat(response.getCompanyName()).isNull();
    }

    @Test
    void shouldMapRequestWithOptionalFieldsNull() {

        UserCreateRequest request = TestDataFactory.createValidUserCreateRequest();
        request.setPhone(null);
        request.setCompanyName(null);


        User user = mapper.toEntity(request);

        assertThat(user).isNotNull();
        assertThat(user.getFullName()).isEqualTo(request.getFullName());
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getPhone()).isNull();
        assertThat(user.getCompanyName()).isNull();
    }

    @Test
    void shouldMapDifferentUserRoles() {

        User adminUser = TestDataFactory.createUserWithRole(Role.ADMIN);
        User editorUser = TestDataFactory.createUserWithRole(Role.EDITOR);
        User regularUser = TestDataFactory.createUserWithRole(Role.USER);


        UserResponse adminResponse = mapper.toResponse(adminUser);
        UserResponse editorResponse = mapper.toResponse(editorUser);
        UserResponse userResponse = mapper.toResponse(regularUser);

        assertThat(adminResponse.getRole()).isEqualTo("ADMIN");
        assertThat(editorResponse.getRole()).isEqualTo("EDITOR");
        assertThat(userResponse.getRole()).isEqualTo("USER");
    }

    @Test
    void shouldPreservePasswordWhenMappingFromRequest() {

        UserCreateRequest request = TestDataFactory.createValidUserCreateRequest();
        String originalPassword = request.getPassword();


        User user = mapper.toEntity(request);

        assertThat(user.getPassword()).isEqualTo(originalPassword);
    }
}