package com.reverso.repository;

import com.reverso.config.TestDataFactory;
import com.reverso.model.Resource;
import com.reverso.model.enums.ResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ResourceRepository - Tests de Integración")
@SuppressWarnings("null")
class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    @DisplayName("Guardar recurso - Success")
    void testSave_NewResource_ReturnsSavedResource() {
        Resource resource = TestDataFactory.createValidResource();
        resource.setId(null);

        Resource saved = resourceRepository.save(resource);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(resource.getTitle());
    }

    @Test
    @DisplayName("Buscar por ID existente - Returns Resource")
    void testFindById_ExistingId_ReturnsResource() {
        Resource resource = TestDataFactory.createValidResource();
        resource.setId(null);
        Resource saved = resourceRepository.save(resource);

        Optional<Resource> found = resourceRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("Buscar recursos públicos - Returns public resources")
    void testFindByIsPublicTrue_ReturnsPublicResources() {
        Resource publicResource = TestDataFactory.createPublicResource();
        publicResource.setId(null);
        resourceRepository.save(publicResource);

        List<Resource> publicResources = resourceRepository.findByIsPublicTrue();

        assertThat(publicResources).isNotEmpty();
        assertThat(publicResources).allMatch(Resource::getIsPublic);
    }


    @Test
    @DisplayName("Eliminar recurso - Success")
    void testDeleteById_ExistingId_DeletesResource() {
        Resource resource = TestDataFactory.createValidResource();
        resource.setId(null);
        Resource saved = resourceRepository.save(resource);

        resourceRepository.deleteById(saved.getId());

        Optional<Resource> found = resourceRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}