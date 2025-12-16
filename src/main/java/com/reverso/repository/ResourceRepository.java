package com.reverso.repository;

import com.reverso.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    List<Resource> findByType(String type);
    List<Resource> findByIsPublicTrue();
}